package me.retask.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import me.retask.R;
import roboguice.inject.InjectResource;

public class SignedUpActivity extends RetaskActivity {
    @InjectResource(R.string.activation_link_sent_message)
    private String activationLinkSentMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signed_up_view);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        if(email == null || email.equals("")) {
            throw new RuntimeException();
        }

        TextView messageTextView = (TextView)findViewById(R.id.messageTextView);
        messageTextView.setText(String.format(activationLinkSentMessage, email));

        Button proceedToSignInButton = (Button)findViewById(R.id.proceedToSignInButton);
        proceedToSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignedUpActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
