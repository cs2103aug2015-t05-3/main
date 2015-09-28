/**
 * Provides a list of methods to manipulate time for classes to use
 * 
 * @author Yan Chan Min Oo
 */
package util;

import java.util.Date;

public class TimeUtil {

	/**
	 * Converts a system time in string to long format.
	 * @return System time in long format if the string is valid
	 */
	public static long StringToLongTime(String sysTimeInString){
		try{
			return Integer.parseInt(sysTimeInString);
		} catch (NumberFormatException e){
			return 0;
		}
	}
	
	/**
	 * Returns date format in String
	 */
	public static String getDate(long time){
		Date date = new Date(time * 1000);
		return date.toString();
	}
}
