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
package org.semanticscience.narf.graphs.lib.cycles;

import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticscience.narf.graphs.lib.cycles.exceptions.CycleException;

/**
 * @author Jose Cruz-Toledo
 * 
 */
public class CycleBasisTest {

	private static UndirectedGraph<String, String> sampleGraph1 = null;
	private static UndirectedGraph<String, String> sampleGraph2 = null;
	private static UndirectedGraph<String, String> sampleGraph3 = null;
	private static UndirectedGraph<String, String> sampleGraph4 = null;
	private static UndirectedGraph<String, String> sampleGraph5 = null;
	private static UndirectedGraph<String, String> sampleHairpinGraph = null;
	private static UndirectedGraph<String, String> dfsGraph = null;
	private static CycleBasis<String, String> ccb = null;
	private static CycleBasis<String, String> ccb2 = null;
	private static CycleBasis<String, String> ccb3 = null;
	private static CycleBasis<String, String> ccb4 = null;
	private static CycleBasis<String, String> ccb5 = null;
	private static CycleBasis<String, String> hairpin = null;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		sampleGraph1 = createSampleGraph();
		sampleGraph2 = createSampleGraph2();
		sampleGraph3 = createSampleGraph3();
		sampleGraph4 = createSampleGraph4();
		sampleGraph5 = createSampleGraph5();
		sampleHairpinGraph = createSampleHairpin();
		dfsGraph = createDPSGraph();
		ccb2 = new FundamentalCycleBasis<String, String>(sampleGraph2);
		ccb3 = new FundamentalCycleBasis<String, String>(sampleGraph3);
		ccb4 = new FundamentalCycleBasis<String, String>(sampleGraph4);
		ccb5 = new FundamentalCycleBasis<String, String>(sampleGraph5);
		hairpin = new FundamentalCycleBasis<String, String>(sampleHairpinGraph);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		sampleGraph1 = null;
		sampleGraph2 = null;
		sampleGraph3 = null;
		sampleGraph4 = null;
		sampleGraph5 = null;
		hairpin = null;

