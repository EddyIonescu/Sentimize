package me.sentimize.sentimize.Utils;


import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import me.sentimize.sentimize.Models.LocalSong;
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

public class LocalMusicAnalysis extends AsyncTask<LocalSong, Void, Object[]>{

    @Override
    protected Object[] doInBackground(LocalSong... localSongs) {
        double[] results = Superpowered.SuperpoweredAnalyzer((localSongs[0]).getPath());
        Object[] objects1 = new Object[1 + results.length];
        objects1[0] = localSongs[0];
        for(int i = 0; i<results.length; i++){
            objects1[i+1] = results[i];
        }
        return objects1;
    }

    @Override
    protected void onPostExecute(Object[] objects) {
        super.onPostExecute(objects);
        System.out.println("Results: " + Arrays.toString(objects));

        int uplifting = (int)(double)objects[1];
        int energetic = (int)(double)objects[2];
        int emotional = (int)(double)objects[3];

        LocalSong song = (LocalSong)objects[0];
        song.uplifting = uplifting;
        song.energetic = energetic;
        song.emotional = emotional;

        LocalSongCaching.cacheLocalSong(song);
        SongFiltering.showSnackbarUpdate("Analyzed " + song);
    }
}
