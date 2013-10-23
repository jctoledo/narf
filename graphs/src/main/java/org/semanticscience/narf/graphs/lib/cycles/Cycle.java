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

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.graph.GraphPathImpl;
import org.semanticscience.narf.graphs.lib.cycles.exceptions.CycleException;

/**
 * @author Jose Cruz-Toledo
 * 
 */
public class Cycle<V, E> extends GraphPathImpl<V, E> {
	/**
	 * The vertices that compose this cycle
	 */
	private LinkedList<V> vertexList;

	/**
	 * Create a Cycle that starts at startVertex ends at endVertex traversing
	 * through edgeList
	 * 
	 * @param aGraph
	 *            the base graph where the cycle is found
	 * @param startVertex
	 *            the fisrt vertex
	 * @param endVertex
	 *            the end vertex
	 * @param edgeList
	 *            the edges that close the cycle
	 * @param weight
	 *            the weight of the cycle
	 * @throws CycleException
	 *             if an invalid number of edges is found
	 */
	public Cycle(Graph<V, E> aGraph, V startVertex, V endVertex,
			List<E> edgeList, double weight) throws CycleException {
		super(aGraph, startVertex, endVertex, edgeList, weight);
		vertexList = new LinkedList<V>();
		// check that you have at least 3 edges
		if (edgeList.size() < 3) {
			throw new CycleException(
					"Invalid number of edges. Minimum 3 accepted!");
		} else {
			// get the first and last edge
			E lastE = edgeList.get(edgeList.size() - 1);
			E firstE = edgeList.get(0);
			V firstV = this.getCommonVertex(firstE, lastE);
			if (!vertexList.contains(firstV)) {
				vertexList.add(firstV);
			}
			for (int i = 1; i < edgeList.size(); i++) {
				E anEdge = edgeList.get(i);
				for (int j = i - 1; j < edgeList.size() - 1; j++) {
					E anotherEdge = edgeList.get(j);
					// get the common vertex
					if (i != j) {
						V cV = this.getCommonVertex(anEdge, anotherEdge);
						if (!vertexList.contains(cV) && cV != null) {
							vertexList.add(cV);
						}
					}
				}
			}
			// now add the last vertex
			E secondToLastE = edgeList.get(edgeList.size() - 2);
			V lastV = this.getCommonVertex(lastE, secondToLastE);
			if (!vertexList.contains(lastV)) {
				vertexList.add(lastV);
			}
			int d = edgeList.size() - vertexList.size();
			if (d < 0) {
				throw new CycleException("Incorrect number of edges");
			}
			// check if start and end vertex are in the vertex list
			if (!this.containsVertex(startVertex)) {
				throw new CycleException(
						"Requested start vertex not found in vertex list");
			}
			if (!this.containsVertex(endVertex)) {
				throw new CycleException(
						"Requested end vertex not found in vertex list");
			}
		}
	}// constructor

	private int computeDistance(V vStart, V vEnd) throws CycleException {
		int startpos = 0;
		int endpos = 0;
		int pos = 0;
		List<V> vl = this.getVertexList();
		if (!vl.contains(vStart) || !vl.contains(vEnd)) {
			throw new CycleException("Either vStart or vEnd were not in vlist");
		}
		Iterator<V> itr = vl.iterator();
		while (itr.hasNext()) {
			V vert = itr.next();
			if (vert.equals(vStart)) {
				startpos = pos;
			}
			if (vert.equals(vEnd)) {
				endpos = pos;
			}
			pos++;
		}
		int dis = Math.abs(startpos - endpos);
		return dis;
	}

	public E getFirstEdge() {
		return this.getEdgeList().get(0);
	}

	public E getLastEdge() {
		int s = this.getEdgeList().size();
		return this.getEdgeList().get(s - 1);
	}

	public V getFirstVertex() {
		return this.getVertexList().get(0);
	}

	public V getLastVertex() {
		int s = this.getVertexList().size();
		return this.getVertexList().get(s - 1);
	}


