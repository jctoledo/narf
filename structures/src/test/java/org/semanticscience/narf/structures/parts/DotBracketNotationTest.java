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

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.semanticscience.narf.structures.lib.exceptions.InvalidDotBracketNotationException;
import org.semanticscience.narf.structures.lib.exceptions.InvalidResidueException;
import org.semanticscience.narf.structures.lib.exceptions.InvalidSequenceException;

/**
 * Various tests for the dot bracket notation class
 * 
 * @author Jose Cruz-Toledo
 * 
 */
public class DotBracketNotationTest {
	public DotBracketNotation dbn;
	public DotBracketNotation dbnTwo;

	

	@Before
	public void setUp() throws InvalidDotBracketNotationException,
			InvalidSequenceException, InvalidResidueException {

		dbn = new DotBracketNotation(
				new Sequence(
						"GGGAATGGATCCACATCTACGAATTCGTGTGCGTCCAGTTTAAGAACAAGCAGCAGTTCACTGCAGACTTGACGAAGCTTAGCATGCATGATGCATGA"),
				".(((.(((((..(((((...{{.....)))))...(((..}}....))).))))).))).[[..(((((.((((.]].)))).).)))).........");
	}

	@After
	public void tearDown() {
		dbn = null;
		dbnTwo = null;
	}

	@Test(expected = InvalidDotBracketNotationException.class)
	public void testInvalidDotBracketNotationException()
			throws InvalidDotBracketNotationException, InvalidSequenceException, InvalidResidueException {
		

		dbnTwo = new DotBracketNotation(
				new Sequence(
						"GGGAATGGATCCACATCTACGAATTCGTGTGCGTCCAGTTTAAGAACAAGCAGCAGTTCACTGCAGACTTGACGAAGCTTAGCATGCATGATGCATGA"),
				".(((.(((((..(((((...{{.....)))))...(((..}}....))).))))).))).[[..(((((.((((.]].)))).).))))...]]......");

	}

	@Test(expected = InvalidDotBracketNotationException.class)
	public void testInvalidDotBracketNotationExceptionTwo()
			throws InvalidDotBracketNotationException, InvalidSequenceException, InvalidResidueException {

		dbnTwo = new DotBracketNotation(
				new Sequence(
						"GGGAATGGATCCACATCTACGAATTCGTGTGCGTCCAGTTTAAGAACAAGCAGCAGTTCACTGCAGACTTGACGAAGCTTAGCATGCATGATGCATGA"),
				"...(.(((((..(((((...{......)))))...(((..}}....))).))))).))).[[..(((((.((((.]].)))).).)))..........");

	}

	@Test
	public void testGetDotBracketOfNucleotide() throws InvalidResidueException {
		assertEquals(new Character('.'),
				dbn.getDotBracketCharacterForNucleotide(new Nucleotide(1, "G")));
		assertEquals(new Character('('),
				dbn.getDotBracketCharacterForNucleotide(new Nucleotide(9, "A")));
		assertEquals(new Character('{'),
				dbn.getDotBracketCharacterForNucleotide(new Nucleotide(21, "G")));
		assertEquals(new Character(')'),
				dbn.getDotBracketCharacterForNucleotide(new Nucleotide(30, "T")));
		assertEquals(new Character('}'),
				dbn.getDotBracketCharacterForNucleotide(new Nucleotide(41, "T")));
		assertEquals(new Character('['),
				dbn.getDotBracketCharacterForNucleotide(new Nucleotide(62, "T")));
		assertEquals(new Character(']'),
				dbn.getDotBracketCharacterForNucleotide(new Nucleotide(77, "G")));
		assertNull(dbn.getDotBracketCharacterForNucleotide(new Nucleotide(1000, "U")));

	}
	@Test
	public void testDotBracketAtPosition() {
		assertEquals(new Character('.'), dbn.getDotBracketCharacterAtPosition(1));
		assertEquals(new Character('('), dbn.getDotBracketCharacterAtPosition(2));
		assertEquals(new Character('.'), dbn.getDotBracketCharacterAtPosition(5));
		assertEquals(new Character(')'), dbn.getDotBracketCharacterAtPosition(30));
		assertEquals(new Character('}'), dbn.getDotBracketCharacterAtPosition(41));
		assertEquals(new Character('['), dbn.getDotBracketCharacterAtPosition(62));
		assertEquals(new Character(']'), dbn.getDotBracketCharacterAtPosition(77));
	}
	
	@Test
	public void testGetInteractions() {
		
		assertEquals(true, dbn.getInteractions().size() == 126);
	}

}