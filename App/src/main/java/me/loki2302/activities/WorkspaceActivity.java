package me.loki2302.activities;

import java.util.List;

import me.loki2302.R;
import me.loki2302.application.Repository;
import me.loki2302.application.Task;
import me.loki2302.application.TaskStatusIsQuery;
import me.loki2302.dal.ApplicationState;
import me.loki2302.dal.apicalls.GetWorkspaceApiCall;
import me.loki2302.dal.dto.TaskDto;
import me.loki2302.dal.dto.TaskStatus;
import me.loki2302.dal.dto.WorkspaceDto;
import me.loki2302.views.OnTaskThumbnailClickedListener;
import me.loki2302.views.SwimlaneView;
import roboguice.inject.ContentView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.inject.Inject;

@ContentView(R.layout.home_view)
public class WorkspaceActivity extends RetaskActivity {	
	@Inject
	private PreferencesService preferencesService;
		
	@Inject
	private ApplicationState applicationState;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		final SwimlaneView toDoSwimlaneView = new SwimlaneView(this);
		final SwimlaneView inProgressSwimlaneView = new SwimlaneView(this);
		final SwimlaneView doneSwimlaneView = new SwimlaneView(this);
		
		ViewPager viewPager = (ViewPager)findViewById(R.id.viewPager);
		viewPager.setAdapter(new PagerAdapter() {
			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				container.removeView((View)object);
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {			
				View v = null;
				if(position == 0) {
					v = toDoSwimlaneView;
				} else if(position == 1) {
					v = inProgressSwimlaneView;
				} else if(position == 2) {
					v = doneSwimlaneView;
				} else {
					throw new RuntimeException();
				}
				
				container.addView(v);
				
				return v;
			}

			@Override
			public int getCount() {
				return 3;
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0.equals(arg1);
			}			
		});
		
		run(new GetWorkspaceApiCall(applicationState.getSessionToken()), new DoneCallback<WorkspaceDto>() {
			@Override
			public void onDone(WorkspaceDto result) {
				Repository<Task> taskRepository = applicationState.getTaskRepository();
				
				for(TaskDto taskDto : result.tasks) {
					Task task = taskFromTaskDto(taskDto);
					taskRepository.add(task);
				}
								
				List<Task> toDoTasks = taskRepository.getWhere(new TaskStatusIsQuery(TaskStatus.NotStarted));
				toDoSwimlaneView.setModel(toDoTasks, onTaskThumbnailClickedListener);
				
				List<Task> inProgressTasks = taskRepository.getWhere(new TaskStatusIsQuery(TaskStatus.InProgress));
				inProgressSwimlaneView.setModel(inProgressTasks, onTaskThumbnailClickedListener);				
				
				List<Task> doneTasks = taskRepository.getWhere(new TaskStatusIsQuery(TaskStatus.Done));
				doneSwimlaneView.setModel(doneTasks, onTaskThumbnailClickedListener);				
			}
		});		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.workspace_menu, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int itemId = item.getItemId();
		if(itemId == R.id.createTaskMenuItem) {
			Intent intent = new Intent(WorkspaceActivity.this, CreateTaskActivity.class);
			startActivity(intent);
			return true;
		} else if(itemId == R.id.resetMenuItem) {
			preferencesService.unsetCredentials();
			finish();
		}
		
		return super.onMenuItemSelected(featureId, item);
	}
	
	private OnTaskThumbnailClickedListener onTaskThumbnailClickedListener = new OnTaskThumbnailClickedListener() {
		@Override
		public void onTaskThumbnailClicked(Task model) {
			Intent intent = new Intent(WorkspaceActivity.this, TaskActivity.class);
			intent.putExtra("taskId", model.id);
			startActivity(intent);
		}		
	};
	
	private static Task taskFromTaskDto(TaskDto taskDto) {
		Task task = new Task();
		task.id = taskDto.taskId;
		task.description = taskDto.taskDescription;
		task.status = taskDto.taskStatus;
		return task;
	}
}
