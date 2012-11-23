package de.accso.accelerated.accounting.activities;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import de.accso.accelerated.accounting.R;
import de.accso.accelerated.accounting.storage.AcceleratedAccountingProvider;

public class ShowRecordedHours  extends AcceleratedAccounting implements OnItemClickListener {
	
	private ListView list;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.show_recorded_hours);
		
		this.initView();
	}


	private void initView() {
		this.list = (ListView) findViewById(R.id.show_recorded_hours_list);
		//list.setOnItemClickListener(this);
		
		String[] from = new String[] { AcceleratedAccountingProvider.COLUMN_RECORDED_HOURS_DATE, AcceleratedAccountingProvider.COLUMN_RECORDED_HOURS_TIME, AcceleratedAccountingProvider.COLUMN_LOCATION_LATITUDE, AcceleratedAccountingProvider.COLUMN_LOCATION_LONGITUDE, AcceleratedAccountingProvider.COLUMN_LOCATION_ACCURACY, AcceleratedAccountingProvider.COLUMN_LOCATION_TYPE, AcceleratedAccountingProvider.COLUMN_LOCATION_NAME };
		int[] to = new int[] {R.id.show_recorded_hours_list_date, R.id.show_recorded_hours_list_time, R.id.show_recorded_hours_list_lat, R.id.show_recorded_hours_list_long, R.id.show_recorded_hours_list_accuracy, R.id.show_recorded_hours_list_type, R.id.show_recorded_hours_list_name};

		Uri uri = Uri.withAppendedPath(AcceleratedAccountingProvider.CONTENT_URI, AcceleratedAccountingProvider.RECORDED_HOURS_CONTENT_DIRECTORY);
		String orderBy = AcceleratedAccountingProvider.COLUMN_RECORDED_HOURS_TIME+" desc";
	    Cursor cursor = getContentResolver().query(uri, null, null, null, orderBy);
	    if(cursor != null) {
	    	startManagingCursor(cursor);
	    
	    	// Set the ListView
	    	SimpleCursorAdapter adapter = new SimpleCursorAdapter(
	            this, R.layout.show_recorded_hours_list_line, cursor, from, to);
	    
	    	list.setAdapter(adapter);
	    }
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//		Uri locationUri = Uri.withAppendedPath(AcceleratedAccountingProvider.LOCATIONS_CONTENT_URI, String.valueOf(id));
//		
//		Intent intent = new Intent(Intent.ACTION_VIEW, locationUri);
//		startActivity(intent);
	}
}
