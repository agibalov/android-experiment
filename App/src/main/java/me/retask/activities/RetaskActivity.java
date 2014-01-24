package me.retask.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.inject.Inject;

import java.util.List;
import java.util.Map;

import me.retask.R;
import me.retask.service.ResponseListener;
import me.retask.service.RetaskService;
import me.retask.service.requests.ServiceRequest;
import me.retask.webapi.ConnectivityException;
import me.retask.webapi.NetworkException;
import me.retask.webapi.RetaskServiceException;
import me.retask.webapi.dto.ServiceResultDto;
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
        if(exception instanceof ConnectivityException) {
            handleConnectivityException(request, (ConnectivityException)exception);
        } else if(exception instanceof NetworkException) {
            handleNetworkException(request, (NetworkException)exception);
        } else if(exception instanceof RetaskServiceException) {
            RetaskServiceException e = (RetaskServiceException)exception;
            if(e.errorCode == ServiceResultDto.RETASK_RESULT_INTERNAL_ERROR) {
                handleRetaskInternalError(request);
            } else if(e.errorCode == ServiceResultDto.RETASK_RESULT_NO_SUCH_USER) {
                handleRetaskNoSuchUserError(request);
            } else if(e.errorCode == ServiceResultDto.RETASK_RESULT_INVALID_PASSWORD) {
                handleRetaskInvalidPasswordError(request);
            } else if(e.errorCode == ServiceResultDto.RETASK_RESULT_USER_ALREADY_REGISTERED) {
                handleRetaskUserAlreadyRegisteredError(request);
            } else if(e.errorCode == ServiceResultDto.RETASK_RESULT_SESSION_EXPIRED) {
                handleRetaskSessionExpiredError(request);
            } else if(e.errorCode == ServiceResultDto.RETASK_RESULT_NO_SUCH_TASK) {
                handleRetaskNoSuchTaskError(request);
            } else if(e.errorCode == ServiceResultDto.RETASK_RESULT_NO_PERMISSIONS) {
                handleRetaskNoPermissionsError(request);
            } else if(e.errorCode == ServiceResultDto.RETASK_RESULT_INVALID_TASK_STATUS) {
                handleRetaskInvalidTaskStatusError(request);
            } else if(e.errorCode == ServiceResultDto.RETASK_RESULT_VALIDATION_ERROR) {
                handleRetaskValidationError(request, e.fieldsInError);
            } else {
                // TODO
            }
        } else {
            // TODO
        }
    }

    protected void handleConnectivityException(ServiceRequest<?> request, ConnectivityException exception) {
        displayErrorMessage("Looks like there's no internet connectivity");
    }

    protected void handleNetworkException(ServiceRequest<?> reuqest, NetworkException exception) {
        displayErrorMessage("A sort of network error");
    }

    protected void handleRetaskInternalError(ServiceRequest<?> request) {
        displayErrorMessage("Internal Error");
    }

    protected void handleRetaskNoSuchUserError(ServiceRequest<?> request) {
        displayErrorMessage("No such user");
    }

    protected void handleRetaskInvalidPasswordError(ServiceRequest<?> request) {
        displayErrorMessage("Invalid password");
    }

    protected void handleRetaskUserAlreadyRegisteredError(ServiceRequest<?> request) {
        displayErrorMessage("User already registered");
    }

    protected void handleRetaskSessionExpiredError(ServiceRequest<?> request) {
        displayErrorMessage("Session expired");
    }

    protected void handleRetaskNoSuchTaskError(ServiceRequest<?> request) {
        displayErrorMessage("No such task");
    }

    protected void handleRetaskNoPermissionsError(ServiceRequest<?> request) {
        displayErrorMessage("Access denied");
    }

    protected void handleRetaskInvalidTaskStatusError(ServiceRequest<?> request) {
        displayErrorMessage("Invalid task status");
    }

    protected void handleRetaskValidationError(ServiceRequest<?> request, Map<String, List<String>> fieldsInError) {
        displayErrorMessage("Validation error");
    }

    protected void displayErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
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
