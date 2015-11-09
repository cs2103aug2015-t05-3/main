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
	// Return values upon resolving time input
	public static final long TIME_NOTSPECIFIED = 0;
	public static final long TIME_INVALIDFORMAT = -1;

	// Supported date formats for user input, and their maximum allowable
	// characters for parsing with that pattern
	private static final String[][] PATTERN_IN_TIME = { { "hha", "4" }, { "HHmm", "4" } };
	private static final String[][] PATTERN_IN_DAY = { { "Ehha", "7" }, { "EHHmm", "7" } };
	private static final String[][] PATTERN_IN_MONTHDAY = { { "ddMMMhha", "9" }, { "ddMMMHHmm", "9" },
			{ "MMMddhha", "9" }, { "MMMddHHmm", "9" }, { "ddMMhha", "8" }, { "ddMMHHmm", "8" } };
	private static final String[][] PATTERN_IN_DATE = { { "ddMMMyyhha", "11" }, { "ddMMMyyHHmm", "11" },
			{ "ddMMyyhha", "10" }, { "ddMMyyHHmm", "10" } };
	// Indexes for the above date formats
	private static final int INDEX_PATTERN = 0;
	private static final int INDEX_MAXCHAR = 1;

	// Supported date formats for output
	private static final String PATTERN_OUT_TIME = "HH:mm";
	private static final String PATTERN_OUT_DAYTIME = "E HH:mm";
	private static final String PATTERN_OUT_DATETIME = "dd MMM HH:mm";

	// Messages for relative dates
	private static final String FORMAT_TMR = "Tmr %1$s";
	private static final String FORMAT_TODAY = "Today %1$s";
	private static final String FORMAT_YTD = "Ytd %1$s";
	private static final String FORMAT_NEXTWEEK = "Next %1$s";
	private static final String FORMAT_ENDDATE = "By %1$s %2$s";
	private static final String FORMAT_DUEIN = "(In %1$s day(s))";
	private static final String FORMAT_OVERDUE = "(%1$s day(s) ago)";
	private static final String FORMAT_STARTENDDATE = "%1$s to %2$s";
	/*
	 * Variables
	 */
	private static TimeProcessor timeP; // Keep a copy of itself
	private Calendar now; // Keeps the current time
	private Calendar temp; // To be used for computing dates from input/output
	private static SimpleDateFormat sdf;

	private TimeProcessor() {
		sdf = new SimpleDateFormat();
		sdf.setTimeZone(TimeZone.getDefault());
		sdf.setLenient(false);
		now = Calendar.getInstance(TimeZone.getDefault());
		temp = Calendar.getInstance(TimeZone.getDefault());
	}

	/**
	 * Converts a user date into a system time
	 * 
	 * @param time
	 *            The time to convert
	 * @return The system time in long after parsing the user's system time. 0
	 *         if the time is not specified, -1 if the time cannot be parsed due
	 *         to invalid format
	 */
	public long resolveTime(String time) {

		// Allow users to reset/remove time
		if (time.equals(Long.toString(TIME_NOTSPECIFIED))) {
			return TIME_NOTSPECIFIED;
		}

		time = reformatDate(time); // Convert the user's time to a format which we can process easily

		long parsedTime = TIME_INVALIDFORMAT;

		// Try to parse with each date format
		for (int i = 1; i <= 4; i++) {
			switch (i) {
			case 1:
				parsedTime = parseFullDate(time);
				break;
			case 2:
				parsedTime = parseMonthDate(time);
				break;
			case 3:
				parsedTime = parseDay(time);
				break;
			case 4:
				parsedTime = parseTime(time);
				break;
			}

			// Check if the date have been successfully parsed
			if (parsedTime != TIME_INVALIDFORMAT) {
				return parsedTime;
			}
		}

		return TIME_INVALIDFORMAT; // Unable to parse date
	}

	/**
	 * Pads a date with zeros in fields when necessary, and removes white
	 * spaces, commas, slash, and colon, and dot
	 * 
	 * @param date
	 *            The date to convert
	 * @return The converted date
	 */
	private String reformatDate(String date) {
		String dateDelimit = "\\s|,|/|:|\\."; // Tokens to delimit fields and remove
		String fields[] = date.split(dateDelimit); // The fields to pad zeros with if necessary
		StringBuilder newField = new StringBuilder(); // The result

		for (String aField : fields) {

			// Left pad a zero in cases where single/odd digits are found
			if ((aField.length() == 1 && aField.matches("\\d")) || (aField.length() == 3 && aField.matches("\\d{3}"))
					|| (aField.length() == 4 && aField.matches("\\d[a-zA-Z]{3}"))) {
				aField = "0" + aField;
			}

			newField.append(aField);
		}
		return newField.toString();
	}

	/**
	 * Parses a date containing <Day of month><Month><Year><Time>
	 * 
	 * @param time
	 *            The time to parse
	 * @return System time in long if date is parsed, -1 if it isn't
	 */
	private long parseFullDate(String time) {

		// Try to parse with each pattern
		for (String[] pattern : PATTERN_IN_DATE) {
			
			// # of char at input must be lesser than the allowed # of char for that pattern
			if (time.length() > Integer.parseInt(pattern[INDEX_MAXCHAR])){
				continue;
			}
			
			sdf.applyPattern(pattern[INDEX_PATTERN]);

			try {
				temp.setTime(sdf.parse(time)); // Convert directly

				return temp.getTimeInMillis();
			} catch (ParseException e) { // Not this format
			} 
			
		}
		
		return TIME_INVALIDFORMAT;
	}

	/**
	 * Parse a date containing <Day of month><Month><Time> or <Month in
	 * text><Day of month><Time>
	 * 
	 * @param time
	 *            The time to parse
	 * @return System time in long if date is parsed, -1 if it isn't
	 */
	private long parseMonthDate(String time) {

		// Try to parse with each pattern
		for (String[] pattern : PATTERN_IN_MONTHDAY) {
			
			// # of char at input must be lesser than the allowed # of char for that pattern
			if (time.length() > Integer.parseInt(pattern[INDEX_MAXCHAR])){
				continue;
			}

			sdf.applyPattern(pattern[INDEX_PATTERN]);

			try {
				temp.setTime(sdf.parse(time));
				temp.set(Calendar.YEAR, now.get(Calendar.YEAR)); // Set the year since it's not specified in the input

				return temp.getTimeInMillis();
			} catch (ParseException e) { // Not this format
			} 
			
		}

		return TIME_INVALIDFORMAT;
	}

	/**
	 * Parse a date containing <Day of week><Time>. The date returned will be on
	 * the following specified day after the current day. Eg, if Wed 2200 is
	 * entered on a Wed, the next coming Wed will be returned.
	 * 
	 * @param time
	 *            The time to parse
	 * @return System time in long of the following specified day if date is
	 *         parsed, -1 if it isn't
	 */
	private long parseDay(String time) {

		// Try to parse with each pattern
		for (String[] pattern : PATTERN_IN_DAY) {

			// # of char at input must be lesser than the allowed # of char for that pattern
			if (time.length() > Integer.parseInt(pattern[INDEX_MAXCHAR])) {
				continue;
			}
				
			sdf.applyPattern(pattern[INDEX_PATTERN]);

			try {
				// Extract the day of week and time
				temp.setTime(sdf.parse(time));
				int day = temp.get(Calendar.DAY_OF_WEEK);
				int hour = temp.get(Calendar.HOUR_OF_DAY);
				int min = temp.get(Calendar.MINUTE);

				temp.setTimeInMillis(System.currentTimeMillis()); // Get the current time

				// Fast forward to the day specified
				do {
					temp.add(Calendar.DAY_OF_YEAR, 1);
				} while (temp.get(Calendar.DAY_OF_WEEK) != day);

				// Set the time
				temp.set(Calendar.HOUR_OF_DAY, hour);
				temp.set(Calendar.MINUTE, min);
				temp.set(Calendar.SECOND, 0);
				temp.set(Calendar.MILLISECOND, 0);

				return temp.getTimeInMillis();
			} catch (ParseException e) { // Not this format
			}
			
		}

		return TIME_INVALIDFORMAT;
	}

	/**
	 * Parse a date containing <Time>. The date returned will be on the same day
	 * if the time specified is after the current time, or on the next day if it
	 * is before the current time.
	 * 
	 * @param time
	 *            The time to parse
	 * @return System time in long if date is parsed, -1 if it isn't
	 */
	private long parseTime(String time) {

		// Try to parse with each pattern
		for (String[] pattern : PATTERN_IN_TIME) {
			
			// # of char at input must be lesser than the allowed # of char for that pattern
			if (time.length() > Integer.parseInt(pattern[INDEX_MAXCHAR])) {
				continue;
			}

			sdf.applyPattern(pattern[INDEX_PATTERN]);

			try {
				Date dtime = sdf.parse(time);

				temp.setTimeInMillis(System.currentTimeMillis()); // Get the current time

				// Set the specified time of the day
				temp.set(Calendar.HOUR_OF_DAY, dtime.getHours());
				temp.set(Calendar.MINUTE, dtime.getMinutes());
				temp.set(Calendar.SECOND, 0);
				temp.set(Calendar.MILLISECOND, 0);

				// Fast forward to the next day if the date is before the current time
				if (TimeUtil.isBeforeNow(temp.getTimeInMillis())) {
						temp.add(Calendar.DAY_OF_YEAR, 1);
				}
					
				return temp.getTimeInMillis();
			} catch (ParseException e) {// Not this format
			} 
			
		}

		return TIME_INVALIDFORMAT;
	}

	/**
	 * Converts a date in system time to a formatted date
	 * 
	 * @param endTime
	 *            The end time to convert
	 * @return The formatted date
	 */
	public String getFormattedDate(long endTime) {

		int daysDiff = TimeUtil.getDayDifference(endTime);
		String extraMsg;

		if (daysDiff < 0) { // Overdue
			extraMsg = String.format(FORMAT_OVERDUE, -daysDiff);
		} else if (daysDiff == 0) { // Same day
			extraMsg = "";
		} else { // Due in the future
			extraMsg = String.format(FORMAT_DUEIN, daysDiff);
		}

		return String.format(FORMAT_ENDDATE, getRelativeDate(endTime), extraMsg);
	}

	/**
	 * Converts the start and end date in system time to a formatted date
	 * 
	 * @param startTime
	 *            The start time to convert
	 * @param endTime
	 *            The end time to convert
	 * @return The formatted date
	 */
	public String getFormattedDate(long startTime, long endTime) {
		return String.format(FORMAT_STARTENDDATE, getRelativeDate(startTime), getRelativeDate(endTime));
	}

	/**
	 * Converts a date in system time to its relative date from the current time
	 * 
	 * @param time
	 *            The time to convert
	 * @return The formatted date
	 */
	private String getRelativeDate(long time) {
		temp.setTimeInMillis(time);
		int dayDifference = TimeUtil.getDayDifference(time); // Get the day
																// difference
		sdf.applyPattern(PATTERN_OUT_TIME); // Use the pattern suitable for
											// displaying dates within 1 day
											// away

		// Check if the date is within 1 day away
		if (dayDifference == 0) { // Same day
			return String.format(FORMAT_TODAY, sdf.format(temp.getTime()));
		} else if (dayDifference == 1) { // Next day
			return String.format(FORMAT_TMR, sdf.format(temp.getTime()));
		} else if (dayDifference == -1) {// Yesterday
			return String.format(FORMAT_YTD, sdf.format(temp.getTime()));
		}

		int weekDifference = TimeUtil.getWeekDifference(time);// Get the week
																// difference
		sdf.applyPattern(PATTERN_OUT_DAYTIME); // Use the pattern suitable for
												// displaying dates within 1
												// week away

		// Check if the date is within this week or next week
		if (weekDifference == 0) { // This week
			return sdf.format(temp.getTime());
		} else if (weekDifference == 1) { // Next week
			return String.format(FORMAT_NEXTWEEK, sdf.format(temp.getTime()));
		}

		sdf.applyPattern(PATTERN_OUT_DATETIME); // Use the default pattern to
												// display dates

		return sdf.format(temp.getTime());
	}

	/**
	 * Returns an instance of itself. Allows only 1 instance of this class
	 * 
	 * @return The current instance
	 */
	public static TimeProcessor getInstance() {
		if (timeP == null) {
			timeP = new TimeProcessor();
		}
		return timeP;
	}

}
