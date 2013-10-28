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

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.graph.SimpleGraph;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticscience.narf.graphs.lib.cycles.exceptions.CycleException;
import org.semanticscience.narf.graphs.nucleicacid.ExtractedNucleicAcid;
import org.semanticscience.narf.graphs.nucleicacid.InteractionEdge;
import org.semanticscience.narf.graphs.nucleicacid.NucleicAcid;
import org.semanticscience.narf.structures.parts.Nucleotide;

import org.openscience.cdk.ringsearch.cyclebasis.CycleBasis;
import org.openscience.cdk.ringsearch.cyclebasis.SimpleCycle;

/**
 * @author  Jose Cruz-Toledo
 *
 */
public class SimpleCycleConverterTest {
	private static String pdbId = "1FFK";
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
	public void test() throws CycleException {
		for (NucleicAcid aNuc : nas){
			SimpleGraph sg = aNuc.makeSimpleGraph();
			CycleBasis cb = new CycleBasis(sg);
			List<SimpleCycle> cycles = (List<SimpleCycle>)cb.cycles();
			for(SimpleCycle sc:cycles){
				//the edges of this cycle
				List<InteractionEdge> el = new ArrayList<InteractionEdge>();
				//populate the edgelist
				Iterator<Edge> sce_itr = sc.edgeSet().iterator();
				while(sce_itr.hasNext()){
					Edge anEdge = sce_itr.next();
					Nucleotide source =(Nucleotide) anEdge.getSource();
					Nucleotide target = (Nucleotide) anEdge.getTarget();
					Set<InteractionEdge> x = aNuc.getAllEdges(source, target);
					el.addAll(x);
				}
				//now order the edges of this cycle
				el = SimpleCycleConverter.sortEdgeList(el);
				Nucleotide fv = SimpleCycleConverter.findFirstNucleotide(el);
				Nucleotide lv = SimpleCycleConverter.findLastNucleotide(el);
				
				//now create a cycle from this information
				Cycle<Nucleotide, InteractionEdge> c = new Cycle<Nucleotide, InteractionEdge>(aNuc, fv, lv, el, el.size());
				System.out.println(c);
				
			}
		}
	}

}
