package de.accso.accelerated.accounting.services;

import java.util.Locale;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import de.accso.accelerated.accounting.NotificationIds;
import de.accso.accelerated.accounting.R;
import de.accso.accelerated.accounting.activities.CaptureLocation;
import de.accso.accelerated.accounting.location.LocationReceiver;
import de.accso.accelerated.accounting.location.MinimumAccuracyLocationListener;
import de.accso.accelerated.accounting.storage.AcceleratedAccountingProvider;

/**
 * Service that captures the GPS position as a background service. 
 * The service will terminate itself after a timout of MAXIMUM_CAPTURING_TIME.
 * The service uses a wake lock to prevent the cpu being switched off by the OS.
 * 
 * @author Susanne Braun
 *
 */
public class CaptureLocationService extends Service implements LocationReceiver {
	
	public static String WAKE_LOCK_NAME = "de.accso.accelerated.accounting.location.CaptureLocationService";
	
	protected static final String LOG_TAG = "CaptureLocation";
	
	/*
	 * Intent extras keys...
	 */
	public static final String EXTRAS_LOCATION_TYPE_KEY = "location_type";

	public static final String EXTRAS_LOCATION_NAME_KEY = "location_name";
	
	public static final String EXTRAS_LOCATION_COLOR_NAME_KEY = "location_color";
	
	
	
	/**
	 * The services strives to capture a position of 
	 * optimal accuracy (which could be 5m or something).
	 */
	protected static final int OPTIMAL_POSITION_ACCURACY = 25;
	
	/**
	 * The least position accuracy, that is tolerated (could be 
	 * 10m).
	 */
	protected static final int MINIMUM_POSITION_ACCURACY = 1000;
	
	/**
	 * Maximum time used for capturing in milliseconds.
	 */
	protected static final int MAXIMUM_CAPTURING_TIME = 11 * 60 * 1000;
	
	
	
	private PowerManager.WakeLock wakeLock = null;
	
	private Handler timedTasksHandler = new Handler();
	
	
	protected LocationManager locationManager;
	
	protected MinimumAccuracyLocationListener locationListener;
	
	
	protected Location capturedLocation;
	
	protected Uri locationUri;
	
	protected String locationName;
	
	protected String locationType;
	
	protected String locationColor;
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		this.locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		this.locationListener = new MinimumAccuracyLocationListener(
				OPTIMAL_POSITION_ACCURACY, MINIMUM_POSITION_ACCURACY, this.locationManager, this, null);
		
		
		this.initWakeLock(this);
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(LOG_TAG, "Capture Location service has been started...");
		
		if (!this.wakeLock.isHeld()) {
			this.wakeLock.acquire();
		}
		
		this.locationUri = null;
		this.getUserInputValues(intent);
		
		this.locationListener.startCapture();
		this.timedTasksHandler.postDelayed(this.stopCapture, MAXIMUM_CAPTURING_TIME);
		
