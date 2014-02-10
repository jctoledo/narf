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
package org.semanticscience.narf.graphs.nucleicacid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org._3pq.jgrapht.graph.SimpleGraph;
import org.jgrapht.alg.NeighborIndex;
import org.openscience.cdk.annotations.TestClass;
import org.openscience.cdk.ringsearch.cyclebasis.CycleBasis;
import org.openscience.cdk.ringsearch.cyclebasis.SimpleCycle;
import org.semanticscience.narf.graphs.lib.cycles.Cycle;
import org.semanticscience.narf.graphs.lib.cycles.CycleHelper;
import org.semanticscience.narf.graphs.lib.cycles.exceptions.CycleException;
import org.semanticscience.narf.structures.interactions.BasePair;
import org.semanticscience.narf.structures.interactions.BaseStack;
import org.semanticscience.narf.structures.interactions.NucleotideInteraction;
import org.semanticscience.narf.structures.interactions.PhosphodiesterBond;
import org.semanticscience.narf.structures.parts.Edge;
import org.semanticscience.narf.structures.parts.Nucleotide;
import org.semanticscience.narf.structures.parts.Sequence;
import org.semanticscience.narf.structures.secondary.SecondaryStructure;

/**
 * Representation of a nucleic acid as an undirected graph that has no loops and
 * at most one edge between any two vertices. The vertices represent nucleotide
 * residues and the edges represent interaction(s) between two nucleotide
 * residues. One Nucleic Acid object will represent one model of a structure in
 * the case of a multi-module structure
 * 
 * @author "Jose Cruz-Toledo"
 * @since 1.6
 */

public class NucleicAcid extends AbstractNucleicAcid {

	private static final long serialVersionUID = 2692448232910041821L;

	/**
	 * Model number of a nucleic acid conformer. Multiple possible conformations
	 * can occur from one structure. This number should correspond to either the
	 * model number in an NMR ensemble or a unique integer for identifying a
	 * predicted nucleic acid from an ensemble of predictions.
	 */
	public int modelNumber;

	/**
	 * A set of all the interactions found in the graph.
	 */
	private Set<NucleotideInteraction> interactions;

	/**
	 * A mapping of the chain identifier to a sequence object representing the
	 * chain.
	 */
	private Map<String, Sequence> chain2SequenceMap;

	/**
	 * A mapping of the chain identifier to the set of interactions in and on
	 * the chain. Each set of interactions for a chain contains intra- and
	 * inter-chain interactions.
	 */
	private Map<String, Set<NucleotideInteraction>> chain2InteractionMap;
	/**
	 * The minimum cycle basis computed for this nucleic acid Uses the CDK
	 * ringsearch implementation of Horton 1984
	 */
	private List<Cycle<Nucleotide, InteractionEdge>> minimumCycleBasis;
	/**
	 * A map where the key is a nucleotide and the value is a list
	 * of cycles that have the given key as a vertex
	 */
	private HashMap<Nucleotide, List<Cycle<Nucleotide, InteractionEdge>>> mcbVertexMap = new HashMap<Nucleotide, List<Cycle<Nucleotide, InteractionEdge>>>();

	/**
	 * Construct a nucleic acid using a mapping of chains to their respective
	 * sequences and a set of interactions that occur in the nucleic acid. The
	 * minimum cyclic motif graph is also constructed to find all the structural
	 * motifs in the graph.
	 * 
	 * @param aModelNumber
	 *            the model number of this structural conformation
	 * @param aSequenceMap
	 *            a mapping of the chain to its respective sequence
	 * @param someInteractions
	 *            a set of interactions present in the nucleic acid
	 * @throws InvalidPhosphodiesterBondException
	 * @since 1.6
	 */
	public NucleicAcid(int aModelNumber, Map<String, Sequence> aSequenceMap,
			Set<NucleotideInteraction> someInteractions) {
		this.modelNumber = aModelNumber;
		this.chain2SequenceMap = aSequenceMap;
		this.interactions = someInteractions;
		this.chain2InteractionMap = makeChain2InteractionMap(aSequenceMap,
				someInteractions);
		this.populateNucleicAcid();
		// now compute the MCB
		this.minimumCycleBasis = this.computeMCB();

	}

