/**
 * Copyright (c) 2011 William Greenwood and Jose Cruz-Toledo
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.semanticscience.narf.structures.parts;

import java.text.CharacterIterator;
import java.text.DecimalFormat;
import java.text.StringCharacterIterator;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.semanticscience.narf.structures.lib.exceptions.InvalidResidueException;
import org.semanticscience.narf.structures.lib.exceptions.InvalidSequenceException;
import org.semanticscience.narf.structures.lib.exceptions.NonConsecutiveNucleotideException;

/**
 * Representation of a nucleotide sequence
 * 
 * @author "Jose Cruz-Toledo"
 * @author "William Greenwood"
 * @since 1.6
 */
public class Sequence implements Iterable<Nucleotide> {

	/**
	 * Map of nucleotide residue position to the nucleotide in the sequence.
	 */
	private SortedMap<Integer, Nucleotide> nucleotides;

	/**
	 * Definition line for a FASTA file.
	 */
	private String definitionLine = null;

	/**
	 * Construct a sequence from a string
	 * 
	 * @param aSequence
	 *            a nucleotide sequence
	 * @throws InvalidSequenceException
	 *             if a given sequence has invalid characters
	 * @throws InvalidResidueException
	 * @since 1.6
	 */
	public Sequence(String aSequence) throws InvalidSequenceException,
			InvalidResidueException {
		this(aSequence, "");
	}

	/**
	 * Construct a sequence with a definition line from a string
	 * 
	 * @param aSequence
	 *            a nucleotide sequence
	 * @param aDefinitionLine
	 *            the definition line of a FASTA file
	 * @param aSequence
	 *            a nucleotide sequence
	 * @throws InvalidSequenceException
	 *             if a given sequence has invalid characters
	 * @throws InvalidResidueException
	 * @since 1.6
	 */
	public Sequence(String aSequence, String aDefinitionLine)
			throws InvalidSequenceException, InvalidResidueException {
		this(aSequence, 1, aDefinitionLine);
	}

	/**
	 * Construct a sequence starting from a given residue position.
	 * 
	 * @param aSequence
	 *            a sequence string
	 * @param aStartingPosition
	 *            the desired position of the first nucleotide
	 * @throws InvalidSequenceException
	 *             if a given sequence has invalid characters
	 * @throws InvalidResidueException
	 * @since 1.6
	 */
	public Sequence(String aSequence, int aStartingPosition)
			throws InvalidSequenceException, InvalidResidueException {
		this(aSequence, aStartingPosition, "");
	}

	/**
	 * Construct a sequence starting from a given residue position.
	 * 
	 * @param aSequence
	 *            a sequence string
	 * @param aStartingPosition
	 *            the desired position of the first nucleotide
	 * @param aDefinitionLine
	 *            a definition line of a FASTA file
	 * @throws InvalidSequenceException
	 *             if a given sequence has invalid characters
	 * @throws InvalidResidueException
	 *             if any of the nucleotides has an invalid residue
	 * @since 1.6
	 */
	public Sequence(String aSequence, int aStartingPosition,
			String aDefinitionLine) throws InvalidSequenceException,
			InvalidResidueException {
		this.definitionLine = aDefinitionLine;
		this.nucleotides = makeNucleotides(aSequence, aStartingPosition);
	}

	/**
	 * Construct a sequence from a set of nucleotides.
	 * 
	 * @param aNucleotideSet
	 *            a set of nucleotides to construct a sequence
	 * 
	 * @throws NonConsecutiveNucleotideException
	 *             if the nucleotide set contains non-consecutive nucleotides
	 * @since 1.6
	 */
	public Sequence(Set<Nucleotide> aNucleotideSet)
			throws NonConsecutiveNucleotideException {
		this(aNucleotideSet, "");
	}

