package me.loki2302.androidtestingexperiment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DummySQLiteOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "dummy.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NOTES = "notes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CONTENT = "content";

    public DummySQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format(
                "create table %s(%s integer primary key autoincrement, %s text not null)",
                TABLE_NOTES,
                COLUMN_ID,
                COLUMN_CONTENT);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NOTES);
        onCreate(db);
    }
}