		ccb = null;
		ccb2 = null;
		ccb3 = null;
		ccb4 = null;
		ccb5 = null;
	}

	@Test
	public void testingCCB() throws CycleException {
		boolean b = true;
		// test if triangle with edges (c, d, f) and vertices (v3, v4, v5) is in
		// the ccb
		List<String> el = new LinkedList<String>();
		el.add("c");
		el.add("d");
		el.add("f");
		Cycle<String, String> triangle = new Cycle<String, String>(
				sampleGraph1, "v3", "v5", el, el.size());
		List<Cycle<String, String>> basis = ccb.getCycleBasis();
		if (basis.contains(triangle)) {
			b = false;
		}
		assertFalse(b);
	}

	@Test
	public void testingCCB2() throws CycleException {
		boolean b = true;
		// test if triangle with edges (h,i, b) and vertices(v2,v6,v3) is in the
		// ccb2
		List<String> el = new LinkedList<String>();
		el.add("h");
		el.add("i");
		el.add("b");
		Cycle<String, String> triangle = new Cycle<String, String>(
				sampleGraph2, "v2", "v3", el, el.size());
		List<Cycle<String, String>> basis = ccb2.getCycleBasis();
		if (basis.contains(triangle)) {
			b = false;
		}
		assertFalse(b);
	}

	/*
	 * @Test public void testingCCB3() { boolean b = true; //test if
	 * List<Cycle<String, String>> l = ccb3.getCycleBasis();
	 * System.out.println(ccb3.getCycleBasis().size()); for(Cycle<String,
	 * String> c :l){ if(c.getWeight() == 4.0){
	 * System.out.println(" Weight 4: "+ c); }else{ System.out.println(
	 * " not 4: "+ c); } } System.out.println(ccb3.getCycleBasis()); Map<Double,
	 * List<Cycle<String,String>>> m = new HashMap<Double,
	 * List<Cycle<String,String>>>(); for (Cycle<String, String> cycle : l) {
	 * Double w = cycle.getWeight(); if(!m.containsKey(w)){
	 * List<Cycle<String,String>> tl = new ArrayList<Cycle<String, String>>();
	 * tl.add(cycle); m.put(w, tl); }else{ //already in there //get the list
	 * //add cycle to the end of it List<Cycle<String,String>> qw = m.get(w);
	 * qw.add(cycle); m.put(w, qw); } } for (Map.Entry<Double,
	 * List<Cycle<String,String>>> entry : m.entrySet()) {
	 * System.out.println("key = "+entry.getKey()+ ", length = "+
	 * entry.getValue().size()+ " , contents = "+entry.getValue() ); } }
	 */

	@Test
	public void testingCCB4() throws CycleException {
		boolean b = true;
		boolean e = true;
		boolean d = true;
		List<Cycle<String, String>> l = ccb4.getCycleBasis();
		List<String> el1 = new ArrayList<String>();
		el1.add("k");
		el1.add("i");
		el1.add("f");
		el1.add("h");
		Cycle<String, String> c1 = new Cycle<String, String>(sampleGraph4,
				"v7", "v4", el1, el1.size());
		List<String> el2 = new ArrayList<String>();
		el2.add("l");
		el2.add("j");
		el2.add("g");
		el2.add("i");
		Cycle<String, String> c2 = new Cycle<String, String>(sampleGraph4,
				"v8", "v5", el2, el2.size());

		if (l.contains(c1)) {
			b = false;
		}
		if (l.contains(c2)) {
			e = false;
		}

		if (b == false && e == false) {
			d = false;
		}
		assertFalse(d);
	}

	@Test
	public void testingCCB5() throws CycleException {
		boolean b = true;
		List<Cycle<String, String>> c = ccb5.getCycleBasis();
		// Cycle: VertexList [v8, v4, v3, v2] EdgeList: [j, c, b, i]
		// StartVertex: v8, EndVertex: v2 Weight: 4.0
		List<String> el1 = new ArrayList<String>();
		el1.add("j");
		el1.add("c");
		el1.add("b");
		el1.add("i");
		Cycle<String, String> c1 = new Cycle<String, String>(sampleGraph5,
				"v8", "v2", el1, el1.size());
		if (c.contains(c1)) {
			b = false;
		}
		assertFalse(b);
	}

	private static UndirectedGraph<String, String> createSampleGraph() {
		UndirectedGraph<String, String> g = new SimpleWeightedGraph<String, String>(
				String.class);
		String v1 = "v1";
		String v2 = "v2";
		String v3 = "v3";
		String v4 = "v4";
		String v5 = "v5";
		String a = "a";
		String b = "b";
		String c = "c";
		String d = "d";
		String e = "e";
		String f = "f";
		g.addVertex(v1);
		g.addVertex(v2);
		g.addVertex(v3);
		g.addVertex(v4);
		g.addVertex(v5);
		g.addEdge(v1, v2, a);
		g.addEdge(v2, v3, b);
		g.addEdge(v3, v4, c);
		g.addEdge(v4, v5, d);
		g.addEdge(v5, v1, e);
		g.addEdge(v3, v5, f);
		return g;
	}

	private static UndirectedGraph<String, String> createSampleGraph2() {
		UndirectedGraph<String, String> rm = new SimpleWeightedGraph<String, String>(
				String.class);
		String v1 = "v1";
		String v2 = "v2";
		String v3 = "v3";
		String v4 = "v4";
		String v5 = "v5";
		String v6 = "v6";
		String a = "a";
		String b = "b";
		String c = "c";
		String d = "d";
		String e = "e";
		String f = "f";
		String g = "g";
		String h = "h";
		String i = "i";
		rm.addVertex(v1);
		rm.addVertex(v2);
		rm.addVertex(v3);
		rm.addVertex(v4);
		rm.addVertex(v5);
		rm.addVertex(v6);
		rm.addEdge(v1, v2, a);
		rm.addEdge(v2, v3, b);
		rm.addEdge(v3, v4, c);
		rm.addEdge(v4, v1, d);
		rm.addEdge(v5, v1, e);
		rm.addEdge(v4, v5, f);
		rm.addEdge(v6, v5, g);
		rm.addEdge(v6, v2, h);
		rm.addEdge(v3, v6, i);
		return rm;
	}

	private static UndirectedGraph<String, String> createSampleGraph3() {
		UndirectedGraph<String, String> rm = new SimpleWeightedGraph<String, String>(
				String.class);
		String v1 = "v1";
		String v2 = "v2";
		String v3 = "v3";
		String v4 = "v4";
		String v5 = "v5";
		String v6 = "v6";
		String v7 = "v7";
		String v8 = "v8";
		String v9 = "v9";
		String v10 = "v10";
		String v11 = "v11";
		String v12 = "v12";
		String a = "a";
		String b = "b";
		String c = "c";
		String d = "d";
		String e = "e";
		String f = "f";
		String g = "g";
		String h = "h";
		String i = "i";
		String j = "j";
		String k = "k";
		String l = "l";
		String m = "m";
		String n = "n";
		String o = "o";
		String p = "p";
		String q = "q";
		rm.addVertex(v1);
		rm.addVertex(v2);
		rm.addVertex(v3);
		rm.addVertex(v4);
		rm.addVertex(v5);
		rm.addVertex(v6);
		rm.addVertex(v7);
		rm.addVertex(v8);
		rm.addVertex(v9);
		rm.addVertex(v10);
		rm.addVertex(v11);
		rm.addVertex(v12);
		rm.addEdge(v1, v2, a);
		rm.addEdge(v2, v3, b);
		rm.addEdge(v3, v4, c);
		rm.addEdge(v5, v1, d);
		rm.addEdge(v2, v6, e);
		rm.addEdge(v3, v7, f);
		rm.addEdge(v4, v8, g);
		rm.addEdge(v5, v6, h);
		rm.addEdge(v6, v7, i);
		rm.addEdge(v7, v8, j);
		rm.addEdge(v5, v9, k);
		rm.addEdge(v6, v10, l);
		rm.addEdge(v7, v11, m);
		rm.addEdge(v8, v12, n);
		rm.addEdge(v9, v10, o);
		rm.addEdge(v11, v10, p);
		rm.addEdge(v12, v11, q);
		return rm;
	}

	private static UndirectedGraph<String, String> createSampleGraph4() {
		UndirectedGraph<String, String> rm = new SimpleWeightedGraph<String, String>(
				String.class);
		String v1 = "v1";
		String v2 = "v2";
		String v3 = "v3";
		String v4 = "v4";
		String v5 = "v5";
		String v6 = "v6";
		String v7 = "v7";
		String v8 = "v8";
		String v9 = "v9";
		String a = "a";
		String b = "b";
		String c = "c";
		String d = "d";
		String e = "e";
		String f = "f";
		String g = "g";
		String h = "h";
		String i = "i";
		String j = "j";
		String k = "k";
		String l = "l";
		rm.addVertex(v1);
		rm.addVertex(v2);
		rm.addVertex(v3);
		rm.addVertex(v4);
		rm.addVertex(v5);
		rm.addVertex(v6);
		rm.addVertex(v7);
		rm.addVertex(v8);
		rm.addVertex(v9);
		rm.addEdge(v1, v2, a);
		rm.addEdge(v2, v3, b);
		rm.addEdge(v1, v4, c);
		rm.addEdge(v5, v2, d);
		rm.addEdge(v3, v6, e);
		rm.addEdge(v4, v5, f);
		rm.addEdge(v5, v6, g);
		rm.addEdge(v4, v7, h);
		rm.addEdge(v5, v8, i);
		rm.addEdge(v6, v9, j);
		rm.addEdge(v7, v8, k);
		rm.addEdge(v8, v9, l);
		return rm;
	}

	private static UndirectedGraph<String, String> createDPSGraph() {
		UndirectedGraph<String, String> rm = new SimpleWeightedGraph<String, String>(
				String.class);
		String vA = "A";
		String vB = "B";
		String vC = "C";
		String vD = "D";
		String vE = "E";
		String vF = "F";
		String vG = "G";
		String vH = "H";
		String a = "a";
		String b = "b";
		String c = "c";
		String d = "d";
		String e = "e";
		String f = "f";
		String g = "g";
		String h = "h";
		String i = "i";
		rm.addVertex(vA);
		rm.addVertex(vB);
		rm.addVertex(vC);
		rm.addVertex(vD);
		rm.addVertex(vE);
		rm.addVertex(vF);
		rm.addVertex(vG);
		rm.addVertex(vH);
		rm.addEdge(vA, vB, a);
		rm.addEdge(vB, vE, e);
		rm.addEdge(vE, vG, g);
		rm.addEdge(vG, vA, f);
		rm.addEdge(vA, vD, b);
		rm.addEdge(vF, vD, c);
		rm.addEdge(vB, vF, d);
		rm.addEdge(vF, vC, h);
		rm.addEdge(vC, vH, i);
		return rm;
	}

	private static UndirectedGraph<String, String> createSampleHairpin() {
		UndirectedGraph<String, String> rm = new SimpleWeightedGraph<String, String>(
				String.class);
		String v1 = "v1";
		String v2 = "v2";
		String v3 = "v3";
		String v4 = "v4";
		String v5 = "v5";
		String v6 = "v6";
		String v7 = "v7";
		String v8 = "v8";
		String v9 = "v9";
		String v10 = "v10";
		String v11 = "v11";
		String v12 = "v12";
		String v13 = "v13";
		String a = "a";
		String b = "b";
		String c = "c";
		String d = "d";
		String e = "e";
		String f = "f";
		String g = "g";
		String h = "h";
		String i = "i";
		String j = "j";
		String k = "k";
		String l = "l";
		String m = "m";
		String o = "o";
		String p = "p";
		String q = "q";
		rm.addVertex(v1);
		rm.addVertex(v2);
		rm.addVertex(v3);
		rm.addVertex(v4);
		rm.addVertex(v5);
		rm.addVertex(v6);
		rm.addVertex(v7);
		rm.addVertex(v8);
		rm.addVertex(v9);
		rm.addVertex(v10);
		rm.addVertex(v11);
		rm.addVertex(v12);
		rm.addVertex(v13);
		rm.addEdge(v1, v2, a);
		rm.addEdge(v2, v3, b);
		rm.addEdge(v3, v4, c);
		rm.addEdge(v5, v4, d);
		rm.addEdge(v5, v6, e);
		rm.addEdge(v6, v7, f);
		rm.addEdge(v7, v8, g);
		rm.addEdge(v9, v8, h);
		rm.addEdge(v10, v9, i);
		rm.addEdge(v4, v10, m);
		rm.addEdge(v10, v11, j);
		rm.addEdge(v11, v12, k);
		rm.addEdge(v11, v3, o);
		rm.addEdge(v2, v12, p);
		rm.addEdge(v12, v13, l);
		return rm;
	}

	private static UndirectedGraph<String, String> createSampleGraph5() {
		UndirectedGraph<String, String> rm = new SimpleWeightedGraph<String, String>(
				String.class);
		String v1 = "v1";
		String v2 = "v2";
		String v3 = "v3";
		String v4 = "v4";
		String v5 = "v5";
		String v6 = "v6";
		String v7 = "v7";
		String v8 = "v8";
		String a = "a";
		String b = "b";
		String c = "c";
		String d = "d";
		String e = "e";
		String f = "f";
		String g = "g";
		String h = "h";
		String i = "i";
		String j = "j";
		String k = "k";
		rm.addVertex(v1);
		rm.addVertex(v2);
		rm.addVertex(v3);
		rm.addVertex(v4);
		rm.addVertex(v5);
		rm.addVertex(v6);
		rm.addVertex(v7);
		rm.addVertex(v8);
		rm.addEdge(v1, v2, a);
		rm.addEdge(v2, v3, b);
		rm.addEdge(v3, v4, c);
		rm.addEdge(v8, v4, j);
		rm.addEdge(v2, v8, i);
		rm.addEdge(v1, v8, h);
		rm.addEdge(v1, v7, g);
		rm.addEdge(v6, v8, k);
		rm.addEdge(v5, v4, d);
		rm.addEdge(v6, v5, e);
		rm.addEdge(v7, v6, f);
		return rm;
	}
}
