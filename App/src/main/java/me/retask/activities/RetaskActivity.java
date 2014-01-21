package me.retask.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.google.inject.Inject;

import me.retask.service.RequestExecutor;
import me.retask.service.requests.RetaskServiceRequest;

public abstract class RetaskActivity extends RoboActionBarActivity implements ProgressDialogSupport {
    @Inject
    protected RequestExecutor requestExecutor;

    protected String run(RetaskServiceRequest retaskServiceRequest) {
        return requestExecutor.run(this, retaskServiceRequest);
    }

    @Override
    public void showProgressDialog() {
        ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment();
        progressDialogFragment.show(getSupportFragmentManager(), "progressDialogFragment");
    }

    @Override
    public void hideProgressDialog() {
        ProgressDialogFragment progressDialogFragment = (ProgressDialogFragment)getSupportFragmentManager().findFragmentByTag("progressDialogFragment");
        progressDialogFragment.dismiss();
    }

    public static class ProgressDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Working...");
            return progressDialog;
        }
    }

}
