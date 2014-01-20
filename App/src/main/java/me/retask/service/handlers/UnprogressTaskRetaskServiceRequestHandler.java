package me.retask.service.handlers;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import org.springframework.web.client.RestTemplate;

import me.retask.dal.RetaskContract;
import me.retask.webapi.ApiCall;
import me.retask.webapi.apicalls.UnprogressTaskApiCall;
import me.retask.webapi.dto.ServiceResultDto;
import me.retask.webapi.dto.TaskDto;
import roboguice.util.Ln;

public class UnprogressTaskRetaskServiceRequestHandler extends RetaskServiceRequestHandler {
    @Override
    public void process(Intent input, ContentResolver contentResolver, String apiRootUrl, RestTemplate restTemplate) {
        long taskId = getTaskId(input);
        String sessionToken = getSessionToken(input);
        int taskRemoteId = getTaskRemoteId(taskId, contentResolver);

        ApiCall<TaskDto> unprogressTaskApiCall = new UnprogressTaskApiCall(sessionToken, taskRemoteId);
        ServiceResultDto<TaskDto> progressTaskApiCallResult = unprogressTaskApiCall.performApiCall(apiRootUrl, restTemplate);
        if(!progressTaskApiCallResult.ok) {
            Ln.i("Request to web API failed");
            return;
        }

        TaskDto taskDto = progressTaskApiCallResult.payload;
        updateTaskStatus(taskId, taskDto.taskStatus, contentResolver);
    }

    private static int getTaskRemoteId(long taskId, ContentResolver contentResolver) {
        Uri taskUri = ContentUris.withAppendedId(RetaskContract.Task.CONTENT_URI, taskId);
        Cursor cursor = contentResolver.query(taskUri, null, null, null, null);
        if(!cursor.moveToFirst()) {
            throw new IllegalStateException("There's not task with given id");
        }

        int taskRemoteIdColumnIndex = cursor.getColumnIndex(RetaskContract.Task.REMOTE_ID);
        int taskRemoteId = cursor.getInt(taskRemoteIdColumnIndex);

        return taskRemoteId;
    }

    private static void updateTaskStatus(long taskId, int taskStatus, ContentResolver contentResolver) {
        Uri taskUri = ContentUris.withAppendedId(RetaskContract.Task.CONTENT_URI, taskId);
        ContentValues contentValues = new ContentValues();
        contentValues.put(RetaskContract.Task.STATUS, taskStatus); // TODO: not very fair
        contentResolver.update(taskUri, contentValues, null, null);
    }
}
