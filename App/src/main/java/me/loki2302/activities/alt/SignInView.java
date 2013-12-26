package me.loki2302.activities.alt;

import me.loki2302.R;
import me.loki2302.views.UiUtils;
import roboguice.inject.InjectView;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;

public class SignInView extends FrameLayout {
	@InjectView(R.id.emailEditText)
	private EditText emailEditText;
	
	@InjectView(R.id.passwordEditText)
	private EditText passwordEditText;
	
	@InjectView(R.id.rememberMeCheckBox)
	private CheckBox rememberMeCheckBox;
	
	@InjectView(R.id.signInButton)
	private Button signInButton;
	
	private SignInController signInController;
	
	public SignInView(Context context) {
		super(context);
		inflate(context, R.layout.sign_in_view, this);
		UiUtils.injectViews(this);
		
		signInButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String email = emailEditText.getText().toString();
				String password = passwordEditText.getText().toString();
				boolean rememberMe = rememberMeCheckBox.isChecked();
				signInController.signIn(email, password, rememberMe);					
			}				
		});
	}
	
	public void setController(SignInController signInController) {
		this.signInController = signInController;
	}
}