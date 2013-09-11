/**
 * Copyright (c) 2011 William Greenwood
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

package org.semanticscience.narf.structures.secondary;

import org.semanticscience.narf.structures.PredictedStructure;
import org.semanticscience.narf.structures.factories.GeneratedStructureFactory;
import org.semanticscience.narf.structures.factories.secondary.PredictedSecondaryStructureFactory;
import org.semanticscience.narf.structures.parts.DotBracketNotation;



/**
 * Representation of the secondary structure of a nucleic acid generated using information
 * from secondary structure predictor.
 * @author William Greenwood
 * @version %I%, %G%
 * @since 1.6
 */
public final class PredictedSecondaryStructure extends SecondaryStructure implements PredictedStructure{

	/**
	 * The factory that generated the predicted secondary structure
	 */
	private final PredictedSecondaryStructureFactory structureFactory;
	
	/**
	 * The minimum free energy of this predicted nucleic acid structure
	 */
	private final double minimumFreeEnergy;
	
	/**
	 * Construct a secondary structure using information generated from a 
	 * secondary structure predictor.
	 * @param aStructureFactory factory that generated the predicted secondary structure
	 * @param aModelNumber model number of the nucleic acid secondary structure conformation
	 * @param aDotBracketNotation the dot bracket notation of the nucleic acid secondary structure
	 * @param aMinimumFreeEnergy the minimum free energy of this prediction
	 */
	public PredictedSecondaryStructure(PredictedSecondaryStructureFactory aStructureFactory, int aModelNumber,
			DotBracketNotation aDotBracketNotation, double aMinimumFreeEnergy) {
		super(aModelNumber, aDotBracketNotation);
		structureFactory = aStructureFactory;
		minimumFreeEnergy = aMinimumFreeEnergy;
	}
	
	@Override
	public String toString(){
		String returnMe = "Predicted by: " + this.getProgramName() + " version: " + this.getProgramVersion() +"\n";
		returnMe += this.getSequenceByChain("A") + "\n";
		returnMe += this.getDotBracketNotation() + " " + this.getMinimumFreeEnergy()  + " \n\n";
		
		return returnMe;		
	}
	
	/**
	 * Get the minimum free energy of this predicted secondary structure
	 * @return the minimum free energy
	 */
	public final double getMinimumFreeEnergy() {
		return minimumFreeEnergy;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final GeneratedStructureFactory getStructureFactory(){
		return structureFactory;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getProgramName(){
		return structureFactory.getProgramName();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getProgramVersion(){
		return structureFactory.getProgramVersion();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUniqueIdentifier() {
		return String.valueOf(this.hashCode());
	}

	
}
