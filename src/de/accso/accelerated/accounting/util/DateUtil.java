package de.accso.accelerated.accounting.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.text.format.DateFormat;
import android.util.Log;


/**
 * Date Utility class.
 * 
 * @author Braun
 *
 */
public class DateUtil {
	
	private static final String LOG_TAG = "DateUtil";
	
	
	public static final String GERMAN_DAY_FORMAT_LONG = "EEEEEEE dd.MM.yyyy";
	
	public static final String GERMAN_TIME_FORMAT_SHORT = "HH:mm";
	
	public static final String DB_DAY_FORMAT = "yyyy-MM-dd";
	
	public static final String DB_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	
	public static final String JSON_DAY_FORMAT = "yyyy-MM-dd";
	
	
	
	public static final SimpleDateFormat GERMAN_DAY_FORMAT_LONG_FORMATTER = new SimpleDateFormat(GERMAN_DAY_FORMAT_LONG, Locale.GERMANY);
	
	public static  final SimpleDateFormat DB_TIME_FORMATTER = new SimpleDateFormat(DB_TIME_FORMAT);
	
	public static final SimpleDateFormat GERMAN_TIME_FORMATTER = new SimpleDateFormat(GERMAN_TIME_FORMAT_SHORT, Locale.GERMANY);
	
	
	public static String convertGermanDayStringToDBString(String dayString) {
		if(dayString == null) {
			return null;
		}
		
		try {
			Date day = GERMAN_DAY_FORMAT_LONG_FORMATTER.parse(dayString);
			CharSequence dbDayString = DateFormat.format(DateUtil.DB_TIME_FORMAT, day);
			
			return dbDayString.toString();
		} catch (ParseException e) {
			Log.e(LOG_TAG, "FAiled to parse day string", e);
		}
		
		return "";
	}

	public static Calendar convertDBTimeFormatToCalendar(String dbTimeString) {
		if(dbTimeString == null) {
			return null;
		}
		
		try {
			Date date = DB_TIME_FORMATTER.parse(dbTimeString);
			if(date != null) {
				Calendar cal = GregorianCalendar.getInstance(Locale.GERMANY);
				cal.setTime(date);
				return cal;
			}
		} catch (ParseException e) {
			Log.e(LOG_TAG, "FAiled to parse DB time string", e);
		}
		
		
		return null;
	}
	
	
	public static String convertCalendarToGermanTime(Calendar time) {
		String timeString = GERMAN_TIME_FORMATTER.format(time.getTime());
		
		return timeString;
	}
	
	public static String convertDateToGermanDateString(Date date) {
		CharSequence dbDayString = DateFormat.format(DateUtil.GERMAN_DAY_FORMAT_LONG, date);
		
		return dbDayString.toString();
	}

	public static String convertCalendarToDBString(Calendar day) {
		CharSequence dbDayString = DateFormat.format(DateUtil.DB_DAY_FORMAT, day.getTime());
		
		return dbDayString.toString();
	}
	
	public static String convertToJSONDate(Date date) {
		if(date == null) {
			return "";
		}
		
		CharSequence jsonDayString = DateFormat.format(JSON_DAY_FORMAT, date);
		
		return jsonDayString.toString();
	}
}
