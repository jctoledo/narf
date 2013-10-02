/**
 * Copyright (c) 2012  Jose Cruz-Toledo
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticscience.narf.structures.lib.exceptions.InvalidResidueException;

/**
 * @author Jose Cruz-Toledo
 * 
 */
public class NucleicAcid1NBR {
	/**
	 * A set of nucleic acids as extracted from one structure model
	 */
	private static Set<NucleicAcid> nAs;
	/**
	 * A set of URLs to PDB files that you wish to test
	 */
	private static Set<URL> urls = new HashSet<URL>();
	/**
	 * A set of File objects corresponding to the urls above
	 */
	private static Set<File> files = new HashSet<File>();

	@BeforeClass
	public static void oneTimeSetUp() throws FileNotFoundException,
			IOException, InvalidResidueException {
		String pdbId = "1NBR";
		URL aURL = new URL("http://www.rcsb.org/pdb/files/" + pdbId + ".pdb");
		File aFile = new File(FileUtils.getTempDirectoryPath() + "/" + pdbId
				+ ".pdb");
		FileUtils.copyURLToFile(aURL, aFile);
		nAs = ExtractedNucleicAcid.x3dnaDssr(aFile);
	}

	@AfterClass
	public static void oneTimeTearDown() {
		nAs = null;
		files = null;
	}
	
	@Test
	public void TestOne(){
		for (NucleicAcid na : nAs) {
			System.out.println(na.getBasePairs());
		}
	}

	/*@Test
	public void testGetChains() {
		for (NucleicAcid na : nAs) {
			String[] chains = na.getChains();
			assertEquals("A", chains[0]);
		}
	}*/
/*
	@Test
	public void testContainsChain() {
		for (NucleicAcid na : nAs) {
			assertTrue(na.containsChain("X"));
			assertFalse(na.containsChain("bob"));
		}
	}

	@Test
	public void testBasePair() {
		for (NucleicAcid na : nAs) {
			assertEquals(38, na.getBasePairs().size());
		}
	}

	@Test
	public void testBaseStacks() {
		for (NucleicAcid na : nAs) {
			assertEquals(58, na.getBaseStacks().size());
		}
	}

	@Test
	public void testBackboneCounts() {
		for (NucleicAcid na : nAs) {
			assertEquals(70, na.getPhosphodiesterBonds().size());
		}
	}
	
*/
}
