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

public class TimeUtil {

	private static SimpleDateFormat _df1 = new SimpleDateFormat("EEEE: dd/MM/yy HH:mm 'GMT'Z");
	private static SimpleDateFormat _df2 = new SimpleDateFormat("dd/MM/yy HH:mm");
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
	 * @return long time ToDo: Refactor this
	 */
	public static long getLongTime(String sDate) {

		if (sDate.equals("0")) {
			return 0L;
		}

		try {
			Date date = (Date) _df1.parse(sDate);
			return date.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 
	 * Converts a long time to date format for display.
	 * 
	 * @return date in sample format: 29/09/15 20:15
	 */
	public static String getUIFormattedDate(long time) {

		if (time == 0L) {
			return "0";
		}

		Date date = new Date(time);
		String dateText = _df2.format(date);
		return dateText;
	}

	public static int getWeekDifference(long time) {
		now.setFirstDayOfWeek(Calendar.MONDAY);
		temp.setFirstDayOfWeek(Calendar.MONDAY);
		now.setTimeInMillis(System.currentTimeMillis());
		temp.setTimeInMillis(time);
		return temp.get(Calendar.WEEK_OF_YEAR) - now.get(Calendar.WEEK_OF_YEAR);
	}

	public static int getDayDifference(long time) {
		now.setTimeInMillis(System.currentTimeMillis());
		temp.setTimeInMillis(time);
		int daysDiff = 0;
		//if(temp.get(Calendar.YEAR) > )
		return temp.get(Calendar.DAY_OF_YEAR) - now.get(Calendar.DAY_OF_YEAR);
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
}
