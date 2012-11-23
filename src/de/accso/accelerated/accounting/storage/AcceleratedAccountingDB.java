package de.accso.accelerated.accounting.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;
import de.accso.accelerated.accounting.util.PropertiesUtil;
import de.accso.accelerated.accounting.util.StringUtil;

public class AcceleratedAccountingDB {

	
	private static final String LOG_TAG = "DataStorage";

	private static final String DATABASE_NAME = "ACCELERATED_ACCOUNTING";

	private static final int DATABASE_VERSION = 35;

	// location names table
	private static final String LOCATION_NAMES_TABLE = "LOCATION_NAMES";

	// locations table
	public static final String LOCATIONS_TABLE = "LOCATIONS";

	public static final String COLUMN_LOCATION_ID = "_id";

	public static final String COLUMN_LOCATION_NAME = "NAME";

	public static final String COLUMN_LOCATION_TYPE = "TYPE";

	public static final String COLUMN_LOCATION_LONGITUDE = "LONGITUDE";

	public static final String COLUMN_LOCATION_LATITUDE = "LATITUDE";

	public static final String COLUMN_LOCATION_ACCURACY = "ACCURACY";

	private static final String COLUMN_LOCATION_FTS_NAME_REF = "FTS_NAME_REF";

	public static final String COLUMN_LOCATION_COLOR = "COLOR";

	// recorded hours table (the table stores locations redudantly)!
	private static final String RECORDED_HOURS_TABLE = "RECORDED_HOURS";

	public static final String COLUMN_RECORDED_HOURS_ID = "_id";

	public static final String COLUMN_RECORDED_HOURS_DATE = "DATE";

	public static final String COLUMN_RECOREDED_HOURS_TIME = "TIME";

	// customer table
	private static final String CUSTOMER_TABLE = "CUSTOMER";

	public static final String COLUMN_CUSTOMER_ID = "_id";

	public static final String COLUMN_CUSTOMER_NAME = "NAME";

	public static final String COLUMN_CUSTOMER_ADDRESS_ID = "ADDRESS_ID";

	// address table
	private static final String ADDRESS_TABLE = "ADDRESS";

	public static final String COLUMN_ADDRESS_ID = "_id";

	public static final String COLUMN_ADDRESS_STREET = "STREET";

	public static final String COLUMN_ADDRESS_CITY = "CITY";

	public static final String COLUMN_ADDRESS_ZIP = "ZIP";

	// project table
	public static final String PROJECT_TABLE = "PROJECT";

	public static final String COLUMN_PROJECT_ID = "_id";

	public static final String COLUMN_PROJECT_NAME = "NAME";

	public static final String COLUMN_PROJECT_DESCRIPTION = "DESCRIPTION";

	public static final String COLUMN_PROJECT_CUSTOMER_ID = "CUSTOMER_ID";

	// account table
	private static final String ACCOUNT_TABLE = "ACCOUNT";

	public static final String COLUMN_ACCOUNT_ID = "_id";

	public static final String COLUMN_ACCOUNT_NAME = "NAME";

	public static final String COLUMN_ACCOUNT_DESCRIPTION = "DESCRIPTION";

	public static final String COLUMN_ACCOUNT_PROJECT_ID = "PROJECT_ID";

	// locationToProject table
	private static final String LOCATION_TO_PROJECT_TABLE = "LOCATION_TO_PROJECT";

	public static final String COLUMN_LOCATION_TO_PROJECT_ID = "_id";

	public static final String COLUMN_LOCATION_TO_PROJECT_LOCATION_ID = "LOCATION_ID";

	public static final String COLUMN_LOCATION_TO_PROJECT_PROJECT_ID = "PROJECT_ID";

	// color table
	public static final String COLOR_TABLE = "COLOR";

	private static final String COLUMN_COLOR_ID = "_id";

	public static final String COLUMN_COLOR_NAME = "NAME";

	public static final String COLUMN_COLOR_CODE = "CODE";

	// hours assigned to account
	private static final String HOURS_ASSIGNED_TO_ACCOUNT_TABLE = "HOURS_ASSIGNED_TO_ACCOUNT";

	public static final String COLUMN_HOURS_ASSIGNED_TO_ACCOUNT_ID = "_id";

	public static final String COLUMN_HOURS_ASSIGNED_TO_ACCOUNT_DATE = "DATE";

	public static final String COLUMN_HOURS_ASSIGNED_TO_ACCOUNT_DURATION = "DURATION";

