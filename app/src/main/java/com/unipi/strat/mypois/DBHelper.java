package com.unipi.strat.mypois;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "Poi1.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table Poi_infos(title TEXT primary key, timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "latitude REAL, longitude REAL, category TEXT, description TEXT)");

        //TRIGGER FOR AUTOMATIC UPDATE OF TIMESTAMP WHEN WE UPDATE A POI
        DB.execSQL("CREATE TRIGGER IF NOT EXISTS Poi_infos_after_update AFTER UPDATE ON " +
                "Poi_infos BEGIN UPDATE Poi_infos  SET timestamp = CURRENT_TIMESTAMP WHERE title = old.title; END;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop Table if exists Poi_infos");
    }

    public Boolean insertuserdata(String title, double latitude, double longitude, String category, String description)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
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

    public Boolean updateuserdata(String title, double latitude, double longitude, String category, String description)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("latitude", latitude);
        contentValues.put("longitude", longitude);
        contentValues.put("category", category);
        contentValues.put("description", description);
        Cursor cursor = DB.rawQuery("Select * from Poi_infos where title = ?", new String[]{title});
        if (cursor.getCount() > 0) {
            long result = DB.update("Poi_infos", contentValues, "title=?", new String[]{title});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    public Boolean deletedata (String title)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Poi_infos where title = ?", new String[]{title});
        if (cursor.getCount() > 0) {
            long result = DB.delete("Poi_infos", "title=?", new String[]{title});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    Cursor readAllData(){
        String query = "SELECT * FROM Poi_infos";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
}
