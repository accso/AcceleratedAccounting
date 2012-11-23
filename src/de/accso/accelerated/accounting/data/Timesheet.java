package de.accso.accelerated.accounting.data;

import java.util.List;

public class Timesheet {
	
	private int year;
	
	private int month;
	
	private List<Accounting> accountings;
	
	
	

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public List<Accounting> getAccountings() {
		return accountings;
	}

	public void setAccountings(List<Accounting> accountings) {
		this.accountings = accountings;
	}
	
}
