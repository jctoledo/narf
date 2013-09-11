/** 
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
package org.semanticscience.narf.structures.parts;

/**
 * Representation of an atom 
 * @author Jose Cruz-Toledo
 * @author William Greenwood
 * @version %I%, %G%
 * @since 1.6
 */
public class Atom {
	
	/**
	 * The atom identifier
	 */
	private String atomLabel;

	/**
	 * Construct an atom from an atom identifier
	 * @param anAtomLabel atom identifier
	 */
	public Atom(String anAtomLabel){
		atomLabel = anAtomLabel;
	}
	
	public Atom(){
		atomLabel = "N/A";
	}
	/**
	 * Get the atom identifier
	 * @return the atomLabel
	 */
	public String getAtomLabel() {
		return atomLabel;
	}

	@Override
	public String toString(){
		return "Atom : "+ this.getAtomLabel();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((atomLabel == null) ? 0 : atomLabel.hashCode());
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
		Atom other = (Atom) obj;
		if (atomLabel == null) {
			if (other.atomLabel != null)
				return false;
		} else if (!atomLabel.equals(other.atomLabel))
			return false;
		return true;
	}

}
