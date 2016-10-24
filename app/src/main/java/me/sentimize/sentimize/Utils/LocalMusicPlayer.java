package me.sentimize.sentimize.Utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;

import me.sentimize.sentimize.Models.LocalSong;
import me.sentimize.sentimize.R;
import me.sentimize.sentimize.SentiApplication;

/**
 * Created by Eddy on 10/23/16.
 */
public class LocalMusicPlayer {

    private Thread backgroundPlayer;
    public void playLocalSong(final LocalSong song, Context context) {
        // Get the device's sample rate and buffer size to enable low-latency Android audio output, if available.
        String samplerateString = null, buffersizeString = null;
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        samplerateString = audioManager.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE);
        buffersizeString = audioManager.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER);

        if (samplerateString == null) samplerateString = "44100";
        if (buffersizeString == null) buffersizeString = "512";
        final String sampleStr = samplerateString;
        final String bufferStr = buffersizeString;
        System.out.println("sample: " + samplerateString + "; buffer: " + buffersizeString);

        Superpowered.MusicPlayer(song.getPath(), Integer.parseInt(sampleStr), Integer.parseInt(bufferStr));


    }

    public void play(){
        Superpowered.onPlayPause(true);
    }

    public void pause(){
        Superpowered.onPlayPause(false);
    }

    public void setProgress(int progress){
        Superpowered.setProgress(progress);
    }
}
