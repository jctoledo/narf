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
package org.semanticscience.narf.structures.factories;

import java.io.File;
import java.io.IOException;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * An abstract class for nucleic acid structure annotators to minimize the
 * effort required for their implementation by sharing common methods.
 * 
 * @author William Greenwood
 * @since 1.6
 */

public abstract class ExtractedStructureFactory extends
		GeneratedStructureFactory {
	/**
	 * Construct a annotated structure factory for annotated nucleic acid
	 * structures.
	 * 
	 * @param aPredictorName
	 *            the name of the program that annotated the nucleic acid
	 *            structure
	 * @param aPredictorVersion
	 *            the version of the program that annotated the nucleic acid
	 *            structure
	 */
	protected ExtractedStructureFactory(String aPredictorName,
			String aPredictorVersion) {
		super(aPredictorName, aPredictorVersion);
	}

	/**
	 * Executes (runs) the PDB structure annotator for a PDB file
	 * 
	 * @param aFile
	 *            a PDB structure file
	 * @param commands
	 *            an additional set of commands to modify the execution of the
	 *            predictor
	 * @return the raw output file of the tertiary structure annotator
	 * @throws IOException
	 *             if any IO error occurs reading or writing the output of the
	 *             tertiary structure annotator
	 */
	protected abstract File execute(File aFile, String[] commands)
			throws IOException;

	/**
	 * Annotate the interactions present in an RDF represntation of a PDB
	 * structure file
	 * 
	 * @param aModel
	 *            a Jena Model containing the RDF representation of a PDB
	 *            structure file
	 * @param commands
	 *            an additional set of commands to modify the execution of the
	 *            predictor
	 * 
	 * @return the raw output file of the tertiary structure annotator
	 * @throws IOException
	 *             if any IO error occurs reading or writing the output of the
	 *             tertiary structure annotator
	 */
	protected abstract File execute(Model aModel, String[] commands)
			throws IOException;

}
