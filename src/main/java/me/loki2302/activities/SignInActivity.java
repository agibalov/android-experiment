package me.loki2302.activities;

import java.util.List;

import me.loki2302.R;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;
import net.sf.oval.context.FieldContext;
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
		
		SignInModel signInModel = new SignInModel();
		Validator validator = new Validator();
		List<ConstraintViolation> violations = validator.validate(signInModel);
		Ln.i("violations: %s", violations);
		for(ConstraintViolation violation : violations) {
			Ln.i("violation: %s", ((FieldContext)violation.getContext()).getField().getName());
			Ln.i(violation.getMessage());
			Ln.i(violation.getMessageTemplate());
		}
	}
	
	private final OnClickListener onSignInClicked = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			String email = emailEditText.getText().toString();
			String password = passwordEditText.getText().toString();
			
			applicationService.signIn(email, password).done(new UiDoneCallback<String>() {
				@Override
				protected void uiOnDone(String result) {
					Ln.i("Authenticated: %s", result);
					
					Intent intent = new Intent(SignInActivity.this, WorkspaceActivityAlt.class);
					startActivity(intent);
					finish();
				}				
			}).fail(new DefaultFailCallback());
		}			
	};	
	
	public static class SignInModel {
		@NotNull
		@NotEmpty		
		private String username;
		
		@NotNull
		@NotEmpty
		private String password;
		
		public String getUsername() {
			return username;
		}
		
		public void setUsername(String username) {
			this.username = username;
		}
		
		public String getPassword() {
			return password;
		}
		
		public void setPassword(String password) {
			this.password = password;
		}
	}
}
