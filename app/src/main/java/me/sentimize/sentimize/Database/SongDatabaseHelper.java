package me.sentimize.sentimize.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Eddy on 10/11/16.
 */
public class SongDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "SentimizeAnalysis.db";
    public static final String SONGS_TABLE_NAME = "songs";
    public static final String SONGS_COLUMN_ID = "name";
    public static final String SONGS_COLUMN_UPLIFTING = "uplifting";
    public static final String SONGS_COLUMN_ENERGETIC = "energetic";
    public static final String SONGS_COLUMN_EMOTIONAL = "emotional";
    private HashMap hp;

    public SongDatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table songs (id integer primary key, name text, uplifting text, energetic text, emotional text )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean insertSong (String name, int uplifting, int energetic, int emotional)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("uplifting", uplifting);
        contentValues.put("energetic", energetic);
        contentValues.put("emotional", emotional);
        db.insert("songs", null, contentValues);
        return true;
    }

    public Cursor getData(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from songs where name=\""+id+"\"", null );
        return res;
    }

}