	/**
	 * Constructed a nucleic acid from its secondary structure. The secondary
	 * structure will only contain {@link BasePair} and
	 * {@link PhosphodiesterBond} interactions.
	 * 
	 * @param aSecondaryStructure
	 *            the secondary structure
	 * @throws InvalidPhosphodiesterBondException
	 * @since 1.6
	 */
	public NucleicAcid(SecondaryStructure aSecondaryStructure) {
		this(1, aSecondaryStructure.getSequenceMap(), aSecondaryStructure
				.getInteractions());
	}

	@SuppressWarnings("unchecked")
	private List<Cycle<Nucleotide, InteractionEdge>> computeMCB() {
		List<Cycle<Nucleotide, InteractionEdge>> rm = new ArrayList<Cycle<Nucleotide, InteractionEdge>>();
		try {
			// create a org._3pq.jgrapht.graph.SimpleGraph representation of
			// this
			SimpleGraph sg = this.makeSimpleGraph();
			// compute mcb
			CycleBasis cb = new CycleBasis(sg);
			List<SimpleCycle> cycles = (List<SimpleCycle>) cb.cycles();
			// iterate over the org._3pq.jgrapht.graph.SimpleCycle list and
			// convert them
			if (cycles.size() == 0 || cycles == null) {
				throw new CycleException("Empty cycle basis!");
			}
			for (SimpleCycle sc : cycles) {
				List<InteractionEdge> el = new ArrayList<InteractionEdge>();
				Iterator<org._3pq.jgrapht.Edge> sce_itr = sc.edgeSet()
						.iterator();
				while (sce_itr.hasNext()) {
					org._3pq.jgrapht.Edge anEdge = sce_itr.next();
					Nucleotide source = (Nucleotide) anEdge.getSource();
					Nucleotide target = (Nucleotide) anEdge.getTarget();
					Set<InteractionEdge> x = this.getAllEdges(source, target);
					el.addAll(x);
				}
				el = CycleHelper.sortEdgeList(el);
				Nucleotide fv = CycleHelper.findFirstNucleotide(el);
				Nucleotide lv = CycleHelper.findLastNucleotide(el);
				// now create a cycle
				Cycle<Nucleotide, InteractionEdge> c = new Cycle<Nucleotide, InteractionEdge>(
						this, fv, lv, el, el.size());
				// add to the mcbVertexMap
				// iterate over every vertex in c and add its neighbours to the
				// adjacency matrix
				List<Nucleotide> vertices = c.getVertexList();
				HashMap<Nucleotide, List<Cycle<Nucleotide, InteractionEdge>>> hm = this
						.getMcbVertexMap();
				for (Nucleotide aNuc : vertices) {
					if (hm.containsKey(aNuc)) {
						List<Cycle<Nucleotide, InteractionEdge>> ac = hm
								.get(aNuc);
						//check if this current cycle (c) is in the ac list
						if (!ac.contains(c)){
							ac.add(c);
							this.getMcbVertexMap().put(aNuc, ac);
						}
					}else{
						List<Cycle<Nucleotide,InteractionEdge>> p = new ArrayList<Cycle<Nucleotide,InteractionEdge>>();
						p.add(c);
						this.getMcbVertexMap().put(aNuc, p);
					}
				}
				rm.add(c);

			}
		} catch (CycleException e) {
			e.printStackTrace();
		}
		return rm;
	}

