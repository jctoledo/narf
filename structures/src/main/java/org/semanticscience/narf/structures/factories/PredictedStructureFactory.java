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
package org.semanticscience.narf.structures.factories;

import java.io.IOException;

import org.semanticscience.narf.structures.lib.exceptions.InvalidSequenceException;
import org.semanticscience.narf.structures.parts.Sequence;

/**
 * An abstract class for describing the output of nucleic acid structure
 * predictors 
 * @author Jose Cruz-Toledo
 * @author William Greenwood
 * @version %I%, %G%
 * @since 1.6
 */

public abstract class PredictedStructureFactory extends
		GeneratedStructureFactory {

	/**
	 * Construct a predicted structure factory for predicted nucleic acid
	 * structures.
	 * 
	 * @param aPredictorName
	 *            the name of the program that predicted the nucleic acid
	 *            structure
	 * @param aPredictorVersion
	 *            the version of the program that predicted the nucleic acid
	 *            structure
	 */
	protected PredictedStructureFactory(String aPredictorName,
			String aPredictorVersion) {
		super(aPredictorName, aPredictorVersion);
	}

	/**
	 * Predict a nucleic acid structure from a nucleic acid sequence.
	 * 
	 * @param aSequence
	 *            a sequence that will be folded into a nucleic acid structure
	 * @param commands
	 *            an additional set of commands to modify the execution of the
	 *            predictor
	 * @return the raw output from the predictor
	 * @throws InvalidSequenceException
	 *             if a given sequence is empty or has invalid characters
	 * @throws IOException
	 *             if any IO error occurs when extracting the output of the
	 *             predictor
	 */
	protected abstract String execute(Sequence aSequence, String[] commands)
			throws InvalidSequenceException, IOException;
}
