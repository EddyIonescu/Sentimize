package me.sentimize.sentimize.Models;

/**
 * Created by Eddy on 9/11/16.
 */
// Represents Type of content in list
public class Song {
    public String id;
    public String name;
    public String artist;
    public boolean isLocal;
    public boolean isSpotify;
    public String storageStatus;

    public Song(){

    }
    //local song
    public Song(long localId, String name, String artist) {
        id = Long.toString(localId);
        this.name = name;
        this.artist = artist;
        isLocal = true;
        isSpotify = false;
        storageStatus = "Local";
    }

    @Override
    public String toString() {
        return name;
    }
}
