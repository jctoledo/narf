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

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticscience.narf.graphs.lib.cycles.Cycle;
import org.semanticscience.narf.graphs.lib.cycles.exceptions.CycleBasisException;
import org.semanticscience.narf.graphs.old.CycleBasisOld;
import org.semanticscience.narf.structures.parts.Nucleotide;

/**
 * @author Jose Cruz-Toledo
 * 
 */
public class NucleicAcid2ZY6 {
	private static String pdbId = "4ENB";
	private static Set<NucleicAcid> nas;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		URL aURL = new URL("http://www.rcsb.org/pdb/files/" + pdbId + ".pdb");
		File aFile = new File(FileUtils.getTempDirectoryPath() + "/" + pdbId
				+ ".pdb");
		FileUtils.copyURLToFile(aURL, aFile);
		nas = ExtractedNucleicAcid.x3dnaDssr(aFile);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		nas = null;
	}

	@Test
	public void testingCycleBasisPrettyPrint() throws IOException {
		for (NucleicAcid aNuc : nas) {
			try {
				CycleBasisOld<Nucleotide, InteractionEdge> cb = new CycleBasisOld<Nucleotide, InteractionEdge>(
						aNuc);
				List<Cycle<Nucleotide, InteractionEdge>> chordlessCB = cb
						.getChordlessCycleBasis();
				// get the total number of cycles
				System.out.println("Cycle Basis for PDBID: " + pdbId);
				System.out.println("Total number of cylces: "
						+ chordlessCB.size());
				String b = "";
				for (Cycle<Nucleotide, InteractionEdge> cycle : chordlessCB) {
					// get the cycle length
					int clen = cycle.size();
					// get the interaction edges
					String edgeSummary = "Edges : [";
					List<InteractionEdge> edges = cycle.getEdgeList();
					for (InteractionEdge anEdge : edges) {
						edgeSummary += anEdge.extractEdgeClasses() + "-";
					}
					// remove the last "-"
					edgeSummary = edgeSummary.substring(0,
							edgeSummary.length() - 1) + "] ";
					// make a vertex summary
					String vertexSummary = "Vertices : [";
					List<Nucleotide> vertices = cycle.getVertexList();
					for (Nucleotide n : vertices) {
						vertexSummary += n.getResidueIdentifier()
								+ n.getResiduePosition() + "_" + n.getChainId()
								+ ", ";
					}
					// remove the last ", "
					vertexSummary = vertexSummary.substring(0,
							vertexSummary.length() - 2)
							+ "]";
					// get the basepair classes
					String bpSummary = "BasePair classes: [";
					for (InteractionEdge anEdge : edges) {
						String bpC = anEdge.extractBasePairClasses();
						if (bpC.length() > 0) {
							bpSummary += anEdge.extractBasePairClasses() + ", ";
						}
					}
					bpSummary = bpSummary.substring(0, bpSummary.length() - 2)
							+ "]";
					String printMe = "Cycle Length : " + clen + "\n";
					printMe += "Start Vertex: "
							+ cycle.getStartVertex().getResidueIdentifier()
							+ cycle.getStartVertex().getResiduePosition() + "_"
							+ cycle.getStartVertex().getChainId() + "\n";
					printMe += "End Vertex: "
							+ cycle.getEndVertex().getResidueIdentifier()
							+ cycle.getEndVertex().getResiduePosition() + "_"
							+ cycle.getEndVertex().getChainId() + "\n";
					printMe += edgeSummary + "\n";
					printMe += vertexSummary + "\n";
					printMe += bpSummary + "\n\n";

					System.out.println(printMe);
				}
				FileUtils.write(new File("/tmp/out.output"), b);
			} catch (CycleBasisException e) {
				e.printStackTrace();
			}
		}
	}

}
