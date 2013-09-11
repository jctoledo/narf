/**
 * Copyright (c) 2013  Jose Cruz-Toledo Alison Callahan and William Greenwood
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
package org.semanticscience.narf.graphs.old;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.alg.KruskalMinimumSpanningTree;
import org.jgrapht.graph.SimpleGraph;
import org.semanticscience.narf.graphs.lib.cycles.Cycle;
import org.semanticscience.narf.graphs.lib.cycles.exceptions.CycleBasisException;
import org.semanticscience.narf.graphs.lib.cycles.exceptions.CycleException;
import org.semanticscience.narf.graphs.nucleicacid.NucleicAcid;

/**
 * @author Jose Cruz-Toledo
 * @author Alison Callahan
 * @author William Greenwood
 * 
 */
public class CycleBasisOld<V, E> {
	/**
	 * The graph on which the cycle basis is computed
	 */
	private UndirectedGraph<V, E> baseGraph;
	/**
	 * A list of the vertices in this graph
	 */
	private List<V> vertexList;
	/**
	 * A list with all chordless cycles in this graph
	 */
	private List<Cycle<V, E>> chordlessCycleBasis;
	/**
	 * A map where the key is a vertex and the value is a list of cycles to
	 * which that vertex is part of
	 */
	private HashMap<V, List<Cycle<V, E>>> cyclesMap;

	private Set<Cycle<V, E>> cycleBasis;
	/**
	 * The minimum spanning tree of the base graph represented as an
	 * undirected graph
	 */
	private UndirectedGraph<V, E> minimumSpanningTreeGraph;

	@SuppressWarnings("unchecked")
	public CycleBasisOld(UndirectedGraph<V, E> aGraph) throws CycleBasisException {
		baseGraph = aGraph;
		vertexList = this.makeVertexList();
		cyclesMap = initializeCycleMap();
		minimumSpanningTreeGraph = this.retrieveKruskalMSTGraph();

		
		
		
		
		chordlessCycleBasis = new LinkedList<Cycle<V, E>>();
		

		// compute the cycle basis
		// iterate over each edge of the passed in graph
		Set<E> edges = aGraph.edgeSet();
		for (E anEdge : edges) {
			// get the source and target
			V s = aGraph.getEdgeSource(anEdge);
			V t = aGraph.getEdgeTarget(anEdge);
			V[] ls = (V[]) new Object[1];
			V[] lt = (V[]) new Object[1];
			ls[0] = s;
			lt[0] = t;
			if (s.equals(t)) {
				throw new CycleBasisException(
						"Invalid Graph! Source and target vertices are the same for edge: "
								+ anEdge.toString());
			}
			findChordlessCycles(ls);
			findChordlessCycles(lt);
		}
	}
	
