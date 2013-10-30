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
import java.util.List;
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
import org.semanticscience.narf.structures.lib.exceptions.InvalidResidueException;
import org.semanticscience.narf.structures.parts.Nucleotide;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * @author Jose Cruz-Toledo
 * 
 */
public class CycleExtractor {
	public static void main(String[] args) throws Exception {
		Options options = createOptions();
		CommandLineParser p = createCliParser();
		String inputdirStr = null;
		File inputDir = null;
		String outputdirStr = null;
		File outputDir = null;

		String format = null;
		try {
			CommandLine c = p.parse(options, args);
			if (c.hasOption("help")) {
				printUsage();
				System.exit(1);
			}
			if (c.hasOption("inputDir")) {
				inputdirStr = c.getOptionValue("inputDir");
				inputDir = new File(inputdirStr);
			} else {
				System.out.println("You must specify an input directory!");
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
			// from the input directory get a list of input files<String>
			List<String> inputFiles = CycleExtractor.getFilePathsFromDir(
					inputDir, "pdb");
			for (String aFilePath : inputFiles) {
				Set<NucleicAcid> nucs = CycleExtractor.runX3DNADSSR(aFilePath);
				if (nucs == null || nucs.size() == 0) {
					throw new CycleException("Could not extract cycles from :"
							+ aFilePath);
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
										aPdbId,aNuc, ccb);
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
										aPdbId, aNuc, ccb);
								File outputFile = new File(
										outputDir.getAbsolutePath() + "/"
												+ aPdbId + "_cycles.tsv");
								FileUtils.writeStringToFile(outputFile, tsv);
							}
						}
					}
				}
			}
		} catch (ParseException e) {
			System.out.println("Unable to parse specified options.");
			printUsage();
			System.exit(1);
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
		} catch (InvalidResidueException e) {
			e.printStackTrace();
		}
		return null;
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
		Option inputDir = OptionBuilder
				.hasArg(true)
				.withDescription(
						"The directory where your input PDB files are located")
				.create("inputDir");
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
		o.addOption(outputFormat);
		o.addOption(inputDir);
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
