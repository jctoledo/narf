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
package org.semanticscience.narf.structures.lib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import org.semanticscience.narf.structures.lib.exceptions.InvalidResidueException;
import org.semanticscience.narf.structures.parts.Nucleotide;

/**
 * PDB utility methods to check for various attributes found in PDB structure
 * files.
 * 
 * @author William Greenwood
 * @author Jose Cruz-Toledo
 * @since 1.6
 */

// TODO: fix getPDBModel so that it includes the header of the PDB file

public class PdbHelper {

	/**
	 * Create a mapping of chain (polymer) identifiers of a PDB structure to
	 * nucleotide residue positions to nucleotide objects from a PDB structure
	 * file. Returns a map where the key is the chain identifier and the value
	 * is a map where the key is the residue position as given in the pdb file
	 * and the value the corresponding nucleotide
	 * 
	 * @param aFile
	 *            the PDB structure file
	 * @return a map of chain identifiers to nucleotide residue positions to
	 *         nucleotide objects. As numbered in the PDB file
	 * @throws IOException
	 *             if any IO error occurs reading the PDB structure file
	 * @throws FileNotFoundException
	 *             if the PDB structure file does not exist
	 * @throws InvalidResidueException
	 *             if any of the created residues are invalid
	 * @since 1.6
	 */
	@SuppressWarnings("resource")
	public static Map<String, Map<Integer, Nucleotide>> getNucleotideMapping(
			File aFile) throws IOException, FileNotFoundException,
			InvalidResidueException {
		String aPdbId = PdbHelper.findPdbId(aFile);
		Map<String, Map<Integer, Nucleotide>> returnMe = new HashMap<String, Map<Integer, Nucleotide>>();
		Map<String, Integer> startingPositions = PdbHelper
				.getStartingPositions(aFile);
		BufferedReader in = new BufferedReader(new FileReader(aFile));
		String extension = PdbHelper.getFileExtension(aFile);
		if (extension.equals("gz")) {
			in = new BufferedReader(new InputStreamReader(new GZIPInputStream(
					new FileInputStream(aFile))));
		}
		int residueCounter = 1;
		String chain = "";
		String line = "";
		while ((line = in.readLine()) != null) {
			line = line.trim();
			if (line.startsWith("SEQRES")) {
				String[] values = line.split("\\s+");
				if (!values[2].equals(chain)) {
					chain = values[2];
					returnMe.put(chain, new TreeMap<Integer, Nucleotide>());
					try{
					residueCounter = startingPositions.get(chain);
					}catch (NullPointerException e){
						System.out.println("Check file: "+ aFile.getAbsolutePath());
						e.printStackTrace();
					}
				}
				for (int i = 4; i < values.length; i++) {
					Nucleotide an = null;
					try {
						an = new Nucleotide(aPdbId, chain, residueCounter,
								values[i], null, null, null);
					} catch (InvalidResidueException e) {
						System.out.println("PLEASE Check File: "
								+ aFile.getName());
						System.out.println(an);
						e.printStackTrace();
						return null;
					}
					if (an != null) {
						returnMe.get(chain).put(residueCounter, an);
						residueCounter++;
					}
				}
			}
		}
		in.close();
		return returnMe;
	}

