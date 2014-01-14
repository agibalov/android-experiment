package me.retask.activities;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import me.retask.R;

public class SignInUi {
    private SignInUiListener listener;
    private EditText emailEditText;
    private EditText passwordEditText;
    private CheckBox rememberMeCheckBox;
    private Button signInButton;

    public SignInUi(View layout) {
        emailEditText = (EditText)layout.findViewById(R.id.emailEditText);
        passwordEditText = (EditText)layout.findViewById(R.id.passwordEditText);
        rememberMeCheckBox = (CheckBox)layout.findViewById(R.id.rememberMeCheckBox);
        signInButton = (Button)layout.findViewById(R.id.signInButton);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener == null) {
                    return;
                }

                listener.onSignInClicked();
            }
        });
    }

    public void setListener(SignInUiListener listener) {
        this.listener = listener;
    }

    public void setEmail(String email) {
        emailEditText.setText(email);
    }

    public String getEmail() {
        return emailEditText.getText().toString();
    }

    public void setPassword(String password) {
        passwordEditText.setText(password);
    }

    public String getPassword() {
        return passwordEditText.getText().toString();
    }

    public boolean isRememberMeChecked() {
        return rememberMeCheckBox.isChecked();
    }

    public void setRememberMeChecked(boolean rememberMe) {
        rememberMeCheckBox.setChecked(rememberMe);
    }

    public static interface SignInUiListener {
        void onSignInClicked();
    }
}
