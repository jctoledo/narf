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
package org.semanticscience.narf.graphs.old;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.semanticscience.narf.graphs.lib.cycles.Cycle;

/**
 * @author Jose Cruz-Toledo
 * 
 */
public class MinimumCycleBasisOld<V, E> {
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
	private List<Cycle<V, E>> minimumCycleBasis;

	private List<E> backEdges = new LinkedList<E>();
	private List<E> treeEdges = new LinkedList<E>();
	/**
	 * a parenthood map key is a vertex, value is a list List<V> for which the
	 * key is a parent of : i.e: key is_parent_of List<V>.get(0), List<V>.get(1)
	 * is_sibling_of List<V>.get(0), etc.
	 */
	Map<V, List<V>> parenthood_map = new HashMap<V, List<V>>();
	/**
	 * A map that keeps track of the visited nodes from a given node. The key is
	 * a vertex and its value is the list of nodes that have been visited from
	 * vertex V
	 */
	Map<V, List<V>> visited_map = new HashMap<V, List<V>>();

	public MinimumCycleBasisOld(UndirectedGraph<V, E> aGraph) {
		baseGraph = aGraph;
		vertexList = this.makeVertexList();
		this.computeMCBEdges2();
	}

	private void computeMCBEdges2(){
		DepthFirstIterator<V, E> dfs_itr = new DepthFirstIterator<V, E>(
				this.getBaseGraph(), this.getFirstVertex());
		//DFS starts by setting node 1 as the current node
		V current =  this.getFirstVertex();
		dfs: while(dfs_itr.hasNext()){
			V node_i = dfs_itr.next();
			//get the neighbors of node_i
			List<V> neighbours = Graphs.neighborListOf(this.getBaseGraph(), node_i);
			for (V aNeighbour : neighbours) {
				if(!this.checkIfVisited(node_i, aNeighbour)){
					V node_j = aNeighbour;
					E anEdge = this.getBaseGraph().getEdge(node_i, node_j);
					if(this.checkIfVisitedFromOtherNode(node_j, node_i)){
						//mark as back edge
						//make sure that anEdge is not in TreeEdges
						if(!this.getTreeEdges().contains(anEdge)){
							//make sure that the edge is not in there more than once
							if(!this.getBackEdges().contains(anEdge)){
								this.getBackEdges().add(anEdge);
							}
						}
					}else{
						//mark edge(i,j) as tree edge and set i
						// as
						// parent of j.
						if(!this.getTreeEdges().contains(anEdge)){
							this.getTreeEdges().add(anEdge);
						}
						List<V> children = this.getParenthoodMap().get(node_i);
						if(children == null){
							ArrayList<V> c = new ArrayList<V>();
							c.add(node_j);
							this.getParenthoodMap().put(node_i, c);
						}else{
							children.add(node_j);
							this.getParenthoodMap().put(node_i, children);
						}
						//mark node j as visited from node_i?
						List<V> v = this.getVisitedMap().get(node_i);
						if(v == null){
							ArrayList<V> c = new ArrayList<V>();
							c.add(node_j);
							this.getVisitedMap().put(node_i, c);
						}else{
							v.add(node_j);
							this.getVisitedMap().put(node_i, v);
						}
					}	
					// set node_j as current node
					current = node_j;
				}
			}
			//no more vecinos! :p ^. .^
			//get the parent of i
			current = this.findParentOf(node_i);
			/*if(current == null){
				break dfs;
			}*/
		}
	}
	
	
	/**
	 * @return
	 */
	private void computeMCBEdges() {
		DepthFirstIterator<V, E> dfs_itr = new DepthFirstIterator<V, E>(
				this.getBaseGraph(), this.getFirstVertex());
		while (dfs_itr.hasNext()) {
			V current = dfs_itr.next();
			V node_i = current;

			List<V> node_i_record = Graphs.neighborListOf(this.getBaseGraph(),
					node_i);
			// if one or more of the neighbours (node-i-record) have not been
			// visited
			// from
			// node_i
			Iterator<V> node_i_record_itr = node_i_record.iterator();
			while (node_i_record_itr.hasNext()) {
				// let node_j be the first node not visited
				V node_j = node_i_record_itr.next();
				if (!this.checkIfVisited(node_i, node_j)) {
					// get an edge between node_j and current
					E anEdge = this.getBaseGraph().getEdge(node_j, node_i);
					if (anEdge != null) {
						// if node_j was already visited by DFS from a node
						// other
						// than node_i
						if (this.checkIfVisitedFromOtherNode(node_j, node_i)) {
							// mark edge (i,j) as back edge
							this.getBackEdges().add(anEdge);
						} else {
							// otherwise mark edge(i,j) as tree edge and set i
							// as
							// parent of j.
							this.getTreeEdges().add(anEdge);
							// get the children of current
							List<V> children = this.getParenthoodMap().get(
									node_i);
							if (children == null) {
								ArrayList<V> c = new ArrayList<V>();
								c.add(node_j);
								this.getParenthoodMap().put(node_i, c);
							} else {
								children.add(node_j);
								this.getParenthoodMap().put(node_i, children);
							}
							// mark node_j as visited in the node_i record
							List<V> c = this.getVisitedMap().get(node_i);
							if (c == null) {
								ArrayList<V> al = new ArrayList<V>();
								al.add(node_j);
								this.getVisitedMap().put(node_i, al);
							} else {
								c.add(node_j);
								this.getVisitedMap().put(node_i, c);
							}
							// set node_j as current node
							current = node_j;
						}
					}
				}
			}
			// i ran out of neighbours
			// otherwise (all nodes of the node_i record were visited), set node
			// k as current node, where node k is the parent of i.
			// get the parent of current
			V node_k = this.findParentOf(node_i);
			current = node_k;
		}
	}

