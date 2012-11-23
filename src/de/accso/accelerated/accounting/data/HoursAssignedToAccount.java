package de.accso.accelerated.accounting.data;

import java.util.Date;

@Deprecated
public class HoursAssignedToAccount implements Comparable<HoursAssignedToAccount> {

	private String locationName;
	private String note;
	private int selectedProjectPosition;
	private boolean isToAssignAccount;
	private Date startTime;
	private Date endTime;
	private String color;
	private int id;

	public HoursAssignedToAccount(RecordedHour startRecordedHour, RecordedHour stopRecordedHour) {
		this.isToAssignAccount = false;
		this.locationName = startRecordedHour.getName();
//		this.startTime = startRecordedHour.getTime();
//		this.endTime = stopRecordedHour.getTime();
		this.color = startRecordedHour.getColor();
	}

	public HoursAssignedToAccount(String locationName, String color, Date startTime, Date endTime, String note, boolean isToAssignAccount) {
		this.isToAssignAccount = isToAssignAccount;
		this.startTime = startTime;
		this.endTime = endTime;
		this.locationName = locationName;
		this.note = note;
		this.color = color;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public boolean getIsToAssignAccount() {
		return isToAssignAccount;
	}

	public void setIsToAssignAccount(boolean isToAssignAccount) {
		this.isToAssignAccount = isToAssignAccount;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getSelectedProjectPosition() {
		return selectedProjectPosition;
	}

	public void setSelectedProjectPosition(int selectedProjectPosition) {
		this.selectedProjectPosition = selectedProjectPosition;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getLocationNameWithTime() {
		return locationName + "\r\n" + RecordedHour.TIME_FORMATTER_LABEL.format(startTime) + " - " + RecordedHour.TIME_FORMATTER_LABEL.format(endTime);
	}

	public HoursAssignedToAccount splitBlock() {
		long difference = this.endTime.getTime() - this.startTime.getTime();
		long start = this.startTime.getTime();
		long end = this.endTime.getTime();
		Date startTime = new Date();
		Date endTime = new Date();
		startTime.setTime(start + (difference / 2));
		endTime.setTime(end);
		this.endTime.setTime(end - (difference / 2));
		return new HoursAssignedToAccount(this.locationName, this.color, startTime, endTime, this.note, this.isToAssignAccount);
	}

	public int compareTo(HoursAssignedToAccount arg0) {
		return this.getStartTime().compareTo(arg0.getStartTime());
	}

	public void Save() {
		if (id == 0) {
			// TODO: Insert
		} else {
			// TODO: Update
		}
	}

}
