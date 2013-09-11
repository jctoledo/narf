/**
 * Copyright (c) 2011 by Jose Cruz-Toledo
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

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.semanticscience.narf.structures.parts.PDBAtom;


/**
 * @author Jose Cruz-Toledo
 *
 */
public class PDBAtomListTest {
	private PDBAtomList anAtomList;
	private PDBAtom one;
	private PDBAtom two;
	private PDBAtom three;
	
	
	@Before
	public void setUp(){
		anAtomList = new PDBAtomList();
		one = new PDBAtom();
		two = new PDBAtom();
		three = new PDBAtom();
	}
	
	@After
	public void tearDown(){
		anAtomList = null;
		one = null;
		two = null;
		three = null;
	}
	@Test
	public void createList(){
		one =  new PDBAtom(1, "a", "b", "c", 1, 1, 1, 1, 1, 1, "d");
		two =  new PDBAtom(2, "e", "f", "g", 1, 1, 1, 1, 1, 1, "h");
		three =  new PDBAtom(3, "i", "j", "k", 1, 1, 1, 1, 1, 1, "l");
		anAtomList.add(three);
		anAtomList.add(two);
		anAtomList.add(one);
		assertEquals(3, anAtomList.size());
	} 

}
