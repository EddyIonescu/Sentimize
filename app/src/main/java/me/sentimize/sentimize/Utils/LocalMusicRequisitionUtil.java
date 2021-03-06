package me.sentimize.sentimize.Utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;

import me.sentimize.sentimize.Models.LocalSong;
import me.sentimize.sentimize.Models.Song;

/**
 * Created by eddy on 16-07-12.
 * Gets list of songs stored locally on device (uses MediaStore content provider)
 */
public class LocalMusicRequisitionUtil {

    public static ArrayList<LocalSong> getSongList(Activity activity) {
        ArrayList<LocalSong> songList = new ArrayList<>();
        if(PermissionUtils.canAccessLocalMusic(activity)) {
            ContentResolver musicResolver = activity.getContentResolver();
            Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
            //http://code.tutsplus.com/tutorials/create-a-music-player-on-android-project-setup--mobile-22764
            if (musicCursor != null && musicCursor.moveToFirst()) {
                int titleColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
                int idColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
                int artistColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST);
                int durationColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
                int pathColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

                do {
                    //we need the thisID so that Android gets us the song we want to play
                    long thisId = musicCursor.getLong(idColumn);
                    String thisTitle = musicCursor.getString(titleColumn);
                    String thisArtist = musicCursor.getString(artistColumn);
                    int thisDuration = musicCursor.getInt(durationColumn);
                    String thisPath = musicCursor.getString(pathColumn);
                    System.out.println(thisTitle + " - " + thisDuration);
                    if(!tooShort(thisDuration)) {
                        LocalSong song = new LocalSong(thisId, thisTitle, thisArtist, thisPath, thisDuration);
                        song = enhanceSongName(song);
                        songList.add(song);
                    }
                }
                while (musicCursor.moveToNext());
                musicCursor.close();
            }
        }
        return songList;
    }
    public static LocalSong enhanceSongName(LocalSong song){
        if (song.name.contains(" by ")) {
            String[] s = song.name.split(" by ");
            if (s.length == 2) {
                song.name = s[0];
                song.artist = s[1];
            }
        }
        else if(song.name.contains(" - ")){
            String[] s = song.name.split(" - ");
            if(s.length == 2){
                song.artist = s[0];
                song.name = s[1];
            }
        }
        return song;
    }
    // eleminate sounds like "facebook pop" and ringtones
    public static boolean tooShort(int duration){
        return duration<30000;
    }
}
