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
package org.semanticscience.narf.graphs.bin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.semanticscience.narf.graphs.lib.cycles.Cycle;
import org.semanticscience.narf.graphs.lib.cycles.FundamentalCycleBasis;
import org.semanticscience.narf.graphs.nucleicacid.ExtractedNucleicAcid;
import org.semanticscience.narf.graphs.nucleicacid.InteractionEdge;
import org.semanticscience.narf.graphs.nucleicacid.NucleicAcid;
import org.semanticscience.narf.structures.lib.exceptions.InvalidResidueException;
import org.semanticscience.narf.structures.parts.Nucleotide;

/**
 * Execute X3DNA-DSSR on a directory of structures
 * 
 * @author Jose Cruz-Toledo
 * 
 */
public class X3DNADSSRer {
	private File inputDir = null;
	private File outputDir = null;

	private X3DNADSSRer(File anInputDir, File anOutputDir)
			throws Exception {
		// check the inputs
		if (!anInputDir.isDirectory() || !anOutputDir.isDirectory()) {
			throw new Exception(
					"Invalid parameters! Pass in a valid inputDir and an outputDir");
		}
		this.setInputDir(anInputDir);
		this.setOutputDir(anOutputDir);
	}

	/**
	 * Executes X3DNA-DSSR on all files in anInputDir and stores the output in
	 * anOutputDir
	 * 
	 * @param anInputDir
	 * @param anOutputDir
	 */

