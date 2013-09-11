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
package org.semanticscience.narf.structures.lib;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticscience.narf.structures.lib.exceptions.InvalidResidueException;
import org.semanticscience.narf.structures.parts.Nucleotide;

/**
 * @author  Jose Cruz-Toledo
 *
 */
public class PdbHelperTest{
	private static File aPdbFile;
	private static File outputDirectory;
	private static String pdbId = "1NBR";
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void oneTimeSetUp() throws Exception {
		
		URL aURL = new URL("http://www.rcsb.org/pdb/files/"+pdbId+".pdb");
		aPdbFile = new File(FileUtils.getTempDirectoryPath()+"/"+pdbId+".pdb");
		outputDirectory = FileUtils.getTempDirectory();
		FileUtils.copyURLToFile(aURL, aPdbFile);
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void oneTimeTearDown() throws Exception {
		aPdbFile = null;
		outputDirectory = null;
	}
	
	@Test
	public void findPdbIdTest(){
		try {
			assertEquals(pdbId, PdbHelper.findPdbId(aPdbFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test 
	public void findNumberOfModelsTest(){
		try {
			assertEquals(15,PdbHelper.findNumberOfModels(aPdbFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void getStartingPositionsTest(){
		Map<String, Integer> aMap = new HashMap<String,Integer>();
		try {
			aMap = PdbHelper.getStartingPositions(aPdbFile);
			Integer i = aMap.get("A");
			assertEquals(1,i.intValue());			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	@Test
	public void getEndingPositionsTest(){
		Map<String, Integer> aMap = new HashMap<String,Integer>();
		try {
			aMap = PdbHelper.getEndingPositions(aPdbFile);
			Integer i = aMap.get("A");
			assertEquals(29,i.intValue());			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	@Test
	public void getHeaderFromPDBFileTest(){
		try {
			String s = PdbHelper.getHeaderFromPDBFile(aPdbFile);
			assertEquals(8254, s.length());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void extractModelFromPDBFileTest() {
		try {
			File f = PdbHelper.extractModelFromPDB(aPdbFile, outputDirectory, pdbId, 2);
			String s = FileUtils.readFileToString(f);
			assertEquals(9105125, s.length());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void getNucleotideMappingTest(){
		Map<String,Map<Integer,Nucleotide>> aMap = new HashMap<String, Map<Integer,Nucleotide>>();
		try {
			aMap = PdbHelper.getNucleotideMapping(aPdbFile);
			assertEquals(29,aMap.get("A").size());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidResidueException e) {
			e.printStackTrace();
		}
	}


}