	/**
	 * @return
	 */
	private UndirectedGraph<V, E> retrieveKruskalMSTGraph() {
		UndirectedGraph<V, E> rm = null;
		KruskalMinimumSpanningTree<V, E> mst = new KruskalMinimumSpanningTree<V,E>(
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

	@SuppressWarnings("unchecked")
	public CycleBasisOld(NucleicAcid na) throws CycleBasisException{
		this((UndirectedGraph<V,E>)na);
	}

	/**
	 * Return true if aV is in aVArr
	 * 
	 * @param aV
	 *            search
	 * @param aVArr
	 *            where to search
	 * @return true if aV is in aVArr
	 */
	private boolean myInArray(V aV, V[] aVArr) {
		for (int i = 0; i < aVArr.length; i++) {
			if (aV.equals(aVArr[i])) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * @param cyclesMap2
	 * @param minimumSpanningTreeGraph2
	 * @return
	 */
	/*private Set<Cycle<V, E>> computeCycleBasis(UndirectedGraph<V, E> anMSTGraph) {
		Set<Cycle<V, E>> rm = new HashSet<Cycle<V, E>>();
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
	}*/
	
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
		// if newstart will be the start of the split edge then
		// new end will be
		V newStart = aCycleToSplit.getGraph().getEdgeSource(splitEdge);
		V splitEdgeTarget = this.getBaseGraph().getEdgeTarget(splitEdge);
		V splitEdgeSource = this.getBaseGraph().getEdgeSource(splitEdge);

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
			/*
			 * if (splitEdgeTarget.equals(vertex)) { // add the edge to c1
			 * c1StartVertex = vertex; c1EdgeList.add(anEdge); vertex =
			 * Graphs.getOppositeVertex(this.getBaseGraph(), anEdge, vertex);
			 * while (edgeIterator.hasNext()) { anEdge = edgeIterator.next(); if
			 * (this.getBaseGraph().getEdgeSource(splitEdge) .equals(vertex)) {
			 * c1EndVertex = vertex; break; }// if c1EdgeList.add(anEdge);
			 * vertex = Graphs.getOppositeVertex(this.getBaseGraph(), anEdge,
			 * vertex); }// while // close c1 c1EdgeList.add(splitEdge); //
			 * start c2 c2StartVertex = vertex; c2EdgeList.add(anEdge); vertex =
			 * Graphs.getOppositeVertex(this.getBaseGraph(), anEdge, vertex);
			 * while (edgeIterator.hasNext()) { anEdge = edgeIterator.next(); if
			 * (this.getBaseGraph().getEdgeTarget(splitEdge) .equals(vertex)) {
			 * // i have reached the end of c2 c2EndVertex = vertex; break; }
			 * c2EdgeList.add(anEdge); vertex =
			 * Graphs.getOppositeVertex(this.getBaseGraph(), anEdge, vertex); }
			 * // add the splitting edge to c2 c2EdgeList.add(splitEdge); break;
			 * }
			 */
			// if (splitEdgeSource.equals(vertex)) {
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
			/*
			 * if (this.getBaseGraph().getEdgeTarget(anEdge).equals(vertex)) {
			 * // start c1 c1EdgeList.add(anEdge); // get the next vertex vertex
			 * = Graphs.getOppositeVertex(this.getBaseGraph(), anEdge, vertex);
			 * while (edgeIterator.hasNext()) { anEdge = edgeIterator.next(); if
			 * (this.getBaseGraph() .getEdgeTarget(anEdge)
			 * .equals(this.getBaseGraph() .getEdgeTarget(splitEdge))) {
			 * c1EndVertex = vertex; break; } if (this.getBaseGraph()
			 * .getEdgeTarget(anEdge) .equals(this.getBaseGraph()
			 * .getEdgeSource(splitEdge))) { c1StartVertex = vertex; break; } //
			 * stop an infinite loop if
			 * (this.getBaseGraph().getEdgeSource(anEdge) .equals(vertex)) { //
			 * c1 end vertex c1EndVertex = vertex; break; }
			 * c1EdgeList.add(anEdge); vertex =
			 * Graphs.getOppositeVertex(this.getBaseGraph(), anEdge, vertex);
			 * }// while // i crossed the splitting line
			 * c1EdgeList.add(splitEdge); // start c2 c2StartVertex = vertex;
			 * c2EdgeList.add(anEdge); vertex =
			 * Graphs.getOppositeVertex(this.getBaseGraph(), anEdge, vertex);
			 * while (edgeIterator.hasNext()) { anEdge = edgeIterator.next(); if
			 * (this.getBaseGraph() .getEdgeTarget(anEdge)
			 * .equals(this.getBaseGraph() .getEdgeTarget(splitEdge))) {
			 * c2StartVertex = vertex; break; } if (this.getBaseGraph()
			 * .getEdgeTarget(anEdge) .equals(this.getBaseGraph()
			 * .getEdgeSource(splitEdge))) { c2EndVertex = vertex; break; } if
			 * (this.getBaseGraph().getEdgeSource(anEdge) .equals(vertex)) {
			 * c2StartVertex = vertex; break; } c2EdgeList.add(anEdge); vertex =
			 * Graphs.getOppositeVertex(this.getBaseGraph(), anEdge, vertex); }
			 * break; }// if
			 */
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

	/**
	 * Return true if there is an edge (in this.getBaseGraph()) between any two
	 * non-adjacent vertices. The start vertex is ignored
	 * 
	 * @param aVertexList
	 *            a list of vertices
	 * @return true if there is an edge between non-adjacent vertices excluding
	 *         the first vertex, false otherwise
	 * @throws CycleBasisException
	 */
	public boolean edgeExistsBetweenNonAdjacentVertices(V[] aVertexList)
			throws CycleBasisException {
		// first check if the first and last element of a VertexList are
		// connected
		V first = aVertexList[0];
		V last = aVertexList[aVertexList.length - 1];
		if (!first.equals(last)) {
			if (this.getBaseGraph().containsEdge(first, last) == false) {
				throw new CycleBasisException(
						"first and last vertex are not connected!");
			}
			for (int i = 0; i < aVertexList.length; i++) {
				for (int j = 0; j < aVertexList.length; j++) {
					if (i != j) {
						if (!(i == 0 && j == aVertexList.length - 1)
								&& !(i == aVertexList.length - 1 && j == 0)) {
							if (j > i + 1 || j < i - 1) {
								if (this.getBaseGraph().containsEdge(
										aVertexList[i], aVertexList[j]) == true) {
									return true;
								}
							}
						}
					}
				}
			}
		} else {
			throw new CycleBasisException("invalid vertex list!!!");
		}
		return false;
	}

	private List<V> makeVertexList() {
		List<V> r = new LinkedList<V>();
		for (V aV : this.getBaseGraph().vertexSet()) {
			r.add(aV);
		}
		return Collections.unmodifiableList(r);
	}

	
	private HashMap<V, List<Cycle<V, E>>> initializeCycleMap() {
		HashMap<V, List<Cycle<V, E>>> rm = new HashMap<V, List<Cycle<V, E>>>();
		for (V aVertex : this.getBaseGraph().vertexSet()) {
			rm.put(aVertex, new LinkedList<Cycle<V, E>>());
		}
		return rm;
	}
	
	private void findChordlessCycle(V[] aVertexList, int aMaxLength)throws CycleBasisException{
		V n = aVertexList[0];
		V x = null;
		V[] sub = (V[]) new Object[aVertexList.length + 1];
		// LinkedList<V> sub = new LinkedList<V>();
		Iterator<E> eItr = this.getBaseGraph().edgeSet().iterator();
		while (eItr.hasNext()) {
			E anEdge = eItr.next();
			// get the source and the target vertices for the current edge
			V source = this.getBaseGraph().getEdgeSource(anEdge);
			V target = this.getBaseGraph().getEdgeTarget(anEdge);
			if (source.equals(target)) {
				throw new CycleBasisException(
						"Invalid Graph! Source and target vertices are the same for edge: "
								+ anEdge.toString());
			}
			if (source.equals(n)) {
				x = target;
				if (!this.myInArray(x, aVertexList)) {
					sub[0] = x;
					System.arraycopy(aVertexList, 0, sub, 1, aVertexList.length);
					findChordlessCycles(sub);
				} else if ((aVertexList.length > 2)
						&& (x.equals(aVertexList[aVertexList.length - 1]))) {
					// cycle found
					// create a cycle from a vertex list
					// check if there is a non adjacent vertex in vertexlist
					boolean rabid = this
							.edgeExistsBetweenNonAdjacentVertices(aVertexList);
					if (rabid == false) {
						Cycle<V, E> c = this.createCycleFromVertices(
								this.getBaseGraph(), aVertexList);
						Cycle<V, E> c_invert = c.invertCycle();
						if (!this.getChordlessCycleBasis().contains(c)) {
							if (!this.getChordlessCycleBasis().contains(
									c_invert)) {
								this.getChordlessCycleBasis().add(c);
								continue;
							}
						}
					}
				}
			} else if (target.equals(n)) {
				x = source;
				if (!this.myInArray(x, aVertexList)) {
					sub[0] = x;
					System.arraycopy(aVertexList, 0, sub, 1, aVertexList.length);
					findChordlessCycles(sub);
				} else if ((aVertexList.length > 2)
						&& (x.equals(aVertexList[aVertexList.length - 1]))) {
					// cycle found
					// create a cycle from a vertex list
					boolean rabid = edgeExistsBetweenNonAdjacentVertices(aVertexList);
					if (rabid == false) {
						Cycle<V, E> c = this.createCycleFromVertices(
								this.getBaseGraph(), aVertexList);
						Cycle<V, E> c_invert = c.invertCycle();
						if (!this.getChordlessCycleBasis().contains(c)) {
							if (!this.getChordlessCycleBasis().contains(
									c_invert)) {
								this.getChordlessCycleBasis().add(c);
								continue;
							}
						}
					}
				}
			}
		}
	}
	/**
	 * Identifies all chordless cycles of this.getBaseGraph()
	 * 
	 * @param aVertexList
	 * @throws CycleBasisException
	 */
	@SuppressWarnings("unchecked")
	private void findChordlessCycles(V[] aVertexList)
			throws CycleBasisException {
		V n = aVertexList[0];
		V x = null;
		V[] sub = (V[]) new Object[aVertexList.length + 1];
		// LinkedList<V> sub = new LinkedList<V>();
		Iterator<E> eItr = this.getBaseGraph().edgeSet().iterator();
		while (eItr.hasNext()) {
			E anEdge = eItr.next();
			// get the source and the target vertices for the current edge
			V source = this.getBaseGraph().getEdgeSource(anEdge);
			V target = this.getBaseGraph().getEdgeTarget(anEdge);
			if (source.equals(target)) {
				throw new CycleBasisException(
						"Invalid Graph! Source and target vertices are the same for edge: "
								+ anEdge.toString());
			}
			if (source.equals(n)) {
				x = target;
				if (!this.myInArray(x, aVertexList)) {
					sub[0] = x;
					System.arraycopy(aVertexList, 0, sub, 1, aVertexList.length);
					findChordlessCycles(sub);
				} else if ((aVertexList.length > 2)
						&& (x.equals(aVertexList[aVertexList.length - 1]))) {
					// cycle found
					// create a cycle from a vertex list
					// check if there is a non adjacent vertex in vertexlist
					boolean rabid = this
							.edgeExistsBetweenNonAdjacentVertices(aVertexList);
					if (rabid == false) {
						Cycle<V, E> c = this.createCycleFromVertices(
								this.getBaseGraph(), aVertexList);
						Cycle<V, E> c_invert = c.invertCycle();
						if (!this.getChordlessCycleBasis().contains(c)) {
							if (!this.getChordlessCycleBasis().contains(
									c_invert)) {
								this.getChordlessCycleBasis().add(c);
								continue;
							}
						}
					}
				}
			} else if (target.equals(n)) {
				x = source;
				if (!this.myInArray(x, aVertexList)) {
					sub[0] = x;
					System.arraycopy(aVertexList, 0, sub, 1, aVertexList.length);
					findChordlessCycles(sub);
				} else if ((aVertexList.length > 2)
						&& (x.equals(aVertexList[aVertexList.length - 1]))) {
					// cycle found
					// create a cycle from a vertex list
					boolean rabid = edgeExistsBetweenNonAdjacentVertices(aVertexList);
					if (rabid == false) {
						Cycle<V, E> c = this.createCycleFromVertices(
								this.getBaseGraph(), aVertexList);
						Cycle<V, E> c_invert = c.invertCycle();
						if (!this.getChordlessCycleBasis().contains(c)) {
							if (!this.getChordlessCycleBasis().contains(
									c_invert)) {
								this.getChordlessCycleBasis().add(c);
								continue;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Create a Cycle object from a given list of vertices. Note that the first
	 * element in the vertex list will be the start of the cycle and the last
	 * element of the parameter will become the end cycle
	 * 
	 * @param aG
	 *            the graph where the cycle is being created
	 * @param aVertexList
	 *            an ordered list of vertices
	 * @return a cycle
	 */
	public Cycle<V, E> createCycleFromVertices(UndirectedGraph<V, E> aG,
			V[] aVertexList) {
		Cycle<V, E> rm = null;
		List<E> el = new LinkedList<E>();
		// iterate over the vertexList and get the list of edges
		for (int i = 1; i < aVertexList.length; i++) {
			V aV = aVertexList[i];
			for (int j = i - 1; j < aVertexList.length - 1; j++) {
				if ((i != j) && (i > j)) {
					V anotherV = aVertexList[j];
					// get the common edge
					E aE = aG.getEdge(aV, anotherV);
					if (aE != null) {
						if (!el.contains(aE)) {
							el.add(aE);
						}
					}
				}
				// add the last edge
				if (i == aVertexList.length - 1) {
					// find the edge between the last and the first vertex
					E lastEdge = aG.getEdge(aVertexList[i], aVertexList[0]);
					if (lastEdge != null) {
						if (!el.contains(lastEdge)) {
							el.add(lastEdge);
						}
					}
				}
			}
		}
		// create the cycle to be returned
		try {
			rm = new Cycle<V, E>(this.getBaseGraph(), aVertexList[0],
					aVertexList[aVertexList.length - 1], el, el.size());
		} catch (CycleException e) {
			e.printStackTrace();
		}
		return rm;
	}

	public UndirectedGraph<V, E> getBaseGraph() {
		return this.baseGraph;
	}

	public List<Cycle<V, E>> getChordlessCycleBasis() {
		return this.chordlessCycleBasis;
	}
	/**
	 * Get the chordless cycles of size aSize or less
	 * @param aSize the maximum cycle size to be returned
	 * @return
	 */
	public List<Cycle<V,E>> getChordlessCycleBasisBySize(int aSize){
		List<Cycle<V,E>> rm = new ArrayList<Cycle<V,E>>();
		Iterator<Cycle<V,E>> itr = this.getChordlessCycleBasis().iterator();
		while(itr.hasNext()){
			Cycle<V,E> c = itr.next();
			if(c.size() <= aSize){
				rm.add(c);
			}
		}
		return rm;
	}

	public List<V> getVertexList() {
		return this.vertexList;
	}

	private HashMap<V, List<Cycle<V, E>>> getCyclesMap() {
		return this.cyclesMap;
	}

}
