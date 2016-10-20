package me.sentimize.sentimize.Utils;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;

import me.sentimize.sentimize.Database.SongContract;
import me.sentimize.sentimize.Database.SongDatabaseHelper;
import me.sentimize.sentimize.Models.LocalSong;
import me.sentimize.sentimize.Models.Song;

/**
 * Created by Eddy on 10/11/16.
 */
public class LocalSongCaching {
    private static SongDatabaseHelper databaseHelper;
    public static void initDatabase(Context context){
        if(databaseHelper==null) {
            databaseHelper = new SongDatabaseHelper(context);
        }
    }
    public static void cacheLocalSong(LocalSong song){
        databaseHelper.insertSong(song.toString(), song.uplifting, song.energetic, song.emotional);
    }
    public static Song retrieveSongAnalysis(Song song){
        Cursor rs = databaseHelper.getData(song.toString());
        try {
            rs.moveToFirst();
            song.uplifting = rs.getInt(rs.getColumnIndex(SongContract.SongEntry.SONGS_COLUMN_UPLIFTING));
            song.energetic = rs.getInt(rs.getColumnIndex(SongContract.SongEntry.SONGS_COLUMN_ENERGETIC));
            song.emotional = rs.getInt(rs.getColumnIndex(SongContract.SongEntry.SONGS_COLUMN_EMOTIONAL));
        }
        catch(CursorIndexOutOfBoundsException e){
            System.out.println(song + " not cached - " + e.getMessage());
        }
        return song;
        // uplifting, energetic, and emotional values will be 0 if retrieval failed, so the caller will then analyze the songs
    }
}
