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
package org.semanticscience.narf.structures.factories.tertiary;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticscience.narf.structures.interactions.BasePair;
import org.semanticscience.narf.structures.interactions.NucleotideInteraction;
import org.semanticscience.narf.structures.interactions.PhosphodiesterBond;
import org.semanticscience.narf.structures.lib.exceptions.InvalidResidueException;
import org.semanticscience.narf.structures.tertiary.ExtractedTertiaryStructure;

/**
 * @author Jose Cruz-Toledo
 * 
 */
public class X3DnaDssrTest {
	static File sampleInputFile = null;
	static File sampleInputDir = null;
	static File largeInputFile = null;
	static File sampleOutputDir = null;
	static File twoChains = null;

	private static String path = "/home/jose/Programs/X3DNA/bin";
	

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		twoChains = new File("/home/jose/Documents/research/aptamers_inPDB/pdb_retriever/smallsample/4kyy.pdb");
		sampleInputFile = new File("/home/jose/Documents/research/aptamers_inPDB/pdb_retriever/smallsample/1d16.pdb");
		largeInputFile = new File("/home/jose/Documents/research/aptamers_inPDB/pdb_retriever/smallsample/1FFK.pdb");
		sampleInputDir = new File(
				"/home/jose/Documents/research/aptamers_inPDB/pdb_retriever/sample/pdb/rna");
		sampleOutputDir = new File("/tmp/x3dna-dssr-dir");
		FileUtils.forceMkdir(sampleOutputDir);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		sampleInputFile = null;
		sampleInputDir = null;
		sampleOutputDir = null;
		largeInputFile = null;
		twoChains = null;
	}

	@Test
	public void runOnOneFile() {
		ExtractedTertiaryStructureFactory p = new X3DnaDssr();
		int bpcount = 0;
		int pdbcount = 0;
		try {
			// construct the output file
			File outputFile = new File(FileUtils.getTempDirectoryPath() + "/"
					+ sampleInputFile.getName() + ".out");
			// construct the command array
			String[] cmdArr = new String[] { path + "/x3dna-dssr",
					"-i=" + sampleInputFile.getAbsolutePath(),
					"-o=" + outputFile.getAbsolutePath() };
			Set<ExtractedTertiaryStructure> s = p
					.getStructures(sampleInputFile, cmdArr);
			for (ExtractedTertiaryStructure ext : s) {
				Set<NucleotideInteraction> ni= ext.getInteractions();
				for (NucleotideInteraction aNi : ni) {
					if(aNi instanceof BasePair){
						bpcount ++;
					}
					if(aNi instanceof PhosphodiesterBond){
						pdbcount ++;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch( InvalidResidueException  e){
			e.printStackTrace();
		}
		assertEquals(21, bpcount+pdbcount);
	}
	
	@Test
	public void runlargeFile(){
		ExtractedTertiaryStructureFactory p = new X3DnaDssr();
		int bpcount = 0;
		int pdbcount = 0;
		try {
			// construct the output file
			File outputFile = new File(FileUtils.getTempDirectoryPath() + "/"
					+ largeInputFile.getName() + ".out");
			// construct the command array
			String[] cmdArr = new String[] { path + "/x3dna-dssr",
					"-i=" + largeInputFile.getAbsolutePath(),
					"-o=" + outputFile.getAbsolutePath() };
			Set<ExtractedTertiaryStructure> s = p
					.getStructures(largeInputFile, cmdArr);
			for (ExtractedTertiaryStructure ext : s) {
				Set<NucleotideInteraction> ni= ext.getInteractions();
				for (NucleotideInteraction aNi : ni) {
					if(aNi instanceof BasePair){
						bpcount ++;
					}
					if(aNi instanceof PhosphodiesterBond){
						pdbcount ++;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch( InvalidResidueException  e){
			e.printStackTrace();
		}
		assertEquals(8201, pdbcount+bpcount);
	}
	
	@Test
	public void runFileWithTwoChains(){
		ExtractedTertiaryStructureFactory p = new X3DnaDssr();
		int bpcount = 0;
		int pdbcount = 0;
		try {
			// construct the output file
			File outputFile = new File(FileUtils.getTempDirectoryPath() + "/"
					+ twoChains.getName() + ".out");
			// construct the command array
			String[] cmdArr = new String[] { path + "/x3dna-dssr",
					"-i=" + twoChains.getAbsolutePath(),
					"-o=" + outputFile.getAbsolutePath() };
			Set<ExtractedTertiaryStructure> s = p
					.getStructures(twoChains, cmdArr);
			for (ExtractedTertiaryStructure ext : s) {
				Set<NucleotideInteraction> ni= ext.getInteractions();
				for (NucleotideInteraction aNi : ni) {
					if(aNi instanceof BasePair){
						bpcount ++;
					}
					if(aNi instanceof PhosphodiesterBond){
						pdbcount ++;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch( InvalidResidueException  e){
			e.printStackTrace();
		}
		assertEquals(49, pdbcount+bpcount);
	}
}
