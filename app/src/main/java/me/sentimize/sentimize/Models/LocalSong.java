package me.sentimize.sentimize.Models;

import android.content.ContentUris;
import android.net.Uri;


/**
 * Created by Eddy on 9/10/16.
 */
public class LocalSong extends Song {

    private String id;
    private String path;
    //local song
    public LocalSong(long localId, String name, String artist, String path) {
        super(name, artist);
        id = Long.toString(localId);
        this.path = path;
    }
    public String getPath(){
        return path;
    }
    public Uri getTrack(){
        return ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, Long.parseLong(id));
    }
}
