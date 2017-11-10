package me.retask.dal;

import android.net.Uri;
import android.provider.BaseColumns;

public class RetaskContract {
    public final static String AUTHORITY = "me.retask.tasks";

    public static abstract class Task implements BaseColumns {
        public final static Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/tasks");
        public final static String CONTENT_TYPE = "vnd.android.cursor.dir/me.retask.task";
        public final static String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/me.retask.task";
        public final static String TABLE_NAME = "tasks";
        public final static String REMOTE_ID = "remoteId";
        public final static String STATUS = "status";
        public final static String DESCRIPTION = "description";

        public final static int TASK_STATUS_TODO = 0;
        public final static int TASK_STATUS_IN_PROGRESS = 1;
        public final static int TASK_STATUS_DONE = 2;
        public final static int TASK_STATUS_COMPLETE = 3;
    }
}
