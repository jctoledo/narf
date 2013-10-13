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

import java.io.IOException;
import java.util.List;
import java.util.Random;
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
 * @author Jose Cruz-Toledo
 * 
 */
public class RDFUtil {

	/**
	 * Creates a narf
	 * 
	 * @param aPdbId
	 *            the pdbId of the structure from where this cycle basis was
	 *            derived
	 * @param acycleList
	 *            the list of cycles computed from a pdb id
	 * @return a jena model that completely describes these cycles
	 * @throws IOException
	 */
	public static Model createNarfModel(String aPdbId,
			List<Cycle<Nucleotide, InteractionEdge>> acycleList)
			throws IOException {
		Model rm = ModelFactory.createDefaultModel();
		for (Cycle<Nucleotide, InteractionEdge> acyc : acycleList) {
			Random rp = new Random();
			int randp = rp.nextInt() + 1;
			// create a cycle resource
			Resource cycleRes = rm.createResource(Vocab.narf_resource
					+ RDFUtil.MD5("cycle" + randp));
			// type it as a cycle
			cycleRes.addProperty(Vocab.rdftype, Vocab.narf_cycle);
			// add a label
			String lbl = "Cycle found in PDBID: " + aPdbId + " of size: "
					+ acyc.size();
			cycleRes.addProperty(Vocab.rdfslabel, lbl);
			// add the size attribute
			Resource sizeRes = rm.createResource(Vocab.narf_resource
					+ RDFUtil.MD5("size" + randp));
			// type it as a size
			sizeRes.addProperty(Vocab.rdftype, Vocab.narf_cycle_size);
			// add the value
			sizeRes.addLiteral(Vocab.hasValue, (double) acyc.size());
			// connect the sizeRes to the cycleRes
			cycleRes.addProperty(Vocab.hasAttribute, sizeRes);
			// get the interaction edges
			List<InteractionEdge> edges = acyc.getEdgeList();
			for (InteractionEdge anEdge : edges) {
				Set<NucleotideInteraction> interactions = anEdge
						.getInteractions();
				for (NucleotideInteraction ni : interactions) {
					if (ni instanceof BasePair) {
						Random r = new Random();
						int rand = r.nextInt() + 1;
						// get the first nucleotide
						Nucleotide fN = ((BasePair) ni).getFirstNucleotide();
						Nucleotide sN = ((BasePair) ni).getSecondNucleotide();
						// create a bio2rdf resource for each nucleotide
						Resource firstNucRes = rm
								.createResource(Vocab.pdb_resource + aPdbId
										+ "/chemicalComponent_"
										+ fN.getChainId()
										+ fN.getResiduePosition());
						Resource secondNucRes = rm
								.createResource(Vocab.pdb_resource + aPdbId
										+ "/chemicalComponent_"
										+ sN.getChainId()
										+ sN.getResiduePosition());
						// type them
						firstNucRes.addProperty(Vocab.rdftype,
								Vocab.pdb_resource + "Residue");
						secondNucRes.addProperty(Vocab.rdftype,
								Vocab.pdb_resource + "Residue");
						// add these nucleotide resources as members of the
						// cycle
						cycleRes.addProperty(Vocab.hasMember, firstNucRes);
						cycleRes.addProperty(Vocab.hasMember, secondNucRes);
						// create a base pair resource
						Resource bpRes = rm.createResource(Vocab.narf_resource
								+ RDFUtil.MD5(fN.toString() + sN.toString()
										+ rand));
						// create a resource from the rnaoclass
						Resource rnaoClass = rm.createResource(((BasePair) ni)
								.inferRnaOClass());
						// type it using the rnaoClass resource
						bpRes.addProperty(Vocab.rdftype, rnaoClass);
						// base pair has part residues
						bpRes.addProperty(Vocab.hasPart, firstNucRes);
						bpRes.addProperty(Vocab.hasPart, secondNucRes);
						// add the paried with property between the residues
						firstNucRes
								.addProperty(Vocab.paired_with, secondNucRes);
						//add the base pair label
						bpRes.addLiteral(Vocab.rdfslabel, ((BasePair)ni).toString());
						
					} else if (ni instanceof PhosphodiesterBond) {
						// get the first nucleotide
						Nucleotide fN = ((PhosphodiesterBond) ni)
								.getFirstNucleotide();
						Nucleotide sN = ((PhosphodiesterBond) ni)
								.getSecondNucleotide();
						// create a bio2rdf resource for each nucleotide
						Resource firstNucRes = rm
								.createResource(Vocab.pdb_resource + aPdbId
										+ "/chemicalComponent_"
										+ fN.getChainId()
										+ fN.getResiduePosition());
						Resource secondNucRes = rm
								.createResource(Vocab.pdb_resource + aPdbId
										+ "/chemicalComponent_"
										+ sN.getChainId()
										+ sN.getResiduePosition());
						// type them
						firstNucRes.addProperty(Vocab.rdftype,
								Vocab.pdb_resource + "Residue");
						secondNucRes.addProperty(Vocab.rdftype,
								Vocab.pdb_resource + "Residue");
						Random r = new Random();
						int rand = r.nextInt() + 1;
						// add these nucleotide resources as members of the
						// cycle
						cycleRes.addProperty(Vocab.hasMember, firstNucRes);
						cycleRes.addProperty(Vocab.hasMember, secondNucRes);
						// create a phosphodiesterbond resource
						Resource phdb = rm.createResource(Vocab.narf_resource
								+ RDFUtil.MD5("phdb" + rand));
						// type it as a narf phdb
						phdb.addProperty(Vocab.rdftype,
								Vocab.narf_phosphodiester_bond);
						// phosphodiester bond has part residuies
						phdb.addProperty(Vocab.hasPart, firstNucRes);
						phdb.addProperty(Vocab.hasPart, secondNucRes);
						// add the covalently connected to property between the
						// residues
						firstNucRes.addProperty(Vocab.covalenty_connected_to,
								secondNucRes);
						//add the phosphodiester bond label
						phdb.addLiteral(Vocab.rdfslabel,((PhosphodiesterBond) ni).toString());
					}
				}
			}

		}

		return rm;
	}

