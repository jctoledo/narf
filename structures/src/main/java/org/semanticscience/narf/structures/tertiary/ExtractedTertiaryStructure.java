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
package org.semanticscience.narf.structures.tertiary;

import java.io.File;
import java.util.Map;
import java.util.Set;

import org.semanticscience.narf.structures.ExtractedStructure;
import org.semanticscience.narf.structures.factories.GeneratedStructureFactory;
import org.semanticscience.narf.structures.factories.tertiary.ExtractedTertiaryStructureFactory;
import org.semanticscience.narf.structures.interactions.NucleotideInteraction;
import org.semanticscience.narf.structures.parts.Sequence;



/**
 * Representation of the tertiary structure of a nucleic acid generated using information
 * from tertiary structure annotator.
 * @author William Greenwood
 * @version %I%, %G%
 * @since 1.6
 */

public class ExtractedTertiaryStructure extends TertiaryStructure implements ExtractedStructure{

	
	/**
	 * The factory that generated the predicted secondary structure
	 */
	private final ExtractedTertiaryStructureFactory structureFactory;
	
	
	/**
	 * The four letter identifier for PDB structure files
	 */
	private final String pdbId;
	
	/**
	 * The PDB structure file that was extracted to generate this structure object
	 */
	private final File pdbFile;
	
	/**
	 * Construct a annotated tertiary structure from a series of sequences and a set of interactions.
	 * @param aModelNumber model number of the nucleic acid tertiary structure conformation
	 * @param aSequenceMap map of the chain identifier of the nucleic acid to the sequence of the chain
	 * @param someInteractions interactions present in the nucleic acid tertiary structure
	 */
	public ExtractedTertiaryStructure(ExtractedTertiaryStructureFactory aStructureFactory, 
			File aPdbFile, String aPdbId, int aModelNumber, Map<String, Sequence> aSequenceMap, 
			Set<NucleotideInteraction> someInteractions) {
		super(aModelNumber, aSequenceMap, someInteractions);
		pdbFile = aPdbFile;
		pdbId = aPdbId;
		structureFactory = aStructureFactory;
	}
	
	/**
	 * Get the The PDB structure file that was extracted to generate 
	 * this structure object
	 * @return the PDB structure File
	 */
	public final File getPdbFile(){
		return pdbFile;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getPdbId(){
		return pdbId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final GeneratedStructureFactory getStructureFactory(){
		return structureFactory;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getProgramName(){
		return structureFactory.getProgramName();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getProgramVersion(){
		return structureFactory.getProgramVersion();
	}

}
