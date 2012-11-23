package de.accso.accelerated.accounting.data;

import java.util.List;

import android.graphics.drawable.Drawable;


public class AccountedHoursOfDay {
	
	private String dayString;
	
	private Drawable indicator;
	
	private List<Accounting> accountedHours;
	

	public String getDayString() {
		return dayString;
	}

	public void setDayString(String dayString) {
		this.dayString = dayString;
	}

	public List<Accounting> getAccountedHours() {
		return accountedHours;
	}

	public void setAccountedHours(List<Accounting> accountedHours) {
		this.accountedHours = accountedHours;
	}

	public Drawable getIndicator() {
		return indicator;
	}

	public void setIndicator(Drawable indicator) {
		this.indicator = indicator;
	}

	
	
}
