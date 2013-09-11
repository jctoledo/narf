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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jgrapht.UndirectedGraph;
import org.semanticscience.narf.graphs.lib.cycles.exceptions.CycleBasisException;
import org.semanticscience.narf.graphs.lib.cycles.exceptions.CycleException;
import org.semanticscience.narf.graphs.nucleicacid.NucleicAcid;

/**
 * @author Jose Cruz-Toledo
 * 
 */
public class ChordlessCycleBasis<V, E> extends CycleBasis<V, E> {
	/**
	 * A list with all chordless cycles in this graph
	 */
	private List<Cycle<V, E>> chordlessCycleBasis;


	@SuppressWarnings("unchecked")
	public ChordlessCycleBasis(UndirectedGraph<V, E> aG) throws CycleBasisException {
		super(aG);
		chordlessCycleBasis = new LinkedList<Cycle<V, E>>();
		// compute the cycle basis
		// iterate over each edge of the passed in graph
		Set<E> edges = aG.edgeSet();
		for (E anEdge : edges) {
			// get the source and target
			V s = aG.getEdgeSource(anEdge);
			V t = aG.getEdgeTarget(anEdge);
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
	
	public List<Cycle<V, E>> getChordlessCycleBasis() {
		return this.chordlessCycleBasis;
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
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticscience.narf.graphs.lib.cycles.CycleBasis#getCycleBasis()
	 */
	@Override
	public List<Cycle<V, E>> getCycleBasis() {
		return this.chordlessCycleBasis;
	}

}
