package de.accso.accelerated.accounting.activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import de.accso.accelerated.accounting.R;
import de.accso.accelerated.accounting.data.AggregatedRecordedHour;
import de.accso.accelerated.accounting.data.AggregatedRecordedHoursOfDay;
import de.accso.accelerated.accounting.data.helper.SBRecordedHoursHelper;



public class SBShowRecordedHoursDay extends Activity {

	private TableLayout recordedHoursList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_recorded_hours_day_sb);
		
		initView();
	}

	private void initView() {
		recordedHoursList = (TableLayout) findViewById(R.id.show_recorded_hours_list_table);
		recordedHoursList.setScrollContainer(false);
		recordedHoursList.setClickable(true);
		
		
		addRecordedHours();
	}

	private void addRecordedHours() {
		SBRecordedHoursHelper helper = SBRecordedHoursHelper.getInstance(this);
		List<Calendar> days = new ArrayList<Calendar>();
		days.add(Calendar.getInstance());
		
		List<AggregatedRecordedHoursOfDay> aggHoursOfDays = helper.getRecordedHoursOfDays(days);
		//List<AggregatedRecordedHoursOfDay> aggHoursOfDays = createDummyData();
		
		for(AggregatedRecordedHoursOfDay aggHoursOfDay: aggHoursOfDays)  {
			recordedHoursList.addView(getSeparatorRow(aggHoursOfDay.getDayString()));
			recordedHoursList.addView(getBorder());
			
			int index = 0;
			for(AggregatedRecordedHour aggHour: aggHoursOfDay.getAggRecoredHours()) {
				Drawable indicator = getIndicator(aggHour);
				TableRow row = this.getRow(aggHour.getLocationName(), aggHour.getStartTimeString(), aggHour.getEndTimeString(), indicator);
				recordedHoursList.addView(row);
				
				if(index < aggHoursOfDay.getAggRecoredHours().size() -1) {
					recordedHoursList.addView(getBorder());
				}
				index++;
			}
		}
	}

	
	private Drawable getIndicator(AggregatedRecordedHour aggHour) {
		if(aggHour.isWorkingPlace()) {
			return getResources().getDrawable(R.drawable.recorded_hours_indicator_none);
		} else {
			return getResources().getDrawable(R.drawable.recorded_hours_indicator_full);
		}
	}

	private TableRow getRow(String location, String startTime, String endTime, Drawable indicator) {
		LayoutInflater vi = getLayoutInflater();
		TableRow tableRow = (TableRow) vi.inflate(R.layout.show_recorded_hours_day_table_row, null);
		
		ImageView image = (ImageView) tableRow.findViewById(R.id.recorded_hours_indicator);
		image.setBackgroundDrawable(indicator);
		
		TextView locationText = (TextView) tableRow.findViewById(R.id.recorded_hours_location_text);
		locationText.setText(location);
		
		TextView timeText = (TextView) tableRow.findViewById(R.id.recorded_hours_time_text);
		timeText.setText(startTime+" - "+endTime);
		
		return tableRow;
	}
	
	private TableRow getSeparatorRow(String dateString) {
		LayoutInflater vi = getLayoutInflater();
		TableRow tableRow = (TableRow) vi.inflate(R.layout.show_recorded_hours_day_table_separator_row, null);
		
		TextView locationText = (TextView) tableRow.findViewById(R.id.recorded_hours_header_text);
		
		// TODO: SB Wichtig DateFormatting in Android: !!
		//CharSequence dateString = DateFormat.format(DateUtil.GERMAN_DAY_FORMAT_LONG, date);
		locationText.setText(dateString);
		
		return tableRow;
	}
	
	public void doAccount(View view) {
		// TODO: SB find out which row has been clicked on via ID of adapter!
		// TODO: SB pass aggregated duration to the OnAccount activity!
		Intent intent = new Intent(this, DoAccount.class);
		startActivity(intent);
	}
	
	private View getBorder()  {
		LayoutInflater vi = getLayoutInflater();
		View borderView = vi.inflate(R.layout.table_list_border, null);
		
		return borderView;
	}
	
	private List<AggregatedRecordedHoursOfDay> createDummyData() {
		List<AggregatedRecordedHoursOfDay> aggHoursOfDays = new ArrayList<AggregatedRecordedHoursOfDay>();
		
		AggregatedRecordedHoursOfDay aggHoursOfDay1 = new AggregatedRecordedHoursOfDay();
		aggHoursOfDay1.setDayString("Heute");
		List<AggregatedRecordedHour> aggHours = new ArrayList<AggregatedRecordedHour>();
		
		AggregatedRecordedHour aggHour1 = new AggregatedRecordedHour();
		aggHour1.setStartTimeString("08:00");
		aggHour1.setEndTimeString("12:15");
		aggHour1.setLocationName("Accso Headquarter");
		aggHour1.setIndicator(getResources().getDrawable(R.drawable.recorded_hours_indicator_none));
		aggHours.add(aggHour1);
		
		AggregatedRecordedHour aggHour2 = new AggregatedRecordedHour();
		aggHour2.setStartTimeString("12:30");
		aggHour2.setEndTimeString("13:00");
		aggHour2.setLocationName("VaPiano");
		aggHour2.setIndicator(getResources().getDrawable(R.drawable.recorded_hours_indicator_full));
		aggHours.add(aggHour2);
		
		AggregatedRecordedHour aggHour3 = new AggregatedRecordedHour();
		aggHour3.setStartTimeString("13:15");
		aggHour3.setEndTimeString("17:00");
		aggHour3.setLocationName("Accso Headquarter");
		aggHour3.setIndicator(getResources().getDrawable(R.drawable.recorded_hours_indicator_none));
		aggHours.add(aggHour3);
		
		aggHoursOfDay1.setAggRecoredHours(aggHours);
		aggHoursOfDays.add(aggHoursOfDay1);
		
		
//		AggregatedRecordedHoursOfDay aggHoursOfDay2 = new AggregatedRecordedHoursOfDay();
//		aggHoursOfDay2.setDayString("Gestern");
//		aggHours = new ArrayList<AggregatedRecordedHour>();
//		
//		aggHour1 = new AggregatedRecordedHour();
//		aggHour1.setStartTimeString("09:00");
//		aggHour1.setEndTimeString("18:00");
//		aggHour1.setLocationName("Deutsche Flugsicherung");
//		aggHour1.setIndicator(getResources().getDrawable(R.drawable.recorded_hours_indicator_none));
//		aggHours.add(aggHour1);
//		
//		aggHoursOfDay2.setAggRecoredHours(aggHours);
//		aggHoursOfDays.add(aggHoursOfDay2);
//		
//		AggregatedRecordedHoursOfDay aggHoursOfDay3 = new AggregatedRecordedHoursOfDay();
//		aggHoursOfDay3.setDayString("Vorgestern");
//		aggHours = new ArrayList<AggregatedRecordedHour>();
//		
//		
//		aggHour1 = new AggregatedRecordedHour();
//		aggHour1.setStartTimeString("08:15");
//		aggHour1.setEndTimeString("12:15");
//		aggHour1.setLocationName("Accso Headquarter");
//		aggHour1.setIndicator(getResources().getDrawable(R.drawable.recorded_hours_indicator_none));
//		aggHours.add(aggHour1);
//		
//		aggHour2 = new AggregatedRecordedHour();
//		aggHour2.setStartTimeString("12:30");
//		aggHour2.setEndTimeString("13:30");
//		aggHour2.setLocationName("Mensa h_da");
//		aggHour2.setIndicator(getResources().getDrawable(R.drawable.recorded_hours_indicator_full));
//		aggHours.add(aggHour2);
//		
//		aggHour3 = new AggregatedRecordedHour();
//		aggHour3.setStartTimeString("13:50");
//		aggHour3.setEndTimeString("18:00");
//		aggHour3.setLocationName("Accso Headquarter");
//		aggHour3.setIndicator(getResources().getDrawable(R.drawable.recorded_hours_indicator_none));
//		aggHours.add(aggHour3);
//		
//		aggHoursOfDay3.setAggRecoredHours(aggHours);
//		aggHoursOfDays.add(aggHoursOfDay3);
		
		return aggHoursOfDays;
	}


}