	/**
	 * Returns an edge connecting aVertex to anotherVertex if such vertices and
	 * such edge exist in this cycle. Otherwise returns null. If any of the
	 * specified vertices is null a CycleException is thrown
	 * 
	 * @param aVertex
	 * @param anotherVertex
	 * @return
	 */
	private E getCommonEdge(V aVertex, V anotherVertex) throws CycleException {
		if (aVertex == null || anotherVertex == null) {
			throw new CycleException("null vertices passed in");
		} else {
			Iterator<E> itr = this.getEdgeList().iterator();
			while (itr.hasNext()) {
				E anEdge = itr.next();
				V s = this.getGraph().getEdgeSource(anEdge);
				V t = this.getGraph().getEdgeTarget(anEdge);
				if ((aVertex.equals(s) && anotherVertex.equals(t))
						|| (aVertex.equals(t) && anotherVertex.equals(s))) {
					return anEdge;
				}
			}
		}
		return null;
	}

	/**
	 * Return the outgoing edge of aVertex
	 * 
	 * @param aVertex
	 * @return the edge pointing to the next vertex in the cycle from aVertex
	 */
	public E getNextEdge(V aVertex) {
		E rm = null;
		List<V> vl = this.getVertexList();
		for (int j = 0; j < vl.size(); j++) {
			if (vl.get(j).equals(aVertex)) {
				rm = this.getEdgeList().get(j);
				break;
			}
		}
		return rm;
	}

	/**
	 * Iterate over the vertex list and returns the next vertex in the set. If
	 * requested vertex is the end vertex, the first vertex is returned
	 * 
	 * @param aVertex
	 *            the search
	 * @return the next vertex in the set, null if not found
	 */
	public V getNextVertex(V aVertex) {
		if (this.getVertexList().contains(aVertex)) {
			if (aVertex.equals(this.getVertexList().get(
					this.getVertexList().size() - 1))) {
				return this.getVertexList().get(0);
			} else {
				int i = this.getVertexList().indexOf(aVertex) + 1;
				return this.getVertexList().get(i);
			}
		}
		return null;
	}

