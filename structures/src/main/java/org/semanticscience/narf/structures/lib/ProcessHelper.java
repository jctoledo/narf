/**
 * This class has some auxiliary methods for when running processes.
 */
package org.semanticscience.narf.structures.lib;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Jose Cruz-Toledo
 *
 */
public class ProcessHelper {

	/**
	 * This method returns the String that is outputted by some Process aProc
	 * @param aProc
	 * @return
	 */
	public static String writeProcessOutput(Process aProc){
		InputStreamReader tmpReader = new  InputStreamReader(new BufferedInputStream(aProc.getInputStream()));
		BufferedReader reader = new BufferedReader(tmpReader);
		String returnMe ="";
		while(true){
			String line ="";
			try {
				line = reader.readLine();
			} catch (IOException e) {
				System.out.println("Could not read input file");
				e.printStackTrace();
				System.exit(-1);
			}
			if(line == null)
				break;
			returnMe += line+"\n";
		}//while
		return returnMe;
	}//writeProcessOutput
}