/**
 * Copyright (c) 2011 by Jose Cruz-Toledo
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

import java.util.TreeMap;

import org.semanticscience.narf.structures.parts.PDBAtom;

/**
 * A utility class designed for the purpose of creating an ordered list of PDB Atoms
 * @author Jose Cruz-Toledo
 *
 */
public class PDBAtomList extends TreeMap{

	public PDBAtomList(){
		super();
	}
	
	/**
	 * Returns true if the PDBAtom was added succesfully
	 * @param addme
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean add(PDBAtom addme){
		if(addme.getAtomId() < 0|| addme.getAtomId() == -1){
			return false;
		}else{
			this.put(addme.getAtomId(), addme);
			return true;
		}
	}
}
