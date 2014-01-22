package me.retask.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.inject.Inject;

import me.retask.R;
import me.retask.service.ApplicationState;
import me.retask.service.RetaskService;
import me.retask.service.RetaskServiceRequestListener;
import me.retask.service.requests.SignInRequest;
import roboguice.fragment.RoboFragment;
import roboguice.util.Ln;

public class SignInFragment extends RoboFragment implements View.OnClickListener, RetaskServiceRequestListener<String> {
    @Inject
    private RetaskService retaskService;

    @Inject
    private ApplicationState applicationState;

    private EditText emailEditText;
    private EditText passwordEditText;
    private CheckBox rememberMeCheckBox;
    private Button signInButton;
    private SignInListener signInListener;
    private String pendingRequestToken;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(!(activity instanceof SignInListener)) {
            throw new ClassCastException("Hosting activity supposed to implement SignInListener");
        }

        signInListener = (SignInListener)activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_in_view, container, false);
        emailEditText = (EditText)view.findViewById(R.id.emailEditText);
        passwordEditText = (EditText)view.findViewById(R.id.passwordEditText);
        rememberMeCheckBox = (CheckBox)view.findViewById(R.id.rememberMeCheckBox);
        signInButton = (Button)view.findViewById(R.id.signInButton);
        signInButton.setOnClickListener(this);

        if(applicationState.getRememberMe()) {
            emailEditText.setText(applicationState.getEmail());
            passwordEditText.setText(applicationState.getPassword());
            rememberMeCheckBox.setChecked(true);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(pendingRequestToken != null) {
            retaskService.setRequestListener(pendingRequestToken, this);
        }

        if(applicationState.getRememberMe()) {
            signIn();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if(pendingRequestToken != null) {
            retaskService.setRequestListener(pendingRequestToken, null);
        }
    }

    @Override
    public void onClick(View view) {
        signIn();
    }

    @Override
    public void onSuccess(String requestToken, String result) {
        pendingRequestToken = null;
        Ln.i("*** SIGN IN FRAGMENT: success for %s, result is %s", requestToken, result);

        signInListener.onSignedIn();
    }

    @Override
    public void onError(String requestToken, RuntimeException exception) {
        pendingRequestToken = null;
        Ln.i("*** SIGN IN FRAGMENT: error for %s, error is %s", requestToken, exception);
    }

    private void signIn() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        boolean rememberMe = rememberMeCheckBox.isChecked();
        pendingRequestToken = retaskService.submit(new SignInRequest(email, password, rememberMe));
        retaskService.setRequestListener(pendingRequestToken, this);
    }

    public static interface SignInListener {
        void onSignedIn();
    }
}