	/**
	 * Extract a single model from a PDB file. This method will return a file
	 * with the original header plus the coordinate information for the desired
	 * model.
	 * 
	 * @param aPdbFile
	 *            the PDB structure file
	 * @param anOutputDirectory
	 *            the output directory where the model of the PDB file will be
	 *            written
	 * @param aPdbId
	 *            the PDB structure identifer
	 * @param aModelNumber
	 *            the model number of the conformer structure
	 * @return a file with the specified model number of the PDB file
	 * @throws FileNotFoundException
	 *             if the PDB structure file does not exist
	 * @throws IOException
	 *             if any IO error occur reading the PDB file and writing the
	 *             model file
	 * @since 1.6
	 */
	public static File extractModelFromPDB(File aPdbFile,
			File anOutputDirectory, String aPdbId, int aModelNumber)
			throws IOException {
		if (!aPdbFile.exists()) {
			throw new FileNotFoundException("No PDB file found!.");
		}
		// get the total number of Models
		int tNM = PdbHelper.findNumberOfModels(aPdbFile);
		if ((tNM < 1) || (aModelNumber > tNM)) {
			throw new IndexOutOfBoundsException(
					"The model number provided is outside the permited range :[1-"
							+ tNM + "]\n");
		}
		// check if there is only one model
		if (tNM == 1) {
			return aPdbFile;
		}
		BufferedReader br = new BufferedReader(new FileReader(aPdbFile));
		File returnMe = new File(anOutputDirectory.getAbsolutePath(),
				aPdbId.toLowerCase() + "_M_" + Integer.toString(aModelNumber)
						+ ".ent");
		BufferedWriter out = new BufferedWriter(new FileWriter(returnMe));
		String pattern = "^MODEL\\s+(\\d+)";
		Pattern myPattern = Pattern.compile(pattern);
		String s = "";
		String aLine = "";
		outerLoop: while ((aLine = br.readLine()) != null) {
			// add the header to the output file
			out.write(PdbHelper.getHeaderFromPDBFile(aPdbFile));
			Matcher m = myPattern.matcher(aLine.trim());
			if (m.matches()) {
				if (Integer.parseInt(m.group(1)) == aModelNumber) {
					String pat2 = "^ENDMDL";
					Pattern p2 = Pattern.compile(pat2);
					String aLine2 = "";
					while ((aLine2 = br.readLine()) != null) {
						Matcher m2 = p2.matcher(aLine2.trim());
						if (m2.matches()) {
							break outerLoop;
						} else {
							out.write(aLine2 + "\n");
						}
					}
				}
			}
		}
		out.close();
		br.close();
		return returnMe;
	}

	/**
	 * This method parses a PDB file and returns the HEADER (Everything before
	 * the ATOM or MODEL directive)
	 * 
	 * @param aPdbFile
	 *            a PDBFile
	 * @return the contents of the header
	 * @throws IOException
	 */
	public static String getHeaderFromPDBFile(File aPdbFile) throws IOException {
		String returnMe = "";
		if (!aPdbFile.exists()) {
			throw new FileNotFoundException("No PDB file found!.");
		}
		BufferedReader br = new BufferedReader(new FileReader(aPdbFile));
		String aLine = "";
		String s1 = "^MODEL\\s+(\\d+)";
		Pattern p1 = Pattern.compile(s1);
		String s2 = "^ATOM\\s+(.*)";
		Pattern p2 = Pattern.compile(s2);
		while (((aLine = br.readLine()) != null)) {
			Matcher m1 = p1.matcher(aLine.trim());
			Matcher m2 = p2.matcher(aLine.trim());
			if (m1.matches()) {
				break;
			}
			if (m2.matches()) {
				break;
			}
			returnMe += aLine.trim() + "\n";

		}
		return returnMe;
	}

