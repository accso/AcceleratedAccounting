package de.accso.accelerated.accounting.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.Process;
import android.util.Log;
import de.accso.accelerated.accounting.NotificationIds;
import de.accso.accelerated.accounting.R;
import de.accso.accelerated.accounting.activities.AcceleratedAccounting;
import de.accso.accelerated.accounting.data.AcceleratedAccountingLocation;
import de.accso.accelerated.accounting.data.AcceleratedAccountingLocation.Type;
import de.accso.accelerated.accounting.location.LocationReceiver;
import de.accso.accelerated.accounting.location.MinimumAccuracyLocationListener;
import de.accso.accelerated.accounting.storage.AcceleratedAccountingProvider;
import de.accso.accelerated.accounting.util.DateUtil;
import de.accso.accelerated.accounting.util.LocationUtil;


public class LocationMonitorService extends Service implements LocationReceiver {
	
	/**
	 * interval of 10 Mins.
	*/
	public static final long CAPTURE_INTERVAL = 10 * 60 * 1000;
	
	public static String WAKE_LOCK_NAME = "de.accso.accelerated.accounting.location.AcceleratedAccountingService";

	protected static final String LOG_TAG = "AccsoService";
	
	public static final DateFormat DATE_FORMATTER = new SimpleDateFormat(DateUtil.DB_DAY_FORMAT);
	
	public static final DateFormat TIME_FORMATTER = new SimpleDateFormat(DateUtil.DB_TIME_FORMAT);
	
	
	
	/**
	 * The services strives to capture a position of 
	 * optimal accuracy (which could be 5m or something).
	 */
	protected static final int OPTIMAL_POSITION_ACCURACY = 100;
	
	/**
	 * The least position accuracy, that is tolerated (could be 
	 * 1000m). -> Momentan nicht beschränkt!
	 */
	protected static final float MINIMUM_POSITION_ACCURACY = Float.MAX_VALUE;
	
	
	/**
	 * If a captured location is in the surroundings of a location
	 * with a maximum radius of 500m, it is assumed that the
	 * location has not changed.
	 */
	protected static final int MAXIMUM_LOCATION_DISTANCE = 250;
	
	
	/**
	 * Maximum time used for capturing in milliseconds.
	 */
	protected static final int MAXIMUM_CAPTURING_TIME_PER_CHECK =  1 * 60 * 1000;
	
	
	protected LocationManager locationManager;
	
	protected MinimumAccuracyLocationListener locationListener;
	
	private PowerManager.WakeLock wakeLock = null;
	
	private Looper captureLooper;
	
	private CaptureHandler captureHandler;
	 
	private Handler stopHandler = new Handler();
		
	
	/**
	 * A location that has just been captured.
	 */
	protected Location capturedLocation;
	
	
	/**
	 * The current location
	 */
	private Location currentLocation;
	
	/**
	 * If the current location is a known location this 
	 * field will be not null.
	 */
	private AcceleratedAccountingLocation currentKnownLocation;
	
	//TODO: SB update on data changes...
	private List<AcceleratedAccountingLocation> knownLocations;
	
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		String[] providers = new String[] {LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER, LocationManager.PASSIVE_PROVIDER};
		
		this.locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		this.locationListener = new MinimumAccuracyLocationListener(
				OPTIMAL_POSITION_ACCURACY, 
				MINIMUM_POSITION_ACCURACY, 
				this.locationManager, this, providers);
		
