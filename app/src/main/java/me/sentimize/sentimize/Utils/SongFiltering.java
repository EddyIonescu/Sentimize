package me.sentimize.sentimize.Utils;

import android.app.Activity;
import android.support.design.widget.Snackbar;

import java.util.ArrayList;

import me.sentimize.sentimize.Models.LocalSong;
import me.sentimize.sentimize.Models.Song;
import me.sentimize.sentimize.R;

/**
 * Created by root on 10/19/16.
 */
public class SongFiltering {
    private static Activity moodActivity;
    public static void showSnackbarUpdate(String message){
        Snackbar.make(moodActivity.findViewById(R.id.main_layout), message, Snackbar.LENGTH_SHORT).show();
    }
    public static ArrayList<Song> filterLocalSongs(ArrayList<LocalSong> songs, int uplifting, int energetic, int emotional, Activity moodAct){
        // possible mood levels are 1, 2, and 3; 1 to 100 for analysis
        ArrayList<Song> relevantSongs = new ArrayList<>();
        moodActivity= moodAct;
        for(LocalSong song : songs){
            song = (LocalSong)LocalSongCaching.retrieveSongAnalysis(song);
            if(!song.isAnalyzed()){
                System.out.println("analyzing (first-time) " + song);
                new LocalMusicAnalysis().execute(song);
            }
            System.out.println("retrieved " + song);
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

}
