package de.accso.accelerated.accounting.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.accso.accelerated.accounting.util.DateUtil;

public class RecordedHour implements Comparable<RecordedHour> {

	// TODO: SB nach DateUtil auslagern!
	public static final DateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
	public static final DateFormat TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	public static final DateFormat DATE_FORMATTER_LABEL = DateFormat.getDateInstance(DateFormat.FULL, Locale.GERMANY);
	public static final DateFormat TIME_FORMATTER_LABEL = new SimpleDateFormat("HH:mm");

	private int id;
	private Date date;
	private Calendar time;
	private String name;
	private String color;

	public RecordedHour(int id, String date, String time, String name, String color) {
		try {
			this.id = id;
			this.date = DATE_FORMATTER.parse(date);
			this.time = DateUtil.convertDBTimeFormatToCalendar(time);
			this.name = name;
			this.color = color;
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public int getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}

	public Calendar getTime() {
		return time;
	}

	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
	}

	@Override
	public int compareTo(RecordedHour other) {
		if(this.date != null && other.date != null) {
			if(this.date.before(other.date)) {
				return -1;
			} else if(this.date.after(other.date)) {
				return 1;
			}
		}
		
		return 0;
	}
	
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		if(this.name != null && this.time != null) { 
			buffer.append(name);
			buffer.append(" ");
			buffer.append(DateUtil.convertCalendarToGermanTime(time));
			
			return buffer.toString();
		}
		
		return super.toString();
	}
}
