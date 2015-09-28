/**
 * Provides a list of methods to manipulate time for classes to use
 * 
 * @author Yan Chan Min Oo
 */
package util;

public class TimeUtil {

	/*
	 * Converts a system time in string to long format. Return the number in long if given string is valid,
	 * false if it is not.
	 */
	public static long StringToLongTime(String sysTimeInString){
		try{
			return Integer.parseInt(sysTimeInString);
		} catch (NumberFormatException e){
			return 0;
		}
	}
}
