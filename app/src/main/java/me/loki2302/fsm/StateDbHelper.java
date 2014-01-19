package me.loki2302.fsm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StateDbHelper extends SQLiteOpenHelper {
    private final static String DB_NAME = "mydb";
    private final static int DB_VERSION = 1;

    public StateDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table states(id integer primary key, tag text not null, stateJson text not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists states");
        onCreate(db);
    }
}
