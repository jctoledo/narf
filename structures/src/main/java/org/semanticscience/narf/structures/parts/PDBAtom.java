/**
 * Copyright (c) 2011 by Jose Cruz-Toledo & William Greenwood
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
 * Representation of a PDB atom for example:
 * 
 * @author Jose Cruz-Toledo
 * @author William Greenwood
 * @version %I%, %G%
 * @since 1.6
 */
public class PDBAtom extends Atom {
	/**
	 * The atom id
	 */
	public int atomId;

	/**
	 * The atom position label eg: O5' or O5p
	 */
	public String positionLabel;

	/**
	 * the residue label
	 */
	public String residueLabel;

	/**
	 * The chain id
	 */
	public String chainId;

	/**
	 * the chain position
	 */
	public int chainPos;

	/**
	 * the x coordinate
	 */
	public double xCoord;

	/**
	 * the y coordinate
	 */
	public double yCoord;

	/**
	 * the z coordinate
	 */
	public double zCoord;

	/**
	 * The occupancy
	 */
	public double occupancy;

	/**
	 * The B-factor
	 */
	public double bfactor;

	/**
	 * The atom label
	 */
	public String atomLabel;

	public PDBAtom() {
		super();
		atomId = -1;
		positionLabel = "N/A";
		residueLabel = "N/A";
		chainId = "N/A";
		chainPos = -1;
		xCoord = -1;
		yCoord = -1;
		zCoord = -1;
		occupancy = -1;
		bfactor = -1;
		atomLabel = "N/A";
	}

	public PDBAtom(int anAtomId, String aPosLabel, String aResLabel,
			String aChainId, int aChainPos, double anXCoord, double aYCoord,
			double aZCoord, double aBFactor, double anOccupancy,
			String anAtomLabel) {
		this();
		atomId = anAtomId;
		positionLabel = aPosLabel;
		residueLabel = aResLabel;
		chainId = aChainId;
		chainPos = aChainPos;
		xCoord = anXCoord;
		yCoord = aYCoord;
		zCoord = aZCoord;
		occupancy = anOccupancy;
		bfactor = aBFactor;
		atomLabel = anAtomLabel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PDBAtom [atomId=" + atomId + ", positionLabel=" + positionLabel
				+ ", residueLabel=" + residueLabel + ", chainId=" + chainId
				+ ", chainPos=" + chainPos + ", xCoord=" + xCoord + ", yCoord="
				+ yCoord + ", zCoord=" + zCoord + ", occupancy=" + occupancy
				+ ", bfactor=" + bfactor + ", atomLabel=" + atomLabel + "]";
	}

	/**
	 * @return the atomId
	 */
	public int getAtomId() {
		return atomId;
	}

	/**
	 * @param atomId
	 *            the atomId to set
	 */
	public void setAtomId(int atomId) {
		this.atomId = atomId;
	}

	/**
	 * @return the positionLabel
	 */
	public String getPositionLabel() {
		return positionLabel;
	}

	/**
	 * @param positionLabel
	 *            the positionLabel to set
	 */
	public void setPositionLabel(String positionLabel) {
		this.positionLabel = positionLabel;
	}

	/**
	 * @return the residueLabel
	 */
	public String getResidueLabel() {
		return residueLabel;
	}

	/**
	 * @param residueLabel
	 *            the residueLabel to set
	 */
	public void setResidueLabel(String residueLabel) {
		this.residueLabel = residueLabel;
	}

	/**
	 * @return the chainId
	 */
	public String getChainId() {
		return chainId;
	}

	/**
	 * @param chainId
	 *            the chainId to set
	 */
	public void setChainId(String chainId) {
		this.chainId = chainId;
	}

	/**
	 * @return the chainPos
	 */
	public int getChainPos() {
		return chainPos;
	}

	/**
	 * @param chainPos
	 *            the chainPos to set
	 */
	public void setChainPos(int chainPos) {
		this.chainPos = chainPos;
	}

	/**
	 * @return the xCoord
	 */
	public double getxCoord() {
		return xCoord;
	}

	/**
	 * @param xCoord
	 *            the xCoord to set
	 */
	public void setxCoord(double xCoord) {
		this.xCoord = xCoord;
	}

	/**
	 * @return the yCoord
	 */
	public double getyCoord() {
		return yCoord;
	}

	/**
	 * @param yCoord
	 *            the yCoord to set
	 */
	public void setyCoord(double yCoord) {
		this.yCoord = yCoord;
	}

	/**
	 * @return the zCoord
	 */
	public double getzCoord() {
		return zCoord;
	}

	/**
	 * @param zCoord
	 *            the zCoord to set
	 */
	public void setzCoord(double zCoord) {
		this.zCoord = zCoord;
	}

	/**
	 * @return the occupancy
	 */
	public double getOccupancy() {
		return occupancy;
	}

	/**
	 * @param occupancy
	 *            the occupancy to set
	 */
	public void setOccupancy(double occupancy) {
		this.occupancy = occupancy;
	}

	/**
	 * @return the bfactor
	 */
	public double getBfactor() {
		return bfactor;
	}

	/**
	 * @param bfactor
	 *            the bfactor to set
	 */
	public void setBfactor(double bfactor) {
		this.bfactor = bfactor;
	}

	/**
	 * @return the atomLabel
	 */
	public String getAtomLabel() {
		return atomLabel;
	}

	/**
	 * @param atomLabel
	 *            the atomLabel to set
	 */
	public void setAtomLabel(String atomLabel) {
		this.atomLabel = atomLabel;
	}

}