	/**
	 * Populate the nucleic acid with the nucleotides and interactions present
	 * in the molecule.
	 * 
	 * @since 1.6
	 */
	private void populateNucleicAcid() {
		// add all nucleotides
		// populate the nucleotides first in the graph
		for (String chain : chain2SequenceMap.keySet()) {
			for (Nucleotide n : chain2SequenceMap.get(chain)) {
				this.addVertex(n);
			}
		}

		// add the interactions to the graph
		for (NucleotideInteraction anInter : this.getInteractions()) {
			// get the participating nucleotides
			Nucleotide n1 = anInter.getFirstNucleotide();
			Nucleotide n2 = anInter.getSecondNucleotide();

			String n1Chain = this.getChainIdentifier(anInter
					.getFirstNucleotide());
			String n2Chain = this.getChainIdentifier(anInter
					.getSecondNucleotide());
			/*
			 * check who is "first" or "second" Make sure the first and second
			 * nucleotide of the interaction are the first and second
			 * nucleotide. The order is dependent on whether the chain
			 * identifier of the first nucleotide precedes the chain identifier
			 * of the second nucleotide alphanumerically or the residue position
			 * of the first nucleotide precedes the residue position of the
			 * second nucleotide and the chain.
			 */
			if ((n1Chain.compareTo(n2Chain) == 0)
					&& (anInter.getFirstNucleotide().getResiduePosition() > anInter
							.getSecondNucleotide().getResiduePosition())) {
				n1 = anInter.getSecondNucleotide();
				n2 = anInter.getFirstNucleotide();
			} else if ((n1Chain.compareTo(n2Chain) > 0)) {
				n1 = anInter.getSecondNucleotide();
				n2 = anInter.getFirstNucleotide();
			}

			/*
			 * Keep track of all interaction Add the interaction to the
			 * interaction edge if it exists or create one otherwise
			 */
			Set<NucleotideInteraction> interactions = new HashSet<NucleotideInteraction>();
			// check if these two nucleotides have other interactions already
			if (this.containsEdge(n1, n2)) {
				interactions.addAll(this.getEdge(n1, n2).getInteractions());
			}
			interactions.add(anInter);

			// create Interaction an interaction edge
			// a higher weight edge is given for interactions that stay within a
			// chain
			InteractionEdge edge = new InteractionEdge(interactions);
			double edgeWeight = 0;
			if (!this.getChainIdentifier(edge.getFirstNucleotide()).equals(
					this.getChainIdentifier(edge.getSecondNucleotide()))) {
				edgeWeight = this.getSequence(
						this.getChainIdentifier(edge.getFirstNucleotide()))
						.getLength()
						+ this.getSequence(
								this.getChainIdentifier(edge
										.getSecondNucleotide())).getLength();
			} else {
				edgeWeight = Math.abs(edge.getSecondNucleotide()
						.getResiduePosition()
						- edge.getFirstNucleotide().getResiduePosition());
			}
			this.removeEdge(this.getEdge(n1, n2));
			this.addEdge(n1, n2, edge);
			this.setEdgeWeight(edge, edgeWeight);

		}//
	}

	/**
	 * Create a mapping of the chain identifiers to a set of the interactions
	 * that occur in and on the respective chain.
	 * 
	 * @param aSm
	 *            a mapping of the chain identifier to the sequence of the chain
	 * @param someInts
	 *            a set of all the interactions in the nucleic acid
	 * @return a mapping of all the chain identifier to a set of interactions
	 *         that occur in and on the respective chain.
	 * @since 1.6
	 */
	private Map<String, Set<NucleotideInteraction>> makeChain2InteractionMap(
			Map<String, Sequence> aSm, Set<NucleotideInteraction> someInts) {
		Map<String, Set<NucleotideInteraction>> returnMe = new HashMap<String, Set<NucleotideInteraction>>();

		/*
		 * find which chains that each nucleotide in an interaction occur and
		 * add them to the chain
		 */
		for (NucleotideInteraction anInt : someInts) {
			Nucleotide n1 = anInt.getFirstNucleotide();
			Nucleotide n2 = anInt.getSecondNucleotide();
			for (String chain : aSm.keySet()) {
				if (!returnMe.containsKey(chain)) {
					returnMe.put(chain,
							new LinkedHashSet<NucleotideInteraction>());
				}
				if (aSm.get(chain).containsNucleotide(n1)) {
					returnMe.get(chain).add(anInt);
				}
				if (aSm.get(chain).containsNucleotide(n2)) {
					returnMe.get(chain).add(anInt);
				}
			}
		}
		return returnMe;
	}

