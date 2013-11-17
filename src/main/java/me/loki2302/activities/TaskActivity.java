package me.loki2302.activities;

import me.loki2302.R;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.petebevin.markdown.MarkdownProcessor;

public class TaskActivity extends RoboActivity {
	private final static MarkdownProcessor markdownProcessor = new MarkdownProcessor();
		
	@InjectView(R.id.taskDescriptionWebView)
	private WebView taskDescriptionWebView;
	
	@InjectResource(R.string.markdown_html_template)
	private String markdownHtmlTemplate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {			
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_view);		
		
		Intent intent = getIntent();
		String taskDescription = intent.getStringExtra("taskDescription");						
		String taskDescriptionHtml = markdownProcessor.markdown(taskDescription);
		String html = String.format(markdownHtmlTemplate, taskDescriptionHtml);				
		taskDescriptionWebView.loadData(html, "text/html", null);
	}		
}