	public static final String COLUMN_HOURS_ASSIGNED_TO_ACCOUNT_LOCATION_NAME = "LOCATION_NAME";

	public static final String COLUMN_HOURS_ASSIGNED_TO_ACCOUNT_PROJECT_ID = "PROJECT_ID";

	public static final String COLUMN_HOURS_ASSIGNED_TO_ACCOUNT_ACCOUNT_ID = "ACCOUNT_ID";

	public static final String COLUMN_HOURS_ASSIGNED_TO_ACCOUNT_NOTE = "NOTE";
	
	

	private DatabaseOpenHelper openHelper;

	private SQLiteDatabase database;

	public AcceleratedAccountingDB(Context context) {
		this.openHelper = new DatabaseOpenHelper(context);
	}

	
	public synchronized void close() {
		if (this.database != null) {
			this.database.close();
		}
	}

	public long insertLocation(String locationName, String type,
			String longitude, String latitude, String accuracy, String color) {
		// store the location name in full-text-search enabled table...
		ContentValues values = new ContentValues();
		values.put(LOCATION_NAMES_TABLE, locationName);

		long rowId = 0;
		// TODO: SB causes errors on htc...
		// rowId = getDB().insert(LOCATION_NAMES_TABLE, null, values);

		values = new ContentValues();
		values.put(COLUMN_LOCATION_NAME, locationName);
		values.put(COLUMN_LOCATION_TYPE, type);
		values.put(COLUMN_LOCATION_LONGITUDE, longitude);
		values.put(COLUMN_LOCATION_LATITUDE, latitude);
		values.put(COLUMN_LOCATION_ACCURACY, accuracy);
		values.put(COLUMN_LOCATION_FTS_NAME_REF, rowId);
		values.put(COLUMN_LOCATION_COLOR, color);

		return getDB().insert(LOCATIONS_TABLE, null, values);
	}

	public Cursor getLocation(String rowId, String[] columns) {
		String selection = "rowid = ?";
		String orderBy = COLUMN_LOCATION_NAME + " asc";
		String[] selectionArgs = new String[] { rowId };

		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables(LOCATIONS_TABLE);

		Cursor cursor = builder.query(getDB(), columns, selection,
				selectionArgs, null, null, orderBy);

		if (cursor == null) {
			return null;
		}

		return cursor;
	}

	public Cursor getLocations(String[] columns) {
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables(LOCATIONS_TABLE);

		Cursor cursor = builder.query(getDB(), columns, null, null, null, null,
				null);

		if (cursor == null) {
			return null;
		}

		return cursor;
	}

	public long insertRecordedHour(ContentValues values) {
		return getDB().insert(RECORDED_HOURS_TABLE, null, values);
	}

	public Cursor getRecordedHours(String[] columns, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables(RECORDED_HOURS_TABLE);

		Cursor cursor = builder.query(getDB(), columns, selection,
				selectionArgs, null, null, sortOrder);

		if (cursor == null) {
			return null;
		} else if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}

