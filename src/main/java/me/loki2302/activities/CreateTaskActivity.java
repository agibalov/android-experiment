package me.loki2302.activities;

import me.loki2302.R;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class CreateTaskActivity extends RoboActivity {
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
				Ln.i("TODO: create task with description %s", taskDescription);
				
				// TODO: create task
				// TODO: go to home screen
				
				finish();
			}			
		});
	}
}