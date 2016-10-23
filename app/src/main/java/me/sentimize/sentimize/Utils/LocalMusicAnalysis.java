package me.sentimize.sentimize.Utils;


import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import me.sentimize.sentimize.Models.LocalSong;
import me.sentimize.sentimize.Models.LocalSongAnalysisRequest;
import me.sentimize.sentimize.Models.Song;

/**
 * Created by Eddy on 10/10/16.
 */

class Superpowered{

    static {
        System.loadLibrary("app");
    }

    public static native double[] SuperpoweredAnalyzer(String path);
}


public class LocalMusicAnalysis extends AsyncTask<LocalSongAnalysisRequest, Void, Object[]>{
    private static boolean busy = false;
    public static boolean isBusy(){
        return busy;
    }

    @Override
    protected Object[] doInBackground(LocalSongAnalysisRequest... localSongs) {
        busy = true;
        LocalSong song = localSongs[0].getSong();
        LocalSongAnalysisRequest nextSong = localSongs[0].getNextRequest();
        double[] results = Superpowered.SuperpoweredAnalyzer(song.getPath());
        if(results != null) {
            Object[] objects1 = new Object[2 + results.length];
            objects1[0] = song;
            objects1[1] = nextSong;
            for (int i = 0; i < results.length; i++) {
                objects1[i + 2] = results[i];
            }
            return objects1;
        }
        return new Object[]{song, nextSong};
    }

    @Override
    protected void onPostExecute(Object[] objects) {
        super.onPostExecute(objects);

        if(objects.length>2) {
            System.out.println("Results: " + Arrays.toString(objects));

            int uplifting = (int) (double) objects[2];
            int energetic = (int) (double) objects[3];
            int emotional = (int) (double) objects[4];

            LocalSong song = (LocalSong) objects[0];
            song.uplifting = uplifting;
            song.energetic = energetic;
            song.emotional = emotional;

            LocalSongCaching.cacheLocalSong(song);
            LocalSongCaching.cacheLocalSongData(song, (double)objects[4], (double)objects[5], (double)objects[6], (double)objects[7], (double)objects[8]);
            SongFiltering.showSnackbarUpdate("Analyzed " + song);
        }
        else{
            LocalSong song = (LocalSong) objects[0];
            SongFiltering.showSnackbarUpdate("Error Analyzing " + song + " (corrupted or unsupported format)");
        }

        if(objects[1] != null){
            new LocalMusicAnalysis().execute((LocalSongAnalysisRequest)objects[1]);
        }
        else{
            busy = false;
        }
    }
}
