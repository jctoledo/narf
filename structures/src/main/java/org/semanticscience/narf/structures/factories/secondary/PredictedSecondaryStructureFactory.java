/**
 * Copyright (c) 2011 William Greenwood
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

import java.io.IOException;
import java.util.Set;

import org.semanticscience.narf.structures.factories.PredictedStructureFactory;
import org.semanticscience.narf.structures.lib.exceptions.InvalidDotBracketNotationException;
import org.semanticscience.narf.structures.lib.exceptions.InvalidSequenceException;
import org.semanticscience.narf.structures.parts.Sequence;
import org.semanticscience.narf.structures.secondary.PredictedSecondaryStructure;

/**
 * An abstract class for nucleic acid secondary structure predictors to minimize
 * the effort required for their implementation by sharing common methods.
 * 
 * @author William Greenwood
 * @version %I%, %G%
 * @since 1.6
 */

public abstract class PredictedSecondaryStructureFactory extends
		PredictedStructureFactory {

	/**
	 * Construct a predicted secondary structure factory for predicted nucleic
	 * acid secondary structures.
	 * 
	 * @param aPredictorName
	 *            the name of the program that predicted the nucleic acid
	 *            secondary structure
	 * @param aPredictorVersion
	 *            the version of the program that predicted the nucleic acid
	 *            secondary structure
	 */
	protected PredictedSecondaryStructureFactory(String aPredictorName,
			String aPredictorVersion) {
		super(aPredictorName, aPredictorVersion);
	}

	/**
	 * Get all nucleic acid secondary structures using information generated by
	 * the secondary structure predictor.
	 * 
	 * @param aSequence
	 *            a sequence that will be folded into a nucleic acid secondary
	 *            structure
	 * @return a set of predicted nucleic acid secondary structures
	 * @throws InvalidDotBracketNotationException
	 *             if the dot bracket notation produced by the secondary
	 *             structure predictor is invalid
	 * @throws InvalidSequenceException
	 *             if the given sequence contains invalid residue labels for
	 *             nucleotide residues
	 * @throws IOException
	 *             if any IO error occur reading the output of the secondary
	 *             structure predictor or writing the output of the secondary
	 *             structure predictor
	 */
	public final Set<PredictedSecondaryStructure> getStructures(
			Sequence aSequence) throws InvalidDotBracketNotationException,
			InvalidSequenceException, IOException {
		return this.getStructures(aSequence, new String[0]);
	}

	/**
	 * Get all nucleic acid secondary structures using information generated by
	 * the secondary structure predictor.
	 * 
	 * @param aSequence
	 *            a sequence that will be folded into a nucleic acid secondary
	 *            structure
	 * @param args
	 *            an additional set of commands to modify the execution of the
	 *            predictor
	 * @return a set of predicted nucleic acid secondary structures
	 * @throws InvalidDotBracketNotationException
	 *             if the dot bracket notation produced by the secondary
	 *             structure predictor is invalid
	 * @throws InvalidSequenceException
	 *             if the given sequence contains invalid residue labels for
	 *             nucleotide residues
	 * @throws IOException
	 *             if any IO error occur reading the output of the secondary
	 *             structure predictor or writing the output of the secondary
	 *             structure predictor
	 */
	public abstract Set<PredictedSecondaryStructure> getStructures(
			Sequence aSequence, String args[])
			throws InvalidDotBracketNotationException,
			InvalidSequenceException, IOException;

}
