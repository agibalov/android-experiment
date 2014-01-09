package me.loki2302.activities;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import me.loki2302.R;

public class SignUpUi {
    private SignUpUiListener listener;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signUpButton;

    public SignUpUi(View layout) {
        emailEditText = (EditText)layout.findViewById(R.id.emailEditText);
        passwordEditText = (EditText)layout.findViewById(R.id.passwordEditText);
        signUpButton = (Button)layout.findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener == null) {
                    return;
                }

                listener.onSignUpClicked();
            }
        });
    }

    public void setListener(SignUpUiListener listener) {
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

    public static interface SignUpUiListener {
        void onSignUpClicked();
    }
}
