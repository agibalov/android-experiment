package me.loki2302.activities;

import org.jdeferred.DoneCallback;

import android.app.Activity;

public abstract class UiDoneCallback<D> implements DoneCallback<D> {
	private final Activity activity;
	
	public UiDoneCallback(Activity activity) {
		this.activity = activity;
	}
	
	@Override
	public void onDone(final D result) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				uiOnDone(result);					
			}				
		});
	}
	
	protected abstract void uiOnDone(D result);
}