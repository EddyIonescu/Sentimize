package me.sentimize.sentimize.Services;

import android.app.Instrumentation;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.view.KeyEvent;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import me.sentimize.sentimize.Models.LocalSong;
import me.sentimize.sentimize.Models.Song;
import me.sentimize.sentimize.MoodScreenActivity;
import me.sentimize.sentimize.R;
import me.sentimize.sentimize.SentiApplication;
import me.sentimize.sentimize.Utils.LocalMusicPlayer;
import me.sentimize.sentimize.Utils.SongFiltering;

/**
 * Created by Eddy on 16-07-13.
 */
public class PlayMusicService extends Service{

    private static LocalMusicPlayer player;

    public void playLocalSong(LocalSong song){

        Message m = Message.obtain();
        m.what = getResources().getInteger(R.integer.PLAY_LOCAL_SONG);
        m.obj = song;
        player.playerHandler.sendMessage(m);

        // todo add mediaplayer panel in notifications bar
        /*
        Intent intent = new Intent(this, MoodScreenActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
        Notification n  = new Notification.Builder(this)
                .setContentTitle("Song Playing")
                .setContentText("Subject")
                .setSmallIcon(R.drawable.energy)
                .setContentIntent(pIntent)
                .setAutoCancel(true).build();


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(1, n);
        startForeground(1, n);
        */
    }

    public void pause(){
        Message m = Message.obtain();
        m.what = getResources().getInteger(R.integer.PAUSE);
        player.playerHandler.sendMessage(m);
    }

    public void play(){
        Message m = Message.obtain();
        m.what = getResources().getInteger(R.integer.PLAY);
        player.playerHandler.sendMessage(m);
    }

    public void setProgress(int progress){
        Message m = Message.obtain();
        m.what = getResources().getInteger(R.integer.SET_PROGRESS);
        m.arg1 = progress;
        player.playerHandler.sendMessage(m);
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
        if(player==null) {
            player = new LocalMusicPlayer(PlayMusicService.this);
            player.start();
        }
        return PlayMusicBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        return false;
    }
}
