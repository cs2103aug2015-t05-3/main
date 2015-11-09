//@@author A0125496X
/**
 * Provides a list of methods to manipulate time for classes to use
 * 
 * @author Yan Chan Min Oo
 */
package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeUtil {

	private static SimpleDateFormat _df1 = new SimpleDateFormat("EEEE: dd/MM/yy HH:mm 'GMT'Z");
	private static SimpleDateFormat _uidf = new SimpleDateFormat("EEEE, dd MMM yy");
	private static Calendar now = Calendar.getInstance();
	private static Calendar temp = Calendar.getInstance();

	/**
	 * Converts a system time in string to long format.
	 * 
	 * @return System time in long format if the string is valid
	 */
	public static long sysStringToLongTime(String sysTimeInString) {
		try {
			return Long.parseLong(sysTimeInString);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	public static int getWeekDifference(long time) {
		now.setTimeInMillis(System.currentTimeMillis());
		temp.setTimeInMillis(time);
		return temp.get(Calendar.WEEK_OF_YEAR) - now.get(Calendar.WEEK_OF_YEAR);
	}

	public static int getDayDifference(long time) {
		now.setTimeInMillis(System.currentTimeMillis());
		temp.setTimeInMillis(time);
		if(now.get(Calendar.YEAR) == temp.get(Calendar.YEAR)){
			return temp.get(Calendar.DAY_OF_YEAR) - now.get(Calendar.DAY_OF_YEAR);
		}
		boolean isBefore = isBeforeNow(temp.getTimeInMillis());
		long diff = isBefore ? now.getTimeInMillis() - temp.getTimeInMillis() : 
			temp.getTimeInMillis() - now.getTimeInMillis();
		long daysDiff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
		if(isBefore){
			daysDiff = -daysDiff;
		}
		
		return (int)daysDiff;
	}
	
	public static boolean isBeforeNow(long time){
		now.setTimeInMillis(System.currentTimeMillis());
		temp.setTimeInMillis(time);
		return temp.getTime().before(now.getTime());
	}
	
	public static int compareMinTime(long t1, long t2){
		temp.setTimeInMillis(t1);
		temp.set(Calendar.SECOND, 0);
		temp.set(Calendar.MILLISECOND, 0);
		t1 = temp.getTimeInMillis();
		
		temp.setTimeInMillis(t2);
		temp.set(Calendar.SECOND, 0);
		temp.set(Calendar.MILLISECOND, 0);
		t2 = temp.getTimeInMillis();
		
		return Long.compare(t1, t2);
	}

	//@@author A0076510M
	/**
	 * 
	 * Converts a long time to date format
	 * 
	 * @return date in sample format: Tuesday: 29/09/15 20:15 GMT+0800
	 */
	public static String getFormattedDate(long time) {

		if (time == 0L) {
			return "0";
		}

		Date date = new Date(time);
		String dateText = _df1.format(date);
		return dateText;
	}


	/**
	 * Converts a date to long time
	 * 
	 * @return long time
	 */
	public static long getLongTime(String sDate) {

		if (sDate.equals("0")) {
			return 0L;
		}

		try {
			Date date = (Date) _df1.parse(sDate);
			return date.getTime();
		} catch (ParseException e) {
			return -1;
		}
	}

	/**
	 * 
	 * Converts a long time to date format for display.
	 * 
	 * @return date in sample format: Tuesday, 29 July 15
	 */
	public static String getUIFormattedDate(long time) {

		if (time == 0L) {
			return "0";
		}

		Date date = new Date(time);
		String dateText = _uidf.format(date);
		return dateText;
	}
}
