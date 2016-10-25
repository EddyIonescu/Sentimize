package me.sentimize.sentimize;

import android.Manifest;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.view.Surface;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import me.sentimize.sentimize.Fragments.Song.NextPrevSongLogic;
import me.sentimize.sentimize.Fragments.Song.SongContent;
import me.sentimize.sentimize.Fragments.SongFragment;
import me.sentimize.sentimize.Models.LocalSong;
import me.sentimize.sentimize.Models.LocalSongAnalysisRequest;
import me.sentimize.sentimize.Models.Song;
import me.sentimize.sentimize.Services.PlayMusicService;
import me.sentimize.sentimize.Utils.LocalMusicAnalysis;
import me.sentimize.sentimize.Utils.LocalMusicPlayer;
import me.sentimize.sentimize.Utils.LocalMusicRequisitionUtil;
import me.sentimize.sentimize.Utils.LocalSongCaching;
import me.sentimize.sentimize.Utils.PermissionUtils;
import me.sentimize.sentimize.Utils.PlaybackLogicUtil;
import me.sentimize.sentimize.Utils.SongFiltering;

public class MoodScreenActivity extends AppCompatActivity implements View.OnClickListener, SongFragment.OnListFragmentInteractionListener, SeekBar.OnSeekBarChangeListener {

    private boolean isPlaying = false;
    final Handler seekHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initFabs();
        lockOrientation();

        // initialize music list fragment
        if (findViewById(R.id.list_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            SongFragment firstFragment = new SongFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.list_container, firstFragment).commit();
        }

