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
 * @author  Jose Cruz-Toledo
 *
 */
public class SimpleCycleConverter {
	/**
	 * From an unsorted set of edges, find a set of concatenated edges and return it. 
	 * @param aListOfEdges
	 * @return
	 */
	public static List<InteractionEdge> initializeEdgeList(List<InteractionEdge> aListOfEdges){
		List<InteractionEdge> rm = new ArrayList<InteractionEdge>();
		boolean flag = true;
		if(aListOfEdges.size()>0){
			do{
				InteractionEdge fe = null;
				//ask whether the rm variable has any elements
				if(rm.isEmpty()){
					//remove the first edge
					fe = aListOfEdges.remove(0);
					//check whether this edge needs to be flipped
					fe = flipEdge(fe);
					//get the source for this edge
					Nucleotide f = fe.getFirstNucleotide();
					if(aListOfEdges.size()>0){
						//find the edge that it binds to 
						InteractionEdge curr = null;
						Iterator<InteractionEdge> itr = aListOfEdges.iterator();
						while(itr.hasNext()){
							InteractionEdge ae = itr.next();
							//get the source and the target for this second edge
							Nucleotide f2 = ae.getFirstNucleotide();
							Nucleotide s2 = ae.getSecondNucleotide();
							if(f2.equals(f) || s2.equals(f)){
								rm.add(fe);
								rm.add(ae);
								//now remove the edge from alistofedges
								itr.remove();
								break;
							}
						}
				}else{
					//then get the last element in rm 
					fe = rm.get(rm.size()-1);
					
				}
				
				}else {
					flag = false;
				}
			}while (flag == true);
		}else{
			return null;
		}
		return rm;
	}
	
	/**
	 * This method "flips" an edge. The flipped edge always has a nucleotide with a lesser
	 * residue position as the first nucleotide
	 * @param fe
	 * @return an edge with the nucleotide with the lesser residue position as the first nucleotide
	 */
	private static InteractionEdge flipEdge(InteractionEdge fe) {
		//get the residue position of the first nucleotide
		Nucleotide fp = fe.getFirstNucleotide();
		//get the residue position of the second nucleotide 
		Nucleotide sp = fe.getSecondNucleotide();
		//now check their positions
		if(fp.getResiduePosition() > sp.getResiduePosition()){
			return fe;
		}else{
			InteractionEdge ie = new InteractionEdge();
			
		}
		// TODO Auto-generated method stub
		return null;
	}
	

}
