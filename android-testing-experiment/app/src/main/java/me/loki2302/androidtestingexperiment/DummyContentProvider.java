package me.loki2302.androidtestingexperiment;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class DummyContentProvider extends ContentProvider {
    private final static UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private final static int NOTES_URI_CODE = 1;
    private final static int NOTE_URI_CODE = 2;

    private DummySQLiteOpenHelper databaseOpenHelper;

    static {
        URI_MATCHER.addURI(DummyContract.AUTHORITY, DummyContract.Notes.NOTES_PATH, NOTES_URI_CODE);
        URI_MATCHER.addURI(DummyContract.AUTHORITY, DummyContract.Notes.NOTE_PATH, NOTE_URI_CODE);
    }

    @Override
    public boolean onCreate() {
        databaseOpenHelper = new DummySQLiteOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        int uriCode = URI_MATCHER.match(uri);
        if(uriCode == NOTES_URI_CODE) {
            queryBuilder.setTables(DummySQLiteOpenHelper.TABLE_NOTES);
        } else if(uriCode == NOTE_URI_CODE) {
            queryBuilder.setTables(DummySQLiteOpenHelper.TABLE_NOTES);

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
        int uriCode = URI_MATCHER.match(uri);
        if(uriCode == NOTES_URI_CODE) {
            long id = db.insert(DummySQLiteOpenHelper.TABLE_NOTES, null, values);
            getContext().getContentResolver().notifyChange(uri, null);
            return Uri.withAppendedPath(DummyContract.Notes.NOTES_CONTENT_URI, String.valueOf(id));
        }

        throw new IllegalArgumentException("Can't insert, unknown URI: " + uri);
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
