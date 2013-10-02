/**
 * Copyright (c) 2013  Jose Cruz-Toledo 
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
package org.semanticscience.narf.graphs.lib.cycles;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.alg.KruskalMinimumSpanningTree;
import org.jgrapht.graph.SimpleGraph;
import org.semanticscience.narf.graphs.lib.cycles.exceptions.CycleException;
import org.semanticscience.narf.graphs.nucleicacid.NucleicAcid;

/**
 * @author Jose Cruz-Toledo
 */
public class FundamentalCycleBasis<V, E> extends CycleBasis<V, E> {
	/**
	 * A map where the key is a vertex and the value is a list of cycles to
	 * which that vertex is part of
	 */
	private HashMap<V, List<Cycle<V, E>>> cyclesMap;

	/**
	 * The minimum spanning tree of the base graph represented as an undirected
	 * graph
	 */
	private UndirectedGraph<V, E> minimumSpanningTreeGraph;

	/**
	 * @param na
	 */
	public FundamentalCycleBasis(NucleicAcid na) {
		super(na);
		cyclesMap = initializeCycleMap();
		minimumSpanningTreeGraph = this.retrieveKruskalMSTGraph();
		cycleBasis = computeCycleBasis(minimumSpanningTreeGraph);

	}

	public FundamentalCycleBasis(UndirectedGraph<V, E> aG) {
		super(aG);
		cyclesMap = initializeCycleMap();
		minimumSpanningTreeGraph = this.retrieveKruskalMSTGraph();
		cycleBasis = compFundamentalCycleBasis(minimumSpanningTreeGraph);
	}

	/**
	 * Compute the fundamental cycle basis of this graph
	 * @param anMSTGraph a minimum spanning tree of the graph
	 * @return the list of fundamental cycles that describe the graph
	 */
	private List<Cycle<V, E>> compFundamentalCycleBasis(
			UndirectedGraph<V, E> anMSTGraph) {
		List<Cycle<V, E>> rm = new LinkedList<Cycle<V, E>>();
		// find all the edges that are not in the mst
		//and store it in a list
		List<E> notInMST = new LinkedList<E>();
		for (E anEdge : this.getBaseGraph().edgeSet()) {
			if (!anMSTGraph.containsEdge(anEdge)) {
				notInMST.add(anEdge);
			}
		}
		for (E aMissingEdge : notInMST) {
			Cycle<V, E> cycle_to_split = null;
			try {
				cycle_to_split = this.intersect(aMissingEdge);
				V s = this.getBaseGraph().getEdgeSource(aMissingEdge);
				V t = this.getBaseGraph().getEdgeTarget(aMissingEdge);
				if (cycle_to_split == null) {
					// find the shortest path between the two vertices that do
					// not
					// include aMissingEdge
					if (!s.equals(t)) {
						List<E> edgeList = DijkstraShortestPath
								.findPathBetween(anMSTGraph, s, t);
						// add the missing edge to complete the shortest cycle
						edgeList.add(aMissingEdge);
						// create a cycle object given the base graph, source,
						// target
						// the edge list and their size as weight
						Cycle<V, E> p = null;
						try {
							p = new Cycle<V, E>(this.getBaseGraph(), s, t,
									edgeList, edgeList.size());
							List<V> vl = p.getVertexList();
							for (V aVertex : vl) {
								// add this new cycle to the cycles map
								// make sure that this cycle is not in the map
								List<Cycle<V, E>> cl = this.getCyclesMap().get(
										aVertex);
								boolean b = false;
								for (Cycle<V, E> cycle : cl) {
									if (cycle.equals(p)) {
										b = true;
										break;
									}
								}
								if (!b) {
									this.getCyclesMap().get(aVertex).add(p);
								}
							}
						} catch (CycleException e) {
							e.printStackTrace();
						}

					}
				} else {
					List<Cycle<V, E>> newCycles = null;
					try {
						newCycles = this.cycleSplitter(cycle_to_split,
								aMissingEdge);
					} catch (CycleException e) {
						e.printStackTrace();
					}
					Cycle<V, E> c1 = null;
					Cycle<V, E> c2 = null;
					if (newCycles != null && newCycles.size() == 2) {
						c1 = newCycles.get(0);
						c2 = newCycles.get(1);
						for (V aVertex : cycle_to_split.getVertexList()) {
							// remove a vertex from cyclesmap
							this.getCyclesMap().get(aVertex)
									.remove(cycle_to_split);

							if (c1.getVertexList().contains(aVertex)
									&& !this.getCyclesMap().get(aVertex)
											.contains(c1)) {
								this.getCyclesMap().get(aVertex).add(c1);
							}
							if (c2.getVertexList().contains(aVertex)
									&& !this.getCyclesMap().get(aVertex)
											.contains(c2)) {
								this.getCyclesMap().get(aVertex).add(c2);
							}// if
						}// for
					}// if
				}// else
					// anMSTGraph.addEdge(s, t, aMissingEdge);
			} catch (CycleException e) {
				e.printStackTrace();
			}
		}// for
			// read cyclesMap and populate the cycle basis
		for (V v : this.getCyclesMap().keySet()) {
			for (Cycle<V, E> c : this.getCyclesMap().get(v)) {
				if (!rm.contains(c)) {
					rm.add(c);
				}
			}
		}
		return rm;
	}

