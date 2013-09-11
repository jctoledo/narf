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
public class CycleTest {
	private static UndirectedGraph<String, String> sampleGraph1 = null;
	private static UndirectedGraph<String, String> sampleGraph2 = null;
	private static UndirectedGraph<String, String> rectangleGraph = null;
	private static UndirectedGraph<String,String> sampleStem = null;

	private static Cycle<String, String> c1 = null;
	private static Cycle<String, String> c2 = null;
	private static Cycle<String, String> c3 = null;

	private static Cycle<String, String> rotateMe = null;
	private static Cycle<String, String> flipped = null;
	private static Cycle<String, String> recCycle = null;
	
	private static Cycle <String, String> cycleOne = null;
	

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		sampleGraph1 = createSampleGraph();
		/*sampleGraph2 = createSampleGraph2();
		rectangleGraph = createRectangleGraph();
		*/
		// cycles 1 and 2 are identical cycle 3 is different
		c1 = createC1(sampleGraph1);
		/*c2 = createC1(sampleGraph1);
		c3 = createC3(sampleGraph1);
		rotateMe = createRotateMe(sampleGraph2);
		flipped = rotateMe.invertCycle();
		recCycle = createCycle1Rectangle(rectangleGraph);*/
		sampleStem = createSampleStem();
		cycleOne = createCycleOne(sampleStem);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		rotateMe = null;
		c1 = null;
		c2 = null;
		c3 = null;
		rectangleGraph = null;
		recCycle = null;
		flipped = null;
		cycleOne = null;
	}
	@Test 
	public void testingRotateCycle(){
		Cycle <String, String>x = null;
		try{
			x = cycleOne.rotateCycle("v2","v1");
			List<String> sl = new ArrayList<String> ();
			sl.add("b");
			sl.add("c");
			sl.add("d");
			sl.add("e");
			sl.add("f");
			sl.add("g");
			sl.add("h");
			sl.add("a");
			assertEquals(sl, x.getEdgeList());
			assertEquals("v1", x.getEndVertex());
			assertEquals("v2", x.getStartVertex());
		}catch(CycleException e){
			System.out.println("poto rico :p( | )");
			e.printStackTrace();
			
		}
	}
	
	@Test
	public void testingcomputeEdgeSubList(){
		List<String> s;
		try {
			s = cycleOne.computeEdgeSubList("v1", "v8");
			List<String> sl = new ArrayList<String> ();
			sl.add("a");
			sl.add("b");
			sl.add("c");
			sl.add("d");
			sl.add("e");
			sl.add("f");
			sl.add("g");
			sl.add("h");
			assertEquals(sl, s);
		} catch (CycleException e) {
			e.printStackTrace();
		}
	}
	/*@Test
	public void testingInvertCycle() {

		boolean b = true;
		boolean c = true;
		boolean d = true;
		if (rotateMe.getNextEdge("v1").equals("a")) {
			b = false;
		}
		if (flipped.getNextEdge("v5").equals("d")) {
			c = false;
		}
		if (c == false && b == false) {
			d = false;
		}
		assertFalse(d);
	}*/

	/**
	 * Testing the equals method on two identical cycles except that one of them
	 * has been inverted
	 */
	/*@Test
	public void testingEquals() {
		boolean b = true;
		if (flipped.equals(rotateMe)) {
			b = false;
		}
		assertFalse(b);
	}*/

	/*@Test
	public void recCycleTest() {

		boolean b = true;
		if (recCycle.getLastVertex().equals("v6")
				&& recCycle.getNextEdge("v5").equals("eE")) {
			b = false;
		}
		assertFalse(b);
	}*/

	/*@Test
	public void testingGetNextVertex() {
		boolean b = true;
		String aV = "v6";
		if (recCycle.getNextVertex(aV).equals("v1")) {
			b = false;
		}
		assertFalse(b);
	}*/

	/*@Test
	public void rotateCycleTest1() {
		Cycle<String, String> x;
		boolean b = false;
		try {
			x = rotateMe.rotateCycle("v3", "v2");
			if (!x.equals(rotateMe)) {
				b = true;
			}
			assertFalse(b);
		} catch (CycleException e) {
			e.printStackTrace();
		}
		assertFalse(b);
	}*/
	
	/*@Test
	public void rotateCycleTest2(){
		Cycle <String, String>x ;
		boolean b =false;
		try{
			x = cycleOne.rotateCycle("v2","v1");
			System.out.println(x);
		}catch(CycleException e){
			e.printStackTrace();
		}
		assertFalse(b);
	}*/

	/*
	@Test
	public void testSubListCompute() {
		boolean b = true;
		try {
			List<String> el = rotateMe.computeEdgeSubList("v2", "v4");
			if (el.get(0).equals("b")) {
				b = false;
			}
		} catch (CycleException e) {
			e.printStackTrace();
		}
		assertFalse(b);
	}*/

	/*
	@Test
	public void testIncomingEdge() {
		boolean b = true;
		String anEdge = rotateMe.getIncomingEdge("v1");
		if (anEdge.equals("e")) {
			b = false;
		}
		assertFalse(b);
	}*/
	/*

	@Test
	public void testContainsVertex() {
		boolean f = true;
		if (rotateMe.containsVertex("v5")) {
			f = false;
		}
		assertFalse(f);
	}*/
	/*

	@Test
	public void testEquals() {
		boolean f = true;
		boolean g = true;
		boolean h = true;
		boolean x = true;
		if (c1.equals(c2)) {
			f = false;
		}
		if (!c1.equals(c3)) {
			g = false;
		}
		if (!c1.equals(rotateMe)) {
			x = false;
		}
		if ((g == false) && (f == false) && (x == false)) {
			h = false;
		} else {
			h = true;
		}
		assertFalse(h);
	}
	*/
	
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

	/**
	 * @return
	 */
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
		g.addEdge(v4, v1, d);
		g.addEdge(v5, v4, e);
		g.addEdge(v3, v5, f);
		return g;
	}

	/**
	 * 
	 * see http://imgur.com/86QrS8M
	 * 
	 * @return
	 */
	private static UndirectedGraph<String, String> createRectangleGraph() {
		UndirectedGraph<String, String> g = new SimpleWeightedGraph<String, String>(
				String.class);
		String v1 = "v1";
		String v2 = "v2";
		String v3 = "v3";
		String v4 = "v4";
		String v5 = "v5";
		String v6 = "v6";
		String eA = "eA";
		String eB = "eB";
		String eC = "eC";
		String eD = "eD";
		String eE = "eE";
		String eF = "eF";
		String eG = "eG";
		g.addVertex(v1);
		g.addVertex(v2);
		g.addVertex(v3);
		g.addVertex(v4);
		g.addVertex(v5);
		g.addVertex(v6);
		g.addEdge(v1, v2, eA);
		g.addEdge(v2, v3, eB);
		g.addEdge(v3, v4, eC);
		g.addEdge(v4, v5, eD);
		g.addEdge(v5, v6, eE);
		g.addEdge(v6, v1, eF);
		g.addEdge(v5, v2, eG);

		return g;
	}

	/**
	 * see http://imgur.com/1z600nw
	 * 
	 * @param g
	 * @return
	 * @throws CycleException
	 */
	private static Cycle<String, String> createCycle1Rectangle(
			UndirectedGraph<String, String> g) throws CycleException {
		List<String> eList = new LinkedList<String>();
		eList.add("eA");
		eList.add("eB");
		eList.add("eC");
		eList.add("eD");
		eList.add("eE");
		eList.add("eF");
		Cycle<String, String> c = new Cycle<String, String>(g, "v1", "v6",
				eList, eList.size());

		return c;
	}
	
	private static Cycle<String,String> createCycleOne(UndirectedGraph<String,String>g){
		List<String>el = new LinkedList<String>();
		el.add("a");
		el.add("b");
		el.add("c");
		el.add("d");
		el.add("e");
		el.add("f");
		el.add("g");
		el.add("h");
		String startV = "v1";
		String endV = "v8";
		Cycle<String,String> rm = null;
		try{
			rm = new Cycle<String, String>(g, startV, endV, el,el.size());
		}catch(CycleException e){
			e.printStackTrace();
		}
		return rm;
	}

	private static Cycle<String, String> createC1(
			UndirectedGraph<String, String> aG) {
		List<String> eL = new ArrayList<String>();
		eL.add("c");
		eL.add("e");
		eL.add("f");
		String startVertex = "v4";
		String endVertex = "v3";
		Cycle<String, String> rm = null;
		try {
			rm = new Cycle<String, String>(aG, startVertex, endVertex, eL,
					eL.size());
		} catch (CycleException e) {
			e.printStackTrace();
		}
		return rm;
	}

	private static Cycle<String, String> createC3(
			UndirectedGraph<String, String> aG) {
		Cycle<String, String> rm = null;
		List<String> el = new ArrayList<String>();
		el.add("c");
		el.add("d");
		el.add("a");
		el.add("b");
		String startVertex = "v4";
		String endVertex = "v3";
		try {
			rm = new Cycle<String, String>(sampleGraph1, startVertex,
					endVertex, el, el.size());
		} catch (CycleException e) {
			e.printStackTrace();
		}
		return rm;
	}

	private static Cycle<String, String> createRotateMe(
			UndirectedGraph<String, String> aG) {
		Cycle<String, String> rm = null;
		List<String> rEList = new LinkedList<String>();
		rEList.add("a");
		rEList.add("b");
		rEList.add("c");
		rEList.add("d");
		rEList.add("e");
		String startV = "v1";
		String endV = "v5";
		try {
			rm = new Cycle<String, String>(aG, startV, endV, rEList,
					rEList.size());
		} catch (CycleException e) {
			e.printStackTrace();
		}
		return rm;
	}
}
