package me.loki2302.fsm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class StateService {
    private final Context context;

    public StateService(Context context) {
        this.context = context;
    }

    public <T> T loadState(Class<T> clazz, int id) {
        String tag = String.format("%s-%d", clazz.toString(), id);
        Log.i("AAA", String.format("RETRIEVE STATE: tag='%s'", tag));

        StateDbHelper stateDbHelper = new StateDbHelper(context);
        SQLiteDatabase db = stateDbHelper.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select id, tag, stateJson from states where tag=?", new String[] { tag });
            try {
                if(!cursor.moveToFirst()) {
                    Log.i("AAA", String.format("RETRIEVE STATE: no records"));
                    return null;
                }

                if(cursor.getCount() != 1) {
                    throw new IllegalStateException();
                }

                int stateJsonColumnIndex = cursor.getColumnIndex("stateJson");
                String stateJson = cursor.getString(stateJsonColumnIndex);
                Log.i("AAA", String.format("RETRIEVE STATE: json=%s", stateJson));
                if(stateJson == null || stateJson.equals("")) {
                    throw new IllegalStateException();
                }

                return (T)deserialize(stateJson, clazz);
            } finally {
                cursor.close();
            }
        } finally {
            db.close();
        }
    }

    public void saveState(Class<?> clazz, Object o, int id) {
        String tag = String.format("%s-%d", clazz.toString(), id);
        String stateJson = serialize(o);
        Log.i("AAA", String.format("SAVE STATE: tag='%s'", tag));
        Log.i("AAA", String.format("SAVE STATE: json=%s", stateJson));
        StateDbHelper stateDbHelper = new StateDbHelper(context);
        SQLiteDatabase db = stateDbHelper.getWritableDatabase();
        try {
            db.execSQL("delete from states where tag=?", new Object[] { tag });
            db.execSQL("insert into states(tag, stateJson) values(?, ?)", new Object[] {tag, stateJson});
        } finally {
            db.close();
        }
    }

    private static String serialize(Object o) {
        if(o == null) {
            throw new IllegalArgumentException("tried to serialize null");
        }

        try {
            return new ObjectMapper().writeValueAsString(o);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static <T> T deserialize(String json, Class<?> clazz) {
        try {
            T o = (T)new ObjectMapper().readValue(json, clazz);
            if(o == null) {
                throw new RuntimeException("deserialized null");
            }

            return o;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
