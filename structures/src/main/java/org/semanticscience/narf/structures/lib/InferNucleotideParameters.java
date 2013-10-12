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
package org.semanticscience.narf.structures.lib;

import java.util.LinkedHashSet;
import java.util.Set;

import org.semanticscience.narf.structures.interactions.BasePair;
import org.semanticscience.narf.structures.lib.exceptions.InvalidEdgeException;
import org.semanticscience.narf.structures.lib.exceptions.InvalidGlycosidicOrientationException;
import org.semanticscience.narf.structures.parts.Edge;
import org.semanticscience.narf.structures.parts.Nucleotide;
import org.semanticscience.narf.structures.parts.SubEdge;

/**
 * A utility class designed for the purpose to infer the parameters for the
 * constituents of nucleic acids that are missing.
 * 
 * @author Jose Cruz-Toledo
 * @author William Greenwood
 * @version %I%, %G%
 * @since 1.6
 */

public final class InferNucleotideParameters {

	/**
	 * Determine if a subedge is part of a Watson-Crick edge
	 * 
	 * @param aSubEdge
	 *            the substructure of an edge
	 * @return <code>true</code> if the subedge is part of a Watson-Crick edge
	 *         otherwise <code>false</code>
	 * @since 1.6
	 */
	public final static boolean isWatsonCrickEdge(SubEdge aSubEdge) {
		if (aSubEdge.getSubEdgeLabel().equals("Wh")
				|| aSubEdge.getSubEdgeLabel().equals("Ww")
				|| aSubEdge.getSubEdgeLabel().equals("Ws")) {
			return true;
		}

		return false;
	}

	/**
	 * Determine if a subedge is part of a Hoogsteen edge
	 * 
	 * @param aSubEdge
	 *            the substructure of an edge
	 * @return <code>true</code> if the subedge is part of a Hoogsteen edge
	 *         otherwise <code>false</code>
	 * @since 1.6
	 */
	public final static boolean isHoogsteenEdge(SubEdge aSubEdge) {
		if (aSubEdge.getSubEdgeLabel().equals("C8")
				|| aSubEdge.getSubEdgeLabel().equals("Hh")
				|| aSubEdge.getSubEdgeLabel().equals("Hw")
				|| aSubEdge.getSubEdgeLabel().equals("Bh")) {
			return true;
		}

		return false;
	}

	/**
	 * Determine if a subedge is part of a Sugar edge
	 * 
	 * @param aSubEdge
	 *            the substructure of an edge
	 * @return <code>true</code> if the subedge is part of a Sugar edge
	 *         otherwise <code>false</code>
	 * @since 1.6
	 */
	public final static boolean isSugarEdge(SubEdge aSubEdge) {
		if (aSubEdge.getSubEdgeLabel().equals("O2'")
				|| aSubEdge.getSubEdgeLabel().equals("O2P")
				|| aSubEdge.getSubEdgeLabel().equals("Sh")
				|| aSubEdge.getSubEdgeLabel().equals("Ss")
				|| aSubEdge.getSubEdgeLabel().equals("Sw")
				|| aSubEdge.getSubEdgeLabel().equals("Bs")) {
			return true;
		}

		return false;
	}

	/**
	 * Determine the type of edge of a {@link BasePair} from a set of subedges.
	 * 
	 * @param someSubEdges
	 *            a set of the substructural components of an edge.
	 * @return a string representation of one of the three types of edges (W for
	 *         Watson-Crick edge, H for Hoogsteen edge and S for Sugar edge) or
	 *         N/A if none of the subedges in the set are part of an edge.
	 * @throws InvalidEdgeException
	 *             if none of the subedges in the set are substructures of any
	 *             known edge
	 * @since 1.6
	 */
	public final static String inferEdge(Set<SubEdge> someSubEdges)
			throws InvalidEdgeException {
		for (SubEdge subEdge : someSubEdges) {
			if (isWatsonCrickEdge(subEdge)) {
				return "W";
			} else if (isHoogsteenEdge(subEdge)) {
				return "H";
			} else if (isSugarEdge(subEdge)) {
				return "S";
			}
		}

		throw new InvalidEdgeException(
				"None of the subedges provided are substructures of any known edge.");
	}

