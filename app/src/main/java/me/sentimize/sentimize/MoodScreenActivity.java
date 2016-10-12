package me.sentimize.sentimize;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import java.io.IOException;

import me.sentimize.sentimize.Fragments.Song.SongContent;
import me.sentimize.sentimize.Fragments.SongFragment;
import me.sentimize.sentimize.Models.LocalSong;
import me.sentimize.sentimize.Models.Song;
import me.sentimize.sentimize.Utils.LocalMusicAnalysis;
import me.sentimize.sentimize.Utils.LocalMusicRequisitionUtil;
import me.sentimize.sentimize.Utils.PermissionUtils;
import me.sentimize.sentimize.Utils.PlaybackLogicUtil;

public class MoodScreenActivity extends AppCompatActivity implements View.OnClickListener, SongFragment.OnListFragmentInteractionListener {

    private static PlaybackLogicUtil playbackLogicUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initFabs();


        if (findViewById(R.id.list_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            SongFragment firstFragment = new SongFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.list_container, firstFragment).commit();
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

    private FloatingActionButton fab_uplifting, fab_energetic, fab_emotional;
    private Button play_btn, pause_btn, prev_btn, skip_btn;
    private Animation fab_low, fab_med, fab_high;

    public void initFabs(){


        fab_uplifting = (FloatingActionButton) findViewById(R.id.fab_uplifting);
        fab_energetic = (FloatingActionButton) findViewById(R.id.fab_energy);
        fab_emotional = (FloatingActionButton) findViewById(R.id.fab_emotion);

        fab_low = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_low);
        fab_med = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_high= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_high);

        fab_uplifting.setOnClickListener(this);
        fab_energetic.setOnClickListener(this);
        fab_emotional.setOnClickListener(this);

        play_btn = (Button) findViewById(R.id.play_btn);
        pause_btn = (Button) findViewById(R.id.pause_btn);
        prev_btn = (Button) findViewById(R.id.prev_btn);
        skip_btn= (Button) findViewById(R.id.next_btn);

        play_btn.setOnClickListener(this);
        pause_btn.setOnClickListener(this);
        prev_btn.setOnClickListener(this);
        skip_btn.setOnClickListener(this);
    }

    public void animateFAB(int fabID){

    }

    @Override
    public void onClick(View v) {
        // without local music access we can't do anything
        if(PermissionUtils.canAccessLocalMusic(this, v)) {

            SongContent.setItems(LocalMusicRequisitionUtil.getSongList(this));

            if (v.getId() == R.id.fab_uplifting) {
                animateFAB(v.getId());
            } else if (v.getId() == R.id.play_btn) {
                songPlaying(true);
            } else if (v.getId() == R.id.fab_pause) {
                songPlaying(false);
            } else {
                System.out.println("Plus/close button was NOT tapped - " + this.getResources().getResourceName(v.getId()));
            }
        }
        else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
    }

    // Implement Marshmellow Permissions Callback
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(PermissionUtils.getView() != null) {
                        this.onClick(PermissionUtils.getView());
                    }
                }
    }

    public void onListFragmentInteraction(Song song) {
        // Do different stuff
        System.out.println("List Clicked-  - " + song.name);
        if(song instanceof LocalSong) {
            System.out.println("Getting BPM");
            LocalMusicAnalysis.getBPM((LocalSong)song, this);
            playbackLogicUtil.playSong((LocalSong)song);

        }
    }

    public void songPlaying(boolean isPlaying){
        if(isPlaying){
            System.out.println("Pressed Play");
            // play song already selected
            playbackLogicUtil.playSong();
            play_btn.setVisibility(View.VISIBLE);
            pause_btn.setVisibility(View.GONE);
        }
        else{
            System.out.println("Pressed Pause");
            playbackLogicUtil.pauseSong();
            play_btn.setVisibility(View.GONE);
            pause_btn.setVisibility(View.VISIBLE);
        }
    }

}
