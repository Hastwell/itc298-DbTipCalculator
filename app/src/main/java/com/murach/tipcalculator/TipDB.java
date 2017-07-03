package com.murach.tipcalculator;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


public class TipDB {

    //database constants
    public static final String DB_NAME = "tipcalculator.db";
    public static final int DB_VERSION = 1;

    //table constant
    public static final String TIP_TABLE = "tip";

    //column constants
    public static final String TIP_ID = "_id";
    public static final int TIP_ID_COL = 0;

    public static final String BILL_DATE = "bill_date_millis";
    public static final int BILL_DATE_COL = 1;

    public static final String BILL_AMOUNT = "bill_amount";
    public static final int BILL_AMOUNT_COL = 2;

    public static final String TIP_PERCENT = "tip_percent";
    public static final int TIP_PERCENT_COL = 3;

    //db command constants
    public static final String CREATE_TIP_TABLE =
            "CREATE TABLE " + TIP_TABLE + " (" +
                    TIP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    BILL_DATE + " INTEGER NOT NULL, " +
                    BILL_AMOUNT + " REAL NOT NULL, " +
                    TIP_PERCENT + " REAL NOT NULL);";

    public static final String DROP_TIP_TABLE =
            "DROP TABLE IF EXISTS " + TIP_TABLE;

    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public TipDB(Context context){

        Log.d("TipsCalc", "Inside TipDB constructor");
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }

    private void openReadableDB() {
        db = dbHelper.getReadableDatabase();
    }

    private  void openWriteableDB() {
        db = dbHelper.getWritableDatabase();
    }

    private void closeDB() {
        if (db != null)
            db.close();
    }

    //add a method to insert new tips into the db

    public long insertTip(Tip tip){
        ContentValues cv = new ContentValues();
        cv.put(BILL_DATE, tip.getDateMillis());
        cv.put(TIP_PERCENT, tip.getTipPercent());
        cv.put(BILL_AMOUNT, tip.getBillAmount());

        Log.d(TipDB.class.getName(), "Adding tip on timestamp " + tip.getDateMillis() + " for " + tip.getBillAmountFormatted() + " with " + tip.getTipPercentFormatted() + " tip");

        this.openWriteableDB();
        long rowID = db.insert(TIP_TABLE, null, cv);
        this.closeDB();
        return rowID;
    }

//    Add a public getTips method that returns an ArrayList<Tip> object that contains all columns and rows from the database table.
    public ArrayList<Tip> getTips() {

        this.openReadableDB();
        Cursor cursor =  db.query(TIP_TABLE, null, null, null, null, null, null);
        ArrayList<Tip> tips = new ArrayList<Tip>();
        while (cursor.moveToNext()) {
            tips.add(getTipFromCursor(cursor));
        }
        //close db connections
        if (cursor != null)
            cursor.close();
        this.closeDB();

        return tips;
    }

    private static Tip getTipFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() <= 0)
            return null;

        try {
            Tip tip = new Tip(
                            cursor.getInt(TIP_ID_COL)
                            ,cursor.getInt(BILL_DATE_COL)
                            ,cursor.getLong(BILL_AMOUNT_COL)
                            ,cursor.getLong(TIP_PERCENT_COL));
            return tip;
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * Created by Conference4 on 6/26/2017.
     */
    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(CREATE_TIP_TABLE);

            db.execSQL("INSERT INTO " + TIP_TABLE + " VALUES (1, 0, 100.00, 0.2)");
            db.execSQL("INSERT INTO " + TIP_TABLE + " VALUES (2, 1, 10.98, 0.15)");

            Log.d("TipsCalc", "Database created");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            Log.d("TipsCalc", "Upgrading db from version " + oldVersion + " to " + newVersion);

            db.execSQL(TipDB.DROP_TIP_TABLE);
            onCreate(db);

        }
    }

}