package me.loki2302;

import me.loki2302.dal.dto.TaskDto;
import android.content.Context;
import android.widget.FrameLayout;
import android.widget.TextView;

public class TaskThumbnailView extends FrameLayout {
	private final TextView taskReprTextView;
	private TaskDto model;
	
	public TaskThumbnailView(Context context) {
		super(context);
		inflate(context, R.layout.task_thumbnail_view, this);
		taskReprTextView = (TextView)findViewById(R.id.taskReprTextView);
	}
	
	public void setModel(TaskDto task) {
		model = task;
		
		String repr = task.taskDescription;
		taskReprTextView.setText(repr);
	}
	
	public TaskDto getModel() {
		return model;
	}
}
