package me.loki2302.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;

import com.google.inject.Inject;
import com.petebevin.markdown.MarkdownProcessor;

import me.loki2302.R;
import me.loki2302.application.Task;
import me.loki2302.dal.ApplicationState;
import me.loki2302.dal.dto.TaskStatus;
import roboguice.inject.InjectResource;

public class TaskActivity extends RetaskActivity {
	private final static MarkdownProcessor markdownProcessor = new MarkdownProcessor();
	
	@Inject
	private ApplicationState applicationState;
			
	private WebView taskDescriptionWebView;
	
	@InjectResource(R.string.markdown_html_template)
	private String markdownHtmlTemplate;

    private Task task;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.task_view);

        taskDescriptionWebView = (WebView)findViewById(R.id.taskDescriptionWebView);

		Intent intent = getIntent();
		int taskId = intent.getIntExtra("taskId", -1);
		if(taskId == -1) {
			throw new IllegalStateException("Looks like taskId is missing in Intent");
		}
		
		task = applicationState.getTaskRepository().getOne(taskId);
		String taskDescription = task.description;
		String taskDescriptionHtml = markdownProcessor.markdown(taskDescription);
		String html = String.format(markdownHtmlTemplate, taskDescriptionHtml);
		taskDescriptionWebView.loadData(html, "text/html", null);	
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.task_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        TaskStatus taskStatus = task.status;
        if(taskStatus.equals(TaskStatus.NotStarted)) {
            menu.findItem(R.id.wontDoMenuItem).setVisible(true);
            menu.findItem(R.id.startMenuItem).setVisible(true);
            menu.findItem(R.id.postponeMenuItem).setVisible(false);
            menu.findItem(R.id.doneMenuItem).setVisible(false);
            menu.findItem(R.id.notDoneMenuItem).setVisible(false);
            menu.findItem(R.id.completeMenuItem).setVisible(false);
        } else if(taskStatus.equals(TaskStatus.InProgress)) {
            menu.findItem(R.id.wontDoMenuItem).setVisible(false);
            menu.findItem(R.id.startMenuItem).setVisible(false);
            menu.findItem(R.id.postponeMenuItem).setVisible(true);
            menu.findItem(R.id.doneMenuItem).setVisible(true);
            menu.findItem(R.id.notDoneMenuItem).setVisible(false);
            menu.findItem(R.id.completeMenuItem).setVisible(false);
        } else if(taskStatus.equals(TaskStatus.Done)) {
            menu.findItem(R.id.wontDoMenuItem).setVisible(false);
            menu.findItem(R.id.startMenuItem).setVisible(false);
            menu.findItem(R.id.postponeMenuItem).setVisible(false);
            menu.findItem(R.id.doneMenuItem).setVisible(false);
            menu.findItem(R.id.notDoneMenuItem).setVisible(true);
            menu.findItem(R.id.completeMenuItem).setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if(itemId == R.id.wontDoMenuItem) {
            return true;
        }

        if(itemId == R.id.startMenuItem) {
            return true;
        }

        if(itemId == R.id.postponeMenuItem) {
            return true;
        }

        if(itemId == R.id.doneMenuItem) {
            return true;
        }

        if(itemId == R.id.notDoneMenuItem) {
            return true;
        }

        if(itemId == R.id.completeMenuItem) {
            return true;
        }

        if(itemId == R.id.editMenuItem) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}