	/**
	 * Construct a sequence from a set of nucleotides.
	 * 
	 * @param aNucleotideSet
	 *            a set of nucleotides to construct a sequence
	 * @param aDefinitionLine
	 *            a definition line of a FASTA file
	 * @throws NonConsecutiveNucleotideException
	 *             if the nucleotide set contains non-consecutive nucleotides
	 * @since 1.6
	 */
	public Sequence(Set<Nucleotide> aNucleotideSet, String aDefinitionLine)
			throws NonConsecutiveNucleotideException {
		this.definitionLine = aDefinitionLine;
		this.nucleotides = new TreeMap<Integer, Nucleotide>();

		// add the nucleotides one at a time to the nucleotides instance
		// variable
		Iterator<Nucleotide> itr = aNucleotideSet.iterator();
		while (itr.hasNext()) {
			this.addNucleotide(itr.next());
		}

		// now check that nucleotides only has consecutive nucleotides
		Iterator<Integer> nucItr = this.nucleotides.keySet().iterator();
		Integer previousPos = this.nucleotides.firstKey() - 1;
		while (nucItr.hasNext()) {
			Integer currentPos = nucItr.next();

			if ((Math.abs(currentPos - previousPos) != 1)) {
				throw new NonConsecutiveNucleotideException(
						"A non consecutive nucleotide was found");
			}
			previousPos = currentPos;
		}
	}
	
	/**
	 * The GC Content for the nucleotide sequence
	 * @return the gc constant value
	 */
	public double getGCContent(){
		double g = StringUtils.countMatches(this.getSequenceString(), "G")*1.0;
		double c = StringUtils.countMatches(this.getSequenceString(), "C")*1.0;
		double t = StringUtils.countMatches(this.getSequenceString(), "T")*1.0;
		double u = StringUtils.countMatches(this.getSequenceString(), "U")*1.0;
		double a = StringUtils.countMatches(this.getSequenceString(), "A")*1.0;
		
		if(u ==0){//if dna
			double q = a+t+g+c;
			double s = t/q ;
			return s*100;
		}else if(t== 0){//if rna
			return ((g+c)/(a+g+u+c))*100;
		}else{
			return ((g+c)/(a+g+u+t+c))*100;
		}
	}

	public int getGuanineCount(){
		return StringUtils.countMatches(this.getSequenceString(), "G");
	}
	public int getCytosineCount(){
		return StringUtils.countMatches(this.getSequenceString(), "C");
	}
	public int getAdenineCount(){
		return StringUtils.countMatches(this.getSequenceString(), "A");
	}
	public int getUracilCount(){
		return StringUtils.countMatches(this.getSequenceString(), "U");
	}
	public int getThymineCount(){
		return StringUtils.countMatches(this.getSequenceString(), "T");
	}
	/**
	 * The length of the nucleotide sequence.
	 * 
	 * @return the length of the sequence
	 */
	public int getLength() {
		return this.getNucleotides().size();
	}

	/**
	 * Determine if the nucleotide that is at a given position.
	 * 
	 * @param aPosition
	 *            the nucleotide residue position
	 * @return <code>true</code> if the nucleotide is at the position, otherwise
	 *         <code>false</code>
	 */
	public boolean containsNucleotideAtPosition(int aPosition) {
		return (nucleotides.containsKey(aPosition));
	}

	/**
	 * Get the nucleotide that is at a given position.
	 * 
	 * @param aPosition
	 *            the nucleotide residue position
	 * @return the nucleotide at the given position or <code>null</code> if
	 *         there is no nucleotide at that position
	 * @since 1.6
	 */
	public Nucleotide getNucleotideAtPosition(int aPosition) {
		return nucleotides.get(aPosition);
	}

	/**
	 * Determine if all the characters representing in a given sequence string
	 * is valid.
	 * 
	 * @param aSequence
	 *            a string representation of a sequence
	 * @return <code>true</code> if only accepted characters are present,
	 *         otherwise <code>false</code>
	 * @since 1.6
	 */
	private boolean checkStringSequence(String aSequence) {
		Pattern p = Pattern.compile("[ACGTURYKMSWBDHVNX-]+");
		Matcher m = p.matcher(aSequence);
		if (m.matches()) {
			return true;
		}
		return false;
	}

