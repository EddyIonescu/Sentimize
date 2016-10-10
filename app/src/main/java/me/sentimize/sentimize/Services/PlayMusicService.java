package me.sentimize.sentimize.Services;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;

import java.io.IOException;

import me.sentimize.sentimize.Models.Song;

/**
 * Created by eddy on 16-07-13.
 */
public class PlayMusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener{

    //http://code.tutsplus.com/tutorials/create-a-music-player-on-android-song-playback--mobile-22778

    //media player
    private MediaPlayer player;

    public void onCreate(){
        //create the service
        super.onCreate();
        //create player
        player = new MediaPlayer();

        initMusicPlayer();
    }

    public void initMusicPlayer(){
        //set player properties
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // todo call back to playbackLogicUtil and ask for the next song
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //start playback
        mp.start();
    }


    // playing music logic
    public void playSong(){
        player.prepareAsync();
    }
    public void playSong(Song playSong) throws IOException {
        //play a song
        player.reset();
        //get id
        if(playSong.isLocal) {
            long currSong = Long.parseLong(playSong.id);
            //set uri
            Uri trackUri = ContentUris.withAppendedId(
                    android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    currSong);
            player.setDataSource(getApplicationContext(), trackUri);
            player.prepareAsync();
        }
    }
    public void pauseSong(){
        player.pause();
    }

    // binding to communicate with other classes

    private final IBinder PlayMusicBind = new PlayMusicBinder();

    // needed for other activities to communicate with this service
    public class PlayMusicBinder extends Binder {
        public PlayMusicService getService() {
            return PlayMusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return PlayMusicBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }
}
