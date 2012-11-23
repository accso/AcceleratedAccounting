package de.accso.accelerated.accounting.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import de.accso.accelerated.accounting.R;
import de.accso.accelerated.accounting.storage.AcceleratedAccountingProvider;


public class ShowLocations extends AcceleratedAccounting implements OnItemClickListener {
	
	private ListView list;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.show_locations);
		
		this.initView();
	}


	private void initView() {
		initAddLocationTable();
		
		this.list = (ListView) findViewById(R.id.show_locations_list);
		list.setOnItemClickListener(this);
		
		String[] from = new String[] { AcceleratedAccountingProvider.COLUMN_LOCATION_NAME, AcceleratedAccountingProvider.COLUMN_LOCATION_TYPE };
		int[] to = new int[] {R.id.show_list_line_1, R.id.show_list_line_2};

		Uri uri = Uri.withAppendedPath(AcceleratedAccountingProvider.CONTENT_URI, AcceleratedAccountingProvider.LOCATIONS_CONTENT_DIRECTORY);
	    Cursor cursor = getContentResolver().query(uri, null, null, null, null);
	    if(cursor != null) {
	    	startManagingCursor(cursor);
	    
	    	// Set the ListView
	    	SimpleCursorAdapter adapter = new SimpleCursorAdapter(
	            this, R.layout.two_line_list_line_view, cursor, from, to);
	    
	    	list.setAdapter(adapter);
	    }
	}

	private void initAddLocationTable() {
		TableLayout headerTable = (TableLayout) findViewById(R.id.show_locations_add_location);
		headerTable.setScrollContainer(false);
		headerTable.setClickable(true);
		
		
		TableRow headerRow = getHeaderRow();
		headerTable.addView(headerRow);
		TextView text = (TextView) headerRow.findViewById(R.id.table_header_row_text);
		text.setText(R.string.add_current_location);
		
		headerRow.setClickable(true);
		headerRow.setFocusable(true);
		headerRow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				addCurrentLocation();
			}
		});
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Uri locationUri = Uri.withAppendedPath(AcceleratedAccountingProvider.LOCATIONS_CONTENT_URI, String.valueOf(id));
		
		Intent intent = new Intent(Intent.ACTION_VIEW, locationUri);
		startActivity(intent);
	}
	
	private TableRow getHeaderRow() {
		LayoutInflater vi = getLayoutInflater();
		TableRow headerRow = (TableRow) vi.inflate(R.layout.table_list_header_row, null);
		
		
		return headerRow;
	}
	
	private void addCurrentLocation() {
		Intent intent = new Intent(this, CaptureLocation.class);
		startActivity(intent);
	}
	    
}
