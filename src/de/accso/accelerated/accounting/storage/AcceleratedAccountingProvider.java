package de.accso.accelerated.accounting.storage;

import de.accso.accelerated.accounting.util.StringUtil;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;



public class AcceleratedAccountingProvider extends ContentProvider {

	public static final String AUTHORITY = "de.accso.accelerated.accounting.storage.acceleratedaccountingprovider";
	
	// content URIs...
	
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
	
	// locations
	public static final String LOCATIONS_CONTENT_DIRECTORY = "locations";
	
	public static final Uri LOCATIONS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + LOCATIONS_CONTENT_DIRECTORY);
	
	// recorded hours
	public static final String RECORDED_HOURS_CONTENT_DIRECTORY = "recordedhours";
	
	public static final Uri RECORDED_HOURS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + RECORDED_HOURS_CONTENT_DIRECTORY);
	
	// customer
	public static final String CUSTOMERS_CONTENT_DIRECTORY = "customers";
	public static final Uri CUSTOMERS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + CUSTOMERS_CONTENT_DIRECTORY);
	
	// address
	public static final String ADDRESSES_CONTENT_DIRECTORY = "addresses";
	public static final Uri ADDRESS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + ADDRESSES_CONTENT_DIRECTORY);
	
	//project
	public static final String PROJECTS_CONTENT_DIRECTORY = "projects";
	public static final Uri PROJECT_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PROJECTS_CONTENT_DIRECTORY);
	
	//project by location
	public static final String PROJECT_TO_LOCATION_CONTENT_DIRECTORY = "projecttolocation";
	public static final Uri PROJECT_TO_LOCATION_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PROJECT_TO_LOCATION_CONTENT_DIRECTORY);
	
	//colors for location
	public static final String AVAILABLE_COLORS_FOR_LOCATION_CONTENT_DIRECTORY = "availablecolorsforlocation";
	public static final Uri AVAILABLE_COLORS_FOR_LOCATION_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + AVAILABLE_COLORS_FOR_LOCATION_CONTENT_DIRECTORY);
	
	//hours assigned to Account
	public static final String HOURS_ASSIGNED_TO_ACCOUNT_CONTENT_DIRECTORY = "hoursassignedtoaccount";
	public static final Uri HOURS_ASSIGNED_TO_ACCOUNT_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + HOURS_ASSIGNED_TO_ACCOUNT_CONTENT_DIRECTORY);
	
	// MIME types...
	
	// MIME types used for locations
    public static final String LOCATION_MIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                                                  "/de.accso.accelerated.accounting.location";
    
	public static final String LOCATIONS_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/de.accso.accelerated.accounting.location";
	
	// MIME types used for recorded hours
    public static final String RECORDED_HOUR_MIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                                                  "/de.accso.accelerated.accounting.recorded.hour";
	public static final String RECORDED_HOURS_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/de.accso.accelerated.accounting.recorded.hour";
	
	// MIME types used for customer
    public static final String CUSTOMER_MIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/de.accso.accelerated.accounting.customer";
    public static final String CUSTOMERS_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/de.accso.accelerated.accounting.customer";
	
	// MIME types used for address
	public static final String ADDRESS_MIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/de.accso.accelerated.accounting.address";
	public static final String ADDRESSES_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/de.accso.accelerated.accounting.address";
	
	// MIME types used for project
	public static final String PROJECT_MIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/de.accso.accelerated.accounting.project";
	public static final String PROJECTS_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/de.accso.accelerated.accounting.projects";
	
	// MIME types used for project by location
	public static final String PROJECT_TO_LOCATION_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/de.accso.accelerated.accounting.projecttolocation";
	
	// MIME types user for AVAILABLE_COLORS_FOR_LOCATION
	public static final String AVAILABLE_COLORS_FOR_LOCATION_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/de.accso.accelerated.accounting.availablecolorsforlocation";
	
	// MIME types used for HOURS_ASSIGNED_TO_ACCOUNT
	public static final String HOURS_ASSIGNED_TO_ACCOUNT_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/de.accso.accelerated.accounting.hoursassignedtoaccount";
    
    // Location column definitions
	public static final String LOCATIONS_TABLE = AcceleratedAccountingDB.LOCATIONS_TABLE;
	
    public static final String COLUMN_LOCATION_ID = AcceleratedAccountingDB.COLUMN_LOCATION_ID;
    
    public static final String COLUMN_LOCATION_NAME = AcceleratedAccountingDB.COLUMN_LOCATION_NAME;
    
    public static final String COLUMN_LOCATION_TYPE = AcceleratedAccountingDB.COLUMN_LOCATION_TYPE;
    
    public static final String COLUMN_LOCATION_LONGITUDE = AcceleratedAccountingDB.COLUMN_LOCATION_LONGITUDE;
    
    public static final String COLUMN_LOCATION_LATITUDE = AcceleratedAccountingDB.COLUMN_LOCATION_LATITUDE;
    
    public static final String COLUMN_LOCATION_ACCURACY = AcceleratedAccountingDB.COLUMN_LOCATION_ACCURACY;
    
    public static final String COLUMN_LOCATION_COLOR = AcceleratedAccountingDB.COLUMN_LOCATION_COLOR;
    
    
    // Recorded hour column definitions
    public static final String COLUMN_RECORDED_HOURS_ID = AcceleratedAccountingDB.COLUMN_RECORDED_HOURS_ID;
	
	public static final String COLUMN_RECORDED_HOURS_DATE = AcceleratedAccountingDB.COLUMN_RECORDED_HOURS_DATE;
	
	public static final String COLUMN_RECORDED_HOURS_TIME = AcceleratedAccountingDB.COLUMN_RECOREDED_HOURS_TIME;
    
    
	// Customer column definition
	public static final String COLUMN_CUSTOMER_ID = AcceleratedAccountingDB.COLUMN_CUSTOMER_ID;
	
	public static final String COLUMN_CUSTOMER_NAME = AcceleratedAccountingDB.COLUMN_CUSTOMER_NAME;
	
	public static final String COLUMN_CUSTOMER_ADDRESS_ID = AcceleratedAccountingDB.COLUMN_CUSTOMER_ADDRESS_ID;
	
	//address table
	public static final String COLUMN_ADDRESS_ID = AcceleratedAccountingDB.COLUMN_ADDRESS_ID;
	
	public static final String COLUMN_ADDRESS_STREET = AcceleratedAccountingDB.COLUMN_ADDRESS_STREET;
	
	public static final String COLUMN_ADDRESS_CITY = AcceleratedAccountingDB.COLUMN_ADDRESS_CITY;
	
	public static final String COLUMN_ADDRESS_ZIP = AcceleratedAccountingDB.COLUMN_ADDRESS_ZIP;
	
	//project table
	public static final String PROJECT_TABLE = AcceleratedAccountingDB.PROJECT_TABLE;
	
	public static final String COLUMN_PROJECT_ID = AcceleratedAccountingDB.COLUMN_PROJECT_ID;
	
	public static final String COLUMN_PROJECT_NAME = AcceleratedAccountingDB.COLUMN_PROJECT_NAME;
	
	public static final String COLUMN_PROJECT_DESCRIPTION = AcceleratedAccountingDB.COLUMN_PROJECT_DESCRIPTION;
	
	public static final String COLUMN_PROJECT_CUSTOMER_ID = AcceleratedAccountingDB.COLUMN_PROJECT_CUSTOMER_ID;
	
	//project to location table
	public static final String COLUMN_PROJECT_TO_LOCATION_PROJECT_ID = AcceleratedAccountingDB.COLUMN_LOCATION_TO_PROJECT_PROJECT_ID;
	
	public static final String COLUMN_PROJECT_TO_LOCATION_LOCATION_ID = AcceleratedAccountingDB.COLUMN_LOCATION_TO_PROJECT_LOCATION_ID;
	
	// color table
	public static final String COLOR_TABLE = AcceleratedAccountingDB.COLOR_TABLE;
	
	public static final String COLUMN_COLOR_NAME = AcceleratedAccountingDB.COLUMN_COLOR_NAME;
	
	public static final String COLUMN_COLOR_CODE = AcceleratedAccountingDB.COLUMN_COLOR_CODE;
	
	// hours assigned to account table
	public static final String COLUMN_HOURS_ASSIGNED_TO_ACCOUNT_DATE = AcceleratedAccountingDB.COLUMN_HOURS_ASSIGNED_TO_ACCOUNT_DATE;

	public static final String COLUMN_HOURS_ASSIGNED_TO_ACCOUNT_DURATION = AcceleratedAccountingDB.COLUMN_HOURS_ASSIGNED_TO_ACCOUNT_DURATION;

	public static final String COLUMN_HOURS_ASSIGNED_TO_ACCOUNT_LOCATION_NAME = AcceleratedAccountingDB.COLUMN_HOURS_ASSIGNED_TO_ACCOUNT_LOCATION_NAME;

	public static final String COLUMN_HOURS_ASSIGNED_TO_ACCOUNT_PROJECT_ID = AcceleratedAccountingDB.COLUMN_HOURS_ASSIGNED_TO_ACCOUNT_PROJECT_ID;

	public static final String COLUMN_HOURS_ASSIGNED_TO_ACCOUNT_ACCOUNT_ID = AcceleratedAccountingDB.COLUMN_HOURS_ASSIGNED_TO_ACCOUNT_ACCOUNT_ID;

	public static final String COLUMN_HOURS_ASSIGNED_TO_ACCOUNT_NOTE = AcceleratedAccountingDB.COLUMN_HOURS_ASSIGNED_TO_ACCOUNT_NOTE;
	
	// UriMatcher stuff
    private static final int LOCATIONS = 0;
    
    private static final int LOCATION = 1;
    
    private static final int RECORDED_HOUR = 2;
    
    private static final int RECORDED_HOURS = 3;
    
    private static final int CUSTOMER = 4;
    
    private static final int CUSTOMERS = 5;
    
    private static final int ADDRESS = 6;
    
    private static final int ADDRESSES = 7;
    
    private static final int PROJECT = 8;
    
    private static final int PROJECTS = 9;
    
    private static final int PROJECT_TO_LOCATION = 10;
    
    private static final int AVAILABLE_COLORS_FOR_LOCATION = 11;
    
    private static final int HOURS_ASSIGNED_TO_ACCOUNT = 12;
    
    private static final UriMatcher uriMatcher = buildUriMatcher();

    /**
     * Builds up a UriMatcher for search suggestion and shortcut refresh queries.
     */
    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher =  new UriMatcher(UriMatcher.NO_MATCH);
        // to get definitions...
        
        // location uris
        matcher.addURI(AUTHORITY, LOCATIONS_CONTENT_DIRECTORY, LOCATIONS);
        matcher.addURI(AUTHORITY, LOCATIONS_CONTENT_DIRECTORY+"/*", LOCATION);
        
        // recorded hour uris
        matcher.addURI(AUTHORITY, RECORDED_HOURS_CONTENT_DIRECTORY, RECORDED_HOURS);
        matcher.addURI(AUTHORITY, RECORDED_HOURS_CONTENT_DIRECTORY+"/*", RECORDED_HOUR);
        
        // customer uris
        matcher.addURI(AUTHORITY, CUSTOMERS_CONTENT_DIRECTORY, CUSTOMERS);
        matcher.addURI(AUTHORITY, CUSTOMERS_CONTENT_DIRECTORY + "/*", CUSTOMER);
        
        // address uri
        matcher.addURI(AUTHORITY, ADDRESSES_CONTENT_DIRECTORY, ADDRESSES);
        matcher.addURI(AUTHORITY, ADDRESSES_CONTENT_DIRECTORY + "/*", ADDRESS);
        
        // project uri
        matcher.addURI(AUTHORITY, PROJECTS_CONTENT_DIRECTORY, PROJECTS);
        matcher.addURI(AUTHORITY, PROJECTS_CONTENT_DIRECTORY + "/*", PROJECT);
        
        // projects by location uri
        matcher.addURI(AUTHORITY, PROJECT_TO_LOCATION_CONTENT_DIRECTORY, PROJECT_TO_LOCATION);
        
        // available Color for Location Uri
        matcher.addURI(AUTHORITY, AVAILABLE_COLORS_FOR_LOCATION_CONTENT_DIRECTORY, AVAILABLE_COLORS_FOR_LOCATION);
        
        matcher.addURI(AUTHORITY, HOURS_ASSIGNED_TO_ACCOUNT_CONTENT_DIRECTORY, HOURS_ASSIGNED_TO_ACCOUNT);
        return matcher;
    }
    
    
    private AcceleratedAccountingDB db;

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		switch (uriMatcher.match(uri)) {
			case PROJECT_TO_LOCATION:
	        	return deleteProjectToLocation(selection, selectionArgs);
			case LOCATIONS:
				return deleteLocation(selection, selectionArgs);
	        default:
	        	throw new UnsupportedOperationException();
		}
	}
	
	private int deleteProjectToLocation(String where, String[] whereArgs){
		return db.deleteProjectToLocation(where, whereArgs);
	}
	
	private int deleteLocation(String where, String[] whereArgs) {
		return db.deleteLocation(where, whereArgs);
	}
	

	@Override
	public String getType(Uri uri) {
		 switch (uriMatcher.match(uri)) {
         case LOCATION:
            return LOCATION_MIME_TYPE;
         case LOCATIONS:
         	return LOCATIONS_MIME_TYPE;
         case RECORDED_HOUR:
        	 return RECORDED_HOUR_MIME_TYPE;
         case RECORDED_HOURS:
        	 return RECORDED_HOURS_MIME_TYPE;
         case CUSTOMER:
        	 return CUSTOMER_MIME_TYPE;
         case CUSTOMERS:
        	 return CUSTOMERS_MIME_TYPE;
         case ADDRESS:
        	 return ADDRESS_MIME_TYPE;
         case ADDRESSES:
        	 return ADDRESSES_MIME_TYPE;
         case PROJECT:
        	 return PROJECT_MIME_TYPE;
         case PROJECTS:
        	 return PROJECTS_MIME_TYPE;
         case PROJECT_TO_LOCATION:
        	 return PROJECT_TO_LOCATION_MIME_TYPE;
         case AVAILABLE_COLORS_FOR_LOCATION:
        	 return AVAILABLE_COLORS_FOR_LOCATION_MIME_TYPE;
         case HOURS_ASSIGNED_TO_ACCOUNT:
        	 return HOURS_ASSIGNED_TO_ACCOUNT_MIME_TYPE;
         default:
             throw new IllegalArgumentException("Unknown URL " + uri);
		 }
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long rowId = -1;
		switch (uriMatcher.match(uri)) {
        case LOCATIONS:
            rowId = insertLocation(uri, values);
            return Uri.withAppendedPath(uri, String.valueOf(rowId));
        case RECORDED_HOURS:
        	rowId = insertRecordedHour(uri, values);
        	return Uri.withAppendedPath(uri, String.valueOf(rowId));
        case CUSTOMERS:
        	rowId = insertCustomer(values);
        	return Uri.withAppendedPath(uri,  String.valueOf(rowId));
        case ADDRESSES:
        	rowId = insertAddress(values);
        	return Uri.withAppendedPath(uri, String.valueOf(rowId));
        case PROJECTS:
        	rowId = insertProject(values);
        	return Uri.withAppendedPath(uri, String.valueOf(rowId));
        case PROJECT_TO_LOCATION:
        	rowId = insertProjectToLocation(values);
        	return Uri.withAppendedPath(uri, String.valueOf(rowId));
        case HOURS_ASSIGNED_TO_ACCOUNT:
        	rowId = insertHoursAssignedToAccount(values);
        	return Uri.withAppendedPath(uri, String.valueOf(rowId));
        default:
            throw new IllegalArgumentException("Unknown URL " + uri);
		 }
	}

	private long insertLocation(Uri uri, ContentValues values) {
		String locationName = values.getAsString(COLUMN_LOCATION_NAME);
		String locationType = values.getAsString(COLUMN_LOCATION_TYPE);
		String longitude = values.getAsString(COLUMN_LOCATION_LONGITUDE);
		String latitude = values.getAsString(COLUMN_LOCATION_LATITUDE);
		String accuracy = values.getAsString(COLUMN_LOCATION_ACCURACY);
		String color = values.getAsString(COLUMN_LOCATION_COLOR);
		
		if(locationName == null || locationName.equals("")) {
			throw new IllegalArgumentException("Failed to insert new location. Location name not specified.");
		}
		if(locationType == null || locationName.equals("")) {
			throw new IllegalArgumentException("Failed to insert new location. Locatin type not specified.");
		}
		if(longitude == null || longitude.equals("")) {
			throw new IllegalArgumentException("Failed to insert new location. Longitude not specified.");
		}
		if(latitude == null || latitude.equals("")) {
			throw new IllegalArgumentException("Failed to insert new location. Latitude not specified.");
		}
		if(accuracy == null || accuracy.equals("")) {
			throw new IllegalArgumentException("Failed to insert new location. Accuracy not specified.");
		}
		if(color == null || color.equals("")) {
			throw new IllegalArgumentException("Failed to insert new location. Color not specified.");
		}
		
		
		return this.db.insertLocation(locationName, locationType, longitude, latitude, accuracy, color);
	}
	
	private long insertRecordedHour(Uri uri, ContentValues values) {
		String date = values.getAsString(COLUMN_RECORDED_HOURS_DATE);
		String time = values.getAsString(COLUMN_RECORDED_HOURS_TIME);
		String locationName = values.getAsString(COLUMN_LOCATION_NAME);
		String locationType = values.getAsString(COLUMN_LOCATION_TYPE);
		String longitude = values.getAsString(COLUMN_LOCATION_LONGITUDE);
		String latitude = values.getAsString(COLUMN_LOCATION_LATITUDE);
		String accuracy = values.getAsString(COLUMN_LOCATION_ACCURACY);
		String color = values.getAsString(COLUMN_LOCATION_COLOR);
		
		if(date == null || date.equals("")) {
			throw new IllegalArgumentException("Failed to insert new location. Date not specified.");
		}
		if(time == null || time.equals("")) {
			throw new IllegalArgumentException("Failed to insert new location. Time not specified.");
		}
		if(locationName == null || locationName.equals("")) {
			throw new IllegalArgumentException("Failed to insert new location. Location name not specified.");
		}
		if(locationType == null || locationName.equals("")) {
			throw new IllegalArgumentException("Failed to insert new location. Locatin not specified.");
		}
		if(longitude == null || longitude.equals("")) {
			throw new IllegalArgumentException("Failed to insert new location. Longitude not specified.");
		}
		if(latitude == null || latitude.equals("")) {
			throw new IllegalArgumentException("Failed to insert new location. Latitude type not specified.");
		}
		if(accuracy == null || accuracy.equals("")) {
			throw new IllegalArgumentException("Failed to insert new location. Accuracy not specified.");
		}
		if(color == null || color.equals("")) {
			throw new IllegalArgumentException("Failed to insert new location. Color not specified.");
		}		
		
		return this.db.insertRecordedHour(values);
	}
	
	private long insertAddress(ContentValues values){
		return this.db.insertAddress(values);
	}
	
	private long insertCustomer(ContentValues values){
		String name = values.getAsString(COLUMN_CUSTOMER_NAME);
		int addressId = values.getAsInteger(COLUMN_CUSTOMER_ADDRESS_ID);
		
		if(name == null || name.equals("")) {
			throw new IllegalArgumentException("Failed to insert new customer. Name not specified.");
		}
		
		if(addressId == 0){
			throw new IllegalArgumentException("Failed to insert new customer. Address not specified.");
		}		
		
		return this.db.insertCustomer(values);
	}
	
	private long insertProject(ContentValues values){
		String name = values.getAsString(COLUMN_PROJECT_NAME);
		int customerId = values.getAsInteger(COLUMN_PROJECT_CUSTOMER_ID);
		
		if(StringUtil.isNullOrEmpty(name)) {
			throw new IllegalArgumentException("Failed to insert new project. Name not specified.");
		}
		
		if(customerId == 0){
			throw new IllegalArgumentException("Failed to insert new project. Customer not specified.");
		}			
		return this.db.insertProject(values);
	}
	
	private long insertProjectToLocation(ContentValues values){
		int projectId = values.getAsInteger(COLUMN_PROJECT_TO_LOCATION_PROJECT_ID);
		int locationId = values.getAsInteger(COLUMN_PROJECT_TO_LOCATION_LOCATION_ID);
		
		if(projectId == 0) {
			throw new IllegalArgumentException("Failed to insert project to loction. ProjectId not specified.");
		}
		
		if(locationId == 0){
			throw new IllegalArgumentException("Failed to insert project to location. LocationId not specified.");
		}			
		return this.db.insertLocationToProject(values);
	}
	
	private long insertHoursAssignedToAccount(ContentValues values){
		int projectId = values.getAsInteger(COLUMN_HOURS_ASSIGNED_TO_ACCOUNT_PROJECT_ID);
		int accountId = values.getAsInteger(COLUMN_HOURS_ASSIGNED_TO_ACCOUNT_ACCOUNT_ID);
		
		if(projectId == 0) {
			throw new IllegalArgumentException("Failed to insert hours assigned to account. ProjectId not specified.");
		}
		
		if(accountId == 0){
			throw new IllegalArgumentException("Failed to insert hours assigned to account. AccountId not specified.");
		}			
		return this.db.insertHoursAssignedToAccount(values);
	}

	@Override
	public boolean onCreate() {
		this.db = new AcceleratedAccountingDB(getContext());
		
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		switch (uriMatcher.match(uri)) {
        case LOCATION:
            return queryLocation(uri, projection, selection, selectionArgs, sortOrder);
        case LOCATIONS:
        	return queryLocations(uri, projection, selection, selectionArgs, sortOrder);
        case RECORDED_HOURS:
        	return queryRecordedHours(uri, projection, selection, selectionArgs, sortOrder);
        case CUSTOMER:
        	return queryCustomer(uri, projection, selection, selectionArgs, sortOrder);
        case CUSTOMERS:
        	return queryCustomers(uri, projection, selection, selectionArgs, sortOrder);
        case ADDRESS:
        	return queryAddress(uri, projection, selection, selectionArgs, sortOrder);
        case ADDRESSES:
        	return queryAddresses(uri, projection, selection, selectionArgs, sortOrder);
        case PROJECT:
        	return queryProject(uri, projection, selection, selectionArgs, sortOrder);
        case PROJECTS:
        	return queryProjects(uri, projection, selection, selectionArgs, sortOrder);
        case PROJECT_TO_LOCATION:
        	return queryProjectToLocation(uri, projection, selection, selectionArgs, sortOrder);
        case AVAILABLE_COLORS_FOR_LOCATION:
        	return queryAvailableColorsForLocation(uri, projection, selection, selectionArgs, sortOrder);
        case HOURS_ASSIGNED_TO_ACCOUNT:
        	return queryHoursAssignedToAccount(uri, projection, selection, selectionArgs, sortOrder);
        default:
            throw new IllegalArgumentException("Unknown URL " + uri);
		 }
	}
	

	private Cursor queryLocations(Uri uri, String[] projection,
			String selection, String[] selectionArgs, String sortOrder) {
		return this.db.getLocations(projection);
	}

	private Cursor queryLocation(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		String rowId = uri.getLastPathSegment();
		
	    return this.db.getLocation(rowId, projection);
	}
	
	private Cursor queryRecordedHours(Uri uri, String[] projection,
			String selection, String[] selectionArgs, String sortOrder) {
		return this.db.getRecordedHours(projection, selection, selectionArgs, sortOrder);
	}
	
	private Cursor queryCustomer(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		return this.db.getCustomer(uri.getLastPathSegment(), projection);
	}
	
	private Cursor queryCustomers(Uri uri, String[] projection,	String selection, String[] selectionArgs, String sortOrder) {
		return this.db.getCustomers(projection, selection, selectionArgs, sortOrder);
	}
	
	private Cursor queryAddress(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		return this.db.getAddress(uri.getLastPathSegment(), projection);
	}
	
	private Cursor queryAddresses(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		return this.db.getAddresses(projection, selection, selectionArgs, sortOrder);
	}
	
	private Cursor queryProject(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		return this.db.getProject (uri.getLastPathSegment(), projection);
	}
	
	private Cursor queryProjects(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		return this.db.getProjects(projection, selection, selectionArgs, sortOrder);
	}
	
	private Cursor queryProjectToLocation(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		return this.db.getProjectToLocation(projection, selection, selectionArgs, sortOrder);
	}
	
	private Cursor queryAvailableColorsForLocation(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		return this.db.getAvailableColorsForLocation (projection, selection, selectionArgs, sortOrder);
	}
	
	private Cursor queryHoursAssignedToAccount(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		return this.db.getHoursAssignedToAccount (projection, selection, selectionArgs, sortOrder);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		switch (uriMatcher.match(uri)) {
        case CUSTOMERS:
        	return updateCustomers(uri, values, selection, selectionArgs);
        case ADDRESSES:
        	return updateAddresses(uri, values, selection, selectionArgs);
        case PROJECTS:
        	return updateProjects(uri, values, selection, selectionArgs);
        case HOURS_ASSIGNED_TO_ACCOUNT:
        	return updateHoursAssignedToAccount(uri, values, selection, selectionArgs);
        default:
        	throw new UnsupportedOperationException();
		 }
	}
	
	private int updateCustomers(Uri uri, ContentValues values, String selection, String[] selectionArgs){
		String name = values.getAsString(COLUMN_CUSTOMER_NAME);
				
		if(name == null || name.equals("")) {
			throw new IllegalArgumentException("Failed to update customer. Name not specified.");
		}
		
		return this.db.updateCustomers(values, selection, selectionArgs);
	}
	
	private int updateAddresses(Uri uri, ContentValues values, String selection, String[] selectionArgs){
		return this.db.updateAddresses(values, selection, selectionArgs);
	}
	
	private int updateProjects(Uri uri, ContentValues values, String selection, String[] selectionArgs){
		String name = values.getAsString(COLUMN_PROJECT_NAME);
		
		if(StringUtil.isNullOrEmpty(name)) {
			throw new IllegalArgumentException("Failed to update project. Name not specified.");
		}
		
		return this.db.updateProjects(values, selection, selectionArgs);
	}
	
	private int updateHoursAssignedToAccount(Uri uri, ContentValues values, String selection, String[] selectionArgs){
		int projectId = values.getAsInteger(COLUMN_HOURS_ASSIGNED_TO_ACCOUNT_PROJECT_ID);
		int accountId = values.getAsInteger(COLUMN_HOURS_ASSIGNED_TO_ACCOUNT_ACCOUNT_ID);
		
		if(projectId == 0) {
			throw new IllegalArgumentException("Failed to update hours assigned to account. ProjectId not specified.");
		}
		
		if(accountId == 0){
			throw new IllegalArgumentException("Failed to update hours assigned to account. AccountId not specified.");
		}	
		
		return this.db.updateHoursAssignedToAccount(values, selection, selectionArgs);
	}
	
	
}