	private static String MD5(String md5) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
						.substring(1, 3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
		}
		return null;
	}

	@SuppressWarnings("unused")
	private static final class Vocab {
		private static final String narf_base = "http://bio2rdf.org/narf";
		private static final String pdb_base = "http://bio2rdf.org/pdb";
		private static final String narf_vocabulary = narf_base
				+ "_vocabulary:";
		private static final String pdb_vocabulary = pdb_base + "_vocabulary:";
		private static final String pdb_resource = pdb_base + "_resource:";
		private static final String narf_resource = narf_base + "_resource:";
		private static final String rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
		private static final String rdfs = "http://www.w3.org/2000/01/rdf-schema#";

		private static Model m = ModelFactory.createDefaultModel();
		public static Property rdftype = m.createProperty(rdf + "type");
		public static Property rdfslabel = m.createProperty(rdfs + "label");
		public static Property hasMember = m.createProperty(narf_vocabulary
				+ "has_member");
		public static Property hasPart = m.createProperty(narf_vocabulary
				+ "has_part");
		public static Property hasAttribute = m.createProperty(narf_vocabulary
				+ "has_attribute");
		public static Property hasValue = m.createProperty(narf_vocabulary
				+ "has_value");
		public static Property paired_with = m.createProperty(narf_vocabulary
				+ "paired_with");
		public static Property stacked_with = m.createProperty(narf_vocabulary
				+ "stacked_with");
		public static Property covalenty_connected_to = m
				.createProperty(narf_vocabulary + "covalently_connected_to");
		public static Resource narf_cycle = m.createResource(narf_vocabulary
				+ "cycle");
		public static Resource narf_phosphodiester_bond = m
				.createResource(narf_vocabulary + "phosphodiester_bond");
		public static Resource narf_cycle_size = m.createResource(narf_vocabulary
				+ "cycle_size");

	}
}
