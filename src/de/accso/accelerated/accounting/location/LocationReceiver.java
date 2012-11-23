package de.accso.accelerated.accounting.location;

import android.location.Location;

/**
 * 
 * @author Susanne Braun
 *
 */
public interface LocationReceiver {
	
	void onLocationCaptured(Location location); 
	
}
