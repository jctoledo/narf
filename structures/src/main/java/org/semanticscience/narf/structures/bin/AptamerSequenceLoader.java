/**
 * Copyright (c) 2012  Jose Cruz-Toledo
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
package org.semanticscience.narf.structures.bin;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.semanticscience.narf.structures.ext.AptamerSequence;
import org.semanticscience.narf.structures.lib.exceptions.InvalidResidueException;
import org.semanticscience.narf.structures.lib.exceptions.InvalidSequenceException;
import org.semanticscience.narf.structures.parts.Sequence;

/**
 * Load Aptamer Sequences from FASTA files. See @AptamerSequenceTopic
 * (narf->structures)
 * 
 * @author Jose Cruz-Toledo
 * 
 */
public class AptamerSequenceLoader {
	static Logger log = Logger.getLogger(AptamerSequenceLoader.class);
	private File inputFastaFile;
	private List<AptamerSequence> aptamerList;

	public AptamerSequenceLoader(File anInputFastaFile) {
		inputFastaFile = anInputFastaFile;
		aptamerList = parseInputFile(inputFastaFile);
	}

	private List<AptamerSequence> parseInputFile(File inputFastaFile2) {
		List<AptamerSequence> rm = new ArrayList<AptamerSequence>();
		try {
			String allLines = FileUtils.readFileToString(inputFastaFile2);
			String[] tmpA = allLines.split("\\n>");
			for (int i = 0; i < tmpA.length; i++) {
				AptamerSequence as = parseInputFastaString(tmpA[i]);
				if (as != null) {
					rm.add(as);
				}
			}
			return rm;
		} catch (IOException e) {
			log.error("Could not read input file! ", e);
		}

		return null;
	}

	// parse one FASTA string at a time
	private AptamerSequence parseInputFastaString(String aStr) {
		AptamerSequence rm = null;
		if (aStr.length() != 0) {
			// split by new lines
			String lines[] = aStr.split("\\n");
			if (lines.length >= 2) {
				String defline = lines[0];
				// now parse the definition line
				String[] defLineA = defline.split("\\s|\\s");
				// check the defline length
				if (defLineA.length == 9) {
					String stype = null;
					URL url = null;
					Double avgKd = null;
					for (int j = 0; j < defLineA.length; j++) {
						if (!defLineA[j].equals("|")) {
							stype = defLineA[0];
							try {
								url = new URL(defLineA[2]);
								String[] s = defLineA[8].split(":");
								avgKd = new Double(s[1]);
							} catch (MalformedURLException e) {
								log.warn(e);
							} catch (NumberFormatException e) {
								avgKd = -1.0;
							} catch (IndexOutOfBoundsException e) {
								avgKd = -1.0;
							}

						}
					}
					// get the sequence
					String seqBuf = "";
					for (int i = 1; i < lines.length; i++) {
						seqBuf += lines[i];
					}
					try {
						Sequence aSeq = new Sequence(seqBuf);

						if (stype != null && url != null && avgKd != null) {
							rm = new AptamerSequence(aSeq, stype, avgKd, url);
							return rm;
						}
					} catch (InvalidSequenceException e) {
						log.warn("Invalid Sequence ", e);
					} catch (InvalidResidueException e) {
						log.warn("Invalid residue", e);
					}
				}

			}
		}
		return null;
	}

	public List<AptamerSequence> getAptamerSequenceList() {
		return this.aptamerList;
	}
}
