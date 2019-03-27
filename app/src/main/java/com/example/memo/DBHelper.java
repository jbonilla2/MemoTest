package com.example.memo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE = "memos.db";
    public static final String TABLE = "memo";
    public static final int VERSION = 2;

    public DBHelper(Context context) {

        super(context, DATABASE, null, VERSION);
    }

    /*private static final String CREATE_TABLE_MEMO =
            "create table memo (_id integer primary key autoincrement, date date, memo text, importance integer );";
     */

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE memo(date INTEGER PRIMARY KEY, memo TEXT, priority integer);");
        //db.execSQL("CREATE TABLE memo(date integer PRIMARY KEY, memo text, importance integer);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to " + newVersion +
               ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS memo");
        onCreate(db);
    }
}
