package de.accso.accelerated.accounting.data;

@Deprecated
public class RecordedHourBlock {
	
	private RecordedHour startRecordedHour;
	private RecordedHour stopRecordedHour;
	
	public RecordedHourBlock(RecordedHour startRecordedHour, RecordedHour stopRecordedHour){
		this.startRecordedHour = startRecordedHour;
		this.stopRecordedHour = stopRecordedHour;
	}
	
	public RecordedHour getStartRecordedHour(){
		return this.startRecordedHour;
	}
	
	public RecordedHour getStopRecordedHour(){
		return this.stopRecordedHour;
	}
}
