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
package org.semanticscience.narf.structures.lib;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticscience.narf.structures.interactions.BasePair;
import org.semanticscience.narf.structures.parts.Edge;
import org.semanticscience.narf.structures.parts.Nucleotide;

/**
 * @author Jose Cruz-Toledo
 * 
 */
public class InferNucleotideParametersTest {
	private static Nucleotide n1 = null;
	private static Nucleotide n2 = null;
	private static BasePair bp = null;
	private static String edge1Label = null;
	private static String edge2Label = null;
	private static String glycosidicBondOrientation = null;
	private static String basePairOrientation = null;
	private static Edge e1 = null;
	private static Edge e2 = null;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		n1 = new Nucleotide(1, "C");
		n2 = new Nucleotide(10, "G");
		edge1Label = "W";
		edge2Label = ".";
		glycosidicBondOrientation = "c";
		// create the edges
		e1 = new Edge(edge1Label, InferNucleotideParameters.inferSubEdges(n1, edge1Label));
		e2 = new Edge(edge2Label, InferNucleotideParameters.inferSubEdges(n2, edge2Label));
		basePairOrientation = InferNucleotideParameters.findStrandBasePairOrientation(e1, e2, glycosidicBondOrientation);
		bp = new BasePair(n1, n2, e1, e2, glycosidicBondOrientation, basePairOrientation);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		n1 = null;
		n2 = null;
		bp = null;
		edge1Label = null;
		edge2Label = null;
		e1 = null;
		e2 = null;
		glycosidicBondOrientation = null;
	}

	@Test
	public void testOne() {
		assertEquals(null, basePairOrientation);
	}

}
