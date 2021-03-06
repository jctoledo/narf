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
import org.semanticscience.narf.structures.lib.exceptions.NonConsecutiveNucleotideException;
import org.semanticscience.narf.structures.parts.Nucleotide;

/**
 * @author  Jose Cruz-Toledo
 *
 */
public class PhosphodiesterBondTest {
	private PhosphodiesterBond aPdb = null; //3-4
	private PhosphodiesterBond aPdb2 = null;//4-3
	
	@Before 
	public void setUp() throws InvalidResidueException{
		//create 2 phosphodiester bonds with the same nucleotides but in different order
		Nucleotide n1 = new Nucleotide(4, "G");
		Nucleotide n2 = new Nucleotide(3, "G");
		try {
			aPdb = new PhosphodiesterBond(n2, n1);
			aPdb2 = new PhosphodiesterBond(n1, n2);
		} catch (NonConsecutiveNucleotideException e) {
			e.printStackTrace();
		}
	}
	
	@After
	public void tearDown(){
		aPdb = null;
		aPdb2 = null;
	}
	
	@Test
	public void testEquals(){
		boolean f = true;
		if(!aPdb.equals(aPdb2)){
			f = false;
		}
		assertFalse(f);
	}

}
