package me.loki2302.activities;

import me.loki2302.dal.RetaskException;
import me.loki2302.dal.dto.ServiceError;
import me.loki2302.dal.dto.ServiceResultDto;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;

import android.app.AlertDialog;

import roboguice.activity.RoboActivity;
import roboguice.util.Ln;

public abstract class RetaskActivity extends RoboActivity {
	protected abstract class UiDoneCallback<D> implements DoneCallback<D> {			
		@Override
		public void onDone(final D result) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					uiOnDone(result);					
				}				
			});
		}
		
		protected abstract void uiOnDone(D result);
	}
	
	protected abstract class UiFailCallback<F> implements FailCallback<F> {
		@Override
		public void onFail(final F result) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					uiOnFail(result);					
				}
			});
		}		
		
		protected abstract void uiOnFail(F result);
	}
	
	protected class DefaultFailCallback extends UiFailCallback<Exception> {
		@Override
		protected void uiOnFail(Exception result) {
			if(!(result instanceof RetaskException)) {
				handle(result);
				return;				
			}
			
			RetaskException retaskException = (RetaskException)result;
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