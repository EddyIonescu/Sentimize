package me.sentimize.sentimize.Database;

import android.provider.BaseColumns;

/**
 * Created by Eddy on 10/15/16.
 */
public final class SongContract {

    public static class SongEntry implements BaseColumns{
        public static final int DATABASE_VERSION = 3;
        public static final String DATABASE_NAME = "SentimizeAnalysis.db";
        public static final String SONGS_TABLE_NAME = "songs";
        public static final String SONGS_COLUMN_NAME = "name";
        public static final String SONGS_COLUMN_UPLIFTING = "uplifting";
        public static final String SONGS_COLUMN_ENERGETIC = "energetic";
        public static final String SONGS_COLUMN_EMOTIONAL = "emotional";
    }
}
