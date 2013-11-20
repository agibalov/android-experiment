package me.loki2302.activities;

import org.jdeferred.DoneCallback;

import roboguice.activity.RoboActivity;

public abstract class RetaskActivity extends RoboActivity {
	protected abstract class UiDoneCallback<D> implements DoneCallback<D> {			
		@Override
		public void onDone(final D result) {
			RetaskActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					uiOnDone(result);					
				}				
			});
		}
		
		protected abstract void uiOnDone(D result);
	}
}