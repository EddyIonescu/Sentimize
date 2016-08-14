package me.sentimize.sentimize.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

/**
 * Created by eddy on 2016-08-13.
 *
 * Needed for Marshmellow permissions - accessing local music
 */
public class PermissionUtils {
    public static boolean canAccessLocalMusic(Activity activity){
        int permissionCheck = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    private static View view;
    public static boolean canAccessLocalMusic(Activity activity, View v){
        int permissionCheck = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        view = v;
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }
    public static View getView(){
        return view;
    }
}
