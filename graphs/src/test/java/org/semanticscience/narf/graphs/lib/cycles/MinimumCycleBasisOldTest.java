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

import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.KruskalMinimumSpanningTree;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticscience.narf.graphs.old.MinimumCycleBasisOld;

/**
 * @author  Jose Cruz-Toledo
 *
 */
public class MinimumCycleBasisOldTest {

	private static UndirectedGraph<String, String> sampleGraph = null;
	private static MinimumCycleBasisOld<String, String> mcb = null;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		sampleGraph = createSampleGraph4();
		mcb = new MinimumCycleBasisOld<String, String>(sampleGraph);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		sampleGraph = null;
		mcb = null;
	}

	@Test
	public void test() {
		List<String> tE = mcb.getTreeEdges();
		List<String> bE = mcb.getBackEdges();
		Set<String> mst = new  KruskalMinimumSpanningTree<String, String>(sampleGraph).getEdgeSet();
		System.out.println(mst);
		System.out.println(tE);
		System.out.println(bE);
	}

	
	private static UndirectedGraph<String, String> createSampleGraph() {
		UndirectedGraph<String, String> g = new SimpleWeightedGraph<String, String>(
				String.class);
		String v1 = "v1";
		String v2 = "v2";
		String v3 = "v3";
		String v4 = "v4";
		String a = "a";
		String b = "b";
		String c = "c";
		String d = "d";
		g.addVertex(v1);
		g.addVertex(v2);
		g.addVertex(v3);
		g.addVertex(v4);
		g.addEdge(v1, v2, a);
		g.addEdge(v2, v3, b);
		g.addEdge(v3, v4, c);
		g.addEdge(v3, v1, d);
		return g;
	}
	
	private static UndirectedGraph<String, String> createSampleGraph2() {
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
	
	private static UndirectedGraph<String,String> createSampleGraph3(){
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
	
	private static UndirectedGraph<String,String> createSampleGraph4(){
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
		rm.addEdge(v5, v4, d);
		rm.addEdge(v5, v6, q);
		rm.addEdge(v6, v3, e);
		rm.addEdge(v6, v7, p);
		rm.addEdge(v7, v2, f);
		rm.addEdge(v8, v7, o);
		rm.addEdge(v1, v8, g);
		rm.addEdge(v8, v9, h);
		rm.addEdge(v9, v10, n);
		rm.addEdge(v7, v10, i);
		rm.addEdge(v10, v11, m);
		rm.addEdge(v6, v11, j);
		rm.addEdge(v11, v12, l);
		rm.addEdge(v12, v5, k);
		return rm;
	}
	
	private static UndirectedGraph<String,String> createSampleGraph5(){
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
		rm.addVertex(v1);
		rm.addVertex(v2);
		rm.addVertex(v3);
		rm.addVertex(v4);
		rm.addVertex(v5);
		rm.addVertex(v6);
		rm.addEdge(v1, v2, a);
		rm.addEdge(v2, v3, b);
		rm.addEdge(v3, v4, c);
		rm.addEdge(v4, v5, d);
		rm.addEdge(v5, v6, e);
		rm.addEdge(v4, v6, f);
		rm.addEdge(v6, v3, g);
		rm.addEdge(v6, v2, h);
		return rm;
	}
	
}
