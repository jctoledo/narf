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
package org.semanticscience.narf.structures.interactions;

import org.semanticscience.narf.structures.lib.InferNucleotideParameters;
import org.semanticscience.narf.structures.lib.exceptions.InvalidEdgeException;
import org.semanticscience.narf.structures.lib.exceptions.InvalidGlycosidicOrientationException;
import org.semanticscience.narf.structures.parts.Edge;
import org.semanticscience.narf.structures.parts.Nucleotide;

/**
 * Representation of a base pairisng interaction between two nucleotides in a
 * nucleic acid.
 * 
 * @author Jose Cruz-Toledo
 * @author William Greenwood
 * @version %I%, %G%
 * @since 1.6
 * @see NucleotideInteraction
 * 
 */
// TODO: Add ALL base pair parameters
public class BasePair extends NucleotideInteraction implements
		Comparable<BasePair> {

	/**
	 * An identifying label for this base pair
	 */
	private String label;
	/**
	 * Orientation of the base pair (parallel|antiparallel).
	 */
	private String strandBPOrientation;
	/**
	 * The Leontis-Westhof nomenclature (cis|trans) for the orientation of the
	 * glycosidic bonds between the ribose sugars and nucleobases of the
	 * nucleotides in the base pair.
	 */
	private String glycosidicOrientation;
	/**
	 * Participating edge from the first nucleotide of the base pair.
	 */
	private Edge firstEdge;
	/**
	 * Participating edge from the second nucleotide of the base pair.
	 */
	private Edge secondEdge;
	/**
	 * torsion angle
	 */
	private Double torsion;
	/**
	 * C1-C1 distance
	 */
	private Double c1c1Distance;
	/**
	 * Distance N1-N9
	 */
	private Double n1n9Distance;
	/**
	 * Distance C6-C8
	 */
	private Double c6c8Distance;

	/**
	 * the saenger class that this base pair belongs to
	 */
	private String saengerClass;
	/**
	 * The leontis westhof class that describes this base pair
	 */
	private String LWClass;

	/**
	 * Construct a new base pairing interactions between two nucleotide
	 * residues.
	 * 
	 * @param aFirstNucleotide
	 *            The first nucleotide residue participating in the base pair.
	 * @param aSecondNucleotide
	 *            The second nucleotide residue participating in the base pair.
	 * @param aFirstEdge
	 *            The edge of the first nucleotide involved in the base pairing
	 *            interaction.
	 * @param aSecondEdge
	 *            The edge of the second nucleotide involved in the base pairing
	 *            interaction.
	 * @param aGlycosidicOrientation
	 *            The Leontis-Westhof nomenclature for the orientation of the
	 *            glycosidic bonds in the base pairing interaction.
	 * @param aStrandBPOrientation
	 *            The orientation of the base pair.
	 * @since 1.6
	 */
	public BasePair(Nucleotide aFirstNucleotide, Nucleotide aSecondNucleotide,
			Edge aFirstEdge, Edge aSecondEdge, String aGlycosidicOrientation,
			String aStrandBPOrientation) {
		super(aFirstNucleotide, aSecondNucleotide);
		firstEdge = aFirstEdge;
		secondEdge = aSecondEdge;
		glycosidicOrientation = aGlycosidicOrientation;
		strandBPOrientation = aStrandBPOrientation;
	}

	/**
	 * Construct a new base pairing interactions between two nucleotide
	 * residues.
	 * 
	 * @param n1
	 *            The first nucleotide residue participating in the base pair.
	 * @param n2
	 *            The second nucleotide residue participating in the base pair.
	 * @param e1
	 *            The edge of the first nucleotide involved in the base pairing
	 *            interaction.
	 * @param e2
	 *            The edge of the second nucleotide involved in the base pairing
	 *            interaction.
	 * @param glyorient
	 *            The Leontis-Westhof nomenclature for the orientation of the
	 *            glycosidic bonds in the base pairing interaction.
	 * @param bporient
	 *            The orientation of the base pair.
	 * @param aLabel
	 *            a descriptive label for this base pair
	 * @param aSaengerClass
	 *            the saenger class that best describes this base pair
	 */
	public BasePair(Nucleotide n1, Nucleotide n2, Edge e1, Edge e2,
			String glyorient, String bporient, String aLabel,
			String aSaengerClass, String aLWClass) {
		this(n1, n2, e1, e2, glyorient, bporient);
		label = aLabel;
		saengerClass = aSaengerClass;
		LWClass = aLWClass;
	}

	/**
	 * Construct a new base pairing interactions between two nucleotide
	 * residues. This constructor infers the strand orientation of the base pair
	 * by using knowledge of the two edges and the glycosidic orientation.
	 * 
	 * @param aFirstNucleotide
	 *            The first nucleotide residue participating in the base pair.
	 * @param aSecondNucleotide
	 *            The second nucleotide residue participating in the base pair.
	 * @param aFirstEdge
	 *            The edge of the first nucleotide involved in the base pairing
	 *            interaction.
	 * @param aSecondEdge
	 *            The edge of the second nucleotide involved in the base pairing
	 *            interaction.
	 * @param aGlycosidicOrientation
	 *            The Leontis-Westhof nomenclature for the orientation of the
	 *            glycosidic bonds in the base pairing interaction.
	 * @throws InvalidGlycosidicOrientationException
	 * @throws InvalidEdgeException
	 * @since 1.6
	 */
	public BasePair(Nucleotide aFirstNucleotide, Nucleotide aSecondNucleotide,
			Edge aFirstEdge, Edge aSecondEdge, String aGlycosidicOrientation)
			throws InvalidEdgeException, InvalidGlycosidicOrientationException {
		this(aFirstNucleotide, aSecondNucleotide, aFirstEdge, aSecondEdge,
				aGlycosidicOrientation, InferNucleotideParameters
						.findStrandBasePairOrientation(aFirstEdge, aSecondEdge,
								aGlycosidicOrientation));
	}

	/**
	 * Construct a new base pairing interactions between two nucleotide
	 * residues. Knowledge of the edges that are interacting in the base pair
	 * and the glycosidic and strand orientation of the base pairing interaction
	 * are not inferred as not enough information is provided. This constructor
	 * is meant for output from secondary structure predictors.
	 * 
	 * @param aFirstNucleotide
	 * @param aSecondNucleotide
	 * @since 1.6
	 */
	public BasePair(Nucleotide aFirstNucleotide, Nucleotide aSecondNucleotide) {
		super(aFirstNucleotide, aSecondNucleotide);
	}

	/**
	 * Get the participating edge from the first nucleotide of the base pair.
	 * 
	 * @return the first edge
	 * @since 1.6
	 */
	public Edge getFirstEdge() {
		return firstEdge;
	}

	/**
	 * Get the participating edge from the second nucleotide of the base pair.
	 * 
	 * @return the second edge
	 * @since 1.6
	 */
	public Edge getSecondEdge() {
		return secondEdge;
	}

	/**
	 * Get the Leontis-Westhof nomenclature for the orientation of the
	 * glycosidic bonds between the ribose sugars and nucleobases of the
	 * nucleotides in the base pair.
	 * 
	 * @return the glycosidic orientation
	 * @since 1.6
	 */
	public String getGlycosidicOrientation() {
		return glycosidicOrientation;
	}

	/**
	 * Orientation of the base pair (parallel|antiparallel).
	 * 
	 * @return the strand orientation of the base pair
	 * @since 1.6
	 */
	public String getStrandBasePairOrientation() {
		return strandBPOrientation;
	}

	/**
	 * positive – this object is greater than o1 zero – this object equals to o1
	 * negative – this object is less than o1
	 */
	@Override
	public int compareTo(BasePair o) {
		return this.getFirstNucleotide().getResiduePosition()
				- o.getFirstNucleotide().getResiduePosition();
	}

	/**
	 * @return the strandBPOrientation
	 */
	public String getStrandBPOrientation() {
		return strandBPOrientation;
	}

	/**
	 * @return the torsion
	 */
	public Double getTorsion() {
		return torsion;
	}

	/**
	 * @return the c1c1Distance
	 */
	public Double getC1c1Distance() {
		return c1c1Distance;
	}

	/**
	 * @return the n1n9Distance
	 */
	public Double getN1n9Distance() {
		return n1n9Distance;
	}

	/**
	 * @return the c6c8Distance
	 */
	public Double getC6c8Distance() {
		return c6c8Distance;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * 
	 * @return The saenger class of this base pair
	 */
	public String getSaengerClass() {
		return saengerClass;
	}

	public String getLWClass() {
		return LWClass;
	}

	/**
	 * This method infers the correct RNAO class for a given BasePair object.
	 * The inferences are drawn from Table 2 of
	 * http://www.ncbi.nlm.nih.gov/pmc/articles/PMC1370104/ and from
	 * http://rnao.googlecode.com/svn-history/r115/trunk/rnao.owl
	 * 
	 * @param aBp
	 *            a BasePair for which an RNAO class must be inferred
	 * @return the RNAO identifier of the corresponding BasePair class, the uri
	 *         http://purl.obolibrary.org/obo/RNAO_0000001 is returned if no
	 *         better fit is found
	 */
	public String inferRnaOClass() {
		String lwclass = this.getLWClass();
		if (lwclass.equals("cWW")) {
			// check if the two nucleotides are guanine
			if (this.getFirstNucleotide().getResidueIdentifier()
					.equalsIgnoreCase("G")
					&& this.getSecondNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("G")) {
				return "http://purl.obolibrary.org/obo/RNAO_0000116";
			} else {
				return "http://purl.obolibrary.org/obo/RNAO_0000003";
			}
		} else if (lwclass.equals("cSS")) {
			return "http://purl.obolibrary.org/obo/RNAO_0000013";
		} else if (lwclass.equals("cWH") || lwclass.equals("cHW")) {
			// check for forbidden pairings
			// CA
			if ((this.getFirstNucleotide().getResidueIdentifier()
					.equalsIgnoreCase("A") && this.getSecondNucleotide()
					.getResidueIdentifier().equalsIgnoreCase("C"))
					|| (this.getFirstNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("C") && this
							.getSecondNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("A"))) {
				return "http://purl.obolibrary.org/obo/RNAO_0000116";
			}
			// UC
			else if ((this.getFirstNucleotide().getResidueIdentifier()
					.equalsIgnoreCase("C") && this.getSecondNucleotide()
					.getResidueIdentifier().equalsIgnoreCase("U"))
					|| (this.getFirstNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("U") && this
							.getSecondNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("C"))) {
				return "http://purl.obolibrary.org/obo/RNAO_0000116";
			}
			// AA
			else if (this.getFirstNucleotide().getResidueIdentifier()
					.equalsIgnoreCase("A")
					&& this.getSecondNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("A")) {
				return "http://purl.obolibrary.org/obo/RNAO_0000116";
			}
			// GC
			else if ((this.getFirstNucleotide().getResidueIdentifier()
					.equalsIgnoreCase("C") && this.getSecondNucleotide()
					.getResidueIdentifier().equalsIgnoreCase("G"))
					|| (this.getFirstNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("G") && this
							.getSecondNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("C"))) {
				return "http://purl.obolibrary.org/obo/RNAO_0000116";
			}
			// GU
			else if ((this.getFirstNucleotide().getResidueIdentifier()
					.equalsIgnoreCase("U") && this.getSecondNucleotide()
					.getResidueIdentifier().equalsIgnoreCase("G"))
					|| (this.getFirstNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("G") && this
							.getSecondNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("U"))) {
				return "http://purl.obolibrary.org/obo/RNAO_0000116";
			} else {
				return "http://purl.obolibrary.org/obo/RNAO_0000005";
			}
		} else if (lwclass.equals("cWS") || lwclass.equals("cSW")) {
			return "http://purl.obolibrary.org/obo/RNAO_0000007";
		} else if (lwclass.equals("cHH")) {
			// check for forbidden pairings
			// CC
			if (this.getFirstNucleotide().getResidueIdentifier()
					.equalsIgnoreCase("A")
					&& this.getSecondNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("A")) {
				return "http://purl.obolibrary.org/obo/RNAO_0000116";
			}
			// CU
			else if ((this.getFirstNucleotide().getResidueIdentifier()
					.equalsIgnoreCase("C") && this.getSecondNucleotide()
					.getResidueIdentifier().equalsIgnoreCase("U"))
					|| (this.getFirstNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("U") && this
							.getSecondNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("C"))) {
				return "http://purl.obolibrary.org/obo/RNAO_0000116";
			}
			// CA
			else if ((this.getFirstNucleotide().getResidueIdentifier()
					.equalsIgnoreCase("C") && this.getSecondNucleotide()
					.getResidueIdentifier().equalsIgnoreCase("A"))
					|| (this.getFirstNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("A") && this
							.getSecondNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("C"))) {
				return "http://purl.obolibrary.org/obo/RNAO_0000116";
			}
			// UA
			else if ((this.getFirstNucleotide().getResidueIdentifier()
					.equalsIgnoreCase("U") && this.getSecondNucleotide()
					.getResidueIdentifier().equalsIgnoreCase("A"))
					|| (this.getFirstNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("A") && this
							.getSecondNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("U"))) {
				return "http://purl.obolibrary.org/obo/RNAO_0000116";
			}
			// UG
			else if ((this.getFirstNucleotide().getResidueIdentifier()
					.equalsIgnoreCase("U") && this.getSecondNucleotide()
					.getResidueIdentifier().equalsIgnoreCase("G"))
					|| (this.getFirstNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("G") && this
							.getSecondNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("U"))) {
				return "http://purl.obolibrary.org/obo/RNAO_0000116";
			}
			// UU
			else if (this.getFirstNucleotide().getResidueIdentifier()
					.equalsIgnoreCase("U")
					&& this.getSecondNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("U")) {
				return "http://purl.obolibrary.org/obo/RNAO_0000116";
			}
			// AA
			else if (this.getFirstNucleotide().getResidueIdentifier()
					.equalsIgnoreCase("A")
					&& this.getSecondNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("A")) {
				return "http://purl.obolibrary.org/obo/RNAO_0000116";
			} else {
				return "http://purl.obolibrary.org/obo/RNAO_0000009";
			}
		} else if (lwclass.equals("cHS") || lwclass.equals("cSH")) {
			return "http://purl.obolibrary.org/obo/RNAO_0000011";
		} else if (lwclass.equals("tHS") || lwclass.equals("tSH")) {
			return "http://purl.obolibrary.org/obo/RNAO_0000012";
		} else if (lwclass.equals("tSS")) {
			// check for forbiden pairings
			// CA
			if ((this.getFirstNucleotide().getResidueIdentifier()
					.equalsIgnoreCase("C") && this.getSecondNucleotide()
					.getResidueIdentifier().equalsIgnoreCase("A"))
					|| (this.getFirstNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("A") && this
							.getSecondNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("C"))) {
				return "http://purl.obolibrary.org/obo/RNAO_0000116";
			}
			// CC
			else if (this.getFirstNucleotide().getResidueIdentifier()
					.equalsIgnoreCase("C")
					&& this.getSecondNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("C")) {
				return "http://purl.obolibrary.org/obo/RNAO_0000116";
			}
			// CU
			else if ((this.getFirstNucleotide().getResidueIdentifier()
					.equalsIgnoreCase("C") && this.getSecondNucleotide()
					.getResidueIdentifier().equalsIgnoreCase("U"))
					|| (this.getFirstNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("U") && this
							.getSecondNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("C"))) {
				return "http://purl.obolibrary.org/obo/RNAO_0000116";
			}
			// CG
			else if ((this.getFirstNucleotide().getResidueIdentifier()
					.equalsIgnoreCase("C") && this.getSecondNucleotide()
					.getResidueIdentifier().equalsIgnoreCase("G"))
					|| (this.getFirstNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("G") && this
							.getSecondNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("C"))) {
				return "http://purl.obolibrary.org/obo/RNAO_0000116";
			}
			// UA
			else if ((this.getFirstNucleotide().getResidueIdentifier()
					.equalsIgnoreCase("U") && this.getSecondNucleotide()
					.getResidueIdentifier().equalsIgnoreCase("A"))
					|| (this.getFirstNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("A") && this
							.getSecondNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("U"))) {
				return "http://purl.obolibrary.org/obo/RNAO_0000116";
			}
			// UU
			else if (this.getFirstNucleotide().getResidueIdentifier()
					.equalsIgnoreCase("U")
					&& this.getSecondNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("U")) {
				return "http://purl.obolibrary.org/obo/RNAO_0000116";
			}
			// UG
			else if ((this.getFirstNucleotide().getResidueIdentifier()
					.equalsIgnoreCase("U") && this.getSecondNucleotide()
					.getResidueIdentifier().equalsIgnoreCase("G"))
					|| (this.getFirstNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("G") && this
							.getSecondNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("U"))) {
				return "http://purl.obolibrary.org/obo/RNAO_0000116";
			} else {
				return "http://purl.obolibrary.org/obo/RNAO_0000014";
			}
		} else if (lwclass.equals("tWW")) {
			// only accepted pairings are GU, GG and GC
			if ((this.getFirstNucleotide().getResidueIdentifier()
					.equalsIgnoreCase("G") && this.getSecondNucleotide()
					.getResidueIdentifier().equalsIgnoreCase("U"))
					|| (this.getFirstNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("U") && this
							.getSecondNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("G"))) {
				return "http://purl.obolibrary.org/obo/RNAO_0000004";
			} else if (this.getFirstNucleotide().getResidueIdentifier()
					.equalsIgnoreCase("G")
					&& this.getSecondNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("G")) {
				return "http://purl.obolibrary.org/obo/RNAO_0000004";
			} else if ((this.getFirstNucleotide().getResidueIdentifier()
					.equalsIgnoreCase("G") && this.getSecondNucleotide()
					.getResidueIdentifier().equalsIgnoreCase("C"))
					|| (this.getFirstNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("C") && this
							.getSecondNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("G"))) {
				return "http://purl.obolibrary.org/obo/RNAO_0000004";
			} else {
				return "http://purl.obolibrary.org/obo/RNAO_0000117";
			}
		} else if (lwclass.equals("tWH") || lwclass.equals("tHW")) {
			// check for forbidden pairings
			// CU
			if ((this.getFirstNucleotide().getResidueIdentifier()
					.equalsIgnoreCase("C") && this.getSecondNucleotide()
					.getResidueIdentifier().equalsIgnoreCase("U"))
					|| (this.getFirstNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("U") && this
							.getSecondNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("C"))) {
				return "http://purl.obolibrary.org/obo/RNAO_0000117";
			}
			// AC
			else if ((this.getFirstNucleotide().getResidueIdentifier()
					.equalsIgnoreCase("A") && this.getSecondNucleotide()
					.getResidueIdentifier().equalsIgnoreCase("C"))
					|| (this.getFirstNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("C") && this
							.getSecondNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("A"))) {
				return "http://purl.obolibrary.org/obo/RNAO_0000117";
			}
			// AU
			else if ((this.getFirstNucleotide().getResidueIdentifier()
					.equalsIgnoreCase("A") && this.getSecondNucleotide()
					.getResidueIdentifier().equalsIgnoreCase("U"))
					|| (this.getFirstNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("U") && this
							.getSecondNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("A"))) {
				return "http://purl.obolibrary.org/obo/RNAO_0000117";
			}
			// GC
			else if ((this.getFirstNucleotide().getResidueIdentifier()
					.equalsIgnoreCase("C") && this.getSecondNucleotide()
					.getResidueIdentifier().equalsIgnoreCase("G"))
					|| (this.getFirstNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("G") && this
							.getSecondNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("C"))) {
				return "http://purl.obolibrary.org/obo/RNAO_0000117";
			}
			// GA
			else if ((this.getFirstNucleotide().getResidueIdentifier()
					.equalsIgnoreCase("G") && this.getSecondNucleotide()
					.getResidueIdentifier().equalsIgnoreCase("A"))
					|| (this.getFirstNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("A") && this
							.getSecondNucleotide().getResidueIdentifier()
							.equalsIgnoreCase("G"))) {
				return "http://purl.obolibrary.org/obo/RNAO_0000117";
			} else {
				return "http://purl.obolibrary.org/obo/RNAO_0000006";
			}
		} else if (lwclass.equals("tWS") || lwclass.equals("tSW")) {
			return "http://purl.obolibrary.org/obo/RNAO_0000008";
		} else if (lwclass.equals("tHH")) {
			return "http://purl.obolibrary.org/obo/RNAO_0000010";
		} else {
			return "http://purl.obolibrary.org/obo/RNAO_0000001";
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BasePair [label=" + label + ", strandBPOrientation="
				+ strandBPOrientation + ", glycosidicOrientation="
				+ glycosidicOrientation + ", first nucleotide="
				+ getFirstNucleotide().toString() + " , second nucleotide="
				+ getSecondNucleotide().toString() + ", firstEdge=" + firstEdge
				+ ", secondEdge=" + secondEdge + ", torsion=" + torsion
				+ ", c1c1Distance=" + c1c1Distance + ", n1n9Distance="
				+ n1n9Distance + ", c6c8Distance=" + c6c8Distance
				+ ", saengerClass=" + saengerClass + ", LWClass=" + LWClass
				+ "]";
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
		result = prime * result + ((LWClass == null) ? 0 : LWClass.hashCode());
		result = prime * result
				+ ((c1c1Distance == null) ? 0 : c1c1Distance.hashCode());
		result = prime * result
				+ ((c6c8Distance == null) ? 0 : c6c8Distance.hashCode());
		result = prime * result
				+ ((firstEdge == null) ? 0 : firstEdge.hashCode());
		result = prime
				* result
				+ ((glycosidicOrientation == null) ? 0 : glycosidicOrientation
						.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result
				+ ((n1n9Distance == null) ? 0 : n1n9Distance.hashCode());
		result = prime * result
				+ ((saengerClass == null) ? 0 : saengerClass.hashCode());
		result = prime * result
				+ ((secondEdge == null) ? 0 : secondEdge.hashCode());
		result = prime
				* result
				+ ((strandBPOrientation == null) ? 0 : strandBPOrientation
						.hashCode());
		result = prime * result + ((torsion == null) ? 0 : torsion.hashCode());
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
		BasePair other = (BasePair) obj;
		if (LWClass == null) {
			if (other.LWClass != null)
				return false;
		} else if (!LWClass.equals(other.LWClass))
			return false;
		if (c1c1Distance == null) {
			if (other.c1c1Distance != null)
				return false;
		} else if (!c1c1Distance.equals(other.c1c1Distance))
			return false;
		if (c6c8Distance == null) {
			if (other.c6c8Distance != null)
				return false;
		} else if (!c6c8Distance.equals(other.c6c8Distance))
			return false;
		if (firstEdge == null) {
			if (other.firstEdge != null)
				return false;
		} else if (!firstEdge.equals(other.firstEdge))
			return false;
		if (glycosidicOrientation == null) {
			if (other.glycosidicOrientation != null)
				return false;
		} else if (!glycosidicOrientation.equals(other.glycosidicOrientation))
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (n1n9Distance == null) {
			if (other.n1n9Distance != null)
				return false;
		} else if (!n1n9Distance.equals(other.n1n9Distance))
			return false;
		if (saengerClass == null) {
			if (other.saengerClass != null)
				return false;
		} else if (!saengerClass.equals(other.saengerClass))
			return false;
		if (secondEdge == null) {
			if (other.secondEdge != null)
				return false;
		} else if (!secondEdge.equals(other.secondEdge))
			return false;
		if (strandBPOrientation == null) {
			if (other.strandBPOrientation != null)
				return false;
		} else if (!strandBPOrientation.equals(other.strandBPOrientation))
			return false;
		if (torsion == null) {
			if (other.torsion != null)
				return false;
		} else if (!torsion.equals(other.torsion))
			return false;
		return true;
	}

}
