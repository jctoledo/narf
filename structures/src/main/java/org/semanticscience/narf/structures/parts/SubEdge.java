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

package org.semanticscience.narf.structures.parts;

/**
 * Represent the faces (subedges) described by the Leontis-Westhof nomenclature.
 * 
 * @author Jose Cruz-Toledo
 * @author William Greenwood
 * @version %I%, %G%
 * @since 1.6
 */
public class SubEdge {
	/**
	 * The subedge identifer
	 */
	private String subEdgeLabel;
	/**
	 * The atom which is fully contained by the physical boundaries of the
	 * subedge
	 */
	private Atom firstAtom;
	/**
	 * (Optional) some subedges extend into multiple atoms
	 */
	private Atom secondAtom;

	/**
	 * Construct a subedge from a subedge identifier.
	 * 
	 * @param aSubEdgeLabel
	 *            subedge identifier
	 * @version 1.6
	 */
	public SubEdge(String aSubEdgeLabel) {
		subEdgeLabel = aSubEdgeLabel;
	}

	/**
	 * Construct a subedge with the subedge label and the first-atom label.
	 * 
	 * @param aSubEdgeLabel
	 *            subedge identifier
	 * @param aFirstAtomLabel
	 *            first atom identifier
	 * @version 1.6
	 */
	public SubEdge(String aSubEdgeLabel, String aFirstAtomLabel) {
		this(aSubEdgeLabel, new Atom(aFirstAtomLabel));
	}

	/**
	 * Construct a subedge by specifying the subedge label and the first-atom
	 * object
	 * 
	 * @param aSubEdgeLabel
	 *            subedge identifier
	 * @param aFirstAtom
	 *            first atom
	 * @version 1.6
	 */
	public SubEdge(String aSubEdgeLabel, Atom aFirstAtom) {
		this(aSubEdgeLabel, aFirstAtom, null);
	}

	/**
	 * Construct a subedge by specifying the subedge label, the first-atom label
	 * and the second-atom label
	 * 
	 * @param aSubEdgeLabel
	 *            subedge identifier
	 * @param aFirstAtomLabel
	 *            first atom identifier
	 * @param aSecondAtomLabel
	 *            second atom identifier
	 * @version 1.6
	 */
	public SubEdge(String aSubEdgeLabel, String aFirstAtomLabel,
			String aSecondAtomLabel) {
		this(aSubEdgeLabel, new Atom(aFirstAtomLabel), new Atom(
				aSecondAtomLabel));
	}

	/**
	 * Construct a subedge by specifying the subedge label, the first-atom
	 * object and the second-atom object
	 * 
	 * @param aSubEdgeLabel
	 *            subedge identifier
	 * @param aFirstAtom
	 *            first atom
	 * @param aSecondAtom
	 *            second atom
	 * @version 1.6
	 */
	public SubEdge(String aSubEdgeLabel, Atom aFirstAtom, Atom aSecondAtom) {
		subEdgeLabel = aSubEdgeLabel;
		firstAtom = aFirstAtom;
		secondAtom = aSecondAtom;
	}

	/**
	 * Get the subedge identifer.
	 * 
	 * @return the string representation of the subedge
	 * @version 1.6
	 */
	public String getSubEdgeLabel() {
		return subEdgeLabel;
	}

	/**
	 * Get the first atom which is fully contained by the physical boundaries of
	 * the subedge.
	 * 
	 * @return the first atom
	 * @version 1.6
	 */
	public Atom getFirstAtom() {
		if (!this.hasFirstAtom()) {
			throw new UnsupportedOperationException(
					"No first atom is present in this subedge.");
		}

		return firstAtom;
	}

	/**
	 * Get the second atom which is fully contained by the physical boundaries
	 * of the subedge.
	 * 
	 * @return the second atom
	 * @version 1.6
	 */
	public Atom getSecondAtom() {
		if (!this.hasSecondAtom()) {
			throw new UnsupportedOperationException(
					"No second atom is present in this subedge.");
		}

		return secondAtom;
	}

	/**
	 * Determine if the subedge contains information relating to the first atom.
	 * 
	 * @return <code>true</code> if the subedge has first atom information,
	 *         otherwise <code>false</code>
	 * @version 1.6
	 */
	public boolean hasFirstAtom() {
		return (firstAtom != null);
	}

	/**
	 * Determine if the subedge contains information relating to the second
	 * atom.
	 * 
	 * @return <code>true</code> if the subedge has first atom information,
	 *         otherwise <code>false</code>
	 * @version 1.6
	 */
	public boolean hasSecondAtom() {
		return (secondAtom != null);
	}

	@Override
	public String toString() {
		String returnMe = "SubEdge : " + this.getSubEdgeLabel();

		if (this.hasFirstAtom()) {
			returnMe += " on atom(s): " + " firstAtom:" + this.getFirstAtom();
		}

		if (this.hasSecondAtom()) {
			returnMe += "  " + "secondAtom: " + this.getSecondAtom();
		}

		return returnMe;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((firstAtom == null) ? 0 : firstAtom.hashCode());
		result = prime * result
				+ ((secondAtom == null) ? 0 : secondAtom.hashCode());
		result = prime * result
				+ ((subEdgeLabel == null) ? 0 : subEdgeLabel.hashCode());
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
		SubEdge other = (SubEdge) obj;
		if (firstAtom == null) {
			if (other.firstAtom != null)
				return false;
		} else if (!firstAtom.equals(other.firstAtom))
			return false;
		if (secondAtom == null) {
			if (other.secondAtom != null)
				return false;
		} else if (!secondAtom.equals(other.secondAtom))
			return false;
		if (subEdgeLabel == null) {
			if (other.subEdgeLabel != null)
				return false;
		} else if (!subEdgeLabel.equals(other.subEdgeLabel))
			return false;
		return true;
	}

}
