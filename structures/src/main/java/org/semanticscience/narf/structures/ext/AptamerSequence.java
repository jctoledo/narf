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
package org.semanticscience.narf.structures.ext;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticscience.narf.structures.factories.secondary.PredictedSecondaryStructureFactory;
import org.semanticscience.narf.structures.factories.secondary.Rnafold;
import org.semanticscience.narf.structures.lib.exceptions.InvalidDotBracketNotationException;
import org.semanticscience.narf.structures.lib.exceptions.InvalidSequenceException;
import org.semanticscience.narf.structures.parts.DotBracketNotation;
import org.semanticscience.narf.structures.parts.Sequence;
import org.semanticscience.narf.structures.secondary.PredictedSecondaryStructure;

/**
 * @author  Jose Cruz-Toledo
 *
 */
public class AptamerSequence {
	static final Logger log = Logger.getLogger(AptamerSequence.class);
	private Sequence sequence;
	private String seqType;
	private URL topicURL;
	private Double avgKd;
	private PredictedSecondaryStructure rnaFoldPrediction;
	
	
	public AptamerSequence(){
		sequence = null;
		seqType = null;
		topicURL = null;
		avgKd = null;
		rnaFoldPrediction = null;
	}
	
	
	public AptamerSequence(Sequence aSequence, String aSeqType, Double anAvgKd){
		sequence = aSequence;
		seqType = aSeqType;
		avgKd = anAvgKd;
		//now compute secondary structure with rnafold
		rnaFoldPrediction = computeRNAFold();
	}
	
	public AptamerSequence(Sequence aSeq, String aSeqType, Double anAvgKd, URL aURL ){
		this(aSeq,aSeqType, anAvgKd);
		topicURL = aURL;
	}
	
	/**
	 * This method computes RNA Fold 1.8.5 with default parameters
	 * @return the MFE computed by RNAFold 1.8.5 for this Aptamer Sequence
	 */
	private PredictedSecondaryStructure computeRNAFold() {
		PredictedSecondaryStructureFactory p = new Rnafold();
		if(this.getSequence() != null){
			try {
				Sequence s = this.getSequence();
				Set<PredictedSecondaryStructure> x = p.getStructures(s);
				if(x.size() >= 1){
					Iterator<PredictedSecondaryStructure> itr = x.iterator();
					while(itr.hasNext()){
						return itr.next();
					}
				}else{
					log.warn("More than one secondary structure was predicted !!! ");
				}
			} catch (InvalidDotBracketNotationException e) {
				log.error("Check the DBN generated", e);
			} catch (InvalidSequenceException e) {
				log.error("something is wrong with the sequence", e);
			} catch (IOException e) {
				log.error("you should not be reading this!!! error! ",e);
			}
			
		}
		return null;
	}


	
	
	public PredictedSecondaryStructure getRNAFoldPrediction(){
		return rnaFoldPrediction;
	}
	
	/**
	 * @return the sequence
	 */
	public Sequence getSequence() {
		return sequence;
	}
	/**
	 * @return the seqType
	 */
	public String getSeqType() {
		return seqType;
	}

	/**
	 * @return the topicURL
	 */
	public URL getTopicURL() {
		return topicURL;
	}
	/**
	 * @return the avgKd
	 */
	public Double getAvgKd() {
		return avgKd;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AptamerSequence [sequence=" + sequence + ", seqType=" + seqType
				+ ", topicURL=" + topicURL + ", avgKd=" + avgKd
				+ ", rnaFoldPrediction=" + rnaFoldPrediction + "]";
	}
	
	


}
