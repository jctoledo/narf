/**
 * Copyright (c) 2012 by Jose Cruz-Toledo
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticscience.narf.structures.lib.exceptions.InvalidResidueException;


/**
 * JUnit test for nucleic acids that are generated using tertiary structure annotators.
 * @author Jose Cruz-Toledo
 *
 */
public class ExtractedNucleicAcidTest {

	private static File nmrSampleFile; 
	private static File xraySampleFile;
	private static String nmrSampleId = "1NBR";
	private static String xraySampleId = "1Y26";
	
	private static Set<NucleicAcid> nmrMcaModels;
	private static Set<NucleicAcid> xrayMcaModels;
	
	
	
	@BeforeClass
	public static void oneTimeSetUp()throws Exception{
		URL urOne = new URL("http://www.rcsb.org/pdb/files/"+nmrSampleId+".pdb");
		URL urTwo = new URL("http://www.rcsb.org/pdb/files/"+xraySampleId+".pdb");
		
		nmrSampleFile = new File(FileUtils.getTempDirectoryPath()+"/"+nmrSampleId+".pdb");
		xraySampleFile = new File(FileUtils.getTempDirectoryPath()+"/"+xraySampleId+".pdb");
		
		FileUtils.copyURLToFile(urOne, nmrSampleFile);
		FileUtils.copyURLToFile(urTwo, xraySampleFile);
		
		nmrMcaModels = ExtractedNucleicAcid.mcannotate(nmrSampleFile);
		xrayMcaModels = ExtractedNucleicAcid.mcannotate(xraySampleFile);
	
	}
	
	@AfterClass
	public static void oneTimeTearDown()throws Exception{
		nmrSampleFile = null;
		xraySampleFile = null;
		xrayMcaModels = null;
		nmrMcaModels = null;
	}
	
	@Test
	public void testXrayModel(){
		
		
		
		Iterator <NucleicAcid> itr = xrayMcaModels.iterator();
		while(itr.hasNext()){
			NucleicAcid na = itr.next();
			System.out.println(na.getBasePairs());
			System.out.println("-------------");
			
		}/**/
		
	}
	
	
	/*
	@Before
	public void setUp() throws FileNotFoundException, IOException, InvalidResidueException{
		URL aURL = new URL("http://www.rcsb.org/pdb/files/1Y26.pdb");
		aPDBFile = new File(FileUtils.getTempDirectoryPath()+"/1Y26.pdb");
		FileUtils.copyURLToFile(aURL, aPDBFile);
	}
	
	@After
	public void tearDown(){
		aPDBFile = null;
	}
	
	@Test
	public void testMcannotate() throws FileNotFoundException, IOException, InvalidResidueException{

		Set<NucleicAcid> nucleicAcids = ExtractedNucleicAcid.mcannotate(aPDBFile);

		NucleicAcid nucleicAcid = nucleicAcids.iterator().next();
		assertEquals("MC-Annotate:1Y26_M_1", nucleicAcid.getUniqueIdentifier());

	}
	
	@Test
	public void testRnaview() throws FileNotFoundException, IOException, InvalidResidueException{

		Set<NucleicAcid> nucleicAcids = ExtractedNucleicAcid.rnaview(aPDBFile);

		NucleicAcid nucleicAcid = nucleicAcids.iterator().next();
		assertEquals("RNAView:1Y26_M_1", nucleicAcid.getUniqueIdentifier());

	}*/
}
