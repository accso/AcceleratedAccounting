package de.accso.accelerated.accounting.data;

import java.util.Date;

public class Accounting {

	private int _id;
	
	private Date day;
	
	private double duration;
	
	private String project;
	
	private String projectTask;
	
	private String comment;

	
	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getProjectTask() {
		return projectTask;
	}

	public void setProjectTask(String projectTask) {
		this.projectTask = projectTask;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
}