	private List<Cycle<V, E>> computeCycleBasis(UndirectedGraph<V, E> anMSTGraph) {
		List<Cycle<V, E>> rm = new LinkedList<Cycle<V, E>>();
		// find all the edges that are not in the mst
		List<E> notInMST = new LinkedList<E>();
		for (E anEdge : this.getBaseGraph().edgeSet()) {
			if (!anMSTGraph.containsEdge(anEdge)) {
				notInMST.add(anEdge);
			}
		}
		for (E aMissingEdge : notInMST) {
			// Find the set of all cycles (at most 2) that have aMissingEdge in
			// them
			List<Cycle<V, E>> split_these = null;
			try {
				split_these = this.intersectCycleByEdge(aMissingEdge);
				if (split_these.size() == 0) {
					// find the shortest path between the two vertices that do
					// not
					// include aMissingEdge
					V s = this.getBaseGraph().getEdgeSource(aMissingEdge);
					V t = this.getBaseGraph().getEdgeTarget(aMissingEdge);
					if (!s.equals(t)) {
						List<E> edgeList = DijkstraShortestPath
								.findPathBetween(anMSTGraph, s, t);

						// add the missing edge to complete the shortest cycle
						edgeList.add(aMissingEdge);
						// create a cycle object given the base graph, source,
						// target
						// the edge list and their size as weight
						Cycle<V, E> p = null;
						try {
							p = new Cycle<V, E>(this.getBaseGraph(), s, t,
									edgeList, edgeList.size());
							for (V aVertex : Graphs.getPathVertexList(p)) {
								// add this new cycle to the cycles map
								// make sure that this cycle is not in the map
								List<Cycle<V, E>> cl = this.getCyclesMap().get(
										aVertex);
								boolean b = false;
								for (Cycle<V, E> cycle : cl) {
									if (cycle.equals(p)) {
										b = true;
										break;
									}
								}
								if (!b) {
									this.getCyclesMap().get(aVertex).add(p);
								}
							}
						} catch (CycleException e) {
							e.printStackTrace();
						}
					}// if
				} else {
					// if aMissingEdge is part-of two cycles
					// get two cycles from the adjacent edge
					for (Cycle<V, E> split_me : split_these) {
						List<Cycle<V, E>> newCycles = null;
						try {
							newCycles = this.cycleSplitter(split_me,
									aMissingEdge);
						} catch (CycleException e) {
							e.printStackTrace();
						}
						Cycle<V, E> c1 = null;
						Cycle<V, E> c2 = null;
						if (newCycles != null && newCycles.size() == 2) {
							c1 = newCycles.get(0);
							c2 = newCycles.get(1);
							for (V aVertex : split_me.getVertexList()) {
								// remove a vertex from cyclesmap
								this.getCyclesMap().get(aVertex)
										.remove(split_me);
								if (Graphs.getPathVertexList(c1).contains(
										aVertex)
										&& !this.getCyclesMap().get(aVertex)
												.contains(c1)) {
									this.getCyclesMap().get(aVertex).add(c1);
								}
								if (Graphs.getPathVertexList(c2).contains(
										aVertex)
										&& !this.getCyclesMap().get(aVertex)
												.contains(c2)) {
									this.getCyclesMap().get(aVertex).add(c2);
								}// if
							}// for
						}// if
					}// for
				}// else
			}// try
			catch (CycleException e) {
				e.printStackTrace();
			}
		}// for
			// read cyclesMap and populate the cycle basis
		for (V v : this.getCyclesMap().keySet()) {
			for (Cycle<V, E> c : this.getCyclesMap().get(v)) {
				rm.add(c);
			}
		}
		return rm;
	}