	/**
	 * Serialize a Nucleic acid as a cdk SimpleGraph
	 * 
	 * @return
	 */
	@TestClass("org.semanticscience.narf.graphs.nucleicacid.NucleicAcidTest")
	private SimpleGraph makeSimpleGraph() {
		SimpleGraph rm = new SimpleGraph();
		// add all the vertices to rm
		for (Nucleotide aNuc : this.vertexSet()) {
			rm.addVertex(aNuc);
		}
		// add all of the edges to rm
		for (InteractionEdge anIntE : this.edgeSet()) {
			for (NucleotideInteraction aNucI : anIntE.getInteractions()) {
				rm.addEdge(aNucI.getFirstNucleotide(),
						aNucI.getSecondNucleotide());
			}
		}
		return rm;
	}

	/**
	 * Get a unmodifiable set of interactions for a given chain. The returned
	 * set contains both intra- and inter-chain interactions.
	 * 
	 * @param aChain
	 *            the chain identifier
	 * @return a set of interactions for the given chain, or empty set if the
	 *         chain does not exist in the nucleic acid
	 * @since 1.6
	 */
	public Set<NucleotideInteraction> getInteractionByChain(String aChain) {
		if (!chain2InteractionMap.containsKey(aChain)) {
			return new LinkedHashSet<NucleotideInteraction>();
		}
		return Collections.unmodifiableSet(chain2InteractionMap.get(aChain));
	}

	/**
	 * Get the entire set of interactions that occur in this nucleic acid.
	 * 
	 * @return a set of interactions
	 * @since 1.6
	 */
	public Set<NucleotideInteraction> getInteractions() {
		return Collections.unmodifiableSet(interactions);
	}

	/**
	 * Get a set of all base pairs in this nucleic acid
	 * 
	 * @return a set of base pairs
	 */
	public Set<BasePair> getBasePairs() {
		Set<BasePair> returnMe = new HashSet<BasePair>();
		Iterator<NucleotideInteraction> itr = interactions.iterator();
		while (itr.hasNext()) {
			NucleotideInteraction anInt = itr.next();
			if (anInt instanceof BasePair) {
				returnMe.add((BasePair) anInt);
			}
		}
		return Collections.unmodifiableSet(returnMe);
	}

	/**
	 * Get a set of all base stacks in this nucleic acid
	 * 
	 * @return a set of base stacks
	 */
	public Set<BaseStack> getBaseStacks() {
		Set<BaseStack> returnMe = new HashSet<BaseStack>();
		Iterator<NucleotideInteraction> itr = interactions.iterator();
		while (itr.hasNext()) {
			NucleotideInteraction anInt = itr.next();
			if (anInt instanceof BaseStack) {
				returnMe.add((BaseStack) anInt);
			}
		}
		return Collections.unmodifiableSet(returnMe);
	}

	/**
	 * Get a set of all phosphodiester linkages in this nucleic acid
	 * 
	 * @return a set of phosphodiester linkages
	 */
	public Set<PhosphodiesterBond> getPhosphodiesterBonds() {
		Set<PhosphodiesterBond> returnMe = new HashSet<PhosphodiesterBond>();
		Iterator<NucleotideInteraction> itr = interactions.iterator();
		while (itr.hasNext()) {
			NucleotideInteraction anInt = itr.next();
			if (anInt instanceof PhosphodiesterBond) {
				returnMe.add((PhosphodiesterBond) anInt);
			}
		}
		return Collections.unmodifiableSet(returnMe);
	}

	/**
	 * Get the model number of this nucleic acid conformer
	 * 
	 * @return the model number
	 * @since 1.6
	 */
	public int getModelNumber() {
		return modelNumber;
	}

