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

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.semanticscience.narf.structures.interactions.BasePair;
import org.semanticscience.narf.structures.interactions.NucleotideInteraction;
import org.semanticscience.narf.structures.interactions.PhosphodiesterBond;
import org.semanticscience.narf.structures.lib.DotBracketNotationConstants;
import org.semanticscience.narf.structures.lib.exceptions.InvalidDotBracketNotationException;
import org.semanticscience.narf.structures.lib.exceptions.NonConsecutiveNucleotideException;

/**
 * Representation of dot bracket notation (DBN): a commonly used format to
 * describe nucleic acid secondary structure. Pseudoknots are accepted in this
 * class by using the [...] and {...} bracket pairs currently.
 * 
 * @author "William Greenwood"
 * @author "Jose Cruz-Toledo"
 * @version %I%, %G%
 * @since 1.6
 */
public class DotBracketNotation {

	/**
	 * The sequence that corresponds to the dot bracket notation
	 */
	private Sequence sequence;

	/**
	 * A mapping of nucleotides found in the sequence to the corresponding
	 * character in the DBN.
	 */
	private Map<Nucleotide, Character> dotBracketMap;

	/**
	 * A set of interactions that are implicit in the DBN (base pairing and
	 * phosphodiester interactions).
	 */
	private Set<NucleotideInteraction> interactions;

	/**
	 * The string representation of the DBN.
	 */
	private String dotBracketNotation;

