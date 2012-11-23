package de.accso.accelerated.accounting.data.helper;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import de.accso.accelerated.accounting.data.AggregatedRecordedHour;
import de.accso.accelerated.accounting.data.AggregatedRecordedHoursOfDay;
import de.accso.accelerated.accounting.data.RecordedHour;
import de.accso.accelerated.accounting.storage.AcceleratedAccountingProvider;
import de.accso.accelerated.accounting.util.DateUtil;



/**
 * TODO: SB Javadoc
 * 
 * App-wide singleton!
 * 
 * @author Braun
 *
 */
public class SBRecordedHoursHelper {
	
	// get all recorded hours (vollständig im hs lassen?)
	// zusammenfassen zu blöcken runden auf viertel stunden)
	// zugriff statisch ermöglichen ggf. bei onScroll nachladen! wie kann man das machen??
	
	private static SBRecordedHoursHelper instance;
	
	private Context context;
	
	//private Map<Calendar, AggregatedRecordedHoursOfDay> recordedHoursByDay = new HashMap<Calendar, AggregatedRecordedHoursOfDay>();
	
	private SBRecordedHoursHelper(Context ctx) {
		this.context = ctx;
	}


	// TODO: SB hier evtl. LRU-Caching implementieren...
	// Map ist potentielles Memory-Leak!
	
	public List<AggregatedRecordedHoursOfDay> getRecordedHoursOfDays(List<Calendar> days) {
		List<AggregatedRecordedHoursOfDay> result = new ArrayList<AggregatedRecordedHoursOfDay>();
		
		for(Calendar day: days) {
			AggregatedRecordedHoursOfDay aggHours = null;
			
//			if(!recordedHoursByDay.containsKey(day)) {
				aggHours = this.getAggregatedHoursOfDay(day);
//			} else {
//				aggHours = this.recordedHoursByDay.get(day);
//			}
			
			if(aggHours != null) {
				result.add(aggHours);
			}
		}
		
		return result;
	}
	
	
	private AggregatedRecordedHoursOfDay getAggregatedHoursOfDay(Calendar day) {
		// TODO SB
		// get all recorded hour records of that day
		// aggregate them
		// add result to map
		// return result
		
		String dbDayString = DateUtil.convertCalendarToDBString(day);
		
		Uri uri = Uri.withAppendedPath(AcceleratedAccountingProvider.CONTENT_URI, AcceleratedAccountingProvider.RECORDED_HOURS_CONTENT_DIRECTORY);
		String where = AcceleratedAccountingProvider.COLUMN_RECORDED_HOURS_DATE + " == ?";
		String[] whereArgs = new String [] {dbDayString};
		String orderBy = AcceleratedAccountingProvider.COLUMN_RECORDED_HOURS_TIME;
		List<RecordedHour> recHours =  getList(context, uri, null, where, whereArgs, orderBy);
		
		// do aggregate
		List<AggregatedRecordedHour> aggHours = doAggregate(recHours);
		AggregatedRecordedHoursOfDay aggHoursOfDay = new AggregatedRecordedHoursOfDay();
		aggHoursOfDay.setAggRecoredHours(aggHours);
		aggHoursOfDay.setDayString(DateUtil.convertDateToGermanDateString(day.getTime()));
		
		
		return aggHoursOfDay;
	}

	
	
	// TODO: SB write unit tests...
	private List<AggregatedRecordedHour> doAggregate(List<RecordedHour> recHours) {
		List<AggregatedRecordedHour> aggHours = new ArrayList<AggregatedRecordedHour>();
		//Collections.sort(recHours);
		//Collections.reverse(recHours);
		
		// aufeinanderfolgende intervalle bestimmen
		// max. distanz zwischen 2 datensätzen <= 15 Minuten -> aufeinanderfolgend
		// intervall muss mindestens aus einem zeitraum >= 15 Minuten bestehen, ansonsten
		// wird es verworfen
		List<Interval> intervals = this.getRecordedHourIntervals(recHours); // TODO: SB hier genügt eig. die erste und die 
		// letzte..
		
		
		// rules
		// quadrant bestimmen
		// aggregated recorded hour aus erstem und letztem element des intervalles bilden mit gerundeten zeiten
		for(Interval interval: intervals)  {
			String startTimeString = getRoundedTimeString(interval.intervalStart.getTime());
			String endTimeString = getRoundedTimeString(interval.intervalEnd.getTime());
			String locationName = interval.intervalStart.getName();
			
			// TODO: SB get workplace information into recorded hour
			boolean isWorkingPlace = true;
			
			
			AggregatedRecordedHour aggHour = new AggregatedRecordedHour();
			aggHour.setStartTimeString(startTimeString);
			aggHour.setEndTimeString(endTimeString);
			aggHour.setLocationName(locationName);
			aggHour.setWorkingPlace(isWorkingPlace);
			aggHours.add(aggHour);
		}
		
		return aggHours;
	}


