package com.games.peter.lab2_matching_game;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by peter on 3/6/18.
 */

public class DatabaseHandler {
    private static final String TAG = "DBAdapter";

    // DB Fields
    public static final String KEY_ROWID = "_id";
    public static final int COL_ROWID = 0;

    public static final String KEY_USER_NAME = "username";
    public static final String KEY_SCORE = "score";
    public static final String KEY_SCORE_TYPE = "score_type";
    public static final String KEY_DATE = "date_time";
    public static final int COL_USERNAME = 1;
    public static final int COL_SCORE = 2;
    public static final int COL_SCORE_TYPE = 3;
    public static final int COL_DATE = 4;

    public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_USER_NAME, KEY_SCORE, KEY_SCORE_TYPE, KEY_DATE};

    public static final String DATABASE_NAME = "Matching_game_db";
    public static final String DATABASE_TABLE = "users";

    // Track DB version
    public static final int DATABASE_VERSION = 1;


    private static final String DATABASE_CREATE_SQL =
            "create table " + DATABASE_TABLE
                    + " (" + KEY_ROWID      + " integer primary key autoincrement, "
                    + KEY_USER_NAME         + " text not null, "
                    + KEY_SCORE             + " integer not null, "
                    + KEY_SCORE_TYPE             + " text not null, "
                    + KEY_DATE              + " string not null"
                    + ");";

    private final Context context;
    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;

    public DatabaseHandler(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    // Open the database connection.
    public DatabaseHandler open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    // Close the database connection.
    public void close() {
        myDBHelper.close();
    }
    public long insertRow(String username, int score, String score_type, String date) {
        // [TO_DO_A8]
        // Update data in the row with new fields.
        // Also change the function's arguments to be what you need!
        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_USER_NAME, username);
        initialValues.put(KEY_SCORE, score);
        initialValues.put(KEY_SCORE_TYPE, score_type);
        initialValues.put(KEY_DATE, date);

        // Insert it into the database.
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean deleteRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        return db.delete(DATABASE_TABLE, where, null) != 0;
    }

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
    public Cursor getAllRows() {
        String where = null;
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public boolean updateRow(long rowId, String username, int score, String score_type,String date) {
        String where = KEY_ROWID + "=" + rowId;

        // [TO_DO_A8]
        // Update data in the row with new fields.
        // Also change the function's arguments to be what you need!
        // Create row's data:
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_USER_NAME, username);
        newValues.put(KEY_SCORE, score);
        newValues.put(KEY_SCORE_TYPE, score_type);
        newValues.put(KEY_DATE, date);


        // Insert it into the database.
        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
    }

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