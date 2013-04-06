package com.example.assignment2;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	// Declare static variables
	
	// Database version
	private static final int DATABASE_VERSION = 1;

	// Database name
	private static final String DATABASE_NAME = "UserLocationDB";

	// Table name
	private static final String USER_LOCATION_DATA_TABLE = "UserLocations";

	// UserLocations table column names
	private static final String ID = "id";
	private static final String ACCEL_X = "accelX";
	private static final String ACCEL_Y = "accelY";
	private static final String ACCEL_Z = "accelZ";
	private static final String ORIENTATION = "orientation";
	private static final String LAT = "latitude";
	private static final String LONG = "longitude";
	private static final String ADDRESS = "address";
	private static final String ACTIVITY = "activity";

	// Calls the SQLiteOpenHelper constructor
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Create the UserLocations table
	@Override
	public void onCreate(SQLiteDatabase db) {
		String createUserLocationsTable = "CREATE TABLE " + USER_LOCATION_DATA_TABLE + "("
				+ ID + " INTEGER PRIMARY KEY," 
				+ ACCEL_X + " FLOAT," + ACCEL_Y + " FLOAT," + ACCEL_Z + " FLOAT," 
				+ ORIENTATION + " INTEGER,"
				+ LAT + " DOUBLE," + LONG + " DOUBLE," 
				+ ADDRESS + " TEXT," 
				+ ACTIVITY + " TEXT" + ")";
		db.execSQL(createUserLocationsTable);
	}

	// Upgrade the database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + USER_LOCATION_DATA_TABLE);

		// Create tables again
		onCreate(db);
	}

	/********** Static methods for retrieving database information **********/
	
	static public String getTableName () {
		return USER_LOCATION_DATA_TABLE;
	}
	
	static public String getColumnNameID () {
		return ID;
	}
	
	static public String getColumnNameAccelX () {
		return ACCEL_X;
	}
	
	static public String getColumnNameAccelY () {
		return ACCEL_Y;
	}
	
	static public String getColumnNameAccelZ () {
		return ACCEL_Z;
	}
	
	static public String getColumnNameOrientation () {
		return ORIENTATION;
	}
	
	static public String getColumnNameLatitude () {
		return LAT;
	}
	
	static public String getColumnNameLongitude () {
		return LONG;
	}
	
	static public String getColumnNameAddress () {
		return ADDRESS;
	}
	
	static public String getColumnNameActivity () {
		return ACTIVITY;
	}
	
	/********** Methods used for accessing and manipulating database objects **********/
	
	// Adds a single record into the database
	void addEntry(UserLocationData entry) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(ACCEL_X, entry.getAccelX());
		values.put(ACCEL_Y, entry.getAccelY());
		values.put(ACCEL_Z, entry.getAccelZ());
		values.put(ORIENTATION, entry.getOrientation());
		values.put(LAT, entry.getLong());
		values.put(LONG, entry.getLat());
		values.put(ADDRESS, entry.getAddress());
		values.put(ACTIVITY, entry.getActivity());

		// Inserting Row
		db.insert(USER_LOCATION_DATA_TABLE, null, values);
	}

	// Retrieves a single record from the UserLocations table
	UserLocationData getEntry(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(USER_LOCATION_DATA_TABLE, new String[] { ID,
				ACCEL_X, ACCEL_Y, ACCEL_Z, 
				ORIENTATION,
				LAT, LONG, 
				ADDRESS, ACTIVITY }, ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);

		UserLocationData entry = new UserLocationData();
		
		if (cursor != null) {
			cursor.moveToFirst();
			entry.setID(Integer.parseInt(cursor.getString(0)));
			entry.setAccelX(Float.parseFloat(cursor.getString(1)));
			entry.setAccelY(Float.parseFloat(cursor.getString(2)));
			entry.setAccelZ(Float.parseFloat(cursor.getString(3)));
			entry.setOrientation(Integer.parseInt(cursor.getString(4)));
			entry.setLat(Double.parseDouble(cursor.getString(5)));
			entry.setLong(Double.parseDouble(cursor.getString(6)));
			entry.setAddress(cursor.getString(7));
			entry.setActivity(cursor.getString(8));
		}

		// return UserLocation record
		return entry;
	}
	
	// Gets all UserLocationData records
	public List<UserLocationData> getAllEntries() {
		List<UserLocationData> entryList = new ArrayList<UserLocationData>();

		String selectQuery = "SELECT  * FROM " + USER_LOCATION_DATA_TABLE;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// While there are rows to retrieve, get all of them
		if (cursor.moveToFirst()) {
			do {
				UserLocationData entry = new UserLocationData();
				entry.setID(Integer.parseInt(cursor.getString(0)));
				entry.setAccelX(Float.parseFloat(cursor.getString(1)));
				entry.setAccelY(Float.parseFloat(cursor.getString(2)));
				entry.setAccelZ(Float.parseFloat(cursor.getString(3)));
				entry.setOrientation(Integer.parseInt(cursor.getString(4)));
				entry.setLat(Double.parseDouble(cursor.getString(5)));
				entry.setLong(Double.parseDouble(cursor.getString(6)));
				entry.setAddress(cursor.getString(7));
				entry.setActivity(cursor.getString(8));
				// Adding UserLocationData record to list
				entryList.add(entry);
			} while (cursor.moveToNext());
		}
		
		cursor.close();

		// Return the list of UserLocations records
		return entryList;
	}

	// Updates the database record according to the ID
	public int updateUserLocationData(UserLocationData entry) {
		SQLiteDatabase db = this.getWritableDatabase();
		int rowsAffected = 0;
		
		ContentValues values = new ContentValues();
		values.put(ACCEL_X, entry.getAccelX());
		values.put(ACCEL_Y, entry.getAccelY());
		values.put(ACCEL_Z, entry.getAccelZ());
		values.put(ORIENTATION, entry.getOrientation());
		values.put(LAT, entry.getLat());
		values.put(LONG, entry.getLong());
		values.put(ADDRESS,  entry.getAddress());
		values.put(ACTIVITY, entry.getActivity());

		// Run the update
		rowsAffected = db.update(USER_LOCATION_DATA_TABLE, values, ID + " = ?",
				new String[] { String.valueOf(entry.getID()) });
		
		db.close();
		return rowsAffected;
	}

	// Delete a single UserLocationData record
	public void deleteUserLocationData(UserLocationData entry) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(USER_LOCATION_DATA_TABLE, ID + " = ?",
				new String[] { String.valueOf(entry.getID()) });
		db.close();
	}

	// Get the row count from the UserLocations table
	public int getUserLocationDataCount() {
		String countQuery = "SELECT  * FROM " + USER_LOCATION_DATA_TABLE;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		
		// return count
		int userLocationsCount = cursor.getCount();
		cursor.close();
		return userLocationsCount;
	}
}
