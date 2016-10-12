package me.sentimize.sentimize.Utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.io.IOException;

import me.sentimize.sentimize.Models.LocalSong;
import me.sentimize.sentimize.Models.Song;
import me.sentimize.sentimize.Services.PlayMusicService;

/**
 * Created by eddy on 16-07-13.
 *
 * Sets up and controls the PlayMusicService based on info from MoodScreenActivity
 */
public class PlaybackLogicUtil {
    private static PlayMusicService musicSrv;
    private static Intent playIntent;

    public PlaybackLogicUtil(Context context) {
        if(playIntent==null){
            //connect to the service
            ServiceConnection musicConnection = new ServiceConnection(){

                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    PlayMusicService.PlayMusicBinder binder = (PlayMusicService.PlayMusicBinder)service;
                    //get service
                    musicSrv = binder.getService();

                    //fixme not being called
                    System.out.println("Playback works");
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {

                }

            };

            playIntent = new Intent(context, PlayMusicService.class);
            context.bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            context.startService(playIntent);
        }
    }

    public void playSong(){
            musicSrv.playLocalSong();
    }
    public void playSong(LocalSong song)  {
            musicSrv.playLocalSong(song);
    }
    public void pauseSong(){
            musicSrv.pauseSong();
    }


}
