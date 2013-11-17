package me.loki2302.activities;

import java.util.ArrayList;
import java.util.List;

import me.loki2302.ApplicationState;
import me.loki2302.R;
import me.loki2302.dal.ApiCallback;
import me.loki2302.dal.RetaskService;
import me.loki2302.dal.dto.TaskDto;
import me.loki2302.dal.dto.TaskStatus;
import me.loki2302.dal.dto.WorkspaceDto;
import me.loki2302.views.OnTaskThumbnailClickedListener;
import me.loki2302.views.SwimlaneView;
import me.loki2302.views.WorkspaceTabView;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import com.google.inject.Inject;

public class WorkspaceActivity extends RoboActivity {
	@Inject
	private RetaskService retaskService;
	
	@Inject
	private ApplicationState applicationState;
	
	@InjectView(R.id.tabHost)
	private TabHost tabHost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_view);
		tabHost.setup();
				
		retaskService.getWorkspace(applicationState.getSessionToken(), new ApiCallback<WorkspaceDto>() {
			@Override
			public void onSuccess(WorkspaceDto result) {
				if(true) {
					final List<TaskDto> tasks = getTasksInStatus(result.tasks, TaskStatus.NotStarted);
					TabSpec tabSpec = tabHost.newTabSpec("todo");
					
					WorkspaceTabView indicator = new WorkspaceTabView(WorkspaceActivity.this);
					indicator.setTabName("TO DO");
					tabSpec.setIndicator(indicator);
					
					tabSpec.setContent(new TabHost.TabContentFactory() {
						@Override
						public View createTabContent(String tag) {
							SwimlaneView swimlaneView = new SwimlaneView(WorkspaceActivity.this);
							swimlaneView.setModel(tasks, onTaskThumbnailClickedListener);
							return swimlaneView;
						}
					});
					tabHost.addTab(tabSpec);
				}
				
				if(true) {
					final List<TaskDto> tasks = getTasksInStatus(result.tasks, TaskStatus.InProgress);
					TabSpec tabSpec = tabHost.newTabSpec("inprogress");
					
					WorkspaceTabView indicator = new WorkspaceTabView(WorkspaceActivity.this);
					indicator.setTabName("DOING");
					tabSpec.setIndicator(indicator);
					
					tabSpec.setContent(new TabHost.TabContentFactory() {
						@Override
						public View createTabContent(String tag) {
							SwimlaneView swimlaneView = new SwimlaneView(WorkspaceActivity.this);
							swimlaneView.setModel(tasks, onTaskThumbnailClickedListener);
							return swimlaneView;
						}
					});
					tabHost.addTab(tabSpec);
				}
				
				if(true) {
					final List<TaskDto> tasks = getTasksInStatus(result.tasks, TaskStatus.Done);
					TabSpec tabSpec = tabHost.newTabSpec("done");
					
					WorkspaceTabView indicator = new WorkspaceTabView(WorkspaceActivity.this);
					indicator.setTabName("DONE");
					tabSpec.setIndicator(indicator);
					
					tabSpec.setContent(new TabHost.TabContentFactory() {
						@Override
						public View createTabContent(String tag) {
							SwimlaneView swimlaneView = new SwimlaneView(WorkspaceActivity.this);
							swimlaneView.setModel(tasks, onTaskThumbnailClickedListener);
							return swimlaneView;
						}
					});
					tabHost.addTab(tabSpec);
				}
			}

			@Override
			public void onError() {
				Ln.i("Failed to load workspace");				
			}
		});
	}
	
	private static List<TaskDto> getTasksInStatus(List<TaskDto> tasks, TaskStatus taskStatus) {
		List<TaskDto> filteredTasks = new ArrayList<TaskDto>();
		for(TaskDto task : tasks) {
			if(!task.taskStatus.equals(taskStatus)) {
				continue;
			}
			
			filteredTasks.add(task);
		}
		
		return filteredTasks;
	}
	
	private OnTaskThumbnailClickedListener onTaskThumbnailClickedListener = new OnTaskThumbnailClickedListener() {
		@Override
		public void onTaskThumbnailClicked(TaskDto model) {
			Intent intent = new Intent(WorkspaceActivity.this, TaskActivity.class);
			intent.putExtra("taskDescription", model.taskDescription);
			startActivity(intent);
		}		
	};
}