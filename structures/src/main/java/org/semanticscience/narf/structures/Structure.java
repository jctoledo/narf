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

package org.semanticscience.narf.structures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticscience.narf.structures.interactions.NucleotideInteraction;
import org.semanticscience.narf.structures.parts.Sequence;

import com.hp.hpl.jena.rdf.model.Model;


/**
 * A skeletal class for nucleic acid structures.
 * @author Jose Cruz-Toledo
 * @author William Greenwood
 * @since 1.6
 */

public abstract class Structure {

	/**
	 * Map of the chain identifiers of the nucleic acid to the sequence of that chain
	 */
	private Map<String, Sequence> sequenceMap;
	
	/**
	 * The set of interactions present in the nucleic acid structure
	 */
	private Set<NucleotideInteraction> interactions;
	
	/**
	 * The model number of this nucleic acid structure conformation
	 */
	private int modelNumber;
	
	/**
	 * A Jena Model representation of this structure
	 */
	protected Model model;
	
	/**
	 * Construct a nucleic acid structure from a map of sequences and a set of interactions.
	 * @param aModelNumber model number of the nucleic acid structure conformation
	 * @param aSequenceMap map of the chain identifier of the nucleic acid to the sequence of the chain
	 * @param someInteractions set of interactions present in the nucleic acid structure
	 */
	public Structure(int aModelNumber, Map<String, Sequence> aSequenceMap, Set<NucleotideInteraction> someInteractions){
		sequenceMap = aSequenceMap;
		interactions = someInteractions;
		modelNumber = aModelNumber;
	}
	
	/**
	 * Get the chain identifiers of all the chains in this nucleic acid structure
	 * @return a set of the chain identifiers
	 */
	public Set<String> getChainIdentifiers(){
		return sequenceMap.keySet();
	}
	
	/**
	 * Determine if a given chain exists in this structure
	 * @param aChain chain identifier
	 * @return <code>true</code> if this chain is present in this structure, otherwise <code>false</code>
	 */
	public boolean containsChain(String aChain){
		return sequenceMap.containsKey(aChain);
	}
	
	/**
	 * Get the sequence of a given chain.
	 * @param aChain chain identifier
	 * @return a sequence
	 */
	public Sequence getSequenceByChain(String aChain){
		return sequenceMap.get(aChain);
	}
	
	/**
	 * Get a list of all chain Sequences 
	 * @retun a list of all chain sequences
	 * 
	 */
	public List<Sequence> getAllChainSequences(){
		List<Sequence> rm = new ArrayList<Sequence>();
		for (Map.Entry<String, Sequence> entry : sequenceMap.entrySet()) {
			rm.add(entry.getValue());
		}
		return rm;
	}
	
	/**
	 * Get the map of the chain identifiers of the nucleic acid to the sequence of that chain
	 * @return the map of chain identifier to sequence
	 */
	public Map<String, Sequence> getSequenceMap(){
		return Collections.unmodifiableMap(sequenceMap);
	}
	
	/**
	 * Get the set of interactions present in the nucleic acid structure.
	 * @return a set of interactions
	 */
	public Set<NucleotideInteraction> getInteractions(){
		return interactions;
	}
	
	/**
	 * Get the model number of this nucleic acid structure conformation.
	 * @return the model number
	 */
	public int getModelNumber(){
		return modelNumber;
	}
	
	/**
	 * Get a Jena Model representation of this structure
	 * @return a Jena Model representation of this structure
	 */
	public Model getModel(){
		return model;
	}
	
}
