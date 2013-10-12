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
package org.semanticscience.narf.graphs.lib;

import java.util.List;
import java.util.Set;

import org.semanticscience.narf.graphs.lib.cycles.Cycle;
import org.semanticscience.narf.graphs.nucleicacid.InteractionEdge;
import org.semanticscience.narf.structures.interactions.BasePair;
import org.semanticscience.narf.structures.interactions.NucleotideInteraction;
import org.semanticscience.narf.structures.interactions.PhosphodiesterBond;
import org.semanticscience.narf.structures.parts.Nucleotide;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * @author  Jose Cruz-Toledo
 *
 */
public class RDFUtil {
	
	/**
	 * Creates a narf 
	 * @param aPdbId the pdbId of the structure from where this cycle basis was derived
	 * @param acycleList the list of cycles computed from a pdb id
	 * @return a jena model that completely describes these cycles
	 */
	public static Model createNarfModel(String aPdbId, List<Cycle<Nucleotide, InteractionEdge>> acycleList){
		Model rm = ModelFactory.createDefaultModel();
		int count = 1;
		for(Cycle<Nucleotide, InteractionEdge> acyc: acycleList){
			//get the vertices
			List<Nucleotide> verts = acyc.getVertexList();
			for(Nucleotide aNuc : verts){
				//create a bio2rdf resource for each nucleotide
				Resource aNucRes = rm.createResource(Vocab.pdb_resource+aPdbId+"/chemicalComponent_"+aNuc.getChainId()+aNuc.getResiduePosition());
				//type it
				aNucRes.addProperty(Vocab.rdftype, Vocab.pdb_resource+"Residue");
			}
			//get the interaction edges
			List<InteractionEdge> edges = acyc.getEdgeList();
			for (InteractionEdge anEdge: edges){
				Set<NucleotideInteraction> interactions = anEdge.getInteractions();
				for (NucleotideInteraction ni:interactions){
					if(ni instanceof BasePair){
						System.out.println(anEdge.getInteractions());
					}
					if(ni instanceof PhosphodiesterBond){
						//blah
					}
				}
				
			}
			count ++;
			
		}
		return rm;
	}
	
	@SuppressWarnings("unused")
	private static final class Vocab{
		private static final String narf_base = "http://bio2rdf.org/narf";
		private static final String pdb_base = "http://bio2rdf.org/pdb";
		private static final String narf_vocabulary = narf_base+"_vocabulary:";
		private static final String pdb_vocabulary = pdb_base+"_vocabulary:";
		private static final String pdb_resource = pdb_base+"_resource:";
		private static final String narf_resource = narf_base+"_resource:";
		private static final String rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
		private static final String rdfs = "http://www.w3.org/2000/01/rdf-schema#";
		
		
		private static Model m = ModelFactory.createDefaultModel();
		public static Property rdftype = m.createProperty(rdf+"type");
		public static Property rdfslabel = m.createProperty(rdfs+"label");
		public static Property hasMember = m.createProperty(narf_vocabulary+"has_member");
		public static Property hasPart = m.createProperty(narf_vocabulary+"has_part");
		public static Property hasAttribute = m.createProperty(narf_vocabulary+"has_attribute");
		public static Property hasValue = m.createProperty(narf_vocabulary+"has_value");		
		public static Property paired_with = m.createProperty(narf_vocabulary+"paired_with");
		public static Property stacked_with = m.createProperty(narf_vocabulary+"stacked_with");
		public static Property covalenty_connected_to = m.createProperty(narf_vocabulary+"covalently_connected_to");
		public static Resource cycle = m.createResource(narf_vocabulary+"cycle");
		public static Resource size = m.createResource(narf_vocabulary+"size");
		
	}
}
