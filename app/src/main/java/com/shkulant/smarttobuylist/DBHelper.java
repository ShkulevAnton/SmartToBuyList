package com.shkulant.smarttobuylist;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "smarttobuyDB";
    public static final String TABLE_LISTS = "lists";
    public static final String TABLE_PRODUCTS = "products";
    public static final String TABLE_BUYLIST = "buylist";

    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_PRODUCT = "product_name";
    public static final String KEY_DEFAMOUNT = "def_amount";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_MEASURE = "measure";
    public static final String KEY_CHECKED = "checked";
    public static final String KEY_STORAGE = "storage";

    public String[] products_names = {"Milk", "Bread", "Sausage", "Eggs", "Cheese"};
    public String[] products_measure = {"L.", "loaf", "kg.", "ten", "kg."};
    public Double[] products_defamount = {1.0, 1.0, 0.5, 1.0, 0.5};
    public Integer[] products_storage = {2, 3, 2, 3, 5};

    private ContentValues cv = new ContentValues();

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_LISTS + "(" + KEY_ID + " integer primary key," + KEY_TITLE + " text)");
        db.execSQL("create table " + TABLE_PRODUCTS + "(" + KEY_ID + " integer primary key," + KEY_PRODUCT + " text," + KEY_DEFAMOUNT + " real," + KEY_MEASURE + " text," + KEY_STORAGE + " integer)");
        db.execSQL("create table " + TABLE_BUYLIST + "(" + KEY_ID + " integer primary key," + KEY_PRODUCT + " text," + KEY_TITLE + " integer," + KEY_AMOUNT + " real," + KEY_MEASURE + " text," + KEY_CHECKED + " integer)");
        for (int i = 0; i < products_names.length; i++){
            cv.put(KEY_ID, i+1);
            cv.put(KEY_PRODUCT, products_names[i]);
            cv.put(KEY_DEFAMOUNT, products_defamount[i]);
            cv.put(KEY_MEASURE, products_measure[i]);
            cv.put(KEY_STORAGE, products_storage[i]);
            db.insert(TABLE_PRODUCTS, null, cv);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_LISTS);
        db.execSQL("drop table if exists " + TABLE_PRODUCTS);
        db.execSQL("drop table if exists " + TABLE_BUYLIST);

        onCreate(db);
    }
}
