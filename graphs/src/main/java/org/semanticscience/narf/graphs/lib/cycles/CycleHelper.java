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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.semanticscience.narf.graphs.nucleicacid.InteractionEdge;
import org.semanticscience.narf.structures.parts.Nucleotide;

/**
 * This class converts a SimpleCycle from CDK into a Cycle<V,E>
 * 
 * @author Jose Cruz-Toledo
 * 
 */
public class CycleHelper {
	
	public static Nucleotide findFirstNucleotide(List<InteractionEdge> aSortedList){
		InteractionEdge firstEdge = aSortedList.get(0);
		InteractionEdge secondEdge = aSortedList.get(1);
		Nucleotide f = firstEdge.getFirstNucleotide();
		Nucleotide s = firstEdge.getSecondNucleotide();
		Nucleotide f2 = secondEdge.getFirstNucleotide();
		Nucleotide s2 = secondEdge.getSecondNucleotide();
		if(f.equals(f2) || f.equals(s2)){
			return f;
		}else if(s.equals(f2) || s.equals(s2)){
			return s;
		}
		return null;
	}
	

	
	public static Nucleotide findLastNucleotide(List<InteractionEdge> aSortedList){
		InteractionEdge firstEdge = aSortedList.get(0);
		InteractionEdge lastEdge = aSortedList.get(aSortedList.size()-1);
		Nucleotide f = firstEdge.getFirstNucleotide();
		Nucleotide s = firstEdge.getSecondNucleotide();
		Nucleotide f2 = lastEdge.getFirstNucleotide();
		Nucleotide s2 = lastEdge.getSecondNucleotide();
		if(f.equals(f2) || f.equals(s2)){
			return f;
		}else if(s.equals(f2) || s.equals(s2)){
			return s;
		}
		return null;
	}
	
	/**
	 * From an unsorted set of edges, find a set of concatenated edges and
	 * return it.
	 * 
	 * @param aListOfEdges
	 * @return
	 */
	public static List<InteractionEdge> sortEdgeList(
			List<InteractionEdge> aListOfEdges) {
		List<InteractionEdge> rm = new ArrayList<InteractionEdge>();
		boolean flag = true;
		if (aListOfEdges.size() > 2) {
			do {
				InteractionEdge fe = null;
				// ask whether the rm variable has any elements
				if (rm.isEmpty()) {
					// remove the first edge from aloe
					fe = aListOfEdges.remove(0);
					rm.add(fe);
					// get the source for this edge
					Nucleotide f = fe.getFirstNucleotide();
					// find the edge that it binds to
					Iterator<InteractionEdge> itr = aListOfEdges.iterator();
					while (itr.hasNext()) {
						InteractionEdge ae = itr.next();
						// get the source and the target for this second edge
						Nucleotide f2 = ae.getFirstNucleotide();
						Nucleotide s2 = ae.getSecondNucleotide();
						if (f2.equals(f) || s2.equals(f)) {
							rm.add(ae);
							// now remove the edge from alistofedges
							itr.remove();
							break;
						}
					}
				} else {
					// now use the last element in the returnme variable
					fe = rm.get(rm.size() - 1);
					// get the source for this edge
					Nucleotide f = fe.getFirstNucleotide();
					Nucleotide s = fe.getSecondNucleotide();
					if (aListOfEdges.size() > 0) {
						// find the edge that it binds to
						Iterator<InteractionEdge> itr = aListOfEdges.iterator();
						while (itr.hasNext()) {
							InteractionEdge ae = itr.next();
							// get the source and the target for this second
							// edge
							Nucleotide f2 = ae.getFirstNucleotide();
							Nucleotide s2 = ae.getSecondNucleotide();
							if (f2.equals(f) || s2.equals(f)) {
								rm.add(ae);
								// now remove the edge from alistofedges
								itr.remove();
								break;
							} else if (f2.equals(s) || s2.equals(s)) {
								rm.add(ae);
								itr.remove();
								break;
							}
						}
					} else {
						flag = false;
					}
				}
				// flag = false;
			} while (flag == true);
		} else {
			return null;
		}
		return rm;
	}

}