	/**
	 * Construct a dot bracket notation from a string representation. This class
	 * can process dot bracket notations that include pseudoknots.
	 * 
	 * @param aSequence
	 *            the corresponding sequence of the dot bracket notation
	 * @param aDotBracketNotation
	 *            the string representation of the dot bracket notation
	 * @throws InvalidDotBracketNotationException
	 *             if the opening and closing characters of the dot bracket
	 *             notation do not correspond properly
	 * @throws InvalidPhosphodiesterBondException
	 * @since 1.6
	 */
	public DotBracketNotation(Sequence aSequence, String aDotBracketNotation)
			throws InvalidDotBracketNotationException {
		if (aSequence.getLength() != aDotBracketNotation.length()) {
			throw new InvalidDotBracketNotationException(
					"Sequence and dot bracket notation are not the same length.");
		}

		if (!this.isNested(aSequence, aDotBracketNotation)) {
			throw new InvalidDotBracketNotationException(
					"The dot bracket notation contains non properly nested characters");
		}

		this.sequence = aSequence;
		this.dotBracketNotation = aDotBracketNotation;
		this.interactions = new LinkedHashSet<NucleotideInteraction>();
		this.dotBracketMap = new LinkedHashMap<Nucleotide, Character>();

		Iterator<Nucleotide> itr = this.sequence.iterator();

		LinkedHashMap<Character, Stack<Nucleotide>> openingStacks = new LinkedHashMap<Character, Stack<Nucleotide>>();

		// iterate over the sequence, ensure that dbn is nested and create base
		// pairs and add them to the interactions set
		int i = 0;
		while (itr.hasNext()) {

			Nucleotide currentNucleotide = itr.next();
			char dotBracket = this.dotBracketNotation.charAt(i++);

			dotBracketMap.put(currentNucleotide, dotBracket);

			if (this.isOpeningCharacter(dotBracket)) {
				if (!openingStacks.containsKey(dotBracket)) {
					openingStacks.put(dotBracket, new Stack<Nucleotide>());
				}

				openingStacks.get(dotBracket).push(currentNucleotide);
			} else if (this.isClosingCharacter(dotBracket)) {
				Stack<Nucleotide> openingStack = openingStacks
						.get(DotBracketNotationConstants.CLOSING_CHARACTERS
								.get(dotBracket));

				BasePair bp = new BasePair(openingStack.pop(),
						currentNucleotide);
				this.interactions.add(bp);
			} else {
				continue;
			}

		}

		for (int j = 0; j < this.sequence.getLength() - 1; j++) {
			Nucleotide curr = this.sequence.getNucleotideAtPosition(j + 1);
			Nucleotide next = this.sequence.getNucleotideAtPosition(j + 2);
			// check if the two nucleotides have a difference of one in their
			// positions
			int check = next.getResiduePosition() - curr.getResiduePosition();
			if (check == 1) {
				PhosphodiesterBond pdb = null;
				try {
					pdb = new PhosphodiesterBond(curr, next);
				} catch (NonConsecutiveNucleotideException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				this.interactions.add(pdb);

			}
		}

	}

	/**
	 * Check if the dbn is properly nested. This method keeps track of each
	 * nucleotide and its corresponding bracket notation. Returns false if the
	 * DBN has unbalanced brackets or letters
	 * 
	 * @param aSequence
	 *            the sequence to be checked
	 * @param aDotBracketNotation
	 *            the dbn to be checked
	 */
	private boolean isNested(Sequence aSequence, String aDotBracketNotation) {
		Iterator<Nucleotide> itr = aSequence.iterator();

		LinkedHashMap<Character, Stack<Nucleotide>> openingStacks = new LinkedHashMap<Character, Stack<Nucleotide>>();

		int i = 0;
		while (itr.hasNext()) {
			Nucleotide currentNucleotide = itr.next();
			char dotBracket = aDotBracketNotation.charAt(i++);

			if (this.isOpeningCharacter(dotBracket)) {
				if (!openingStacks.containsKey(dotBracket)) {
					openingStacks.put(dotBracket, new Stack<Nucleotide>());
				}

				openingStacks.get(dotBracket).push(currentNucleotide);
			} else if (this.isClosingCharacter(dotBracket)) {
				Stack<Nucleotide> openingStack = openingStacks
						.get(DotBracketNotationConstants.CLOSING_CHARACTERS
								.get(dotBracket));

				if (openingStack.isEmpty()) {
					return false;
				}

				openingStack.pop();
			} else {
				continue;
			}

		}

		return true;
	}

	/**
	 * Check to determine if the character is an opening character.
	 * 
	 * @param c
	 *            the character to be checked
	 * @return true if the character is an Opening Character
	 */
	private boolean isOpeningCharacter(char c) {
		return DotBracketNotationConstants.OPENING_CHARACTERS.containsKey(c);
	}

	/**
	 * Check to determine if the character is a closing character.
	 * 
	 * @param c
	 *            the character to be checked
	 * @return true if the parameter is an Closing Character
	 */
	private boolean isClosingCharacter(char c) {
		return DotBracketNotationConstants.CLOSING_CHARACTERS.containsKey(c);
	}

	/**
	 * Get the sequence that corresponds to the dot bracket notation.
	 * 
	 * @return the sequence object of the dot bracket notation
	 * @since 1.6
	 */
	public Sequence getSequence() {
		return sequence;
	}

	/**
	 * Get the character of the dot bracket notation of a given nucleotide.
	 * 
	 * @param aNucleotide
	 *            the nucleotide
	 * @return the character corresponding to the nucleotide, or
	 *         <code>null</code> if there is no nucleotide in the sequence
	 * @since 1.6
	 */
	public Character getDotBracketCharacterForNucleotide(Nucleotide aNucleotide) {
		return dotBracketMap.get(aNucleotide);
	}

	/**
	 * Get the character of the dot bracket notation at a given nucleotide
	 * residue position
	 * 
	 * @param aPosition
	 *            the nucleotide residue position
	 * @return the character corresponding at the nucleotide residue position,
	 *         or <code>null</code> if there is no nucleotide at this position
	 * @since 1.6
	 */
	public Character getDotBracketCharacterAtPosition(int aPosition) {
		return this.getDotBracketCharacterForNucleotide(sequence
				.getNucleotideAtPosition(aPosition));
	}

	/**
	 * Get the character of the dot bracket notation of a given nucleotide.
	 * 
	 * @param aNucleotide
	 *            the nucleotide
	 * @return the character corresponding to the nucleotide, or
	 *         <code>null</code> if there is no nucleotide in the sequence
	 * @since 1.6
	 */
	public Nucleotide getNucleotideAtPosition(int aPosition) {
		return sequence.getNucleotideAtPosition(aPosition);
	}

	/**
	 * Get all the interactions (base pairing and phosphodiester interactions)
	 * implied by the dot bracket notation.
	 * 
	 * @return a set of interactions present in the dot bracket notation
	 * @since 1.6
	 */
	public Set<NucleotideInteraction> getInteractions() {
		return Collections.unmodifiableSet(interactions);
	}

	/**
	 * Get the string representation of the dot bracket notation.
	 * 
	 * @return a string of the dot bracket notation
	 * @since 1.6
	 */
	public String getDotBracketNotation() {
		return dotBracketNotation;
	}

	@Override
	public String toString() {
		return sequence + "\n" + dotBracketNotation + "\n";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dotBracketMap == null) ? 0 : dotBracketMap.hashCode());
		result = prime
				* result
				+ ((dotBracketNotation == null) ? 0 : dotBracketNotation
						.hashCode());
		result = prime * result
				+ ((interactions == null) ? 0 : interactions.hashCode());
		result = prime * result
				+ ((sequence == null) ? 0 : sequence.hashCode());
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
		DotBracketNotation other = (DotBracketNotation) obj;
		if (dotBracketMap == null) {
			if (other.dotBracketMap != null)
				return false;
		} else if (!dotBracketMap.equals(other.dotBracketMap))
			return false;
		if (dotBracketNotation == null) {
			if (other.dotBracketNotation != null)
				return false;
		} else if (!dotBracketNotation.equals(other.dotBracketNotation))
			return false;
		if (interactions == null) {
			if (other.interactions != null)
				return false;
		} else if (!interactions.equals(other.interactions))
			return false;
		if (sequence == null) {
			if (other.sequence != null)
				return false;
		} else if (!sequence.equals(other.sequence))
			return false;
		return true;
	}
}