	/**
	 * Returns true if the specified vertex exists in this Cycle's vertexlist
	 * 
	 * @return true if aVert is found in vertexList
	 */
	public boolean containsVertex(V aVertex) {
		boolean rm = false;
		if (aVertex == null) {
			return rm;
		}
		List<V> vl = this.getVertexList();
		if (vl != null) {
			for (V aVert : vl) {
				try {
					if (aVert == null) {
						rm = false;
						break;
					}
					if (aVert.equals(aVertex)) {
						rm = true;
						break;
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
		} else {
			rm = false;
		}
		return rm;
	}

	/**
	 * Compute the inverse of the passed in cycle. The vertexlist is inverted
	 * and so are the edges
	 * 
	 * @param aCycle
	 *            a cycle that the inverse of the parameter
	 * @return an inverted cycle
	 */
	public Cycle<V, E> invertCycle() {
		// define the new start and new end
		V newStart = this.getEndVertex();
		V newEnd = this.getStartVertex();
		try {
			List<E> newEdList = new LinkedList<E>();
			for (int i = this.getEdgeList().size() - 2; i >= 0; i--) {
				newEdList.add(this.getEdgeList().get(i));
			}
			newEdList
					.add(this.getEdgeList().get(this.getEdgeList().size() - 1));
			// retrun a new cycle
			Cycle<V, E> r = new Cycle<V, E>(this.getGraph(), newStart, newEnd,
					newEdList, newEdList.size());
			return r;
		} catch (CycleException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Returns the incoming edge to a vertex in the cycle
	 * 
	 * @param aVertex
	 *            the vertex to search for
	 * @return null if the vertex is not in this cycle
	 */
	public E getIncomingEdge(V aVertex) {
		E rm = null;
		// find v in vlist
		List<V> vList = this.getVertexList();
		for (int j = 0; j < vList.size(); j++) {
			if (vList.get(j).equals(aVertex)) {
				List<E> el = this.getEdgeList();
				if (j == 0) {
					// use the last edge of edgelist
					return el.get(el.size() - 1);
				} else {
					return el.get(j - 1);
				}
			}
		}
		return rm;
	}

	/**
	 * Return the size of the cycle by returning the number of edges in this
	 * cycle
	 */
	public int size() {
		return this.getEdgeList().size();
	}

	/**
	 * Rotate this cycle. Define the new start and end of this cycle
	 * 
	 * @param newStart
	 *            the start of the cycle to return
	 * @param newEnd
	 *            the end of the cycle to return
	 * @return a cycle of the same directionality that has the same nodes and
	 *         edges start and end. If newStart and newEnd where the same as
	 *         the original cycle then the original cycle is returned
	 * @throws CycleException
	 */
	public Cycle<V, E> rotateCycle(V newStart, V newEnd) throws CycleException {
		if (newStart.equals(newEnd)) {
			throw new CycleException(
					"Start and end vertices must be different!");
		} else {
			// check if the distance(newStart, newEnd) is more than 1
			int distance = this.computeDistance(newStart, newEnd);
			if (distance > 1) {
				throw new CycleException(
						"Start and end vertices must be adjacent!");
			}
			// check for the base case (the newstart and newend vertices are the
			// same as the bases'
			if (newStart.equals(this.getStartVertex())
					&& newEnd.equals(this.getEndVertex())) {
				return this;
			}
			// check if the newstart and newend are flipped
			else if (newStart.equals(this.getEndVertex())
					&& newEnd.equals(this.getStartVertex())) {
				// reverse the order of the contents
				List<E> newEdList = new LinkedList<E>();
				for (int i = this.getEdgeList().size() - 1; i >= 0; i--) {
					newEdList.add(this.getEdgeList().get(i));
				}
				// retrun a new cycle
				Cycle<V, E> r = new Cycle<V, E>(this.getGraph(), newStart,
						newEnd, newEdList, newEdList.size());
				return r;
			}
			// edges between newstart and end
			// check if start is equal to new end
			if (this.getStartVertex().equals(newEnd)) {
				// do something special :)
				if(newStart.equals(this.getEndVertex())){
					int p = 0;
					
				}
					
				List<E> e = computeEdgeSubList(newStart, this.getEndVertex());
				E extra = this.getNextEdge(this.getFirstVertex());
				e.add(extra);
				Cycle<V, E> c = new Cycle<V, E>(this.getGraph(), newStart,
						newEnd, e, e.size());
				return c;
			} else {
				if(newStart.equals(this.getEndVertex()) || newEnd.equals(this.getStartVertex())){
					int p = 0;
					
				}
				// check if newEnd is equal to start
				List<E> e = computeEdgeSubList(newStart, this.getEndVertex());
				List<E> d = computeEdgeSubList(this.getStartVertex(), newEnd);
				// combine them
				e.addAll(d);
				Cycle<V, E> c = new Cycle<V, E>(this.getGraph(), newStart,
						newEnd, e, e.size());
				return c;
			}

		}
	}
	
	/**
	 * The edges that are between startV and endV including the edge that starts
	 * at endV
	 * 
	 * @param startV
	 * @param endV
	 * @return
	 * @throws CycleException
	 */
	public List<E> computeEdgeSubList(V startV, V endV) throws CycleException {
		if (startV.equals(endV)) {
			throw new CycleException("Start Vertex cannot equal End Vertex!");
		} else {
			List<E> eList = new LinkedList<E>();
			// iterate over vertex list and find vertices that are between
			// startV and endV
			List<V> vList = new LinkedList<V>();
			boolean b = false;
			Iterator<V> vItr = this.getVertexList().iterator();
			x: while (vItr.hasNext()) {
				V aVert = vItr.next();
				if (aVert.equals(startV)) {
					vList.add(aVert);
					b = true;
					continue x;
				}
				if (b == true) {
					vList.add(aVert);
					if (aVert.equals(endV)) {
						b = false;
						break x;
					}
				}
			}// while

			for (int i = 1; i < vList.size(); i++) {
				V aV = vList.get(i);
				for (int j = i - 1; j < vList.size() - 1; j++) {
					V anotherV = vList.get(j);
					// get common edge
					E aE = this.getCommonEdge(aV, anotherV);
					if (aE != null) {
						if (!eList.contains(aE)) {
							eList.add(aE);
						}// if
					}// if
				}// for
				if (i == vList.size() - 1) {
					E lastEdge = this.getNextEdge(aV);
					eList.add(lastEdge);
				}
			}// for
			return eList;
		}
	}

	/**
	 * Retrieve the complete list of vertices that compose this cycle
	 * @return an unmodifiable list of vertices
	 */
	public List<V> getVertexList() {
		return Collections.unmodifiableList(vertexList);
	}

	public boolean equals(Object obj) {
		boolean d = false;
		boolean e = false;
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		@SuppressWarnings("unchecked")
		final Cycle<V, E> other = (Cycle<V, E>) obj;
		// check that both cycles have the same edges
		if (compareEdgeLists(this.getEdgeList(), other.getEdgeList())) {
			if (compareVertexLists(this.getVertexList(), other.getVertexList())) {
				if (this.getWeight() == other.getWeight()) {
					// check that c contains both the start and the end
					// vertex of this
					if (this.containsVertex(other.getStartVertex())
							&& this.containsVertex(other.getEndVertex())) {
						return true;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
		return false;
	}

	/**
	 * This method takes in two lists of edges and compares their contents. If
	 * the lists have the same number of elements in any order then it should
	 * return true. For example if aList = {"1", "2","3"} and anotherList =
	 * {"2","1","3"} then this method will return true
	 * 
	 * @param aList
	 *            a list to compare
	 * @param anotherList
	 *            another list to compare
	 * @return true iff both lists have the same number of elements and are in
	 *         any order
	 */
	private boolean compareEdgeLists(List<E> aList, List<E> anotherList) {
		if (aList.size() == anotherList.size()) {
			for (E e : aList) {
				if (!anotherList.contains(e)) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}// compareEdgeLists

	/**
	 * See compare edgeLists
	 * 
	 * @param aList
	 * @param anotherList
	 * @return
	 */
	private boolean compareVertexLists(List<V> aList, List<V> anotherList) {
		if (aList.size() == anotherList.size()) {
			for (V aVert : aList) {
				if (!anotherList.contains(aVert)) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}// compareVertexLists

	public String toString() {
		String r = "";
		r += "Cycle: Size: [" + vertexList.size() + "] ,\nVertexList [";
		for (V ver : vertexList) {
			r += ver + "\n ";
		}
		r = r.substring(0, r.length() - 2);
		r += "]\n EdgeList: [";
		for (E anEdge : this.getEdgeList()) {
			r += anEdge + "\n";
		}
		r = r.substring(0, r.length() - 2);
		r += "] \nStartVertex: " + this.getStartVertex() + ", EndVertex: "
				+ this.getEndVertex();
		r += " Weight: " + this.getWeight()+"\n\n";
		return r;
	}

	/**
	 * Compute the shared vertex between edge2 and edge1
	 * 
	 * @param edge1
	 *            an edge
	 * @param edge2
	 *            another edge
	 * @return the vertex that edge2 and edge1 share
	 */
	private V getCommonVertex(E edge1, E edge2) {
		if (this.getGraph().getEdgeSource(edge1)
				.equals(this.getGraph().getEdgeSource(edge2))) {
			return this.getGraph().getEdgeSource(edge1);
		} else if (this.getGraph().getEdgeSource(edge1)
				.equals(this.getGraph().getEdgeTarget(edge2))) {
			return this.getGraph().getEdgeSource(edge1);
		} else if (this.getGraph().getEdgeTarget(edge1)
				.equals(this.getGraph().getEdgeSource(edge2))) {
			return this.getGraph().getEdgeTarget(edge1);
		} else if (this.getGraph().getEdgeTarget(edge1)
				.equals(this.getGraph().getEdgeTarget(edge2))) {
			return this.getGraph().getEdgeTarget(edge1);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((vertexList == null) ? 0 : vertexList.hashCode());
		return result;
	}

}
