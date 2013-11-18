package me.loki2302.activities;

import me.loki2302.dal.LongOperationListener;
import roboguice.inject.ContextSingleton;
import android.app.ProgressDialog;
import android.content.Context;

import com.google.inject.Inject;

@ContextSingleton 
class ProgressDialogLongOperationListener implements LongOperationListener {
	@Inject
	private Context context;
	
	private ProgressDialog progressDialog;

	@Override
	public void onLongOperationStarted(String message) {		
		progressDialog = ProgressDialog.show(context, "Working", message, true);
	}

	@Override
	public void onLongOperationFinished() {
		progressDialog.dismiss();
		progressDialog = null;
	}
}