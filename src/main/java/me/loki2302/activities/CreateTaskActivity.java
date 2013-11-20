package me.loki2302.activities;

import me.loki2302.R;
import me.loki2302.application.Task;

import org.jdeferred.DoneCallback;

import roboguice.inject.InjectView;
import roboguice.util.Ln;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.inject.Inject;

public class CreateTaskActivity extends RetaskActivity {
	@Inject
	private ContextApplicationService applicationService;
	
	@InjectView(R.id.taskDescriptionEditText)
	private EditText taskDescriptionEditText;
	
	@InjectView(R.id.doneButton)
	private Button doneButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {			
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_task_view);
		
		doneButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String taskDescription = taskDescriptionEditText.getText().toString();				
				applicationService.createTask(taskDescription).done(new DoneCallback<Task>() {
					@Override
					public void onDone(Task result) {
						Ln.i("Created task with id %d", result.id);
						finish();						
					}					
				}).fail(new DefaultFailCallback());;				
			}			
		});
	}
}