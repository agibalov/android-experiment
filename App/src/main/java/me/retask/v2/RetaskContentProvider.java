package me.retask.v2;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

public class RetaskContentProvider extends ContentProvider {
    private final static int TASKS = 1;
    private final static int TASK_ID = 2;

    private final static UriMatcher uriMatcher;

    private final static Map<String, String> tasksProjectionMap;

    private DbHelper dbHelper;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(RetaskContract.AUTHORITY, "tasks", TASKS);
        uriMatcher.addURI(RetaskContract.AUTHORITY, "tasks/#", TASK_ID);

        tasksProjectionMap = new HashMap<String, String>();
        tasksProjectionMap.put(RetaskContract.Task._ID, RetaskContract.Task._ID);
        tasksProjectionMap.put(RetaskContract.Task.REMOTE_ID, RetaskContract.Task.REMOTE_ID);
        tasksProjectionMap.put(RetaskContract.Task.STATUS, RetaskContract.Task.STATUS);
        tasksProjectionMap.put(RetaskContract.Task.DESCRIPTION, RetaskContract.Task.DESCRIPTION);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int uriId = uriMatcher.match(uri);

        if(uriId == TASKS) {
            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
            queryBuilder.setTables(RetaskContract.Task.TABLE_NAME);
            queryBuilder.setProjectionMap(tasksProjectionMap);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        }

        if(uriId == TASK_ID) {
            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
            queryBuilder.setTables(RetaskContract.Task.TABLE_NAME);
            queryBuilder.setProjectionMap(tasksProjectionMap);
            queryBuilder.appendWhere(RetaskContract.Task._ID + "=" + uri.getLastPathSegment());
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        }

        throw new IllegalArgumentException("Unknown URI " + uri);
    }

    @Override
    public String getType(Uri uri) {
        int uriId = uriMatcher.match(uri);

        if(uriId == TASKS) {
            return RetaskContract.Task.CONTENT_TYPE;
        }

        if(uriId == TASK_ID) {
            return RetaskContract.Task.CONTENT_ITEM_TYPE;
        }

        throw new IllegalArgumentException("Unknown URI " + uri);
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int uriId = uriMatcher.match(uri);

        if(uriId == TASKS) {
            if(!contentValues.containsKey(RetaskContract.Task.REMOTE_ID)) {
                throw new IllegalArgumentException("RemoteId has not been provided");
            }

            if(!contentValues.containsKey(RetaskContract.Task.STATUS)) {
                throw new IllegalArgumentException("Status has not been provided");
            }

            if(!contentValues.containsKey(RetaskContract.Task.DESCRIPTION)) {
                throw new IllegalArgumentException("TaskDescription has not been provided");
            }

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            long rowId = db.insert(RetaskContract.Task.TABLE_NAME, null, contentValues);
            if(rowId == 0) {
                throw new RuntimeException("Failed to insert row");
            }

            Uri taskUri = ContentUris.withAppendedId(RetaskContract.Task.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(taskUri, null);
            return taskUri;
        }

        throw new IllegalArgumentException("Unknown URI " + uri);
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        int uriId = uriMatcher.match(uri);

        if(uriId == TASKS) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int rowCount = db.delete(RetaskContract.Task.TABLE_NAME, where, whereArgs);
            getContext().getContentResolver().notifyChange(uri, null);
            return rowCount;
        }

        if(uriId == TASK_ID) {
            String taskId = uri.getPathSegments().get(1);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int rowCount = db.delete(RetaskContract.Task.TABLE_NAME,
                    RetaskContract.Task._ID + "=" + taskId + (!TextUtils.isEmpty(where) ? " and (" + where + ")" : ""),
                    whereArgs);
            getContext().getContentResolver().notifyChange(uri, null);
            return rowCount;
        }

        throw new IllegalArgumentException("Unknown URI " + uri);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String where, String[] whereArgs) {
        int uriId = uriMatcher.match(uri);

        if(uriId == TASKS) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int rowCount = db.update(RetaskContract.Task.TABLE_NAME, contentValues, where, whereArgs);
            getContext().getContentResolver().notifyChange(uri, null);
            return rowCount;
        }

        if(uriId == TASK_ID) {
            String taskId = uri.getPathSegments().get(1);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int rowCount = db.update(RetaskContract.Task.TABLE_NAME,
                    contentValues,
                    RetaskContract.Task._ID + "=" + taskId + (!TextUtils.isEmpty(where) ? " and (" + where + ")" : ""),
                    whereArgs);
            getContext().getContentResolver().notifyChange(uri, null);
            return rowCount;
        }

        throw new IllegalArgumentException("Unknown URI " + uri);
    }

    private static class DbHelper extends SQLiteOpenHelper {
        private final static int DATABASE_VERSION = 1;
        private final static String DATABASE_NAME = "retask.db";

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(String.format(
                    "create table %s(" +
                    "  %s integer primary key," +
                    "  %s text not null," +
                    "  %s int not null," +
                    "  %s text not null" +
                    ")",
                    RetaskContract.Task.TABLE_NAME,
                    RetaskContract.Task._ID,
                    RetaskContract.Task.REMOTE_ID,
                    RetaskContract.Task.STATUS,
                    RetaskContract.Task.DESCRIPTION));
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            sqLiteDatabase.execSQL("drop table if exists " + RetaskContract.Task.TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
    }
}
