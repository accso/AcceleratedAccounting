package de.accso.accelerated.accounting.location;

import java.util.Calendar;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

/**
 * Location listener for the exact current GPS position. The ExactGPSPositionListener
 * should be a service that runs in the background. If a gps position with apropriate 
 * accuracy has been captured the listener starts an activity to display the results
 * or notifies the user via status bar.
 * 
 * Implementation is *not* thread-safe!
 * 
 * @author Susanne Braun
 *
 */
public class MinimumAccuracyLocationListener extends SimpleLocationListener implements LocationListener {
	
	/**
	 * Required minium accuracy of the captured loacation in meters.
	 * (95% of all measurements have accuracy of at least 7.8m, 
	 * guaranteed accuracy is 15m).
	 */
	private float optAccuracy;
	
	/**
	 * Minimum accuracy, that is not optimal, but still acceptable (e.g. 10m).
	 */
	private float minAccuracy;
	
	
	/**
	 * Capturing start time.
	 */
	private long capturingStartTime;
	
	/**
	 * Reference to the location mgr.
	 */
	private LocationManager locationManager;
	
	/**
	 * Used location providers.
	 */
	private String[] provider;
	
	
	/**
	 * Location that meet accuracy requirements.
	 */
	private Location exactLocation;
	
	private boolean capturing;

	/**
	 * Receiver of the lcoaton.
	 */
	private LocationReceiver locationReceiver;
	

	public MinimumAccuracyLocationListener(float optAccuracy,
			float minAccuracy, LocationManager locationManager, LocationReceiver locationReceiver, String[] provider) {
		super();
		this.optAccuracy = optAccuracy;
		this.minAccuracy = minAccuracy;
		this.capturing = false;
		this.locationManager = locationManager;
		this.locationReceiver = locationReceiver;
		
		if(provider == null || provider.length <= 0) {
			this.provider = new String[] {LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER, LocationManager.PASSIVE_PROVIDER};
		} else {
			this.provider = provider;
		}
	}
	

	public synchronized void startCapture() {
		if (!this.capturing) {
			Log.i(LOG_TAG, "Starting to capture exact position...");
			this.capturingStartTime = Calendar.getInstance().getTimeInMillis();
			this.capturing = true;

			// Register the location listener with the
			// its providers
			// first delete old update requests, if any.
			this.locationManager.removeUpdates(this);
			
			for(String provider: this.provider) {
				this.locationManager.requestLocationUpdates(provider, 0, 0, this);
			}
		}
	}
	
	
	public synchronized void stopCapture() {
		if (this.capturing) {
			Log.i(LOG_TAG, "Stopping to capture exact position...");
			this.locationManager.removeUpdates(this);
			this.capturing = false;

			if (this.exactLocation == null && this.currentBestLocation != null) {

				float accuracy = this.currentBestLocation.getAccuracy();
				if (accuracy <= this.minAccuracy) {
					Log.i(LOG_TAG, "Exact position has been captured: "
							+ this.currentBestLocation + ". Accuracy is "
							+ accuracy + "m.");
					this.exactLocation = currentBestLocation;

				} else {
					Log.e(LOG_TAG,
							"Failed to capture exact position. Best Accuracy has been "
									+ accuracy + "m.");
				}
			}

			if (this.exactLocation != null) {
				this.locationReceiver.onLocationCaptured(this.exactLocation);
			}
		}
	}

	
	@Override
	public void onLocationChanged(Location location) {
		
		boolean isNewLocation = this.isNewLocation(location);
		boolean isMoreAccurateLocation = this.isMoreAccurateLocation(location);
		
		if (isNewLocation) {

			if (isMoreAccurateLocation) {
				this.currentBestLocation = location;
				float accuracy = location.getAccuracy();
				
				if(accuracy <= this.optAccuracy) {
					Log.i(LOG_TAG, "Exact position has been captured: "+location+". Accuracy is "+accuracy+"m.");
					this.exactLocation = location;
					this.stopCapture();
				}
			}
		}
	}
	
	private boolean isNewLocation(Location location) {
		long timeDelta = location.getTime() - this.capturingStartTime;
		return timeDelta >= 0;
	}
	
	private boolean isMoreAccurateLocation(Location location) {
		if (this.currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - this.currentBestLocation
				.getAccuracy());
		boolean isMoreAccurate = accuracyDelta < 0;

		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate) {
			return true;
		} 
		
		return false;
	}

	public void setLocationManager(LocationManager locationManager) {
		this.locationManager = locationManager;
	}

	public Location getExactLocation() {
		return this.exactLocation;
	}
	
	public boolean isCapturing() {
		return this.capturing;
	}

}
