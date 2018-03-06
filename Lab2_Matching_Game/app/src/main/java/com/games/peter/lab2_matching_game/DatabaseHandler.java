package com.games.peter.lab2_matching_game;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by peter on 3/6/18.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Matching_game_db.db";
    public static final String USERS_TABLE_NAME = "users";
    public static final String USERS_COLUMN_ID = "id";
    public static final String USERS_COLUMN_USERNAME = "username";
    public static final String USERS_COLUMN_SCORE = "score";
    public static final String[] ALL_KEYS = new String[] {USERS_COLUMN_ID, USERS_COLUMN_USERNAME, USERS_COLUMN_SCORE};

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "create table "+USERS_TABLE_NAME +
                        " ("+USERS_COLUMN_ID +" integer primary key, "+USERS_COLUMN_USERNAME+" text, "+USERS_COLUMN_SCORE+" text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+USERS_TABLE_NAME);
        onCreate(sqLiteDatabase);

    }

    public boolean insertUser (String username, String score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERS_COLUMN_USERNAME, username);
        if (score!=null)
            contentValues.put(USERS_COLUMN_SCORE, score);
        db.insert(USERS_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getUserData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+USERS_TABLE_NAME+"where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, USERS_TABLE_NAME);
        return numRows;
    }

    public boolean updateUser (Integer id, String username, String score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if (username!=null)
            contentValues.put(USERS_COLUMN_USERNAME, username);
        if (score!=null)
            contentValues.put(USERS_COLUMN_SCORE, score);
        db.update(USERS_TABLE_NAME, contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteUser (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(USERS_TABLE_NAME,
                "id = ? ",
                new String[] { Integer.toString(id) });
    }
    public boolean deleteRow(long rowId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = USERS_COLUMN_ID + "=" + rowId;
        return db.delete(USERS_TABLE_NAME, where, null) != 0;
    }
    public void deleteAll() {
        Cursor c = getAllRows();
        long rowId = c.getColumnIndexOrThrow(USERS_COLUMN_ID);
        if (c.moveToFirst()) {
            do {
                deleteRow(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
    }
    public ArrayList<User> getAllUsers() {
        ArrayList<User> array_list = new ArrayList<User>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+USERS_TABLE_NAME, null );
        res.moveToFirst();

        while(!res.isAfterLast()){//need to add all user attributes
            array_list.add(new User(res.getString(res.getColumnIndex(USERS_COLUMN_USERNAME)),
                    res.getString(res.getColumnIndex(USERS_COLUMN_SCORE))));
            res.moveToNext();
        }
        return array_list;
    }
    public Cursor getAllRows() {
        String where = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = 	db.query(true, USERS_TABLE_NAME, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
}