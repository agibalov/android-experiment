package me.loki2302.activities;

import java.util.List;

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

public class WorkspaceActivityAlt extends RetaskActivity {
	@Inject
	private ContextApplicationService applicationService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_view_alt);
		
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
				/*LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				FrameLayout pageView = new FrameLayout(WorkspaceActivityAlt.this);
				inflater.inflate(R.layout.page_view, pageView);
								
				TextView pageTitleTextView = (TextView)pageView.findViewById(R.id.pageTitle);
				String title = String.format("Page #%d", position);
				pageTitleTextView.setText(title);			
				
				TextView pageContentTextView = (TextView)pageView.findViewById(R.id.pageContent);
				String content = String.format("Content #%d", position);
				pageContentTextView.setText(content);
				
				container.addView(pageView);
				
				return pageView; // TODO: this shouldn't necessarily be a view*/
				
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
				
		/*TabSpec toDoTabSpec = tabHost.newTabSpec("todo");		
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
				Intent intent = new Intent(WorkspaceActivityAlt.this, CreateTaskActivity.class);
				startActivity(intent);
			}
		});*/
		
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
