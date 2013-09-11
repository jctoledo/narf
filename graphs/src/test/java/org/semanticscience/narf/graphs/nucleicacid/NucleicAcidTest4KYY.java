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

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticscience.narf.graphs.lib.cycles.Cycle;
import org.semanticscience.narf.graphs.lib.cycles.CycleBasis;
import org.semanticscience.narf.graphs.lib.cycles.FundamentalCycleBasis;
import org.semanticscience.narf.structures.parts.Nucleotide;

/**
 * @author Jose Cruz-Toledo
 * 
 */
public class NucleicAcidTest4KYY {
	private static String pdbId = "4KYY";
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
	public void checkVertexCount() {
		for (NucleicAcid aNucAc : nas) {
			//get the set of vertices
			Set<Nucleotide> ns = aNucAc.vertexSet();
			assertEquals(16, ns.size());
		}		
	}
	@Test
	public void testEdgeCount(){
		//count the number of non-unique edges
		int edgeCount = 0;
		for(NucleicAcid aNucAc : nas){
			Set<Nucleotide> ns = aNucAc.vertexSet();
			for (Nucleotide aNuc : ns) {
				Set<InteractionEdge> ied = aNucAc.edgesOf(aNuc);
				edgeCount += ied.size();
			}
		}
		assertEquals(42, edgeCount);
	}
	
	@Test
	public void testingCycleBasis(){
		for(NucleicAcid aNuc: nas){
			CycleBasis<Nucleotide,InteractionEdge> cb = new FundamentalCycleBasis<Nucleotide, InteractionEdge>(aNuc);
			List<Cycle<Nucleotide,InteractionEdge>> fcb = cb.getCycleBasis();
			for (Cycle<Nucleotide, InteractionEdge> cycle : fcb) {
				System.out.println(cycle+"\n");
			}
			
		}
	}
}
