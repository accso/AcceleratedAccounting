package de.accso.accelerated.accounting.data.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import de.accso.accelerated.accounting.data.HoursAssignedToAccount;
import de.accso.accelerated.accounting.data.RecordedHour;
import de.accso.accelerated.accounting.services.LocationMonitorService;
import de.accso.accelerated.accounting.storage.AcceleratedAccountingProvider;

public class RecordedHoursHelper {
	
	public static List<RecordedHour> getByDate(Context context, Date date){
		Uri uri = Uri.withAppendedPath(AcceleratedAccountingProvider.CONTENT_URI, AcceleratedAccountingProvider.RECORDED_HOURS_CONTENT_DIRECTORY);
		String where = AcceleratedAccountingProvider.COLUMN_RECORDED_HOURS_DATE + " == ?";
		String[] whereArgs = new String [] { RecordedHour.DATE_FORMATTER.format(date) } ;
		String orderBy = AcceleratedAccountingProvider.COLUMN_RECORDED_HOURS_TIME;
	    return getList(context, uri, null, where, whereArgs, orderBy);
	}
	
	public static List<HoursAssignedToAccount> getAsBlockByDate(Context context, Date date){
		return getBlockList(getByDate(context, date));
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
	
	private static List<HoursAssignedToAccount> getBlockList(List<RecordedHour> recordedHours){
		List<HoursAssignedToAccount> recordedHourBlocks = new ArrayList<HoursAssignedToAccount>();
		if(recordedHours.size() == 0){
			return recordedHourBlocks;
		}
		RecordedHour startRecordedHour = null;
		RecordedHour lastRecordedHour = null;
		for(RecordedHour recordedHour: recordedHours){
			if(startRecordedHour == null){
				startRecordedHour = recordedHour;
			}
			if(lastRecordedHour == null){
				lastRecordedHour = recordedHour;
			}else{
				long timeDifference = recordedHour.getTime().getTimeInMillis() - lastRecordedHour.getTime().getTimeInMillis();
				if(timeDifference > (LocationMonitorService.CAPTURE_INTERVAL + 5 * 60 * 1000) || !recordedHour.getName().equals(lastRecordedHour.getName())){
					recordedHourBlocks.add(new HoursAssignedToAccount(startRecordedHour, lastRecordedHour));
					startRecordedHour = recordedHour;
					lastRecordedHour = null;
				} 
				lastRecordedHour = recordedHour;
			}
		}
		
		recordedHourBlocks.add(new HoursAssignedToAccount(startRecordedHour, recordedHours.get(recordedHours.size() - 1)));
		return recordedHourBlocks;
	}
	


}
