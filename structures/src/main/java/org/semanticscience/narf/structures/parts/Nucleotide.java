/**
 * Copyright (c) 2011 Jose Miguel Cruz Toledo and William Greenwood
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
package org.semanticscience.narf.structures.parts;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.semanticscience.narf.structures.lib.exceptions.InvalidResidueException;

/**
 * Representation of a nucleotide residue component of a nucleic acid.
 * 
 * @author Jose Cruz-Toledo
 * @author William Greenwood
 * @version %I%, %G%
 * @since 1.6
 */
public class Nucleotide extends Residue {
	/**
	 * A mapping between the residue identifier and a unique integer
	 */
	private Map<String, Integer> normalizedNucleotideMapping;
	/**
	 * the Pdb id where tho
	 */
	private String pdbId;
	/**
	 * the chain identifier for this nucleotide
	 */
	private String chainId;
	/**
	 * Glycosidic bond rotation (anti or syn).
	 */
	private final String nucleotideConformation;

	/**
	 * Residue position in the chain of the nucleic acid.
	 */
	private final int residuePosition;
	/**
	 * Puckering atom of this nucleotide residue
	 */
	private final String puckerAtom;
	/**
	 * Quality of puckering atom (endo|exo)
	 */
	private final String puckerQuality;

	/**
	 * Construct a nucleotide from only its residue position and residue label.
	 * 
	 * @param aResiduePosition
	 *            the position in chain
	 * @param aResidueIdentifier
	 *            the three letter PDB chemical identifier for the nucleotide or
	 *            a single letter code
	 * @throws InvalidResidueException
	 */
	public Nucleotide(int aResiduePosition, String aResidueIdentifier)
			throws InvalidResidueException {
		this(aResiduePosition, aResidueIdentifier, null);
	}

	/**
	 * Construct a nucleotide from only its residue position, molecule label and
	 * conformation label.
	 * 
	 * @param aResiduePosition
	 *            the position on the chain
	 * @param aResidueIdentifier
	 *            the three letter PDB chemical identifier for the nucleotide
	 * @param aConformationLabel
	 *            the conformation of the nucleotide (anti | syn)
	 * @throws InvalidResidueException
	 */
	public Nucleotide(int aResiduePosition, String aResidueIdentifier,
			String aConformationLabel) throws InvalidResidueException {
		this(aResiduePosition, aResidueIdentifier, aConformationLabel, null,
				null);
	}

	/**
	 * Construct a nucleotide from only its residue position, molecule label,
	 * conformation label, pucker atom and pucker atom quality.
	 * 
	 * @param residuePosition
	 *            the position on the chain
	 * @param aResidueIdentifier
	 *            the three letter PDB chemical identifier for the nucleotide
	 * @param aConformationLabel
	 *            the conformation of the nucleotide (anti | syn)
	 * @param aPuckerAtom
	 *            the atom that is puckered
	 * @param aPuckerQuality
	 *            the quality of the puckering
	 * @throws InvalidResidueException
	 */
	public Nucleotide(int aResiduePosition, String aResidueIdentifier,
			String aConformationLabel, String aPuckerAtom, String aPuckerQuality)
			throws InvalidResidueException {
		super(aResiduePosition, aResidueIdentifier);
		residuePosition = aResiduePosition;
		nucleotideConformation = aConformationLabel;
		puckerAtom = aPuckerAtom;
		puckerQuality = aPuckerQuality;
		normalizedNucleotideMapping = this.makeNormalizedNucleotideMap();
	}

	/**
	 * Construct a nucleotide from its pdbid, chainid, residue position,
	 * molecule label, conformation label, pucker atom and pucker atom quality.
	 * 
	 * @param aPdbId
	 *            the pdbid corresponding to this nucleotide
	 * @param aChainId
	 *            the chain identifier
	 * @param aResiduePosition
	 *            the position on the chain
	 * @param aResidueIdentifier
	 *            the three letter PDB chemical identifier for the nucleotide
	 * @param aConformationLabel
	 *            the conformation of the nucleotide (anti | syn)
	 * @param aPuckerAtom
	 *            the atom that is puckered
	 * @param aPuckerQuality
	 *            the quality of the puckering
	 * @throws InvalidResidueException
	 */
	public Nucleotide(String aPdbId, String aChainId, int aResiduePosition,
			String aResidueIdentifier, String aConformationLabel,
			String aPuckerAtom, String aPuckerQuality)
			throws InvalidResidueException {
		this(aResiduePosition, aResidueIdentifier, aConformationLabel,
				aPuckerAtom, aPuckerQuality);
		pdbId = aPdbId;
		chainId = aChainId;
		normalizedNucleotideMapping = this.makeNormalizedNucleotideMap();

	}

