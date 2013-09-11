/**
 * Copyright (c) 2011 Jose Miguel Cruz Toledo
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
package org.semanticscience.narf.structures.lib;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.log4j.Logger;

/**
 * This class holds methods and variables to help with the execution of a local
 * program
 * 
 * @author "Jose Cruz-Toledo"
 * 
 */
public class ProcessRunner {
	static Logger log = Logger.getLogger(ProcessRunner.class);
	/**
	 * Name of the local program that will be run
	 */
	public String processName;

	/**
	 * Create a ProcessRunner by passing in the name of the program on the local
	 * filesystem that you wish to execute
	 * 
	 * @param aProcessName
	 */
	public ProcessRunner(String aProcessName) {
		if (checkProcessPath(aProcessName)) {
			this.setProcessName(aProcessName);
		} else {
			System.out.println("Could not find " + aProcessName
					+ " in your path");
			System.exit(-1);
		}
	}

	/**
	 * This method will check the current path for the existence of the program
	 * 
	 * @param aProcessName
	 *            the name of the program that will be run
	 * @return true if the program is found in the path
	 */
	public boolean checkProcessPath(String aProcessName) {
		if (aProcessName.length() > 0) {
			Process p = null;
			try {
				p = Runtime.getRuntime().exec(aProcessName);

				return true;
			} catch (IOException ioe) {
				ioe.printStackTrace();
				System.out
						.println("Error executing this process!\nMake sure that "
								+ aProcessName + " is in your path.");
				System.exit(-1);
			} finally {
				p.destroy();
			}
		}
		return false;
	}

	/**
	 * This method returns the String that is outputted by some Process aProc
	 * 
	 * @param aProc
	 * @return
	 */
	public static String writeProcessOutput(Process aProc) {
		InputStreamReader tmpReader = new InputStreamReader(
				new BufferedInputStream(aProc.getInputStream()));
		BufferedReader reader = new BufferedReader(tmpReader);
		String returnMe = "";
		while (true) {
			String line = "";
			try {
				line = reader.readLine();
			} catch (IOException e) {
				System.out.println("Could not read input file");
				e.printStackTrace();
				System.exit(-1);
			}
			if (line == null)
				break;
			returnMe += line + "\n";
		}// while
		return returnMe;
	}// writeProcessOutput

	/**
	 * @return the processName
	 */
	public String getProcessName() {
		return processName;
	}

	/**
	 * @param processName
	 *            the processName to set
	 */
	public void setProcessName(String processName) {
		this.processName = processName;
	}

	/**
	 * Copies data from the given source to the given sink. This task is carried
	 * out by the thread returned. The thread is started automatically.
	 * 
	 * The virtual machine is turned off if this execution fails!
	 * 
	 * @param acceptTermination
	 *            Do not worry if the sink does not accept the whole source
	 *            data.
	 */
	public static Thread bindStreams(final InputStream in,
			final OutputStream out, final String name,
			final boolean acceptTermination) {
		Thread outputThread = new Thread() {
			public void run() {
				try {
					long use = 0;
					byte[] buf = new byte[1024];
					int got;

					while ((got = in.read(buf)) >= 0) {
						if (got > 0) {
							try {
								out.write(buf, 0, got);
							} catch (IOException e) {
								log.error("Warning: target stream was closed before the end of the content. Only "
										+ use
										+ " bytes were written by "
										+ name + '.');
								if (acceptTermination)
									return;
								throw e;
							} finally {
								out.close();
							}
							use += got;
						} else {
							log.warn("Zero bytes obtained.");
						}
					}
					try {
						out.flush();
					} catch (IOException e) {
						log.error("Warning: target stream was closed before the end of the content. Only "
								+ use + " bytes were written by " + name + '.');
						if (!acceptTermination)
							throw e;
					} finally {
						out.close();
					}
				} catch (IOException e) {
					log.error("I/O error in " + name + "...**");
					e.printStackTrace();
					System.exit(342);
				} finally {
					try {
						in.close();
						out.flush();
						out.close();

					} catch (IOException e) {
						log.error(e);
					}
				}
			}
		};
		outputThread.setName(name);
		outputThread.setDaemon(true);
		outputThread.start();
		return outputThread;
	}

}
