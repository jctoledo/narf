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
package org.semanticscience.narf.structures.tertiary;

import java.util.Map;
import java.util.Set;

import org.semanticscience.narf.structures.Structure;
import org.semanticscience.narf.structures.interactions.NucleotideInteraction;
import org.semanticscience.narf.structures.parts.Sequence;

/**
 * Representation of the tertiary structure of nucleic acids.
 * 
 * @author William Greenwood
 * @author Jose Cruz-Toledo
 * @since 1.6
 */

public class TertiaryStructure extends Structure {
	/**
	 * The PDB identifier for this structure
	 */
	protected String pdbId;

	/**
	 * Construct a tertiary structure from a series of sequences and a set of
	 * interactions.
	 * 
	 * @param aModelNumber
	 *            model number of the nucleic acid tertiary structure
	 *            conformation
	 * @param aSequenceMap
	 *            map of the chain identifier of the nucleic acid to the
	 *            sequence of the chain
	 * @param someInteractions
	 *            interactions present in the nucleic acid tertiary structure
	 */
	public TertiaryStructure(int aModelNumber,
			Map<String, Sequence> aSequenceMap,
			Set<NucleotideInteraction> someInteractions) {
		super(aModelNumber, aSequenceMap, someInteractions);
	}

	public TertiaryStructure(int aModelNumber, Map<String, Sequence> aSeqMap,
			Set<NucleotideInteraction> someInteractions, String aPdbId) {
		this(aModelNumber, aSeqMap, someInteractions);
		pdbId = aPdbId;
	}
	
	/**
	 * @return the pdbid for this 3d structure
	 */
	public String getPDBId() {
		return pdbId;

	}
}
