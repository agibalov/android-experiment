package me.loki2302.androidtestingexperiment;

import android.net.Uri;

public final class DummyContract {
    public final static String AUTHORITY = "me.loki2302.androidtestingexperiment.DummyContentProvider";
    public final static Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final class Notes {
        public final static String NOTES_PATH = "notes";
        public final static Uri NOTES_CONTENT_URI = Uri.withAppendedPath(DummyContract.CONTENT_URI, NOTES_PATH);

        public final static String NOTE_PATH = "notes/#";
        public final static Uri NOTE_CONTENT_URI = Uri.withAppendedPath(DummyContract.CONTENT_URI, NOTE_PATH);
    }
}
