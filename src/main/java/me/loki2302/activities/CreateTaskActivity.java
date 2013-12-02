package me.loki2302.activities;

import me.loki2302.R;
import me.loki2302.dal.ApplicationState;
import me.loki2302.dal.apicalls.CreateTaskApiCall;
import me.loki2302.dal.dto.TaskDescriptionDto;
import me.loki2302.dal.dto.TaskDto;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.inject.Inject;

@ContentView(R.layout.create_task_view)
public class CreateTaskActivity extends RetaskActivity {
	@Inject
	private ApplicationState applicationState;
	
	@InjectView(R.id.taskDescriptionEditText)
	private EditText taskDescriptionEditText;
	
	@InjectView(R.id.doneButton)
	private Button doneButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {			
		super.onCreate(savedInstanceState);		
		
		doneButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String taskDescription = taskDescriptionEditText.getText().toString();
				TaskDescriptionDto taskDescriptionDto = new TaskDescriptionDto();
				taskDescriptionDto.taskDescription = taskDescription;
				
				run(new CreateTaskApiCall(applicationState.getSessionToken(), taskDescriptionDto)).done(new UiDoneCallback<TaskDto>() {
					@Override
					protected void uiOnDone(TaskDto result) {
						Ln.i("Created task with id %d", result.taskId);
						finish();						
					}										
				}).fail(new DefaultFailCallback());				
			}			
		});
	}
}