	/**
	 * Check whether vertex searchMe has been visited from sourceVert vertex in
	 * visited_map. Where sourceVert is a key from visited_map
	 * 
	 * @param sourceVert
	 *            the source vertex
	 * @param searchMe
	 *            the vertex to search for if it has been visited from
	 *            sourceVert
	 * @return true if it has been visited false otherwise
	 */
	private boolean checkIfVisited(V sourceVert, V searchMe) {
		List<V> aL = this.getVisitedMap().get(sourceVert);
		if (aL != null) {
			if (aL.contains(searchMe)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Search for vertex findMe in the values of this.getVisitedMap() except in
	 * the List<V> associated with the key excludeThisKey
	 * 
	 * @param findMe
	 *            the vertex that we are looking for
	 * @param excludeThisKey
	 *            the key to exclude in this search
	 * @return true if findMe was found as a value in a List<V> that is not
	 *         excludeThisKey's List<V>. False otherwise
	 */
	private boolean checkIfVisitedFromOtherNode(V findMe, V excludeThisKey) {
		Set<V> m = this.getVisitedMap().keySet();
		for (V aKey : m) {
			if (!aKey.equals(excludeThisKey)) {
				List<V> al = this.getVisitedMap().get(aKey);
				if (al.contains(findMe)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Search the values of this.getParenthoodMap in search for the key that
	 * holds searchMe as a value
	 * 
	 * @param searchMe
	 *            the vertex for which we want to find a parent
	 * @return the parental Vertex, null otherwise
	 */
	private V findParentOf(V searchMe) {
		Set<V> m = this.getParenthoodMap().keySet();
		for (V aKey : m) {
			List<V> al = this.getParenthoodMap().get(aKey);
			if (al.contains(searchMe)) {
				return aKey;
			}
		}
		return null;
	}

	private List<V> makeVertexList() {
		List<V> r = new LinkedList<V>();
		for (V aV : this.getBaseGraph().vertexSet()) {
			r.add(aV);
		}
		return Collections.unmodifiableList(r);
	}

	public UndirectedGraph<V, E> getBaseGraph() {
		return this.baseGraph;
	}

	public List<V> getVertexList() {
		return this.vertexList;
	}

	public V getFirstVertex() {
		return this.getVertexList().get(0);
	}

	public V getLastVertex() {
		return this.getVertexList().get(this.getVertexList().size() - 1);
	}

	private Map<V, List<V>> getVisitedMap() {
		return this.visited_map;
	}

	public List<E> getBackEdges() {
		return this.backEdges;
	}

	public List<E> getTreeEdges() {
		return this.treeEdges;
	}

	private Map<V, List<V>> getParenthoodMap() {
		return this.parenthood_map;
	}
}
