package de.accso.accelerated.accounting.location;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;

/**
 * Simple location listener implementation, which is widely identical
 * with the location listener example from the android docs.
 * 
 * @author Susanne Braun
 *
 */
public class SimpleLocationListener implements LocationListener {

	private static final int TWO_MINUTES = 1000 * 60 * 2;
	
	protected static final String LOG_TAG = "Location";
	
	/**
	 * Current best location, that has  been captured.
	 */
	protected Location currentBestLocation;

	@Override
	public void onLocationChanged(Location location) {
		// Called when a new location is found by the network location provider.
		makeUseOfNewLocation(location);
	}

	@Override
	public void onProviderDisabled(String provider) {
		StringBuilder buffer = new StringBuilder();
		buffer.append( "Location proivder \'").append(provider).append("\' has been disabled");
		
		Log.i(LOG_TAG, buffer.toString());
	}

	@Override
	public void onProviderEnabled(String provider) {
		StringBuilder buffer = new StringBuilder();
		buffer.append( "Location proivder \'").append(provider).append("\' has been enabled");
		
		Log.i(LOG_TAG, buffer.toString());
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		StringBuilder buffer = new StringBuilder();
		buffer.append( "The status of location proivder \'").append(provider).append("\' has been changed to ");
		
		switch (status) {
		case LocationProvider.AVAILABLE:
			buffer.append("\'available\'");
			break;
		case LocationProvider.OUT_OF_SERVICE:
			buffer.append("\'out of service\'");
			break;
		case LocationProvider.TEMPORARILY_UNAVAILABLE:
			buffer.append("\'temporarily unavailable\'");
			break;

		default:
			buffer.append("an unknown status (").append(status).append(")");
			break;
		}
		
		
		Log.i(LOG_TAG, buffer.toString());
	}

	private synchronized void makeUseOfNewLocation(Location location) {
		if(this.isBetterLocation(location)) {
			this.currentBestLocation = location;
			if(Log.isLoggable(LOG_TAG, Log.VERBOSE)) {
				this.logCurrentLocationUpdate();
			}
		}
	}

	private void logCurrentLocationUpdate() {
		StringBuilder buffer = new StringBuilder();
		
		buffer.append("Location has been updated to: ");
		buffer.append(this.currentBestLocation.toString());
		
		
		Log.v(LOG_TAG, buffer.toString());
	}

	/**
	 * Determines whether one Location reading is better than the current
	 * Location fix
	 * 
	 * @param location
	 *            The new Location that you want to evaluate
	 * @param currentBestLocation
	 *            The current Location fix, to which you want to compare the new
	 *            one
	 */
	private boolean isBetterLocation(Location location) {
		if (this.currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - this.currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use
		// the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be
			// worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - this.currentBestLocation
				.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(),
				this.currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate
				&& isFromSameProvider) {
			return true;
		}
		return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}

}
