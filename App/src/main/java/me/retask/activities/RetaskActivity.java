package me.retask.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import org.springframework.web.client.RestTemplate;

import me.retask.R;
import me.retask.dal.ApiCall;
import me.retask.dal.RetaskException;
import me.retask.dal.dto.ServiceError;
import me.retask.dal.dto.ServiceResultDto;
import roboguice.util.Ln;
import roboguice.util.RoboAsyncTask;

public abstract class RetaskActivity extends RoboActionBarActivity {
	private Dialog progressDialog;
	
	@Inject
	@Named("apiRootUrl")
	private String apiRootUrl;
	
	@Inject	
	private RestTemplate restTemplate;

    public <TResult> void run(ApiCall<TResult> apiCall, DoneCallback<TResult> doneCallback) {
		run(apiCall, doneCallback, new DefaultFailCallback());
	}
	
	protected <TResult> void run(ApiCall<TResult> apiCall, DoneCallback<TResult> doneCallback, FailCallback failCallback) {			
		new CallApiRoboAsyncTask<TResult>(
				this,
				doneCallback,
				failCallback,
				apiCall).execute();
	}
	
	private final class CallApiRoboAsyncTask<TResult> extends RoboAsyncTask<ServiceResultDto<TResult>> {
		private final DoneCallback<TResult> doneCallback;
		private final FailCallback failCallback;
		private final ApiCall<TResult> apiCall;

		private CallApiRoboAsyncTask(
				Context context,
				DoneCallback<TResult> doneCallback,
				FailCallback failCallback,
				ApiCall<TResult> apiCall) {
			
			super(context);
			this.doneCallback = doneCallback;
			this.failCallback = failCallback;
			this.apiCall = apiCall;
		}

		@Override
		public ServiceResultDto<TResult> call() throws Exception {				
			ServiceResultDto<TResult> result = apiCall.performApiCall(apiRootUrl, restTemplate);
			return result;
		}

		@Override
		protected void onPreExecute() throws Exception {
            progressDialog = new Dialog(context, R.style.RetaskProgressDialog);
            progressDialog.setContentView(R.layout.spinner_view);
            progressDialog.show();
		}

		@Override
		protected void onSuccess(ServiceResultDto<TResult> result) throws Exception {			
			if(result.ok) {
				doneCallback.onDone(result.payload);
			} else {						
				failCallback.onFail(new RetaskException(result));
			}
		}

		@Override
		protected void onException(Exception e) throws RuntimeException {
			failCallback.onFail(e);
		}

		@Override
		protected void onFinally() throws RuntimeException {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
	
	public interface DoneCallback<TResult> {
		void onDone(TResult result);		
	}
	
	protected interface FailCallback {
		void onFail(Exception e);
	}
	
	protected class DefaultFailCallback implements FailCallback {
		@Override
		public void onFail(Exception e) {			
			if(!(e instanceof RetaskException)) {
				handle(e);
				return;				
			}
			
			RetaskException retaskException = (RetaskException)e;
			ServiceResultDto<?> serviceResult = retaskException.serviceResult;
			ServiceError serviceError = serviceResult.error;			
						
			if(serviceError.equals(ServiceError.InternalError)) {
				onInternalError(serviceResult);
			} else if(serviceError.equals(ServiceError.NoSuchUser)) {
				onNoSuchUser(serviceResult);
			} else if(serviceError.equals(ServiceError.InvalidPassword)) {
				onInvalidPassword(serviceResult);
			} else if(serviceError.equals(ServiceError.UserAlreadyRegistered)) {
				onUserAlreadyRegistered(serviceResult);
			} else if(serviceError.equals(ServiceError.SessionExpired)) {
				onSessionExpired(serviceResult);
			} else if(serviceError.equals(ServiceError.NoSuchTask)) {
				onNoSuchTask(serviceResult);
			} else if(serviceError.equals(ServiceError.NoPermissions)) {
				onNoPermissions(serviceResult);
			} else if(serviceError.equals(ServiceError.InvalidTaskStatus)) {
				onInvalidTaskStatus(serviceResult);
			} else if(serviceError.equals(ServiceError.ValidationError)) {
				onValidationError(serviceResult);
			} else if(serviceError.equals(ServiceError.NoSuchPendingUser)) {
				onNoSuchPendingUser(serviceResult);
			} else if(serviceError.equals(ServiceError.NoSuchPendingPasswordResetRequest)) {
				onNoSuchPendingPasswordResetRequest(serviceResult);
			} else {
				handle(serviceResult);
			}			
		}
		
		protected void onInternalError(ServiceResultDto<?> serviceResult) {
			handle(serviceResult);
		}
		
		protected void onNoSuchUser(ServiceResultDto<?> serviceResult) {
			handle(serviceResult);
		}
		
		protected void onInvalidPassword(ServiceResultDto<?> serviceResult) {
			handle(serviceResult);
		}
		
		protected void onUserAlreadyRegistered(ServiceResultDto<?> serviceResult) {
			handle(serviceResult);
		}
		
		protected void onSessionExpired(ServiceResultDto<?> serviceResult) {
			handle(serviceResult);
		}
		
		protected void onNoSuchTask(ServiceResultDto<?> serviceResult) {
			handle(serviceResult);
		}
		
		protected void onNoPermissions(ServiceResultDto<?> serviceResult) {
			handle(serviceResult);
		}
		
		protected void onInvalidTaskStatus(ServiceResultDto<?> serviceResult) {
			handle(serviceResult);
		}
		
		protected void onValidationError(ServiceResultDto<?> serviceResult) {
			handle(serviceResult);
		}
		
		protected void onNoSuchPendingUser(ServiceResultDto<?> serviceResult) {
			handle(serviceResult);
		}
		
		protected void onNoSuchPendingPasswordResetRequest(ServiceResultDto<?> serviceResult) {
			handle(serviceResult);
		}
		
		private void handle(Exception result) {
			Ln.e("DEFAULT ERROR HANDLER");
			
			new AlertDialog.Builder(RetaskActivity.this)
				.setTitle("Unexpected error")
				.setMessage(String.format("Something is wrong: %s", result.getMessage()))
				.create()
				.show();
		}
		
		private void handle(ServiceResultDto<?> result) {			
			Ln.e("DEFAULT ERROR HANDLER");
			
			new AlertDialog.Builder(RetaskActivity.this)
				.setTitle("Error")
				.setMessage(String.format("Service says something is wrong: %s", result.error))
				.create()
				.show();
		}		
	}	
}