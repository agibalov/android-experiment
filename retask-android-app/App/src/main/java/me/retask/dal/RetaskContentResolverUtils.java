package me.retask.dal;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.List;

import me.retask.webapi.dto.TaskDto;

public abstract class RetaskContentResolverUtils {
    public static void reset(ContentResolver contentResolver) {
        contentResolver.delete(RetaskContract.Task.CONTENT_URI, null, null);
    }

    public static void addTask(ContentResolver contentResolver, TaskDto task) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(RetaskContract.Task.REMOTE_ID, task.taskId);
        contentValues.put(RetaskContract.Task.STATUS, task.taskStatus);
        contentValues.put(RetaskContract.Task.DESCRIPTION, task.taskDescription);
        contentResolver.insert(RetaskContract.Task.CONTENT_URI, contentValues);
    }

    public static void addTasks(ContentResolver contentResolver, List<TaskDto> tasks) {
        for(TaskDto task : tasks) {
            addTask(contentResolver, task);
        }
    }

    public static int getTaskRemoteId(ContentResolver contentResolver, long taskId) {
        Uri taskUri = ContentUris.withAppendedId(RetaskContract.Task.CONTENT_URI, taskId);
        Cursor cursor = contentResolver.query(taskUri, null, null, null, null);
        try {
            if(!cursor.moveToFirst()) {
                throw new IllegalStateException("There's no task with given id");
            }

            int taskRemoteIdColumnIndex = cursor.getColumnIndex(RetaskContract.Task.REMOTE_ID);
            int taskRemoteId = cursor.getInt(taskRemoteIdColumnIndex);

            return taskRemoteId;
        } finally {
            cursor.close();
        }
    }

    public static void updateTask(ContentResolver contentResolver, long taskId, TaskDto task) {
        Uri taskUri = ContentUris.withAppendedId(RetaskContract.Task.CONTENT_URI, taskId);
        ContentValues contentValues = new ContentValues();
        contentValues.put(RetaskContract.Task.REMOTE_ID, task.taskId);
        contentValues.put(RetaskContract.Task.STATUS, task.taskStatus); // TODO: not very fair
        contentValues.put(RetaskContract.Task.DESCRIPTION, task.taskDescription);
        contentResolver.update(taskUri, contentValues, null, null);
    }

    public static void deleteTask(ContentResolver contentResolver, long taskId) {
        Uri taskUri = ContentUris.withAppendedId(RetaskContract.Task.CONTENT_URI, taskId);
        contentResolver.delete(taskUri, null, null);
    }
}
