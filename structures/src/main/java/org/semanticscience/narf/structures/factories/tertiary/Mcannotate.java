/**
 * Copyright (c) 2011 Jose Cruz-Toledo and William Greenwood
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
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.semanticscience.narf.structures.interactions.BasePair;
import org.semanticscience.narf.structures.interactions.BaseStack;
import org.semanticscience.narf.structures.interactions.NucleotideInteraction;
import org.semanticscience.narf.structures.interactions.PhosphodiesterBond;
import org.semanticscience.narf.structures.lib.InferNucleotideParameters;
import org.semanticscience.narf.structures.lib.PdbHelper;
import org.semanticscience.narf.structures.lib.StreamGobbler;
import org.semanticscience.narf.structures.lib.exceptions.InvalidEdgeException;
import org.semanticscience.narf.structures.lib.exceptions.InvalidResidueException;
import org.semanticscience.narf.structures.lib.exceptions.NonConsecutiveNucleotideException;
import org.semanticscience.narf.structures.parts.Edge;
import org.semanticscience.narf.structures.parts.Nucleotide;
import org.semanticscience.narf.structures.parts.Sequence;
import org.semanticscience.narf.structures.parts.SubEdge;
import org.semanticscience.narf.structures.tertiary.ExtractedTertiaryStructure;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * The factory to create tertiary structures using inforamtion produced by the
 * MC-Annotate tertiary structure annotator.
 * 
 * @author Jose Cruz-Toledo
 * @author William Greenwood
 * @version %I%, %G%
 * @since 1.6
 */
public class Mcannotate extends ExtractedTertiaryStructureFactory {

	private static final String path = "/home/jose/Programs/MCAnnotate/";
	/**
	 * Construct the factory for the MC-Annotate tertiary structure annotator.
	 */
	public Mcannotate() {
		super("MC-Annotate", "1.0");
	}

