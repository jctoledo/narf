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

package org.semanticscience.narf.graphs.nucleicacid;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.semanticscience.narf.structures.factories.secondary.PredictedSecondaryStructureFactory;
import org.semanticscience.narf.structures.factories.secondary.Rnafold;
import org.semanticscience.narf.structures.lib.exceptions.InvalidDotBracketNotationException;
import org.semanticscience.narf.structures.lib.exceptions.InvalidSequenceException;
import org.semanticscience.narf.structures.parts.Sequence;
import org.semanticscience.narf.structures.secondary.PredictedSecondaryStructure;

/**
 * An extension of the nucleic acid graph class to incorporate information
 * unique to predicted nucleic acids.
 * 
 * @author William Greenwood
 * @author Jose Cruz-Toledo
 * 
 **/

public class PredictedNucleicAcid extends NucleicAcid implements
		GeneratedNucleicAcid {

	private static final long serialVersionUID = -7309715532853151905L;

	/**
	 * The name of the program that predicted the nucleic acid.
	 */
	private final String programName;
	/**
	 * The version of the program that predicted the nucleic acid.
	 */
	private final String programVersion;

	/**
	 * Construct a nucleic acid from a given sequence and the set of
	 * interactions predicted by a structure predictor.
	 * 
	 * @param aProgramName
	 *            the name of the program that predicted the nucleic acid
	 * @param aProgramVersion
	 *            the version of the program that predicted the nucleic acid
	 * @param modelNumber
	 *            the model number of the conformer
	 * @param aStructure
	 *            the secondary structure produced by the structure predictor
	 *            factory
	 * @throws InvalidPhosphodiesterBondException
	 * @since 1.6
	 */
	private PredictedNucleicAcid(String aProgramName, String aProgramVersion,
			int modelNumber, PredictedSecondaryStructure aStructure) {
		super(modelNumber, aStructure.getSequenceMap(), aStructure
				.getInteractions());

		programName = aProgramName;
		programVersion = aProgramVersion;
	}

	/**
	 * Execute a given structure predictorand construct each nucleic acid
	 * structure conformer using the secondary structure produced by the
	 * structure predictor factory.
	 * 
	 * @param aSequence
	 *            the nucleic acid sequence
	 * @param aPredictedSecondaryStructureFactory
	 *            the factory that produces the predicted nucleic acid structure
	 * @return a set of predicted nucleic acids conformers
	 * @throws InvalidDotBracketNotationException
	 *             if the dot bracket notation produced by the predictor is
	 *             invalid
	 * @throws InvalidSequenceException
	 *             if the given sequence has invalid nucleotide residue
	 *             identifiers
	 * @throws IOException
	 *             if a I/O problem arose while the reading the predicted
	 *             nucleic acid output file
	 */
	private static Set<NucleicAcid> predictStructures(
			Sequence aSequence,
			PredictedSecondaryStructureFactory aPredictedSecondaryStructureFactory)
			throws InvalidDotBracketNotationException,
			InvalidSequenceException, IOException {

		int modelNumber = 1;

		Set<NucleicAcid> nucleicAcids = new LinkedHashSet<NucleicAcid>();

		for (PredictedSecondaryStructure aStructure : aPredictedSecondaryStructureFactory
				.getStructures(aSequence)) {
			nucleicAcids.add(new PredictedNucleicAcid(
					aPredictedSecondaryStructureFactory.getProgramName(),
					aPredictedSecondaryStructureFactory.getProgramVersion(),
					modelNumber++, aStructure));

		}

		return nucleicAcids;
	}

	/**
	 * A factory method that executes the RNAfold structure predictor on a PDB
	 * structure file and creates a set of nucleic acid conformers from the
	 * information produced by RNAfold.
	 * 
	 * @param aSequence
	 *            the nucleic acid sequence
	 * @return a set of predicted nucleic acids conformers
	 * @throws InvalidDotBracketNotationException
	 *             if the dot bracket notation produced by the predictor is
	 *             invalid
	 * @throws InvalidSequenceException
	 *             if the given sequence has invalid nucleotide residue
	 *             identifiers
	 * @throws IOException
	 *             if a I/O problem arose while the reading the predicted
	 *             nucleic acid output file
	 * @since 1.6
	 */
	public static Set<NucleicAcid> rnafold(Sequence aSequence)
			throws InvalidDotBracketNotationException,
			InvalidSequenceException, IOException {
		PredictedSecondaryStructureFactory rnafold = new Rnafold();

		return predictStructures(aSequence, rnafold);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getProgramName() {
		return programName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getProgramVersion() {
		return programVersion;
	}
}
