package com.example.cafebookingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME    = "cafe.db";
    private static final int    DB_VERSION = 1;

    public static final String TABLE    = "orders";
    public static final String COL_ID   = "id";
    public static final String COL_NAME = "name";
    public static final String COL_PHONE= "phone";
    public static final String COL_ITEM = "item";
    public static final String COL_QTY  = "qty";
    public static final String COL_ADDR = "address";
    public static final String COL_PAY  = "payment";
    public static final String COL_NOTE = "note";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " (" +
                COL_ID   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_PHONE+ " TEXT, " +
                COL_ITEM + " TEXT, " +
                COL_QTY  + " TEXT, " +
                COL_ADDR + " TEXT, " +
                COL_PAY  + " TEXT, " +
                COL_NOTE + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int o, int n) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public boolean insertOrder(String name, String phone, String item,
                               String qty, String address,
                               String payment, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COL_NAME, name);
        v.put(COL_PHONE, phone);
        v.put(COL_ITEM, item);
        v.put(COL_QTY, qty);
        v.put(COL_ADDR, address);
        v.put(COL_PAY, payment);
        v.put(COL_NOTE, note);
        return db.insert(TABLE, null, v) != -1;
    }

    public ArrayList<String[]> getAllOrders() {
        ArrayList<String[]> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE + " ORDER BY id DESC", null);
        if (c.moveToFirst()) {
            do {
                list.add(new String[]{
                        c.getString(c.getColumnIndexOrThrow(COL_ITEM)),
                        c.getString(c.getColumnIndexOrThrow(COL_NAME)),
                        c.getString(c.getColumnIndexOrThrow(COL_QTY)),
                        c.getString(c.getColumnIndexOrThrow(COL_ADDR)),
                        c.getString(c.getColumnIndexOrThrow(COL_PAY))
                });
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public void clearAll() {
        getWritableDatabase().delete(TABLE, null, null);
    }
}