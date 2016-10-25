package me.sentimize.sentimize.Utils;

import android.app.Activity;
import android.support.design.widget.Snackbar;

import java.util.ArrayList;

import me.sentimize.sentimize.Models.LocalSong;
import me.sentimize.sentimize.Models.LocalSongAnalysisRequest;
import me.sentimize.sentimize.Models.Song;
import me.sentimize.sentimize.R;

/**
 * Created by Eddy on 10/19/16.
 */
public class SongFiltering {
    private static Activity moodActivity;
    public static void showSnackbarUpdate(String message){
        if(moodActivity != null)
            Snackbar.make(moodActivity.findViewById(R.id.main_layout), message, Snackbar.LENGTH_SHORT).show();
        else
            System.out.println("MoodActivity not initialized");
    }
    public static void initMoodAct(Activity moodAct){
        moodActivity = moodAct;
    }
    private static int uplifting, energetic, emotional;
    public static boolean songEligible(LocalSong song){
        int score = 0;
        if(uplifting==0 && song.uplifting<40){
            score++;
        }
        else if(uplifting==1 && song.uplifting>20 && song.uplifting<80){
            score++;
        }
        else if(uplifting==2 && song.uplifting>60){
            score++;
        }

        if(energetic==0 && song.energetic<35){
            score++;
        }
        else if(energetic==1 && song.energetic>30 && song.energetic<75){
            score++;
        }
        else if(energetic==2 && song.energetic>70){
            score++;
        }

        return score==2;
    }
    public static ArrayList<Song> filterLocalSongs(ArrayList<LocalSong> songs, int _uplifting, int _energetic, int _emotional, Activity moodAct){
        // possible mood levels are 1, 2, and 3; 1 to 100 for analysis
        ArrayList<Song> relevantSongs = new ArrayList<>();
        LocalSongAnalysisRequest firstRequest = null;
        LocalSongAnalysisRequest currentRequest = null;
        moodActivity= moodAct;
        uplifting = _uplifting;
        energetic = _energetic;
        emotional = _emotional;
        int analyzeCount = 0;
        for(LocalSong song : songs){
            song = (LocalSong)LocalSongCaching.retrieveSongAnalysis(song);
            if(!song.isAnalyzed()){
                System.out.println("analyzing (first-time) " + song);
                analyzeCount++;
                if(firstRequest == null){
                    firstRequest = new LocalSongAnalysisRequest(song, null);
                    currentRequest = firstRequest;
                }
                else{
                    LocalSongAnalysisRequest request = new LocalSongAnalysisRequest(song, null);
                    currentRequest.setNextRequest(request);
                    currentRequest = request;
                }
                continue;
            }
            System.out.println("retrieved " + song);
            if(songEligible(song)){
                relevantSongs.add(song);
            }
        }
        if(!LocalMusicAnalysis.isBusy() && firstRequest != null){
            new LocalMusicAnalysis().execute(firstRequest);
            showSnackbarUpdate("Analyzing " + analyzeCount + " songs");
        }
        return relevantSongs;
    }

}
