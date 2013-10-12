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
import org.semanticscience.narf.graphs.nucleicacid.NucleicAcid;

/**
 * @author  Jose Cruz-Toledo
 *
 */
public class RDFUtilTest {
	/**
	 * The PDBId of the file that will be used in this test
	 */
	private static String pdbId = "1Y26";
	/**
	 * The pdb file that will be used for this test
	 */
	private static File pdbFile = null;
	/**
	 * The output of X3DNADSSR
	 */
	Set<NucleicAcid> nucs = null;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//download a pdb file
		URL aURL = new URL("http://www.rcsb.org/pdb/files/" + pdbId + ".pdb");
		pdbFile = new File(FileUtils.getTempDirectoryPath() + "/" + pdbId
				+ ".pdb");
		FileUtils.copyURLToFile(aURL, pdbFile);
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		FileUtils.forceDeleteOnExit(pdbFile);
		
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
