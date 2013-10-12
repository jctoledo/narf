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
package org.semanticscience.narf.graphs.lib;

import static org.junit.Assert.fail;

import java.io.File;
import java.net.URL;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticscience.narf.graphs.lib.cycles.FundamentalCycleBasis;
import org.semanticscience.narf.graphs.nucleicacid.ExtractedNucleicAcid;
import org.semanticscience.narf.graphs.nucleicacid.InteractionEdge;
import org.semanticscience.narf.graphs.nucleicacid.NucleicAcid;
import org.semanticscience.narf.structures.parts.Nucleotide;

/**
 * @author  Jose Cruz-Toledo
 *
 */
public class RDFUtilTest {
	/**
	 * The FCB that will be RDFized
	 */
	private static FundamentalCycleBasis<Nucleotide, InteractionEdge> fcb = null;
	/**
	 * The PDBId of the file that will be used in this test
	 */
	private static String pdbId = "1Y26";
	/**
	 * The directory where the pdb files that will be used for this test
	 * are stored
	 */
	private static File pdbFilesDir = null;
	/**
	 * The directory where the output of this test will be stored
	 */
	private static File outputDir = null;
	/**
	 * The output of X3DNADSSR
	 */
	private static Set<NucleicAcid> nucs = null;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//download a pdb file
		URL aURL = new URL("http://www.rcsb.org/pdb/files/" + pdbId + ".pdb");
		//make a temporary direcotry
		pdbFilesDir = new File(FileUtils.getTempDirectoryPath()+"/pdb_files");
		File aPdbFile = new File(pdbFilesDir+ "/" + pdbId
				+ ".pdb");
		FileUtils.copyURLToFile(aURL, aPdbFile);
		
		outputDir = new File(FileUtils.getTempDirectory()+"/rdfutil_output");
		FileUtils.forceMkdir(outputDir);
		nucs = ExtractedNucleicAcid.x3dnaDssr(aPdbFile);
		//now compute the fundamental cycle basis
		if(nucs.size() == 1){
			for(NucleicAcid aNuc: nucs){
				
			}
		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		FileUtils.forceDeleteOnExit(pdbFilesDir);
		nucs = null;
		fcb = null;
	}

	@Test
	public void test() {
		
		System.out.println(nucs);
		
	}

}
