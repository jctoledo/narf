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
package org.semanticscience.narf.structures.bin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.semanticscience.narf.structures.ext.AptamerSequence;
import org.semanticscience.narf.structures.parts.DotBracketNotation;
import org.semanticscience.narf.structures.parts.Sequence;

/**
 * @author  Jose Cruz-Toledo
 *
 */
public class EntropyCalculator {
	
	public static Double combinedShannonEntropy(List<AptamerSequence> values){
		Map<DotBracketNotation, Integer> map = new HashMap<DotBracketNotation, Integer>();
		for(AptamerSequence as : values){
			DotBracketNotation aDBN = as.getRNAFoldPrediction().getDotBracketNotation();
			if(!map.containsKey(aDBN)){
				map.put(aDBN, 0);
			}
			map.put(aDBN, map.get(aDBN) +1);
		}
		Double rm = 0.0;
		for(DotBracketNotation d : map.keySet()){
			Double freq = (double) map.get(d)/values.size();
			rm -= freq * (Math.log(freq)/Math.log(2));
		}
		return rm;
	}
	
	public static Double combinedShannonEntropyPerResidueDNA(List<AptamerSequence> seqs){
		Map<String, Integer> map = new HashMap<String, Integer>();
		int totalSeqLength = 0;
		for(AptamerSequence as : seqs){
			Integer gcount = as.getSequence().getGuanineCount();
			Integer ccount = as.getSequence().getCytosineCount();
			Integer acount = as.getSequence().getAdenineCount();
			Integer tcount = as.getSequence().getThymineCount();
			map.put("G", gcount);
			map.put("C", ccount);
			map.put("T", tcount);
			map.put("A", acount);
			totalSeqLength += as.getSequence().getLength();
		}

		Double rm = 0.0;
		for(String key : map.keySet()){
			Double freq = (double)map.get(key)/totalSeqLength;
			rm -= freq *(Math.log(freq)/Math.log(4));
		}
		return rm;
	}
	
	public static Map<String, Double> perResidueProbabilityDistribution(List<AptamerSequence> seqs){
		Map<String, Integer> map = new HashMap<String, Integer>();
		Map<String, Double> rm = new HashMap<String, Double>();
		int totalSeqLength = 0;
		for(AptamerSequence as : seqs){
			Sequence s = as.getSequence();
			int l = s.getLength();
			Integer gcount = as.getSequence().getGuanineCount();
			Integer ccount = as.getSequence().getCytosineCount();
			Integer acount = as.getSequence().getAdenineCount();
			Integer tcount = as.getSequence().getThymineCount();
			if(!map.containsKey("C")){
				map.put("C", ccount);
			}else{
				map.put("C", map.get("C") +1);
			}
			if(!map.containsKey("G")){
				map.put("G", gcount);
			}else{
				map.put("G", map.get("G") +1);
			}
			if(!map.containsKey("T")){
				map.put("T", tcount);
			}else{
				map.put("T", map.get("T") +1);
			}
			if(!map.containsKey("A")){
				map.put("A", acount);
			}else{
				map.put("A", map.get("A") +1);
			}			
			totalSeqLength += l;
			//System.out.println(totalSeqLength);
		}
		rm.put("Total residues", (double)totalSeqLength);
		for(String k : map.keySet()){
			Double freq = (double)map.get(k)/totalSeqLength;
			rm.put(k, freq);
		}
		return rm;
	}
	
}
