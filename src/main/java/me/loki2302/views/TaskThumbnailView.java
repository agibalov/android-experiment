package me.loki2302.views;

import me.loki2302.R;
import me.loki2302.application.Task;
import android.content.Context;
import android.widget.FrameLayout;
import android.widget.TextView;

public class TaskThumbnailView extends FrameLayout {
	private final TextView taskReprTextView;
	private Task model;
	
	public TaskThumbnailView(Context context) {
		super(context);
		inflate(context, R.layout.task_thumbnail_view, this);
		taskReprTextView = (TextView)findViewById(R.id.taskReprTextView);
	}
	
	public void setModel(Task task) {
		model = task;
		
		String repr = task.description;
		taskReprTextView.setText(repr);
	}
	
	public Task getModel() {
		return model;
	}
}
