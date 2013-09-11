/*
 * Copyright (c) 2011 Jose Cruz-Toledo and William Greenwood
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
 * An object used to represent a base pairing interaction between the nucleobases of two nucleotides 
 * in a nucleic acid.
 * @author Jose Cruz-Toledo
 * @author William Greenwood
 * @version %I%, %G%
 * @since 1.6
 * @see NucleotideInteraction
 */
public class BaseStack extends NucleotideInteraction{

	/**
	 * The directionality of the normal vectors on the interacting nucleobases of the 
	 * participating nucleotide residues in the base stacking (upward, inward, etc.)
	 */
	private String stackOrientation;
	
	/**
	 * The adjacency of the two nucleotides that are involved in the base stacking interaction.
	 */
	private boolean stackAdjacency;
	
	/**
	 * Construct a new base pairing interactions between two nucleotide residues. This constructor does
	 * not infer the orientation of the base stacking interaction.
	 * @param aFirstNucleotide	The first nucleotide residue participating in the base stack.
	 * @param aSecondNucleotide	The second nucleotide residue participating in the base stack.
	 * @param aStackAdjacency 	The adjacency of the two nucleotides in the base stacking interaction.
	 * @since 1.6
	 */
	public BaseStack(Nucleotide aFirstNucleotide, Nucleotide aSecondNucleotide, boolean aStackAdjacency) {
		this(aFirstNucleotide, aSecondNucleotide, aStackAdjacency, null);
	}
	
	/**
	 * Construct a new base pairing interactions between two nucleotide residues.
	 * @param aFirstNucleotide	The first nucleotide residue participating in the base stack.
	 * @param aSecondNucleotide	The second nucleotide residue participating in the base stack.
	 * @param aStackAdjacency	The adjacency of the two nucleotides in the base stacking interaction.
	 * @param aStackOrientation	The directionality of the nucleobases of the two nucleotides in the base stacking interaction.
	 * @since 1.6
	 */
	public BaseStack(Nucleotide aFirstNucleotide, Nucleotide aSecondNucleotide, boolean aStackAdjacency, String aStackOrientation) {
		super(aFirstNucleotide, aSecondNucleotide);
		stackAdjacency = aStackAdjacency;
		stackOrientation = aStackOrientation;
	}

	/**
	 * Determine whether the two nucleotides that are involved in the base stacking interaction are adjacent.
	 * @return whether the two nucleotides in the base stacking are adjacent.
	 * @since 1.6
	 */
	public boolean isAdjacentStack(){
		return stackAdjacency;
	}
	
	/**
	 * Get the directionality of the normal vectors on the interacting nucleobases of the 
	 * participating nucleotide residues in the base stacking.
	 * @return the directionality of the interacting nucleobases of the base stacking
	 * @since 1.6
	 */
	public String getStackOrientation() {		
		return stackOrientation;
	}
	
	@Override
	public String toString() {
		return "Nucleotide base stack: ("+this.getFirstNucleotide().getResidueIdentifier()+
			"-"+this.getSecondNucleotide().getResidueIdentifier()+")["+
			this.getStackOrientation()+"] residue positions: "+
			this.getFirstNucleotide().getResiduePosition()+"-"+
			+this.getSecondNucleotide().getResiduePosition()+"\n";

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (stackAdjacency ? 1231 : 1237);
		result = prime
				* result
				+ ((stackOrientation == null) ? 0 : stackOrientation.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseStack other = (BaseStack) obj;
		if (stackAdjacency != other.stackAdjacency)
			return false;
		if (stackOrientation == null) {
			if (other.stackOrientation != null)
				return false;
		} else if (!stackOrientation.equals(other.stackOrientation))
			return false;
		return true;
	}



}
