package me.sentimize.sentimize.Utils;


import android.content.Context;
import android.media.AudioManager;

import me.sentimize.sentimize.Models.LocalSong;

/**
 * Created by Eddy on 10/10/16.
 */
public class LocalMusicAnalysis {

    static {
        System.loadLibrary("app");
    }

    public static double getBPM(LocalSong song, Context context){

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        String samplerateString = audioManager.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE);
        String buffersizeString = audioManager.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER);


        if (samplerateString == null) samplerateString = "44100";
        if (buffersizeString == null) buffersizeString = "512";


        System.out.println("path: " + song.getPath());
        double bpm = SuperpoweredAnalyzer(Integer.parseInt(samplerateString), song.getPath());
        System.out.println("bpm: " + bpm);
        return bpm;
    }

    public static native double SuperpoweredAnalyzer(int samplerate, String path);
}
