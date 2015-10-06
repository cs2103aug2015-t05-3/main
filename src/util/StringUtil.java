/**
 * Provides methods which allows String manipulation
 * 
 * @author Yan Chan Min Oo
 *
 */

package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	
	private static final String tokenDelim = " ";

	/**
	 * Takes in a string, and returns the first word, delimited by a whitespace
	 */
	public static String getFirstWord(String s) {
		assert s != null;
		assert !s.isEmpty();
		return s.split(tokenDelim)[0];
	}

	/**
	 * Takes in a string, removes the first word and returns the rest
	 */
	public static String removeFirstWord(String s) {
		String[] tokens = s.split(tokenDelim, 2);
		if (tokens.length < 2) {
			return null;
		} else {
			return tokens[1];
		}
	}
	
	/**
	 * Takes in a search term (word), and look for it in a full string. Returns true if
	 * that word is found, false if it isn't
	 */
	public static boolean containsWord(String searchIn, String searchTerm){
		String regex = "\\b" + searchTerm + "\\b";
		
		Matcher m = Pattern.compile(regex).matcher(searchIn);
		
		return m.find();
	}

}