	@Override
	protected File execute(File aPdbFile, String[] commands) throws IOException {
		File outputFile = new File(aPdbFile.getParentFile(), aPdbFile.getName()
				+ ".mca");

		try {
			String[] cmdArr = new String[commands.length + 2];
			System.arraycopy(
					new String[] { path+"MC-Annotate", aPdbFile.getAbsolutePath() },
					0, cmdArr, 0, 2);
			System.arraycopy(commands, 0, cmdArr, 2, commands.length);

			if (!outputFile.exists()) {
				outputFile.getParentFile().mkdirs();
				outputFile.createNewFile();
			}

			FileOutputStream fos = new FileOutputStream(outputFile);
			ProcessBuilder builder = new ProcessBuilder(cmdArr);
			Process proc = builder.start();
			// any output?
			StreamGobbler outputGobbler = new StreamGobbler(
					proc.getInputStream(), "OUTPUT", fos);
			// kick them off
			outputGobbler.start();
			outputGobbler.join();
			// any error???
			proc.waitFor();
			fos.flush();
			fos.close();
		} catch (InterruptedException ie) {
			System.out.println("MC-Annotate was interrupted.");
			System.exit(-1);
		}

		return outputFile;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Set<NucleotideInteraction> parseInteractions(
			Map<String, Sequence> aSequenceMap, File anOutputFile)
			throws IOException {
		// check that the sequence map is not empty
		if (aSequenceMap.isEmpty()) {
			throw new IOException("Empty sequence map! Nothing to do!");
		}
		Set<NucleotideInteraction> interactions = new LinkedHashSet<NucleotideInteraction>();
		// add phosphodiester bonds
		for (String chain : aSequenceMap.keySet()) {
			Nucleotide previousNucleotide = null;
			for (Nucleotide nucleotide : aSequenceMap.get(chain)) {
				if (previousNucleotide != null) {
					try {
						interactions.add(new PhosphodiesterBond(
								previousNucleotide, nucleotide));
					} catch (NonConsecutiveNucleotideException e) {
						e.printStackTrace();
					}
				}
				previousNucleotide = nucleotide;
			}
		}// for
		// add other interactions (adj stackings, non-adj stackings and
		// basepairs
		BufferedReader input = new BufferedReader(new FileReader(anOutputFile));
		String strLine;
		while ((strLine = input.readLine()) != null) {
			if (strLine.startsWith("Adjacent stackings")) {
				interactions.addAll(this.parseAdjacentStackings(aSequenceMap,
						input));
			} else if (strLine.startsWith("Non-Adjacent stackings")) {
				interactions.addAll(this.parseNonAdjacentStackings(
						aSequenceMap, input));
			} else if (strLine.startsWith("Base-pairs")) {
				interactions.addAll(this.parseBasePairs(aSequenceMap, input));
			}
		}

		return interactions;
	}

	/**
	 * Parse all the base pairs from the raw output of MC-Annotate.
	 * 
	 * @param aSequenceMap
	 *            a mapping of the chain identifiers of a nucleic acid to the
	 *            sequence of the chain
	 * @param input
	 *            the raw buffered output of MC-Annotate
	 * @return a set of base pairs
	 * @throws IOException
	 *             if any IO error occurs while reading and parsing the raw
	 *             output of MC-Annotate
	 */
	private Set<NucleotideInteraction> parseBasePairs(
			Map<String, Sequence> aSequenceMap, BufferedReader input)
			throws IOException {
		// A6-A35 : G-C Ww/Ww pairing antiparallel cis XIX
		Set<NucleotideInteraction> returnMe = new LinkedHashSet<NucleotideInteraction>();

		Pattern cLinePattern = Pattern
				.compile("(\\S+)\\s:\\s(\\S+)((?:\\s(?:[A-Za-z0-9'-_]+\\/[A-Za-z0-9'-_]+))+)(?:\\sinward|)\\spairing(?:\\s(parallel|antiparallel))?(?:\\s(cis|trans))?(?:\\s(.*))?");
		String strLine = "";

		while ((strLine = input.readLine()) != null) {
			Matcher aMatch = cLinePattern.matcher(strLine.trim());
			if (aMatch.matches()) {
				String a = aMatch.group(1);// X13-X83
				// String b = aMatch.group(2);// C-G
				String c = aMatch.group(3).trim();// Ww/Ww or Ww/Ss Bh/O2'
				String d = aMatch.group(4);// parallel or antiparallel
				String e = aMatch.group(5);// cis |trans
				// String f = aMatch.group(6);// XIX or one_hbond or 81

				String[] aTmp = a.split("-");
				// String[] bTmp = b.split("-");

				String[] edgeArray = c.split("\\s");
				Set<SubEdge> edge1Subedges = new LinkedHashSet<SubEdge>();
				Set<SubEdge> edge2Subedges = new LinkedHashSet<SubEdge>();

				for (int i = 0; i < edgeArray.length; i++) {
					String[] edges = edgeArray[i].split("\\/");
					edge1Subedges.add(new SubEdge(edges[0]));
					edge2Subedges.add(new SubEdge(edges[1]));
				}

				String glycosidicOrientation = null; // cis | trans
				String strandOrientation = null; // parallel | antiparallel
				/*
				 * String res1Label = null; // C String res2Label = null; // G
				 */
				String chainId1 = null;// X
				String chainId2 = null;// X
				int residueNum1 = -1;// 13
				int residueNum2 = -1;// 83

				if (aTmp.length == 2) {
					Pattern chainPosPat = Pattern
							.compile("(?:\\'(\\d)\\'|(\\w))(\\d+)");
					Matcher chainPosMat1 = chainPosPat.matcher(aTmp[0]);
					Matcher chainPosMat2 = chainPosPat.matcher(aTmp[1]);
					if (chainPosMat1.matches() && chainPosMat2.matches()) {

						chainId1 = chainPosMat1.group(1);

						if (chainId1 == null) {
							chainId1 = chainPosMat1.group(2);
						}

						residueNum1 = Integer.parseInt(chainPosMat1.group(3));

						chainId2 = chainPosMat2.group(1);
						if (chainId2 == null) {
							chainId2 = chainPosMat2.group(2);
						}
						residueNum2 = Integer.parseInt(chainPosMat2.group(3));
					}
				}

				if ((e != null) && (e.equals("cis") || e.equals("trans"))) {
					glycosidicOrientation = e.trim();
				}

				if ((d != null)
						&& (d.equals("parallel") || d.equals("antiparallel"))) {
					strandOrientation = d.trim();
				}

				if (!aSequenceMap.containsKey(chainId1)
						|| !aSequenceMap.containsKey(chainId2)) {
					continue;
				}

				if (!aSequenceMap.get(chainId1).containsNucleotideAtPosition(
						residueNum1)
						|| !aSequenceMap.get(chainId2)
								.containsNucleotideAtPosition(residueNum2)) {
					continue;
				}

				Nucleotide nucleotide1 = aSequenceMap.get(chainId1)
						.getNucleotideAtPosition(residueNum1);
				Nucleotide nucleotide2 = aSequenceMap.get(chainId2)
						.getNucleotideAtPosition(residueNum2);

				try {
					Edge edge1 = new Edge(
							InferNucleotideParameters.inferEdge(edge1Subedges),
							edge1Subedges);
					Edge edge2 = new Edge(
							InferNucleotideParameters.inferEdge(edge2Subedges),
							edge2Subedges);
					BasePair bp = new BasePair(nucleotide1, nucleotide2, edge1,
							edge2, glycosidicOrientation, strandOrientation);
					returnMe.add(bp);
				} catch (InvalidEdgeException e1) {
					e1.printStackTrace();
				}

			} else {
				break;
			}
		}
		return returnMe;
	}

	/**
	 * Parse all the non-adjacent base stack from the raw output of MC-Annotate.
	 * 
	 * @param aSequenceMap
	 *            a mapping of the chain identifiers of a nucleic acid to the
	 *            sequence of the chain
	 * @param anInput
	 *            the raw buffered output of MC-Annotate
	 * @return a set of base stacks
	 * @throws IOException
	 *             if any IO error occurs while reading and parsing the raw
	 *             output of MC-Annotate
	 */
	private Set<NucleotideInteraction> parseNonAdjacentStackings(
			Map<String, Sequence> aSequenceMap, BufferedReader input)
			throws IOException {
		// A11-A35 : upward pairing

		Set<NucleotideInteraction> returnMe = new LinkedHashSet<NucleotideInteraction>();
		Pattern nasPattern = Pattern.compile("(\\S+)\\s\\:\\s(\\S+).*");

		String strLine = "";

		while ((strLine = input.readLine()) != null) {
			Matcher m = nasPattern.matcher(strLine.trim());
			if (m.matches()) {

				String participants = m.group(1).trim();
				String stackingDirection = m.group(2).trim();
				String[] chainTmp = participants.split("-");
				Pattern p = Pattern.compile("(?:\\'(\\d)\\'|(\\w))(\\d+)");// A11-A35
				Matcher matches = p.matcher(chainTmp[0]);
				Matcher matches2 = p.matcher(chainTmp[1]);
				String chainId1 = "";
				int residueNum1 = -1;
				String chainId2 = "";
				int residueNum2 = -1;
				if (matches.matches()) {
					chainId1 = matches.group(1);
					if (chainId1 == null) {
						chainId1 = matches.group(2);
					}
					residueNum1 = Integer.parseInt(matches.group(3));
				}// if
				if (matches2.matches()) {
					chainId2 = matches2.group(1);
					if (chainId2 == null) {
						chainId2 = matches2.group(2);
					}
					residueNum2 = Integer.parseInt(matches2.group(3));
				}// if

				if (!aSequenceMap.containsKey(chainId1)
						|| !aSequenceMap.containsKey(chainId2)) {
					continue;
				}

				if (!aSequenceMap.get(chainId1).containsNucleotideAtPosition(
						residueNum1)
						|| !aSequenceMap.get(chainId2)
								.containsNucleotideAtPosition(residueNum2)) {
					continue;
				}

				Nucleotide nucleotide1 = aSequenceMap.get(chainId1)
						.getNucleotideAtPosition(residueNum1);
				Nucleotide nucleotide2 = aSequenceMap.get(chainId2)
						.getNucleotideAtPosition(residueNum2);

				if (nucleotide1 == null || nucleotide2 == null) {
					continue;
				}

				BaseStack s = new BaseStack(nucleotide1, nucleotide2, false,
						stackingDirection);
				returnMe.add(s);

			}// if
			else {
				break;
			}
		}// for
		return returnMe;
	}

	/**
	 * Parse all the adjacent base stacks from the raw output of MC-Annotate.
	 * 
	 * @param aSequenceMap
	 *            a mapping of the chain identifiers of a nucleic acid to the
	 *            sequence of the chain
	 * @param anInput
	 *            the raw buffered output of MC-Annotate
	 * @return a set of base stacks
	 * @throws IOException
	 *             if any IO error occurs while reading and parsing the raw
	 *             output of MC-Annotate
	 */
	private Set<NucleotideInteraction> parseAdjacentStackings(
			Map<String, Sequence> aSequenceMap, BufferedReader anInput)
			throws IOException {
		/* This method parses the adjacent stackings section of the MCA output */
		// A6-A7 : adjacent_5p upward

		Set<NucleotideInteraction> returnMe = new LinkedHashSet<NucleotideInteraction>();

		Pattern aSPattern = Pattern
				.compile("(\\S+)\\s\\:\\s(\\S+)\\s(\\S+)(.*)");

		String strLine = "";

		while ((strLine = anInput.readLine()) != null) {
			Matcher m = aSPattern.matcher(strLine.trim());

			if (m.matches()) {
				/*
				 * m.group(3) upward |downward | inward | outward
				 */
				if (m.group(3).length() != 0) {
					/**
					 * Groups 1 -> Chain1Res1-Chain2Res2 2 -> 3 -> stack
					 * direction (upward)
					 */
					String participants = m.group(1).trim();// A6-A7
					String[] chainTmp = participants.split("-");
					Pattern p = Pattern.compile("(?:\\'(\\d)\\'|(\\w))(\\d+)");
					Matcher matches = p.matcher(chainTmp[0]);
					Matcher matches2 = p.matcher(chainTmp[1]);
					String chainId1 = "";
					int residueNum1 = -1;
					String chainId2 = "";
					int residueNum2 = -1;
					String stackingDirection = m.group(3);
					if (matches.matches()) {
						chainId1 = matches.group(1);

						if (chainId1 == null) {
							chainId1 = matches.group(2);
						}

						residueNum1 = Integer.parseInt(matches.group(3));

					}// if

					if (matches2.matches()) {

						chainId2 = matches2.group(1);

						if (chainId2 == null) {
							chainId2 = matches2.group(2);
						}

						residueNum2 = Integer.parseInt(matches2.group(3));

					}// if

					if (!aSequenceMap.containsKey(chainId1)
							|| !aSequenceMap.containsKey(chainId2)) {
						continue;
					}

					if (!aSequenceMap.get(chainId1)
							.containsNucleotideAtPosition(residueNum1)
							|| !aSequenceMap.get(chainId2)
									.containsNucleotideAtPosition(residueNum2)) {
						continue;
					}

					Nucleotide nucleotide1 = aSequenceMap.get(chainId1)
							.getNucleotideAtPosition(residueNum1);
					Nucleotide nucleotide2 = aSequenceMap.get(chainId2)
							.getNucleotideAtPosition(residueNum2);

					BaseStack s = new BaseStack(nucleotide1, nucleotide2, true,
							stackingDirection);

					returnMe.add(s);

				}// if
			}// if
			else {
				break;
			}
		}// for
		return returnMe;
	}

	@Override
	protected Map<String, Sequence> parseSequences(File aPdbFile,
			File anOutputFile) throws IOException, InvalidResidueException {

		Map<String, Map<Integer, Nucleotide>> nucleotideMapping = PdbHelper
				.getNucleotideMapping(aPdbFile);
		if (nucleotideMapping.size() == 0) {
			throw new IOException("invalid nucleotide mapping!");
		}
		BufferedReader input = new BufferedReader(new FileReader(anOutputFile));
		Pattern rcPattern = Pattern
				.compile("(\\S+)\\s\\:\\s(\\S+)\\s(\\S+)\\s(\\S+)");
		String strLine;
		while ((strLine = input.readLine()) != null) {
			Matcher m = rcPattern.matcher(strLine.trim());
			/**
			 * Groups: 1 -> chain+residue position X23 2 -> residue label C 3 ->
			 * puckeratom+quality C3p_endo 4 -> residue conformation anti | syn
			 */
			if (m.matches()) {
				if ((m.group(4)).equals("anti") || (m.group(4)).equals("syn")) {
					// split the chain from the residue
					Pattern chainResPatt = Pattern
							.compile("(?:\\'(\\d)\\'|(\\w))(\\d+)");
					Matcher m2 = chainResPatt.matcher(m.group(1).trim());
					// split puckering line
					Pattern puckerPatt = Pattern.compile("(\\S+)\\_(\\S+)");
					Matcher m3 = puckerPatt.matcher(m.group(3).trim());
					// A8 : G C2p_exo anti
					if (m3.matches() && m2.matches()) {
						String puckerAtom = m3.group(1); // C2p
						String puckerQual = m3.group(2); // exo
						String chainId = m2.group(1); // A
						if (chainId == null) {
							chainId = m2.group(2);
						}
						int residuePosition = Integer.parseInt(m2.group(3));// 8
						String residueName = m.group(2); // G
						String residueConformation = m.group(4);// anti
						// create a nucleotide obj
						Nucleotide nucletoide = new Nucleotide(residuePosition,
								residueName, residueConformation, puckerAtom,
								puckerQual);
						// replace the nucleotide object with the one generated
						// on line 510
						if (nucleotideMapping.containsKey(chainId)
								&& nucleotideMapping.get(chainId).containsKey(
										residuePosition)) {
							nucleotideMapping.get(chainId).put(residuePosition,
									nucletoide);
						}
					}
				}
			}
		}
		Map<String, Sequence> returnMe = new HashMap<String, Sequence>();
		for (String chain : nucleotideMapping.keySet()) {
			Set<Nucleotide> aSequenceMap = new LinkedHashSet<Nucleotide>();
			for (int residuePosition : nucleotideMapping.get(chain).keySet()) {
				aSequenceMap.add(nucleotideMapping.get(chain).get(
						residuePosition));
			}
			try {
				returnMe.put(chain, new Sequence(aSequenceMap));
			} catch (NonConsecutiveNucleotideException e) {
				returnMe.remove(chain);
				e.printStackTrace();
			}
		}
		return returnMe;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticscience.narf.structures.factories.ExtractedStructureFactory
	 * #execute(com.hp.hpl.jena.rdf.model.Model, java.lang.String[])
	 */
	@Override
	protected File execute(Model aModel, String[] commands) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.semanticscience.narf.structures.factories.tertiary.
	 * ExtractedTertiaryStructureFactory#getStructures(java.io.File,
	 * java.io.File)
	 */
	@Override
	protected Map<String, Set<ExtractedTertiaryStructure>> getStructures(
			File anInputDir, File anOutputDir) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
}
