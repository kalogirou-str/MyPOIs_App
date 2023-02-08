package com.unipi.strat.mypois;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "Poi.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table Poi_infos(title TEXT primary key, timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "latidute REAL, longitude REAL, category TEXT, description TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop Table if exists Poi_infos");
    }

    public Boolean insertuserdata(String title, long timestamp, double latitude, double longitude, String category, String description)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tile", title);
        contentValues.put("timestamp", timestamp);
        contentValues.put("latitude", latitude);
        contentValues.put("longitude", longitude);
        contentValues.put("category", category);
        contentValues.put("description", description);
        long result=DB.insert("Poi_infos", null, contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }
}
