package me.retask.views;

import me.retask.R;
import me.retask.application.Task;
import roboguice.inject.InjectView;
import android.content.Context;
import android.widget.FrameLayout;
import android.widget.TextView;

public class TaskThumbnailView extends FrameLayout {
	@InjectView(R.id.taskReprTextView)
	private TextView taskReprTextView;
	
	private Task model;
	
	public TaskThumbnailView(Context context) {
		super(context);
		inflate(context, R.layout.task_thumbnail_view, this);
		UiUtils.injectViews(this);
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
