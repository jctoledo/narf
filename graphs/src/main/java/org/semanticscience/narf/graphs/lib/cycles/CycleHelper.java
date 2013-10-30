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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.semanticscience.narf.graphs.lib.cycles.exceptions.CycleException;
import org.semanticscience.narf.graphs.nucleicacid.InteractionEdge;
import org.semanticscience.narf.graphs.nucleicacid.NucleicAcid;

import org.semanticscience.narf.structures.interactions.BasePair;
import org.semanticscience.narf.structures.interactions.NucleotideInteraction;
import org.semanticscience.narf.structures.interactions.PhosphodiesterBond;
import org.semanticscience.narf.structures.parts.Nucleotide;


/**
 * This class converts a SimpleCycle from CDK into a Cycle<V,E>
 * 
 * @author Jose Cruz-Toledo
 * 
 */
public class CycleHelper {

	/**
	 * For a given cycle, compute all counter-clockwise 1-step unique rotations
	 * of aCycle, and return the double representation of the smallest one
	 * 
	 * @param aNucleicAcid
	 * @param aCycle
	 * @return
	 */
	public static BigDecimal findMinmalNormalization(NucleicAcid aNucleicAcid,
			Cycle<Nucleotide, InteractionEdge> aCycle) {
		// find all rotations of the parameter cycle
		List<Cycle<Nucleotide, InteractionEdge>> rotatedCycles = CycleHelper
				.findAllRotations(aNucleicAcid, aCycle);
		// now find the smallest double
		BigDecimal min = BigDecimal.valueOf(Double.MAX_VALUE);
		for (Cycle<Nucleotide, InteractionEdge> rot : rotatedCycles) {
			BigDecimal ad = CycleHelper.normalizeCycle(rot);
			
			//if (ad < min) {
			if (ad.compareTo(min) < 0) {
				min = ad;
			}
			
		}
		return min;
	}
 
	/**
	 * Make a Double representation of a Cycle<Nucleotide, InteractionEdge>
	 * 
	 * @param ac
	 *            a cycle
	 * @return a double representation of the cycle that includes the following
	 *         features: Nucleotides, backbones and base pairs only
	 */
	public static BigDecimal normalizeCycle(Cycle<Nucleotide, InteractionEdge> ac) {
		List<Nucleotide> verts = ac.getVertexList();
		LinkedList<Integer> tmpints = new LinkedList<Integer>();
		for (Nucleotide aNuc : verts) {
			try{
				int nn = aNuc.getNormalizedNucleotide();
				tmpints.add(nn);
			}catch(NullPointerException e){
				System.out.println("offending nucleotide: "+ aNuc);
				e.printStackTrace();
				System.exit(1);
			}
			InteractionEdge ie = ac.getNextEdge(aNuc);
			Set<NucleotideInteraction> nis = ie.getInteractions();
			for (NucleotideInteraction aNi : nis) {
				if (aNi instanceof BasePair) {
					try {
						int bp = ((BasePair) aNi).getNormalizedBasePairClass();
						tmpints.add(bp);
					} catch (NullPointerException e) {
						e.printStackTrace();
					}
				}
				if (aNi instanceof PhosphodiesterBond) {
					int pdb = ((PhosphodiesterBond) aNi)
							.getNormalizedBackBone();
					tmpints.add(pdb);
				}
				//break;
			}
		}
		String intStr = "";
		for (Integer anInt : tmpints) {
			intStr += anInt;
		}
		
		BigDecimal d= null;
		try {
		  d = new BigDecimal(intStr);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return d;
	}

	/**
	 * Calculate all counter clockwise rotations of the given cycle and return
	 * them as a list of cycles
	 * 
	 * @param aCycle
	 *            a cycle for which you wish to find all possible rotations
	 * @return a list of cycles containing all non identical rotations of the
	 *         parameter cycle
	 */
	public static List<Cycle<Nucleotide, InteractionEdge>> findAllRotations(
			NucleicAcid aNuc, Cycle<Nucleotide, InteractionEdge> aCycle) {
		List<Cycle<Nucleotide, InteractionEdge>> rm = new ArrayList<Cycle<Nucleotide, InteractionEdge>>();
		// find out how many times we need to rotate this cycle
		int rotations = aCycle.size() - 1;
		// add the parameter cycle
		rm.add(aCycle);
		for (int i = 0; i < rotations; i++) {
			try {
				LinkedList<InteractionEdge> el = aCycle.getEdgeLinkedList();
				// rotate the list
				LinkedList<InteractionEdge> new_el = CycleHelper
						.rotateCounterClockwise(el);
				// now find a new start and end vertex
				Nucleotide sv = CycleHelper.findFirstNucleotide(new_el);
				Nucleotide ev = CycleHelper.findLastNucleotide(new_el);
				// create a new cycle with the rotated edges
				aCycle = null;
				aCycle = new Cycle<Nucleotide, InteractionEdge>(aNuc, sv, ev,
						new_el, new_el.size());
				rm.add(aCycle);
				
			} catch (CycleException e) {
				e.printStackTrace();
			}
		}
		return rm;
	}

	/**
	 * Rotate a list counterclockwise by one step. relative positioning of
	 * elements remains the same for example: [a,b,c] -> [b,a,c]
	 * 
	 * @param aSortedList
	 *            a sorted list of edges that belong to a cycle
	 * @return a rotated edge list that preserves relative positioning of
	 *         elements
	 */
	private static LinkedList<InteractionEdge> rotateCounterClockwise(
			LinkedList<InteractionEdge> aSortedList) {
		LinkedList<InteractionEdge> rm = new LinkedList<InteractionEdge>();
		rm = aSortedList;
		// remove the first element
		InteractionEdge f = rm.remove(0);
		// now add it at the end
		rm.add(f);
		return rm;
	}

	public static Nucleotide findFirstNucleotide(
			List<InteractionEdge> aSortedList) {
		InteractionEdge firstEdge = aSortedList.get(0);
		InteractionEdge secondEdge = aSortedList.get(1);
		Nucleotide f = firstEdge.getFirstNucleotide();
		Nucleotide s = firstEdge.getSecondNucleotide();
		Nucleotide f2 = secondEdge.getFirstNucleotide();
		Nucleotide s2 = secondEdge.getSecondNucleotide();
		if (f.equals(f2) || f.equals(s2)) {
			return s;
		} else if (s.equals(f2) || s.equals(s2)) {
			return f;
		}
		return null;
	}

	public static Nucleotide findLastNucleotide(
			List<InteractionEdge> aSortedList) {
		InteractionEdge firstEdge = aSortedList.get(0);
		InteractionEdge lastEdge = aSortedList.get(aSortedList.size() - 1);
		Nucleotide f = firstEdge.getFirstNucleotide();
		Nucleotide s = firstEdge.getSecondNucleotide();
		Nucleotide f2 = lastEdge.getFirstNucleotide();
		Nucleotide s2 = lastEdge.getSecondNucleotide();
		if (f2.equals(f) || f2.equals(s)) {
			return s2;
		} else if (s2.equals(f) || s2.equals(s)) {
			return f2;
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
