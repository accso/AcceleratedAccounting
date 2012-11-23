package de.accso.accelerated.accounting.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import de.accso.accelerated.accounting.R;
import de.accso.accelerated.accounting.data.Project;
import de.accso.accelerated.accounting.data.helper.ProjectHelper;
import de.accso.accelerated.accounting.map.LocationMarkerOverlay;
import de.accso.accelerated.accounting.storage.AcceleratedAccountingProvider;
import de.accso.accelerated.accounting.util.LocationUtil;

/**
 * TODO: SB ShowLocation leitet nicht von der MainActivity ab
 * -> Menü nicht verfügbar.
 * 
 * @author Susanne Braun
 *
 */
public class ShowLocation extends MapActivity {

	protected static final String LOG_TAG = "ShowLocation";

	
	private String locationName;
	
	@SuppressWarnings("unused")
	private String locationType;
	
	private String longitudeString;
	
	private String latitudeString;
	
	@SuppressWarnings("unused")
	private String accuracy;
	
	private int locationId;
	
	List<Project> allProjects;
	List<Project> locationProjects;
	private ArrayList<Integer> selectedProjectsId;
	private String[] projectNames;
	private boolean[] isProjectSelected;


	private TableLayout projectsList;
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_location);
		
		this.getIntentData();
		this.initView();
	}


	private void getIntentData() {
		Intent intent = getIntent();
		
		if(intent != null) {
			Uri locationUri = intent.getData();
			if(locationUri != null) {
				Cursor cursor = getContentResolver().query(locationUri, null, null, null, null);
				try {
					if (cursor.getCount() > 0) {
						cursor.moveToFirst();

						int nameIndex = cursor
								.getColumnIndex(AcceleratedAccountingProvider.COLUMN_LOCATION_NAME);
						this.locationName = cursor.getString(nameIndex);
						int typeIndex = cursor
								.getColumnIndex(AcceleratedAccountingProvider.COLUMN_LOCATION_TYPE);
						this.locationType = cursor.getString(typeIndex);
						int longitudeIndex = cursor
								.getColumnIndex(AcceleratedAccountingProvider.COLUMN_LOCATION_LONGITUDE);
						this.longitudeString = cursor.getString(longitudeIndex);
						int latitudeIndex = cursor
								.getColumnIndex(AcceleratedAccountingProvider.COLUMN_LOCATION_LATITUDE);
						this.latitudeString = cursor.getString(latitudeIndex);
						int accuracyIndex = cursor
						.getColumnIndex(AcceleratedAccountingProvider.COLUMN_LOCATION_ACCURACY);
						this.accuracy = cursor.getString(accuracyIndex);
						int idIndex = cursor.getColumnIndex(AcceleratedAccountingProvider.COLUMN_LOCATION_ID);
						this.locationId = cursor.getInt(idIndex);
					}
				} finally {
					if (cursor != null) {
						cursor.close();
					}
				}
			}
		}
	}
	
	private void initView() {
		TextView nameLabel = (TextView) findViewById(R.id.show_loction_name);
		nameLabel.setText(this.locationName);
		
		MapView map = (MapView) findViewById(R.id.show_loction_map);
		this.initMap(map);
		
		initProjectsList();
	}


	private void initProjectsList() {
		projectsList = (TableLayout) findViewById(R.id.show_location_projects_list_table);
		projectsList.setScrollContainer(false);
		projectsList.setClickable(true);
		
		
		addProjectRows();
		
		TableLayout headerTable = (TableLayout) findViewById(R.id.show_location_projects_list_table_header);
		headerTable.setScrollContainer(false);
		headerTable.setClickable(true);
		
		TableRow headerRow = getHeaderRow();
		headerTable.addView(headerRow);
		headerRow.setClickable(true);
		headerRow.setFocusable(true);
		headerRow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				addItem();
			}
		});
	}


	private void addProjectRows() {
		//locationProjects = ProjectHelper.getByLocationIdAsList(this, locationId);
		locationProjects = getDummyProjects();
		
		for(int index=0; index<locationProjects.size(); index++) {
			Project project = locationProjects.get(index);
			String porjectName = project.getName();
			
			TableRow row = getRow(porjectName);
			projectsList.addView(row);
			
			if(index < locationProjects.size() -1) {
				projectsList.addView(getBorder());
			}
			
			
			final int rowIndex = index;
			ImageView icon = (ImageView) row.findViewById(R.id.table_row_icon);
			icon.setClickable(true);
			icon.setFocusable(true);
			icon.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View clickedRow) {
					removeItemAtRow(rowIndex);
				}
			});
			
			
		}
	}
	
	private List<Project> getDummyProjects() {
		List<Project> projects = new ArrayList<Project>();
		
		Project p1 = new Project(this, "Android-Projekt", "", 0);
		projects.add(p1);
		
		Project p2 = new Project(this, "HTML5-Projekt", "", 0);
		projects.add(p2);
		
		return projects;
	}


	private void initMap(MapView map) {
		if(map == null) {
			return;
		}
	
		double longitude = Location.convert(this.longitudeString.replace(',', '.'));
		double latitude = Location.convert(this.latitudeString.replace(',', '.'));
		int intLongitude = (int) Math.floor(longitude * 1E6);
		int intLatitude = (int) Math.floor(latitude * 1E6);
		GeoPoint point = new GeoPoint(intLatitude, intLongitude);
		map.getController().setCenter(point);
		
		int zoom = 17;
		if(map.getMaxZoomLevel() < zoom) {
			zoom = map.getMaxZoomLevel();
		}
		map.getController().setZoom(zoom);
		
		// TODO: SB refactor this, move code into LocationMarkerOverlay!#
		StringBuilder snippet = new StringBuilder();
		snippet.append(LocationUtil.toNiceString(this.latitudeString)).append("\n");
		snippet.append(LocationUtil.toNiceString(this.longitudeString));
		
		OverlayItem overlayItem = new OverlayItem(point, this.locationName, snippet.toString());
		Drawable markerIcon = this.getResources().getDrawable(R.drawable.maps_marker);
		
		LocationMarkerOverlay marker = new LocationMarkerOverlay(markerIcon, this);
		marker.addOverlay(overlayItem);
		
		List<Overlay> mapOverlays = map.getOverlays();
		mapOverlays.add(marker);
		
		map.setBuiltInZoomControls(true);
	}


	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	public void editProjects(){
		allProjects = ProjectHelper.getAllAsList(this);
		
		projectNames = new String[allProjects.size()];
		isProjectSelected = new boolean[allProjects.size()];
		selectedProjectsId = new ArrayList<Integer>();
		int index = 0;
		for(Project project: allProjects){
			projectNames[index] = project.getName();
			if(locationProjects.contains(project)){
				isProjectSelected[index] = true;
				selectedProjectsId.add(project.getId());
			}else{
				isProjectSelected[index] = false;
			}
			index++;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Projekte hinzufügen / entfernen");
        
        OnMultiChoiceClickListener listener = new OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1, boolean arg2) {
				if(arg2 == true){
					selectedProjectsId.add(allProjects.get(arg1).getId());
				}else{
					selectedProjectsId.remove((Object)allProjects.get(arg1).getId());
				}
			} 
        };
        
		builder.setMultiChoiceItems(projectNames, isProjectSelected, listener);
		builder.setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() { 
            public void onClick(DialogInterface dialog, int whichButton) { 
            	//insert
            	for(int id: selectedProjectsId){
            		 boolean isInDatabase = false;
            		 for(Project project: locationProjects){
            			 if(project.getId() == id){
            				 isInDatabase = true;
            			 }
            		 }
            		 if(!isInDatabase){
            			 ProjectHelper.addProjectToLocation(getApplicationContext(), locationId, id);
            		 }
            	}
            	//delete
            	for(Project project: locationProjects){
            		if(!selectedProjectsId.contains(project.getId())){
            			ProjectHelper.removeProjectToLocation(getApplicationContext(), locationId, project.getId());
            		}
            	}
            	
            	updateProjectsList();
            	dialog.cancel();
            	//MessageUtil.ShowMessage(getApplicationContext(), "Projekte wurden dem Standort " + locationName + " hinzugefügt / entfernt.");
            } 
        });
		
        builder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() { 
            public void onClick(DialogInterface dialog, int whichButton) { 
            	dialog.cancel(); 
            } 
        });

        builder.create().show();
	}
	
	private TableRow getRow(String tableItem) {
		LayoutInflater vi = getLayoutInflater();
		TableRow tableRow = (TableRow) vi.inflate(R.layout.table_list_row, null);
		
		TextView textView = (TextView) tableRow.findViewById(R.id.table_row_text);
		textView.setText(tableItem);
		
		return tableRow;
	}

	private TableRow getHeaderRow() {
		LayoutInflater vi = getLayoutInflater();
		TableRow headerRow = (TableRow) vi.inflate(R.layout.table_list_header_row, null);
		
		
		return headerRow;
	}
	
	private View getBorder()  {
		LayoutInflater vi = getLayoutInflater();
		View borderView = vi.inflate(R.layout.table_list_border, null);
		
		return borderView;
	}
	
	protected void addItem() {
		this.editProjects();
	}
	
	private void updateProjectsList() {
		this.removeAllProjectsFromList();
		this.addProjectRows();
	}


	private void removeAllProjectsFromList() {
		if(this.projectsList != null) {
			this.projectsList.removeAllViews();
		}
	}


	private void removeItemAtRow(int index) {
		if(this.locationProjects != null && index < locationProjects.size()) {
			Project project = locationProjects.get(index);
			if(project != null) {
				ProjectHelper.removeProjectToLocation(this, locationId, project.getId());
				updateProjectsList();
			}
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.show_location_menu, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.show_location_menu_item_delete:
			this.deleteLocation();
		
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	private void deleteLocation() {
		Uri uri = Uri.withAppendedPath(AcceleratedAccountingProvider.CONTENT_URI, AcceleratedAccountingProvider.LOCATIONS_CONTENT_DIRECTORY);
		String where = AcceleratedAccountingProvider.COLUMN_LOCATION_ID + " == ? ";
		this.getContentResolver().delete(uri, where, new String [] { String.valueOf(locationId)});
		
		showLocations();
	}
	
	
	private void showLocations() {
		Intent intent = new Intent(this, ShowLocations.class);
		startActivity(intent);
	}
}
