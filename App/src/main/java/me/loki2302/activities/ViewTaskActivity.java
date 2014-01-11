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
import me.loki2302.dal.apicalls.ProgressTaskApiCall;
import me.loki2302.dal.apicalls.UnprogressTaskApiCall;
import me.loki2302.dal.dto.TaskDto;
import me.loki2302.dal.dto.TaskStatus;
import roboguice.inject.InjectResource;
import roboguice.util.Ln;

public class ViewTaskActivity extends RetaskActivity {
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
	}

    @Override
    protected void onResume() {
        super.onResume();

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
        menuInflater.inflate(R.menu.view_task_menu, menu);
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
            unprogressTask();
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
            intent.putExtra("taskId", task.id);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void progressTask() {
        run(new ProgressTaskApiCall(applicationState.getSessionToken(), task.id), onTaskStatusChanged);
    }

    private void unprogressTask() {
        run(new UnprogressTaskApiCall(applicationState.getSessionToken(), task.id), onTaskStatusChanged);
    }

    private final DoneCallback<TaskDto> onTaskStatusChanged = new DoneCallback<TaskDto>() {
        @Override
        public void onDone(TaskDto taskDto) {
            applicationState.getTaskRepository().add(Task.fromTaskDto(taskDto));

            if(!taskDto.taskStatus.equals(TaskStatus.Complete)) {
                Intent intent = new Intent(ViewTaskActivity.this, ViewTaskActivity.class);
                intent.putExtra("taskId", taskDto.taskId);
                startActivity(intent);
            }

            finish();
        }
    };
}