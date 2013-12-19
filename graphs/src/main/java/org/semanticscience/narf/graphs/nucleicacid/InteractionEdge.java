/**
 * Copyright (c) 2013 by William Greenwood and Jose Cruz-Toledo
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
package org.semanticscience.narf.graphs.nucleicacid;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.semanticscience.narf.structures.interactions.BasePair;
import org.semanticscience.narf.structures.interactions.BaseStack;
import org.semanticscience.narf.structures.interactions.NucleotideInteraction;
import org.semanticscience.narf.structures.interactions.PhosphodiesterBond;
import org.semanticscience.narf.structures.parts.Nucleotide;

/**
 * A graph edge representing the interactions that can occur between two
 * nucleotides. More than two interactions should not be possible between two
 * nucleotides.
 * 
 * @author William Greenwood
 * @author Jose Cruz-Toledo
 * 
 */
public class InteractionEdge extends DefaultWeightedEdge {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1303032312640087015L;
	/**
	 * All interactions encapsulated in the edge.
	 */
	private Set<NucleotideInteraction> interactions;
	/**
	 * The first nucleotide residue participating in the interaction. The first
	 * nucleotide is the nucleotide whose residue position comes first
	 * numerically in the polymer (chain) or whose polymer (chain) identifier is
	 * alphanumerically before the second nucleotide.
	 */
	private Nucleotide firstNucleotide;
	/**
	 * The second nucleotide residue participating in the interaction. The
	 * second nucleotide is the nucleotide whose residue position comes second
	 * numerically in the polymer (chain) or whose polymer (chain) identifier is
	 * alphanumerically after the first nucleotide.
	 */
	private Nucleotide secondNucleotide;

	/**
	 * Create an interaction edge that encapsulates zero interactions
	 */
	public InteractionEdge() {
		interactions = new HashSet<NucleotideInteraction>();
	}

	/**
	 * Create an interaction edge that encapsulates one interaction
	 * 
	 * @param anInt
	 *            An interaction to be used to initialize this interaction edge.
	 */
	public InteractionEdge(NucleotideInteraction anInt) {
		this();
		firstNucleotide = anInt.getFirstNucleotide();
		secondNucleotide = anInt.getSecondNucleotide();
		this.addInteraction(anInt);
	}

	/**
	 * Create an interaction form a set of NucleotideInteractions
	 * 
	 * @param interactions
	 */
	public InteractionEdge(Set<NucleotideInteraction> interactions) {
		this();
		for (NucleotideInteraction i : interactions) {
			if (firstNucleotide == null) {
				firstNucleotide = i.getFirstNucleotide();
			}

			if (secondNucleotide == null) {
				secondNucleotide = i.getSecondNucleotide();
			}

			if (i == null) {
				continue;
			}
			this.addInteraction(i);
		}
	}

	/**
	 * Add an interaction to this edge. If the edge already contains the
	 * interaction it returns <code>false</code>.
	 * 
	 * @param anInt
	 *            the interaction to be added to the interaction edge
	 * @return <code>true</code> if the interaction was successfully added
	 */
	private boolean addInteraction(NucleotideInteraction anInt) {
		if (anInt == null) {
			return false;
		}
		// check if the parameter interaction is the same as the interaction
		// used to initialize the object
		if (firstNucleotide.equals(anInt.getFirstNucleotide())
				&& secondNucleotide.equals(anInt.getSecondNucleotide())) {
			return this.interactions.add(anInt);
		}
		return false;
	}

	/**
	 * Get the set of <code>NucleotideInteraction</code>s that are encapsulated
	 * by the interaction edge
	 * 
	 * @return an unmodifiable set of <code>NucleotideInteraction</code> objects
	 *         corresponding to this interaction edge
	 */
	public Set<NucleotideInteraction> getInteractions() {
		return Collections.unmodifiableSet(this.interactions);
	}

	/**
	 * Get a string representation of the BasePair edges that form part of this
	 * list of interaction edges
	 * 
	 * @return a string with the names of the base pair classes that form part
	 *         of this set of interactions
	 */
	public String extractBasePairClasses() {
		String returnMe = "";
		for (NucleotideInteraction i : this.interactions) {
			if (i.getClass().equals(BasePair.class)) {
				BasePair bp = (BasePair) i;
				if (bp.getFirstEdge() != null && bp.getFirstEdge() != null) {
					if (bp.getFirstEdge().toString() != ""
							&& bp.getSecondEdge().toString() != "") {
						returnMe += bp.getGlycosidicOrientation()
								+ bp.getFirstEdge() + "/" + bp.getSecondEdge();
					}
				}
			}
		}
		return returnMe;
	}

	/**
	 * Get a string representation of all edges that form part of this list of
	 * interaction edges
	 * 
	 * @return the labels of the edges. eg.: L-P-L-P
	 */
	public String extractEdgeClasses() {
		String link = "";
		String bp = "";
		String bs = "";

		for (NucleotideInteraction interaction : interactions) {
			if (interaction.getClass().equals(PhosphodiesterBond.class)) {
				link += "L";
			} else if (interaction.getClass().equals(BasePair.class)) {
				bp += "P";
			} else if (interaction.getClass().equals(BaseStack.class)) {
				bs += "S";
			}
		}

		return link + bp + bs;
	}

	/**
	 * Determine if the edge has a specific class of interaction
	 * 
	 * @param anInteractionClass
	 *            the interaction class
	 * @return <code>true</code> if the interaction class exists in the
	 *         interaction edge or a sub type of it, otherwise
	 *         <code>false</code>.
	 * @since 1.6
	 */
	public boolean hasClassOfInteraction(
			Class<? extends NucleotideInteraction> anInteractionClass) {

		for (NucleotideInteraction interaction : this.getInteractions()) {
			if (interaction.getClass().equals(anInteractionClass)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String toString() {
		return interactions.toString();
	}

	/**
	 * @return the firstNucleotide
	 */
	public Nucleotide getFirstNucleotide() {
		return firstNucleotide;
	}

	/**
	 * @return the secondNucleotide
	 */
	public Nucleotide getSecondNucleotide() {
		return secondNucleotide;
	}

}