	private String getRoundedTimeString(Calendar time) {
		// quadrant bestimmen
		// aggregated recorded hour aus erstem und letztem element des intervalles bilden mit gerundeten zeiten
		// TODO: SB this is only the simple version...
		
		int hours = time.get(Calendar.HOUR_OF_DAY);
		int minutes = time.get(Calendar.MINUTE);
		
		if(minutes <= 7) {
			minutes = 0;
		} else if(minutes > 7 && minutes <= 22) {
			minutes = 15;
		} else if(minutes > 22 && minutes <= 37) {
			minutes = 30;
		} else if(minutes > 37 && minutes <= 52) {
			minutes = 45;
		} else  {
			hours++;
			minutes = 0;
		}
		
		time.set(Calendar.HOUR, hours);
		time.set(Calendar.MINUTE, minutes);
		
		return DateUtil.convertCalendarToGermanTime(time);
	}


	// TOOD: SB chek location is the same!!!
	private List<Interval> getRecordedHourIntervals(List<RecordedHour> recHours) {
		List<Interval> intervals = new ArrayList<SBRecordedHoursHelper.Interval>();
		
		if (recHours.size() > 0) {
			RecordedHour currentIntervalStart = recHours.get(0);
			RecordedHour previousIntervalElm = recHours.get(0);
			RecordedHour currentIntervalEnd = null;
			
			for (int i = 1; i < recHours.size(); i++) {
				RecordedHour currentIntervalElm = recHours.get(i);
				
				if ((getDiffInMinutes(previousIntervalElm, currentIntervalElm) < 15)
						&& previousIntervalElm.getName().equals(
								currentIntervalElm.getName())) {
					// continue
					if(i != (recHours.size() - 1)) {
						previousIntervalElm = currentIntervalElm;
					} else {
						// last elm
						currentIntervalEnd = currentIntervalElm;
						createNewInterval(intervals, currentIntervalStart,
								currentIntervalEnd);	
					}
				} else { 
					currentIntervalEnd = previousIntervalElm;
					createNewInterval(intervals, currentIntervalStart,
							currentIntervalEnd);
					
					// start next interval...
					currentIntervalStart = currentIntervalElm;
					previousIntervalElm = currentIntervalElm;
					currentIntervalEnd = null;
				}
			}
		}
		
		return intervals;
	}


	private void createNewInterval(List<Interval> intervals,
			RecordedHour currentIntervalStart, RecordedHour currentIntervalEnd) {
		if(getDiffInMinutes(currentIntervalStart, currentIntervalEnd) >= 15) {
			Interval interval = new Interval();
			interval.intervalStart = currentIntervalStart;
			interval.intervalEnd = currentIntervalEnd;
			intervals.add(interval);
		}
	}


	private long getDiffInMinutes(RecordedHour previousIntervalElm,
			RecordedHour currentIntervalElm) {
		if (previousIntervalElm == null
				|| previousIntervalElm.getTime() == null
				|| currentIntervalElm == null
				|| currentIntervalElm.getTime() == null) {
			return 0;
		}

		long time1 = previousIntervalElm.getTime().getTimeInMillis();
		long time2 = currentIntervalElm.getTime().getTimeInMillis();

		long diff = Math.abs(time2 - time1);

		long diffInMinutes = Math.round(diff / 1000 / 60);

		return diffInMinutes;
	}


	private static List<RecordedHour> getList(Context context, Uri uri, String[] projection, String selection, String [] selectionArgs, String sortOrder){
		Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
	    List<RecordedHour> recordedHours = new ArrayList<RecordedHour>();
	    
		if (cursor != null) {
			cursor.moveToFirst();
			
			do {
				recordedHours.add(createRecordedHour(cursor));
			} while (cursor.moveToNext());
		}
	    
		return recordedHours;
	}
	
	private static RecordedHour createRecordedHour(Cursor cursor){
		int id = cursor.getInt(cursor.getColumnIndex(AcceleratedAccountingProvider.COLUMN_RECORDED_HOURS_ID));
		String date = cursor.getString(cursor.getColumnIndex(AcceleratedAccountingProvider.COLUMN_RECORDED_HOURS_DATE));
		String time = cursor.getString(cursor.getColumnIndex(AcceleratedAccountingProvider.COLUMN_RECORDED_HOURS_TIME));
		String name = cursor.getString(cursor.getColumnIndex(AcceleratedAccountingProvider.COLUMN_LOCATION_NAME));
		String color = cursor.getString(cursor.getColumnIndex(AcceleratedAccountingProvider.COLUMN_LOCATION_COLOR));
		return new RecordedHour(id, date, time, name, color);
	}
	

	public static SBRecordedHoursHelper getInstance(Context ctx) {
		if(ctx == null) {
			return null;
		}
		
		if(instance == null) {
			instance = new SBRecordedHoursHelper(ctx);
		}
		
		return instance;
	}
	
	
	private static final class Interval {
		
		RecordedHour intervalStart;
		
		RecordedHour intervalEnd;
		
	}

}
