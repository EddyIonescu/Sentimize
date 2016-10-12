package me.sentimize.sentimize.Utils;


import android.content.Context;
import android.media.AudioManager;

import java.util.ArrayList;

import me.sentimize.sentimize.Models.LocalSong;
import me.sentimize.sentimize.Models.Song;

/**
 * Created by Eddy on 10/10/16.
 */
public class LocalMusicAnalysis {

    static {
        System.loadLibrary("app");
    }

    public static ArrayList<Song> filterLocalSongs(ArrayList<LocalSong> songs, int uplifting, int energetic, int emotional){
        // possible mood levels are 1, 2, and 3; 1 to 100 for analysis
        ArrayList<Song> relevantSongs = new ArrayList<>();
        for(LocalSong song : songs){
            song = (LocalSong)analyzeSong(song);
            int score = 0;
            if(uplifting==1 && song.uplifting<40){
                score++;
            }
            else if(uplifting==2 && song.uplifting>20 && song.uplifting<80){
                score++;
            }
            else if(uplifting==3 && song.uplifting>60){
                score++;
            }

            if(energetic==1 && song.energetic<40){
                score++;
            }
            else if(energetic==2 && song.energetic>20 && song.energetic<80){
                score++;
            }
            else if(energetic==3 && song.energetic>60){
                score++;
            }

            if(emotional==1 && song.emotional<40){
                score++;
            }
            else if(emotional==2 && song.emotional>20 && song.emotional<80){
                score++;
            }
            else if(emotional==3 && song.emotional>60){
                score++;
            }

            if(score>=2){
                relevantSongs.add(song);
            }
        }
        return relevantSongs;
    }

    public static Song analyzeSong(Song song){
        song = LocalSongCaching.getSongAnalysis(song);
        if(song instanceof LocalSong){
            if(!song.isAnalyzed()){

                System.out.println("analyzing (first-time) " + song);
                int energetic = (int)(getBPM((LocalSong)song)/2.0);

                // todo implement these
                int uplifting = energetic;
                int emotional = energetic;

                song.uplifting = uplifting;
                song.energetic = energetic;
                song.emotional = emotional;

                LocalSongCaching.cacheLocalSong((LocalSong)song);
            }
            System.out.println("retrieved " + song);
        }
        return song;
    }

    public static double getBPM(LocalSong song){
        System.out.println("path: " + song.getPath());
        double bpm = SuperpoweredAnalyzer(song.getPath());
        System.out.println("bpm: " + bpm);
        return bpm;
    }

    public static native double SuperpoweredAnalyzer(String path);
}
