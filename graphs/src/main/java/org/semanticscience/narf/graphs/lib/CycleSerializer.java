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
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.semanticscience.narf.graphs.lib.cycles.Cycle;
import org.semanticscience.narf.graphs.lib.cycles.CycleHelper;
import org.semanticscience.narf.graphs.nucleicacid.InteractionEdge;
import org.semanticscience.narf.graphs.nucleicacid.NucleicAcid;
import org.semanticscience.narf.structures.interactions.BasePair;
import org.semanticscience.narf.structures.interactions.NucleotideInteraction;
import org.semanticscience.narf.structures.interactions.PhosphodiesterBond;
import org.semanticscience.narf.structures.parts.Nucleotide;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * Serialize the minimum cycle bases of Nucleic Acid structures
 * 
 * @author Jose Cruz-Toledo
 * 
 */
public class CycleSerializer {
	/**
	 * A map where the key is an identfier of the structure (eg: PDBID) where
	 * the cycle basis was obtained from and the value is a list of the mcb
	 * cycle fingerprints. These cycles will include glycosidic bond orientation
	 * and edge-edge interactions
	 */
	private HashMap<String, List<String>> level_2_mcb = new HashMap<String, List<String>>();
	/**
	 * A map where the key is an identfier of the structure (eg: PDBID) where
	 * the cycle basis was obtained from and the value is a list of the mcb
	 * cycle fingerprints. These cycles will **NOT** include glycosidic bond
	 * orientation and edge-edge interactions
	 */
	private HashMap<String, List<String>> level_1_mcb = new HashMap<String, List<String>>();

	/**
	 * Create a TSV representation from a list of cycles
	 * 
	 * @param aPdbId
	 *            the pdbId of the structure from where this cycle basis was
	 *            derived
	 * @param acycleList
	 *            the list of cycles computed from a pdb id
	 * @param basepaironly
	 *            a boolean flag that specifies the level of desired annotation
	 *            for the base pair class if set to true base pairs classes will
	 *            not make use neither glycosidic bond orientation nor of
	 *            edge-edge interactions
	 * @return a TSV string representation of the list of cycles
	 */
	public String createNarfTsv(String aPdbId, NucleicAcid aNucleicAcid,
			List<Cycle<Nucleotide, InteractionEdge>> aCycleList,
			String aptamerType, int structureId, boolean basepaironly) {
		String rm = "";
		// put the header in
		if (basepaironly) {
			rm += "id\tcycle_len\tstart_vertex\tend_vertex\tedge_summary\tvertex_summary\tmin_norm_no_gly_no_edges\taptamertype\tstructure_id\n";
		} else {
			rm += "pdbid\tcycle_len\tstart_vertex\tend_vertex\tedge_summary\tvertex_summary\tmin_norm\tmin_norm_no_gly_no_edges\n";
		}
		List<String> richFingerPrints = new ArrayList<String>();
		List<String> poorFingerPrints = new ArrayList<String>();
		for (Cycle<Nucleotide, InteractionEdge> cycle : aCycleList) {
			BigDecimal min_norm = null;
			// if basepaironly was set to false then compute the basepair only
			// version aswell
			BigDecimal min_norm_no_edges_no_glybond = null;
			if (basepaironly) {
				min_norm_no_edges_no_glybond = CycleHelper
						.findMinmalNormalization(aNucleicAcid, cycle,
								basepaironly);
				poorFingerPrints.add("#" + min_norm_no_edges_no_glybond);
			} else {
				min_norm_no_edges_no_glybond = CycleHelper
						.findMinmalNormalization(aNucleicAcid, cycle, true);
				poorFingerPrints.add("#" + min_norm_no_edges_no_glybond);
				min_norm = CycleHelper.findMinmalNormalization(aNucleicAcid,
						cycle, false);
				richFingerPrints.add("#" + min_norm);
			}
			int cLen = cycle.size();
			String sV = cycle.getStartVertex().getResidueIdentifier()
					+ cycle.getStartVertex().getResiduePosition();
			if (cycle.getStartVertex().getChainId() != null) {
				sV += "_" + cycle.getStartVertex().getChainId();
			}
			String eV = cycle.getEndVertex().getResidueIdentifier()
					+ cycle.getEndVertex().getResiduePosition();
			if (cycle.getEndVertex().getChainId() != null) {
				eV += "_" + cycle.getEndVertex().getChainId();
			}
			// edgeclass
			String edgeSummary = "";
			String bpSummary = "";

			List<InteractionEdge> edges = cycle.getEdgeList();
			for (InteractionEdge anEdge : edges) {

				String bpC = anEdge.extractBasePairClasses();
				if (bpC.length() > 0) {
					bpSummary += bpC + ", ";
				}
				edgeSummary += anEdge.extractEdgeClasses() + "-";
			}
			if (bpSummary.length() > 0) {
				bpSummary = bpSummary.substring(0, bpSummary.length() - 2);
			}
			edgeSummary = edgeSummary.substring(0, edgeSummary.length() - 1);
			String vertexSummary = "";
			List<Nucleotide> vertices = cycle.getVertexList();
			for (Nucleotide n : vertices) {
				vertexSummary += n.getResidueIdentifier()
						+ n.getResiduePosition() + "_" + n.getChainId() + ", ";
			}
			vertexSummary = vertexSummary.substring(0,
					vertexSummary.length() - 2);
			String data = null;
			if (basepaironly) {
				data = aPdbId + "\t" + cLen + "\t" + sV + "\t" + eV + "\t"
						+ edgeSummary + "\t" + bpSummary + "\t" + vertexSummary
						+ "\t#" + min_norm_no_edges_no_glybond;
				if (aptamerType != null) {
					data += "\t" + aptamerType;
				}
				if (structureId > 0) {
					data += "\t" + structureId;
				}
				rm += data + "\n";
			} else {
				data = aPdbId + "\t" + cLen + "\t" + sV + "\t" + eV + "\t"
						+ edgeSummary + "\t" + bpSummary + "\t" + vertexSummary
						+ "\t#" + min_norm + "\t#"
						+ min_norm_no_edges_no_glybond;
				rm += data + "\n";

			}// else

		}// for
		this.keepTrack(aPdbId, richFingerPrints, poorFingerPrints);
		return rm;
	}

