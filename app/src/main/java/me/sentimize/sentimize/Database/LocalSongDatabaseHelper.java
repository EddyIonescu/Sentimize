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
public class LocalSongDatabaseHelper extends SQLiteOpenHelper {

    public LocalSongDatabaseHelper(Context context)
    {
        super(context, LocalSongContract.SongEntry.DATABASE_NAME, null, LocalSongContract.SongEntry.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format("CREATE TABLE %1$s (id integer primary key, %2$s text, " +
                "%3$s double, %4$s double, %5$s double, %6$s double, %7$s double, %8$s double, %9$s double, %10$s double )",
                LocalSongContract.SongEntry.SONGS_TABLE_NAME,
                LocalSongContract.SongEntry.SONGS_COLUMN_NAME,
                LocalSongContract.SongEntry.SONGS_COLUMN_UPLIFTING,
                LocalSongContract.SongEntry.SONGS_COLUMN_ENERGETIC,
                LocalSongContract.SongEntry.SONGS_COLUMN_EMOTIONAL,
                LocalSongContract.SongEntry.SONGS_COLUMN_BPM,
                LocalSongContract.SongEntry.SONGS_COLUMN_AVG_LOW_FREQ,
                LocalSongContract.SongEntry.SONGS_COLUMN_AVG_MED_FREQ,
                LocalSongContract.SongEntry.SONGS_COLUMN_AVG_HIGH_FREQ,
                LocalSongContract.SongEntry.SONGS_COLUMN_KEY));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + LocalSongContract.SongEntry.SONGS_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertSong (String name, int uplifting, int energetic, int emotional,
                               double bpm, double avgLowFreq, double avgMedFreq, double avgHighFreq, double keyIndex) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LocalSongContract.SongEntry.SONGS_COLUMN_NAME, name);
        contentValues.put(LocalSongContract.SongEntry.SONGS_COLUMN_UPLIFTING, uplifting);
        contentValues.put(LocalSongContract.SongEntry.SONGS_COLUMN_ENERGETIC, energetic);
        contentValues.put(LocalSongContract.SongEntry.SONGS_COLUMN_EMOTIONAL, emotional);
        contentValues.put(LocalSongContract.SongEntry.SONGS_COLUMN_BPM, bpm);
        contentValues.put(LocalSongContract.SongEntry.SONGS_COLUMN_AVG_LOW_FREQ, avgLowFreq);
        contentValues.put(LocalSongContract.SongEntry.SONGS_COLUMN_AVG_MED_FREQ, avgMedFreq);
        contentValues.put(LocalSongContract.SongEntry.SONGS_COLUMN_AVG_HIGH_FREQ, avgHighFreq);
        contentValues.put(LocalSongContract.SongEntry.SONGS_COLUMN_KEY, keyIndex);
        db.insert(LocalSongContract.SongEntry.SONGS_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        if(id.charAt(0)=='"') id = id.substring(1, id.length()-1);
        id = DatabaseUtils.sqlEscapeString(id);
        Cursor res =  db.rawQuery( "SELECT * FROM " + LocalSongContract.SongEntry.SONGS_TABLE_NAME +
                " WHERE " + LocalSongContract.SongEntry.SONGS_COLUMN_NAME + " = " + id, null );
        return res;
    }

}
