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

package org.semanticscience.narf.structures.parts;

import java.util.Collections;
import java.util.Set;

/**
 * This class describes an edge as described by the Leontis-Westhof nomenclature.
 * @author Jose Cruz-Toledo
 * @author William Greenwood
 */
public class Edge {

	/**
	 * The one letter identifier of the edge described by the Leontis-Westhof nomenclature
	 */
	private String edgeLabel;
	/**
	 * Set of the substructural components involved in the edge
	 */
	private Set<SubEdge> subEdges;

	
	/**
	 * Construct an edge from an edge label and a set of subedges
	 * @param anEdgeLabel one letter identifier of the edge
	 * @param someSubEdges set of subedges involved in the edge
	 * @since 1.6
	 */
	public Edge(String anEdgeLabel, Set<SubEdge> someSubEdges){
		edgeLabel = anEdgeLabel;
		subEdges = someSubEdges;
	}
	
	
	/**
	 * Get the one letter identifier of the edge described by the Leontis-Westhof nomenclature
	 * @return the one letter identifier of the edge 
	 * @since 1.6
	 */
	public String getEdgeLabel() {
		return edgeLabel;
	}
	
	/**
	 * Get the set of substructural elements involved in the edge
	 * @return the set of subedges
	 * @since 1.6
	 */
	public Set<SubEdge> getSubEdges() {
		return Collections.unmodifiableSet(subEdges);
	}

	public String toString(){
		return edgeLabel;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((edgeLabel == null) ? 0 : edgeLabel.hashCode());
		result = prime * result
				+ ((subEdges == null) ? 0 : subEdges.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		if (edgeLabel == null) {
			if (other.edgeLabel != null)
				return false;
		} else if (!edgeLabel.equals(other.edgeLabel))
			return false;
		if (subEdges == null) {
			if (other.subEdges != null)
				return false;
		} else if (!subEdges.equals(other.subEdges))
			return false;
		return true;
	}

}
