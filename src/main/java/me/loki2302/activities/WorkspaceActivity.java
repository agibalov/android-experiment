package me.loki2302.activities;

import java.util.List;

import me.loki2302.R;
import me.loki2302.application.Task;
import me.loki2302.dal.ApplicationService;
import me.loki2302.dal.dto.TaskStatus;
import me.loki2302.views.OnTaskThumbnailClickedListener;
import me.loki2302.views.SwimlaneView;
import me.loki2302.views.WorkspaceTabView;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.google.inject.Inject;

public class WorkspaceActivity extends RoboActivity {
	@Inject
	private ApplicationService applicationService;
	
	@Inject
	private ProgressDialogLongOperationListener progressDialogLongOperationListener;
	
	@InjectView(R.id.tabHost)
	private TabHost tabHost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_view);
		tabHost.setup();
		
		applicationService.getTasksByStatus(progressDialogLongOperationListener, TaskStatus.NotStarted, new RunOnUiThreadApplicationServiceCallback<List<Task>> (this) {
			@Override
			protected void onSuccessOnUiThread(final List<Task> result) {
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
			protected void onErrorOnUiThread() {				
			}			
		});
		
		applicationService.getTasksByStatus(progressDialogLongOperationListener, TaskStatus.InProgress, new RunOnUiThreadApplicationServiceCallback<List<Task>>(this) {
			@Override
			protected void onSuccessOnUiThread(final List<Task> result) {
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
			protected void onErrorOnUiThread() {				
			}			
		});
		
		applicationService.getTasksByStatus(progressDialogLongOperationListener, TaskStatus.Done, new RunOnUiThreadApplicationServiceCallback<List<Task>>(this) {
			@Override
			protected void onSuccessOnUiThread(final List<Task> result) {
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
			protected void onErrorOnUiThread() {
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