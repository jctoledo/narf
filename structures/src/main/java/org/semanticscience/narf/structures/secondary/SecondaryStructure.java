/**
 * Copyright (c) 2011 William Greenwood and Jose Cruz-Toledo
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
package org.semanticscience.narf.structures.secondary;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.semanticscience.narf.structures.Structure;
import org.semanticscience.narf.structures.parts.DotBracketNotation;
import org.semanticscience.narf.structures.parts.Sequence;
/**
 * A class to represent secondary structures of nucleic acids
 * @author William Greenwood
 * @author Jose Cruz-Toledo
 *
 */

public class SecondaryStructure extends Structure {

	/**
	 * The dot bracket notation of the nucleic acid structure
	 */
	protected final DotBracketNotation dotBracketNotation;

	/**
	 * Construct a nucleic acid secondary structure from a dot bracket notation.
	 * 
	 * @param aModelNumber
	 *            model numbesssr of the nucleic acid secondary structure
	 *            conformation
	 * @param aDotBracketNotation
	 *            dot bracket notation of the nucleic acid secondary structure
	 */
	public SecondaryStructure(int aModelNumber,
			DotBracketNotation aDotBracketNotation) {
		super(aModelNumber, inferChains(aDotBracketNotation),
				aDotBracketNotation.getInteractions());
		dotBracketNotation = aDotBracketNotation;
	}

	/**
	 * Construct a nucleic acid secondary structure from a dot bracket notation
	 * 
	 * @param aDBN
	 *            dot bracket notation string of the nucleic acid secondary
	 *            structure
	 */
	public SecondaryStructure(DotBracketNotation aDBN) {
		this(1, aDBN);
	}

	/**
	 * Create a mapping of chain identifiers to the sequences in the dot bracket
	 * notation
	 * 
	 * @param aDotBracketNotation
	 *            dot bracket notation of the nucleic acid secondary structure
	 * @return a map of chain identifiers to the sequence of the respective
	 *         chain
	 */
	private static Map<String, Sequence> inferChains(
			DotBracketNotation aDotBracketNotation) {
		Map<String, Sequence> sequences = new HashMap<String, Sequence>();

		sequences.put("A", aDotBracketNotation.getSequence());

		return sequences;
	}

	/**
	 * Get the dot bracket notation of the nucleic acid secondary structure
	 * 
	 * @return the dot bracket notation
	 */
	public DotBracketNotation getDotBracketNotation() {
		return dotBracketNotation;
	}

	/**
	 * This method creates a FASTA like output file with the following form
	 * >AGGAGAGAAACGUA ...[((..))]..
	 * 
	 * @param outputFile
	 *            the file where the output will be written
	 * @return <code>true</code> if the file was written, otherwise
	 *         <code>false</code>
	 * @throws IOException
	 *             if any IO occurs writing the file
	 */
	public final boolean exportAsFasta(File outputFile) throws IOException {

		if ((outputFile == null) || (outputFile.isDirectory())) {
			return false;
		}

		BufferedWriter out = new BufferedWriter(
				new FileWriter(outputFile, true));
		out.write(this.toString());
		out.close();

		return true;
	}

	@Override
	public String toString() {
		String returnMe = ">secondary structure\n";
		returnMe += this.getSequenceByChain("A") + "\n";
		returnMe += this.getDotBracketNotation() + "\n\n";

		return returnMe;
	}

}
