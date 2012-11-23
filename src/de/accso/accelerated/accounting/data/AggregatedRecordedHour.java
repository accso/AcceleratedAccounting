package de.accso.accelerated.accounting.data;

import android.graphics.drawable.Drawable;


public class AggregatedRecordedHour {
	
	
	private String startTimeString;
	
	private String endTimeString;
	
	private String locationName;
	
	// TODO: SB Indicator entsprechend setzen
	private boolean isWorkingPlace;
	
	private Drawable indicator;



	public String getStartTimeString() {
		return startTimeString;
	}

	public void setStartTimeString(String startTimeString) {
		this.startTimeString = startTimeString;
	}

	public String getEndTimeString() {
		return endTimeString;
	}

	public void setEndTimeString(String endTimeString) {
		this.endTimeString = endTimeString;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public boolean isWorkingPlace() {
		return isWorkingPlace;
	}

	public void setWorkingPlace(boolean isWorkingPlace) {
		this.isWorkingPlace = isWorkingPlace;
	}

	public Drawable getIndicator() {
		return indicator;
	}

	public void setIndicator(Drawable indicator) {
		this.indicator = indicator;
	}
	
	

}
