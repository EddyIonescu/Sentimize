package me.sentimize.sentimize.Models;

import android.support.design.widget.FloatingActionButton;

/**
 * Created by Eddy on 9/10/16.
 */
public class MoodModel {
    private int id;
    private FloatingActionButton fab;
    private String mood;

    /**
     * Energy is a measure from 0.0 to 1.0 and represents a perceptual measure of intensity and activity.
     * Typically, energetic tracks feel fast, loud, and noisy.
     * For example, death metal has high energy, while a Bach prelude scores low on the scale.
     * Perceptual features contributing to this attribute include dynamic range, perceived loudness, timbre, onset rate, and general entropy.
     * https://developer.spotify.com/web-api/get-audio-features/
     */
    private double energy;

    /**	A measure from 0.0 to 1.0 describing the musical positiveness conveyed by a track.
     * Tracks with high valence sound more positive (e.g. happy, cheerful, euphoric),
     * while tracks with low valence sound more negative (e.g. sad, depressed, angry).
     * https://developer.spotify.com/web-api/get-audio-features/
    */
     private double valence;

}
