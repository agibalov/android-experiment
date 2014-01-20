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
import android.webkit.WebView;

import com.google.inject.Inject;
import com.petebevin.markdown.MarkdownProcessor;

import me.retask.R;
import me.retask.dal.ApplicationState;
import me.retask.service.requests.DeleteTaskRetaskServiceRequest;
import me.retask.service.requests.ProgressTaskRetaskServiceRequest;
import me.retask.service.requests.UnprogressTaskRetaskServiceRequest;
import me.retask.dal.RetaskContract;
import roboguice.inject.InjectResource;

public class ViewTaskActivity extends RetaskActivity implements LoaderManager.LoaderCallbacks<Cursor> {
	private final static MarkdownProcessor markdownProcessor = new MarkdownProcessor();
	
	@Inject
	private ApplicationState applicationState;
			
	private WebView taskDescriptionWebView;
	
	@InjectResource(R.string.markdown_html_template)
	private String markdownHtmlTemplate;

    private long taskId;
    private Integer taskStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.task_view);

        taskDescriptionWebView = (WebView)findViewById(R.id.taskDescriptionWebView);

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
        menuInflater.inflate(R.menu.view_task_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(taskStatus == null) {
            return true;
        }

        if(taskStatus.equals(RetaskContract.Task.TASK_STATUS_TODO)) {
            menu.findItem(R.id.wontDoMenuItem).setVisible(true);
            menu.findItem(R.id.startMenuItem).setVisible(true);
            menu.findItem(R.id.postponeMenuItem).setVisible(false);
            menu.findItem(R.id.doneMenuItem).setVisible(false);
            menu.findItem(R.id.notDoneMenuItem).setVisible(false);
            menu.findItem(R.id.completeMenuItem).setVisible(false);
        } else if(taskStatus.equals(RetaskContract.Task.TASK_STATUS_IN_PROGRESS)) {
            menu.findItem(R.id.wontDoMenuItem).setVisible(false);
            menu.findItem(R.id.startMenuItem).setVisible(false);
            menu.findItem(R.id.postponeMenuItem).setVisible(true);
            menu.findItem(R.id.doneMenuItem).setVisible(true);
            menu.findItem(R.id.notDoneMenuItem).setVisible(false);
            menu.findItem(R.id.completeMenuItem).setVisible(false);
        } else if(taskStatus.equals(RetaskContract.Task.TASK_STATUS_DONE)) {
            menu.findItem(R.id.wontDoMenuItem).setVisible(false);
            menu.findItem(R.id.startMenuItem).setVisible(false);
            menu.findItem(R.id.postponeMenuItem).setVisible(false);
            menu.findItem(R.id.doneMenuItem).setVisible(false);
            menu.findItem(R.id.notDoneMenuItem).setVisible(true);
            menu.findItem(R.id.completeMenuItem).setVisible(true);
        } else {
            throw new RuntimeException("Should never get here");
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if(itemId == R.id.wontDoMenuItem) {
            deleteTask();
            return true;
        }

        if(itemId == R.id.startMenuItem) {
            progressTask();
            return true;
        }

        if(itemId == R.id.postponeMenuItem) {
            unprogressTask();
            return true;
        }

        if(itemId == R.id.doneMenuItem) {
            progressTask();
            return true;
        }

        if(itemId == R.id.notDoneMenuItem) {
            unprogressTask();
            return true;
        }

        if(itemId == R.id.completeMenuItem) {
            progressTask();
            return true;
        }

        if(itemId == R.id.editMenuItem) {
            Intent intent = new Intent(ViewTaskActivity.this, EditTaskActivity.class);
            intent.putExtra("taskId", taskId);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void progressTask() {
        run(new ProgressTaskRetaskServiceRequest(applicationState.getSessionToken(), taskId));
    }

    private void unprogressTask() {
        run(new UnprogressTaskRetaskServiceRequest(applicationState.getSessionToken(), taskId));
    }

    private void deleteTask() {
        run(new DeleteTaskRetaskServiceRequest(applicationState.getSessionToken(), taskId));
        finish();
    }

    @Override
    public Loader onCreateLoader(int id, Bundle bundle) {
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
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(!cursor.moveToFirst()) {
            return;
        }

        String taskDescription = cursor.getString(cursor.getColumnIndex(RetaskContract.Task.DESCRIPTION));
        taskStatus = cursor.getInt(cursor.getColumnIndex(RetaskContract.Task.STATUS));

        if(taskStatus == RetaskContract.Task.TASK_STATUS_COMPLETE) {
            finish();
        }

        String taskDescriptionHtml = markdownProcessor.markdown(taskDescription);
        String html = String.format(markdownHtmlTemplate, taskDescriptionHtml);
        taskDescriptionWebView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
        supportInvalidateOptionsMenu();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
