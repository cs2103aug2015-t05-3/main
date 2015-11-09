//@@author A0125496X
/**
 * Provides methods which allows String manipulation
 * 
 * @author Yan Chan Min Oo
 *
 */

package util;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	private static final String WHITESPACE = " ";

	/**
	 * Takes in a string, and returns the first word, delimited by a whitespace
	 * @param input
	 * 			The input to extract the word from
	 * @return
	 * 			The word if found, null otherwise
	 */
	public static String getFirstWord(String input) {
		assert input != null;
		assert !input.isEmpty();
		String[] tokens = input.split(WHITESPACE);
		
		if(tokens.length < 1){
			return null;
		}
		return input.split(WHITESPACE)[0];
	}

	/**
	 * Takes in a string, removes the first word and returns the rest
	 * @param input
	 * 			The input to remove the word from
	 * @return
	 * 			The remaining string after word is removed if word deletion is successful,
	 * 			null otherwise
	 */
	public static String removeFirstWord(String input) {
		String[] tokens = input.split(WHITESPACE, 2);
		if (tokens.length < 2) {
			return null;
		} else {
			return tokens[1];
		}
	}

	/**
	 * Takes in a search term (word), and look for it in a full string. Returns
	 * true if that word is found, false if it isn't
	 * @param searchIn
	 * 			The string to search in
	 * @param searchTerm
	 * 			The string to look for
	 * @return
	 * 			True if search term is found, false otherwise
	 */
	public static boolean containsWord(String searchIn, String searchTerm) {
		String regex = "\\b" + searchTerm + "\\b";

		Matcher m = Pattern.compile(regex).matcher(searchIn);

		return m.find();
	}

	/**
	 * Takes in a search term, look for it, then return the word following the
	 * search term
	 * 
	 * @param fullString
	 *            The full string to search in
	 * @param searchTerm
	 *            The search term
	 * @return The following word if searchTerm is found, null if it isn't
	 */
	public static String getWordAfter(String fullString, String searchTerm) {
		Matcher m = Pattern.compile("\\b" + Pattern.quote(searchTerm) + "\\b\\s\\b(.*){1}\\b").matcher(fullString);
		if (m.find()) {
			String tokens[] = m.group(1).split(WHITESPACE);
			return tokens[0];
		} else {
			return null;
		}
	}

	/**
	 * Takes in a search term, look for it, then return the entire string
	 * following the search term
	 * 
	 * @param fullString
	 *            The full string to search in
	 * @param searchTerm
	 *            The search term
	 * @return The following string if searchTerm is found, null if it isn't
	 */
	public static String getStringAfter(String fullString, String searchTerm) {
		Matcher m = Pattern.compile(Pattern.quote(searchTerm) + "(.*)").matcher(fullString);
		if (m.find()) {
			return m.group(1);
		} else {
			return null;
		}
	}

	/**
	 * Takes in a search term, look for it, then return the string following the
	 * search term, before stopAt
	 * 
	 * @param fullString
	 *            The full string to search in
	 * @param searchTerm
	 *            The search term NOTE: Support for multiple search terms using
	 *            | is not supported yet
	 * @param stopAt
	 *            The term to stop the string search at its first regex match, if
	 *            it is found
	 * @return The following string if searchTerm is found, null if it isn't
	 */
	public static String getStringAfter(String fullString, String searchTerm, String stopAt) {
		String [] tokens = fullString.split(" ");
		StringBuilder result = new StringBuilder();
		int i = 0;
		if(searchTerm != null && !searchTerm.isEmpty()){
			for(; i < tokens.length;i++){
				if(tokens[i].equals(searchTerm)){
					break;
				}
			}
			i++;
		}
	
		for(; i < tokens.length;i++){
			if(tokens[i].matches(stopAt)){
				break;
			} else {
				result.append(tokens[i] + " ");
			}
		}
		return result.length() == 0 ? null : result.toString();
	}
	
	/**
	 * Removes leading and trailing white spaces
	 * @param s
	 * 		The string to trim
	 * @return
	 * 		The trimmed string
	 */
	public static String trim(String s){
		if(s != null){
			return s.trim();
		} else {
			return null;
		}
	}
	
	/**
	 * Gets all occurrences of regex matches on a string
	 * @param fullString
	 * 			The string to analyse
	 * @param regex
	 * 			The regex to use
	 * @return
	 * 			A list of occurrences found. Null if no results are found
	 */
	public static String[] getOccurrences(String fullString, String regex){
		LinkedList<String> result = new LinkedList<>();
		Matcher m = Pattern.compile(regex).matcher(fullString);
		
		while(m.find()){
			result.add(m.group().trim());
		}
		
		return result.isEmpty() ? null : result.toArray(new String[result.size()]);
	}

}
