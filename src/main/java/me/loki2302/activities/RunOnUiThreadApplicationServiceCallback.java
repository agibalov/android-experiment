package me.loki2302.activities;

import me.loki2302.dal.ApplicationServiceCallback;
import android.app.Activity;

public abstract class RunOnUiThreadApplicationServiceCallback<T> implements ApplicationServiceCallback<T> {
	private final Activity activity;
	
	public RunOnUiThreadApplicationServiceCallback(Activity activity) {
		this.activity = activity;
	}

	@Override
	public void onSuccess(final T result) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				onSuccessOnUiThread(result);					
			}				
		});			
	}

	@Override
	public void onError() {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				onErrorOnUiThread();					
			}				
		});			
	}
	
	protected abstract void onSuccessOnUiThread(T result);
	protected abstract void onErrorOnUiThread();		
}