package de.accso.accelerated.accounting.activities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import de.accso.accelerated.accounting.R;
import de.accso.accelerated.accounting.data.Accounting;
import de.accso.accelerated.accounting.data.Timesheet;
import de.accso.accelerated.accounting.jsonadapter.JSONSerialiser;

public class SendTimesheet extends Activity {
	
	protected static final String LOG_TAG = "SendTimesheet";
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.send_monthly_report);
		
		
		// buttons and handlers
		Button okButton = (Button) findViewById(R.id.send_timesheet_ok_button);
		okButton.setOnClickListener(okButtonListener);
	}
	
	
	/**
	 * OK Button (Capture location) listener
	 */
	private OnClickListener okButtonListener = new OnClickListener() {

		public void onClick(View v) {
			// do something when the button is clicked
			Log.d(LOG_TAG, "OK Button of has been pressed");
			
			
			Timesheet timesheet = createDummyData();
			try {
				JSONObject jsonData = JSONSerialiser.getJSONTimesheet(timesheet);
				Log.d(LOG_TAG, jsonData.toString());
				
				sendTimesheet(timesheet);
				
			} catch (JSONException e) {
				
			}
			
		}

		
	};
	
	private class SendTimesheetTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... uris) {
			if(uris.length <= 0) {
				return null;
			}
			
			// TODO: SB implement me!
//			HttpClient httpclient = new DefaultHttpClient();
//			HttpPut httpPut = new HttpPut(uris[0]);
//			
//			File file = new File(filename);         
//
//			MultipartEntity entity = new MultipartEntity();
//			ContentBody body = new FileBody(file, "image/jpeg");
//			entity.addPart("userfile", body);
//
//			httpPut.setEntity(entity);
//			HttpResponse response = httpclient.execute(post);
//			HttpEntity resEntity = response.getEntity();
//			
			
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

		
	     
	 }
	
	private void sendTimesheet(Timesheet timesheet) {
		try {
			JSONObject jsonTimesheet = JSONSerialiser.getJSONTimesheet(timesheet);
		
		
		} catch (JSONException e) {
			Log.e(LOG_TAG, "Failed to serialize timesheet into JSON.", e);
			// TODO: SB show toast message
		}
	}
	
	private String getRequestString() {
		return "http://127.0.0.1:5984/accso_accounting/"+String.valueOf(System.currentTimeMillis());
	}
	

	
	private Timesheet createDummyData() {
		List<Accounting> accHoursYesterday = new ArrayList<Accounting>();
		
		Accounting hour1 = new Accounting();
		hour1.setDay(new Date());
		hour1.setDuration(6);
		hour1.setProject("Android-Projekt");
		hour1.setProjectTask("ENTW");
		hour1.setComment("Feature x, Feature y");
		accHoursYesterday.add(hour1);
		
		Accounting hour2 = new Accounting();
		hour2.setDay(new Date());
		hour2.setDuration(2);
		hour2.setProject("Android-Projekt");
		hour2.setProjectTask("TEST");
		hour2.setComment("Testing Feature x + Feature y");
		accHoursYesterday.add(hour2);
		
		Timesheet timesheet = new Timesheet();
		timesheet.setAccountings(accHoursYesterday);
		timesheet.setYear(2012);
		timesheet.setMonth(5);
		
		return timesheet;
	}

}
