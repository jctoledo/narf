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
package org.semanticscience.narf.structures.ext;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticscience.narf.structures.lib.exceptions.InvalidResidueException;
import org.semanticscience.narf.structures.lib.exceptions.InvalidSequenceException;
import org.semanticscience.narf.structures.parts.Sequence;

/**
 * @author Jose Cruz-Toledo
 * 
 */
public class AptamerSequenceTest {
	static Sequence seq1 = null;
	static String seqType = null;
	static Double avgKD = null;
	static URL aURL = null;
	

	@BeforeClass
	public static void oneTimeSetUp() {
		try {
			seq1 = new Sequence(
					"CGGGATCCTAATGACCAAGGGTGGGAGGGAGGGGGTCATTAAATCCAGTATCAACACGCCACGATGGGATCACCGCCATGGGCCGTCCCACTGGTGCCAGTCGGATAGTGTTCCTATAGTGAGTCGTATTAGAA");
			seqType = "DNA";
			avgKD = 1E-32;
			aURL = new URL("http://bob.org/example");
		} catch (InvalidSequenceException e) {
			e.printStackTrace();
		} catch (InvalidResidueException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void afterTests() {
		seq1 = null;
	}

	@Test
	public void MFETest() {
		AptamerSequence as = new AptamerSequence(seq1, seqType, avgKD, aURL);
		assertEquals(-41.43, as.getRNAFoldPrediction().getMinimumFreeEnergy(), 0.0001);
	}

}
