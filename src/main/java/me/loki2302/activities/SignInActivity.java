package me.loki2302.activities;

import me.loki2302.R;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.inject.Inject;

@ContentView(R.layout.sign_in_view)
public class SignInActivity extends RetaskActivity {
	@Inject
	private ContextApplicationService applicationService;
	
	@Inject
	private PreferencesService preferencesService;
			
	@InjectView(R.id.emailEditText)
	private EditText emailEditText;
	
	@InjectView(R.id.passwordEditText)
	private EditText passwordEditText;
	
	@InjectView(R.id.rememberMeCheckBox)
	private CheckBox rememberMeCheckBox;
	
	@InjectView(R.id.signInButton)
	private Button signInButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Credentials credentials = preferencesService.getCredentials();
		if(credentials != null) {
			String email = credentials.getEmail();
			String password = credentials.getPassword();
			
			emailEditText.setText(email);			
			passwordEditText.setText(password);			
			
			signIn(email, password);
		}
		
		rememberMeCheckBox.setChecked(true);
		
		signInButton.setOnClickListener(onSignInClicked);
	}
	
	private void signIn(final String email, final String password) {
		applicationService.signIn(email, password).done(new UiDoneCallback<String>() {
			@Override
			protected void uiOnDone(String result) {
				Ln.i("Authenticated: %s", result);
				if(rememberMeCheckBox.isChecked()) {
					Credentials credentials = new Credentials(email, password);
					preferencesService.setCredentials(credentials);
				}
				
				Intent intent = new Intent(SignInActivity.this, WorkspaceActivityAlt.class);
				startActivity(intent);
				finish();
			}				
		}).fail(new DefaultFailCallback());
	}
	
	private final OnClickListener onSignInClicked = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			String email = emailEditText.getText().toString();
			String password = passwordEditText.getText().toString();
			signIn(email, password);
		}			
	};	
}
