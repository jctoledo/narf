/**
 * Copyright (c) 2012  Jose Cruz-Toledo
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


import org._3pq.jgrapht.graph.SimpleGraph;
import org.junit.Before;
import org.junit.Test;
import org.openscience.cdk.ringsearch.cyclebasis.CycleBasis;

/**
 * @author Jose Cruz-Toledo
 * 
 */
public class TestCDK {
	private CycleBasis basis1;
	private CycleBasis basis2;
	private CycleBasis basis3;
	private CycleBasis basis4;
	
	private SimpleGraph g1;
	private SimpleGraph g2;
	private SimpleGraph g3;
	private SimpleGraph g4;
	@Before
	public void setUp(){
		g1 = new SimpleGraph();
		g1.addVertex("a");
		g1.addVertex("b");
		g1.addVertex("c");
		g1.addVertex("d");
		g1.addVertex("e");
		g1.addVertex("f");
		g1.addVertex("g");
		g1.addVertex("h");
		g1.addVertex("i");
		g1.addVertex("j");
		g1.addVertex("k");
		
		g1.addEdge("a","b");
		g1.addEdge("a","c");
		g1.addEdge("b","c");
		g1.addEdge("b","d");
		g1.addEdge("c","d");
		g1.addEdge("d","e");
		g1.addEdge("d","g");
		g1.addEdge("e","f");
		g1.addEdge("e","h");
		g1.addEdge("f","h");
		g1.addEdge("i","j");
		g1.addEdge("i","k");
		g1.addEdge("j","k");
		basis1 = new CycleBasis(g1);
		
		g2 = new SimpleGraph();
		g2.addVertex("a");
		g2.addVertex("b");
		g2.addVertex("c");
		g2.addVertex("d");
		g2.addVertex("e");
		g2.addVertex("f");
		
		g2.addEdge("a", "b");
		g2.addEdge("b", "c");
		g2.addEdge("c", "d");
		g2.addEdge("d", "a");
		g2.addEdge("a", "e");
		g2.addEdge("e", "f");
		g2.addEdge("e", "d");
		g2.addEdge("f", "b");
		g2.addEdge("f", "c");
		basis2 = new CycleBasis(g2);
		
		g3 = new SimpleGraph();
		g3.addVertex("a");
		g3.addVertex("b");
		g3.addVertex("c");
		g3.addVertex("d");
		g3.addVertex("e");
		g3.addEdge("a", "b");
		g3.addEdge("b", "c");
		g3.addEdge("c", "d");
		g3.addEdge("a", "d");
		g3.addEdge("e", "c");
		g3.addEdge("e", "b");
		g3.addEdge("e", "d");
		g3.addEdge("e", "a");
		basis3 = new CycleBasis(g3);
		
		g4 = new SimpleGraph();
		g4.addVertex("a");
		g4.addVertex("b");
		g4.addVertex("c");
		g4.addVertex("d");
		g4.addVertex("e");
		g4.addVertex("f");
		g4.addVertex("g");
		g4.addVertex("h");
		g4.addVertex("i");
		g4.addVertex("j");
		g4.addVertex("k");
		g4.addVertex("l");
		g4.addVertex("m");
		g4.addVertex("n");
		g4.addVertex("o");
		g4.addVertex("p");
		
		g4.addEdge("a", "b");
		g4.addEdge("c", "b");
		g4.addEdge("c", "d");
		g4.addEdge("d", "h");
		g4.addEdge("g", "h");
		g4.addEdge("f", "g");
		g4.addEdge("e", "f");
		g4.addEdge("a", "e");
		g4.addEdge("e", "i");
		g4.addEdge("i", "j");
		g4.addEdge("j", "k");
		g4.addEdge("l", "k");
		g4.addEdge("l", "p");
		g4.addEdge("o", "p");
		g4.addEdge("o", "n");
		g4.addEdge("m", "n");
		g4.addEdge("m", "i");
		g4.addEdge("n", "j");
		g4.addEdge("o", "k");
		g4.addEdge("g", "k");
		g4.addEdge("j", "f");
		g4.addEdge("l", "h");
		g4.addEdge("g", "c");
		g4.addEdge("b", "f");
		basis4 = new CycleBasis(g4);
	}
	
	
	@Test
	public void testOne(){
		System.out.println(basis1.cycles());
	}
	
	@Test
	public void tesTwo(){
		System.out.println(basis2.cycles());
		
	}
	@Test
	public void testThree(){
		System.out.println(basis3.cycles());
	}
	@Test
	public void testFour(){
		System.out.println(basis4.cycles());
	}
	
}
