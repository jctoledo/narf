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

import java.util.List;
import java.util.Set;

import org.semanticscience.narf.graphs.lib.cycles.Cycle;
import org.semanticscience.narf.structures.interactions.BasePair;
import org.semanticscience.narf.structures.interactions.NucleotideInteraction;
import org.semanticscience.narf.structures.parts.Nucleotide;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;

/**
 * @author  Jose Cruz-Toledo
 *
 */
public class RDFUtil {
	private static String base = "http://bio2rdf.org/narf:";
	private static String resource = base.substring(0, base.length()-1)+"_resource:";
	private static String vocabulary = base.substring(0, base.length()-1)+"_vocabulary:";
	
	public static Model createModel(List<Cycle<Nucleotide, InteractionEdge>> acycleList){
		Model rm = ModelFactory.createDefaultModel();
		for(Cycle<Nucleotide, InteractionEdge> acyc: acycleList){
			Nucleotide sv = acyc.getStartVertex();
			InteractionEdge ie = acyc.getIncomingEdge(sv);
			
		}
		return null;
	}
	
	private static final class Vocab{
		private static Model m = ModelFactory.createDefaultModel();
		public static Property rdftype = m.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
		
	}
	
}
