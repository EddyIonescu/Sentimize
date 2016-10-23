package me.sentimize.sentimize.Models;

/**
 * Created by Eddy on 9/11/16.
 */
// Represents Type of content in list
public class Song {
    public String name;
    public String artist;
    private String storageStatus;
    private int duration;

    public int uplifting = 0;
    public int energetic = 0;
    public int emotional = 0;

    public Song(){

    }

    public Song(String name, String artist, int duration) {
        this.name = name;
        this.artist = artist;
        storageStatus = "Local";
        this.duration = duration;
    }

    public Song(String name, String artist, int duration, int uplifting, int energetic, int emotional) {
        this.name = name;
        this.artist = artist;
        storageStatus = "Local";
        this.uplifting = uplifting;
        this.energetic = energetic;
        this.emotional = emotional;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return name + " - " + artist; // this represents the song's id in our database
    }

    public int getDuration(){
        return duration;
    }

    public String getStorageStatus(){
        return storageStatus;
    }

    public boolean isAnalyzed(){
        return uplifting+energetic+emotional>0;
    }
}
