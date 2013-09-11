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
package org.semanticscience.narf.structures.parts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.semanticscience.narf.structures.lib.exceptions.InvalidResidueException;
import org.semanticscience.narf.structures.lib.exceptions.InvalidSequenceException;
import org.semanticscience.narf.structures.lib.exceptions.NonConsecutiveNucleotideException;


/**
 * @author Jose Cruz-Toledo
 *
 */
public class SequenceTest {
	private Sequence sequence;
	
	@Before
	public void setUp()throws InvalidSequenceException, InvalidResidueException{
		sequence = new Sequence("AAACACATGATACTA", 13, "test DNA sequence");
	}
	
	@After
	public void tearDown(){
		sequence = null;
	}
	
	@Test
	public void testGetGCContent(){
		double d = sequence.getGCContent();
		assertEquals(new Double(20.0),new Double(d));
	}
	
	@Test
	public void testGetLength(){
		assertEquals(15, sequence.getLength());
	}
	
	@Test
	public void containsNucleotideAtPosition(){
		assertEquals(false, sequence.containsNucleotideAtPosition(1));
		assertEquals(false, sequence.containsNucleotideAtPosition(28));
		for (int i=13; i<28;i++){
			assertEquals(true, sequence.containsNucleotideAtPosition(i));
		}
	}
	
	@Test
	public void getNucleotideAtPosition() throws InvalidResidueException{
		Nucleotide t = new Nucleotide(13, "A");
		Nucleotide q = new Nucleotide(16, "c");
		Nucleotide s = new Nucleotide(27, "A");
		assertEquals(t, sequence.getNucleotideAtPosition(13));
		assertEquals(q, sequence.getNucleotideAtPosition(16));
		assertEquals(s, sequence.getNucleotideAtPosition(27));
		assertEquals(null, sequence.getNucleotideAtPosition(1));
		assertNull(sequence.getNucleotideAtPosition(12));
	}
	
	@Test (expected = InvalidSequenceException.class)
	public void checkStringSequence() throws InvalidSequenceException, InvalidResidueException{
		String aSeq = "EQP";
		Sequence s = new Sequence(aSeq);
	}
	
	@Test (expected = NonConsecutiveNucleotideException.class)
	public void sequenceFromSet() throws InvalidResidueException, NonConsecutiveNucleotideException{
		Set<Nucleotide> aSet = new HashSet<Nucleotide>();
		aSet.add(new Nucleotide(13, "A"));
		aSet.add(new Nucleotide(15, "A"));
		aSet.add(new Nucleotide(14, "A"));
		aSet.add(new Nucleotide(17, "A"));
		Sequence s = new Sequence(aSet);
	}
	
	@Test
	public void makeNucleotides() throws InvalidSequenceException, InvalidResidueException{
		String s = "ACT";
		int t = 5;
		Sequence v = new Sequence(s, t);
		SortedMap<Integer, Nucleotide> c = v.makeNucleotides(s, t);
		Integer i = c.firstKey();
		Integer j = c.lastKey();
		assertEquals(2, j-i);
	}
	
	@Test
	public void firstNucleotide() throws InvalidResidueException{
		assertEquals(new Nucleotide(13, "A"), sequence.getFirstNucleotide());
	}
	
	@Test
	public void lastNucleotide() throws InvalidResidueException{
		assertEquals(new Nucleotide(27, "A"), sequence.getLastNucleotide());
	}
	
	@Test
	public void getSubSequence(){
		
		
	}
}
