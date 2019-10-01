package com.wjs.calc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class SQLiteUtils {

    public static void saveData(SQLiteDatabase db, String table, String data, Context context){
        if(data.length()<128) {
            Integer[] ids=getids(db,table);
            if(ids[2]>=1024) {
                deleteData(db,table,String.valueOf(ids[0]));
            }
            ContentValues value = new ContentValues();
            value.put("data", data);
            db.insert(table, null, value);
        }else{
            Toast.makeText(context,"记录失败：数据过长",Toast.LENGTH_SHORT).show();
        }
    }

    public static String queryData(SQLiteDatabase db, String table, String id){
        String data="";
        Cursor cursor = db.query(table,new String[]{"data"}, "id = ?", new String[]{id}, null, null, null);
        if (cursor.moveToFirst()) {
            data = cursor.getString(cursor.getColumnIndex("data"));
        }
        cursor.close();
        return data;
    }

    public static Integer[] getids(SQLiteDatabase db, String table)
    {
        Integer[] ids=new Integer[]{0,0,0};
        Cursor cursor = db.query(table, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            ids[0]=Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
        }
        if (cursor.moveToLast()) {
            ids[1]=Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
        }
        cursor.close();
        ids[2]=ids[1]-ids[0]+1;
        if(ids[0]==0||ids[1]==0){
            ids[2]=0;
        }
        return ids;
    }

    public static void deleteData(SQLiteDatabase db,  String table, String id){
        db.delete(table, "id = ?",new String[]{id});
    }

    public static void clearData(SQLiteDatabase db, String table){
        db.delete(table, null,null);
        db.execSQL("update sqlite_sequence set seq=0 where name='"+table+"'");
    }
}