		this.initWakeLock(this);
		this.initCaptureHandler();
	}

	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(LOG_TAG, "Capture Location service has been started...");
		if (!this.wakeLock.isHeld()) {
			this.wakeLock.acquire();
		}
		this.doStartInForeground();
		this.startCaptureHandler();
		
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		
		this.cleanup();
	}


	
	private void doStartInForeground() {
	     int icon = R.drawable.notification_icon;        // icon from resources
		 CharSequence tickerText = "Accelerated Accounting Service up and running";              // ticker-text
		 long when = System.currentTimeMillis();         // notification time
		 Context context = getApplicationContext();      // application Context
		 CharSequence contentTitle = "Accelerated Accounting Service up and running";   // expanded message title
		 CharSequence contentText = "... tracking your working hours";  // expanded message text
		
		 Intent notificationIntent = new Intent(this, AcceleratedAccounting.class);
		 PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		 // the next two lines initialize the Notification, using the configurations above
		 Notification notification = new Notification(icon, tickerText, when);
		 notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		 notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;
		
		 startForeground(NotificationIds.ONGOING_NOTIFICATION, notification); 
	}
	
	
	

	private void startCaptureHandler() {
		Message msg = captureHandler.obtainMessage();
	    captureHandler.sendMessage(msg);
	}
	
	private void startCapture() {
		initKnownLocations();

		stopHandler.postDelayed(stopCapture,
				MAXIMUM_CAPTURING_TIME_PER_CHECK);
		locationListener.startCapture();
	}
	
	private Runnable stopCapture = new Runnable() {
		 public void run() {
			 stopCapture();
		 }
	};
	
	@Override
	public synchronized void onLocationCaptured(Location location) {
		this.capturedLocation = location;
		this.stopCapture();
		
	}
	
	protected synchronized void stopCapture() {
		// do all the cleanup stuff.
	try {
		this.locationListener.stopCapture();
		
		if (this.capturedLocation != null) {
			this.handleCapturedLocation();
		} else {
			// reasonably good location has not been captured!!
			Log.e(LOG_TAG, "Failed to capture, current location. AcceleratedAccounting will not work properly...");
		}
	} finally {
		cleanup();
		stopSelf();
	}
}

	private synchronized void handleCapturedLocation() {
		// check if 
		// a) location has changed (does not lie in surroundings of old location (radius of 20m)
		// b) if location has changed
		//   c) check if the location is in the surroundings of a known location
		//   d) if yes write a record into the db
		// e) if location has not changed and previous location is a known location
		//   f) write a record into the db.
		if(this.capturedLocation == null) {
			return;
		}
		
		boolean locationHasChanged = false;
		boolean firstLocationCapture = false;
		
		if(this.currentLocation != null) {
			float distance = LocationUtil.distanceBetween(this.currentLocation, this.capturedLocation);
			
			if(distance >= 0 && distance <= MAXIMUM_LOCATION_DISTANCE) {
				// location has not changed
				
				this.currentLocation = this.capturedLocation;
				if(this.currentKnownLocation != null) {
					// current location has been known and is still a known location
					writeRecord();
				} // else current location has not been known before and is still not known!
			
			} else {
				locationHasChanged = true;
			}
		
		} else {
			firstLocationCapture = true;
		}
		
		if (locationHasChanged || firstLocationCapture) {
			// no current location yet or location has changed...
			this.currentLocation = capturedLocation;
			this.currentKnownLocation = null;

			AcceleratedAccountingLocation closestKnownLocation = this.getClosestKnownLocation(this.capturedLocation);
			if (closestKnownLocation != null) {
				float distance = LocationUtil.distanceBetween(this.capturedLocation, closestKnownLocation);
				if (distance >= 0 && distance <= MAXIMUM_LOCATION_DISTANCE) {
					this.currentKnownLocation = closestKnownLocation;
					writeRecord();
				}
			}
		}
	}

	private void initKnownLocations() {
		this.knownLocations = new ArrayList<AcceleratedAccountingLocation>();
		
		Uri uri = Uri.withAppendedPath(AcceleratedAccountingProvider.CONTENT_URI, AcceleratedAccountingProvider.LOCATIONS_CONTENT_DIRECTORY);
	    Cursor cursor = getContentResolver().query(uri, null, null, null, null);
	    
	    try {
	    	if(cursor != null && cursor.getCount() > 0) {
	    		int nameIndex = cursor.getColumnIndex(AcceleratedAccountingProvider.COLUMN_LOCATION_NAME);
	    		int typeIndex = cursor.getColumnIndex(AcceleratedAccountingProvider.COLUMN_LOCATION_TYPE);
	    		int colorIndex = cursor.getColumnIndex(AcceleratedAccountingProvider.COLUMN_LOCATION_COLOR);
	    		int longitudeIndex = cursor.getColumnIndex(AcceleratedAccountingProvider.COLUMN_LOCATION_LONGITUDE);
	    		int latitudeIndex = cursor.getColumnIndex(AcceleratedAccountingProvider.COLUMN_LOCATION_LATITUDE);
	    		
	    		while(cursor.moveToNext()) {
	    			String name = cursor.getString(nameIndex);
	    			String typeString = cursor.getString(typeIndex);
	    			String color = cursor.getString(colorIndex);
	    			String longitudeString = cursor.getString(longitudeIndex);
	    			String latitudeString = cursor.getString(latitudeIndex);
	    			
	    			double longitude = Location.convert(longitudeString.replace(',', '.'));
	    			double latitude = Location.convert(latitudeString.replace(',', '.'));
	    			Type type = Type.fromGuiString(typeString);
	    			
	    			AcceleratedAccountingLocation knownLocation = new AcceleratedAccountingLocation(name, color, longitude, latitude, type);
	    			this.knownLocations.add(knownLocation);
	    		}
	    		
	    	}
	    	
	    } finally {
	    	if(cursor != null) {
	    		cursor.close();
	    	}
	    }
		
	}
	
	private void writeRecord() {
		// get the content provider URI...
		Uri baseUri = AcceleratedAccountingProvider.CONTENT_URI;
		Uri locationsUri = Uri.withAppendedPath(baseUri, AcceleratedAccountingProvider.RECORDED_HOURS_CONTENT_DIRECTORY);
		
		
		// prepare the data
		Calendar now = Calendar.getInstance();
		Date date = now.getTime();
		String dateString = DATE_FORMATTER.format(date);
		String timeString = TIME_FORMATTER.format(date);
		
		String name = this.currentKnownLocation.getName();
		String type = this.currentKnownLocation.getType().toString();
		String color = this.currentKnownLocation.getColor();
		String longitude = Location.convert(this.currentKnownLocation.getLongitude(), Location.FORMAT_SECONDS);
		String latitude = Location.convert(this.currentKnownLocation.getLatitude(), Location.FORMAT_SECONDS);
		String accuracy = String.valueOf(capturedLocation.getAccuracy());
		
		// create values map
		ContentValues values = new ContentValues();
		values.put(AcceleratedAccountingProvider.COLUMN_RECORDED_HOURS_DATE, dateString);
		values.put(AcceleratedAccountingProvider.COLUMN_RECORDED_HOURS_TIME, timeString);
		values.put(AcceleratedAccountingProvider.COLUMN_LOCATION_NAME, name);
		values.put(AcceleratedAccountingProvider.COLUMN_LOCATION_TYPE, type);
		values.put(AcceleratedAccountingProvider.COLUMN_LOCATION_LONGITUDE, longitude);
		values.put(AcceleratedAccountingProvider.COLUMN_LOCATION_LATITUDE, latitude);
		values.put(AcceleratedAccountingProvider.COLUMN_LOCATION_ACCURACY, accuracy);
		values.put(AcceleratedAccountingProvider.COLUMN_LOCATION_COLOR, color);
		this.logRecord(dateString, timeString, name, type, longitude, latitude, accuracy, color);
		
		// insert data into db
		getContentResolver().insert(locationsUri, values);
	}

	private void logRecord(String dateString, String timeString, String name,
			String type, String longitude, String latitude, String accuracy, String color) {
		StringBuilder buffer = new StringBuilder("The following record will be written into the db: [");
		buffer.append("date=").append(dateString).append(", ");
		buffer.append("time=").append(timeString).append(", ");
		buffer.append("name=").append(name).append(", ");
		buffer.append("type=").append(type).append(", ");
		buffer.append("latitude=").append(latitude).append(", ");
		buffer.append("longitude=").append(longitude);
		buffer.append("accuracy=").append(accuracy);
		buffer.append("color=").append(color);
		buffer.append("].");
		
		Log.i(LOG_TAG, buffer.toString());
	}

	private AcceleratedAccountingLocation getClosestKnownLocation(Location location) {
		if(this.knownLocations == null || this.knownLocations.size() == 0) {
			return null;
		}
		
		float minDistance = LocationUtil.distanceBetween(location, this.knownLocations.get(0));
		AcceleratedAccountingLocation closestKnownLocation = this.knownLocations.get(0);
		
		for(int i=1; i<this.knownLocations.size(); i++) {
			AcceleratedAccountingLocation knownLocation = this.knownLocations.get(i);
			float distance = LocationUtil.distanceBetween(location, knownLocation);
			if(distance < minDistance) {
				minDistance = distance;
				closestKnownLocation = knownLocation;
			}
		}
		
		return closestKnownLocation;
	}

	private void cleanup() {
		try {
			// call stop capture in any case
			// if won't do any harm if it is called more than just once
			this.locationListener.stopCapture();
			this.stopHandler.removeCallbacks(this.stopCapture);
		} finally {
			if (this.wakeLock.isHeld()) {
				this.wakeLock.release();
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	private synchronized PowerManager.WakeLock initWakeLock(Context context) {
		if (this.wakeLock == null) {
			PowerManager powerMgr = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
			
			this.wakeLock = powerMgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKE_LOCK_NAME);
			this.wakeLock.setReferenceCounted(true);
		}
		
		return this.wakeLock;
	}
	
	private void initCaptureHandler() {
		HandlerThread thread = new HandlerThread("LocationMonitorThread", Process.THREAD_PRIORITY_FOREGROUND);
	    thread.start();
	    
	    captureLooper = thread.getLooper();
	    captureHandler = new CaptureHandler(captureLooper);
	}
	
	
	private final class CaptureHandler extends Handler {
		public CaptureHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			startCapture();
		}
	}
}
