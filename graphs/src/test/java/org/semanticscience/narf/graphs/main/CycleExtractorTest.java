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
package org.semanticscience.narf.graphs.main;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticscience.narf.graphs.nucleicacid.NucleicAcid;
import org.semanticscience.narf.graphs.nucleicacid.PredictedNucleicAcid;
import org.semanticscience.narf.structures.lib.exceptions.InvalidDotBracketNotationException;
import org.semanticscience.narf.structures.lib.exceptions.InvalidResidueException;
import org.semanticscience.narf.structures.lib.exceptions.InvalidSequenceException;
import org.semanticscience.narf.structures.parts.Sequence;

/**
 * @author Jose Cruz-Toledo
 * 
 */
public class CycleExtractorTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/*@Test
	public void test1() {
		String[] args = new String[] {
				"-inputSeqFile",
				"/home/jose/Documents/research/aptamerbase_trends/data/dbn-distint-clean-sample.csv",
				"-outputDir",
				"/tmp/poto",
				"-outputFormat", "tsv" };
		try {
			CycleExtractor.main(args);
		} catch (Exception e) {uh
			e.printStackTrace();
		}
	}*/
	
	/*@Test
	public void test44(){
		//create
		try{
			Sequence seq = new Sequence("GGGAAUGGAUCCACAUCUACGAAUUCCGCGCAGGCGCCGUGAGACGGCGAUAUGCUUCACUGCAGACUUGACGAAGCUU");
			Set<NucleicAcid> nas = PredictedNucleicAcid.rnafold(seq);
			Iterator<NucleicAcid> itr = nas.iterator();
			while(itr.hasNext()){
				NucleicAcid na = itr.next();
				System.out.println(na);
			}
		}catch(InvalidResidueException e){
			e.printStackTrace();
		}catch (InvalidSequenceException e2){
			e2.printStackTrace();
		} catch (InvalidDotBracketNotationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}*/
	/*@Test 
	public void test3(){
		String[] args = new String[] {
				"-inputSeqFile",
				"/home/jose/Documents/research/aptamerbase_trends/data/input.sequences.clean",
				"-outputDir",
				"/tmp/pico/rdf",
				"-outputFormat", "RDF" };
		try {
			CycleExtractor.main(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test2() {
		String[] args = new String[] {
				"-inputPDBDir",
				"/home/jose/Documents/research/aptamers_inPDB/pdb_retriever/Nov2013/pdb_sample/rna",
				"-outputDir",
				"/tmp/pico/tsv",
				"-outputFormat", "tsv" };
		try {
			CycleExtractor.main(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
	@Test
	public void test3() {
		String[] args = new String[] {
				"-inputPDBDir",
				"/tmp/poto",
				"-outputDir",
				"/tmp/pico/rdf",
				"-outputFormat", "RDF" };
		try {
			CycleExtractor.main(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