	/**
	 * Determine if the nucleotide contains information relating to nucleotide
	 * conformation.
	 * 
	 * @return <code>true</code> if the nucleotide has nucleotide conformation,
	 *         otherwise <code>false</code>
	 * @since 1.6
	 */
	public boolean hasNucleotideConformation() {
		return (nucleotideConformation != null);
	}

	/**
	 * Determine if the nucleotide contains information relating to the pucker
	 * atom.
	 * 
	 * @return <code>true</code> if the nucleotide has a pucker atom, otherwise
	 *         <code>false</code>
	 * @since 1.6
	 */
	public boolean hasPuckerAtom() {
		return (puckerAtom != null);
	}

	/**
	 * Determine if the nucleotide contains information relating to pucker
	 * quality.
	 * 
	 * @return <code>true</code> if the nucleotide has a pucker quality,
	 *         otherwise <code>false</code>
	 * @since 1.6
	 */
	public boolean hasPuckerQuality() {
		return (puckerQuality != null);
	}

	/**
	 * Get the glycosidic bond roation of the nucleotide
	 * 
	 * @return the string representation of the glycosidic bond rotation
	 * @since 1.6
	 */
	public String getNucleotideConformation() {
		if (!this.hasNucleotideConformation()) {
			throw new UnsupportedOperationException(
					"No nucleotide conformation is present in this nucleotide.");
		}

		return nucleotideConformation;
	}

	/**
	 * Get the residue position of the nucleotide
	 * 
	 * @return the residue position integer
	 * @since 1.6
	 */
	public int getResiduePosition() {
		return residuePosition;
	}

	/**
	 * Get the pucker atom of the nucleotide
	 * 
	 * @return the string representation of the pucker atom
	 * @since 1.6
	 */
	public String getPuckerAtom() {
		if (!this.hasPuckerAtom()) {
			throw new UnsupportedOperationException(
					"No pucker atom is present in this nucleotide.");
		}

		return puckerAtom;
	}

	/**
	 * Get the pucker quality of the nucleotide
	 * 
	 * @return the string representation of the pucker quality
	 * @since 1.6
	 */
	public String getPuckerQuality() {
		if (!this.hasPuckerQuality()) {
			throw new UnsupportedOperationException(
					"No pucker quality is present in this nucleotide.");
		}

		return puckerQuality;
	}

	public String getChainId() {
		return chainId;
	}

	/**
	 * REtrieve an integer representation of this nucleotide. The returned
	 * integer corresponds only to the nucleobase and not to its position on the
	 * sequence
	 * 
	 * @return an integer representation for a given nucleotide
	 */
	public Integer getNormalizedNucleotide() {
		Map<String, Integer> m = this.getNormalizedNucleotideMapping();
		String res_id = this.getResidueIdentifier();
		return m.get(res_id);
	}

