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

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Jose Cruz-Toledo
 * 
 */
public class FundamentalCycleBasisTest {
	private static UndirectedGraph<String, String> sampleGraph = null;
	private static UndirectedGraph<String, String> sampleStem = null;

	private static FundamentalCycleBasis<String, String> mcb = null;
	private static FundamentalCycleBasis<String, String> mcb_stem = null;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//sampleGraph = createSampleGraph();
		sampleStem = createSampleStem();
		//mcb = new MinimumCycleBasis<String, String>(sampleGraph);
		mcb_stem = new FundamentalCycleBasis<String, String>(sampleStem);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		sampleGraph = null;
		mcb = null;
		mcb_stem = null;
		sampleStem = null;
	}

	@Test
	public void test() {
		System.out.println(mcb_stem.getCycleBasis());
	}

	private static UndirectedGraph<String, String> createSampleGraph() {
		UndirectedGraph<String, String> rm = new SimpleWeightedGraph<String, String>(
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
		rm.addVertex(v1);
		rm.addVertex(v2);
		rm.addVertex(v3);
		rm.addVertex(v4);
		rm.addVertex(v5);
		rm.addEdge(v1, v2, a);
		rm.addEdge(v2, v3, b);
		rm.addEdge(v3, v4, c);
		rm.addEdge(v5, v4, d);
		rm.addEdge(v5, v1, e);
		rm.addEdge(v2, v5, f);
		return rm;
	}

	private static UndirectedGraph<String, String> createSampleStem() {
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
		rm.addEdge(v5, v4, d);
		rm.addEdge(v5, v6, e);
		rm.addEdge(v6, v7, f);
		rm.addEdge(v7, v8, g);
		rm.addEdge(v1, v8, h);
		rm.addEdge(v2, v7, i);
		rm.addEdge(v3, v6, j);
		return rm;
	}
	
	/*private static UndirectedGraph<String, String> createSampleComplicated(){
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
		String l = "l";
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
		rm.addEdge(v5, v4, d);
		rm.addEdge(v5, v6, e);
		rm.addEdge(v6, v7, f);
		rm.addEdge(v7, v8, g);
		rm.addEdge(v1, v8, h);
		rm.addEdge(v2, v7, i);
		rm.addEdge(v3, v6, j);
		return rm;
	}*/
}
