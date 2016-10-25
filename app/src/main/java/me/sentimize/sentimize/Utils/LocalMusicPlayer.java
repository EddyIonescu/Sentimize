package me.sentimize.sentimize.Utils;

import android.app.Instrumentation;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;

import java.util.Timer;
import java.util.TimerTask;

import me.sentimize.sentimize.Models.LocalSong;
import me.sentimize.sentimize.R;
import me.sentimize.sentimize.SentiApplication;
import me.sentimize.sentimize.Services.PlayMusicService;

/**
 * Created by Eddy on 10/23/16.
 */
public class LocalMusicPlayer extends Thread{

    private Context context;
    private int wat = -1;
    public Handler playerHandler;

    public LocalMusicPlayer(Context context){
        super();
        this.context = context;
    }

    @Override
    public void run(){
        Looper.prepare();
        playerHandler = new Handler()
        {
            public void handleMessage(Message msg)
            {
                wat = msg.what;
                switch (wat){
                    case 0:
                        playLocalSong((LocalSong)msg.obj);
                        break;
                    case 1:
                        play();
                        break;
                    case 2:
                        pause();
                        break;
                    case 3:
                        setProgress(msg.arg1);
                }
            }
        };
        Looper.loop();
    }

    private void playLocalSong(final LocalSong song) {
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

        if(!Superpowered.MusicPlayer(song.getPath(), Integer.parseInt(sampleStr), Integer.parseInt(bufferStr))){
            System.out.println("Changing rate to 44100");
            Superpowered.MusicPlayer(song.getPath(), 44100, Integer.parseInt(bufferStr));
            // because xperia keeps changing its mind and returns wrong value - in case sampleRate determined is unsupported by device
        }

    }

    private void play(){
        Superpowered.onPlayPause(true);
    }

    private void pause(){
        Superpowered.onPlayPause(false);
    }

    private void setProgress(int progress){
        Superpowered.setProgress(progress);
    }
}
