package me.loki2302.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.inject.Inject;

import me.loki2302.R;
import me.loki2302.dal.ApplicationState;
import me.loki2302.dal.apicalls.SignInApiCall;
import me.loki2302.dal.dto.SessionDto;
import roboguice.activity.event.OnActivityResultEvent;
import roboguice.event.Observes;
import roboguice.util.Ln;

public class SignInActivity extends RetaskActivity {
	@Inject
	private PreferencesService preferencesService;
	
	@Inject
	private ConnectivityService connectivityService;
	
	@Inject
	private ApplicationState applicationState;
			
	private EditText emailEditText;
	private EditText passwordEditText;
	private CheckBox rememberMeCheckBox;
	private Button signInButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_view);
        emailEditText = (EditText)findViewById(R.id.emailEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        rememberMeCheckBox = (CheckBox)findViewById(R.id.rememberMeCheckBox);
        signInButton = (Button)findViewById(R.id.signInButton);
		
		Credentials credentials = preferencesService.getCredentials();
		rememberMeCheckBox.setChecked(true);
		if(credentials != null) {
			String email = credentials.getEmail();
			String password = credentials.getPassword();
			
			emailEditText.setText(email);			
			passwordEditText.setText(password);
		}
		
		signInButton.setOnClickListener(onSignInClicked);
		
		// TODO: handle "is connecting"? (android.net.conn.CONNECTIVITY_CHANGE)
		boolean isConnected = connectivityService.isConnected();
		if(!isConnected) {			
			new AlertDialog.Builder(this)
				.setTitle("Network")
				.setMessage("It looks like you're not connected to the Internet at the moment")
				.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						startActivityForResult(new Intent(Settings.ACTION_WIRELESS_SETTINGS), 123);
					}
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();						
					}
				}).show();
		} else {
			if(credentials != null) {
				String email = credentials.getEmail();
				String password = credentials.getPassword();
				signIn(email, password);
			}
		}
	}
	
	@SuppressWarnings("unused")
	private void onActivityResult(@Observes OnActivityResultEvent e) {
		Ln.i("GOT RESULT for request: %d", e.getRequestCode());
		// TODO: handle "is connecting"?
	}
	
	private void signIn(final String email, final String password) {
		run(new SignInApiCall(email, password), new DoneCallback<SessionDto>() {
			@Override
			public void onDone(SessionDto result) {
				Ln.i("Authenticated: %s", result.sessionToken);

				applicationState.setSessionToken(result.sessionToken);

				if(rememberMeCheckBox.isChecked()) {
					Credentials credentials = new Credentials(email, password);
					preferencesService.setCredentials(credentials);
				}

				Intent intent = new Intent(SignInActivity.this, WorkspaceActivity.class);
				startActivity(intent);
				finish();
			}
		});
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