		return START_REDELIVER_INTENT;
	}
	
	private void getUserInputValues(Intent intent) {
		Bundle extras = intent.getExtras();
		
		this.locationName = extras.getString(CaptureLocationService.EXTRAS_LOCATION_NAME_KEY);
		this.locationType = extras.getString(CaptureLocationService.EXTRAS_LOCATION_TYPE_KEY);
		this.locationColor = extras.getString(CaptureLocationService.EXTRAS_LOCATION_COLOR_NAME_KEY);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		
		this.cleanup();
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	private void cleanup() {
		try {
			// call stop capture in any case
			// if won't hurt if it is called more than just once
			this.locationListener.stopCapture();
		} finally {
			if (this.wakeLock.isHeld()) {
				this.wakeLock.release();
			}
		}
	}

	private void stopCapture() {
		this.locationListener.stopCapture();
		this.timedTasksHandler.removeCallbacks(this.stopCapture);
		
		if (this.capturedLocation != null && this.locationUri == null) {
			// ok locaton has been captured...
			this.locationUri = storeLocation();
			
			// notify user via status bar, that capturing the location was successfull
			this.createSuccessNotification(locationUri);
			
		} else {
			// accurate location has not been captured.
			// notify via status bar, that capturing the location has failed.
			this.createFailureNotification();
		}

		this.cleanup();
		this.stopSelf();
	}

	private Uri storeLocation() {
		Locale.getDefault();
		Locale.setDefault(Locale.US);	
		Uri baseUri = AcceleratedAccountingProvider.CONTENT_URI;
		Uri locationsUri = Uri.withAppendedPath(baseUri, AcceleratedAccountingProvider.LOCATIONS_CONTENT_DIRECTORY);
		
		ContentValues values = new ContentValues();
		values.put(AcceleratedAccountingProvider.COLUMN_LOCATION_NAME, this.locationName);
		values.put(AcceleratedAccountingProvider.COLUMN_LOCATION_TYPE, this.locationType);
		values.put(AcceleratedAccountingProvider.COLUMN_LOCATION_COLOR, this.locationColor);
		values.put(AcceleratedAccountingProvider.COLUMN_LOCATION_LONGITUDE, Location.convert(capturedLocation.getLongitude(), Location.FORMAT_SECONDS));
		values.put(AcceleratedAccountingProvider.COLUMN_LOCATION_LATITUDE, Location.convert(capturedLocation.getLatitude(), Location.FORMAT_SECONDS));
		values.put(AcceleratedAccountingProvider.COLUMN_LOCATION_ACCURACY, String.valueOf(capturedLocation.getAccuracy()));
		
		Uri locationUri = getContentResolver().insert(locationsUri, values);
		
		return locationUri;
	}
	
	 private void createSuccessNotification(Uri locationUri) {
		 int icon = R.drawable.notification_icon;        // icon from resources
		 CharSequence tickerText = getResources().getText(R.string.capture_location_successfull_notification_ticker);              // ticker-text
		 long when = System.currentTimeMillis();         // notification time
		 Context context = getApplicationContext();      // application Context
		 CharSequence contentTitle = getResources().getText(R.string.capture_location_successfull_notification_ticker);   // expanded message title
		 CharSequence contentText = getResources().getText(R.string.capture_location_successfull_notification_msg);  // expanded message text

		 Intent notificationIntent = new Intent(Intent.ACTION_VIEW, locationUri);
		 PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		 // the next two lines initialize the Notification, using the configurations above
		 Notification notification = new Notification(icon, tickerText, when);
		 notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		 notification.flags |= Notification.FLAG_AUTO_CANCEL;
		 
		 String ns = Context.NOTIFICATION_SERVICE;
		 NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		 // TODO: SB gescheite ID vergeben.
		 mNotificationManager.notify(NotificationIds.CAPTURE_LOCATION_SUCCESS, notification);
	}

	private void createFailureNotification() {
		 int icon = R.drawable.notification_icon;        // icon from resources
		 CharSequence tickerText = getResources().getText(R.string.capture_location_failed_notification_ticker);              // ticker-text
		 long when = System.currentTimeMillis();         // notification time
		 Context context = getApplicationContext();      // application Context
		 CharSequence contentTitle = getResources().getText(R.string.capture_location_failed_notification_ticker);   // expanded message title
		 CharSequence contentText = getResources().getText(R.string.capture_location_failed_notification_msg);  // expanded message text

		 Intent notificationIntent = new Intent(this, CaptureLocation.class);
		 PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		 // the next two lines initialize the Notification, using the configurations above
		 Notification notification = new Notification(icon, tickerText, when);
		 notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		 notification.flags |= Notification.FLAG_AUTO_CANCEL;
		 notification.defaults |= Notification.DEFAULT_SOUND;
		 
		 String ns = Context.NOTIFICATION_SERVICE;
		 NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		 // TODO: SB gescheite ID vergeben.
		 mNotificationManager.notify(NotificationIds.CAPTURE_LOCATION_FAILED, notification);
	}

	private synchronized PowerManager.WakeLock initWakeLock(Context context) {
		if (this.wakeLock == null) {
			PowerManager powerMgr = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
			
			this.wakeLock = powerMgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKE_LOCK_NAME);
			this.wakeLock.setReferenceCounted(true);
		}
		
		return this.wakeLock;
	}
	
	/**
	 * Task used to dismiss the confirmation dialog after 
	 * short period of time.
	 */
	private Runnable stopCapture = new Runnable() {
		 public void run() {
			 CaptureLocationService.this.stopCapture();
		 }
	};


	@Override
	public void onLocationCaptured(Location location) {
		// location has been captured
		this.capturedLocation = location;
		this.stopCapture();
	}
	
	

	

}
