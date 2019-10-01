package com.wjs.calc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBOpenHelper extends SQLiteOpenHelper {

    public MyDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    //数据库第一次创建时被调用
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE listSci(id INTEGER PRIMARY KEY AUTOINCREMENT,data VARCHAR(128))");
        db.execSQL("CREATE TABLE listPg(id INTEGER PRIMARY KEY AUTOINCREMENT,data VARCHAR(128))");
        db.execSQL("CREATE TABLE document(id INTEGER PRIMARY KEY AUTOINCREMENT,name INTEGER, data VARCHAR(2048))");
    }
    //软件版本号发生改变时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}