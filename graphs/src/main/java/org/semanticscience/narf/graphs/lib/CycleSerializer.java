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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import com.hp.hpl.jena.ontology.ObjectProperty;
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
	 * The name of the program used to make the nucleic acid
	 */
	private String program_name = null;
	/**
	 * The version of the program used to make this nucleic acid
	 */
	private String program_version = null;

	/**
	 * @param pn the program name
	 * @param nv the version
	 */
	public CycleSerializer(String pn, String vn) {
		this.program_name = pn;
		
		this.program_version = vn;
	}

	/**
	 * Creates a RDF representation of a list of cycles
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
	 * @return a jena model that completely describes these cycles
	 * @throws IOException
	 */
	public Model createNarfModel(String aPdbId, NucleicAcid aNucleicAcid,
			List<Cycle<Nucleotide, InteractionEdge>> acycleList, boolean basepaironly)
			throws IOException {
		Random r_i = new Random();
		int rand_i = r_i.nextInt()+1;
		Model rm = ModelFactory.createDefaultModel();
		//add mcb computation resource
		Resource mcb_computation = rm.createResource(
				Vocab.narf_resource + CycleSerializer.MD5("mcb_computation"+rand_i));
		//type it
		mcb_computation.addProperty(Vocab.rdftype, Vocab.narf_mcb_computation);
		rm.add(Vocab.narf_mcb_computation, Vocab.rdftype, Vocab.rdfs_class);
		mcb_computation.addProperty(Vocab.rdftype, Vocab.named_individual);
		//add a software resource
		Resource sw_res = rm.createResource(Vocab.narf_resource+CycleSerializer.MD5("software"+rand_i));
		sw_res.addProperty(Vocab.rdftype, Vocab.narf_software);
		rm.add(Vocab.narf_software, Vocab.rdftype, Vocab.rdfs_class);
		sw_res.addProperty(Vocab.rdftype, Vocab.named_individual);		
		//add a mcb resource
		Resource mcb_res = rm.createResource(Vocab.narf_resource+CycleSerializer.MD5("mcb"+rand_i));
		mcb_res.addProperty(Vocab.rdftype, Vocab.narf_mcb);
		rm.add(Vocab.narf_mcb, Vocab.rdftype, Vocab.rdfs_class);
		mcb_res.addProperty(Vocab.rdftype, Vocab.named_individual);	
		mcb_res.addLiteral(Vocab.rdfslabel, "Minimum Cycle Basis");
		//connect them back to the mcb_computation
		mcb_computation.addProperty(Vocab.has_attribute, sw_res);
		mcb_computation.addProperty(Vocab.has_attribute, mcb_res);
		
		//create a resource for the pdb_structure
		Resource pdb_struct_resource = rm.createResource("http://bio2rdf.org/pdb:"+aPdbId.toUpperCase());
		mcb_res.addProperty(Vocab.derived_from, pdb_struct_resource);		
		
		for (Cycle<Nucleotide, InteractionEdge> acyc : acycleList) {
			Random rp = new Random();
			int randp = rp.nextInt() + 1;
			// create a cycle resource
			Resource cycleRes = rm.createResource(Vocab.narf_resource
					+ CycleSerializer.MD5(acyc.toString()+aPdbId));
			// type it as a cycle
			cycleRes.addProperty(Vocab.rdftype, Vocab.narf_cycle);
			rm.add(Vocab.narf_cycle, Vocab.rdftype, Vocab.rdfs_class);
			cycleRes.addProperty(Vocab.rdftype, Vocab.named_individual);
			mcb_res.addProperty(Vocab.has_member, cycleRes);
			
			// add a label
			String lbl = "Cycle found in PDBID: " + aPdbId + " of size: "
					+ acyc.size();
			cycleRes.addLiteral(Vocab.rdfslabel, lbl);
			
			//add 1st degree neighbour cycle set
			//all the cycles that share one or more vertices with acyc
			List<Cycle<Nucleotide, InteractionEdge>> firstDegNeigh = aNucleicAcid.findMCBNeighbours(acyc);
			if(firstDegNeigh.size()>0){
				String r = "r"+randp;
				Resource firstDegCNS = rm.createResource(Vocab.narf_resource+CycleSerializer.MD5(r));
				String lb = "First degree cycle neighbourset for :"+acyc.toString();
				//add a label
				firstDegCNS.addLiteral(Vocab.rdfslabel, lb);
				//type it as a 1st degree neighbour cycle set
				firstDegCNS.addProperty(Vocab.rdftype, Vocab.narf_firstDegreeCycleNeighbourSet);
				rm.add(Vocab.narf_firstDegreeCycleNeighbourSet, Vocab.rdftype, Vocab.rdfs_class);
				firstDegCNS.addProperty(Vocab.rdftype, Vocab.named_individual);
				cycleRes.addProperty(Vocab.has_attribute, firstDegCNS);
				//now iterate over the neighbours
				for (Cycle<Nucleotide, InteractionEdge> an : firstDegNeigh) {
					//create a resource for each
					Resource an_res = rm.createResource(Vocab.narf_resource+CycleSerializer.MD5(an.toString()+aPdbId));
					//attach them to firstDegCNS using hasmember
					firstDegCNS.addProperty(Vocab.has_member, an_res);
				}
			}
			
			// add the size attribute
			Resource sizeRes = rm.createResource(Vocab.narf_resource
					+ CycleSerializer.MD5("size" + randp));
			// type it as a size
			sizeRes.addProperty(Vocab.rdftype, Vocab.narf_cycle_size);
			rm.add(Vocab.narf_cycle_size, Vocab.rdftype, Vocab.rdfs_class);
			sizeRes.addProperty(Vocab.rdftype, Vocab.named_individual);
			//add a label
			String l = "Cycle size for :"+acyc.toString();
			sizeRes.addLiteral(Vocab.rdfslabel, l);
			// add the value
			sizeRes.addLiteral(Vocab.has_value, (int) acyc.size());
			// connect the sizeRes to the cycleRes
			cycleRes.addProperty(Vocab.has_attribute, sizeRes);
			
			//create a gccontent res System.out.println(CycleHelper.computeCycleGCContent(acyc));
			Resource gcCont = rm.createResource(Vocab.narf_resource+CycleSerializer.MD5("gcContent"+randp+acyc.hashCode()));
			gcCont.addProperty(Vocab.rdftype, Vocab.narf_gc_content);
			rm.add(Vocab.narf_gc_content, Vocab.rdftype, Vocab.rdfs_class);
			//add a label
			String l2 = "Cycle gc content for :"+acyc.toString();
			gcCont.addLiteral(Vocab.rdfslabel, l);
			gcCont.addProperty(Vocab.rdftype, Vocab.named_individual);
			double gc_cont =  CycleHelper.computeCycleGCContent(acyc);
			gcCont.addLiteral(Vocab.has_value,gc_cont);
			cycleRes.addProperty(Vocab.has_attribute, gcCont);
			
			if(basepaironly){
				Resource lvl_1 = rm.createResource(Vocab.narf_resource+CycleSerializer.MD5("lvl_1"+randp));
				lvl_1.addProperty(Vocab.rdftype, Vocab.narf_cycle_profile_level_1);
				rm.add(Vocab.narf_cycle_profile_level_1, Vocab.rdftype, Vocab.rdfs_class);
				lvl_1.addProperty(Vocab.rdftype, Vocab.named_individual);
				//add a label
				String l = "Cycle profile level 1 for :"+acyc.toString();
				lvl_1.addLiteral(Vocab.rdfslabel, l);
				//get the level 1 normalized version of this string
				String lvl_1_str = CycleHelper.findMinmalNormalization(aNucleicAcid, acyc, true).toString();
				lvl_1.addLiteral(Vocab.has_value,"#"+lvl_1_str);
				lvl_1.addLiteral(Vocab.hasMD5,CycleSerializer.MD5(lvl_1_str));
				cycleRes.addProperty(Vocab.has_attribute, lvl_1);
			}else{
				
				Resource lvl_1 = rm.createResource(Vocab.narf_resource+CycleSerializer.MD5("lvl_1"+randp));
				lvl_1.addProperty(Vocab.rdftype, Vocab.narf_cycle_profile_level_1);
				rm.add(Vocab.narf_cycle_profile_level_1, Vocab.rdftype, Vocab.rdfs_class);
				lvl_1.addProperty(Vocab.rdftype, Vocab.named_individual);
				//add a label
				String l = "Cycle profile level 2 for :"+acyc.toString();
				lvl_1.addLiteral(Vocab.rdfslabel, l);
				//get the level 1 normalized version of this string
				String lvl_1_str = CycleHelper.findMinmalNormalization(aNucleicAcid, acyc, true).toString();
				lvl_1.addLiteral(Vocab.has_value,"#"+lvl_1_str);
				lvl_1.addLiteral(Vocab.hasMD5,CycleSerializer.MD5(lvl_1_str));
				cycleRes.addProperty(Vocab.has_attribute, lvl_1);
				
				Resource lvl_2 = rm.createResource(Vocab.narf_resource
						+ CycleSerializer.MD5("norm_string_lvl_2" + randp));
				lvl_2.addProperty(Vocab.rdftype, Vocab.narf_cycle_profile_level_2);
				rm.add(Vocab.narf_cycle_profile_level_2, Vocab.rdftype, Vocab.rdfs_class);
				lvl_2.addProperty(Vocab.rdftype, Vocab.named_individual);
				String n_str_lvl_2 = CycleHelper.findMinmalNormalization(aNucleicAcid,
						acyc, false).toString();
				lvl_2.addLiteral(Vocab.has_value, "#" + n_str_lvl_2);
				lvl_2.addLiteral(Vocab.hasMD5, CycleSerializer.MD5(n_str_lvl_2));
				cycleRes.addProperty(Vocab.has_attribute, lvl_2);
			}
			
			
			// get the interaction edges
			List<InteractionEdge> edges = acyc.getEdgeList();
			for (InteractionEdge anEdge : edges) {
				Set<NucleotideInteraction> interactions = anEdge
						.getInteractions();
				Random ra = new Random();
				int rand = ra.nextInt() + 1;
				for (NucleotideInteraction ni : interactions) {
					if (ni instanceof BasePair) {
						
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
								Vocab.pdb_residue);
						rm.add(Vocab.pdb_residue, Vocab.rdftype, Vocab.rdfs_class);
						firstNucRes.addProperty(Vocab.rdftype, Vocab.named_individual);
						secondNucRes.addProperty(Vocab.rdftype,
								Vocab.pdb_residue);
						secondNucRes.addProperty(Vocab.rdftype, Vocab.named_individual);
						// add these nucleotide resources as members of the
						// cycle
						cycleRes.addProperty(Vocab.has_part, firstNucRes);
						cycleRes.addProperty(Vocab.has_part, secondNucRes);
						// create a base pair resource
						Resource bpRes = rm.createResource(Vocab.narf_resource
								+ CycleSerializer.MD5(fN.toString()
										+ sN.toString()));
						// create a resource from the rnaoclass
						String rnaoClassStr = ((BasePair) ni).inferRnaOClass();
						Resource rnaoClass = rm.createResource(rnaoClassStr);
						if (rnaoClass != null) {
							// type it using the rnaoClass resource
							bpRes.addProperty(Vocab.rdftype, rnaoClass);
							rm.add(rnaoClass, Vocab.rdftype, Vocab.rdfs_class);
							bpRes.addProperty(Vocab.rdftype, Vocab.named_individual);
						}else{
							bpRes.addProperty(Vocab.rdftype, rm.createResource("http://purl.obolibrary.org/obo/RNAO_0000001"));
							bpRes.addProperty(Vocab.rdftype, Vocab.named_individual);
							rm.add(rm.createResource("http://purl.obolibrary.org/obo/RNAO_0000001"),Vocab.rdftype, Vocab.rdfs_class);
						}
						// base pair has part residues
						bpRes.addProperty(Vocab.has_part, firstNucRes);
						bpRes.addProperty(Vocab.has_part, secondNucRes);
						// add the paried with property between the residues
						firstNucRes
								.addProperty(Vocab.paired_with, secondNucRes);
						// add the base pair label
						bpRes.addLiteral(Vocab.rdfslabel,
								((BasePair) ni).toString());
						cycleRes.addProperty(Vocab.has_attribute, bpRes);

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
								Vocab.pdb_residue);
						firstNucRes.addProperty(Vocab.rdftype, Vocab.named_individual);
						secondNucRes.addProperty(Vocab.rdftype,
								Vocab.pdb_residue);
						secondNucRes.addProperty(Vocab.rdftype, Vocab.named_individual);
						// add these nucleotide resources as members of the
						// cycle
						cycleRes.addProperty(Vocab.has_part, firstNucRes);
						cycleRes.addProperty(Vocab.has_part, secondNucRes);
						// create a phosphodiesterbond resource
						Resource phdbRes = rm.createResource(Vocab.narf_resource
								+ CycleSerializer.MD5("phdb" + rand));
						// type it as a narf phdb
						phdbRes.addProperty(Vocab.rdftype,
								Vocab.narf_phosphodiester_bond);
						rm.add(rm.createResource(Vocab.narf_phosphodiester_bond), Vocab.rdftype, Vocab.rdfs_class);
						phdbRes.addProperty(Vocab.rdftype, Vocab.named_individual);
						// phosphodiester bond has part residuies
						phdbRes.addProperty(Vocab.has_part, firstNucRes);
						phdbRes.addProperty(Vocab.has_part, secondNucRes);
						// add the covalently connected to property between the
						// residues
						firstNucRes.addProperty(Vocab.covalenty_connected_to,
								secondNucRes);
						// add the phosphodiester bond label
						phdbRes.addLiteral(Vocab.rdfslabel,
								((PhosphodiesterBond) ni).toString());
						cycleRes.addProperty(Vocab.has_attribute, phdbRes);
					}
				}
			}
		}
		return rm;
	}

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
		List<String> level_2 = new ArrayList<String>();
		// no edge-edge interaction information and no glycosidic bond
		// orientation info
		List<String> level_1 = new ArrayList<String>();
		for (Cycle<Nucleotide, InteractionEdge> cycle : aCycleList) {
			BigDecimal min_norm = null;
			// if basepaironly was set to false then compute the basepair only
			// version aswell
			BigDecimal min_norm_no_edges_no_glybond = null;
			if (basepaironly) {
				min_norm_no_edges_no_glybond = CycleHelper
						.findMinmalNormalization(aNucleicAcid, cycle,
								basepaironly);
				level_1.add("#" + min_norm_no_edges_no_glybond);
			} else {
				min_norm_no_edges_no_glybond = CycleHelper
						.findMinmalNormalization(aNucleicAcid, cycle, true);
				level_1.add("#" + min_norm_no_edges_no_glybond);
				min_norm = CycleHelper.findMinmalNormalization(aNucleicAcid,
						cycle, false);
				level_2.add("#" + min_norm);
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
		this.keepTrack(aPdbId, level_2, level_1);
		return rm;
	}

	/**
	 * Keep track of the computed cycle bases by their structure id (eg. PDBID)
	 * 
	 * @param anId
	 *            eg. the pdbid
	 * @param level_2_serialization
	 *            a list of cycles found in the given structure id (with
	 *            glycosidic bond orientations and edge edge interactions)
	 * @param level_1_serialization
	 *            a list of cycles found in the given structure id without
	 *            glycosidic bond orientation info nor edge-edge interactions
	 */
	private void keepTrack(String anId, List<String> level_2_serialization,
			List<String> level_1_serialization) {
		this.level_2_mcb.put(anId, level_2_serialization);
		this.level_1_mcb.put(anId, level_1_serialization);
	}

	/**
	 * Retrieve a unique set of rich cycles computed for this round
	 * 
	 * @return a unique set of rich cycles computed for this round as extracted
	 *         from this.getCompleteRichMap()
	 */
	public List<String> getUniqueLevel2() {
		List<String> rm = new ArrayList<String>();
		Map<String, List<String>> x = this.get_complete_level_2_mcb();
		for (Map.Entry<String, List<String>> entry : x.entrySet()) {
			List<String> value = entry.getValue();
			for (String aFp : value) {
				if (!rm.contains(aFp)) {
					rm.add(aFp);
				}
			}
		}
		return rm;
	}

	public String makeSummary() {
		List<String> x = this.getPDBIds();
		List<String> y = this.getUniquelevel1();
		List<String> z = this.getUniqueLevel2();

		Date myDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss");
		String date = sdf.format(myDate);
		String rm_c = "Minimum cycle basis extracted from : "
				+ this.getPDBIds().size() + " structures computed on:" + date
				+ "\n";
		rm_c += "PDBIDs used: ";
		for (String a : x) {
			rm_c += a + ",";
		}
		rm_c = rm_c.substring(0, rm_c.length() - 1);
		rm_c += "\nLevel 1 Unique MCBs :\n";
		for (String b : y) {
			rm_c += b + ",";
		}
		rm_c = rm_c.substring(0, rm_c.length() - 1);
		rm_c += "\n\n";
		if (z != null && z.size() > 0) {
			rm_c += "\nLevel 2 Unique MCBs :\n";
			for (String c : z) {
				rm_c += c + ",";
			}
			rm_c = rm_c.substring(0, rm_c.length() - 1);
		}
		rm_c += "\n\n";
		rm_c += "pdbid\tlevel_1_cycles\n";
		for (Map.Entry<String, List<String>> w : this
				.get_complete_level_1_mcb().entrySet()) {
			String anId = w.getKey();
			List<String> mcb = w.getValue();
			for (String amcb : mcb) {
				rm_c += anId + "\t" + amcb + "\n";
			}
		}
		rm_c += "\n\n";
		if (z != null && z.size() > 0) {
			rm_c += "pdbid\tlevel_2_cycles\n";
			for (Map.Entry<String, List<String>> w : this
					.get_complete_level_2_mcb().entrySet()) {
				String anId = w.getKey();
				List<String> mcb = w.getValue();
				for (String amcb : mcb) {
					rm_c += anId + "\t" + amcb + "\n";
				}
			}
		}
		return rm_c;
	}

	/**
	 * retrieve the total size of the mcb level 2. This is the total count of
	 * all minimum cycles found in every structure in this run
	 * 
	 * @return the total size of the mcb level 2. This is the total count of all
	 *         minimum cycles found in every structure in this run
	 */
	public int get_level_2_basis_size() {
		int rm = 0;
		Map<String, List<String>> y = this.get_complete_level_2_mcb();
		for (Map.Entry<String, List<String>> entry : y.entrySet()) {
			List<String> v = entry.getValue();
			rm += v.size();
		}
		return rm;
	}

	/**
	 * retrieve the total size of the mcb level 1. This is the total count of
	 * all minimum cycles found in every structure in this run
	 * 
	 * @return the total size of the mcb level 1. This is the total count of all
	 *         minimum cycles found in every structure in this run
	 */
	public int get_level_1_basis_size() {
		int rm = 0;
		Map<String, List<String>> y = this.get_complete_level_1_mcb();
		for (Map.Entry<String, List<String>> entry : y.entrySet()) {
			List<String> v = entry.getValue();
			rm += v.size();
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
	public List<String> getUniquelevel1() {
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
	public Map<String, List<String>> get_complete_level_2_mcb() {
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
	private void set_complete_level_2_mcb(HashMap<String, List<String>> x) {
		this.level_2_mcb = x;
	}

	/**
	 * @return the complete_mcb_map_poor
	 */
	public Map<String, List<String>> get_complete_level_1_mcb() {
		return level_1_mcb;
	}

	/**
	 * @param complete_mcb_map_poor
	 *            the complete_mcb_map_poor to set
	 */
	private void set_complete_level_1_mcb(HashMap<String, List<String>> y) {
		this.level_1_mcb = y;
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
		private static final String owl = "http://www.w3.org/2002/07/owl#";

		private static Model m = ModelFactory.createDefaultModel();
		//properties
		public static Property rdftype = m.createProperty(rdf + "type");
		public static Property rdfslabel = m.createProperty(rdfs + "label");
		public static Property has_member = m.createProperty(narf_vocabulary
				+ "has_member");
		public static Property has_part = m.createProperty(narf_vocabulary
				+ "has_part");
		public static Property has_attribute = m.createProperty(narf_vocabulary
				+ "has_attribute");
		public static Property has_value = m.createProperty(narf_vocabulary
				+ "has_value");
		public static Property hasMD5 = m.createProperty(narf_vocabulary
				+ "has_md5hash");
		public static Property paired_with = m.createProperty(narf_vocabulary
				+ "paired_with");
		public static Property stacked_with = m.createProperty(narf_vocabulary
				+ "stacked_with");
		public static Property covalenty_connected_to = m
				.createProperty(narf_vocabulary + "covalently_connected_to");
		public static Property has_version = m
				.createProperty(narf_vocabulary + "has_version");
		public static Property derived_from = m
				.createProperty(narf_vocabulary + "derived_from");
		public static Property has_name = m
				.createProperty(narf_vocabulary + "has_name");
		
		
		public static Resource narf_firstDegreeCycleNeighbourSet = m.createResource(narf_vocabulary+"first_degree_cycle_neighbour_set");
		public static Resource narf_cycle = m.createResource(narf_vocabulary
				+ "cycle");
		public static Resource narf_gc_content = m.createResource(narf_vocabulary+"gc_content");
		public static Resource narf_mcb_computation = m.createResource(narf_vocabulary+"mcb_computation");
		public static Resource narf_mcb = m.createResource(narf_vocabulary+"mcb");
		public static Resource narf_software = m.createResource(narf_vocabulary+"software");
		public static Resource narf_phosphodiester_bond = m
				.createResource(narf_vocabulary + "phosphodiester_bond");
		public static Resource narf_cycle_size = m
				.createResource(narf_vocabulary + "cycle_size");
		public static Resource narf_normalized_string = m
				.createResource(narf_vocabulary + "cycle_normalized_string");
		public static Resource narf_cycle_profile_level_1 = m
				.createResource(narf_vocabulary + "cycle_profile_level_1");
		public static Resource narf_cycle_profile_level_2 = m
				.createResource(narf_vocabulary + "cycle_profile_level_2");
		public static Resource pdb_residue = m.createResource(pdb_resource + "Residue");
		public static Resource rdfs_class = m.createResource(rdfs+"Class");
		public static Resource named_individual = m.createResource(owl+"NamedIndividual");
	}

	/**
	 * @return the program_name
	 */
	public String getProgram_name() {
		return program_name;
	}

	/**
	 * @return the program_version
	 */
	public String getProgram_version() {
		return program_version;
	}
}
