package de.accso.accelerated.accounting.data.helper;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import de.accso.accelerated.accounting.data.Project;
import de.accso.accelerated.accounting.storage.AcceleratedAccountingProvider;

public class ProjectHelper {
	
	public static Cursor getAll(Context context){
		Uri uri = Uri.withAppendedPath(AcceleratedAccountingProvider.CONTENT_URI, AcceleratedAccountingProvider.PROJECTS_CONTENT_DIRECTORY);
	    Cursor cursor = context.getContentResolver().query(uri, null, null, null, AcceleratedAccountingProvider.COLUMN_PROJECT_NAME);
	    return cursor;
	}
	
	public static List<Project> getAllAsList(Context context){
		Uri uri = Uri.withAppendedPath(AcceleratedAccountingProvider.CONTENT_URI, AcceleratedAccountingProvider.PROJECTS_CONTENT_DIRECTORY);
	    return getList(context, uri, null, null, null, AcceleratedAccountingProvider.COLUMN_PROJECT_NAME) ;
	}
		
	public static Project getById(Context context, int id){
		Uri uri = Uri.withAppendedPath(AcceleratedAccountingProvider.CONTENT_URI, AcceleratedAccountingProvider.PROJECTS_CONTENT_DIRECTORY);
		Cursor cursor = context.getContentResolver().query(uri, null, AcceleratedAccountingProvider.COLUMN_PROJECT_ID + " == ?", new String [] { String.valueOf(id) }, null);
		
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			
			return createProject(context, cursor);
		}
		
		return null;
	}
	
	public static List<Project> getByCustomerIdAsList(Context context, int customerId){
		Uri uri = Uri.withAppendedPath(AcceleratedAccountingProvider.CONTENT_URI, AcceleratedAccountingProvider.PROJECTS_CONTENT_DIRECTORY);
		return getList(context, uri, null, AcceleratedAccountingProvider.COLUMN_PROJECT_CUSTOMER_ID + " == ?", new String [] { String.valueOf(customerId) }, AcceleratedAccountingProvider.COLUMN_PROJECT_NAME);
	}
	
	public static List<Project> getByLocationIdAsList(Context context, int locationId){
		Uri uri = Uri.withAppendedPath(AcceleratedAccountingProvider.CONTENT_URI, AcceleratedAccountingProvider.PROJECT_TO_LOCATION_CONTENT_DIRECTORY);
		String [] projection = new String [] { AcceleratedAccountingProvider.PROJECT_TABLE + "." + AcceleratedAccountingProvider.COLUMN_PROJECT_ID, 
											   AcceleratedAccountingProvider.PROJECT_TABLE + "." + AcceleratedAccountingProvider.COLUMN_PROJECT_NAME, 
											   AcceleratedAccountingProvider.PROJECT_TABLE + "." + AcceleratedAccountingProvider.COLUMN_PROJECT_DESCRIPTION, 
											   AcceleratedAccountingProvider.PROJECT_TABLE + "." + AcceleratedAccountingProvider.COLUMN_PROJECT_CUSTOMER_ID };
		String selection = AcceleratedAccountingProvider.COLUMN_PROJECT_TO_LOCATION_LOCATION_ID + " == ? ";
		String [] selectionArgs = new String [] { String.valueOf(locationId) };
		return getList(context, uri, projection, selection, selectionArgs, null);
	}
	
	public static String [] getNamesByLocationName (Context context, String locationName){
		Uri uri = Uri.withAppendedPath(AcceleratedAccountingProvider.CONTENT_URI, AcceleratedAccountingProvider.PROJECT_TO_LOCATION_CONTENT_DIRECTORY);
		String [] projection = new String [] { AcceleratedAccountingProvider.PROJECT_TABLE + "." + AcceleratedAccountingProvider.COLUMN_PROJECT_NAME };
		String selection = AcceleratedAccountingProvider.LOCATIONS_TABLE + "." + AcceleratedAccountingProvider.COLUMN_LOCATION_NAME  + " == ? ";
		String [] selectionArgs = new String [] { locationName };
		Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
		
		List<String> projectNames = new ArrayList<String>();
	    
		if (cursor != null) {
			cursor.moveToFirst();
			
			do {
				projectNames.add(cursor.getString(cursor.getColumnIndex(AcceleratedAccountingProvider.COLUMN_PROJECT_NAME)));
			} while (cursor.moveToNext());
		}
		return projectNames.toArray(new String[projectNames.size()]);
	}
	
	public static void addProjectToLocation(Context context, int locationId, int projectId){
		Uri uri = Uri.withAppendedPath(AcceleratedAccountingProvider.CONTENT_URI, AcceleratedAccountingProvider.PROJECT_TO_LOCATION_CONTENT_DIRECTORY);
		ContentValues values = new ContentValues();
		values.put(AcceleratedAccountingProvider.COLUMN_PROJECT_TO_LOCATION_LOCATION_ID, locationId);
		values.put(AcceleratedAccountingProvider.COLUMN_PROJECT_TO_LOCATION_PROJECT_ID, projectId);		
		context.getContentResolver().insert(uri, values);
	}
	
	public static void removeProjectToLocation(Context context, int locationId, int projectId){
		Uri uri = Uri.withAppendedPath(AcceleratedAccountingProvider.CONTENT_URI, AcceleratedAccountingProvider.PROJECT_TO_LOCATION_CONTENT_DIRECTORY);
		String where = AcceleratedAccountingProvider.COLUMN_PROJECT_TO_LOCATION_LOCATION_ID + " == ? AND " + AcceleratedAccountingProvider.COLUMN_PROJECT_TO_LOCATION_PROJECT_ID + " == ?";
		context.getContentResolver().delete(uri, where, new String [] { String.valueOf(locationId), String.valueOf(projectId) });
	}
	
	private static List<Project> getList(Context context, Uri uri, String[] projection, String selection, String [] selectionArgs, String sortOrder){
		Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
	    List<Project> projects = new ArrayList<Project>();
	    
		if (cursor != null) {
			cursor.moveToFirst();
			
			do {
				projects.add(createProject(context, cursor));
			} while (cursor.moveToNext());
		}
	    
		return projects;
	}
	
	private static Project createProject(Context context, Cursor cursor){
		int id = cursor.getInt(cursor.getColumnIndex(AcceleratedAccountingProvider.COLUMN_PROJECT_ID));
		String name = cursor.getString(cursor.getColumnIndex(AcceleratedAccountingProvider.COLUMN_PROJECT_NAME));
		String description = cursor.getString(cursor.getColumnIndex(AcceleratedAccountingProvider.COLUMN_PROJECT_DESCRIPTION));
		int customerId = cursor.getInt(cursor.getColumnIndex(AcceleratedAccountingProvider.COLUMN_PROJECT_CUSTOMER_ID));
		return new Project(context, id, name, description, customerId);
	}
	
}
