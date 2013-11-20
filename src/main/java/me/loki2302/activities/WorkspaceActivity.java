package me.loki2302.activities;

import java.util.List;
import me.loki2302.R;
import me.loki2302.application.Task;
import me.loki2302.dal.dto.TaskStatus;
import me.loki2302.views.OnTaskThumbnailClickedListener;
import me.loki2302.views.SwimlaneView;
import me.loki2302.views.WorkspaceTabView;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabHost.TabContentFactory;

import com.google.inject.Inject;

public class WorkspaceActivity extends RoboActivity {
	@Inject
	private ContextApplicationService applicationService;
	
	@InjectView(R.id.createTaskButton)
	private Button createTaskButton;
		
	@InjectView(R.id.tabHost)
	private TabHost tabHost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_view);
		tabHost.setup();
		
		TabSpec toDoTabSpec = tabHost.newTabSpec("todo");		
		WorkspaceTabView toDoTabView = new WorkspaceTabView(this);
		final SwimlaneView toDoSwimlaneView = new SwimlaneView(this);
		toDoTabView.setTabName("TO DO");
		toDoTabSpec.setIndicator(toDoTabView);
		toDoTabSpec.setContent(new TabContentFactory() {
			@Override
			public View createTabContent(String tag) {		
				return toDoSwimlaneView;
			}
		});		
		tabHost.addTab(toDoTabSpec);
		
		TabSpec inProgressTabSpec = tabHost.newTabSpec("inprogress");				
		WorkspaceTabView inProgressTabView = new WorkspaceTabView(this);
		final SwimlaneView inProgressSwimlaneView = new SwimlaneView(this);
		inProgressTabView.setTabName("DOING");
		inProgressTabSpec.setIndicator(inProgressTabView);
		inProgressTabSpec.setContent(new TabContentFactory() {
			@Override
			public View createTabContent(String tag) {		
				return inProgressSwimlaneView;
			}
		});
		tabHost.addTab(inProgressTabSpec);
		
		TabSpec doneTabSpec = tabHost.newTabSpec("done");				
		WorkspaceTabView doneTabView = new WorkspaceTabView(this);
		final SwimlaneView doneSwimlaneView = new SwimlaneView(this);
		doneTabView.setTabName("DONE");
		doneTabSpec.setIndicator(doneTabView);
		doneTabSpec.setContent(new TabContentFactory() {
			@Override
			public View createTabContent(String tag) {		
				return doneSwimlaneView;
			}
		});
		tabHost.addTab(doneTabSpec);
		
		createTaskButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(WorkspaceActivity.this, CreateTaskActivity.class);
				startActivity(intent);
			}
		});
		
		applicationService.getTasksByStatus(TaskStatus.NotStarted).done(new UiDoneCallback<List<Task>>(this) {
			@Override
			protected void uiOnDone(final List<Task> result) {
				toDoSwimlaneView.setModel(result, onTaskThumbnailClickedListener);
			}			
		});
		
		applicationService.getTasksByStatus(TaskStatus.InProgress).done(new UiDoneCallback<List<Task>>(this) {
			@Override
			protected void uiOnDone(final List<Task> result) {
				inProgressSwimlaneView.setModel(result, onTaskThumbnailClickedListener);
			}			
		});
		
		applicationService.getTasksByStatus(TaskStatus.Done).done(new UiDoneCallback<List<Task>>(this) {
			@Override
			protected void uiOnDone(final List<Task> result) {
				doneSwimlaneView.setModel(result, onTaskThumbnailClickedListener);
			}			
		});		
	}
	
	private OnTaskThumbnailClickedListener onTaskThumbnailClickedListener = new OnTaskThumbnailClickedListener() {
		@Override
		public void onTaskThumbnailClicked(Task model) {
			Intent intent = new Intent(WorkspaceActivity.this, TaskActivity.class);
			intent.putExtra("taskId", model.id);
			startActivity(intent);
		}		
	};
}
