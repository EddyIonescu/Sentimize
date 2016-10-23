package me.sentimize.sentimize;

import android.app.Application;

import com.facebook.stetho.Stetho;

import me.sentimize.sentimize.Utils.LocalSongCaching;
import me.sentimize.sentimize.Utils.PlaybackLogicUtil;

/**
 * Created by Eddy on 10/22/16.
 */
public class SentiApplication extends Application {
    private static PlaybackLogicUtil playbackLogicUtil;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        playbackLogicUtil = new PlaybackLogicUtil(this);
        LocalSongCaching.initDatabase(this);
        System.out.println("cached");
    }
    public static PlaybackLogicUtil getPlaybackLogicUtil(){
        return playbackLogicUtil;
    }
    public static void setProgress(int i){
        if(playbackLogicUtil != null) {
            playbackLogicUtil.setProgress(i);
        }
    }
}
