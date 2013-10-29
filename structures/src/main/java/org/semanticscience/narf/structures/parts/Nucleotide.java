/**
 * Copyright (c) 2011 Jose Miguel Cruz Toledo and William Greenwood
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

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.semanticscience.narf.structures.lib.exceptions.InvalidResidueException;

/**
 * Representation of a nucleotide residue component of a nucleic acid.
 * 
 * @author Jose Cruz-Toledo
 * @author William Greenwood
 * @version %I%, %G%
 * @since 1.6
 */
public class Nucleotide extends Residue {
	/**
	 * A mapping between the residue identifier and a unique integer
	 */
	private Map<String, Integer> normalizedNucleotideMapping;
	/**
	 * the Pdb id where tho
	 */
	private String pdbId;
	/**
	 * the chain identifier for this nucleotide
	 */
	private String chainId;
	/**
	 * Glycosidic bond rotation (anti or syn).
	 */
	private final String nucleotideConformation;

	/**
	 * Residue position in the chain of the nucleic acid.
	 */
	private final int residuePosition;
	/**
	 * Puckering atom of this nucleotide residue
	 */
	private final String puckerAtom;
	/**
	 * Quality of puckering atom (endo|exo)
	 */
	private final String puckerQuality;

	/**
	 * Construct a nucleotide from only its residue position and residue label.
	 * 
	 * @param aResiduePosition
	 *            the position in chain
	 * @param aResidueIdentifier
	 *            the three letter PDB chemical identifier for the nucleotide or
	 *            a single letter code
	 * @throws InvalidResidueException
	 */
	public Nucleotide(int aResiduePosition, String aResidueIdentifier)
			throws InvalidResidueException {
		this(aResiduePosition, aResidueIdentifier, null);
	}

	/**
	 * Construct a nucleotide from only its residue position, molecule label and
	 * conformation label.
	 * 
	 * @param aResiduePosition
	 *            the position on the chain
	 * @param aResidueIdentifier
	 *            the three letter PDB chemical identifier for the nucleotide
	 * @param aConformationLabel
	 *            the conformation of the nucleotide (anti | syn)
	 * @throws InvalidResidueException
	 */
	public Nucleotide(int aResiduePosition, String aResidueIdentifier,
			String aConformationLabel) throws InvalidResidueException {
		this(aResiduePosition, aResidueIdentifier, aConformationLabel, null,
				null);
	}

	/**
	 * Construct a nucleotide from only its residue position, molecule label,
	 * conformation label, pucker atom and pucker atom quality.
	 * 
	 * @param residuePosition
	 *            the position on the chain
	 * @param aResidueIdentifier
	 *            the three letter PDB chemical identifier for the nucleotide
	 * @param aConformationLabel
	 *            the conformation of the nucleotide (anti | syn)
	 * @param aPuckerAtom
	 *            the atom that is puckered
	 * @param aPuckerQuality
	 *            the quality of the puckering
	 * @throws InvalidResidueException
	 */
	public Nucleotide(int aResiduePosition, String aResidueIdentifier,
			String aConformationLabel, String aPuckerAtom, String aPuckerQuality)
			throws InvalidResidueException {
		super(aResiduePosition, aResidueIdentifier);
		residuePosition = aResiduePosition;
		nucleotideConformation = aConformationLabel;
		puckerAtom = aPuckerAtom;
		puckerQuality = aPuckerQuality;
		normalizedNucleotideMapping = this.makeNormalizedNucleotideMap();
	}

	/**
	 * Construct a nucleotide from its pdbid, chainid, residue position,
	 * molecule label, conformation label, pucker atom and pucker atom quality.
	 * 
	 * @param aPdbId
	 *            the pdbid corresponding to this nucleotide
	 * @param aChainId
	 *            the chain identifier
	 * @param aResiduePosition
	 *            the position on the chain
	 * @param aResidueIdentifier
	 *            the three letter PDB chemical identifier for the nucleotide
	 * @param aConformationLabel
	 *            the conformation of the nucleotide (anti | syn)
	 * @param aPuckerAtom
	 *            the atom that is puckered
	 * @param aPuckerQuality
	 *            the quality of the puckering
	 * @throws InvalidResidueException
	 */
	public Nucleotide(String aPdbId, String aChainId, int aResiduePosition,
			String aResidueIdentifier, String aConformationLabel,
			String aPuckerAtom, String aPuckerQuality)
			throws InvalidResidueException {
		this(aResiduePosition, aResidueIdentifier, aConformationLabel,
				aPuckerAtom, aPuckerQuality);
		pdbId = aPdbId;
		chainId = aChainId;
	}

	/**
	 * Determine if the nucleotide contains information relating to nucleotide
	 * conformation.
	 * 
	 * @return <code>true</code> if the nucleotide has nucleotide conformation,
	 *         otherwise <code>false</code>
	 * @since 1.6
	 */
	public boolean hasNucleotideConformation() {
		return (nucleotideConformation != null);
	}