        /*
        Timer updateBar = new Timer();
        updateBar.schedule(new TimerTask() {
            @Override
            public void run() {updateSeekbar();}
        }, 0, 10000);
        */
    }

    private void updateSeekbar(){
        if(isPlaying) {
            seekBar.setProgress(seekBar.getProgress()+10000);
        }
        //seekHandler.post(seekRunnable);

    }

    final Runnable seekRunnable = new Runnable() {
        public void run() {
            if(isPlaying) {
                seekBar.setProgress(seekBar.getProgress()+6000);
            }
        }
    };

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
        else if(id == R.id.action_reanalyze){
            // for testing purposes
            SongFiltering.initMoodAct(this); // required to show snackbar
            ArrayList<LocalSong> localSongs = LocalMusicRequisitionUtil.getSongList(this);
            Collections.shuffle(localSongs);

            LocalSongAnalysisRequest firstRequest = null;
            LocalSongAnalysisRequest currentRequest = null;
            for(LocalSong song : localSongs){
                System.out.println("analyzing (reanalyze-mode) " + song);
                if(firstRequest == null){
                    firstRequest = new LocalSongAnalysisRequest(song, null);
                    currentRequest = firstRequest;
                }
                else{
                    LocalSongAnalysisRequest request = new LocalSongAnalysisRequest(song, null);
                    currentRequest.setNextRequest(request);
                    currentRequest = request;
                }
            }
            if(!LocalMusicAnalysis.isBusy() && firstRequest != null) new LocalMusicAnalysis().execute(firstRequest);
        }
        return super.onOptionsItemSelected(item);
    }

    private FloatingActionButton fab_uplifting, fab_energetic, fab_emotional;
    private AppCompatImageButton play_btn, pause_btn, prev_btn, skip_btn;
    private Animation fab_lowtomed, fab_medtolow, fab_medtohigh, fab_hightomed;
    private SeekBar seekBar;
    private int uplifting = 1;
    private int energetic = 1;
    private int emotional = 1;

    public void initFabs(){

        fab_uplifting = (FloatingActionButton) findViewById(R.id.fab_uplifting);
        fab_energetic = (FloatingActionButton) findViewById(R.id.fab_energy);
        fab_emotional = (FloatingActionButton) findViewById(R.id.fab_emotion);

        fab_lowtomed = AnimationUtils.loadAnimation(this, R.anim.fab_lowtomed);
        fab_medtolow = AnimationUtils.loadAnimation(this, R.anim.fab_medtolow);
        fab_medtohigh= AnimationUtils.loadAnimation(this, R.anim.fab_medtohigh);
        fab_hightomed= AnimationUtils.loadAnimation(this, R.anim.fab_hightomed);

        fab_uplifting.setOnClickListener(this);
        fab_energetic.setOnClickListener(this);
        fab_emotional.setOnClickListener(this);

        play_btn = (AppCompatImageButton) findViewById(R.id.play_btn);
        pause_btn = (AppCompatImageButton) findViewById(R.id.pause_btn);
        prev_btn = (AppCompatImageButton) findViewById(R.id.prev_btn);
        skip_btn= (AppCompatImageButton) findViewById(R.id.next_btn);

        play_btn.setOnClickListener(this);
        pause_btn.setOnClickListener(this);
        prev_btn.setOnClickListener(this);
        skip_btn.setOnClickListener(this);

        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setMax(1);
        seekBar.setProgress(0);
    }

    private void lockOrientation(){
        int orientation = this.getRequestedOrientation();
        int rotation = ((WindowManager) this.getSystemService(
                Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                break;
            case Surface.ROTATION_90:
                orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                break;
            case Surface.ROTATION_180:
                orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                break;
            default:
                orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                break;
        }
        this.setRequestedOrientation(orientation);
    }

    private boolean increasing = true;
    private int changeFabSize(final FloatingActionButton fab, int fromSize){
        fab_uplifting.setEnabled(false);
        fab_energetic.setEnabled(false);
        fab_emotional.setEnabled(false);
        int toSize = 0;
        switch (fromSize){
            case 0:
                fab.setAnimation(fab_lowtomed);
                increasing = true;
                toSize = 1;
                break;
            case 1:
                if(increasing) fab.setAnimation(fab_medtohigh); else fab.setAnimation(fab_medtolow);
                toSize = increasing ? 2 : 0;
                break;
            case 2:
                fab.setAnimation(fab_hightomed);
                increasing = false;
                toSize = 1;
        }
        fab.animate();
        return toSize;
    }

    public static void updateListPostAnalysis(LocalSong song){
        if(SongFiltering.songEligible(song)){
            SongContent.addItem(song);
        }
    }

    private void updateList(){
        SongContent.setItems(SongFiltering.filterLocalSongs(LocalMusicRequisitionUtil.getSongList(this),
                uplifting, energetic, emotional, this));
        fab_uplifting.setEnabled(true);
        fab_energetic.setEnabled(true);
        fab_emotional.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        // without local music access we can't do anything
        if(PermissionUtils.canAccessLocalMusic(this, v)) {

            if (v.getId() == R.id.fab_uplifting) {
                uplifting = changeFabSize(fab_uplifting, uplifting);
                updateList();
            }
            else if(v.getId() == R.id.fab_energy) {
                energetic = changeFabSize(fab_energetic, energetic);
                updateList();
            }
            else if(v.getId() == R.id.fab_emotion){
                emotional = changeFabSize(fab_emotional, emotional);
                updateList();
                SongFiltering.showSnackbarUpdate("emotion/passion analysis coming soon");
            }
            else if (v.getId() == R.id.play_btn) {
                pressedPlay();
            }
            else if (v.getId() == R.id.pause_btn) {
                pressedPause();
            }
            else if (v.getId() == R.id.next_btn) {
                onListFragmentInteraction(NextPrevSongLogic.nextSong());
            }
            else if (v.getId() == R.id.prev_btn) {
                onListFragmentInteraction(NextPrevSongLogic.prevSong());
            }
            else {
                System.out.println("Plus/close button was NOT tapped - " + this.getResources().getResourceName(v.getId()));
            }
            System.out.println("Uplifting: " + uplifting + " Energetic: " + energetic + " Emotional: " + emotional);
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
        System.out.println("List Clicked-  - " + song.toString());
        if(song instanceof LocalSong) {
            NextPrevSongLogic.setCurrentSong(song);
            seekBar.setProgress(0);
            seekBar.setMax(song.getDuration());
            SentiApplication.getPlaybackLogicUtil().playSong((LocalSong)song);
            pressedPlay();
        }
    }

    public void pressedPlay(){
        isPlaying = true;
        System.out.println("Pressed Play");
        // play song already selected
        SentiApplication.getPlaybackLogicUtil().playSong();
        play_btn.setVisibility(View.GONE);
        pause_btn.setVisibility(View.VISIBLE);
    }

    public void pressedPause(){
        isPlaying = false;
        System.out.println("Pressed Pause");
        SentiApplication.getPlaybackLogicUtil().pauseSong();
        play_btn.setVisibility(View.VISIBLE);
        pause_btn.setVisibility(View.GONE);
    }

    // Implement Seekbar Methods
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        SentiApplication.setProgress(i);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
