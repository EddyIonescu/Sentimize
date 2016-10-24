package me.sentimize.sentimize.Utils;

/**
 * Created by Eddy on 10/10/16.
 */

public class Superpowered{

    static {
        System.loadLibrary("app");
    }

    public static native double[] SuperpoweredAnalyzer(String path);
    public static native void MusicPlayer(String path, int sampleRate, int buffersize);
    public static native void onPlayPause(boolean play);
    public static native void setProgress(int progress);
}
