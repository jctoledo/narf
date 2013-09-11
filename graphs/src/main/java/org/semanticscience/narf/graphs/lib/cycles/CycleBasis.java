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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jgrapht.UndirectedGraph;
import org.semanticscience.narf.graphs.nucleicacid.NucleicAcid;

/**
 * @author  Jose Cruz-Toledo
 *
 */
public abstract class CycleBasis<V,E> {
	/**
	 * The graph on which the cycle basis is computed
	 */
	private  UndirectedGraph<V, E> baseGraph;
	/**
	 * A list of the vertices in this graph
	 */
	private List<V> vertexList = null;
	
	/**
	 * The cycle basis 
	 */
	protected List<Cycle<V,E>> cycleBasis;
	
	public CycleBasis(UndirectedGraph<V, E>aG){
		baseGraph = aG;
		vertexList = this.makeVertexList();
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
	
	@SuppressWarnings("unchecked")
	public CycleBasis(NucleicAcid na){
		this((UndirectedGraph<V,E>)na);
	}
	/** 
	 * 
	 * @return A list of the cycles that belong to this basis
	 */
	public abstract List<Cycle<V,E>> getCycleBasis();
}
