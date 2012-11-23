package de.accso.accelerated.accounting.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Arrays;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import de.accso.accelerated.accounting.R;
import de.accso.accelerated.accounting.activities.adapters.TileAdapter;
import de.accso.accelerated.accounting.services.LocationMonitorService;
import de.accso.accelerated.accounting.util.MessageUtil;
import de.accso.accelerated.accounting.util.PropertiesUtil;

/**
 * Main activity
 * 
 * @author Susanne Braun
 * 
 */
public class AcceleratedAccounting extends Activity {


	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);

		initView();
	}

	private void initView() {
		GridView gridview = (GridView) findViewById(R.id.main_tiles_grid);
		gridview.setAdapter(new TileAdapter(this));
		
		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				handleClickOnTile(position);
			}
		});
	}

	protected void handleClickOnTile(int position) {
		String msg = "";
		
		switch (position) {
		case TileAdapter.TILE_HOURS:
			this.showHours();
			break;
		case TileAdapter.TILE_PUSH:
			this.sendMonthlyReport();
			break;
		case TileAdapter.TILE_LOCATION:
			this.showLocations();
			break;
		case TileAdapter.TILE_START:
			this.startLocationMonitorService();
			msg = this.getString(R.string.msg_service_started);
			MessageUtil.ShowMessage(this, msg);
			break;
		case TileAdapter.TILE_STOP:
			this.stopLocationMonitorService();
			msg = this.getString(R.string.msg_service_stopped);
			MessageUtil.ShowMessage(this, msg);
			break;
		case TileAdapter.TILE_CUSTOMERS:
			this.showCustomers();
			break;

		default:
			break;
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		if (!PropertiesUtil.isDeveloper(this)) {
			menu.findItem(R.id.main_menu_item_copy_database).setVisible(false);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.main_menu_item_start:
			this.startLocationMonitorService();
			return true;
		case R.id.main_menu_item_stop:
			this.stopLocationMonitorService();
			return true;
		case R.id.main_menu_item_capture_location:
			this.captureLocation();
			return true;
		case R.id.main_menu_item_locations:
			this.showLocations();
			return true;
		case R.id.main_menu_item_settings:
			// TODO: open settings menu
			return true;
		case R.id.main_menu_item_customers:
			showCustomers();
			return true;
		case R.id.main_menu_item_recorded_hours:
			this.showRecordedHours();
			return true;
		case R.id.main_menu_item_show_recorded_hours_day:
			showRecordedHoursDay();
			return true;
		case R.id.main_menu_item_copy_database:
			copyDatabase();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void showHours() {
		Intent intent = new Intent(this, ShowRecordedAndAccountedHours.class);
		startActivity(intent);
	}
	
	private void sendMonthlyReport() {
		Intent intent = new Intent(this, SendTimesheet.class);
		startActivity(intent);
	}
	
	
	
	private void startLocationMonitorService() {
		long now = System.currentTimeMillis();
			
		// schedule periodic invokations of the service
		PendingIntent pendingIntent = getPendingLocationMonitorServiceIntent();
		AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, now, LocationMonitorService.CAPTURE_INTERVAL,
				pendingIntent);
	}

	private void stopLocationMonitorService() {
		// important: first cancel all future invokations of the service
		PendingIntent pendingIntent = getPendingLocationMonitorServiceIntent();
		AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmMgr.cancel(pendingIntent);
				
		// explicitly stop the service, if currently running
		Intent intent = new Intent(this, LocationMonitorService.class);
		stopService(intent);
	}

	private PendingIntent getPendingLocationMonitorServiceIntent() {
		Intent intent = new Intent(this, LocationMonitorService.class);
		PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
		
		return pendingIntent;
	}

	
	private void showLocations() {
		Intent intent = new Intent(this, ShowLocations.class);
		startActivity(intent);
	}

	private void showRecordedHours() {
		Intent intent = new Intent(this, ShowRecordedHours.class);
		startActivity(intent);
	}

	private void captureLocation() {
		Intent intent = new Intent(this, CaptureLocation.class);
		startActivity(intent);
	}

	private void showCustomers() {
		Intent intent = new Intent(this, ShowCustomers.class);
		startActivity(intent);
	}
	
	private void showRecordedHoursDay(){
//		Intent intent = new Intent(this, AssignedHoursTab.class);
//		startActivity(intent);
	}

	private void copyDatabase() {
		try {
			File sd = Environment.getExternalStorageDirectory();
			if (!Arrays.asList(sd.list()).contains("AcceleratedAccounting")) {
				File acceleratedAccountingDirectory = new File(
						sd.getCanonicalPath() + "/AcceleratedAccounting");
				acceleratedAccountingDirectory.mkdir();
			}
			File data = Environment.getDataDirectory();

			String currentDBPath = "/data/de.accso.accelerated.accounting/databases/ACCELERATED_ACCOUNTING";
			String backupDBPath = "AcceleratedAccounting/ACCELERATED_ACCOUNTING";
			File currentDB = new File(data, currentDBPath);
			File backupDB = new File(sd, backupDBPath);
			if (!backupDB.exists()) {
				backupDB.createNewFile();
			}

			if (currentDB.exists()) {
				FileChannel src = new FileInputStream(currentDB).getChannel();
				FileChannel dst = new FileOutputStream(backupDB).getChannel();
				dst.transferFrom(src, 0, src.size());
				src.close();
				dst.close();
			}
			
			MessageUtil.ShowMessage(this, "Datenbank wurde kopiert.");

		} catch (Exception e) {
			MessageUtil.ShowMessage(this, "Datenbank konnte nicht kopiert werden: " + e.getMessage());
		}

	}

}
