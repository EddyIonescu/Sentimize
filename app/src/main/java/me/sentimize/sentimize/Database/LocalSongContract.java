package me.sentimize.sentimize.Database;

        import android.provider.BaseColumns;

/**
 * Created by Eddy on 10/15/16.
 */
public final class LocalSongContract {

    public static class SongEntry implements BaseColumns{
        public static final int DATABASE_VERSION = 10;
        public static final String DATABASE_NAME = "SentimizeLocalAnalysisData.db";
        public static final String SONGS_TABLE_NAME = "songs";
        public static final String SONGS_COLUMN_NAME = "name";
        public static final String SONGS_COLUMN_UPLIFTING = "positivity";
        public static final String SONGS_COLUMN_ENERGETIC = "energy";
        public static final String SONGS_COLUMN_EMOTIONAL = "emotion";
        public static final String SONGS_COLUMN_BPM = "BPM";
        public static final String SONGS_COLUMN_AVG_LOW_FREQ = "AverageLowFreq";
        public static final String SONGS_COLUMN_AVG_MED_FREQ = "AverageMidFreq";
        public static final String SONGS_COLUMN_AVG_HIGH_FREQ = "AverageHighFreq";
        public static final String SONGS_COLUMN_KEY = "KeyIndex";
    }
}
