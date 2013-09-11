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
package org.semanticscience.narf.structures.bin;

import java.io.File;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author  Jose Cruz-Toledo
 *
 */
public class AptamerSequenceLoaderTest {
	static File inputFile = null;
	
	@BeforeClass
	public static void oneTimeSetUp() {
		inputFile = new File("/tmp/dnaFasta.fasta.all");
	}
	@Test
	public void getPerResidueProbabilityDistribution(){
		AptamerSequenceLoader asl = new AptamerSequenceLoader(inputFile);
		Map<String, Double> prpd = EntropyCalculator.perResidueProbabilityDistribution(asl.getAptamerSequenceList());
		for(String k: prpd.keySet()){
			System.out.println(k+","+prpd.get(k));
		}		
	}
	
	@Test
	public  void aptamerSequenceLoader(){
		AptamerSequenceLoader asl = new AptamerSequenceLoader(inputFile);
		//System.out.println(asl.getAptamerSequenceList());
		Double d = EntropyCalculator.combinedShannonEntropyPerResidueDNA(asl.getAptamerSequenceList());
		System.out.println(d);
	}
	
}