	/**
	 * Determine the set of subedges of a given {@link Edge} involved in a
	 * {@link BasePair}.
	 * 
	 * @param aNucleotide
	 *            nucleotide involved in a {@link BasePair}.
	 * @param anEdgeLabel
	 *            string identifier of an {@link Edge} of
	 *            <code>aNucleotide</code>.
	 * @return a set of the subedges that comprise an {@link Edge} of a
	 *         nucleotide in a {@link BasePair}. An empty set will be returned
	 *         if the edge label does not correspond with one of the three
	 *         common Leontis-Westhof edges.
	 * @throws InvalidEdgeException
	 *             if the edge label is not of any currently known edge
	 * @since 1.6
	 */
	public static final Set<SubEdge> inferSubEdges(Nucleotide aNucleotide,
			String anEdgeLabel) throws InvalidEdgeException {
		Set<SubEdge> subEdges = new LinkedHashSet<SubEdge>();
		
		if (anEdgeLabel.equals(".")) {
			return subEdges;
		}

		if (!anEdgeLabel.equals("W") && !anEdgeLabel.equals("H")
				&& !anEdgeLabel.equals("S")) {
			throw new InvalidEdgeException(
					"The provided edge label is not of any currently known edge.");
		}

		if (anEdgeLabel.equals("W")) {
			if (aNucleotide.getResidueIdentifier().equalsIgnoreCase("A")) {
				subEdges.add(new SubEdge("Wh"));
				subEdges.add(new SubEdge("Ww"));
			} else if (aNucleotide.getResidueIdentifier().equalsIgnoreCase("G")) {
				subEdges.add(new SubEdge("Wh"));
				subEdges.add(new SubEdge("Ww"));
				subEdges.add(new SubEdge("Ws"));
			} else if (aNucleotide.getResidueIdentifier().equalsIgnoreCase("U")) {
				subEdges.add(new SubEdge("Wh"));
				subEdges.add(new SubEdge("Ww"));
				subEdges.add(new SubEdge("Ws"));
			} else if (aNucleotide.getResidueIdentifier().equalsIgnoreCase("C")) {
				subEdges.add(new SubEdge("Wh"));
				subEdges.add(new SubEdge("Ww"));
				subEdges.add(new SubEdge("Ws"));
			}
		}// if Watson
		else if (anEdgeLabel.equals("H")) {
			if (aNucleotide.getResidueIdentifier().equalsIgnoreCase("A")) {
				subEdges.add(new SubEdge("C8"));
				subEdges.add(new SubEdge("Hh"));
				subEdges.add(new SubEdge("Hw"));
				subEdges.add(new SubEdge("Bh"));
			} else if (aNucleotide.getResidueIdentifier().equalsIgnoreCase("G")) {
				subEdges.add(new SubEdge("C8"));
				subEdges.add(new SubEdge("Hh"));
				subEdges.add(new SubEdge("Hw"));
				subEdges.add(new SubEdge("Bh"));
			} else if (aNucleotide.getResidueIdentifier().equalsIgnoreCase("U")) {
				subEdges.add(new SubEdge("Hh"));
				subEdges.add(new SubEdge("Hw"));
				subEdges.add(new SubEdge("Bh"));
			} else if (aNucleotide.getResidueIdentifier().equalsIgnoreCase("C")) {
				subEdges.add(new SubEdge("Hh"));
				subEdges.add(new SubEdge("Hw"));
				subEdges.add(new SubEdge("Bh"));
			}
		}// elseif Hoogsteen
		else if (anEdgeLabel.equals("S")) {

			if (aNucleotide.getResidueIdentifier().equalsIgnoreCase("A")) {
				subEdges.add(new SubEdge("Bs"));
				subEdges.add(new SubEdge("Ss"));
				subEdges.add(new SubEdge("O2'"));
			} else if (aNucleotide.getResidueIdentifier().equalsIgnoreCase("G")) {
				subEdges.add(new SubEdge("Bs"));
				subEdges.add(new SubEdge("Sw"));
				subEdges.add(new SubEdge("Ss"));
				subEdges.add(new SubEdge("O2'"));
			} else if (aNucleotide.getResidueIdentifier().equalsIgnoreCase("U")) {
				subEdges.add(new SubEdge("Bs"));
				subEdges.add(new SubEdge("Sw"));
				subEdges.add(new SubEdge("O2'"));
			} else if (aNucleotide.getResidueIdentifier().equalsIgnoreCase("C")) {
				subEdges.add(new SubEdge("Bs"));
				subEdges.add(new SubEdge("Sw"));
				subEdges.add(new SubEdge("O2'"));
			}
		}// elseif Sugar
		return subEdges;
	}

