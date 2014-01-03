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
package org.semanticscience.narf.graphs.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.semanticscience.narf.graphs.lib.CycleSerializer;
import org.semanticscience.narf.graphs.lib.cycles.Cycle;
import org.semanticscience.narf.graphs.lib.cycles.FundamentalCycleBasis;
import org.semanticscience.narf.graphs.lib.cycles.exceptions.CycleException;
import org.semanticscience.narf.graphs.nucleicacid.ExtractedNucleicAcid;
import org.semanticscience.narf.graphs.nucleicacid.InteractionEdge;
import org.semanticscience.narf.graphs.nucleicacid.NucleicAcid;
import org.semanticscience.narf.graphs.nucleicacid.PredictedNucleicAcid;
import org.semanticscience.narf.structures.lib.exceptions.InvalidDotBracketNotationException;
import org.semanticscience.narf.structures.lib.exceptions.InvalidResidueException;
import org.semanticscience.narf.structures.lib.exceptions.InvalidSequenceException;
import org.semanticscience.narf.structures.parts.Nucleotide;
import org.semanticscience.narf.structures.parts.Sequence;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * @author Jose Cruz-Toledo
 * 
 */
public class CycleExtractor {
	public static void main(String[] args) {
		Options options = createOptions();
		CommandLineParser p = createCliParser();
		String inputPDBDirStr = null;
		String inputSeqFileStr = null;
		File inputSeqFile = null;
		File inputPDBDir = null;
		String outputdirStr = null;
		File outputDir = null;

		String format = null;
		try {
			CommandLine c = p.parse(options, args);
			if (c.hasOption("help")) {
				printUsage();
				System.exit(1);
			}
			if (c.hasOption("inputPDBDir")) {
				inputPDBDirStr = c.getOptionValue("inputPDBDir");
				inputPDBDir = new File(inputPDBDirStr);
			}

			if (c.hasOption("inputSeqFile")) {
				inputSeqFileStr = c.getOptionValue("inputSeqFile");
				inputSeqFile = new File(inputSeqFileStr);
			}
			// check that either the input sequence file or the input PDB
			// directory are specified
			if (inputSeqFile == null && inputPDBDir == null) {
				System.out
						.println("Either an input PDB directory or an input sequence file must be specified!");
				printUsage();
				System.exit(1);
			}
			if (c.hasOption("outputDir")) {
				outputdirStr = c.getOptionValue("outputDir");
				outputDir = new File(outputdirStr);
			} else {
				System.out.println("You must specify an output directory!");
				printUsage();
				System.exit(1);
			}
			if (c.hasOption("outputFormat")) {
				format = c.getOptionValue("outputFormat");
			} else {
				System.out.println("You must specify an output format!");
				printUsage();
				System.exit(1);
			}
			if (inputPDBDir != null) {
				// from the input directory get a list of input files<String>
				List<String> inputFiles = CycleExtractor.getFilePathsFromDir(
						inputPDBDir, "pdb");
				for (String aFilePath : inputFiles) {
					Set<NucleicAcid> nucs = CycleExtractor
							.runX3DNADSSR(aFilePath);
					if (nucs == null || nucs.size() == 0) {
						throw new CycleException(
								"Could not extract cycles from :" + aFilePath);
					} else {
						// only one model
						if (nucs.size() == 1) {
							for (NucleicAcid aNuc : nucs) {
								List<Cycle<Nucleotide, InteractionEdge>> ccb = aNuc
										.getMinimumCycleBasis();
								// the pdbid
								String aPdbId = CycleExtractor
										.getPdbIdFromFilePath(aFilePath);
								if (format.equals("RDF")) {
									Model m = CycleSerializer.createNarfModel(
											aPdbId, aNuc, ccb);
									// make an output file
									File outputFile = new File(
											outputDir.getAbsolutePath() + "/"
													+ aPdbId + "_cycles.rdf");
									// create a fop
									FileOutputStream fop = new FileOutputStream(
											outputFile);
									m.write(fop);
									fop.close();
								} else if (format.equals("tsv")) {
									String tsv = CycleSerializer.createNarfTsv(
											aPdbId, aNuc, ccb, null, -1);
									File outputFile = new File(
											outputDir.getAbsolutePath() + "/"
													+ aPdbId + "_cycles.tsv");
									FileUtils
											.writeStringToFile(outputFile, tsv);
								}
							}
						}
					}
				}
			} else if (inputSeqFile != null) {
				// open the file
				List<String> lines = FileUtils.readLines(inputSeqFile);
				// foreach line in the file
				for (String aLine : lines) {
					// get a sequence
					List<String> sl = Arrays.asList(aLine.split(","));
					// skip the first line
					if (sl.get(0).equals("sequence")) {
						continue;
					}
					String aSeq = sl.get(0).replace("\"", "");
					// call runMfold
					Set<NucleicAcid> nas = CycleExtractor.runMfold(aSeq);
					if (nas == null || nas.size() == 0) {
						throw new CycleException(
								"Could not extract cycles from :" + sl);
					} else {
						if (nas.size() == 1) {
							for (NucleicAcid aNuc : nas) {
								Random r = new Random();
								int rand = (r.nextInt(65536)-32768);
								// get the MCB of each prediction
								List<Cycle<Nucleotide, InteractionEdge>> ccb = aNuc
										.getMinimumCycleBasis();
								//get the aptamer type
								String apt_type = sl.get(5).replace("\"", "");
								//get the selex experiment mid
								String se_mid = sl.get(4).replace("\"", "");
								se_mid = se_mid.replace("\\/", "");
								//se_mid += rand;
								if(format.equals("RDF")){
									
								}else if(format.equals("tsv")){
									String tsv = CycleSerializer.createNarfTsv(se_mid, aNuc, ccb,apt_type,rand);
									File outputFile = new File(
											outputDir.getAbsolutePath() + "/"
													+ se_mid+"-"+rand + "_cycles.tsv");
									FileUtils
									.writeStringToFile(outputFile, tsv);
								}
								
							}

						}

					}
				}
			}
		} catch (ParseException e) {
			System.out.println("Unable to parse specified options.");
			printUsage();
			System.exit(1);
		} catch (CycleException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Set<NucleicAcid> runX3DNADSSR(String aPathToPDBFile) {
		File iF = new File(aPathToPDBFile);
		try {
			return ExtractedNucleicAcid.x3dnaDssr(iF);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Runs Mfold on the given sequence string
	 * 
	 * @param aSequence
	 *            a string of valid sequence characters which should only
	 *            contain these characters:
	 *            [AaCcGgTtUuRrYyKkMmSsWwBbDdHhVvNnXx-]+
	 * @return a set of Nucleic acids
	 */
	private static Set<NucleicAcid> runMfold(String aSequence) {
		Set<NucleicAcid> rm = null;
		try {
			Sequence s = new Sequence(aSequence);
			rm = PredictedNucleicAcid.rnafold(s);
			return rm;
		} catch (InvalidSequenceException e) {
			System.out.println("invalid sequence: " + aSequence);
			return null;
		} catch (InvalidResidueException e) {
			System.out.println("invalid sequence: " + aSequence);
			return null;
		} catch (InvalidDotBracketNotationException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
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

	private static String getPdbIdFromFilePath(String aPath) {
		String rm = aPath.substring(aPath.lastIndexOf("/") + 1,
				aPath.indexOf("."));
		return rm;
	}

	@SuppressWarnings("static-access")
	private static Options createOptions() {
		Options o = new Options();
		OptionBuilder.withArgName("/path/to/input/dir");
		Option inputPDBDir = OptionBuilder
				.hasArg(true)
				.withDescription(
						"The directory where your input PDB files are located")
				.create("inputPDBDir");
		Option inputSeqFile = OptionBuilder
				.hasArg(true)
				.withDescription(
						"The path to the file of one aptamer sequence per line")
				.create("inputSeqFile");
		Option outputDir = OptionBuilder
				.withArgName("/path/to/output/dir")
				.hasArg(true)
				.withDescription(
						"The directory where the cycle output will be stored")
				.create("outputDir");
		Option outputFormat = OptionBuilder.withArgName("outputFormat")
				.hasArg(true)
				.withDescription("The output format for the cycles (RDF|tsv)")
				.isRequired().create("outputFormat");
		o.addOption(inputSeqFile);
		o.addOption(outputFormat);
		o.addOption(inputPDBDir);
		o.addOption(outputDir);
		return o;
	}

	private static void printUsage() {
		HelpFormatter hf = new HelpFormatter();
		hf.printHelp("cycleExtractor [OPTIONS]", createOptions());
	}

	private static CommandLineParser createCliParser() {
		return new GnuParser();
	}
}
