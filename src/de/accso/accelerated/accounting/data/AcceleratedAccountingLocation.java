package de.accso.accelerated.accounting.data;

import android.location.Location;



public class AcceleratedAccountingLocation {
	
	public static final int LOCATION_FORMAT = Location.FORMAT_SECONDS;

	public static enum Type {
		Workplace(0, "Arbeitsplatz"), 
		Freetime(1, "Freizeit");
	
		private int intType;
		
		private String guiString;
		
		Type(int intType, String guiString) {
			this.intType = intType;
			this.guiString = guiString;
		}
		
		public static Type fromInt(int type) {
			switch (type) {
			case 0:
				return Workplace;
			case 1:
				return Freetime;
			
			default:
				return null;
			}
		}
		
		public static Type fromGuiString(String guiString) {
			if(Workplace.guiString.equals(guiString)) {
				return Workplace;
			} else if(Freetime.guiString.equals(guiString)) {
				return Freetime;
			}
			
			return null;
		}
		
		public int toInt() {
			return this.intType;
		}
		
		
	};
	
	private String name;
	
	private String color;
	
	private double longitude;
	
	private double latitude;
	
	private Type type;
	
	

	public AcceleratedAccountingLocation(String name, String color, double longitude, double latitude,
			Type type) {
		super();
		this.name = name;
		this.color = color;
		this.longitude = longitude;
		this.latitude = latitude;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}
	
	public String getColor(){
		return color;
	}
	
}
