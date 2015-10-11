/**
 * Provides a list of methods to manipulate time for classes to use
 * 
 * @author Yan Chan Min Oo
 */
package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

	private static SimpleDateFormat _df = new SimpleDateFormat("EEEE: dd/MM/yy HH:mm 'GMT'Z");
	/**
	 * Converts a system time in string to long format.
	 * 
	 * @return System time in long format if the string is valid
	 */
	public static long sysStringToLongTime(String sysTimeInString) {
		try {
			return Integer.parseInt(sysTimeInString);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * 
	 * Converts a long time to date format
	 * @return date in sample format: Tuesday: 29/09/15 20:15 GMT+0800
	 */
	public static String getFormattedDate(long time) {
		
		if (time == 0L) {
			return "0";
		}
		
		Date date = new Date(time);
		String dateText = _df.format(date);
		return dateText;
	}
	
	/**
	 * Converts a date to long time
	 * @return long time
	 * ToDo: Refactor this
	 */
	public static long getLongTime(String sDate) {
		
		if (sDate.equals("0")) {
			return 0L;
		}
		
		try {
			Date date = (Date)_df.parse(sDate);
			return date.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}
