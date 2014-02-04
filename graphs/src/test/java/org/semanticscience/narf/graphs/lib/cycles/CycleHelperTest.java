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

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticscience.narf.graphs.nucleicacid.ExtractedNucleicAcid;
import org.semanticscience.narf.graphs.nucleicacid.InteractionEdge;
import org.semanticscience.narf.graphs.nucleicacid.NucleicAcid;
import org.semanticscience.narf.structures.parts.Nucleotide;

/**
 * @author Jose Cruz-Toledo
 * 
 */
public class CycleHelperTest {
	private static String pdbId = "1Y26";
	private static Set<NucleicAcid> nas;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		/*URL aURL = new URL("http://www.rcsb.org/pdb/files/" + pdbId + ".pdb");
		File aFile = new File(FileUtils.getTempDirectoryPath() + "/" + pdbId
				+ ".pdb");
		FileUtils.copyURLToFile(aURL, aFile);
		nas = ExtractedNucleicAcid.x3dnaDssr(aFile);*/
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		nas = null;
	}
	
	@Test
	public void test(){
		String haystack = "hello2342hZzZzZZelhlolkjsadlUHsakjhello";
		String needle = "z";
		int c = CycleHelper.countOccurences(haystack, needle);
		System.out.println(c);
	}
	
	/*@SuppressWarnings("unused")
	@Test 
	public void test() {
		for (NucleicAcid aNuc : nas) {
			List<Cycle<Nucleotide, InteractionEdge>> mcb = aNuc
					.getMinimumCycleBasis();
			System.out.println("Total cycles in mcb: "+mcb.size());
			
			for (Cycle<Nucleotide, InteractionEdge> aCycle : mcb) {
				BigDecimal d = CycleHelper.findMinmalNormalization(aNuc, aCycle,false);
				// get all rotations of the cycle c
				List<Cycle<Nucleotide, InteractionEdge>> rotatedCycles = CycleHelper
						.findAllRotations(aNuc, aCycle);
				for (Cycle<Nucleotide, InteractionEdge> rot:rotatedCycles){
					BigDecimal i = CycleHelper.normalizeCycle(rot,false);
					System.out.println("norm: "+i);
				}
				System.out.println("min: "+d);
				System.out.println("*****************");
			}
		}
	}*/
	
}
