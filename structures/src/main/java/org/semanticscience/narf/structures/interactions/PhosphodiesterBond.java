/**
 * Copyright (c) 2011 William Greenwood
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

import org.semanticscience.narf.structures.lib.exceptions.NonConsecutiveNucleotideException;
import org.semanticscience.narf.structures.parts.Nucleotide;

/**
 * A class for describing the backbone interaction between two annexed
 * nucleotides: the phosphodiester bond.
 * 
 * @author "William Greenwood"
 * @version %I%, %G%
 * @since 1.6
 * @see NucleotideInteraction
 */

public class PhosphodiesterBond extends NucleotideInteraction {

	/**
	 * Construct a new phosphodiester bond interaction.
	 * 
	 * @param theFirstNucleotide
	 *            The first nucleotide residue participating in the interaction.
	 * @param theSecondNucleotide
	 *            The second nucleotide residue participating in the
	 *            interaction.
	 * @throws NonConsecutiveNucleotideException 
	 * @throws InvalidPhosphodiesterBondException
	 * @since 1.6
	 */
	public PhosphodiesterBond(Nucleotide theFirstNucleotide,
			Nucleotide theSecondNucleotide) throws NonConsecutiveNucleotideException
			{
		super(theFirstNucleotide, theSecondNucleotide);
		//check that the two residues are adjacent
		int d =  theSecondNucleotide.getResiduePosition() - theFirstNucleotide.getResiduePosition();
		if(Math.abs(d) != 1){
			throw new NonConsecutiveNucleotideException("A non consecutive nucleotide was found in creating a phosphodiester object");
		}
	}
	
	/**
	 * 
	 * @return an integer representation of this backbone class. Currently there is only 1 so 1 is always returned
	 */
	public Integer getNormalizedBackBone(){
		return 100;
	}

	@Override
	public String toString() {
		String returnMe = "Phosphodiester Bond: ("
				+ this.getFirstNucleotide().getResidueIdentifier() + "-"
				+ this.getSecondNucleotide().getResidueIdentifier()
				+ ") residue positions: "
				+ this.getFirstNucleotide().getResiduePosition() + "-"
				+ this.getSecondNucleotide().getResiduePosition() +"\n";

		return returnMe;
	}

}
