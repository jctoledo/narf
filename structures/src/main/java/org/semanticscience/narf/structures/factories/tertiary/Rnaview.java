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
package org.semanticscience.narf.structures.factories.tertiary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.semanticscience.narf.structures.interactions.BasePair;
import org.semanticscience.narf.structures.interactions.BaseStack;
import org.semanticscience.narf.structures.interactions.NucleotideInteraction;
import org.semanticscience.narf.structures.interactions.PhosphodiesterBond;
import org.semanticscience.narf.structures.lib.InferNucleotideParameters;
import org.semanticscience.narf.structures.lib.PdbHelper;
import org.semanticscience.narf.structures.lib.exceptions.InvalidEdgeException;
import org.semanticscience.narf.structures.lib.exceptions.InvalidGlycosidicOrientationException;
import org.semanticscience.narf.structures.lib.exceptions.InvalidResidueException;
import org.semanticscience.narf.structures.lib.exceptions.NonConsecutiveNucleotideException;
import org.semanticscience.narf.structures.parts.Edge;
import org.semanticscience.narf.structures.parts.Nucleotide;
import org.semanticscience.narf.structures.parts.Sequence;
import org.semanticscience.narf.structures.tertiary.ExtractedTertiaryStructure;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * The factory to create tertiary structures using inforamtion produced by the
 * RNAView tertiary structure annotator.
 * 
 * @author William Greenwood
 * @author Jose Cruz-Toledo
 * @since 1.6
 */
