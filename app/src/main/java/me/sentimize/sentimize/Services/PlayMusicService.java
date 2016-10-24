package me.sentimize.sentimize.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;

import java.io.IOException;

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
public class PlayMusicService extends Service {

    private static LocalMusicPlayer player = new LocalMusicPlayer();

    public void playLocalSong(LocalSong song){
        player.playLocalSong(song, PlayMusicService.this);

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
        player.pause();
    }

    public void play(){
        player.play();
    }

    public int getProgress(){
        return 0;
    }

    public void setProgress(int progress){
        player.setProgress(progress);
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
        player = new LocalMusicPlayer();
        return PlayMusicBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        return false;
    }
}
