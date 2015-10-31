/**
 * Resolves time from user input
 * 
 */
package parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
	private static final String[] PATTERN_TIME = {"hha","HHmm"};
	private static final String[] PATTERN_DAY = {"Ehha","EHHmm"};
	private static final String[] PATTERN_MONTHDAY = {"ddMMMhha","ddMMMHHmm",
			"MMMddhha","MMMddHHmm","ddMMhha","ddMMHHmm","MMddhha","MMddHHmm"};
	/*
	 * Variables
	 */
	private static TimeProcessor timeP;
	
	private TimeProcessor(){
		sdf = new SimpleDateFormat();
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
	}
	
	public long resolveTime(String time){
		time = time.replaceAll("\\s|,", ""); // Remove whitespaces and commas
		Date parsedTime = null;
		
		for(String pattern : PATTERN_TIME){
			sdf.applyPattern(pattern);
			try{
				parsedTime = sdf.parse(time);
				setCurrentDay(parsedTime);
				setCurrentMonth(parsedTime);
				setCurrentYear(parsedTime);
				return parsedTime.getTime();
			} catch (ParseException e){ }
		}
		
		for(String pattern : PATTERN_DAY){
			sdf.applyPattern(pattern);
			try{
				parsedTime = sdf.parse(time);
				setCurrentMonth(parsedTime);
				setCurrentYear(parsedTime);
				return parsedTime.getTime();
			} catch (ParseException e){ }
		}
		
		for(String pattern : PATTERN_MONTHDAY){
			sdf.applyPattern(pattern);
			try{
				parsedTime = sdf.parse(time);
				setCurrentYear(parsedTime);
				return parsedTime.getTime();
			} catch (ParseException e){ }
		}
		
		return TIME_INVALID;
	}
	
	public String getFormattedDate(long endTime){
		return "By Wed 2359";
	}
	
	public String getFormattedDate(long startTime, long endTime){
		return "Wed 2359 to Thur 2359";
	}
	
	private void setCurrentDay(Date date){
		date.setDate(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
	}
	
	private void setCurrentMonth(Date date){
		date.setMonth(Calendar.getInstance().get(Calendar.MONTH));
	}
	
	private void setCurrentYear(Date date){
		date.setYear(Calendar.getInstance().get(Calendar.YEAR) - 1900);
	}
	
	public static TimeProcessor getInstance(){
		if(timeP == null){
			timeP = new TimeProcessor();
		}
		return timeP;
	}

}
