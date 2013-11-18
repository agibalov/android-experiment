package me.loki2302.views;

import java.util.List;

import me.loki2302.R;
import me.loki2302.application.Task;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;

public class SwimlaneView extends FrameLayout {
	@InjectView(R.id.taskThumbailsListView)
	private ListView taskThumbnailsListView;
	
	private OnTaskThumbnailClickedListener onTaskThumbnailClickedListener;
	
	public SwimlaneView(Context context) {
		super(context);
		inflate(context, R.layout.swimlane_view, this);
		UiUtils.injectViews(this);
		
		taskThumbnailsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Task task = ((TaskThumbnailView)view).getModel();
				Ln.i("item clicked - %d", task.id);
				if(onTaskThumbnailClickedListener != null) {
					onTaskThumbnailClickedListener.onTaskThumbnailClicked(task);
				}
			}			
		});
	}
	
	public void setModel(List<Task> tasks, OnTaskThumbnailClickedListener onTaskThumbnailClickedListener) {
		this.onTaskThumbnailClickedListener = onTaskThumbnailClickedListener;
		taskThumbnailsListView.setAdapter(new ModelListAdapter<Task>(tasks, new ViewFactory<Task>() {
			@Override
			public View makeView(Task model) {
				TaskThumbnailView taskThumbnailView = new TaskThumbnailView(getContext());
				taskThumbnailView.setModel(model);
				return taskThumbnailView;
			}			
		}));	
	}
}