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
import me.retask.service.RequestExecutor;
import me.retask.service.requests.SignInRetaskServiceRequest;
import roboguice.fragment.RoboFragment;

public class SignInFragment extends RoboFragment implements View.OnClickListener, RequestExecutor.RequestListener {
    @Inject
    private RequestExecutor requestExecutor;

    private EditText emailEditText;
    private EditText passwordEditText;
    private CheckBox rememberMeCheckBox;
    private Button signInButton;
    private SignInListener signInListener;
    private ProgressDialogSupport progressDialogSupport;
    private String pendingRequestToken;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(!(activity instanceof SignInListener)) {
            throw new ClassCastException("Hosting activity supposed to implement SignInListener");
        }

        if(!(activity instanceof ProgressDialogSupport)) {
            throw new ClassCastException("Hosting activity supposed to implement ProgressDialogSupport");
        }

        signInListener = (SignInListener)activity;
        progressDialogSupport = (ProgressDialogSupport)activity;
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
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(pendingRequestToken != null) {
            requestExecutor.setRequestListener(pendingRequestToken, this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if(pendingRequestToken != null) {
            requestExecutor.setRequestListener(pendingRequestToken, null);
        }
    }

    @Override
    public void onClick(View view) {
        progressDialogSupport.showProgressDialog();

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        pendingRequestToken = requestExecutor.run(getActivity(), new SignInRetaskServiceRequest(email, password));
        requestExecutor.setRequestListener(pendingRequestToken, this);
    }

    @Override
    public boolean onRequestComplete(String requestToken) {
        progressDialogSupport.hideProgressDialog();
        pendingRequestToken = null;
        signInListener.onSignedIn();
        return true;
    }

    public static interface SignInListener {
        void onSignedIn();
    }
}
