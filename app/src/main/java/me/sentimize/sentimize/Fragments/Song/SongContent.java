package me.sentimize.sentimize.Fragments.Song;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.sentimize.sentimize.Fragments.SongFragment;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class SongContent {

    public static final List<Song> ITEMS = new ArrayList<Song>();

    /**
     * A map of Songs, by ID.
     */
    public static final Map<String, Song> ITEM_MAP = new HashMap<String, Song>();

    public static void addItem(Song item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.Id, item);
        SongFragment.UpdateList();
    }

    public static void addItems(ArrayList<Song> items){
        for(Song s : items){
            ITEMS.add(s);
            ITEM_MAP.put(s.Id, s);
        }
        SongFragment.UpdateList();
    }

    // Represents Type of content in list
    public static class Song {
        public String Id;
        public String Name;
        public String Artist;
        public double Happiness;
        public boolean IsLocal;
        public boolean IsSpotify;
        public String StorageStatus;
        public boolean IsAnalyzed;
        public ArrayList<Integer> IsInPlaylists;

        public Song(){

        }
        //local song
        public Song(long localId, String name, String artist) {
            Id = Long.toString(localId);
            Name = name;
            Artist = artist;
            IsLocal = true;
            IsSpotify = false;
            StorageStatus = "Local";
        }

        @Override
        public String toString() {
            return Name;
        }
    }
}