	// TODO: fix to work on multiple models
	public static void makeFundamentalCycleBasis(File anInputDir,
			File anOutputDir) throws Exception {
		X3DNADSSRer x = new X3DNADSSRer(anInputDir, anOutputDir);
		List<String> inputFiles = getFilePathsFromDir(anInputDir, "pdb");
		for (String aFilePath : inputFiles) {
			Set<NucleicAcid> nucs = runX3DNADSSR(aFilePath);
			// only one model
			if (nucs.size() == 1) {
				for (NucleicAcid aNuc : nucs) {
					// check cyclebasis type

					FundamentalCycleBasis<Nucleotide, InteractionEdge> cb = new FundamentalCycleBasis<Nucleotide, InteractionEdge>(
							aNuc);
					List<Cycle<Nucleotide, InteractionEdge>> ccb = cb
							.getCycleBasis();
					// the pdbid
					String aPdbId = x.getPdbIdFromFilePath(aFilePath);
					// create an file for output here inside of
					// anOutputDir
					File outFile = new File(anOutputDir.getAbsolutePath() + "/"
							+ aPdbId + ".cycles.out");
					for (Cycle<Nucleotide, InteractionEdge> cycle : ccb) {
						int cLen = cycle.size();
						String sV = cycle.getStartVertex()
								.getResidueIdentifier()
								+ cycle.getStartVertex().getResiduePosition()
								+ "_" + cycle.getStartVertex().getChainId();

						String eV = cycle.getEndVertex().getResidueIdentifier()
								+ cycle.getEndVertex().getResiduePosition()
								+ "_" + cycle.getEndVertex().getChainId();
						// edgeclass
						String edgeSummary = "";
						String bpSummary = "";
						List<InteractionEdge> edges = cycle.getEdgeList();
						for (InteractionEdge anEdge : edges) {
							String bpC = anEdge.extractBasePairClasses();
							if (bpC.length() > 0) {
								bpSummary += bpC + ", ";
							}
							edgeSummary += anEdge.extractEdgeClasses() + "-";
						}
						bpSummary = bpSummary.substring(0,
								bpSummary.length() - 2);
						edgeSummary = edgeSummary.substring(0,
								edgeSummary.length() - 1);
						String vertexSummary = "";
						List<Nucleotide> vertices = cycle.getVertexList();
						for (Nucleotide n : vertices) {
							vertexSummary += n.getResidueIdentifier()
									+ n.getResiduePosition() + "_"
									+ n.getChainId() + ", ";
						}
						vertexSummary = vertexSummary.substring(0,
								vertexSummary.length() - 2);
						String data = aPdbId + "\t" + cLen + "\t" + sV + "\t"
								+ eV + "\t" + edgeSummary + "\t" + bpSummary
								+ "\t" + vertexSummary + "\n";
						try {
							FileUtils.writeStringToFile(outFile, data, true);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	/*
	 * public X3DNADSSRer(File anInputDir, File anOutputDir, String basisType)
	 * throws Exception{ //check the inputs if(!anInputDir.isDirectory() ||
	 * !anOutputDir.isDirectory()){ throw new
	 * Exception("Invalid parameters! Pass in a valid inputDir and an outputDir"
	 * ); } inputFiles = getFilePathsFromDir(anInputDir, "pdb"); for (String
	 * aFilePath : inputFiles) { Set<NucleicAcid> nucs =
	 * this.runX3DNADSSR(aFilePath); // only one model if (nucs.size() == 1) {
	 * for (NucleicAcid aNuc : nucs) { // check cyclebasis type if
	 * (basisType.equals("fundamental")) { FundamentalCycleBasis<Nucleotide,
	 * InteractionEdge> cb = new FundamentalCycleBasis<Nucleotide,
	 * InteractionEdge>( aNuc); List<Cycle<Nucleotide, InteractionEdge>> ccb =
	 * cb .getCycleBasis(); // the pdbid String aPdbId =
	 * this.getPdbIdFromFilePath(aFilePath); // create an file for output here
	 * inside of // anOutputDir File outFile = new
	 * File(anOutputDir.getAbsolutePath() + "/" + aPdbId + ".cycles.out"); for
	 * (Cycle<Nucleotide, InteractionEdge> cycle : ccb) { int cLen =
	 * cycle.size(); String sV = cycle.getStartVertex() .getResidueIdentifier()
	 * + cycle.getStartVertex() .getResiduePosition() + "_" +
	 * cycle.getStartVertex().getChainId();
	 * 
	 * String eV = cycle.getEndVertex() .getResidueIdentifier() +
	 * cycle.getEndVertex().getResiduePosition() + "_" +
	 * cycle.getEndVertex().getChainId(); // edgeclass String edgeSummary = "";
	 * String bpSummary = ""; List<InteractionEdge> edges = cycle.getEdgeList();
	 * for (InteractionEdge anEdge : edges) { String bpC =
	 * anEdge.extractBasePairClasses(); if (bpC.length() > 0) { bpSummary += bpC
	 * + ", "; } edgeSummary += anEdge.extractEdgeClasses() + "-"; } bpSummary =
	 * bpSummary.substring(0, bpSummary.length() - 2); edgeSummary =
	 * edgeSummary.substring(0, edgeSummary.length() - 1); String vertexSummary
	 * = ""; List<Nucleotide> vertices = cycle.getVertexList(); for (Nucleotide
	 * n : vertices) { vertexSummary += n.getResidueIdentifier() +
	 * n.getResiduePosition() + "_" + n.getChainId() + ", "; } vertexSummary =
	 * vertexSummary.substring(0, vertexSummary.length() - 2); String data =
	 * aPdbId + "\t" + cLen + "\t" + sV + "\t" + eV + "\t" + edgeSummary + "\t"
	 * + bpSummary + "\t" + vertexSummary + "\n"; try { FileUtils
	 * .writeStringToFile(outFile, data, true); } catch (IOException e) {
	 * e.printStackTrace(); } } }// fundamental else if
	 * (basisType.equals("chordless")) { // TODO:add code here
	 * 
	 * } } } } }
	 */

	private static Set<NucleicAcid> runX3DNADSSR(String aPathToPDBFile) {
		File iF = new File(aPathToPDBFile);
		try {
			return ExtractedNucleicAcid.x3dnaDssr(iF);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidResidueException e) {
			e.printStackTrace();
		}

		return null;

	}

	private String getPdbIdFromFilePath(String aPath) {
		String rm = aPath.substring(aPath.lastIndexOf("/") + 1,
				aPath.indexOf("."));
		return rm;
	}

	/**
	 * Create a list of file path strings of a given extension from a given
	 * directory
	 * 
	 * @param aDir
	 *            the directory to search
	 * @param anExtension
	 *            the file extension to use as filter
	 * @return a list of absolute file paths that have the parameter extension
	 */
	private static List<String> getFilePathsFromDir(File aDir,
			String anExtension) {

		List<String> fns = new ArrayList<String>();
		File[] lof = aDir.listFiles();
		for (int i = 0; i < lof.length; i++) {
			File af = lof[i];
			if (af.isFile()) {
				int dotindx = af.getName().lastIndexOf('.');
				if (dotindx > 0) {
					String ext = af.getName().substring(dotindx + 1);
					if (ext.equalsIgnoreCase(anExtension)) {
						fns.add(af.getAbsolutePath());
					}
				}
			}
		}
		return fns;
	}

	/**
	 * @return the inputDir
	 */
	public File getInputDir() {
		return inputDir;
	}

	/**
	 * @param inputDir the inputDir to set
	 */
	private void setInputDir(File inputDir) {
		this.inputDir = inputDir;
	}

	/**
	 * @return the outputDir
	 */
	public File getOutputDir() {
		return outputDir;
	}

	/**
	 * @param outputDir the outputDir to set
	 */
	private void setOutputDir(File outputDir) {
		this.outputDir = outputDir;
	}

}
