package me.sentimize.sentimize;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import java.io.IOException;
import java.io.NotActiveException;

import me.sentimize.sentimize.Fragments.Song.SongContent;
import me.sentimize.sentimize.Fragments.SongFragment;
import me.sentimize.sentimize.Utils.LocalMusicRequisitionUtil;
import me.sentimize.sentimize.Utils.PermissionUtils;
import me.sentimize.sentimize.Utils.PlaybackLogicUtil;

public class MoodScreenActivity extends AppCompatActivity implements View.OnClickListener, SongFragment.OnListFragmentInteractionListener {

    public PlaybackLogicUtil playbackLogicUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab_play = (FloatingActionButton) findViewById(R.id.fab_play);
        fab_pause = (FloatingActionButton) findViewById(R.id.fab_pause);
        fab_play.setClickable(true);
        fab_pause.setClickable(true);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab_playlist = (FloatingActionButton) findViewById(R.id.fab_playlist);
        fab_happy = (FloatingActionButton) findViewById(R.id.fab_happy);
        fab_sad = (FloatingActionButton) findViewById(R.id.fab_sad);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);

        fab_play.setOnClickListener(this);
        fab_pause.setOnClickListener(this);
        fab.setOnClickListener(this);
        fab_playlist.setOnClickListener(this);
        fab_happy.setOnClickListener(this);
        fab_sad.setOnClickListener(this);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.list_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            try {
                // Passing on animateFAB as it can't be made static, so the function pointer is being passed via reflection
                SongFragment firstFragment = new SongFragment(this.getClass().getMethod("animateFAB"));

                // In case this activity was started with special instructions from an
                // Intent, pass the Intent's extras to the fragment as arguments
                firstFragment.setArguments(getIntent().getExtras());

                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.list_container, firstFragment).commit();
            } catch (NoSuchMethodException e) {

            }
        }

        playbackLogicUtil = new PlaybackLogicUtil(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mood_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public static Boolean isFabOpen = false;
    private FloatingActionButton fab, fab_playlist, fab_happy, fab_sad, fab_play, fab_pause;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;

    public void animateFAB(){

        if(isFabOpen){

            fab.startAnimation(rotate_backward);
            fab_playlist.startAnimation(fab_close);
            fab_happy.startAnimation(fab_close);
            fab_sad.startAnimation(fab_close);

            fab_playlist.setClickable(false);
            fab_happy.setClickable(false);
            fab_sad.setClickable(false);

            isFabOpen = false;

        } else {

            fab.startAnimation(rotate_forward);
            fab_playlist.startAnimation(fab_open);
            fab_happy.startAnimation(fab_open);
            fab_sad.startAnimation(fab_open);

            fab_playlist.setClickable(true);
            fab_happy.setClickable(true);
            fab_sad.setClickable(true);

            isFabOpen = true;

        }
    }

    @Override
    public void onClick(View v) {
        if(PermissionUtils.canAccessLocalMusic(this, v)) {
            if (v.getId() == R.id.fab) {
                animateFAB();
            } else if (v.getId() == R.id.fab_happy) {
                SongContent.addItems(LocalMusicRequisitionUtil.getSongList(this));
            } else if (v.getId() == R.id.fab_play) {
                System.out.println("Pressed Play");
                // play selected song
                playbackLogicUtil.playSong();
                fab_pause.show();
                fab_play.hide();
            } else if (v.getId() == R.id.fab_pause) {
                System.out.println("Pressed Pause");
                playbackLogicUtil.pauseSong();
                fab_play.show();
                fab_pause.hide();
            } else {
                System.out.println("Plus/close button was NOT tapped - " + this.getResources().getResourceName(v.getId()));
            }
        }
        else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
    }

    // Implement Marshmellow Permissions Callback (can't place this in utils) :(
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(PermissionUtils.getView() != null) {
                        this.onClick(PermissionUtils.getView());
                    }
                }
    }

    public void onListFragmentInteraction(SongContent.Song song) {
        // Do different stuff
        System.out.println("List Clicked - " + song.Name);
        try {
            playbackLogicUtil.playSong(song);
        } catch (IOException e) {
            // error playing song
            System.out.println(e.getMessage());
        }
    }

}
