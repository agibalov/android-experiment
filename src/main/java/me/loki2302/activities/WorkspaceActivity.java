package me.loki2302.activities;

import java.util.List;

import me.loki2302.R;
import me.loki2302.application.Task;
import me.loki2302.dal.ApplicationServiceCallback;
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
		
		applicationService.getTasksByStatus(TaskStatus.NotStarted, new ApplicationServiceCallback<List<Task>>() {
			@Override
			public void onSuccess(final List<Task> result) {
				TabSpec tabSpec = tabHost.newTabSpec("todo");
				
				WorkspaceTabView indicator = new WorkspaceTabView(WorkspaceActivity.this);
				indicator.setTabName("TO DO");
				tabSpec.setIndicator(indicator);
				
				tabSpec.setContent(new TabHost.TabContentFactory() {
					@Override
					public View createTabContent(String tag) {
						SwimlaneView swimlaneView = new SwimlaneView(WorkspaceActivity.this);
						swimlaneView.setModel(result, onTaskThumbnailClickedListener);
						return swimlaneView;
					}
				});
				tabHost.addTab(tabSpec);				
			}

			@Override
			public void onError() {
			}			
		});
		
		applicationService.getTasksByStatus(TaskStatus.InProgress, new ApplicationServiceCallback<List<Task>>() {
			@Override
			public void onSuccess(final List<Task> result) {
				TabSpec tabSpec = tabHost.newTabSpec("inprogress");
				
				WorkspaceTabView indicator = new WorkspaceTabView(WorkspaceActivity.this);
				indicator.setTabName("DOING");
				tabSpec.setIndicator(indicator);
				
				tabSpec.setContent(new TabHost.TabContentFactory() {
					@Override
					public View createTabContent(String tag) {
						SwimlaneView swimlaneView = new SwimlaneView(WorkspaceActivity.this);
						swimlaneView.setModel(result, onTaskThumbnailClickedListener);
						return swimlaneView;
					}
				});
				tabHost.addTab(tabSpec);				
			}

			@Override
			public void onError() {				
			}			
		});
		
		applicationService.getTasksByStatus(TaskStatus.Done, new ApplicationServiceCallback<List<Task>>() {
			@Override
			public void onSuccess(final List<Task> result) {
				TabSpec tabSpec = tabHost.newTabSpec("done");
				
				WorkspaceTabView indicator = new WorkspaceTabView(WorkspaceActivity.this);
				indicator.setTabName("DONE");
				tabSpec.setIndicator(indicator);
				
				tabSpec.setContent(new TabHost.TabContentFactory() {
					@Override
					public View createTabContent(String tag) {
						SwimlaneView swimlaneView = new SwimlaneView(WorkspaceActivity.this);
						swimlaneView.setModel(result, onTaskThumbnailClickedListener);
						return swimlaneView;
					}
				});
				tabHost.addTab(tabSpec);				
			}

			@Override
			public void onError() {
			}			
		});
		
		createTaskButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(WorkspaceActivity.this, CreateTaskActivity.class);
				startActivity(intent);
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
