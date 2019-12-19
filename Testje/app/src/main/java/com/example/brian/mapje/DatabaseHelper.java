package com.example.brian.mapje;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME ="Stad.db";
    public static final String TABLE_NAME ="StadMonumenten";
    public static final String COL_1 ="ID";
    public static final String COL_2 ="STAPPEN";
    public static final String COL_3 ="SCORE";
    public static final String COL_4 ="CALORIE";




    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS \"StadMonumenten\" (\"ID\" INTEGER PRIMARY KEY DEFAULT 1, \"STAPPEN\" INTEGER DEFAULT 0, \"SCORE\" INTEGER DEFAULT 0,\"CALORIE\" INTEGER DEFAULT 0)");
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,1);
        contentValues.put(COL_2,0);
        contentValues.put(COL_3,0);
        contentValues.put(COL_4,0);

        db.insert(TABLE_NAME,null,contentValues);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        onCreate(db);
    }


    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public void updateStapData(int stap,int calo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,stap);
        contentValues.put(COL_4,calo);
        db.update(TABLE_NAME,contentValues,"id=1",null);
    }

    public void updateVisitedMonuments(int bezocht){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_3,bezocht);
        db.update(TABLE_NAME,contentValues,"id=1",null);
    }

}

