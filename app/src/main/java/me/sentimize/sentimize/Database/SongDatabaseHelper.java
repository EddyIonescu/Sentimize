package me.sentimize.sentimize.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Eddy on 10/11/16.
 */
public class SongDatabaseHelper extends SQLiteOpenHelper {

    public SongDatabaseHelper(Context context)
    {
        super(context, SongContract.SongEntry.DATABASE_NAME, null, SongContract.SongEntry.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format("CREATE TABLE %1$s (id integer primary key, %2$s text, %3$s text, %4$s text, %5$s text )",
                SongContract.SongEntry.SONGS_TABLE_NAME, SongContract.SongEntry.SONGS_COLUMN_NAME,
                SongContract.SongEntry.SONGS_COLUMN_UPLIFTING, SongContract.SongEntry.SONGS_COLUMN_ENERGETIC,
                SongContract.SongEntry.SONGS_COLUMN_EMOTIONAL));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + SongContract.SongEntry.SONGS_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertSong (String name, int uplifting, int energetic, int emotional)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SongContract.SongEntry.SONGS_COLUMN_NAME, name);
        contentValues.put(SongContract.SongEntry.SONGS_COLUMN_UPLIFTING, uplifting);
        contentValues.put(SongContract.SongEntry.SONGS_COLUMN_ENERGETIC, energetic);
        contentValues.put(SongContract.SongEntry.SONGS_COLUMN_EMOTIONAL, emotional);
        db.insertWithOnConflict(SongContract.SongEntry.SONGS_TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        return true;
    }

    public Cursor getData(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        if(id.charAt(0)=='"') id = id.substring(1, id.length()-1);
        id = DatabaseUtils.sqlEscapeString(id);
        Cursor res =  db.rawQuery( "SELECT * FROM " + SongContract.SongEntry.SONGS_TABLE_NAME +
                " WHERE " + SongContract.SongEntry.SONGS_COLUMN_NAME + " = " + id, null );
        return res;
    }

}
