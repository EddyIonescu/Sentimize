package me.sentimize.sentimize.Utils;

import android.content.Context;
import android.database.Cursor;

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
    public static Song getSongAnalysis(Song song){
        Cursor rs = databaseHelper.getData(song.toString());
        if(rs==null){
            return song;
        }
        rs.moveToFirst();
        song.uplifting = rs.getInt(rs.getColumnIndex(SongDatabaseHelper.SONGS_COLUMN_UPLIFTING));
        song.energetic = rs.getInt(rs.getColumnIndex(SongDatabaseHelper.SONGS_COLUMN_ENERGETIC));
        song.emotional = rs.getInt(rs.getColumnIndex(SongDatabaseHelper.SONGS_COLUMN_EMOTIONAL));
        return song;
    }
}
