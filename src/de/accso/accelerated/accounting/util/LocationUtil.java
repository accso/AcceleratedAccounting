package de.accso.accelerated.accounting.util;

import android.location.Location;
import de.accso.accelerated.accounting.data.AcceleratedAccountingLocation;

public class LocationUtil {
	
	public static String toNiceString(String longitudeOrLatitude) {
		
		int start = 0;
		
		int endOfDegrees = longitudeOrLatitude.indexOf(":", start);
		String deegrees = longitudeOrLatitude.substring(start, endOfDegrees);
		
		start = endOfDegrees + 1;
		int endOfMinutes = longitudeOrLatitude.indexOf(":", start);
		String minutes = longitudeOrLatitude.substring(start, endOfMinutes);
		
		start = endOfMinutes +1;
		String seconds = longitudeOrLatitude.substring(start);
		
		StringBuilder buffer = new StringBuilder();
		
		buffer.append(deegrees).append("° ");
		buffer.append(minutes).append("\' ");
		buffer.append(seconds).append("\"");
		
		return buffer.toString();
	}
	
	public static float distanceBetween(Location loc1, Location loc2) {
		if(loc1 == null || loc2 == null) {
			return -1;
		}
		float[] results = new float[1];
		Location.distanceBetween(loc1.getLatitude(), loc1.getLongitude(), loc2.getLatitude(), loc2.getLongitude(), results);
		
		return results[0];
	}
	
	public static float distanceBetween(Location loc1, AcceleratedAccountingLocation loc2) {
		if(loc1 == null || loc2 == null) {
			return -1;
		}
		float[] results = new float[1];
		Location.distanceBetween(loc1.getLatitude(), loc1.getLongitude(), loc2.getLatitude(), loc2.getLongitude(), results);
		
		return results[0];
	}

}
