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
package org.semanticscience.narf.graphs.nucleicacid;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.graph.SimpleGraph;
import org.junit.Test;

import org.openscience.cdk.ringsearch.cyclebasis.CycleBasis;
import org.openscience.cdk.ringsearch.cyclebasis.SimpleCycle;
import org.semanticscience.narf.graphs.lib.cycles.Cycle;
import org.semanticscience.narf.graphs.lib.cycles.exceptions.CycleException;
import org.semanticscience.narf.structures.interactions.BasePair;
import org.semanticscience.narf.structures.interactions.BaseStack;
import org.semanticscience.narf.structures.interactions.PhosphodiesterBond;
import org.semanticscience.narf.structures.parts.Nucleotide;

/**
 * @author Jose Cruz-Toledo
 * 
 */
public class NucleicAcidTest {
	private static String pdbId = "3Q50";
	private static Set<NucleicAcid> nas;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		URL aURL = new URL("http://www.rcsb.org/pdb/files/" + pdbId + ".pdb");
		File aFile = new File(FileUtils.getTempDirectoryPath() + "/" + pdbId
				+ ".pdb");
		FileUtils.copyURLToFile(aURL, aFile);
		nas = ExtractedNucleicAcid.x3dnaDssr(aFile);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		nas = null;
	}

	@Test 
	public void testingNucleicAcid(){
		for(NucleicAcid aNuc: nas){
			Set<Nucleotide> nucs = aNuc.vertexSet();
			Set<BasePair> basePairs = aNuc.getBasePairs();
			Set<BaseStack> baseStacks = aNuc.getBaseStacks();
			Set <PhosphodiesterBond> backbone = aNuc.getPhosphodiesterBonds();
			Set<InteractionEdge> allEdges = aNuc.edgeSet();
			System.out.println("# of base pairs : "+ basePairs.size());
			System.out.println("# of Phosphodiester bonds :" + backbone.size());
			System.out.println("# of Base stacks : "+baseStacks.size());
			System.out.println("# of total edges : "+allEdges.size());
			System.out.println("# of total nucleotides : "+ nucs.size());
						
		}
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testMakeSimpleGraph() throws IOException{
		for(NucleicAcid aNuc: nas){
			SimpleGraph sg = aNuc.makeSimpleGraph();
			CycleBasis cb = new CycleBasis(sg);
			List<SimpleCycle> cycles =  (List<SimpleCycle>) cb.cycles();
			System.out.println(cycles.size());
			Map<Double, Double> w = new HashMap<Double, Double>();
			for(SimpleCycle sc: cycles){
				if (w.containsKey(sc.weight())){
					Double d =w.get(sc.weight())+1.0;
					w.put(sc.weight(), d);
				}else{
					w.put(sc.weight(), 1.0);
				}
				//System.out.println("Cycle of weight: "+sc.weight());
				//System.out.println("Members:");
				Set<Nucleotide> nucs = sc.vertexSet();
				//Cycle c = new Cycle<Nucleotide, InteractionEdge>(null, sc.vertexList()
				//		., endVertex, edgeList, weight)
				List<InteractionEdge> el = new ArrayList<InteractionEdge>();
				
				//iterate over the edges get the source and target 
				//then query aNuc for the cooresponding interaction edge
				Iterator<Edge> sce_itr = sc.edgeSet().iterator();
				while(sce_itr.hasNext()){
					Edge anEdge = sce_itr.next();
					Nucleotide source =(Nucleotide) anEdge.getSource();
					Nucleotide target = (Nucleotide) anEdge.getTarget();
					Set<InteractionEdge> x = aNuc.getAllEdges(source, target);
					el.addAll(x);
				}
				
				
				//System.out.println("Edges:");
				//System.out.println(el);
				/**/Nucleotide first_nuc = null;
				Nucleotide last_nuc = null;
				int nuc_count = 0;
			
				for(Nucleotide an : nucs){
					if(nuc_count == 0){
						first_nuc = an;
					}
					nuc_count++;
					if(nuc_count == nucs.size()){
						last_nuc = an;
					}
				}
				try{
					Cycle <Nucleotide , InteractionEdge> c = new Cycle<Nucleotide, InteractionEdge>(aNuc, first_nuc, last_nuc, el, sc.weight());
					System.out.println(c);
				}catch(CycleException e){
					e.printStackTrace();
				} 
				System.out.println("******");
			}
			//now write to file the frequency
			String buf = "";
			Iterator it = w.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry x = (Map.Entry) it.next();
				Double k = (Double) x.getKey();
				Double v = (Double) x.getValue();
				buf += k+"\t"+v+"\n";
			}
			FileUtils.writeStringToFile(new File("/tmp/freq.tsv"), buf);
		}
	}
	
	
	/*@Test
	public void testingCycleBasisPrettyPrint() throws IOException {
		for (NucleicAcid aNuc : nas) {
			CycleBasis<Nucleotide, InteractionEdge> cb = new FundamentalCycleBasis<Nucleotide, InteractionEdge>(
					aNuc);
			List<Cycle<Nucleotide, InteractionEdge>> chordlessCB = cb
					.getCycleBasis();
			// get the total number of cycles
			System.out.println("Cycle Basis for PDBID: " + pdbId);
			System.out.println("Total number of cylces: " + chordlessCB.size());
			String b = "";
			for (Cycle<Nucleotide, InteractionEdge> cycle : chordlessCB) {
				// get the cycle length
				int clen = cycle.size();
				// get the interaction edges
				String edgeSummary = "Edges : [";
				List<InteractionEdge> edges = cycle.getEdgeList();
				for (InteractionEdge anEdge : edges) {
					edgeSummary += anEdge.extractEdgeClasses() + "-";
				}
				// remove the last "-"
				edgeSummary = edgeSummary
						.substring(0, edgeSummary.length() - 1) + "] ";
				// make a vertex summary
				String vertexSummary = "Vertices : [";
				List<Nucleotide> vertices = cycle.getVertexList();
				for (Nucleotide n : vertices) {
					vertexSummary += n.getResidueIdentifier()
							+ n.getResiduePosition() + "_" + n.getChainId()
							+ ", ";
				}
				// remove the last ", "
				vertexSummary = vertexSummary.substring(0,
						vertexSummary.length() - 2)
						+ "]";
				// get the basepair classes
				String bpSummary = "BasePair classes: [";
				for (InteractionEdge anEdge : edges) {
					String bpC = anEdge.extractBasePairClasses();
					if (bpC.length() > 0) {
						bpSummary += anEdge.extractBasePairClasses() + ", ";
					}
				}
				bpSummary = bpSummary.substring(0, bpSummary.length() - 2)
						+ "]";
				String printMe = "Cycle Length : " + clen + "\n";
				printMe += "Start Vertex: "
						+ cycle.getStartVertex().getResidueIdentifier()
						+ cycle.getStartVertex().getResiduePosition() + "_"
						+ cycle.getStartVertex().getChainId() + "\n";
				printMe += "End Vertex: "
						+ cycle.getEndVertex().getResidueIdentifier()
						+ cycle.getEndVertex().getResiduePosition() + "_"
						+ cycle.getEndVertex().getChainId() + "\n";
				printMe += edgeSummary + "\n";
				printMe += vertexSummary + "\n";
				printMe += bpSummary + "\n\n";

				System.out.println(printMe);
			}
			FileUtils.write(new File("/tmp/out.output"), b);
		}
	}*/

}
