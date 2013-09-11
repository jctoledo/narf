/**
 * Copyright (c) 2011 Jose Miguel Cruz Toledo
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
package org.semanticscience.narf.structures.factories.secondary;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.semanticscience.narf.structures.lib.PdbHelper;
import org.semanticscience.narf.structures.lib.ProcessRunner;
import org.semanticscience.narf.structures.lib.exceptions.InvalidDotBracketNotationException;
import org.semanticscience.narf.structures.lib.exceptions.InvalidSequenceException;
import org.semanticscience.narf.structures.parts.DotBracketNotation;
import org.semanticscience.narf.structures.parts.Sequence;
import org.semanticscience.narf.structures.secondary.PredictedSecondaryStructure;

/**
 * The factory to create secondary structures using inforamtion produced by the
 * RNAfold secondary structure predictor.
 * 
 * @author Jose Cruz-Toledo
 * @author William Greenwood
 * @version %I%, %G%
 * @since 1.6
 */
public final class Rnafold extends PredictedSecondaryStructureFactory {
	static Logger log = Logger.getLogger(Rnafold.class);
	/**
	 * Construct the factory for the RNAfold secondary structure predictor.
	 */
	public Rnafold() {
		super("RNAfold", "1.8.5");
	}

	@Override
	public Set<PredictedSecondaryStructure> getStructures(Sequence aSequence,
			String args[]) throws InvalidDotBracketNotationException,
			InvalidSequenceException, IOException {
		// fold
		String rnafoldOutput = this.execute(aSequence, args);

		// split by new line
		String[] outSplit = rnafoldOutput.split("\\n");

		// make sure that there are only two lines
		if (outSplit.length < 2) {
			throw new IOException("RNAFold output problem !!!\n"
					+ rnafoldOutput);
		}

		Set<PredictedSecondaryStructure> ssSet = new HashSet<PredictedSecondaryStructure>();

		for (int i = 1; i < outSplit.length; i++) {
			Pattern dbnMfePattern = Pattern
					.compile("(^[\\.\\(\\)\\[\\]\\{\\}]+)\\s+\\((.*)\\)");
			Matcher dbnMfeMatcher = dbnMfePattern.matcher(outSplit[i].trim());

			if (!dbnMfeMatcher.matches()) {
				throw new InvalidDotBracketNotationException();
			}

			DotBracketNotation dbn = new DotBracketNotation(aSequence,
					dbnMfeMatcher.group(1));
			double mfe = Double.valueOf(dbnMfeMatcher.group(2));
			ssSet.add(new PredictedSecondaryStructure(this, i, dbn, mfe));
		}

		return ssSet;
	}

	@Override
	protected String execute(Sequence aSequence, String[] commands)
			throws InvalidSequenceException, IOException {
		String returnMe = "";
		if (aSequence.getLength() == 0) {
			throw new InvalidSequenceException(
					"The sequence provided to execute RNAFold was empty!");
		}

		String[] cmdArr = new String[commands.length + 1];
		System.arraycopy(new String[] { "RNAfold" }, 0, cmdArr, 0, 1);
		System.arraycopy(commands, 0, cmdArr, 1, commands.length);

		Process p = Runtime.getRuntime().exec(cmdArr);
		InputStream output_stream = p.getInputStream();
		InputStream seq_stream = PdbHelper.makeInputStreamFromString(aSequence
				.getSequenceString());
		OutputStream out_stream = p.getOutputStream();
		InputStream error_stream = p.getErrorStream();

		ByteArrayOutputStream out_baos = new ByteArrayOutputStream();
		ByteArrayOutputStream error_baos = new ByteArrayOutputStream();

		try {
			// A thread for handling the input to RNAFold
			Thread stdInThread = ProcessRunner.bindStreams(seq_stream,
					out_stream, seq_stream + " feed", true);
			// A thread for handling the output of RNAFold
			Thread stdOutThread = ProcessRunner.bindStreams(output_stream,
					out_baos, "writer", false);
	
			try {
				int status = p.waitFor();
				if (status != 0) {
					log.error("Something bad happened!\n!");
					System.exit(-1);
				}
			} catch (InterruptedException e) {
				log.error("wating problem\n", e);
				System.exit(-1);
			}
			returnMe = out_baos.toString();
			if (returnMe.length() > 0) {
				return returnMe;
			}
			stdInThread.join(10*1000);
			stdOutThread.join(10*1000);
		} catch (InterruptedException e) {
			log.error("problem\n", e);
		} finally {
			try {
				output_stream.close();
				seq_stream.close();
				out_stream.close();
				out_baos.close();
			} catch (IOException e) {
				log.error("io problem\n", e);
			}
			p.destroy();
		}

		return returnMe;
	}

}