	/**
	 * Make a map where the key is a residue identifier and the value is a
	 * unique integer
	 * 
	 * @return mapping between a residue identifier and a unique integer
	 */
	private Map<String, Integer> makeNormalizedNucleotideMap() {
		Map<String, Integer> rm = new HashMap<String, Integer>();
		rm.put("0C", 21);
		rm.put("0DX", 22);
		rm.put("0G", 23);
		rm.put("0KZ", 24);
		rm.put("0TN", 25);
		rm.put("0U", 26);
		rm.put("104", 27);
		rm.put("125", 28);
		rm.put("126", 29);
		rm.put("127", 30);
		rm.put("12A", 31);
		rm.put("13D", 32);
		rm.put("18Y", 33);
		rm.put("1AP", 34);
		rm.put("1GL", 35);
		rm.put("1MA", 36);
		rm.put("1MG", 37);
		rm.put("1P1", 38);
		rm.put("1P2", 39);
		rm.put("1PE", 40);
		rm.put("1SC", 41);
		rm.put("1TL", 42);
		rm.put("1TW", 43);
		rm.put("218", 44);
		rm.put("232", 45);
		rm.put("2AD", 46);
		rm.put("2AT", 47);
		rm.put("2AU", 48);
		rm.put("2BP", 49);
		rm.put("2BT", 50);
		rm.put("2GF", 51);
		rm.put("2GT", 52);
		rm.put("2HP", 53);
		rm.put("2MG", 54);
		rm.put("2MU", 55);
		rm.put("2NT", 56);
		rm.put("2OT", 57);
		rm.put("2ST", 58);
		rm.put("3AD", 59);
		rm.put("3AW", 60);
		rm.put("3AY", 61);
		rm.put("3CO", 62);
		rm.put("3DA", 63);
		rm.put("3ME", 64);
		rm.put("40A", 65);
		rm.put("40C", 66);
		rm.put("40G", 67);
		rm.put("40T", 68);
		rm.put("42B", 69);
		rm.put("44D", 70);
		rm.put("4SU", 71);
		rm.put("5AZ", 72);
		rm.put("5BD", 73);
		rm.put("5BU", 74);
		rm.put("5CF", 75);
		rm.put("5CM", 76);
		rm.put("5FC", 77);
		rm.put("5GP", 78);
		rm.put("5HC", 79);
		rm.put("5IC", 80);
		rm.put("5IU", 81);
		rm.put("5MC", 82);
		rm.put("5MU", 83);
		rm.put("5OC", 84);
		rm.put("5PM", 85);
		rm.put("5SE", 86);
		rm.put("6AP", 87);
		rm.put("6GO", 88);
		rm.put("6GU", 89);
		rm.put("6HA", 90);
		rm.put("6HC", 91);
		rm.put("6HG", 92);
		rm.put("6HS", 93);
		rm.put("6HT", 94);
		rm.put("6MA", 95);
		rm.put("6OG", 96);
		rm.put("70U", 97);
		rm.put("7AD", 98);
		rm.put("7DA", 99);
		rm.put("7GU", 100);
		rm.put("7MG", 101);
		rm.put("804", 102);
		rm.put("881", 103);
		rm.put("8AD", 104);
		rm.put("8OG", 105);
		rm.put("9AD", 106);
		rm.put("9TA", 107);
		rm.put("A", 108);
		rm.put("A23", 109);
		rm.put("A2F", 110);
		rm.put("A2M", 111);
		rm.put("A38", 112);
		rm.put("A40", 113);
		rm.put("A43", 114);
		rm.put("A44", 115);
		rm.put("A47", 116);
		rm.put("A4C", 117);
		rm.put("A4L", 118);
		rm.put("A5M", 119);
		rm.put("A66", 120);
		rm.put("A6A", 121);
		rm.put("A6C", 122);
		rm.put("A6G", 123);
		rm.put("A6U", 124);
		rm.put("A71", 125);
		rm.put("A72", 126);
		rm.put("AB6", 127);
		rm.put("AB9", 128);
		rm.put("ACA", 129);
		rm.put("ACT", 130);
		rm.put("ADE", 131);
		rm.put("AF2", 132);
		rm.put("AGD", 133);
		rm.put("AKN", 134);
		rm.put("ALA", 135);
		rm.put("AM2", 136);
		rm.put("AN9", 137);
		rm.put("ANT", 138);
		rm.put("APN", 139);
		rm.put("AQS", 140);
		rm.put("ARG", 141);
		rm.put("ARI", 142);
		rm.put("AS", 143);
		rm.put("ASN", 144);
		rm.put("ASP", 145);
		rm.put("AU3", 146);
		rm.put("B12", 147);
		rm.put("B1Z", 148);
		rm.put("B7C", 149);
		rm.put("B9A", 150);
		rm.put("BA", 151);
		rm.put("BBZ", 152);
		rm.put("BDA", 153);
		rm.put("BDC", 154);
		rm.put("BDG", 155);
		rm.put("BER", 156);
		rm.put("BFA", 157);
		rm.put("BFT", 158);
		rm.put("BGC", 159);
		rm.put("BGF", 160);
		rm.put("BGM", 161);
		rm.put("BMO", 162);
		rm.put("BOE", 163);
		rm.put("BPF", 164);
		rm.put("BR", 165);
		rm.put("BRA", 166);
		rm.put("BRN", 167);
		rm.put("BRU", 168);
		rm.put("BTN", 169);
		rm.put("BZG", 170);
		rm.put("C", 171);
		rm.put("C2E", 172);
		rm.put("C34", 173);
		rm.put("C38", 174);
		rm.put("C42", 175);
		rm.put("C43", 176);
		rm.put("C45", 177);
		rm.put("C46", 178);
		rm.put("C66", 179);
		rm.put("C6G", 180);
		rm.put("C7P", 181);
		rm.put("CA", 182);
		rm.put("CAC", 183);
		rm.put("CBR", 184);
		rm.put("CBV", 185);
		rm.put("CCC", 186);
		rm.put("CD", 187);
		rm.put("CDR", 188);
		rm.put("CFZ", 189);
		rm.put("CGQ", 190);
		rm.put("CL", 191);
		rm.put("CMD", 192);
		rm.put("CMY", 193);
		rm.put("CNC", 194);
		rm.put("CNY", 195);
		rm.put("CO", 196);
		rm.put("CP1", 197);
		rm.put("CPH", 198);
		rm.put("CPN", 199);
		rm.put("CPT", 200);
		rm.put("CS", 201);
		rm.put("CSL", 202);
		rm.put("CSM", 203);
		rm.put("CTI", 204);
		rm.put("CU", 205);
		rm.put("CUD", 206);
		rm.put("CUF", 207);
		rm.put("CUL", 208);
		rm.put("CYS", 209);
		rm.put("D18", 210);
		rm.put("D19", 211);
		rm.put("D1B", 212);
		rm.put("D24", 213);
		rm.put("D2A", 214);
		rm.put("D2X", 215);
		rm.put("D34", 216);
		rm.put("D35", 217);
		rm.put("D3N", 218);
		rm.put("D63", 219);
		rm.put("D83", 220);
		rm.put("D98", 221);
		rm.put("DA", 222);
		rm.put("DA5", 223);
		rm.put("DA6", 224);
		rm.put("DA7", 225);
		rm.put("DAI", 226);
		rm.put("DAP", 227);
		rm.put("DAX", 228);
		rm.put("DB9", 229);
		rm.put("DBN", 230);
		rm.put("DC", 231);
		rm.put("DCM", 232);
		rm.put("DCZ", 233);
		rm.put("DG", 234);
		rm.put("DGP", 235);
		rm.put("DI", 236);
		rm.put("DIM", 237);
		rm.put("DIT", 238);
		rm.put("DLY", 239);
		rm.put("DM1", 240);
		rm.put("DM2", 241);
		rm.put("DM3", 242);
		rm.put("DM4", 243);
		rm.put("DM5", 244);
		rm.put("DM6", 245);
		rm.put("DM7", 246);
		rm.put("DM8", 247);
		rm.put("DM9", 248);
		rm.put("DMM", 249);
		rm.put("DMS", 250);
		rm.put("DMY", 251);
		rm.put("DNH", 252);
		rm.put("DOD", 253);
		rm.put("DPY", 254);
		rm.put("DR1", 255);
		rm.put("DRC", 256);
		rm.put("DRP", 257);
		rm.put("DT", 258);
		rm.put("DU", 259);
		rm.put("DX4", 260);
		rm.put("E96", 261);
		rm.put("E97", 262);
		rm.put("EDA", 263);
		rm.put("EDC", 264);
		rm.put("EDO", 265);
		rm.put("EEM", 266);
		rm.put("EHN", 267);
		rm.put("EIT", 268);
		rm.put("EL", 269);
		rm.put("EOH", 270);
		rm.put("EPE", 271);
		rm.put("ERI", 272);
		rm.put("ERN", 273);
		rm.put("F", 274);
		rm.put("F3H", 275);
		rm.put("F4H", 276);
		rm.put("F5H", 277);
		rm.put("F6H", 278);
		rm.put("FA2", 279);
		rm.put("FE2", 280);
		rm.put("FFD", 281);
		rm.put("FFO", 282);
		rm.put("FLD", 283);
		rm.put("FMN", 284);
		rm.put("FOZ", 285);
		rm.put("G", 286);
		rm.put("G31", 287);
		rm.put("G36", 288);
		rm.put("G38", 289);
		rm.put("G46", 290);
		rm.put("G47", 291);
		rm.put("G48", 292);
		rm.put("G49", 293);
		rm.put("G6P", 294);
		rm.put("GCK", 295);
		rm.put("GCU", 296);
		rm.put("GDP", 297);
		rm.put("GET", 298);
		rm.put("GF2", 299);
		rm.put("GGT", 300);
		rm.put("GLC", 301);
		rm.put("GLN", 302);
		rm.put("GLP", 303);
		rm.put("GLU", 304);
		rm.put("GLY", 305);
		rm.put("GMP", 306);
		rm.put("GMS", 307);
		rm.put("GMU", 308);
		rm.put("GNG", 309);
		rm.put("GOL", 310);
		rm.put("GPN", 311);
		rm.put("GRB", 312);
		rm.put("GTP", 313);
		rm.put("GUN", 314);
		rm.put("H2U", 315);
		rm.put("HG", 316);
		rm.put("HGL", 317);
		rm.put("HIS", 318);
		rm.put("HOH", 319);
		rm.put("HP1", 320);
		rm.put("HP2", 321);
		rm.put("HPA", 322);
		rm.put("HRG", 323);
		rm.put("HT", 324);
		rm.put("HT1", 325);
		rm.put("HT2", 326);
		rm.put("I", 327);
		rm.put("I2A", 328);
		rm.put("IA", 329);
		rm.put("IB", 330);
		rm.put("IBB", 331);
		rm.put("IEL", 332);
		rm.put("IGU", 333);
		rm.put("IIP", 334);
		rm.put("ILE", 335);
		rm.put("ILT", 336);
		rm.put("IOD", 337);
		rm.put("IPA", 338);
		rm.put("IPN", 339);
		rm.put("IPY", 340);
		rm.put("IR", 341);
		rm.put("IR3", 342);
		rm.put("IRI", 343);
		rm.put("IU", 344);
		rm.put("JOS", 345);
		rm.put("JS4", 346);
		rm.put("JS5", 347);
		rm.put("JS6", 348);
		rm.put("K", 349);
		rm.put("KAN", 350);
		rm.put("KDO", 351);
		rm.put("KPT", 352);
		rm.put("LC2", 353);
		rm.put("LCG", 354);
		rm.put("LEU", 355);
		rm.put("LHA", 356);
		rm.put("LHC", 357);
		rm.put("LI", 358);
		rm.put("LIV", 359);
		rm.put("LLL", 360);
		rm.put("LPT", 361);
		rm.put("LU", 362);
		rm.put("LYS", 363);
		rm.put("M1B", 364);
		rm.put("M2G", 365);
		rm.put("M5M", 366);
		rm.put("MAR", 367);
		rm.put("MBB", 368);
		rm.put("MBC", 369);
		rm.put("MCY", 370);
		rm.put("MES", 371);
		rm.put("MET", 372);
		rm.put("MG", 373);
		rm.put("MMP", 374);
		rm.put("MN", 375);
		rm.put("MOE", 376);
		rm.put("MPD", 377);
		rm.put("MT9", 378);
		rm.put("MTR", 379);
		rm.put("MTU", 380);
		rm.put("N", 381);
		rm.put("N30", 382);
		rm.put("N33", 383);
		rm.put("N3D", 384);
		rm.put("N6G", 385);
		rm.put("NA", 386);
		rm.put("NAG", 387);
		rm.put("NC5", 388);
		rm.put("NCE", 389);
		rm.put("NCI", 390);
		rm.put("NCJ", 391);
		rm.put("NCK", 392);
		rm.put("NCL", 393);
		rm.put("NCO", 394);
		rm.put("NCQ", 395);
		rm.put("NF2", 396);
		rm.put("NGM", 397);
		rm.put("NGU", 398);
		rm.put("NH2", 399);
		rm.put("NH4", 400);
		rm.put("NI", 401);
		rm.put("NII", 402);
		rm.put("NME", 403);
		rm.put("NMS", 404);
		rm.put("NMT", 405);
		rm.put("NMY", 406);
		rm.put("NOD", 407);
		rm.put("NPM", 408);
		rm.put("NRU", 409);
		rm.put("NT", 410);
		rm.put("NUF", 411);
		rm.put("NYM", 412);
		rm.put("O", 413);
		rm.put("OHX", 414);
		rm.put("OLZ", 415);
		rm.put("OMC", 416);
		rm.put("OMG", 417);
		rm.put("OMU", 418);
		rm.put("OPN", 419);
		rm.put("OS", 420);
		rm.put("P1P", 421);
		rm.put("P24", 422);
		rm.put("P2T", 423);
		rm.put("P5P", 424);
		rm.put("PA2", 425);
		rm.put("PAR", 426);
		rm.put("PAW", 427);
		rm.put("PB", 428);
		rm.put("PCU", 429);
		rm.put("PDI", 430);
		rm.put("PET", 431);
		rm.put("PG4", 432);
		rm.put("PGP", 433);
		rm.put("PHE", 434);
		rm.put("PNI", 435);
		rm.put("PNT", 436);
		rm.put("PO2", 437);
		rm.put("PO4", 438);
		rm.put("POH", 439);
		rm.put("PPU", 440);
		rm.put("PQ0", 441);
		rm.put("PRF", 442);
		rm.put("PRL", 443);
		rm.put("PRO", 444);
		rm.put("PSO", 445);
		rm.put("PSU", 446);
		rm.put("PT", 447);
		rm.put("PT4", 448);
		rm.put("PTN", 449);
		rm.put("PYI", 450);
		rm.put("PYN", 451);
		rm.put("PYY", 452);
		rm.put("R14", 453);
		rm.put("R1C", 454);
		rm.put("R1Z", 455);
		rm.put("R8G", 456);
		rm.put("RAP", 457);
		rm.put("RB", 458);
		rm.put("RBF", 459);
		rm.put("RCE", 460);
		rm.put("RCZ", 461);
		rm.put("RHD", 462);
		rm.put("RHM", 463);
		rm.put("RIA", 464);
		rm.put("RIO", 465);
		rm.put("RKA", 466);
		rm.put("RKL", 467);
		rm.put("RKP", 468);
		rm.put("RKT", 469);
		rm.put("RML", 470);
		rm.put("RNR", 471);
		rm.put("RO2", 472);
		rm.put("ROS", 473);
		rm.put("RS3", 474);
		rm.put("RU", 475);
		rm.put("RU6", 476);
		rm.put("RUS", 477);
		rm.put("S02", 478);
		rm.put("S2M", 479);
		rm.put("S4C", 480);
		rm.put("S9L", 481);
		rm.put("SAH", 482);
		rm.put("SAM", 483);
		rm.put("SAU", 484);
		rm.put("SC", 485);
		rm.put("SE4", 486);
		rm.put("SER", 487);
		rm.put("SFG", 488);
		rm.put("SIN", 489);
		rm.put("SIS", 490);
		rm.put("SLZ", 491);
		rm.put("SMT", 492);
		rm.put("SN6", 493);
		rm.put("SN7", 494);
		rm.put("SN8", 495);
		rm.put("SN9", 496);
		rm.put("SNS", 497);
		rm.put("SO4", 498);
		rm.put("SPD", 499);
		rm.put("SPE", 500);
		rm.put("SPK", 501);
		rm.put("SPM", 502);
		rm.put("SPS", 503);
		rm.put("SR", 504);
		rm.put("SRY", 505);
		rm.put("SS0", 506);
		rm.put("SSP", 507);
		rm.put("SSU", 508);
		rm.put("T23", 509);
		rm.put("T32", 510);
		rm.put("T38", 511);
		rm.put("T39", 512);
		rm.put("T48", 513);
		rm.put("T49", 514);
		rm.put("T4S", 515);
		rm.put("T5E", 516);
		rm.put("T5O", 517);
		rm.put("T5S", 518);
		rm.put("T66", 519);
		rm.put("T6A", 520);
		rm.put("TAF", 521);
		rm.put("TB", 522);
		rm.put("TBZ", 523);
		rm.put("TCY", 524);
		rm.put("TEA", 525);
		rm.put("TEL", 526);
		rm.put("TER", 527);
		rm.put("TFE", 528);
		rm.put("TFT", 529);
		rm.put("THF", 530);
		rm.put("THR", 531);
		rm.put("TL", 532);
		rm.put("TLC", 533);
		rm.put("TLN", 534);
		rm.put("TNT", 535);
		rm.put("TOY", 536);
		rm.put("TP1", 537);
		rm.put("TPN", 538);
		rm.put("TPP", 539);
		rm.put("TPS", 540);
		rm.put("TRS", 541);
		rm.put("TTI", 542);
		rm.put("TYR", 543);
		rm.put("U", 544);
		rm.put("U33", 545);
		rm.put("U36", 546);
		rm.put("UAR", 547);
		rm.put("UCL", 548);
		rm.put("UFP", 549);
		rm.put("UFR", 550);
		rm.put("UFT", 551);
		rm.put("ULF", 552);
		rm.put("UMS", 553);
		rm.put("UMX", 554);
		rm.put("URX", 555);
		rm.put("US2", 556);
		rm.put("US3", 557);
		rm.put("US4", 558);
		rm.put("US5", 559);
		rm.put("USM", 560);
		rm.put("UVX", 561);
		rm.put("UZR", 562);
		rm.put("V", 563);
		rm.put("VAL", 564);
		rm.put("VN4", 565);
		rm.put("XAD", 566);
		rm.put("XAL", 567);
		rm.put("XAN", 568);
		rm.put("XAR", 569);
		rm.put("XCL", 570);
		rm.put("XCT", 571);
		rm.put("XGL", 572);
		rm.put("XGU", 573);
		rm.put("XTF", 574);
		rm.put("XTH", 575);
		rm.put("XTL", 576);
		rm.put("XTR", 577);
		rm.put("XUA", 578);
		rm.put("XUG", 579);
		rm.put("XXX", 580);
		rm.put("YG", 581);
		rm.put("YYG", 582);
		rm.put("ZAD", 583);
		rm.put("ZBC", 584);
		rm.put("ZBU", 585);
		rm.put("ZCY", 586);
		rm.put("ZDU", 587);
		rm.put("ZGU", 588);
		rm.put("ZHP", 589);
		rm.put("ZN", 590);
		rm.put("ZTH", 591);
		return rm;
	}

