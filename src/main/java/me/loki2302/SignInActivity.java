package me.loki2302;

import me.loki2302.dal.ApiCallback;
import me.loki2302.dal.RetaskService;
import me.loki2302.dal.dto.SessionDto;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.inject.Inject;

public class SignInActivity extends RoboActivity {
	@Inject
	private RetaskService retaskService;
	
	@Inject
	private ApplicationState applicationState;
	
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
				
		signInButton.setOnClickListener(onSignInClicked);	
	}
	
	private final OnClickListener onSignInClicked = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			final String email = emailEditText.getText().toString();
			final String password = passwordEditText.getText().toString();
			retaskService.signIn(email, password, new ApiCallback<SessionDto>() {
				@Override
				public void onSuccess(SessionDto result) {
					Ln.i("Authenticated: %s", result.sessionToken);
					
					applicationState.setSessionToken(result.sessionToken);
					
					Intent intent = new Intent(SignInActivity.this, WorkspaceActivity.class);
					startActivity(intent);
					finish();
				}

				@Override
				public void onError() {
					Ln.i("Failed to authenticate");
				}				
			});			
		}			
	};
}
