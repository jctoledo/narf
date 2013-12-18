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
package org.semanticscience.narf.graphs.nucleicacid;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Jose Cruz-Toledo
 * 
 */
public class AbstractNucleicAcidTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void test() throws IOException, InterruptedException {
		// Runtime rt = Runtime.getRuntime();
		// String [] cmds = {"python",
		// "/home/jose/Documents/research/aptamerbase_trends/python-scripts/mfold_seqs.py"};
		ProcessBuilder ps = new ProcessBuilder(
				"python",
				"/home/jose/Documents/research/aptamerbase_trends/python-scripts/mfold_one.py",
				"AGGGCUCUAGUUCUAUGUCUAGUAGUCAGUCUAGUCAGUUUGGGGCCCCCCCCUUUUCUCUCUCUCG");
		ps.redirectErrorStream(true);
		Process pr = ps.start();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				pr.getInputStream()));
		String line;
		while ((line = in.readLine()) != null) {
			System.out.println(line);
		}
		pr.waitFor();
		System.out.println("ok!");

		in.close();
	}

}
