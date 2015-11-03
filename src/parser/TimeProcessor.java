/**
 * Resolves time from user input
 * 
 * Examples of supported date formats (after removing white spaces, comma, and slash :
 * Time for today: 4pm / 2359 (24 hour format)
 * Next specified day with time: Wed <time> / Wednesday <time>
 * Date with month and time: 24 Jul <time> / Jul 24 <time> / 2407 <time> 
 * Date with month, year and time: 24 Jul 15 <time> / 240715 <time>
 * 
 */
package parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import util.TimeUtil;

/**
 * @author Yan Chan Min Oo
 *
 */
public class TimeProcessor {
	
	/*
	 * Constants
	 */
	public static final long TIME_INVALID = 0;
	private static SimpleDateFormat sdf;
	private static final String[] PATTERN_IN_TIME = {"hha","HHmm"};
	private static final String[] PATTERN_IN_DAY = {"Ehha","EEEEhha","EHHmm","EEEEHHmm"};
	private static final String[] PATTERN_IN_MONTHDAY = {"ddMMMhha","ddMMMHHmm",
			"MMMddhha","MMMddHHmm","ddMMhha","ddMMHHmm"};
	private static final String[] PATTERN_IN_DATE = {"ddMMMyyhha","ddMMMyyHHmm","ddMMyyhha","ddMMyyHHmm"}; 
	private static final String PATTERN_OUT_TIME = "HH:mm";
	private static final String PATTERN_OUT_DAYTIME = "E HH:mm";
	private static final String PATTERN_OUT_DATETIME = "dd MMM HH:mm";
	private static final String FORMAT_TMR = "Tmr %1$s";
	private static final String FORMAT_TODAY = "Today %1$s";
	private static final String FORMAT_YTD = "Ytd %1$s";
	private static final String FORMAT_NEXTWEEK = "Next %1$s";
	private static final String FORMAT_LASTWEEK = "Last %1$s";
	private static final String FORMAT_ENDDATE = "By %1$s (%2$s)";
	private static final String FORMAT_DUEIN = "In %1$s day(s)";
	private static final String FORMAT_OVERDUE = "Over by %1$s day(s)";
	private static final String FORMAT_STARTENDDATE = "%1$s to %2$s";
	/*
	 * Variables
	 */
	private static TimeProcessor timeP;
	private Calendar now;
	private Calendar temp;
	
	private TimeProcessor(){
		sdf = new SimpleDateFormat();
		sdf.setTimeZone(TimeZone.getDefault());
		now = Calendar.getInstance(TimeZone.getDefault());
		temp = Calendar.getInstance(TimeZone.getDefault());
		now.setFirstDayOfWeek(Calendar.MONDAY);
		temp.setFirstDayOfWeek(Calendar.MONDAY);
	}
	
	public long resolveTime(String time){
		time = time.replaceAll("\\s|,|/", ""); // Remove whitespaces and commas and slash
		
		if(time.equals(TIME_INVALID)){
			return TIME_INVALID;
		}
		
		for(String pattern : PATTERN_IN_DATE){
			sdf.applyPattern(pattern);
			try{
				temp.setTime(sdf.parse(time));
				System.out.println("year");
				return temp.getTimeInMillis();
			} catch (ParseException e){ }
		}
		
		for(String pattern : PATTERN_IN_MONTHDAY){
			sdf.applyPattern(pattern);
			try{
				temp.setTime(sdf.parse(time));
				temp.set(Calendar.YEAR,now.get(Calendar.YEAR));
				System.out.println("month");
				return temp.getTimeInMillis();
			} catch (ParseException e){ }
		}
		
		for(String pattern : PATTERN_IN_DAY){
			sdf.applyPattern(pattern);
			try{
				temp.setTime(sdf.parse(time));
				int day = temp.get(Calendar.DAY_OF_WEEK);
				int hour = temp.get(Calendar.HOUR_OF_DAY);
				int min = temp.get(Calendar.MINUTE);

				temp.setTimeInMillis(System.currentTimeMillis());
				do {
					temp.add(Calendar.DAY_OF_YEAR, 1);
				} while(temp.get(Calendar.DAY_OF_WEEK) != day);
				temp.set(Calendar.HOUR_OF_DAY, hour);
				System.out.println(hour +" "+min);
				temp.set(Calendar.MINUTE, min);
				temp.set(Calendar.SECOND, 0);
				temp.set(Calendar.MILLISECOND, 0);
				System.out.println("day");
				
				return temp.getTimeInMillis();
			} catch (ParseException e){ }
		}
		
		for(String pattern : PATTERN_IN_TIME){
			sdf.applyPattern(pattern);
			try{
				Date dtime = sdf.parse(time);
				temp.setTimeInMillis(System.currentTimeMillis());
				temp.set(Calendar.HOUR_OF_DAY, dtime.getHours());
				temp.set(Calendar.MINUTE, dtime.getMinutes());
				temp.set(Calendar.SECOND, 0);
				temp.set(Calendar.MILLISECOND, 0);
				System.out.println("time");
				return temp.getTimeInMillis();
			} catch (ParseException e){ }
		}
		
		return TIME_INVALID;
	}
	
	public String getFormattedDate(long endTime){
		int daysDiff = TimeUtil.getDayDifference(endTime);
		String extraMsg;
		if(daysDiff < 0){ // Overdue
			extraMsg = String.format(FORMAT_OVERDUE, -daysDiff);
		} else {
			extraMsg = String.format(FORMAT_DUEIN, daysDiff);
		}
		return String.format(FORMAT_ENDDATE, getRelativeDate(endTime), extraMsg);
	}
	
	public String getFormattedDate(long startTime, long endTime){
		return String.format(FORMAT_STARTENDDATE, getRelativeDate(startTime), getRelativeDate(endTime));
	}
	
	private String getRelativeDate(long time){
		temp.setTimeInMillis(time);
		int dayDifference = TimeUtil.getDayDifference(time);
		sdf.applyPattern(PATTERN_OUT_TIME);
		
		if(dayDifference == 0){
			return String.format(FORMAT_TODAY, sdf.format(temp.getTime()));
		} else if (dayDifference == 1){
			return String.format(FORMAT_TMR, sdf.format(temp.getTime()));
		} else if (dayDifference == -1){
			return String.format(FORMAT_YTD, sdf.format(temp.getTime()));
		}
		
		int weekDifference = TimeUtil.getWeekDifference(time);
		sdf.applyPattern(PATTERN_OUT_DAYTIME);
		if(weekDifference == 0) {
			return sdf.format(temp.getTime());
		} else if (weekDifference == 1){
			return String.format(FORMAT_NEXTWEEK,sdf.format(temp.getTime()));
		} else if (weekDifference == -1) {
			return String.format(FORMAT_LASTWEEK, sdf.format(temp.getTime()));
		}
		
		sdf.applyPattern(PATTERN_OUT_DATETIME);
		return sdf.format(temp.getTime());
	}
	
	public static TimeProcessor getInstance(){
		if(timeP == null){
			timeP = new TimeProcessor();
		}
		return timeP;
	}

}
