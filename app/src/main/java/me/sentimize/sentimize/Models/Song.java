package me.sentimize.sentimize.Models;

/**
 * Created by Eddy on 9/11/16.
 */
// Represents Type of content in list
public class Song {
    public String name;
    public String artist;
    public String storageStatus;

    public Song(){

    }

    public Song(String name, String artist) {
        this.name = name;
        this.artist = artist;
        storageStatus = "Local";
    }

    @Override
    public String toString() {
        return name;
    }
}