package com.example.mysqlitememo1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NHSDatabaseHelper extends SQLiteOpenHelper {
    static final private String DBNAME = "nhs10627db";
    static final private int VERSION = 1;

    NHSDatabaseHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (db != null) {
            db.execSQL("CREATE TABLE memopad (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, memo TEXT, write_date TEXT)");
            db.execSQL("INSERT INTO memopad(title, memo, write_date)" + "VALUES('テスト', 'これはテストです', '2000/01/01 0:0:0')");
            db.execSQL("INSERT INTO memopad(title, memo, write_date)" + "VALUES('テスト1', 'これはテスト1です', '2001/01/01 0:0:0')");
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        if (db != null) {
            db.execSQL("DROP TABLE IF EXISTS memopad");
            onCreate(db);
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}