	/**
	 * Get the sequence of a given chain.
	 * 
	 * @param aChain
	 *            the chain identifier
	 * @return the sequence of a chain, or <code>null</code> if the chain does
	 *         not exist in the nucleic acid
	 * @since 1.6
	 */
	public Sequence getSequence(String aChain) {
		return chain2SequenceMap.get(aChain);
	}

	/**
	 * Get the chain identifier of a given nucleotide
	 * 
	 * @param nucleotide
	 *            the nucleotide
	 * @return the chain identifier of the given nucleotide, or
	 *         <code>null</code> if the chain does not exist in the nucleic acid
	 * @since 1.6
	 */
	public String getChainIdentifier(Nucleotide nucleotide) {

		for (String chain : chain2SequenceMap.keySet()) {
			if (chain2SequenceMap.get(chain).containsNucleotide(nucleotide)) {
				return chain;
			}
		}

		return null;
	}

	/**
	 * Get the minimum cycle basis of this graph
	 * 
	 * @return the MCB as computed by CDK's ringsearch
	 */
	public List<Cycle<Nucleotide, InteractionEdge>> getMinimumCycleBasis() {
		return this.minimumCycleBasis;
	}

	/**
	 * Get a set of the chain identifiers present in the nucleic acid.
	 * 
	 * @return a set of chain identifiers
	 * @since 1.6
	 */
	public Set<String> getChainIdentifiers() {
		return chain2SequenceMap.keySet();
	}

	/**
	 * Determine if a chain exists in the nucleic acid
	 * 
	 * @param aChain
	 *            chain identifier
	 * @return <code>true</code> if the chain exists in the nucleic acid,
	 *         otherwise <code>false</code>
	 * @since 1.6
	 */
	public boolean containsChain(String aChain) {
		return chain2SequenceMap.containsKey(aChain);
	}

	/**
	 * Retrieve a set with all of the chains for this nucleic acid
	 * 
	 * @return a set with all of the chains
	 */
	public String[] getChains() {
		String[] returnMe = new String[chain2SequenceMap.size()];
		Set<String> s = chain2SequenceMap.keySet();
		int c = 0;
		for (String str : s) {
			returnMe[c] = str;
			c++;
		}

		return returnMe;
	}

	@Override
	public String toString() {
		String buf = "NucleicAcid : modelNumber=" + modelNumber
				+ " number of chains=" + this.getChainIdentifiers().size()
				+ "\n";
		// iterate over each chain
		for (String aC : this.chain2SequenceMap.keySet()) {
			buf += " Sequence for chain: " + aC + "\n";
			buf += chain2SequenceMap.get(aC).getSequenceString() + "\n";
			buf += " Sequence length:"
					+ chain2SequenceMap.get(aC).getSequenceString().length()
					+ "\n";
			buf += " residue details:\n"
					+ chain2SequenceMap.get(aC).getNucleotides().toString()
					+ "\n";
			buf += " interaction details:\n"
					+ this.getInteractions().toString() + "\n";
		}

		return buf;
	}

	/*
	 * public CyclicMotifGraph getCyclicMotifGraph() { return cyclicMotifGraph;
	 * }
	 */

	public String getUniqueIdentifier() {
		return String.valueOf(this.hashCode());
	}

	/**
	 *
	 * A map where the key is a nucleotide and the value is a list
	 * of cycles that have the given key as a vertex
	 *
	 * @return the mcbVertexMap
	 */
	public HashMap<Nucleotide, List<Cycle<Nucleotide, InteractionEdge>>> getMcbVertexMap() {
		return mcbVertexMap;
	}

	/**
	 * @param mcbVertexMap the mcbVertexMap to set
	 */
	private void setMcbVertexMap(
			HashMap<Nucleotide, List<Cycle<Nucleotide, InteractionEdge>>> mcbVertexMap) {
		this.mcbVertexMap = mcbVertexMap;
	}

	

}
