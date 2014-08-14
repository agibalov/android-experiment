package me.loki2302.androidtestingexperiment;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class DummyContentProvider extends ContentProvider {
    public final static String AUTHORITY = "me.loki2302.androidtestingexperiment.DummyContentProvider";

    private final static UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private final static String NOTES_PATH = "notes";
    private final static String NOTE_PATH = "notes/#";

    public final static Uri CONTENT_URI = Uri.parse(String.format("content://%s/%s", AUTHORITY, NOTES_PATH));

    private final static int NOTES_URI_TYPE = 1;
    private final static int NOTE_URI_TYPE = 2;

    private DummySQLiteOpenHelper databaseOpenHelper;

    static {
        URI_MATCHER.addURI(AUTHORITY, NOTES_PATH, NOTES_URI_TYPE);
        URI_MATCHER.addURI(AUTHORITY, NOTE_PATH, NOTE_URI_TYPE);
    }

    @Override
    public boolean onCreate() {
        databaseOpenHelper = new DummySQLiteOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DummySQLiteOpenHelper.TABLE_NOTES);

        int uriType = URI_MATCHER.match(uri);
        if(uriType == NOTES_URI_TYPE) {
            // intentionally doing nothing
        } else if(uriType == NOTE_URI_TYPE) {
            String id = uri.getLastPathSegment();
            queryBuilder.appendWhere(DummySQLiteOpenHelper.COLUMN_ID + "=" + id);
        } else {
            throw new IllegalArgumentException("Can't query, unknown URI: " + uri);
        }

        SQLiteDatabase db = databaseOpenHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = databaseOpenHelper.getWritableDatabase();
        int uriType = URI_MATCHER.match(uri);
        long id;
        if(uriType == NOTES_URI_TYPE) {
            id = db.insert(DummySQLiteOpenHelper.TABLE_NOTES, null, values);
            getContext().getContentResolver().notifyChange(uri, null);
        } else {
            throw new IllegalArgumentException("Can't insert, unknown URI: " + uri);
        }

        return Uri.parse(String.format("%s/%d", NOTES_PATH, id));
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }
}
