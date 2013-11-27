package me.loki2302.activities;

import java.util.List;

import roboguice.inject.ContentView;

import me.loki2302.R;
import me.loki2302.application.Task;
import me.loki2302.dal.dto.TaskStatus;
import me.loki2302.views.OnTaskThumbnailClickedListener;
import me.loki2302.views.SwimlaneView;
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

@ContentView(R.layout.home_view_alt)
public class WorkspaceActivityAlt extends RetaskActivity {
	@Inject
	private ContextApplicationService applicationService;
	
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
		
		applicationService.getTasksByStatus(TaskStatus.NotStarted).done(new UiDoneCallback<List<Task>>() {
			@Override
			protected void uiOnDone(final List<Task> result) {
				toDoSwimlaneView.setModel(result, onTaskThumbnailClickedListener);
			}			
		}).fail(new DefaultFailCallback());
		
		applicationService.getTasksByStatus(TaskStatus.InProgress).done(new UiDoneCallback<List<Task>>() {
			@Override
			protected void uiOnDone(final List<Task> result) {
				inProgressSwimlaneView.setModel(result, onTaskThumbnailClickedListener);
			}			
		}).fail(new DefaultFailCallback());
		
		applicationService.getTasksByStatus(TaskStatus.Done).done(new UiDoneCallback<List<Task>>() {
			@Override
			protected void uiOnDone(final List<Task> result) {
				doneSwimlaneView.setModel(result, onTaskThumbnailClickedListener);
			}			
		}).fail(new DefaultFailCallback());		
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
			Intent intent = new Intent(WorkspaceActivityAlt.this, CreateTaskActivity.class);
			startActivity(intent);
			return true;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}
	
	private OnTaskThumbnailClickedListener onTaskThumbnailClickedListener = new OnTaskThumbnailClickedListener() {
		@Override
		public void onTaskThumbnailClicked(Task model) {
			Intent intent = new Intent(WorkspaceActivityAlt.this, TaskActivity.class);
			intent.putExtra("taskId", model.id);
			startActivity(intent);
		}		
	};
}
