/**
 * Copyright (c) 2011 William Greenwood and Jose Miguel Cruz Toledo
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

import org.semanticscience.narf.structures.lib.exceptions.InvalidResidueException;

/**
 * A representation of a residue that forms part of a molecular structure,
 * either as part of a sequence or a structure
 * 
 * @author Jose Cruz-Toledo
 * 
 */
public abstract class Residue implements Comparable<Residue>{

	/**
	 * The PDB chemical identifier for the molecule. Eg: A-> Adenine, G->
	 * Guanine
	 */
	private final String residueIdentifier;

	/**
	 * Residue position in the chain of the nucleic acid.
	 */
	private final int residuePosition;

	protected Residue(int aResiduePosition, String aResidueIdentifier)
			throws InvalidResidueException {
		// check for length of residue identifier
		if (aResiduePosition < 0) {
			throw new InvalidResidueException(
					"The provided residue position : " + aResiduePosition
							+ " may not be negative.");
		} else if (aResidueIdentifier.length() < 1
				|| aResidueIdentifier.length() > 3
				|| aResidueIdentifier == null) {
			throw new InvalidResidueException(
					"The provided residue identifier is invalid!");
		}
		residueIdentifier = aResidueIdentifier;
		residuePosition = aResiduePosition;
	}

	/**
	 * Get the three letter chemical identifier of the nucleotide residue
	 * 
	 * @return the three letter identifier of the nucleotide residue
	 * @since 1.6
	 */
	public String getResidueIdentifier() {
		return residueIdentifier.toUpperCase();
	}

	/**
	 * Get the residue position of the nucleotide
	 * 
	 * @return the residue position integer
	 * @since 1.6
	 */
	public int getResiduePosition() {
		return residuePosition;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((residueIdentifier == null) ? 0 : residueIdentifier
						.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Residue other = (Residue) obj;
		if (residueIdentifier == null) {
			if (other.residueIdentifier != null)
				return false;
		} else if (!residueIdentifier.equals(other.residueIdentifier))
			return false;
		return true;
	}
	
	
	public int compareTo(Residue o) {
		if( o.getResiduePosition() > this.getResiduePosition()){
			return -1;
		}else if ( o.getResiduePosition() < this.getResiduePosition()){
			return 1;
		}
		return 0;
	}


}
