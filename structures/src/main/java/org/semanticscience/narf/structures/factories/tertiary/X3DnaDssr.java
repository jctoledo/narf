/**
 * Copyright (c) 2013  Jose Cruz-Toledo
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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.semanticscience.narf.structures.interactions.BasePair;
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
 * @author Jose Cruz-Toledo TODO: change the way I specify the version of DSSR
 *         (see default constructor) TODO: figure out how to deal with NMR
 *         structures (currently not working)
 */
public class X3DnaDssr extends ExtractedTertiaryStructureFactory {
	private final String dssrPath = "/home/jose/Programs/X3DNA/bin";

	/**
	 * Use the default version of X3DNA-DSSR to annotate a PDB file
	 */
	public X3DnaDssr() {
		super("DSSR", "beta-r21-on-20130903");
	}

	/**
	 * 
	 * @param aPredictorName
	 * @param aPredictorVersion
	 */
	public X3DnaDssr(String aPredictorName, String aPredictorVersion) {
		super(aPredictorName, aPredictorVersion);
	}

	/**
	 * Creates a map where the key is a chain Id and the value is its
	 * corresponding Sequence. Currently set to be very stringent. Will not
	 * accept sequences that have non-adjacent nucleotide residues {@inheritDoc}
	 */
	@Override
	protected Map<String, Sequence> parseSequences(File aPdbFile,
			File anOutputFile) throws IOException, InvalidResidueException {
		Map<String, Sequence> rm = new HashMap<String, Sequence>();
		// key => chain, value(k => position, v=>nucleotide)
		Map<String, Map<Integer, Nucleotide>> nucleotideMapping = PdbHelper
				.getNucleotideMapping(aPdbFile);
		
		if (nucleotideMapping.size() == 0) {
			throw new IOException(
					"invalid nucleotide mapping! Sorry, but you failed!");
		}
		for (String chainId : nucleotideMapping.keySet()) {
			// create a set for the sequence in this chain
			Set<Nucleotide> aSeqMap = new LinkedHashSet<Nucleotide>();
			for (int residuePos : nucleotideMapping.get(chainId).keySet()) {
				aSeqMap.add(nucleotideMapping.get(chainId).get(residuePos));
			}
			// now create a sequence object
			try {
				String def = "Sequence generated from parsing PDBID: "
						+ aPdbFile.getName() + " to be annotated by: "
						+ this.getProgramName() + " version:"
						+ this.getProgramVersion();
				rm.put(chainId, new Sequence(aSeqMap, def));
			} catch (NonConsecutiveNucleotideException e) {
				System.out
						.println("Removing chain from map!!\n Non adjacent residues found !\nPlease check input!");
				e.printStackTrace();
				// remove the chain that was just added
				rm.remove(chainId);
			}
		}
		return rm;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticscience.narf.structures.factories.ExtractedStructureFactory
	 * #execute(java.io.File, java.lang.String[])
	 */
	@Override
	protected File execute(File aFile, String[] commands) {
		File oF = null;
		try {
			if (commands.length > 0) {
				Process p = Runtime.getRuntime().exec(commands);
				p.waitFor();

			} else {
				throw new IOException("invalid command array! Nothing to do!");
			}
			// now grab the output file
			// directly from the command array
			for (String str : commands) {
				if (str.contains("-o=")) {
					String[] c = str.split("-o=");
					if (c.length == 2) {
						oF = new File(c[1]);
					}
				}
			}
		} catch (InterruptedException ie) {
			System.out.println("3DNA was interrupted");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return oF;
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
		// TODO: fill me out
		return null;
	}

	/**
	 * Run X3dna on just one file
	 */
	@Override
	public Set<ExtractedTertiaryStructure> getStructures(File aPdbFile) {
		Set<ExtractedTertiaryStructure> rm = new HashSet<ExtractedTertiaryStructure>();
		// create an output file
		File outputFile = new File(FileUtils.getTempDirectoryPath() + "/"
				+ aPdbFile.getName() + ".out");
		// construct the command array
		String[] cmdArr = new String[] { dssrPath + "/x3dna-dssr",
				"-i=" + aPdbFile.getAbsolutePath(),
				"-o=" + outputFile.getAbsolutePath() };
		try {
			Set<ExtractedTertiaryStructure> ets = this.getStructures(aPdbFile,
					cmdArr);
			return ets;
		} catch (InvalidResidueException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Get all nucleic acid extracted structures found in the input directory.
	 * Store the annotator's output files in the output directory
	 * 
	 * @param anInputDir
	 *            the input directory containing pdb files
	 * @param anOutputDir
	 *            the directory where the output of the annotator will be stored
	 * @return a map of a set of extracted tertiary structures, where the key is
	 *         a PDBId and the value is the set of extracted tertiary structures
	 * @throws IOException
	 *             if either the input or output directory are not valid
	 */
	public Map<String, Set<ExtractedTertiaryStructure>> getStructures(
			File anInputDir, File anOutputDir) throws IOException {
		Map<String, Set<ExtractedTertiaryStructure>> rm = new HashMap<String, Set<ExtractedTertiaryStructure>>();
		// this method makes use of getStructures(File, String[]).
		// iterate over the input directory and call the sibling method
		Iterator<File> itr = FileUtils.iterateFiles(anInputDir, new String[] {
				"pdb", "PDB" }, false);
		while (itr.hasNext()) {
			File aPdbFile = itr.next();
			// construct the output file
			File outputFile = new File(anOutputDir.getAbsolutePath() + "/"
					+ aPdbFile.getName() + ".out");
			// construct the command array
			String[] cmdArr = new String[] { dssrPath + "/x3dna-dssr",
					"-i=" + aPdbFile.getAbsolutePath(),
					"-o=" + outputFile.getAbsolutePath() };
			try {
				Set<ExtractedTertiaryStructure> ets = this.getStructures(
						aPdbFile, cmdArr);
				rm.put(aPdbFile.getName(), ets);
			} catch (InvalidResidueException e) {
				e.printStackTrace();
			}
		}
		return rm;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.semanticscience.narf.structures.factories.tertiary.
	 * ExtractedTertiaryStructureFactory#parseInteractions(java.util.Map,
	 * java.io.File)
	 */
	@Override
	protected Set<NucleotideInteraction> parseInteractions(
			Map<String, Sequence> aSequenceMap, File annotatorOutputFile)
			throws IOException {
		// check that the sequence map is not empty
		if (aSequenceMap.isEmpty()) {
			throw new IOException("Empty sequence map! Nothing to do!");
		}
		Set<NucleotideInteraction> rm = new LinkedHashSet<NucleotideInteraction>();
		// add phosphodiester bonds
		for (String aChainId : aSequenceMap.keySet()) {
			Nucleotide prev_nuc = null;
			for (Nucleotide aNuc : aSequenceMap.get(aChainId)) {
				if (prev_nuc != null) {
					try {
						rm.add(new PhosphodiesterBond(prev_nuc, aNuc));
					} catch (NonConsecutiveNucleotideException e) {
						e.printStackTrace();
					}
				}
				prev_nuc = aNuc;
			}
		}
		// now add all other interactions (base pairs, multiplets, hairpin
		// loops, internal loops, stems, helices)
		BufferedReader input = new BufferedReader(new FileReader(
				annotatorOutputFile));

		String aLine = null;
		Pattern bpPat = Pattern.compile("List of\\s(\\d+)\\sbase pair\\(s\\)");
		Pattern multipletsPat = Pattern
				.compile("List of\\s\\d+\\smultiplet\\(s\\)");
		Pattern stemPat = Pattern.compile("List of\\s\\d+\\sstems");
		Pattern helixPat = Pattern.compile("List of\\s\\d+\\shelices");
		Pattern hairpinPat = Pattern
				.compile("List of\\s\\d+\\shairpin loop\\(s\\)");
		Pattern internalLoops = Pattern
				.compile("List of\\s\\d+\\sinternal loop(s)");
		while ((aLine = input.readLine()) != null) {
			Matcher bpMat = bpPat.matcher(aLine);
			Matcher mMat = multipletsPat.matcher(aLine);
			Matcher sMat = stemPat.matcher(aLine);
			Matcher hMat = helixPat.matcher(aLine);
			Matcher hairMat = hairpinPat.matcher(aLine);
			Matcher iMat = internalLoops.matcher(aLine);
			if (bpMat.matches()) {
				// get the only basepair section of this file
				BufferedReader basePairChop = this.retrieveBasePairSection(
						input, Integer.parseInt(bpMat.group(1)));
				Set<NucleotideInteraction> bps = this.parseBasePairs(
						aSequenceMap, basePairChop);
				rm.addAll(bps);
			}
			if (mMat.matches()) {
				// get only the multiplet section of this file

			}
			if (sMat.matches()) {

			}
			if (hMat.matches()) {

			}
			if (hairMat.matches()) {

			}
			if (iMat.matches()) {

			}
		}
		return rm;
	}

	/**
	 * Takes in the raw output of x3dna-dssr and returns a buffered reader with
	 * only the Base pair section
	 * 
	 * @param input
	 *            a buffered reader holding all of the output from x3dna-dssr
	 * @return a bufferedreader of only the basepair section from x3dna-dssr
	 */
	private BufferedReader retrieveBasePairSection(BufferedReader input,
			int numOfBps) {
		// read the file line by line
		String aLine = null;
		boolean recordFlag = true;
		String buf = "";
		try {
			// convert the output of x3dna-dssr to a List of strings
			List<String> ol = this.bufferedReaderToListString(input);
			// last line to parse for base pairs
			int limit = 1 * numOfBps;
			for (int i = 0; i < ol.size(); i++) {
				if (i < limit) {
					buf += ol.get(i) + "\n";
				} else {
					// stop reading
					break;
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedReader rm = new BufferedReader(new StringReader(buf));
		return rm;
	}

	/**
	 * Parse the base pair section of the output of x3dna-dssr
	 * 
	 * @param aSequenceMap
	 *            A Map where the key is the chainid and the value is its
	 *            corresponding sequnece
	 * @param input
	 *            the basepair section of the output of x3dna-dssr
	 * @return a set of nucleotideinteractions (basepairs and phosphodiester
	 *         bonds)
	 */
	private Set<NucleotideInteraction> parseBasePairs(
			Map<String, Sequence> aSequenceMap, BufferedReader input) {
		Set<NucleotideInteraction> rm = new LinkedHashSet<NucleotideInteraction>();
		Pattern p2 = Pattern.compile("^\\s*(\\d+)\\s+(\\w+)\\.(\\D+)(\\d+)\\s+(\\w+)\\.(\\D+)(\\d+)\\s+(\\w+\\W\\w+\\s*\\w+\\s+|\\w+\\W\\w+\\s+)(\\S+)\\s+(\\S+)\\s+(\\S+)$");
		String al = "";
		try {
			while ((al = input.readLine()) != null) {
				Matcher m = p2.matcher(al.trim());
				if (m.matches()) {
					//   1 A.DC1            A.DG16           C-G WC           19-XIX    cWW cW-W
					String chainId1 = m.group(2).trim();
					String resLabel1 = m.group(3).trim();
					int resPos1 = Integer.parseInt(m.group(4).trim());
					String chainId2 = m.group(5).trim();
					String resLabel2 = m.group(6).trim();
					int resPos2 = Integer.parseInt(m.group(7).trim());
					String basePairLabel = m.group(8).trim();
					String saengerClass = m.group(9).trim();
					String bpLWClass = m.group(10).trim();


					// identify the glycosidic bond orientation and interacting
					// edges
					String glycoOrient = null;
					String edge1Label = null;
					String edge2Label = null;
					String basePairOrientation = null;
					if (bpLWClass.length() == 3) {
						glycoOrient = Character.toString(bpLWClass.charAt(0));
						if (glycoOrient.equalsIgnoreCase("c")) {
							glycoOrient = "cis";
						} else if (glycoOrient.equalsIgnoreCase("t")) {
							glycoOrient = "trans";
						}
						edge1Label = Character.toString(bpLWClass.charAt(1));
						edge2Label = Character.toString(bpLWClass.charAt(2));
					}

					if (!aSequenceMap.containsKey(chainId1)
							|| !aSequenceMap.containsKey(chainId2)) {
						continue;
					}
					if (!aSequenceMap.get(chainId1)
							.containsNucleotideAtPosition(resPos1)
							|| !aSequenceMap.get(chainId2)
									.containsNucleotideAtPosition(resPos2)) {
						continue;
					}

					Nucleotide n1 = aSequenceMap.get(chainId1)
							.getNucleotideAtPosition(resPos1);
					Nucleotide n2 = aSequenceMap.get(chainId2)
							.getNucleotideAtPosition(resPos2);
					try {
						if (edge1Label != null && edge2Label != null) {
							Edge e1 = new Edge(edge1Label,
									InferNucleotideParameters.inferSubEdges(n1,
											edge1Label));
							Edge e2 = new Edge(edge2Label,
									InferNucleotideParameters.inferSubEdges(n1,
											edge1Label));
							basePairOrientation = InferNucleotideParameters
									.findStrandBasePairOrientation(e1, e2,
											glycoOrient);

							BasePair bp = new BasePair(n1, n2, e1, e2,
									glycoOrient, basePairOrientation,
									basePairLabel, saengerClass, bpLWClass);
							rm.add(bp);
						}
					} catch (InvalidEdgeException e) {
						e.printStackTrace();
					} catch (InvalidGlycosidicOrientationException e) {
						e.printStackTrace();
					}
				}// if
			}// while
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rm;
	}

	/**
	 * Convert a buffered reader to a List of Strings
	 * 
	 * @param aBr
	 *            a buffered reader
	 * @return an ArrayList<Strings>
	 * @throws IOException
	 *             thrown by readLine()
	 */
	private List<String> bufferedReaderToListString(BufferedReader aBr)
			throws IOException {
		List<String> rm = new ArrayList<String>();
		String line;
		while ((line = aBr.readLine()) != null) {
			rm.add(line);
		}
		return rm;
	}
}