	/**
	 * Find the list of cycles that share anEdge from this.getCylesMap()
	 * 
	 * @return the list of cycles that share anEdge
	 * @throws CycleException
	 */
	private List<Cycle<V, E>> intersectCycleByEdge(E anEdge)
			throws CycleException {
		List<Cycle<V, E>> rm = new LinkedList<Cycle<V, E>>();
		// iterate over the vertexlist
		V v1 = this.getBaseGraph().getEdgeSource(anEdge);
		V v2 = this.getBaseGraph().getEdgeTarget(anEdge);
		if (v1.equals(v2)) {
			throw new CycleException("Invalid Edge!");
		}
		for (Cycle<V, E> c1 : this.getCyclesMap().get(v1)) {
			for (Cycle<V, E> c2 : this.getCyclesMap().get(v2)) {
				if (c1.equals(c2)) {
					rm.add(c1);
				}
			}
		}
		return rm;
	}
	
	private Cycle<V, E> intersect(E anEdge) throws CycleException {
		V v1 = this.getBaseGraph().getEdgeSource(anEdge);
		V v2 = this.getBaseGraph().getEdgeTarget(anEdge);
		if (v1.equals(v2)) {
			throw new CycleException("Invalid Edge!");
		}
		for (Cycle<V, E> c1 : this.getCyclesMap().get(v1)) {
			for (Cycle<V, E> c2 : this.getCyclesMap().get(v2)) {
				if (c1.equals(c2)) {
					return c1;
				}
			}
		}
		return null;
	}

