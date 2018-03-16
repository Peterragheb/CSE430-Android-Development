package com.example.peter.dbdemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by peter on 3/6/18.
 */

public class dbadapter {

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

    public static final String KEY_USER_NAME = "username";
    public static final String KEY_SCORE = "score";
    public static final String KEY_DATE = "date_time";

    // [TO_DO_A3]
    // Update the field numbers here (0 = KEY_ROWID, 1=...)
    public static final int COL_USERNAME = 1;
    public static final int COL_SCORE = 2;
    public static final int COL_DATE = 3;

    // [TO_DO_A4]
    // Update the ALL-KEYS string array
    public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_USER_NAME, KEY_SCORE, KEY_DATE};

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
                    + " (" + KEY_ROWID      + " integer primary key autoincrement, "
                    + KEY_USER_NAME         + " text not null, "
                    + KEY_SCORE             + " integer not null, "
                    + KEY_DATE              + " string not null"
                    + ");";

    // Context of application who uses us.
    private final Context context;

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;

    // ==================
    //	Public methods:
    // ==================

    public dbadapter(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    // Open the database connection.
    public dbadapter open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    // Close the database connection.
    public void close() {
        myDBHelper.close();
    }


    // Add a new set of values to the database.
    public long insertRow(String username, int score, String date) {
        // [TO_DO_A8]
        // Update data in the row with new fields.
        // Also change the function's arguments to be what you need!
        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_USER_NAME, username);
        initialValues.put(KEY_SCORE, score);
        initialValues.put(KEY_DATE, date);

        // Insert it into the database.
        return db.insert(DATABASE_TABLE, null, initialValues);
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


