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
	public static String getFirstWord(String input) {
		assert input != null;
		assert !input.isEmpty();
		return input.split(tokenDelim)[0];
	}

	/**
	 * Takes in a string, removes the first word and returns the rest
	 */
	public static String removeFirstWord(String input) {
		String[] tokens = input.split(tokenDelim, 2);
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
	
	/**
	 * Takes in a search term (word), look for it, then return the word following the search term
	 * @param fullString 
	 * 		The full string to search in
	 * @param afterWord 
	 * 		The search term (a word) NOTE: Support for multiple search terms using | is not supported yet
	 * @return The following word if afterWord is found, null if it isn't
	 */
	public static String getWordAfter(String fullString, String afterWord){
		Matcher m = Pattern.compile("\\b"+afterWord+"\\b\\s\\b(.*){1}\\b").matcher(fullString);
		if(m.find()){
			String tokens[] = m.group(1).split(" ");
			return tokens[0];
		} else {
			return null;
		}
	}

}
