package me.sentimize.sentimize.Fragments.Song;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.sentimize.sentimize.Fragments.SongFragment;
import me.sentimize.sentimize.Models.LocalSong;
import me.sentimize.sentimize.Models.Song;

public class SongContent {

    public static final List<Song> ITEMS = new ArrayList<Song>();

    /**
     * A map of Songs, by ID.
     */
    public static final Map<String, Song> ITEM_MAP = new HashMap<String, Song>();

    public static void addItem(Song item) {
        ITEMS.add(item);
        //ITEM_MAP.put(item.id, item);
        SongFragment.UpdateList();
    }

    public static void addItems(ArrayList<Song> items){
        for(Song s : items){
            ITEMS.add(s);
            //ITEM_MAP.put(s.id, s);
        }
        SongFragment.UpdateList();
    }

    public static void setItems(ArrayList<Song> items){
        ITEMS.clear();
        //ITEM_MAP.clear();
        addItems(items);
    }


}
