package com.example.memo;

import android.accessibilityservice.AccessibilityService;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;

import com.example.memo.Memo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DBAccess {
    private SQLiteDatabase database;
    private DBHelper openHelper;
    private static volatile DBAccess instance;
    private DBAccess(Context context) {
        this.openHelper = new DBHelper(context);
    }

    public static synchronized DBAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DBAccess(context);
        }
        return instance;
    }

    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public void save(Memo memo) {
        ContentValues values = new ContentValues();
        values.put("date", memo.getTime());
        values.put("memo", memo.getText());
        values.put("priority", memo.getPriority());
        /*values.put("importance", memo.getImportance());*/
        database.insert(DBHelper.TABLE, null, values);
    }


    public void update(Memo memo) {
        ContentValues values = new ContentValues();
        values.put("date", new Date().getTime());
        values.put("memo", memo.getText());
        values.put("priority", memo.getPriority());
        String date = Long.toString(memo.getTime());
        database.update(DBHelper.TABLE, values, "date = ?", new String[]{date});
    }


   public void delete(Memo memo) {
       String date = Long.toString(memo.getTime());
       database.delete(DBHelper.TABLE, "date = ?", new String[]{date});
   }


    public ArrayList<Memo> getAllMemos() {

        String q1 = "SELECT * From memo ORDER BY date DESC";
        String q2 = "SELECT * From memo ORDER BY priority DESC";

        ArrayList<Memo> memos = new ArrayList<Memo>();

        try {
            Memo newMemo;
            Cursor cursor = database.rawQuery("SELECT * From memo ORDER BY date DESC", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                long time = cursor.getLong(0);
                String text = cursor.getString(1);
                int priority = cursor.getInt(2);
                memos.add(new Memo(time, text, priority));
                cursor.moveToNext();

            }
            cursor.close();
        }
        catch (Exception e) {
            memos = new ArrayList<Memo>();
        }

        return memos;
    }

    public ArrayList<Memo> sortMemosByImp(String sortField, String SortOrder) {

        ArrayList<Memo> memos = new ArrayList<Memo>();

        try {
            Memo newMemo;
            Cursor cursor = database.rawQuery("SELECT * From memo ORDER BY priority ASC", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                long time = cursor.getLong(0);
                String text = cursor.getString(1);
                int priority = cursor.getInt(2);
                memos.add(new Memo(time, text, priority));
                cursor.moveToNext();

            }
            cursor.close();
        }
        catch (Exception e) {
            memos = new ArrayList<Memo>();
        }

        return memos;
    }



   /*public boolean delete(int MemoId) {
        boolean didDelete = false;

        try {
            didDelete = database.delete("memo", "_id=" + MemoId, null) > 0;
        }
        catch (Exception e) {
            //Do nothing -return value already set to false
        }
        return didDelete;
    }*/

  /*public boolean insertMemo(Memo memo) {
        boolean didSucceed = false;
        try {
            ContentValues values = new ContentValues();

            //initialValues.put("_id", m.getMemoID()); // the ID values are making it complicated

            values.put("memo", memo.getText());
            values.put("memo", memo.getText());
            values.put("importance", memo.getImportance());


            didSucceed = database.insert("memo", null, values) > 0;

        }
        catch (Exception e) {

            //MAYBE ADD A TOAST HERE?

            //Do nothing -will return false if there is an exception
        }
        return didSucceed;
    }


    public boolean updateMemo(Memo memo) {
        boolean didSucceedUpdate = false;

        try {
            Long memoId = (long) memo.getMemoId();
            ContentValues values = new ContentValues();

            values.put("memo", memo.getText());
            values.put("memo", memo.getText());
            values.put("importance", memo.getImportance());

            didSucceedUpdate = database.update("memo", values, "_id=" + memoId, null) > 0;

        }catch (Exception ex){
            //will return false if it didn't work
        }

        return didSucceedUpdate;
    }
    */
   /* public ArrayList<Memo> getMemos(String sortField) {
        ArrayList<Memo> memos = new ArrayList<Memo>();
        try {
            String query = "SELECT  * FROM memo ORDER BY " + sortField;

            Cursor cursor = database.rawQuery(query, null);

            Memo newMemo;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                newMemo = new Memo();
                newMemo.setMemoId(cursor.getInt(0));
                newMemo.setDate(cursor.getString(1));
                newMemo.setText(cursor.getString(2));
                newMemo.setImportance(cursor.getString(3));

                memos.add(newMemo);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            memos = new ArrayList<Memo>();
        }
        return memos;
    } */

}
