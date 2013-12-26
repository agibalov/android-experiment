package me.loki2302.activities.alt;

import me.loki2302.activities.RetaskActivity;
import me.loki2302.activities.WorkspaceActivity;
import android.content.Intent;
import android.os.Bundle;

import com.google.inject.Inject;

public class SignInActivity2 extends RetaskActivity implements SignInControllerListener {	
	@Inject
	private SignInController signInController;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SignInView signInView = new SignInView(this);
		signInView.setController(signInController);
		setContentView(signInView);
	}

	@Override
	public void onSignedIn() {
		Intent intent = new Intent(this, WorkspaceActivity.class);
		startActivity(intent);
		finish();
	}
}