	/**
	 * Create the set of nucleotides from a nucleic acid sequence string. A
	 * mapping is constructed of the residue position of the nucleotides to the
	 * nucleotides. The method enables the sequence to commence at an arbitrary
	 * sequence position.
	 * 
	 * @param aSequence
	 *            sequence string
	 * @param aStartingPosition
	 *            the residue position of the first nucleotide
	 * @return a mapping of the nucleotide residue positions to their respective
	 *         nucleotide residue position
	 * @throws InvalidSequenceException
	 *             if any of the characters in the sequence string is not a
	 *             valid residue identifier
	 * @throws InvalidResidueException
	 *             if the created nucleotide contains a residue exception
	 * @since 1.6
	 */
	SortedMap<Integer, Nucleotide> makeNucleotides(String aSequence,
			int aStartingPosition) throws InvalidSequenceException,
			InvalidResidueException {
		//first make the sequence all upper case
		aSequence = aSequence.toUpperCase();
		if ((aSequence == null) || (aSequence.length() == 0)) {
			throw new InvalidSequenceException(
					"The provided sequence has no characters.");
		}

		if (!this.checkStringSequence(aSequence)) {
			throw new InvalidSequenceException(
					"The inputted sequence has a invalid sequence character.\n"
							+ aSequence + "\n");
		}

		SortedMap<Integer, Nucleotide> returnMe = new TreeMap<Integer, Nucleotide>();
		int currentPosition = aStartingPosition;
		CharacterIterator it = new StringCharacterIterator(aSequence);

		for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
			returnMe.put(currentPosition, new Nucleotide(currentPosition,
					String.valueOf(ch)));
			currentPosition++;
		}
		return returnMe;
	}

	/**
	 * Adds a nucleotide to the sequence at the residue position within the
	 * nucleotide.
	 * 
	 * @param aNucleotide
	 *            the nucleotide to be added to the sequence
	 * @since 1.6
	 */
	private boolean addNucleotide(Nucleotide aNucleotide) {
		// check if currently the residue position of the nucleotide is null
		if (nucleotides.containsKey(aNucleotide.getResiduePosition())
				&& (nucleotides.get(aNucleotide.getResiduePosition()) == null)) {
			nucleotides.put(aNucleotide.getResiduePosition(), aNucleotide);
			return true;
		}
		return (nucleotides.put(aNucleotide.getResiduePosition(), aNucleotide) != null);
	}

	/**
	 * Get a FASTA string representation of the sequence which includes the
	 * definition line and the sequence
	 * 
	 * @return a FASTA string of the sequence
	 * @since 1.6
	 */
	public String getFastaString() {
		String returnMe = "";
		if ((this.getDefinitionLine().length() == 0) || (this.getDefinitionLine() == null)) {
			returnMe += ">No definition line available\n";
		} else {
			//check if this definition line starts with ">"
			if(!this.getDefinitionLine().startsWith(">")){
				returnMe += ">" + this.getDefinitionLine() + "\n";
			}else{
				returnMe +=  this.getDefinitionLine() + "\n";
			}
		}

		returnMe += this.getSequenceString() + "\n";

		return returnMe;
	}

	/**
	 * Get the string representation of the nucleotides in the sequence.
	 * 
	 * @return a string representation of the sequence
	 * @since 1.6
	 */
	public String getSequenceString() {
		String returnMe = "";

		Iterator<Nucleotide> itr = this.getNucleotides().iterator();
		while (itr.hasNext()) {
			Nucleotide aNuc = itr.next();
			returnMe += aNuc.getResidueIdentifier().toUpperCase();
		}
		return returnMe;
	}

	/**
	 * Get the definition line for a FASTA file.
	 * 
	 * @return the definition line
	 * @since 1.6
	 */
	public String getDefinitionLine() {
		return definitionLine;
	}
	//TODO: Compute a random sequence given a GC content and a sequence type
	/**
	 * Create a random Sequence given a GC content value
	 * @param aGCContent the value of the GC content that you want for your output
	 * @param sequenceType accepted values RNA or DNA
	 * @param the length of the output sequence
	 * @return a sequence of random nucleotides of a given GC content
	 */
	public static Sequence createRandomSequence(Double aGCContent, String sequenceType, int lenght){
		
		return null;
	}

	//TODO:check ME!!!
	public void setDefinitionLine(String aDefLine){
		//check if parameter is empty
		if((aDefLine.length() != 0) && (aDefLine != null)){
			//make sure that it starts with a ">"
			if(aDefLine.substring(0,1) != ">"){
				this.definitionLine = ">"+aDefLine;
			}else{
				this.definitionLine = aDefLine;
			}
		}else{
			this.definitionLine = ">no definition available";
		}
	}
	
	/**
	 * Get a subsequence of this sequence. Specify the start and end nucleotides
	 * of the residues you wish to extract. The returned subsequence includes
	 * the start and end positions
	 * 
	 * @param aStartNucleotide
	 *            the nucleotide at the first position
	 * @param anEndNucleotide
	 *            the nucleotide at the end position
	 * @return a sequence from <code>aStartNucleotide</code> to
	 *         <code>anEndNucleotide</code>
	 * @throws NonConsecutiveNucleotideException
	 *             if any of the nucleotides in the subsequence are not in
	 *             consecutive order
	 * @since 1.6
	 */
	public Sequence getSubsequence(Nucleotide aStartNucleotide,
			Nucleotide anEndNucleotide) throws NonConsecutiveNucleotideException {
		if (!this.containsNucleotide(aStartNucleotide)
				|| !this.containsNucleotide(anEndNucleotide)) {
			return null;
		}
		
		if(aStartNucleotide.compareTo(anEndNucleotide) == -1 ){
			return null;
		}

		boolean addToSubSequence = false;
		Set<Nucleotide> returnMe = new LinkedHashSet<Nucleotide>();

		for (Nucleotide nucleotide : this.getNucleotides()) {
			if (addToSubSequence && nucleotide.equals(anEndNucleotide)) {
				returnMe.add(nucleotide);
				addToSubSequence = false;
				break;
			} else if (addToSubSequence) {
				returnMe.add(nucleotide);
			} else if (nucleotide.equals(aStartNucleotide)) {
				returnMe.add(nucleotide);
				addToSubSequence = true;
			}

		}

		if (addToSubSequence) {
			return null;
		}

		return new Sequence(returnMe);
	}

	/**
	 * Get all the nucleotides in the sequence in order by their residue
	 * position
	 * 
	 * @return a collection of all the nucleotides in the sequence
	 * @since 1.6
	 */
	public Collection<Nucleotide> getNucleotides() {
		return Collections.unmodifiableCollection(nucleotides.values());
	}

	@Override
	public String toString() {
		return this.getFastaString();
	}

	/**
	 * Get the last nucleotide in the sequence.
	 * 
	 * @return the first nucleotide in the sequence
	 * @since 1.6
	 */
	public Nucleotide getFirstNucleotide() {
		return nucleotides.get(nucleotides.firstKey());
	}

	/**
	 * Get the last nucleotide in the sequence.
	 * 
	 * @return the last nucleotide in the sequence
	 * @since 1.6
	 */
	public Nucleotide getLastNucleotide() {
		return nucleotides.get(nucleotides.lastKey());
	}

	public Iterator<Nucleotide> iterator() {
		return nucleotides.values().iterator();
	}

	/**
	 * Determine if the sequence contains the given nucleotide.
	 * 
	 * @param nucleotide
	 *            a nucleotide used for searching
	 * @return <code>true</code>e if the nucleotide is in the sequence,
	 *         otherwise <code>false</code>
	 * @since 1.6
	 */
	public boolean containsNucleotide(Nucleotide nucleotide) {
		if (nucleotides.containsValue(nucleotide)) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((definitionLine == null) ? 0 : definitionLine.hashCode());
		result = prime * result
				+ ((nucleotides == null) ? 0 : nucleotides.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sequence other = (Sequence) obj;
		if (definitionLine == null) {
			if (other.definitionLine != null)
				return false;
		} else if (!definitionLine.equals(other.definitionLine))
			return false;
		if (nucleotides == null) {
			if (other.nucleotides != null)
				return false;
		} else if (!nucleotides.equals(other.nucleotides))
			return false;
		return true;
	}

}