	/**
	 * Split aCycleToSplit given an Edge (anEdgeNotinMST).
	 * 
	 * @param aCycleToSplit
	 *            a cycle that can be split into two sub cycles
	 * @param splitEdge
	 *            the edge that would define two new cycles
	 * @return a set of cycles containing the two newly created cycles
	 * @throws CycleException
	 */
	public List<Cycle<V, E>> cycleSplitter(Cycle<V, E> aCycleToSplit,
			E splitEdge) throws CycleException {
		V newStart = aCycleToSplit.getGraph().getEdgeSource(splitEdge);
		V splitEdgeTarget = this.getBaseGraph().getEdgeTarget(splitEdge);
		if (splitEdgeTarget.equals(newStart)) {
			throw new CycleException("Invalid split edge!");
		}
		// find the incoming edge to newStart
		E incE = aCycleToSplit.getIncomingEdge(newStart);
		// now I can get the new end
		V newEnd = Graphs.getOppositeVertex(aCycleToSplit.getGraph(), incE,
				newStart);
		if (!newStart.equals(aCycleToSplit.getStartVertex())
				&& !newEnd.equals(aCycleToSplit.getEndVertex())) {
			aCycleToSplit = aCycleToSplit.rotateCycle(newStart, newEnd);
		}
		List<E> c1EdgeList = new LinkedList<E>();
		List<E> c2EdgeList = new LinkedList<E>();
		V c1StartVertex = null;
		V c1EndVertex = null;
		V c2StartVertex = null;
		V c2EndVertex = null;
		V vertex = aCycleToSplit.getStartVertex();
		Iterator<E> edgeIterator = aCycleToSplit.getEdgeList().iterator();
		while (edgeIterator.hasNext()) {
			E anEdge = edgeIterator.next();
			if (newStart.equals(vertex)) {
				// then c1start is vertex
				c1StartVertex = vertex;
				c1EdgeList.add(anEdge);
				vertex = aCycleToSplit.getNextVertex(vertex);

				while (edgeIterator.hasNext()) {
					anEdge = edgeIterator.next();
					// check if I have reached the ending node
					if (splitEdgeTarget.equals(vertex)) {
						// now set the ending vertex of cycle 1
						c1EndVertex = vertex;
						break;
					}// if
					c1EdgeList.add(anEdge);
					vertex = aCycleToSplit.getNextVertex(vertex);
				}// while
					// add the last edge to cycle 1
				c1EdgeList.add(splitEdge);
				// set the starting vertex for cycle 2
				c2StartVertex = vertex;
				// add anEdge to cycle2
				c2EdgeList.add(anEdge);
				// c2EdgeList.add(anEdgeNotinMST);
				vertex = aCycleToSplit.getNextVertex(vertex);
				while (edgeIterator.hasNext()) {
					anEdge = edgeIterator.next();
					c2EdgeList.add(anEdge);
					vertex = aCycleToSplit.getNextVertex(vertex);
				}
				// set the end Vertex for c2
				c2EndVertex = vertex;
				// now add the last edge
				c2EdgeList.add(splitEdge);
				break;
			}// if
			c2EdgeList.add(anEdge);
			vertex = aCycleToSplit.getNextVertex(vertex);
		}// while
			// now create the two cycles and add them to the basis
		if (c1EdgeList.size() > 1 && c2EdgeList.size() > 1) {
			if (c1StartVertex != null && c1EndVertex != null
					&& c2StartVertex != null && c2EndVertex != null) {
				try {
					Cycle<V, E> c1 = new Cycle<V, E>(this.getBaseGraph(),
							c1StartVertex, c1EndVertex, c1EdgeList,
							c1EdgeList.size());
					Cycle<V, E> c2 = new Cycle<V, E>(this.getBaseGraph(),
							c2StartVertex, c2EndVertex, c2EdgeList,
							c2EdgeList.size());
					List<Cycle<V, E>> rm = new LinkedList<Cycle<V, E>>();
					rm.add(c1);
					rm.add(c2);
					return rm;
				} catch (CycleException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.semanticscience.narf.graphs.lib.cycles.CycleBasis#cycleBasis()
	 */
	@Override
	public List<Cycle<V, E>> getCycleBasis() {
		return this.cycleBasis;
	}

	private HashMap<V, List<Cycle<V, E>>> initializeCycleMap() {
		HashMap<V, List<Cycle<V, E>>> rm = new HashMap<V, List<Cycle<V, E>>>();
		for (V aVertex : this.getBaseGraph().vertexSet()) {
			rm.put(aVertex, new LinkedList<Cycle<V, E>>());
		}
		return rm;
	}

	/**
	 * Return a map where the key is a vertex and the value is a list of cycles
	 * where the key vertex is part of
	 * 
	 * @return a map where the key is a vertex and the value is a list of cycles
	 *         where the key vertex is part of
	 */
	private HashMap<V, List<Cycle<V, E>>> getCyclesMap() {
		return this.cyclesMap;
	}

	/**
	 * Compute the Kruskal Minimum spanning try of this.getBaseGraph() and
	 * return it as an undirected graph
	 * 
	 * @return an undirected graph representation of the minimum spanning tree
	 *         for this graph
	 * @see http
	 *      ://jgrapht.org/javadoc/org/jgrapht/alg/KruskalMinimumSpanningTree
	 *      .html
	 */
	private UndirectedGraph<V, E> retrieveKruskalMSTGraph() {
		UndirectedGraph<V, E> rm = null;
		KruskalMinimumSpanningTree<V, E> mst = new KruskalMinimumSpanningTree<V, E>(
				this.getBaseGraph());
		if (mst != null) {
			rm = new SimpleGraph<V, E>(this.getBaseGraph().getEdgeFactory());
			for (E anEdge : mst.getEdgeSet()) {
				V source = null;
				V target = null;
				try {
					source = this.getBaseGraph().getEdgeSource(anEdge);
					target = this.getBaseGraph().getEdgeTarget(anEdge);
					if (source != null && target != null) {
						if (!source.equals(target)) {
							rm.addVertex(source);
							rm.addVertex(target);
							rm.addEdge(source, target, anEdge);
						}
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}

			}
		}
		return rm;
	}
}
