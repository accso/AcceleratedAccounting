package de.accso.accelerated.accounting.data;

import java.util.List;

public class AggregatedRecordedHoursOfDay {
	
	private String dayString;
	
	private List<AggregatedRecordedHour> aggRecoredHours;

	public String getDayString() {
		return dayString;
	}

	public void setDayString(String dayString) {
		this.dayString = dayString;
	}

	public List<AggregatedRecordedHour> getAggRecoredHours() {
		return aggRecoredHours;
	}

	public void setAggRecoredHours(List<AggregatedRecordedHour> aggRecoredHours) {
		this.aggRecoredHours = aggRecoredHours;
	}
	
	

}
