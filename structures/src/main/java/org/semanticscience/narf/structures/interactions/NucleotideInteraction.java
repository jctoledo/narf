/**
 * Copyright (c) 2011 Jose Cruz-Toledo and  William Greenwood
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
package org.semanticscience.narf.structures.interactions;

import org.semanticscience.narf.structures.parts.Nucleotide;

/**
 * An abstract class used to mirror interactions present in nucleic acid molecules.
 * @author Jose Cruz-Toledo
 * @author William Greenwood
 * @version %I%, %G%
 * @since 1.6
 */
public abstract class NucleotideInteraction {	
	
	/**
	 * The first nucleotide residue participating in the interaction. The first nucleotide
	 * is the nucleotide whose residue position comes first numerically in the polymer (chain) 
	 * or whose polymer (chain) identifier is alphanumerically before the second nucleotide.
	 */
	private Nucleotide firstNucleotide;
	
	/**
	 * The second nucleotide residue participating in the interaction. The second nucleotide
	 * is the nucleotide whose residue position comes first numerically in the polymer (chain) 
	 * or whose polymer (chain) identifier is alphanumerically before the second nucleotide.
	 */
	private Nucleotide secondNucleotide;

	/**
	 * Construct a new abstract interaction class.
	 * @param theFirstNucleotide	The first nucleotide residue participating in the interaction.
	 * @param theSecondNucleotide	The second nucleotide residue participating in the interaction.
	 * @since 1.6
	 */
	protected NucleotideInteraction(Nucleotide theFirstNucleotide, Nucleotide theSecondNucleotide){
		firstNucleotide = theFirstNucleotide;
		secondNucleotide = theSecondNucleotide;
	}
	
	/**
	 * The first nucleotide is the nucleotide whose residue position comes first numerically in the polymer (chain) 
	 * or whose polymer (chain) identifier is alphanumerically before the second nucleotide.
	 * @return 	the first nucleotide residue participating in the interaction.
	 * @since 1.6
	 */
	public Nucleotide getFirstNucleotide(){
		return firstNucleotide;
	}

	/**
	 * The second nucleotide is the nucleotide whose residue position comes first numerically in the polymer (chain) 
	 * or whose polymer (chain) identifier is alphanumerically before the second nucleotide.
	 * @return 	the second nucleotide residue participating in the interaction.
	 * @since 1.6
	 */
	public Nucleotide getSecondNucleotide(){
		return secondNucleotide;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((firstNucleotide == null) ? 0 : firstNucleotide.hashCode());
		result = prime
				* result
				+ ((secondNucleotide == null) ? 0 : secondNucleotide.hashCode());
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
		NucleotideInteraction other = (NucleotideInteraction) obj;
		if (firstNucleotide == null) {
			if (other.firstNucleotide != null)
				return false;
		} else if (!firstNucleotide.equals(other.firstNucleotide))
			return false;
		if (secondNucleotide == null) {
			if (other.secondNucleotide != null)
				return false;
		} else if (!secondNucleotide.equals(other.secondNucleotide))
			return false;
		return true;
	}

	
}