	/**
	 * Keep track of the computed cycle bases by their structure id (eg. PDBID)
	 * 
	 * @param anId
	 *            eg. the pdbid
	 * @param richFps
	 *            a list of cycles found in the given structure id (with
	 *            glycosidic bond orientations and edge edge interactions)
	 * @param poorFps
	 *            a list of cycles found in the given structure id without
	 *            glycosidic bond orientation info nor edge-edge interactions
	 */
	private void keepTrack(String anId, List<String> richFps,
			List<String> poorFps) {
		this.level_2_mcb.put(anId, richFps);
		this.level_1_mcb.put(anId, poorFps);
	}

	/**
	 * Retrieve a unique set of rich cycles computed for this round
	 * 
	 * @return a unique set of rich cycles computed for this round as extracted
	 *         from this.getCompleteRichMap()
	 */
	public List<String> getUniqueCycleTypesRich() {
		List<String> rm = new ArrayList<String>();
		Map<String, List<String>> x = this.get_complete_level_2_mcb();
		for (Map.Entry<String, List<String>> entry : x.entrySet()) {
			String key = entry.getKey();
			List<String> value = entry.getValue();
			for (String aFp : value) {
				if (!rm.contains(aFp)) {
					rm.add(aFp);
				}
			}
		}
		return rm;
	}

	/**
	 * Retrieve a unique list of PDBIDS used in this round
	 * 
	 * @return a unique list of PDBIDS used in this round
	 */
	public List<String> getPDBIds() {
		List<String> rm = new ArrayList<String>();
		rm.addAll(this.level_1_mcb.keySet());
		return rm;
	}

	/**
	 * Retrieve a unique set of rich cycles computed for this round
	 * 
	 * @return a unique set of rich cycles computed for this round as extracted
	 *         from this.getCompletePoorMap()
	 */
	public List<String> getUniqueCycleTypesPoor() {
		List<String> rm = new ArrayList<String>();
		Map<String, List<String>> x = this.get_complete_level_1_mcb();
		for (Map.Entry<String, List<String>> entry : x.entrySet()) {
			String key = entry.getKey();
			List<String> value = entry.getValue();
			for (String aFp : value) {
				if (!rm.contains(aFp)) {
					rm.add(aFp);
				}
			}
		}
		return rm;
	}

