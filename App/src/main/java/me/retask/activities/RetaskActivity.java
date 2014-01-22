package me.retask.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.google.inject.Inject;

import me.retask.R;
import me.retask.service.RetaskService;
import me.retask.service.requests.ServiceRequest;

public abstract class RetaskActivity extends RoboActionBarActivity implements RetaskService.ProgressListener {
    @Inject
    private RetaskService retaskService;

    protected <T> String run(ServiceRequest<T> serviceRequest) {
        return retaskService.submit(serviceRequest);
    }

    @Override
    public void onShouldDisplayProgress() {
        ProgressDialogFragment progressDialogFragment = (ProgressDialogFragment)getSupportFragmentManager().findFragmentByTag("progressDialogFragment");
        if(progressDialogFragment == null) {
            progressDialogFragment = new ProgressDialogFragment();
        }

        if(!progressDialogFragment.isVisible()) {
            progressDialogFragment.show(getSupportFragmentManager(), "progressDialogFragment");
        }
    }

    @Override
    public void onShouldNotDisplayProgress() {
        ProgressDialogFragment progressDialogFragment = (ProgressDialogFragment)getSupportFragmentManager().findFragmentByTag("progressDialogFragment");
        if(progressDialogFragment != null) {
            progressDialogFragment.dismiss();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        retaskService.setProgressListener(null);
        onShouldNotDisplayProgress();
    }

    @Override
    protected void onResume() {
        super.onResume();
        retaskService.setProgressListener(this);
    }

    public static class ProgressDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog progressDialog = new Dialog(getActivity(), R.style.RetaskProgressDialog);
            progressDialog.setCancelable(false);
            progressDialog.setContentView(R.layout.spinner_view);
            return progressDialog;
        }
    }
}
