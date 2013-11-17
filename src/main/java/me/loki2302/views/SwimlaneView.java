package me.loki2302.views;

import java.util.List;

import me.loki2302.R;
import me.loki2302.dal.dto.TaskDto;
import roboguice.util.Ln;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;

public class SwimlaneView extends FrameLayout {
	private final ListView taskThumbnailsListView;
	private OnTaskThumbnailClickedListener onTaskThumbnailClickedListener;
	
	public SwimlaneView(Context context) {
		super(context);
		inflate(context, R.layout.swimlane_view, this);
		taskThumbnailsListView = (ListView)findViewById(R.id.taskThumbailsListView);
		
		taskThumbnailsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TaskDto taskDto = ((TaskThumbnailView)view).getModel();
				Ln.i("item clicked - %d", taskDto.taskId);
				if(onTaskThumbnailClickedListener != null) {
					onTaskThumbnailClickedListener.onTaskThumbnailClicked(taskDto);
				}
			}			
		});
	}
	
	public void setModel(List<TaskDto> tasks, OnTaskThumbnailClickedListener onTaskThumbnailClickedListener) {
		this.onTaskThumbnailClickedListener = onTaskThumbnailClickedListener;
		taskThumbnailsListView.setAdapter(new ModelListAdapter<TaskDto>(tasks, new ViewFactory<TaskDto>() {
			@Override
			public View makeView(TaskDto model) {
				TaskThumbnailView taskThumbnailView = new TaskThumbnailView(getContext());
				taskThumbnailView.setModel(model);
				return taskThumbnailView;
			}			
		}));	
	}
}