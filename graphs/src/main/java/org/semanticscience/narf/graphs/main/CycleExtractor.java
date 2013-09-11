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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.semanticscience.narf.graphs.bin.X3DNADSSRer;


/**
 * @author Jose Cruz-Toledo
 * 
 */
public class CycleExtractor {
	public static void main(String[] args) {
		Options options = createOptions();
		CommandLineParser p = createCliParser();
		String inputdir = null;
		String outputdir = null;
		try{
			CommandLine c = p.parse(options, args);
			if(c.hasOption("inputDir")){
				inputdir= c.getOptionValue("inputDir");
			}
			if(c.hasOption("outputDir")){
				outputdir= c.getOptionValue("outputDir");
			}
			if(inputdir != null && outputdir !=null){
				X3DNADSSRer x = new X3DNADSSRer(new File(inputdir), new File(outputdir), "fundamental");
			}
		}catch(ParseException e){
			e.printStackTrace();
		} 

	}

	@SuppressWarnings("static-access")
	private static Options createOptions() {
		Options o = new Options();
		OptionBuilder
				.withArgName("/path/to/input/dir");
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
		o.addOption(inputDir);
		o.addOption(outputDir);
		return o;
	}

	private static CommandLineParser createCliParser() {
		return new GnuParser();
	}
}
