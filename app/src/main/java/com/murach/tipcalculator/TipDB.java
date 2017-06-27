package com.murach.tipcalculator;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class TipDB {

    //
    public static final String DB_NAME = "tipcalculator.db";
    public static final int DB_VERSION = 1;


    public static final String TIP_TABLE = "tip";

    public static final String TIP_ID = "_id";
    public static final int TIP_ID_COL = 0;

    public static final String BILL_DATE = "bill_date";
    public static final int BILL_DATE_COL = 1;

    public static final String BILL_AMOUNT = "bill_amount";
    public static final int BILL_AMOUNT_COL = 2;

    public static final String TIP_PERCENT = "tip_percent";
    public static final int TIP_PERCENT_COL = 3;

    public static final String CREATE_TIP_TABLE =
            "CREATE TABLE " + TIP_TABLE + " (" +
                    TIP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    BILL_DATE + " TEXT NOT NULL, " +
                    BILL_AMOUNT + " REAL NOT NULL, " +
                    TIP_PERCENT + " REAL NOT NULL);";

    public static final String DROP_TIP_TABLE =
            "DROP TABLE IF EXISTS " + TIP_TABLE;

    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public TipDB(Context context){

        Log.d("Tip", "Inside TipDB constructor");
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

            db.execSQL("INSERT INTO " + TIP_TABLE + " VALUES ('0', 100.00, 0.2)");
            db.execSQL("INSERT INTO " + TIP_TABLE + " VALUES ('1', 10.98, 0.15)");

            Log.d("Tip", "Database created");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            Log.d("Tip", "Upgrading db from version " + oldVersion + " to " + newVersion);

            db.execSQL(TipDB.DROP_TIP_TABLE);
            onCreate(db);

        }
    }

}