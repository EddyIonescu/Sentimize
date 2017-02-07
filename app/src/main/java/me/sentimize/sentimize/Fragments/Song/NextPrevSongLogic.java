package me.sentimize.sentimize.Fragments.Song;

import me.sentimize.sentimize.Models.Song;

/**
 * Created by Eddy on 10/25/16.
 */
public class NextPrevSongLogic {
        private static Song currentSong;
        public static void setCurrentSong(Song song){
            currentSong = song;
        }
        public static Song nextSong(){
            return SongContent.ITEMS.get((SongContent.ITEMS.indexOf(currentSong) + 1)%SongContent.ITEMS.size());
        }
        public static Song prevSong(){
            return SongContent.ITEMS.get((SongContent.ITEMS.indexOf(currentSong) - 1 + SongContent.ITEMS.size())%SongContent.ITEMS.size());
        }
        public static boolean valid(){
            return currentSong != null && SongContent.ITEMS != null;
        }
}
