// ------------------------------------ DBADapter.java ---------------------------------------------

// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Search for "TO_DO", and make the appropriate changes.
// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++

// [TO_DO_A1]
// Change the package to match your project package name
package com.example.peter.lab5_pedometer.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;




public class DBAdapter {

	/////////////////////////////////////////////////////////////////////
	//	Constants & Data
	/////////////////////////////////////////////////////////////////////
	// For logging:
	private static final String TAG = "DBAdapter";
	
	// DB Fields
	public static final String KEY_ROWID = "_id";
	public static final int COL_ROWID = 0;
	
	// [TO_DO_A2]
	// TODO: Change the field names (column names) of your table
	
	public static final String KEY_DATE = "date";
	public static final String KEY_STEPS = "steps";
	public static final String KEY_CALORIES = "calories";
	public static final String KEY_DISTANCE = "distance";
	public static final String KEY_SPEED = "speed";
	// [TO_DO_A3]
	// Update the field numbers here (0 = KEY_ROWID, 1=...)
	public static final int COL_DATE = 1;
	public static final int COL_STEPS = 2;
	public static final int COL_CALORIES = 3;
	public static final int COL_DISTANCE = 4;
	public static final int COL_SPEED = 5;
	// [TO_DO_A4]
	// Update the ALL-KEYS string array
	public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_DATE, KEY_STEPS, KEY_CALORIES, KEY_DISTANCE, KEY_SPEED};
	
	// [TO_DO_A5]
	// DB info: db name and table name.
	public static final String DATABASE_NAME = "MyDb";
	public static final String DATABASE_TABLE = "mainTable";
	
	// [TO_DO_A6]
	// Track DB version
	public static final int DATABASE_VERSION = 1;	
	
	
	// [TO_DO_A7]
	// DATABASE_CREATE SQL command 
	private static final String DATABASE_CREATE_SQL = 
			"create table " + DATABASE_TABLE 
					+ " (" + KEY_ROWID + " integer primary key autoincrement, "
					+ KEY_DATE         + " text not null, "
					+ KEY_STEPS   + " integer not null, "
					+ KEY_CALORIES    + " float not null, "
					+ KEY_DISTANCE    + " float not null, "
					+ KEY_SPEED    + " float not null "
			+ ");";
	
	// Context of application who uses us.
	private final Context context;
	
	private DatabaseHelper myDBHelper;
	private SQLiteDatabase db;

	// ==================
	//	Public methods:
	// ==================
	
	public DBAdapter(Context ctx) {
		this.context = ctx;
		myDBHelper = new DatabaseHelper(context);
	}
	
	// Open the database connection.
	public DBAdapter open() {
		db = myDBHelper.getWritableDatabase();
		return this;
	}
	
	// Close the database connection.
	public void close() {
		myDBHelper.close();
	}
	
	// Add a new set of values to the database.
	public long insertRow(String date, int steps, float calories, float distance, float speed) {
		// [TO_DO_A8]
		// Update data in the row with new fields.
		// Also change the function's arguments to be what you need!
		// Create row's data:
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_DATE, date);
		initialValues.put(KEY_STEPS, steps);
		initialValues.put(KEY_CALORIES, calories);
		initialValues.put(KEY_DISTANCE, distance);
        initialValues.put(KEY_SPEED, speed);

		// Insert it into the database.
		return db.insert(DATABASE_TABLE, null, initialValues);
	}
	
	// Delete a row from the database, by rowId (primary key)
	public boolean deleteRow(long rowId) {
		String where = KEY_ROWID + "=" + rowId;
		return db.delete(DATABASE_TABLE, where, null) != 0;
	}
	
	// Delete all records
	public void deleteAll() {
		Cursor c = getAllRows();
		long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
		if (c.moveToFirst()) {
			do {
				deleteRow(c.getLong((int) rowId));				
			} while (c.moveToNext());
		}
		c.close();
	}
	
	// Return all rows in the database.
	public Cursor getAllRows() {
		String where = null;
		Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS, 
							where, null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}

	// Get a specific row (by rowId)
	public Cursor getRow(long rowId) {
		String where = KEY_ROWID + "=" + rowId;
		Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS, 
						where, null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}
	
	// Change an existing row to be equal to new data.
	public boolean updateRow(long rowId, String date, int steps, float calories, float distance, float speed) {
		String where = KEY_ROWID + "=" + rowId;

		// [TO_DO_A8]
		// Update data in the row with new fields.
		// Also change the function's arguments to be what you need!
		// Create row's data:
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_DATE, date);
		newValues.put(KEY_STEPS, steps);
		newValues.put(KEY_CALORIES, calories);
		newValues.put(KEY_DISTANCE, distance);
        newValues.put(KEY_SPEED, speed);

		// Insert it into the database.
		return db.update(DATABASE_TABLE, newValues, where, null) != 0;
	}
	
	
	
    // ==================
	//	Private Helper Classes:
	// ==================
	
	/**
	 * Private class which handles database creation and upgrading.
	 * Used to handle low-level database access.
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(DATABASE_CREATE_SQL);			
		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading application's database from version " + oldVersion
					+ " to " + newVersion + ", which will destroy all old data!");
			
			// Destroy old database:
			_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			
			// Recreate new database:
			onCreate(_db);
		}
	}
}