public class Rnaview extends ExtractedTertiaryStructureFactory {
	/**
	 * Construct the factory for the RNAView tertiary structure annotator.
	 */
	public Rnaview() {
		super("RNAView", "1.0");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticscience.nucleicacid.factories.common.ExtractedStructureFactory
	 * #execute(com.hp.hpl.jena.rdf.model.Model, java.lang.String[])
	 */
	@Override
	protected File execute(Model aModel, String[] commands) throws IOException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	protected File execute(File aPdbFile, String[] commands) throws IOException {

		try {
			String[] cmdArr = new String[commands.length + 2];
			System.arraycopy(
					new String[] { "rnaview", aPdbFile.getAbsolutePath() }, 0,
					cmdArr, 0, 2);
			System.arraycopy(commands, 0, cmdArr, 2, commands.length);

			Process p = Runtime.getRuntime().exec(cmdArr);
			p.waitFor();

			new File(aPdbFile.getParentFile(), aPdbFile.getName() + ".out")
					.renameTo(new File(aPdbFile.getParentFile(), aPdbFile
							.getName() + ".rnaview"));
		} catch (InterruptedException ie) {
			System.out.println("RNAView was interrupted.");
		}
		return new File(aPdbFile.getParentFile(), aPdbFile.getName()
				+ ".rnaview");
	}

	/**
	 * Parse a modified nucleotide residue from a line of raw-output from
	 * RNAView
	 * 
	 * @param aLine
	 *            a line of the raw output of RNAView
	 * @return a mapping of the chain identifier of the modified nucleotide
	 *         residue to the modified nucleotide residue
	 */
	private Map<String, Map<Integer, String>> parseModifiedResidue(String line) {
		Map<String, Map<Integer, String>> modifiedResidueMap = new HashMap<String, Map<Integer, String>>();

		Pattern modifiedResiduePattern = Pattern
				.compile("^\\S+\\s+\\S+\\s+(\\S+)\\s+(\\S+)\\s+\\S+\\s+\\S+\\s+(\\S+)\\s+\\S+\\s+\\S+\\s+\\S+\\s+\\S+$");
		Matcher m = modifiedResiduePattern.matcher(line);

		if (m.matches()) {
			if (modifiedResidueMap.containsKey(m.group(3))) {
				modifiedResidueMap.put(m.group(3),
						new TreeMap<Integer, String>());
			}
			modifiedResidueMap.get(m.group(3)).put(
					Integer.parseInt(m.group(2)), m.group(1));
		}

		return modifiedResidueMap;
	}

	/**
	 * Determine whether two nucleotide residues are adjacent relative to their
	 * position in the nucleic acid.
	 * 
	 * @param nucleotide1ChainId
	 *            the chain identifier of the first nucleotide
	 * @param nucleotide1
	 *            the first nucleotide
	 * @param nucleotide2ChainId
	 *            the chain identifier of the second nucleotide
	 * @param nucleotide2
	 *            the second nucleotide
	 * @return <code>true</code> if the two nucleotides are adjacent, otherwise
	 *         <code>false</code>
	 */
	private boolean isAdjacent(String nucleotide1ChainId,
			Nucleotide nucleotide1, String nucleotide2ChainId,
			Nucleotide nucleotide2) {
		return nucleotide1ChainId.equals(nucleotide2ChainId)
				&& (Math.abs(nucleotide2.getResiduePosition()
						- nucleotide1.getResiduePosition()) == 1);
	}

	/**
	 * Parse an interaction from a line of raw-output from RNAView
	 * 
	 * @param aSequenceMap
	 *            a mapping of the chain identifiers of a nucleic acid to the
	 *            sequence of the chain
	 * @param aLine
	 *            a line of the raw output of RNAView
	 * @return an Interaction
	 */
	private NucleotideInteraction parseInteraction(
			Map<String, Sequence> aSequenceMap, String aLine) {

		String nucleotide1ChainId = aLine.substring(11, 12);
		int nucleotide1ResiduePosition = Integer.parseInt(aLine.substring(13,
				19).trim());

		String nucleotide2ChainId = aLine.substring(30, 31);
		int nucleotide2ResiduePosition = Integer.parseInt(aLine.substring(23,
				29).trim());

		if (aLine.contains("stacked")) {
			Nucleotide nucleotide1 = aSequenceMap.get(nucleotide1ChainId)
					.getNucleotideAtPosition(nucleotide1ResiduePosition);
			Nucleotide nucleotide2 = aSequenceMap.get(nucleotide2ChainId)
					.getNucleotideAtPosition(nucleotide2ResiduePosition);

			return new BaseStack(nucleotide1, nucleotide2, this.isAdjacent(
					nucleotide1ChainId, nucleotide1, nucleotide2ChainId,
					nucleotide2));
		} else {
			String edge1Label = aLine.substring(33, 34);
			String edge2Label = aLine.substring(35, 36);

			if (edge1Label.equals("+") || edge1Label.equals("-")
					|| edge1Label.equals("X")) {
				edge1Label = "W";
			}

			if (edge2Label.equals("+") || edge2Label.equals("-")
					|| edge2Label.equals("X")) {
				edge2Label = "W";
			}

			String basePairGlycosidicOrientation = aLine.substring(37, 41)
					.trim();

			if (basePairGlycosidicOrientation.equals("tran")) {
				basePairGlycosidicOrientation = "trans";
			}

			Nucleotide nucleotide1 = aSequenceMap.get(nucleotide1ChainId)
					.getNucleotideAtPosition(nucleotide1ResiduePosition);
			Nucleotide nucleotide2 = aSequenceMap.get(nucleotide2ChainId)
					.getNucleotideAtPosition(nucleotide2ResiduePosition);

			if (!aSequenceMap.containsKey(nucleotide1ChainId)
					|| !aSequenceMap.containsKey(nucleotide2ChainId)) {
				return null;
			}

			if (!aSequenceMap.get(nucleotide1ChainId)
					.containsNucleotideAtPosition(nucleotide1ResiduePosition)
					|| !aSequenceMap.get(nucleotide1ChainId)
							.containsNucleotideAtPosition(
									nucleotide2ResiduePosition)) {
				return null;
			}

			try {
				Edge edge1 = new Edge(edge1Label,
						InferNucleotideParameters.inferSubEdges(nucleotide1,
								edge1Label));
				Edge edge2 = new Edge(edge2Label,
						InferNucleotideParameters.inferSubEdges(nucleotide2,
								edge2Label));

				return new BasePair(nucleotide1, nucleotide2, edge1, edge2,
						basePairGlycosidicOrientation);
			} catch (InvalidEdgeException e) {
				e.printStackTrace();
			} catch (InvalidGlycosidicOrientationException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<NucleotideInteraction> parseInteractions(
			Map<String, Sequence> aSequenceMap, File aPdbFile)
			throws IOException {
		Set<NucleotideInteraction> interactions = new LinkedHashSet<NucleotideInteraction>();

		for (String chain : aSequenceMap.keySet()) {
			Nucleotide previousNucleotide = null;

			for (Nucleotide nucleotide : aSequenceMap.get(chain)) {
				if (previousNucleotide != null) {

					try {
						interactions.add(new PhosphodiesterBond(previousNucleotide,
								nucleotide));
					} catch (NonConsecutiveNucleotideException e) {
						e.printStackTrace();
					}

				}

				previousNucleotide = nucleotide;
			}
		}

		String line;
		boolean beginInteractionParsing = false;
		BufferedReader in = new BufferedReader(new FileReader(aPdbFile));

		while ((line = in.readLine()) != null) {

			if (line.startsWith("BEGIN_base-pair")) {
				beginInteractionParsing = true;
			} else if (line.startsWith("END_base-pair")) {
				break;
			} else if (beginInteractionParsing) {
				NucleotideInteraction interaction = this.parseInteraction(
						aSequenceMap, line);

				if (interaction != null) {
					interactions.add(interaction);
				}
			}
		}

		in.close();

		return interactions;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws InvalidResidueException
	 */
	protected Map<String, Sequence> parseSequences(File aPdbFile,
			File anOutputFile) throws IOException, InvalidResidueException {
		Map<String, Map<Integer, String>> modifiedResidueMap = new HashMap<String, Map<Integer, String>>();
		Map<String, Map<Integer, Nucleotide>> nucleotideMapping = PdbHelper
				.getNucleotideMapping(aPdbFile);

		String line;
		boolean beginInteractionParsing = false;
		BufferedReader in = new BufferedReader(new FileReader(anOutputFile));

		while ((line = in.readLine()) != null) {

			if (line.startsWith("uncommon residue")) {
				modifiedResidueMap.putAll(this.parseModifiedResidue(line));
			} else if (line.startsWith("BEGIN_base-pair")) {
				beginInteractionParsing = true;
			} else if (line.startsWith("END_base-pair")) {
				break;
			} else if (beginInteractionParsing) {
				String[] values = line.split("\\s+");

				String nucleotide1ChainId = line.substring(11, 12);
				int nucleotide1ResiduePosition = Integer.parseInt(line
						.substring(13, 19).trim());
				String nucleotide1ResidueId = line.substring(20, 21);
				String nucleotide1Conformation = "anti";

				String nucleotide2ChainId = line.substring(30, 31);
				int nucleotide2ResiduePosition = Integer.parseInt(line
						.substring(23, 29).trim());
				String nucleotide2ResidueId = line.substring(22, 23);
				String nucleotide2Conformation = "anti";

				/*
				 * if (modifiedResidueMap.containsKey(nucleotide1ChainId) &&
				 * modifiedResidueMap
				 * .get(nucleotide1ChainId).containsKey(nucleotide1ResiduePosition
				 * )){ nucleotide1ResidueId =
				 * modifiedResidueMap.get(nucleotide1ChainId
				 * ).get(nucleotide1ResidueId); }
				 * 
				 * if (modifiedResidueMap.containsKey(nucleotide2ChainId) &&
				 * modifiedResidueMap
				 * .get(nucleotide2ChainId).containsKey(nucleotide2ResiduePosition
				 * )){ nucleotide2ResidueId =
				 * modifiedResidueMap.get(nucleotide2ChainId
				 * ).get(nucleotide2ResidueId); }
				 */

				if (line.contains("stacked")) {
					if (line.substring(33, 36).equals("syn")) {
						nucleotide1Conformation = "syn";

						if (line.substring(37, 40).equals("syn")) {
							nucleotide2Conformation = "syn";
						}
					}

					if (line.substring(35, 38).equals("syn")) {
						nucleotide2Conformation = "syn";
					}

					Nucleotide nucleotide1 = new Nucleotide(
							nucleotide1ResiduePosition, nucleotide1ResidueId,
							nucleotide1Conformation);
					Nucleotide nucleotide2 = new Nucleotide(
							nucleotide2ResiduePosition, nucleotide2ResidueId,
							nucleotide2Conformation);

					if (nucleotideMapping.containsKey(nucleotide1ChainId)
							&& nucleotideMapping.get(nucleotide1ChainId)
									.containsKey(nucleotide1ResiduePosition)) {
						nucleotideMapping.get(nucleotide1ChainId).put(
								nucleotide1ResiduePosition, nucleotide1);
					}

					if (nucleotideMapping.containsKey(nucleotide2ChainId)
							&& nucleotideMapping.get(nucleotide2ChainId)
									.containsKey(nucleotide2ResiduePosition)) {
						nucleotideMapping.get(nucleotide2ChainId).put(
								nucleotide2ResiduePosition, nucleotide2);
					}
				} else {
					if (values[8].equals("syn")) {
						if (values[9].equals("syn")) {
							nucleotide1Conformation = "syn";
							nucleotide2Conformation = "syn";
						} else if (line.lastIndexOf("syn") == (line
								.lastIndexOf(values[9])) - 3) {
							nucleotide2Conformation = "syn";
						} else {
							nucleotide1Conformation = "syn";
						}
					}

					if (line.substring(44, 47).equals("syn")) {
						nucleotide1Conformation = "syn";

						if (line.substring(48, 51).equals("syn")) {
							nucleotide2Conformation = "syn";
						}
					}

					if (line.substring(46, 49).equals("syn")) {
						nucleotide2Conformation = "syn";
					}

					Nucleotide nucleotide1 = new Nucleotide(
							nucleotide1ResiduePosition, nucleotide1ResidueId,
							nucleotide1Conformation);
					Nucleotide nucleotide2 = new Nucleotide(
							nucleotide2ResiduePosition, nucleotide2ResidueId,
							nucleotide2Conformation);

					if (nucleotideMapping.containsKey(nucleotide1ChainId)
							&& nucleotideMapping.get(nucleotide1ChainId)
									.containsKey(nucleotide1ResiduePosition)) {
						nucleotideMapping.get(nucleotide1ChainId).put(
								nucleotide1ResiduePosition, nucleotide1);
					}

					if (nucleotideMapping.containsKey(nucleotide2ChainId)
							&& nucleotideMapping.get(nucleotide2ChainId)
									.containsKey(nucleotide2ResiduePosition)) {
						nucleotideMapping.get(nucleotide2ChainId).put(
								nucleotide2ResiduePosition, nucleotide2);
					}
				}
			}
		}

		in.close();

		Map<String, Sequence> sequences = new HashMap<String, Sequence>();

		for (String chain : nucleotideMapping.keySet()) {
			Set<Nucleotide> nucleotides = new LinkedHashSet<Nucleotide>();

			for (int residuePosition : nucleotideMapping.get(chain).keySet()) {
				nucleotides.add(nucleotideMapping.get(chain).get(
						residuePosition));
			}

			try {
				sequences.put(chain, new Sequence(nucleotides));
			} catch (NonConsecutiveNucleotideException e) {
				e.getStackTrace();
			}
		}

		return sequences;
	}

	/* (non-Javadoc)
	 * @see org.semanticscience.narf.structures.factories.tertiary.ExtractedTertiaryStructureFactory#getStructures(java.io.File, java.io.File)
	 */
	@Override
	protected Map<String, Set<ExtractedTertiaryStructure>> getStructures(
			File anInputDir, File anOutputDir) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
}
