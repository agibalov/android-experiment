package me.loki2302.activities;

import me.loki2302.R;
import me.loki2302.application.Task;
import me.loki2302.dal.ApplicationServiceCallback;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.google.inject.Inject;
import com.petebevin.markdown.MarkdownProcessor;

public class TaskActivity extends RoboActivity {
	private final static MarkdownProcessor markdownProcessor = new MarkdownProcessor();
	
	@Inject
	private ContextApplicationService applicationService;
			
	@InjectView(R.id.taskDescriptionWebView)
	private WebView taskDescriptionWebView;
	
	@InjectResource(R.string.markdown_html_template)
	private String markdownHtmlTemplate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {			
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_view);		
		
		Intent intent = getIntent();
		int taskId = intent.getIntExtra("taskId", -1);
		if(taskId == -1) {
			throw new IllegalStateException("Looks like taskId is missing in Intent");
		}
		
		applicationService.getTask(taskId, new ApplicationServiceCallback<Task>() {
			@Override
			public void onSuccess(Task result) {
				String taskDescription = result.description;
				String taskDescriptionHtml = markdownProcessor.markdown(taskDescription);
				String html = String.format(markdownHtmlTemplate, taskDescriptionHtml);
				taskDescriptionWebView.loadData(html, "text/html", null);				
			}

			@Override
			public void onError() {
				Ln.i("Error ocurred while retrieving task");				
			}			
		});	
	}		
}