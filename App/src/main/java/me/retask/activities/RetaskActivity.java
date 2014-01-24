package me.retask.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.inject.Inject;

import me.retask.R;
import me.retask.service.ResponseListener;
import me.retask.service.RetaskService;
import me.retask.service.requests.ServiceRequest;
import roboguice.util.Ln;

public abstract class RetaskActivity extends RoboActionBarActivity implements RetaskService.ProgressListener, ResponseListener {
    @Inject
    private RetaskService retaskService;

    protected <T> void run(ServiceRequest<T> serviceRequest) {
        retaskService.submit(serviceRequest);
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
        retaskService.setRequestListener(null);
        onShouldNotDisplayProgress();
    }

    @Override
    protected void onResume() {
        super.onResume();
        retaskService.setProgressListener(this);
        retaskService.setRequestListener(this);
    }

    @Override
    public final void onSuccess(final ServiceRequest<?> request, final Object result) {
        Ln.i("Success: %s [request was: %s]", result, request);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                handleSuccessOnUiThread(request, result);
            }
        });
    }

    @Override
    public final void onError(final ServiceRequest<?> request, final RuntimeException exception) {
        Ln.i("Error: %s [request was: %s]", exception, request);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                handleErrorOnUiThread(request, exception);
            }
        });
    }

    protected void handleSuccessOnUiThread(ServiceRequest<?> request, Object result) {
    }

    protected void handleErrorOnUiThread(ServiceRequest<?> request, RuntimeException exception) {
    }

    public static class ProgressDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog progressDialog = new Dialog(getActivity(), R.style.RetaskProgressDialog);
            progressDialog.setCancelable(false);
            progressDialog.setContentView(R.layout.spinner_view);
            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.spinner);
            progressDialog.findViewById(R.id.spinner).setAnimation(animation);
            return progressDialog;
        }
    }
}