		return cursor;
	}

	public Cursor getRecordedHour(String rowId, String[] columns) {
		String selection = "rowid = ?";
		String[] selectionArgs = new String[] { rowId };

		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables(RECORDED_HOURS_TABLE);

		Cursor cursor = builder.query(getDB(), columns, selection,
				selectionArgs, null, null, null);

		if (cursor == null) {
			return null;
		} else if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}

		return cursor;
	}

	public Cursor getCustomer(String rowId, String[] columns) {
		String selection = "rowid = ?";
		String[] selectionArgs = new String[] { rowId };

		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables(CUSTOMER_TABLE);

		Cursor cursor = builder.query(getDB(), columns, selection,
				selectionArgs, null, null, null);

		if (cursor == null) {
			return null;
		} else if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}

		return cursor;
	}

	public Cursor getCustomers(String[] columns, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables(CUSTOMER_TABLE);

		Cursor cursor = builder.query(getDB(), columns, selection,
				selectionArgs, null, null, sortOrder);

		if (cursor == null) {
			return null;
		} else if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}

		return cursor;
	}

	public Cursor getAddress(String rowId, String[] columns) {
		String selection = "rowid = ?";
		String[] selectionArgs = new String[] { rowId };

		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables(ADDRESS_TABLE);

		Cursor cursor = builder.query(getDB(), columns, selection,
				selectionArgs, null, null, null);

		if (cursor == null) {
			return null;
		} else if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}

		return cursor;
	}

	public Cursor getAddresses(String[] columns, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables(ADDRESS_TABLE);

		Cursor cursor = builder.query(getDB(), columns, selection,
				selectionArgs, null, null, sortOrder);

		if (cursor == null) {
			return null;
		} else if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}

		return cursor;
	}

	public Cursor getProject(String rowId, String[] columns) {
		String selection = "rowid = ?";
		String[] selectionArgs = new String[] { rowId };
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables(PROJECT_TABLE);
		Cursor cursor = builder.query(getDB(), columns, selection,
				selectionArgs, null, null, null);

		if (cursor == null) {
			return null;
		} else if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}

		return cursor;
	}

	public Cursor getProjects(String[] columns, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables(PROJECT_TABLE);
		Cursor cursor = builder.query(getDB(), columns, selection,
				selectionArgs, null, null, sortOrder);

		if (cursor == null) {
			return null;
		} else if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}

		return cursor;
	}

	public Cursor getProjectToLocation(String[] columns, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		String tables = PROJECT_TABLE + " JOIN " + LOCATION_TO_PROJECT_TABLE
				+ " ON (" + PROJECT_TABLE + "." + COLUMN_PROJECT_ID + " = "
				+ LOCATION_TO_PROJECT_TABLE + "."
				+ COLUMN_LOCATION_TO_PROJECT_PROJECT_ID + ") " + "JOIN "
				+ LOCATIONS_TABLE + " ON (" + LOCATIONS_TABLE + "."
				+ COLUMN_LOCATION_ID + " = " + LOCATION_TO_PROJECT_TABLE + "."
				+ COLUMN_LOCATION_TO_PROJECT_LOCATION_ID + ")";
		builder.setTables(tables);
		Cursor cursor = builder.query(getDB(), columns, selection,
				selectionArgs, null, null, sortOrder);

		if (cursor == null) {
			return null;
		} else if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}

		return cursor;
	}

	public Cursor getAvailableColorsForLocation(String[] columns,
			String selection, String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		String tables = COLOR_TABLE + " LEFT OUTER JOIN " + LOCATIONS_TABLE
				+ " ON (" + COLOR_TABLE + "." + COLUMN_COLOR_CODE + " = "
				+ LOCATIONS_TABLE + "." + COLUMN_LOCATION_COLOR + ")";
		builder.setTables(tables);
		Cursor cursor = builder.query(getDB(), columns, selection,
				selectionArgs, null, null, sortOrder);

		if (cursor == null) {
			return null;
		} else if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}

		return cursor;
	}
	
	
	public Cursor getHoursAssignedToAccount(String[] columns, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables(HOURS_ASSIGNED_TO_ACCOUNT_TABLE);
		Cursor cursor = builder.query(getDB(), columns, selection, selectionArgs, null, null, sortOrder);

		if (cursor == null) {
			return null;
		} else if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}

		return cursor;
	}

	public int deleteProjectToLocation(String where, String[] whereArgs) {
		return getDB().delete(LOCATION_TO_PROJECT_TABLE, where, whereArgs);
	}

	public int deleteLocation(String where, String[] whereArgs) {
		return getDB().delete(LOCATIONS_TABLE, where, whereArgs);
	}
	
	public long insertCustomer(ContentValues values) {
		return getDB().insert(CUSTOMER_TABLE, null, values);
	}

	public long insertAddress(ContentValues values) {
		return getDB().insert(ADDRESS_TABLE, null, values);
	}

	public long insertProject(ContentValues values) {
		return getDB().insert(PROJECT_TABLE, null, values);
	}

	public long insertLocationToProject(ContentValues values) {
		return getDB().insert(LOCATION_TO_PROJECT_TABLE, null, values);
	}
	
	public long insertHoursAssignedToAccount(ContentValues values) {
		return getDB().insert(HOURS_ASSIGNED_TO_ACCOUNT_TABLE, null, values);
	}

	public int updateCustomers(ContentValues values, String whereClause,
			String[] whereArgs) {
		return getDB().update(CUSTOMER_TABLE, values, whereClause, whereArgs);
	}

	public int updateAddresses(ContentValues values, String whereClause,
			String[] whereArgs) {
		return getDB().update(ADDRESS_TABLE, values, whereClause, whereArgs);
	}

	public int updateProjects(ContentValues values, String whereClause,
			String[] whereArgs) {
		return getDB().update(PROJECT_TABLE, values, whereClause, whereArgs);
	}
	
	public int updateHoursAssignedToAccount(ContentValues values, String whereClause, String[] whereArgs) {
		return getDB().update(HOURS_ASSIGNED_TO_ACCOUNT_TABLE, values, whereClause, whereArgs);
	}

	private synchronized SQLiteDatabase getDB() {
		if (this.database == null || !this.database.isOpen()) {
			this.database = this.openHelper.getWritableDatabase();
		}

		return this.database;
	}

	/**
	 * This creates/opens the database.
	 */
	private static class DatabaseOpenHelper extends SQLiteOpenHelper {
		
		private Context context;

		/*
		 * Note that FTS3 does not support column constraints and thus, you
		 * cannot declare a primary key. However, "rowid" is automatically used
		 * as a unique identifier.
		 */
		/**
		 * Full-text search enabled table with all location names.
		 */
		private static final String LOCATION_NAMES_TABLE_CREATE = "CREATE VIRTUAL TABLE "
				+ LOCATION_NAMES_TABLE + " USING fts3();";

		private static final String LOCATIONS_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
				+ LOCATIONS_TABLE
				+ "("
				+ COLUMN_LOCATION_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COLUMN_LOCATION_NAME
				+ " TEXT, "
				+ COLUMN_LOCATION_TYPE
				+ " TEXT, "
				+ COLUMN_LOCATION_LONGITUDE
				+ " TEXT, "
				+ COLUMN_LOCATION_LATITUDE
				+ " TEXT, "
				+ COLUMN_LOCATION_ACCURACY
				+ " TEXT, "
				+ COLUMN_LOCATION_FTS_NAME_REF
				+ " INTEGER, "
				+ COLUMN_LOCATION_COLOR + " TEXT" + ");";

		private static final String RECORDED_HOURS_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
				+ RECORDED_HOURS_TABLE
				+ "("
				+ COLUMN_RECORDED_HOURS_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COLUMN_RECORDED_HOURS_DATE
				+ " TEXT, "
				+ COLUMN_RECOREDED_HOURS_TIME
				+ " TEXT, "
				+ COLUMN_LOCATION_NAME
				+ " TEXT, "
				+ COLUMN_LOCATION_TYPE
				+ " TEXT, "
				+ COLUMN_LOCATION_LONGITUDE
				+ " TEXT, "
				+ COLUMN_LOCATION_LATITUDE
				+ " TEXT, "
				+ COLUMN_LOCATION_ACCURACY
				+ " TEXT, "
				+ COLUMN_LOCATION_FTS_NAME_REF
				+ " INTEGER, "
				+ COLUMN_LOCATION_COLOR + " TEXT" + ");";

		private static final String ADDRESS_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
				+ ADDRESS_TABLE
				+ "("
				+ COLUMN_ADDRESS_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COLUMN_ADDRESS_STREET
				+ " TEXT, "
				+ COLUMN_ADDRESS_CITY
				+ " TEXT, " + COLUMN_ADDRESS_ZIP + " TEXT " + ");";

		private static final String CUSTOMER_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
				+ CUSTOMER_TABLE
				+ "("
				+ COLUMN_CUSTOMER_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COLUMN_CUSTOMER_NAME
				+ " TEXT, "
				+ COLUMN_CUSTOMER_ADDRESS_ID
				+ " INTEGER, "
				+ "FOREIGN KEY("
				+ COLUMN_CUSTOMER_ADDRESS_ID
				+ ") REFERENCES "
				+ ADDRESS_TABLE + " (" + COLUMN_ADDRESS_ID + ")" + ");";

		private static final String PROJECT_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
				+ PROJECT_TABLE
				+ "("
				+ COLUMN_PROJECT_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COLUMN_PROJECT_NAME
				+ " TEXT, "
				+ COLUMN_PROJECT_DESCRIPTION
				+ " TEXT, "
				+ COLUMN_PROJECT_CUSTOMER_ID
				+ " INTEGER, "
				+ "FOREIGN KEY("
				+ COLUMN_PROJECT_CUSTOMER_ID
				+ ") REFERENCES "
				+ CUSTOMER_TABLE
				+ " (" + COLUMN_CUSTOMER_ID + ")" + ");";

		private static final String ACCOUNT_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
				+ ACCOUNT_TABLE
				+ "("
				+ COLUMN_ACCOUNT_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COLUMN_ACCOUNT_NAME
				+ " TEXT, "
				+ COLUMN_ACCOUNT_DESCRIPTION
				+ " TEXT, "
				+ COLUMN_ACCOUNT_PROJECT_ID
				+ " INTEGER, "
				+ "FOREIGN KEY("
				+ COLUMN_ACCOUNT_PROJECT_ID
				+ ") REFERENCES "
				+ PROJECT_TABLE
				+ " (" + COLUMN_PROJECT_ID + ")" + ");";

		private static final String LOCATION_TO_PROJECT_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
				+ LOCATION_TO_PROJECT_TABLE
				+ "("
				+ COLUMN_LOCATION_TO_PROJECT_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COLUMN_LOCATION_TO_PROJECT_LOCATION_ID
				+ " INTEGER, "
				+ COLUMN_LOCATION_TO_PROJECT_PROJECT_ID
				+ " INTEGER, "
				+ "FOREIGN KEY("
				+ COLUMN_LOCATION_TO_PROJECT_LOCATION_ID
				+ ") REFERENCES "
				+ LOCATIONS_TABLE
				+ " ("
				+ COLUMN_LOCATION_ID
				+ "), "
				+ "FOREIGN KEY("
				+ COLUMN_LOCATION_TO_PROJECT_PROJECT_ID
				+ ") REFERENCES "
				+ PROJECT_TABLE + " (" + COLUMN_PROJECT_ID + ")" + ");";

		private static final String COLOR_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
				+ COLOR_TABLE
				+ "("
				+ COLUMN_COLOR_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COLUMN_COLOR_NAME
				+ " TEXT, " + COLUMN_COLOR_CODE + " TEXT " + ");";

		private static final String HOURS_ASSIGNED_TO_ACCOUNT_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
				+ HOURS_ASSIGNED_TO_ACCOUNT_TABLE
				+ " ("
				+ COLUMN_HOURS_ASSIGNED_TO_ACCOUNT_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COLUMN_HOURS_ASSIGNED_TO_ACCOUNT_DATE
				+ " TEXT, "
				+ COLUMN_HOURS_ASSIGNED_TO_ACCOUNT_DURATION
				+ " TEXT, "
				+ COLUMN_HOURS_ASSIGNED_TO_ACCOUNT_LOCATION_NAME
				+ " TEXT, "
				+ COLUMN_HOURS_ASSIGNED_TO_ACCOUNT_NOTE
				+ " TEXT, "
				+ COLUMN_HOURS_ASSIGNED_TO_ACCOUNT_PROJECT_ID
				+ " INTEGER, "
				+ COLUMN_HOURS_ASSIGNED_TO_ACCOUNT_ACCOUNT_ID
				+ " INTEGER,"
				+ "FOREIGN KEY("
				+ COLUMN_HOURS_ASSIGNED_TO_ACCOUNT_PROJECT_ID
				+ ") REFERENCES "
				+ PROJECT_TABLE
				+ " ("
				+ COLUMN_PROJECT_ID
				+ "), "
				+ "FOREIGN KEY("
				+ COLUMN_HOURS_ASSIGNED_TO_ACCOUNT_ACCOUNT_ID
				+ ") REFERENCES "
				+ ACCOUNT_TABLE + " (" + COLUMN_ACCOUNT_ID + ")" + ");";

		private DatabaseOpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			
			this.context = context;
			File file = context.getDatabasePath(DATABASE_NAME);
			long dbSize = file.length();

			Log.d(LOG_TAG, "Database size: " + dbSize);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(LOCATION_NAMES_TABLE_CREATE);
			db.execSQL(LOCATIONS_TABLE_CREATE);
			db.execSQL(RECORDED_HOURS_TABLE_CREATE);
			db.execSQL(ADDRESS_TABLE_CREATE);
			db.execSQL(CUSTOMER_TABLE_CREATE);
			db.execSQL(PROJECT_TABLE_CREATE);
			db.execSQL(ACCOUNT_TABLE_CREATE);
			db.execSQL(LOCATION_TO_PROJECT_TABLE_CREATE);
			db.execSQL(COLOR_TABLE_CREATE);
			db.execSQL(HOURS_ASSIGNED_TO_ACCOUNT_TABLE_CREATE);
			initialInsertProjects(db);
			initialInsertAccount(db);
			initialInsertColors(db);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(LOG_TAG, "Upgrading database from version " + oldVersion
					+ " to " + newVersion);

			db.execSQL("DROP TABLE IF EXISTS " + LOCATION_NAMES_TABLE);

			if (PropertiesUtil.dropTablesOnDatabaseUpgrade(context)) {
				db.execSQL("DROP TABLE IF EXISTS " + LOCATIONS_TABLE);
				db.execSQL("DROP TABLE IF EXISTS " + RECORDED_HOURS_TABLE);
				db.execSQL("DROP TABLE IF EXISTS " + ADDRESS_TABLE);
				db.execSQL("DROP TABLE IF EXISTS " + CUSTOMER_TABLE);
				db.execSQL("DROP TABLE IF EXISTS " + PROJECT_TABLE);
				db.execSQL("DROP TABLE IF EXISTS " + ACCOUNT_TABLE);
				db.execSQL("DROP TABLE IF EXISTS " + LOCATION_TO_PROJECT_TABLE);
				db.execSQL("DROP TABLE IF EXISTS " + COLOR_TABLE);
				db.execSQL("DROP TABLE IF EXISTS "
						+ HOURS_ASSIGNED_TO_ACCOUNT_TABLE);
			}
			onCreate(db);
		}

		private void initialInsertProjects(SQLiteDatabase db) {
			try {
				AssetManager assetManager = context.getResources().getAssets();
				InputStream inputStream = assetManager.open("Projects.csv");
				InputStreamReader streamReader = new InputStreamReader(
						inputStream);
				BufferedReader in = new BufferedReader(streamReader);
				String readString;
				while ((readString = in.readLine()) != null) {
					if (!StringUtil.isNullOrEmpty(readString)) {
						String[] projectArray = readString.split(";", 2);
						db.execSQL("INSERT INTO " + PROJECT_TABLE + " ("
								+ COLUMN_PROJECT_NAME + ", "
								+ COLUMN_PROJECT_DESCRIPTION + ") "
								+ "SELECT '" + projectArray[0] + "', '"
								+ projectArray[1] + "' "
								+ "WHERE NOT EXISTS (SELECT 1 FROM "
								+ PROJECT_TABLE + " WHERE "
								+ COLUMN_PROJECT_NAME + " == '"
								+ projectArray[0] + "')");
					}
				}
				streamReader.close();
				in.close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		private void initialInsertAccount(SQLiteDatabase db) {
			try {
				AssetManager assetManager = context.getResources().getAssets();
				InputStream inputStream = assetManager.open("Account.csv");
				InputStreamReader streamReader = new InputStreamReader(
						inputStream);
				BufferedReader in = new BufferedReader(streamReader);
				String readString;
				while ((readString = in.readLine()) != null) {
					if (!StringUtil.isNullOrEmpty(readString)) {
						String[] accountArray = readString.split(";", 3);
						db.execSQL("INSERT INTO " + ACCOUNT_TABLE + "("
								+ COLUMN_ACCOUNT_NAME + ", "
								+ COLUMN_ACCOUNT_DESCRIPTION + ", "
								+ COLUMN_ACCOUNT_PROJECT_ID + ") " + "SELECT '"
								+ accountArray[0] + "', '" + accountArray[1]
								+ "', (SELECT " + COLUMN_PROJECT_ID + " FROM "
								+ PROJECT_TABLE + " WHERE NAME = '"
								+ accountArray[2] + "') "
								+ "WHERE NOT EXISTS (SELECT 1 FROM "
								+ ACCOUNT_TABLE + " WHERE "
								+ COLUMN_ACCOUNT_NAME + " = '"
								+ accountArray[0] + "' AND "
								+ COLUMN_ACCOUNT_PROJECT_ID + " = (SELECT "
								+ COLUMN_PROJECT_ID + " FROM " + PROJECT_TABLE
								+ " WHERE " + COLUMN_PROJECT_NAME + " = '"
								+ accountArray[2] + "'))");
					}
				}
				streamReader.close();
				in.close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		private void initialInsertColors(SQLiteDatabase db) {
			try {
				AssetManager assetManager = context.getResources().getAssets();
				InputStream inputStream = assetManager.open("Colors.csv");
				InputStreamReader streamReader = new InputStreamReader(
						inputStream);
				BufferedReader in = new BufferedReader(streamReader);
				String readString;
				while ((readString = in.readLine()) != null) {
					if (!StringUtil.isNullOrEmpty(readString)) {
						String[] colorArray = readString.split(";");
						db.execSQL("INSERT INTO " + COLOR_TABLE + " ("
								+ COLUMN_COLOR_NAME + ", " + COLUMN_COLOR_CODE
								+ ") " + "SELECT '" + colorArray[0] + "', '"
								+ colorArray[1] + "' "
								+ "WHERE NOT EXISTS (SELECT 1 FROM "
								+ COLOR_TABLE + " WHERE " + COLUMN_COLOR_NAME
								+ " == '" + colorArray[0] + "')");
					}
				}
				streamReader.close();
				in.close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
