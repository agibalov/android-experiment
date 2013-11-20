package me.loki2302.activities;

import me.loki2302.R;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.inject.Inject;

public class SignInActivity extends RetaskActivity {
	@Inject
	private ContextApplicationService applicationService;
			
	@InjectView(R.id.emailEditText)
	private EditText emailEditText;
	
	@InjectView(R.id.passwordEditText)
	private EditText passwordEditText;
	
	@InjectView(R.id.signInButton)
	private Button signInButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_in_view);
		
		emailEditText.setText("");
		passwordEditText.setText("");
		
		signInButton.setOnClickListener(onSignInClicked);
	}
	
	private final OnClickListener onSignInClicked = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			final String email = emailEditText.getText().toString();
			final String password = passwordEditText.getText().toString();
			
			applicationService.signIn(email, password).done(new UiDoneCallback<String>() {
				@Override
				protected void uiOnDone(String result) {
					Ln.i("Authenticated: %s", result);
					
					Intent intent = new Intent(SignInActivity.this, WorkspaceActivity.class);
					startActivity(intent);
					finish();
				}				
			});
		}			
	};
}
