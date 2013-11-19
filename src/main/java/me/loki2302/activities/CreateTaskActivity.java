package me.loki2302.activities;

import me.loki2302.R;
import me.loki2302.application.Task;
import me.loki2302.dal.ApplicationServiceCallback;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.inject.Inject;

public class CreateTaskActivity extends RoboActivity {
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
				applicationService.createTask(taskDescription, new ApplicationServiceCallback<Task>() {
					@Override
					public void onSuccess(Task result) {
						Ln.i("Created task with id %d", result.id);
						finish();
						
						// TODO: workspace activity should now reload the data
					}

					@Override
					public void onError() {
						// TODO
					}					
				});
			}			
		});
	}
}