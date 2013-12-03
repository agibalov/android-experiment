package me.loki2302.activities.alt;

import me.loki2302.activities.Credentials;
import me.loki2302.activities.PreferencesService;
import me.loki2302.activities.RetaskActivity;
import me.loki2302.activities.RetaskActivity.DoneCallback;
import me.loki2302.dal.ApplicationState;
import me.loki2302.dal.apicalls.SignInApiCall;
import me.loki2302.dal.dto.SessionDto;
import roboguice.inject.ContextSingleton;
import roboguice.util.Ln;
import android.app.Activity;

import com.google.inject.Inject;

@ContextSingleton
public class SignInController {
	@Inject
	private Activity activity;
	
	@Inject
	// private SignInControllerListener listener;
	private Activity listener;
	
	@Inject
	private ApplicationState applicationState;		
	
	@Inject
	private PreferencesService preferencesService;
	
	public void signIn(final String email, final String password, final boolean rememberMe) {
		((RetaskActivity)activity).run(new SignInApiCall(email, password), new DoneCallback<SessionDto>() {
			@Override
			public void onDone(SessionDto result) {
				Ln.i("Authenticated: %s", result.sessionToken);
				
				applicationState.setSessionToken(result.sessionToken);				
				
				if(rememberMe) {
					Credentials credentials = new Credentials(email, password);
					preferencesService.setCredentials(credentials);
				}
				
				((SignInActivity2)activity).onSignedIn();				
			}
		});
	}
}