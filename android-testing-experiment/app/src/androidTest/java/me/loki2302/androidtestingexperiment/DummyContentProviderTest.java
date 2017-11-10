package me.loki2302.androidtestingexperiment;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;

import static android.test.MoreAsserts.assertNotEqual;

public class DummyContentProviderTest extends ProviderTestCase2<DummyContentProvider> {
    public DummyContentProviderTest() {
        super(DummyContentProvider.class, DummyContract.AUTHORITY);
    }

    public void testContentProviderIsEmptyByDefault() {
        ContentResolver contentResolver = getMockContentResolver();
        Cursor cursor = contentResolver.query(DummyContract.Notes.NOTES_CONTENT_URI, null, null, null, null);
        try {
            assertEquals(2, cursor.getColumnCount());

            int idColumnIndex = cursor.getColumnIndex(DummySQLiteOpenHelper.COLUMN_ID);
            assertNotEqual(-1, idColumnIndex);

            int contentColumnIndex = cursor.getColumnIndex(DummySQLiteOpenHelper.COLUMN_CONTENT);
            assertNotEqual(-1, contentColumnIndex);

            int rowCount = cursor.getCount();
            assertEquals(0, rowCount);
        } finally {
            cursor.close();
        }
    }

    public void testCanAddDataToContentProvider() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DummySQLiteOpenHelper.COLUMN_CONTENT, "hello");

        ContentResolver contentResolver = getMockContentResolver();
        Uri uri = contentResolver.insert(DummyContract.Notes.NOTES_CONTENT_URI, contentValues);
        String idString = uri.getLastPathSegment();
        assertEquals("1", idString);

        Cursor cursor = contentResolver.query(DummyContract.Notes.NOTES_CONTENT_URI, null, null, null, null);
        try {
            int rowCount = cursor.getCount();
            assertEquals(1, rowCount);

            int idColumnIndex = cursor.getColumnIndex(DummySQLiteOpenHelper.COLUMN_ID);
            int contentColumnIndex = cursor.getColumnIndex(DummySQLiteOpenHelper.COLUMN_CONTENT);
            assertTrue(cursor.moveToFirst());
            assertEquals(1, cursor.getLong(idColumnIndex));
            assertEquals("hello", cursor.getString(contentColumnIndex));
        } finally {
            cursor.close();
        }
    }
}