	/**
	 * Determine the strand orientation for a {@link BasePair} given its two
	 * interacting edges and its glycosidic orientation.
	 * 
	 * @param anEdge1
	 *            the first edge of the {@link BasePair}
	 * @param anEdge2
	 *            the second edge of the {@link BasePair}
	 * @param aGlycosidicOrientation
	 *            the glycosidic orientation of the {@link BasePair}
	 * @return string identifier of the strand orientation for the
	 *         {@link BasePair} or null if either edge is "."
	 * @throws InvalidEdgeException
	 *             if the edge is not any currently known edge
	 * @throws InvalidGlycosidicOrientationException
	 *             if the glycosidic orientation is not recognized
	 * @since 1.6
	 */
	public final static String findStrandBasePairOrientation(Edge anEdge1,
			Edge anEdge2, String aGlycosidicOrientation)
			throws InvalidEdgeException, InvalidGlycosidicOrientationException {
		if(anEdge1.getEdgeLabel().equals(".") || anEdge2.getEdgeLabel().equals(".")){
			return null;
		}
		if (aGlycosidicOrientation.equals("cis")
				|| aGlycosidicOrientation.equals("c")) {
			if (anEdge1.getEdgeLabel().equals("W")
					&& anEdge2.getEdgeLabel().equals("W")) {
				return "antiparallel";
			} else if ((anEdge1.getEdgeLabel().equals("W") && anEdge2
					.getEdgeLabel().equals("H"))
					|| (anEdge1.getEdgeLabel().equals("H") && anEdge2
							.getEdgeLabel().equals("W"))) {
				return "parallel";
			} else if ((anEdge1.getEdgeLabel().equals("W") && anEdge2
					.getEdgeLabel().equals("S"))
					|| (anEdge1.getEdgeLabel().equals("S") && anEdge2
							.getEdgeLabel().equals("W"))) {
				return "antiparallel";
			} else if ((anEdge1.getEdgeLabel().equals("H") && anEdge2
					.getEdgeLabel().equals("W"))
					|| (anEdge1.getEdgeLabel().equals("W") && anEdge2
							.getEdgeLabel().equals("H"))) {
				return "parallel";
			} else if (anEdge1.getEdgeLabel().equals("H")
					&& anEdge2.getEdgeLabel().equals("H")) {
				return "antiparallel";
			} else if ((anEdge1.getEdgeLabel().equals("H") && anEdge2
					.getEdgeLabel().equals("S"))
					|| (anEdge1.getEdgeLabel().equals("S") && anEdge2
							.getEdgeLabel().equals("H"))) {
				return "parallel";
			} else if ((anEdge1.getEdgeLabel().equals("S") && anEdge2
					.getEdgeLabel().equals("W"))
					|| (anEdge1.getEdgeLabel().equals("W") && anEdge2
							.getEdgeLabel().equals("S"))) {
				return "antiparallel";
			} else if ((anEdge1.getEdgeLabel().equals("S") && anEdge2
					.getEdgeLabel().equals("H"))
					|| (anEdge1.getEdgeLabel().equals("H") && anEdge2
							.getEdgeLabel().equals("S"))) {
				return "parallel";
			} else if (anEdge1.getEdgeLabel().equals("S")
					&& anEdge2.getEdgeLabel().equals("S")) {
				return "antiparallel";
			}
			throw new InvalidEdgeException(
					"The provided edge is not of any currently known edge.");
		} else if (aGlycosidicOrientation.equals("trans")
				|| aGlycosidicOrientation.equals("t")) {
			if (anEdge1.getEdgeLabel().equals("W")
					&& anEdge2.getEdgeLabel().equals("W")) {
				return "parallel";
			} else if ((anEdge1.getEdgeLabel().equals("W") && anEdge2
					.getEdgeLabel().equals("H"))
					|| (anEdge1.getEdgeLabel().equals("H") && anEdge2
							.getEdgeLabel().equals("W"))) {
				return "antiparallel";
			} else if ((anEdge1.getEdgeLabel().equals("W") && anEdge2
					.getEdgeLabel().equals("S"))
					|| (anEdge1.getEdgeLabel().equals("S") && anEdge2
							.getEdgeLabel().equals("W"))) {
				return "parallel";
			} else if ((anEdge1.getEdgeLabel().equals("H") && anEdge2
					.getEdgeLabel().equals("W"))
					|| (anEdge1.getEdgeLabel().equals("W") && anEdge2
							.getEdgeLabel().equals("H"))) {
				return "antiparallel";
			} else if (anEdge1.getEdgeLabel().equals("H")
					&& anEdge2.getEdgeLabel().equals("H")) {
				return "parallel";
			} else if ((anEdge1.getEdgeLabel().equals("H") && anEdge2
					.getEdgeLabel().equals("S"))
					|| (anEdge1.getEdgeLabel().equals("S") && anEdge2
							.getEdgeLabel().equals("H"))) {
				return "antiparallel";
			} else if ((anEdge1.getEdgeLabel().equals("S") && anEdge2
					.getEdgeLabel().equals("W"))
					|| (anEdge1.getEdgeLabel().equals("W") && anEdge2
							.getEdgeLabel().equals("S"))) {
				return "parallel";
			} else if ((anEdge1.getEdgeLabel().equals("S") && anEdge2
					.getEdgeLabel().equals("H"))
					|| (anEdge1.getEdgeLabel().equals("H") && anEdge2
							.getEdgeLabel().equals("S"))) {
				return "antiparallel";
			} else if (anEdge1.getEdgeLabel().equals("S")
					&& anEdge2.getEdgeLabel().equals("S")) {
				return "parallel";
			}
			throw new InvalidEdgeException(
					"The provided edge is not of any currently known edge.");
		}
		throw new InvalidGlycosidicOrientationException(
				"The provided glycosidic orientation is not recognized.");
	}
}
