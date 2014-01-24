package me.retask.activities;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import me.retask.R;
import me.retask.dal.RetaskContract;
import me.retask.service.requests.ServiceRequest;
import me.retask.service.requests.UpdateTaskRequest;

public class EditTaskActivity extends RetaskActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private EditText taskDescriptionEditText;

    private long taskId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task_view);

        taskDescriptionEditText = (EditText)findViewById(R.id.taskDescriptionEditText);

        Intent intent = getIntent();
        taskId = intent.getLongExtra("taskId", -1);
        if(taskId == -1) {
            throw new IllegalStateException("Looks like taskId is missing in Intent");
        }

        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.updateTaskMenuItem) {
            String taskDescription = taskDescriptionEditText.getText().toString();
            if(taskDescription == null || taskDescription.equals("")) {
                return true;
            }

            run(new UpdateTaskRequest(taskId, taskDescription));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Uri taskUri = ContentUris.withAppendedId(RetaskContract.Task.CONTENT_URI, taskId);
        CursorLoader cursorLoader = new CursorLoader(
                this,
                taskUri,
                new String[] {
                        RetaskContract.Task._ID,
                        RetaskContract.Task.DESCRIPTION,
                        RetaskContract.Task.STATUS
                },
                null, null, null);

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if(!cursor.moveToFirst()) {
            return;
        }

        String taskDescription = cursor.getString(cursor.getColumnIndex(RetaskContract.Task.DESCRIPTION));
        taskDescriptionEditText.setText(taskDescription);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }

    @Override
    public void onSuccess(ServiceRequest<?> request, Object result) {
        super.onSuccess(request, result);

        if(request instanceof UpdateTaskRequest) {
            finish();
            return;
        }

        throw new IllegalStateException("Didn't expect this request here");
    }
}
