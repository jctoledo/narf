/**
 * Copyright (c) 2011 "Jose Cruz-Toledo" and "William Greenwood"
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
package org.semanticscience.narf.structures.lib.exceptions;

/**
 * Signals that the nucleotides passed to a nucleotide sequence object are 
 * not in consecutive order.
 * @author "Jose Cruz-Toledo"
 * @author "William Greenwood"
 * @version %I%, %G%
 * @since 1.6
 */
public class NonConsecutiveNucleotideException extends Exception{

	private static final long serialVersionUID = 3270755208828007011L;
	
	public NonConsecutiveNucleotideException(){}
	public NonConsecutiveNucleotideException(String msg){super(msg);}
	public NonConsecutiveNucleotideException(Throwable cause){super(cause);}
	public NonConsecutiveNucleotideException(String msg, Throwable cause){super(msg, cause);}
	
}
