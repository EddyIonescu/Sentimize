package me.sentimize.sentimize.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

import me.sentimize.sentimize.Fragments.Song.SongContent;

/**
 * Created by eddy on 16-07-12.
 * Gets list of songs stored locally on device (uses MediaStore content provider)
 */
public class LocalMusicRequisitionUtil {

    public static ArrayList<SongContent.Song> getSongList(Activity activity) {
        ArrayList<SongContent.Song> songList = new ArrayList<SongContent.Song>();
        if(PermissionUtils.canAccessLocalMusic(activity)) {
            ContentResolver musicResolver = activity.getContentResolver();
            Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
            //http://code.tutsplus.com/tutorials/create-a-music-player-on-android-project-setup--mobile-22764
            if (musicCursor != null && musicCursor.moveToFirst()) {
                int titleColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media.TITLE);
                int idColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media._ID);
                int artistColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media.ARTIST);

                do {
                    //we need the thisID so that Android gets us the song we want to play
                    long thisId = musicCursor.getLong(idColumn);
                    String thisTitle = musicCursor.getString(titleColumn);
                    String thisArtist = musicCursor.getString(artistColumn);
                    songList.add(new SongContent.Song(thisId, thisTitle, thisArtist));
                }
                while (musicCursor.moveToNext());
                musicCursor.close();
            }
        }
        return songList;
    }
}