	/**
	 * Creates a RDF representation of a list of cycles
	 * 
	 * @param aPdbId
	 *            the pdbId of the structure from where this cycle basis was
	 *            derived
	 * @param acycleList
	 *            the list of cycles computed from a pdb id
	 * @return a jena model that completely describes these cycles
	 * @throws IOException
	 */
	// TODO: deal with provenance of program used
	public Model createNarfModel(String aPdbId, NucleicAcid aNucleicAcid,
			List<Cycle<Nucleotide, InteractionEdge>> acycleList)
			throws IOException {
		Model rm = ModelFactory.createDefaultModel();
		for (Cycle<Nucleotide, InteractionEdge> acyc : acycleList) {
			Random rp = new Random();
			int randp = rp.nextInt() + 1;
			// create a cycle resource
			Resource cycleRes = rm.createResource(Vocab.narf_resource
					+ CycleSerializer.MD5("cycle" + randp));
			// type it as a cycle
			cycleRes.addProperty(Vocab.rdftype, Vocab.narf_cycle);
			// add a label
			String lbl = "Cycle found in PDBID: " + aPdbId + " of size: "
					+ acyc.size();
			cycleRes.addProperty(Vocab.rdfslabel, lbl);
			// add the size attribute
			Resource sizeRes = rm.createResource(Vocab.narf_resource
					+ CycleSerializer.MD5("size" + randp));
			// type it as a size
			sizeRes.addProperty(Vocab.rdftype, Vocab.narf_cycle_size);
			// add the value
			sizeRes.addLiteral(Vocab.hasValue, (double) acyc.size());
			// connect the sizeRes to the cycleRes
			cycleRes.addProperty(Vocab.hasAttribute, sizeRes);
			// add the normalized string attribute
			Resource norm_str = rm.createResource(Vocab.narf_resource
					+ CycleSerializer.MD5("norm_string" + randp));
			// type it
			norm_str.addProperty(Vocab.rdftype, Vocab.narf_normalized_string);
			// add the value
			norm_str.addLiteral(
					Vocab.hasValue,
					"#"
							+ CycleHelper.findMinmalNormalization(aNucleicAcid,
									acyc, false).toString());
			// connect norm_str back to the cycleRes
			cycleRes.addProperty(Vocab.hasAttribute, norm_str);
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
								+ CycleSerializer.MD5(fN.toString()
										+ sN.toString() + rand));
						// create a resource from the rnaoclass
						String rnaoClassStr = ((BasePair) ni).inferRnaOClass();
						Resource rnaoClass = rm.createResource(rnaoClassStr);
						if (rnaoClass != null) {
							// type it using the rnaoClass resource
							bpRes.addProperty(Vocab.rdftype, rnaoClass);
						}
						// base pair has part residues
						bpRes.addProperty(Vocab.hasPart, firstNucRes);
						bpRes.addProperty(Vocab.hasPart, secondNucRes);
						// add the paried with property between the residues
						firstNucRes
								.addProperty(Vocab.paired_with, secondNucRes);
						// add the base pair label
						bpRes.addLiteral(Vocab.rdfslabel,
								((BasePair) ni).toString());

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
								+ CycleSerializer.MD5("phdb" + rand));
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
						// add the phosphodiester bond label
						phdb.addLiteral(Vocab.rdfslabel,
								((PhosphodiesterBond) ni).toString());
					}
				}
			}

		}

		return rm;
	}

	private static String MD5(String md5) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
						.substring(1, 3));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
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
		public static Resource narf_cycle_size = m
				.createResource(narf_vocabulary + "cycle_size");
		public static Resource narf_normalized_string = m
				.createResource(narf_vocabulary + "cycle_normalized_string");

	}

	/**
	 * A map where the key is an identfier of the structure where the cycle
	 * basis was obtained from and the value is a list of the counts of mcbs
	 * found for that structure. The length of the list will be equal to the
	 * total number of cycles extracted in this execution of the program. These
	 * cycles will include glycosidic bond orientation and edge-edge
	 * interactions
	 * 
	 * @return the complete_mcb_map_rich
	 */
	public HashMap<String, List<String>> get_complete_level_2_mcb() {
		return level_2_mcb;
	}

	/**
	 * A map where the key is an identfier of the structure where the cycle
	 * basis was obtained from and the value is a list of the counts of mcbs
	 * found for that structure. The length of the list will be equal to the
	 * total number of cycles extracted in this execution of the program. These
	 * cycles will include glycosidic bond orientation and edge-edge
	 * interactions
	 * 
	 * @param complete_mcb_map_rich
	 *            the complete_mcb_map_rich to set
	 */
	private void set_complete_level_2_mcb(
			HashMap<String, List<String>> x) {
		this.level_2_mcb = x;
	}

	/**
	 * @return the complete_mcb_map_poor
	 */
	public HashMap<String, List<String>> get_complete_level_1_mcb() {
		return level_1_mcb;
	}

	/**
	 * @param complete_mcb_map_poor
	 *            the complete_mcb_map_poor to set
	 */
	private void set_complete_level_1_mcb(
			HashMap<String, List<String>> y) {
		this.level_1_mcb = y;
	}
}
