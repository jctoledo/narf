/**
 * Copyright (c) 2011 William Greenwood
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

package org.semanticscience.narf.structures.factories;

/**
 * An abstract class for nucleic acid structure annotators and predictors to minimize the effort
 * required for their implementation.
 * @author William Greenwood
 * @since 1.6
 */

public abstract class GeneratedStructureFactory {

	/**
	 * The name of the structure generator
	 */
	private final String PROGRAM_NAME;
	/**
	 * The version of the structure generator
	 */
	private final String PROGRAM_VERSION;
	
	
	/**
	 * Construct a generated structure factory for generated nucleic acid structures.
	 * @param aProgramName the name of the program
	 * @param aProgramVersion the version of the program
	 */
	protected GeneratedStructureFactory(String aProgramName, String aProgramVersion){
		PROGRAM_NAME = aProgramName;
		PROGRAM_VERSION = aProgramVersion;
	}

	/**
	 * Get the name of the program that generated the structure.
	 * @return the program name
	 */
	public String getProgramName() {
		return PROGRAM_NAME;
	}

	/**
	 * Get the version of the program that generated the structure.
	 * @return the program version
	 */
	public String getProgramVersion() {
		return PROGRAM_VERSION;
	}
}
