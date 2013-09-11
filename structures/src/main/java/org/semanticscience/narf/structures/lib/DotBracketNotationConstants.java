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

package org.semanticscience.narf.structures.lib;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class used to define the commonly used opening and closing characters found in dot 
 * bracket notation (DBN).
 * @author William Greenwood
 * @since 1.6
 */

public final class DotBracketNotationConstants {
	
	/**
	 * Map of the common opening characters to its respective closing character for 
	 * dot bracket notation.
	 */
	public static final Map<Character, Character> OPENING_CHARACTERS = createOpeningCharacterMap();
	
	/**
	 * Map of the common closing characters to its respective opening character for 
	 * dot bracket notation.
	 */
	public static final Map<Character, Character> CLOSING_CHARACTERS = createClosingCharacterMap();
	
	/**
	 * Initialize the mapping of closing characters to opening characters
	 * @return a mapping of the closing characters to its respective opening character
	 */
    private static Map<Character, Character> createClosingCharacterMap(){
        Map<Character, Character> map = new LinkedHashMap<Character, Character>();

        //initialize all instances in which brackets are used as closing and opening characters
        map.put(')', '(');
        map.put('}', '{');
        map.put(']', '[');
        
        /*
         * initialize all instances in which letters are used as closing and opening characters
         * (i.e. a and A, b and B, etc.)
         */
        for(int i=65; i<=90; i++){
        	map.put((char)(65+32), (char)65);
        }
        
        return Collections.unmodifiableMap(map);
    }
    
    /*
     * Initialize the mapping of opening characters to closing characters
     */
    private static Map<Character, Character> createOpeningCharacterMap(){
        Map<Character, Character> map = new LinkedHashMap<Character, Character>();
        
        //initialize all instances in which brackets are used as opening and closing characters
        map.put('(', ')');
        map.put('{', '}');
        map.put('[', ']');
               
        /*
         * initialize all instances in which letters are used as opening and closing characters
         * (i.e. A and a, B and b, etc.)
         */
        for(int i=65; i<=90; i++){
        	map.put( (char)65, (char)(65+32));
        }
        
        return Collections.unmodifiableMap(map);
    }
}
