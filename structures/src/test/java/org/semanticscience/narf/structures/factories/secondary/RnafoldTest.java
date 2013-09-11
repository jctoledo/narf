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
package org.semanticscience.narf.structures.factories.secondary;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticscience.narf.structures.interactions.NucleotideInteraction;
import org.semanticscience.narf.structures.lib.exceptions.InvalidDotBracketNotationException;
import org.semanticscience.narf.structures.lib.exceptions.InvalidResidueException;
import org.semanticscience.narf.structures.lib.exceptions.InvalidSequenceException;
import org.semanticscience.narf.structures.parts.DotBracketNotation;
import org.semanticscience.narf.structures.parts.Sequence;
import org.semanticscience.narf.structures.secondary.PredictedSecondaryStructure;

/**
 * @author Jose Cruz-Toledo
 * 
 */
public class RnafoldTest {
	static Sequence seq1 = null;
	static Sequence seq2 = null;
	static Sequence seq3 = null;
	static Sequence seq4 = null;
	static List<Sequence> seqL = null;

	@BeforeClass
	public static void oneTimeSetUp() throws InvalidSequenceException,
			InvalidResidueException {
		seq1 = new Sequence(
				"CGGGATCCTAATGACCAAGGGTGGGAGGGAGGGGGTCATTAAATCCAGTATCAACACGCCACGATGGGATCACCGCCATGGGCCGTCCCACTGGTGCCAGTCGGATAGTGTTCCTATAGTGAGTCGTATTAGAA");
		seq2 = new Sequence(
				"TGGTGGCTGTAGGTCAGCATCTGATCGGGTGTGGGTGGCGTAAAGGGAGCATCGGACAACG");
		seq3 = new Sequence("CCGGCCAAGGGTGGGAAGGAGGGGGCCGG");
		seqL = new ArrayList<Sequence>();
		seqL.add(seq1);
		seqL.add(seq2);
		seqL.add(seq3);

	}

	@AfterClass
	public static void afterTests() {
		seq1 = null;
		seq2 = null;
		seq3 = null;
		seqL = null;
	}

	@Test
	public void foldInput1() throws InvalidDotBracketNotationException,
			InvalidSequenceException, IOException {

		PredictedSecondaryStructureFactory p = new Rnafold();
		Set<PredictedSecondaryStructure> x = p.getStructures(seq1);
		if (x.size() == 1) {
			Iterator<PredictedSecondaryStructure> itr = x.iterator();
			while (itr.hasNext()) {
				PredictedSecondaryStructure pss = itr.next();
				assertEquals(-41.43, pss.getMinimumFreeEnergy(), 0.001);
			}
		}
	}

	@Test
	public void foldInputList(){
		List<Double> expected = new ArrayList<Double>();
		expected.add(-41.43);
		expected.add(-14.4);
		expected.add(-10.03);
		List<Double> result = new ArrayList<Double>();
		
		for(Sequence aSeq: seqL){
			PredictedSecondaryStructureFactory p = new Rnafold();
			try {
				Set<PredictedSecondaryStructure> x = p.getStructures(aSeq);
				
				Iterator<PredictedSecondaryStructure> itr = x.iterator();
				while (itr.hasNext()) {
					PredictedSecondaryStructure pss = itr.next();
					Set<NucleotideInteraction> s =pss.getInteractions();
					result.add(pss.getMinimumFreeEnergy());
				}
			} catch (InvalidDotBracketNotationException e) {
				e.printStackTrace();
			} catch (InvalidSequenceException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		assertEquals(expected, result);
	}
}
