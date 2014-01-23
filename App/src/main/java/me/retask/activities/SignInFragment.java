package me.retask.activities;

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
import me.retask.service.requests.SignInRequest;
import roboguice.fragment.RoboFragment;

public class SignInFragment extends RoboFragment implements View.OnClickListener {
    @Inject
    private RetaskService retaskService;

    @Inject
    private ApplicationState applicationState;

    private EditText emailEditText;
    private EditText passwordEditText;
    private CheckBox rememberMeCheckBox;
    private Button signInButton;

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

        if(applicationState.getRememberMe()) {
            signIn();
        }
    }

    @Override
    public void onClick(View view) {
        signIn();
    }

    private void signIn() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        boolean rememberMe = rememberMeCheckBox.isChecked();
        retaskService.submit(new SignInRequest(email, password, rememberMe));
    }
}
