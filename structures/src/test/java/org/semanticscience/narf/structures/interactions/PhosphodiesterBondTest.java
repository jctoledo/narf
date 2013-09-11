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
package org.semanticscience.narf.structures.interactions;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.semanticscience.narf.structures.lib.exceptions.InvalidResidueException;
import org.semanticscience.narf.structures.parts.Nucleotide;

/**
 * @author  Jose Cruz-Toledo
 *
 */
public class PhosphodiesterBondTest {
	private Nucleotide nucA1;
	private Nucleotide nucA2;
	private Nucleotide nucB1;
	private Nucleotide nucB2;
	
	@Before 
	public void setUp() throws InvalidResidueException{
		//create four nucleotides
		nucA1 = new Nucleotide(1, "A");
		nucA2 = new Nucleotide(2, "C");
		nucB1 = new Nucleotide(5, "G");
		nucB2 = new Nucleotide(13, "T");
	}
	
	@After
	public void tearDown(){
		nucA1 = null;
		nucA2 = null;
		nucB1 = null;
		nucB2 = null;
	}
	


}