	/**
	 * Create an InputStream from a pathname
	 * 
	 * @param aStream
	 *            the pathname
	 * @return
	 */
	public static InputStream makeInputStreamFromString(String aStream) {
		byte[] bytes = aStream.getBytes();
		InputStream returnMe = new ByteArrayInputStream(bytes);
		try {
			returnMe.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return returnMe;
	}

	/**
	 * Retrieve the file extension
	 * 
	 * @param aFile
	 *            a File
	 * @return the extension of the file
	 */
	public static String getFileExtension(File aFile) {
		String returnMe = aFile.getName().substring(
				aFile.getName().lastIndexOf('.') + 1);
		return returnMe;
	}

	/**
	 * Finds the nucleotide residue positions that each chain in a PDB structure
	 * file ends with.
	 * 
	 * @param aFile
	 *            a PDB structure file
	 * @return a mapping of the chain identifier to the nucleotide residue
	 *         position that each chain ends with
	 * @throws IOException
	 *             if any IO error occurs reading the PDB structure file
	 * @throws FileNotFoundException
	 *             if the PDB structure file does not exist
	 * @since 1.6
	 */
	public static Map<String, Integer> getEndingPositions(File aFile)
			throws IOException, FileNotFoundException {

		Map<String, Integer> endPositions = new HashMap<String, Integer>();

		String line;
		BufferedReader in;
		String extension = PdbHelper.getFileExtension(aFile);

		if (extension.equals("gz")) { // PDB files that are gzipped can be used
			in = new BufferedReader(new InputStreamReader(new GZIPInputStream(
					new FileInputStream(aFile))));
		} else {
			in = new BufferedReader(new FileReader(aFile));
		}

		while ((line = in.readLine()) != null) {
			if (line.startsWith("DBREF ") || line.startsWith("DBREF1")) {
				String chainLabel = line.substring(13 - 1, 13);

				int seqEnd = Integer
						.parseInt(line.substring(21 - 1, 24).trim());
				endPositions.put(chainLabel, seqEnd);
			}

			if (line.startsWith("SEQADV ")) {
				break;
			}
		}

		in.close();

		return endPositions;
	}

	/**
	 * Finds the nucleotide residue positions that each chain in a PDB structure
	 * file begins with.
	 * 
	 * @param aFile
	 *            a PDB structure file
	 * @return a mapping of the chain identifier to the nucleotide residue
	 *         position that each chain begins with
	 * @throws IOException
	 *             if any IO error occurs reading the PDB structure file
	 * @throws FileNotFoundException
	 *             if the PDB structure file does not exist
	 * @since 1.6
	 */
	public static Map<String, Integer> getStartingPositions(File file)
			throws IOException, FileNotFoundException {
		Map<String, Integer> startPositions = new HashMap<String, Integer>();
		try {
			String line;
			BufferedReader in;
			String extension = PdbHelper.getFileExtension(file);
			if (extension.equals("gz")) {
				in = new BufferedReader(new InputStreamReader(
						new GZIPInputStream(new FileInputStream(file))));
			} else {
				in = new BufferedReader(new FileReader(file));
			}

			while ((line = in.readLine()) != null) {
				if (line.startsWith("DBREF ") || line.startsWith("DBREF1")) {
					String chainLabel = line.substring(13 - 1, 13);
					int seqBegin = Integer.parseInt(line.substring(15 - 1, 18)
							.trim());
					startPositions.put(chainLabel, seqBegin);
					if (line.startsWith("SEQADV ")) {
						break;
					}
				}
			}
			in.close();
		} catch (IOException e) {
			System.out.println(e);
		}
		return startPositions;
	}

	/**
	 * Find the four letter alphanumeric identifier of a PDB structure file
	 * 
	 * @param aFile
	 *            a PDB structure file
	 * @return the PDB identifier
	 * @throws IOException
	 *             if any IO error occurs reading the PDB structure file
	 * @throws FileNotFoundException
	 *             if the PDB structure file does not exist
	 * @since 1.6
	 */
	public static String findPdbId(File file) throws IOException,
			FileNotFoundException {

		String line;
		String returnMe = "";
		BufferedReader in;
		String extension = PdbHelper.getFileExtension(file);

		if (extension.equals("gz")) { // PDB files that are gzipped can be used
			in = new BufferedReader(new InputStreamReader(new GZIPInputStream(
					new FileInputStream(file))));
		} else {
			in = new BufferedReader(new FileReader(file));
		}

		while ((line = in.readLine()) != null) {
			if (line.startsWith("HEADER")) {
				// PDB identifier is mentioned between the 63rd and 66th
				// characters
				returnMe = line.substring(63 - 1, 67 - 1).trim();
			}
		}
		in.close();
		return returnMe.toUpperCase();
	}

	/**
	 * Find the number of structure conformers (models) present in a PDB
	 * structure file.
	 * 
	 * @param aFile
	 *            the PDB structure file.
	 * @return the number of structure conformers
	 * @throws IOException
	 *             if any IO error occurs reading the PDB structure file
	 * @throws FileNotFoundException
	 *             if the PDB structure file does not exist
	 * @since 1.6
	 */
	public static int findNumberOfModels(File aFile) throws IOException,
			FileNotFoundException {
		Integer returnMe = new Integer(-1);
		String line;
		BufferedReader in;
		String extension = PdbHelper.getFileExtension(aFile);

		if (extension.equals("gz")) { // PDB files that are gzipped can be used
			in = new BufferedReader(new InputStreamReader(new GZIPInputStream(
					new FileInputStream(aFile))));
		} else {
			in = new BufferedReader(new FileReader(aFile));
		}
		while ((line = in.readLine()) != null) {
			if (line.startsWith("NUMMDL")) {
				returnMe = Integer.parseInt(line.substring(11 - 1, 15 - 1)
						.trim());
			}
		}
		in.close();
		if (returnMe.intValue() == -1) {
			return 1;
		} else {
			return returnMe;
		}
	}

	public static String makeAtomLine(int atomPos, String atomLabel,
			String residueName, String chain, int position, double x, double y,
			double z, double occ, double bfact, String atomLetter) {
		String buf = "";
		buf += "ATOM\t" + atomPos + "\t" + atomLabel + "\t" + residueName
				+ "\t" + chain + "\t" + position + "\t" + x + "\t" + y + "\t"
				+ z + "\t" + occ + "\t" + bfact + "\t" + atomLetter + "\n";
		return buf;
	}
}
