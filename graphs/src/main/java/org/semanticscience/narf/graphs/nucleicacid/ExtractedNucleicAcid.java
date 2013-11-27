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

package org.semanticscience.narf.graphs.nucleicacid;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.semanticscience.narf.structures.factories.tertiary.ExtractedTertiaryStructureFactory;
import org.semanticscience.narf.structures.factories.tertiary.Mcannotate;
import org.semanticscience.narf.structures.factories.tertiary.Rnaview;
import org.semanticscience.narf.structures.factories.tertiary.X3DnaDssr;
import org.semanticscience.narf.structures.interactions.BasePair;
import org.semanticscience.narf.structures.lib.exceptions.InvalidResidueException;
import org.semanticscience.narf.structures.tertiary.ExtractedTertiaryStructure;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

/**
 * An extension of the nucleic acid graph class to incorporate information
 * unique to annotated nucleic acids.
 * 
 * 
 * @author William Greenwood
 * @author Jose Cruz-Toledo
 * @since 1.6
 */
public class ExtractedNucleicAcid extends NucleicAcid implements
		GeneratedNucleicAcid {

	private static final long serialVersionUID = -1796881104967863346L;

	/**
	 * The name of the program that annotated the nucleic acid.
	 */
	private final String programName;
	/**
	 * The version of the program that annotated the nucleic acid.
	 */
	private final String programVersion;

	/**
	 * Four letter identifier of a PDB structure file
	 */
	private final String pdbId;

	/**
	 * The PDB structure file that was extracted to generate this structure
	 * object
	 */
	private final File pdbFile;

	/**
	 * Construct a nucleic acid from the sequence extracted from the PDB file
	 * and the set of interactions annotated by a structure annotator.
	 * 
	 * @param aProgramName
	 *            the name of the program that annotated the nucleic acid
	 * @param aProgramVersion
	 *            the version of the program that annotated the nucleic acid
	 * @param aModelNumber
	 *            the model number of the conformer
	 * @param aStructure
	 *            the tertiary structure produced by the structure annotator
	 *            factory
	 * @throws InvalidPhosphodiesterBondException
	 * @since 1.6
	 */
	private ExtractedNucleicAcid(String aProgramName, String aProgramVersion,
			File aPdbFile, int aModelNumber,
			ExtractedTertiaryStructure aStructure) {
		super(aModelNumber, aStructure.getSequenceMap(), aStructure
				.getInteractions());

		programName = aProgramName;
		programVersion = aProgramVersion;
		pdbId = aStructure.getPdbId();
		pdbFile = aPdbFile;
	}

	/**
	 * Execute a given structure annotator and construct each nucleic acid
	 * structure conformer using the tertiary structure produced by the
	 * structure annotator factory.
	 * 
	 * @param aPdbFile
	 *            the PDB structure file
	 * @param anExtractedTertiaryStructureFactory
	 *            the factory that produces the annotated nucleic acid structure
	 * @return a set of annotated nucleic acids conformers
	 * @throws FileNotFoundException
	 *             if the PDB file does not exist
	 * @throws IOException
	 *             if a I/O problem arose while the reading the PDB or annotated
	 *             nucleic acid output file
	 * @throws InvalidResidueException
	 *             if an invalid residue is created
	 */
	private static Set<NucleicAcid> annotateStructures(
			File aPdbFile,
			ExtractedTertiaryStructureFactory anExtractedTertiaryStructureFactory)
			throws FileNotFoundException, IOException, InvalidResidueException {

		int modelNumber = 1;

		Set<NucleicAcid> nucleicAcids = new LinkedHashSet<NucleicAcid>();
		for (ExtractedTertiaryStructure aStructure : anExtractedTertiaryStructureFactory
				.getStructures(aPdbFile)) {
			nucleicAcids.add(new ExtractedNucleicAcid(
					anExtractedTertiaryStructureFactory.getProgramName(),
					anExtractedTertiaryStructureFactory.getProgramVersion(),
					aPdbFile, modelNumber++, aStructure));

		}
		return nucleicAcids;
	}

	/**
	 * A factory method that executes the MC-Annotate structure annotator on a
	 * PDB structure file and creates a set of nucleic acid conformers from the
	 * information produced by MC-Annotate.
	 * 
	 * @param aPdbFile
	 *            the PDB structure file
	 * @return a set of annotated nucleic acids conformers one per model
	 * @throws FileNotFoundException
	 *             if the PDB file does not exist
	 * @throws IOException
	 *             if a I/O problem arose while the reading the PDB or annotated
	 *             nucleic acid output file
	 * @throws InvalidResidueException
	 *             if an invalid residue was created
	 * @since 1.6
	 */
	public static Set<NucleicAcid> mcannotate(File aPdbFile)
			throws FileNotFoundException, IOException, InvalidResidueException {

		ExtractedTertiaryStructureFactory mcannotate = new Mcannotate();

		return annotateStructures(aPdbFile, mcannotate);
	}

	/**
	 * A factory method that executes the MC-Annotate structure annotator on a
	 * PDB structure file and creates a nucleic acid for the desired model
	 * number conformers from the information produced by MC-Annotate.
	 * 
	 * @param aPdbFile
	 *            the PDB strucutre file
	 * @param modelNumber
	 *            the desired model number from the structure model
	 * @return the nucleic acid object for the specified modelnumber
	 * @throws FileNotFoundException
	 *             if the PDB file does not exist
	 * @throws IOException
	 *             if a I/O problem arose while the reading the PDB or annotated
	 *             nucleic acid output file
	 * @throws InvalidResidueException
	 *             if an invalid residue was created
	 */
	public static NucleicAcid mcannotate(File aPdbFile, int modelNumber)
			throws FileNotFoundException, IOException, InvalidResidueException {
		ExtractedTertiaryStructureFactory mca = new Mcannotate();
		Set<NucleicAcid> nas = annotateStructures(aPdbFile, mca);

		for (NucleicAcid na : nas) {

			if (na.getModelNumber() == modelNumber) {
				return na;
			}
		}
		return null;

	}

	/**
	 * A factory method that executes the RNAView structure annotator on a PDB
	 * structure file and creates a set of nucleic acid conformers from the
	 * information produced by RNAView.
	 * 
	 * @param aPdbFile
	 *            the PDB structure file
	 * @return a set of annotated nucleic acids conformers
	 * @throws FileNotFoundException
	 *             if the PDB file does not exist
	 * @throws IOException
	 *             if a I/O problem arose while the reading the PDB or annotated
	 *             nucleic acid output file
	 * @throws InvalidResidueException
	 *             if an invalid residue was created
	 * @since 1.6
	 */
	public static Set<NucleicAcid> rnaview(File aPdbFile)
			throws FileNotFoundException, IOException, InvalidResidueException {
		ExtractedTertiaryStructureFactory rnaview = new Rnaview();
		return annotateStructures(aPdbFile, rnaview);
	}

	/**
	 * A factory method that executes the 3DNA-DSSR structure annotator on a PDB
	 * structure file and creates a set of nucleic acid conformers from the
	 * information produced by this program.
	 * 
	 * @param aPdbFile
	 *            the PDB structure file
	 * @return a set of annotated nucleic acids conformers
	 * @throws InvalidResidueException
	 *             if an invalid residue is found
	 * @throws IOException
	 *             if there is a problem with the pdb file or the annotator
	 * @throws FileNotFoundException
	 *             if there is a problem with the pdb file
	 */
	public static Set<NucleicAcid> x3dnaDssr(File aPdbFile)
			throws FileNotFoundException, IOException {
		try{
		ExtractedTertiaryStructureFactory x3dnaDssr = new X3DnaDssr();
		return annotateStructures(aPdbFile, x3dnaDssr);
		}catch(InvalidResidueException e ){
			System.out.println("Check File : "+ aPdbFile);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getProgramName() {
		return programName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getProgramVersion() {
		return programVersion;
	}

	/**
	 * Get the four letter identifier of a PDB structure for this nucleic acid
	 * 
	 * @return the PDB identifier
	 */
	public final String getPdbId() {
		return pdbId;
	}

	/**
	 * Get the The PDB structure file that was extracted to generate this
	 * structure object
	 * 
	 * @return the PDB structure File
	 */
	public final File getPdbFile() {
		return pdbFile;
	}

	private Model getCollectionOfBasePairs() {
		Model returnMe = ModelFactory.createDefaultModel();
		// get the set of BasePairs
		Set<BasePair> bps = getBasePairs();
		Iterator<BasePair> itr = bps.iterator();
		while (itr.hasNext()) {
			BasePair abp = itr.next();
		}

		return returnMe;
	}

	/*
	 * public Model getModel() { Model returnMe =
	 * ModelFactory.createDefaultModel();
	 * 
	 * // create a structure model Resource structureModel = returnMe
	 * .createResource("http://bio2rdf.org/pdb:" + this.getPdbId().toUpperCase()
	 * + "/model_" + this.getModelNumber());
	 * structureModel.addProperty(Vocabulary.rdftype,
	 * Vocabulary.STRUCTURE_MODEL);
	 * 
	 * // create an annotated stucture model Resource annotatedStucModel =
	 * returnMe.createResource(RdfUtils .createUniqueURI());
	 * annotatedStucModel.addProperty(Vocabulary.rdftype,
	 * Vocabulary.ANNOTATED_STRUCTURE_MODEL);
	 * 
	 * // connect to the structure model
	 * annotatedStucModel.addProperty(Vocabulary.isAbout, structureModel);
	 * 
	 * // connect to program used Resource threeDAnn = returnMe
	 * .createResource(RdfUtils.createUniqueURI());
	 * threeDAnn.addProperty(Vocabulary.rdftype, Vocabulary.ThreeDAnnotator);
	 * 
	 * // add the version to the program (?)
	 * threeDAnn.addProperty(Vocabulary.has_version, this.getProgramVersion());
	 * threeDAnn.addProperty(Vocabulary.has_name, this.getProgramName());
	 * 
	 * structureModel.addProperty(Vocabulary.annotatedBy, threeDAnn);
	 * 
	 * return returnMe; }
	 */

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getUniqueIdentifier() {
		return this.getProgramName() + ":" + this.getPdbId() + "_M_"
				+ this.getModelNumber();
	}

	public String toString() {
		String returnMe = "";
		returnMe += "== Extracted Nucleic Acid == ";
		returnMe += "\n Program used: " + getProgramName();
		returnMe += "\n Program version: " + getProgramVersion();
		returnMe += "\n PDBID : " + getPdbId();
		return returnMe + "\n" + super.toString();
	}

	@SuppressWarnings("unused")
	public static final class Vocabulary {
		private static Model m_model = ModelFactory.createDefaultModel();
		// rdf type
		public static final Property rdftype = m_model
				.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
		// is about
		public static final Property isAbout = m_model
				.createProperty("http://semanticscience.org/is_about");
		// has version
		public static final Property has_version = m_model
				.createProperty("http://semanticscience.org/has_version");
		// has name
		public static final Property has_name = m_model
				.createProperty("http://semanticscience.org/has_name");
		// program used
		public static final Property annotatedBy = m_model
				.createProperty("http://semanticscience.org/program_used");

		// base pair
		public static final Resource BASE_PAIR = m_model
				.createResource("http://semanticscience.org/1234");
		// base stack
		public static final Resource BASE_STACK = m_model
				.createResource("http://semanticscience.org/12345");
		// Phosphodiester linkage
		public static final Resource PHOSPHODIESTER_LINKAGE = m_model
				.createResource("http://semanticscience.org/123456");
		// backbone
		public static final Resource BACKBONE = m_model
				.createResource("http://semanticscience.org/1234235");
		// structure model
		public static final Resource STRUCTURE_MODEL = m_model
				.createResource("http://semanticscience.org/123432425");
		// annotated structure model
		public static final Resource ANNOTATED_STRUCTURE_MODEL = m_model
				.createResource("http://semanticscience.org/1234safdsad5");
		// collection of base pairs
		public static final Resource COLLECTION_OF_BASEPAIRS = m_model
				.createResource("http://semanticscience.org/asdfasdf");
		// collection of stacks
		public static final Resource COLLECTION_OF_STACKS = m_model
				.createResource("http://semanticscience.org/12345");
		// 3d annotator
		public static final Resource ThreeDAnnotator = m_model
				.createResource("http://semanticscience.org/MCA");

		// Adenine

		// Guanine

		// Cytosine
		// Thymine
		// Uracil

	}
}