	/**
	 * Determine if the nucleotide contains information relating to the pucker
	 * atom.
	 * 
	 * @return <code>true</code> if the nucleotide has a pucker atom, otherwise
	 *         <code>false</code>
	 * @since 1.6
	 */
	public boolean hasPuckerAtom() {
		return (puckerAtom != null);
	}

	/**
	 * Determine if the nucleotide contains information relating to pucker
	 * quality.
	 * 
	 * @return <code>true</code> if the nucleotide has a pucker quality,
	 *         otherwise <code>false</code>
	 * @since 1.6
	 */
	public boolean hasPuckerQuality() {
		return (puckerQuality != null);
	}

	/**
	 * Get the glycosidic bond roation of the nucleotide
	 * 
	 * @return the string representation of the glycosidic bond rotation
	 * @since 1.6
	 */
	public String getNucleotideConformation() {
		if (!this.hasNucleotideConformation()) {
			throw new UnsupportedOperationException(
					"No nucleotide conformation is present in this nucleotide.");
		}

		return nucleotideConformation;
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
	 * Get the pucker atom of the nucleotide
	 * 
	 * @return the string representation of the pucker atom
	 * @since 1.6
	 */
	public String getPuckerAtom() {
		if (!this.hasPuckerAtom()) {
			throw new UnsupportedOperationException(
					"No pucker atom is present in this nucleotide.");
		}

		return puckerAtom;
	}

	/**
	 * Get the pucker quality of the nucleotide
	 * 
	 * @return the string representation of the pucker quality
	 * @since 1.6
	 */
	public String getPuckerQuality() {
		if (!this.hasPuckerQuality()) {
			throw new UnsupportedOperationException(
					"No pucker quality is present in this nucleotide.");
		}

		return puckerQuality;
	}

	public String getChainId() {
		return chainId;
	}
	/**
	 * REtrieve an integer representation of this nucleotide. The returned integer corresponds only to the nucleobase and not to its position on the sequence
	 * @return an integer representation for a given nucleotide
	 */
	public Integer getNormalizedNucleotide(){
		Map <String, Integer> m = this.getNormalizedNucleotideMapping();
		String res_id = this.getResidueIdentifier();
		return m.get(res_id);
	}
	/**
	 * Make a map where the key is a residue identifier and the value is a
	 * unique integer
	 * 
	 * @return mapping between a residue identifier and a unique integer
	 */
	private Map<String, Integer> makeNormalizedNucleotideMap(){
		Map<String, Integer> rm = new HashMap<String, Integer>();
		rm.put("A", 21);
		rm.put("G", 22);
		rm.put("U", 23);
		rm.put("C", 24);
		rm.put("T", 25);
		rm.put("X", 26);
		rm.put("Y", 27);
		rm.put("R", 28);
		return rm;
	}
	
	public Map<String, Integer> getNormalizedNucleotideMapping(){
		return this.normalizedNucleotideMapping;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((chainId == null) ? 0 : chainId.hashCode());
		result = prime
				* result
				+ ((nucleotideConformation == null) ? 0
						: nucleotideConformation.hashCode());
		result = prime * result + ((pdbId == null) ? 0 : pdbId.hashCode());
		result = prime * result
				+ ((puckerAtom == null) ? 0 : puckerAtom.hashCode());
		result = prime * result
				+ ((puckerQuality == null) ? 0 : puckerQuality.hashCode());
		result = prime * result + residuePosition;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Nucleotide other = (Nucleotide) obj;
		if (chainId == null) {
			if (other.chainId != null)
				return false;
		} else if (!chainId.equals(other.chainId))
			return false;
		if (nucleotideConformation == null) {
			if (other.nucleotideConformation != null)
				return false;
		} else if (!nucleotideConformation.equals(other.nucleotideConformation))
			return false;
		if (pdbId == null) {
			if (other.pdbId != null)
				return false;
		} else if (!pdbId.equals(other.pdbId))
			return false;
		if (puckerAtom == null) {
			if (other.puckerAtom != null)
				return false;
		} else if (!puckerAtom.equals(other.puckerAtom))
			return false;
		if (puckerQuality == null) {
			if (other.puckerQuality != null)
				return false;
		} else if (!puckerQuality.equals(other.puckerQuality))
			return false;
		if (residuePosition != other.residuePosition)
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Nucleotide [pdbId=" + pdbId + ", chainId=" + chainId
				+ " , residueIdentifier=" + this.getResidueIdentifier()
				+ ", nucleotideConformation=" + nucleotideConformation
				+ ", residuePosition=" + residuePosition + ", puckerAtom="
				+ puckerAtom + ", puckerQuality=" + puckerQuality + "]";
	}

	public int compareTo(Nucleotide other){
		int pos = other.getResiduePosition();
		return this.getResiduePosition() - pos;
	}

}