	public Map<String, Integer> getNormalizedNucleotideMapping() {
		return this.normalizedNucleotideMapping;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((chainId == null) ? 0 : chainId.hashCode());
		result = prime
				* result
				+ ((nucleotideConformation == null) ? 0
						: nucleotideConformation.hashCode());
		result = prime * result + ((pdbId == null) ? 0 : pdbId.hashCode());
		result = prime * result
				+ ((puckerAtom == null) ? 0 : puckerAtom.hashCode());
		result = prime * result
				+ ((puckerQuality == null) ? 0 : puckerQuality.hashCode());
		result = prime * result + residuePosition;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Nucleotide other = (Nucleotide) obj;
		if (chainId == null) {
			if (other.chainId != null)
				return false;
		} else if (!chainId.equals(other.chainId))
			return false;
		if (nucleotideConformation == null) {
			if (other.nucleotideConformation != null)
				return false;
		} else if (!nucleotideConformation.equals(other.nucleotideConformation))
			return false;
		if (pdbId == null) {
			if (other.pdbId != null)
				return false;
		} else if (!pdbId.equals(other.pdbId))
			return false;
		if (puckerAtom == null) {
			if (other.puckerAtom != null)
				return false;
		} else if (!puckerAtom.equals(other.puckerAtom))
			return false;
		if (puckerQuality == null) {
			if (other.puckerQuality != null)
				return false;
		} else if (!puckerQuality.equals(other.puckerQuality))
			return false;
		if (residuePosition != other.residuePosition)
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Nucleotide [pdbId=" + pdbId + ", chainId=" + chainId
				+ " , residueIdentifier=" + this.getResidueIdentifier()
				+ ", nucleotideConformation=" + nucleotideConformation
				+ ", residuePosition=" + residuePosition + ", puckerAtom="
				+ puckerAtom + ", puckerQuality=" + puckerQuality + "]";
	}

	public int compareTo(Nucleotide other) {
		int pos = other.getResiduePosition();
		return this.getResiduePosition() - pos;
	}

}
