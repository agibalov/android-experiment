package me.loki2302.dal;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import me.loki2302.dal.dto.ServiceResultDto;

import roboguice.inject.ContextSingleton;
import roboguice.util.Ln;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.google.inject.Inject;

@ContextSingleton
public class ApiCallProcessor {	
	private final Executor executor = Executors.newSingleThreadExecutor();
	
	@Inject
	private RetaskApi retaskApi;
	
	@Inject
	private Context context;
	
	public <TResult> void process(final ApiCall<TResult> apiCall, final ApiCallback<TResult> callback) {
		
		final ProgressDialog progressDialog = ProgressDialog.show(context, "Working...", apiCall.describe(), true);
		
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					final ServiceResultDto<TResult> result = apiCall.performApiCall(retaskApi);
					
					Ln.i("Response ok=%b, error=%s", result.ok, result.error);
					
					if(result.ok) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								callback.onSuccess(result.payload);								
							}							
						});						
					} else {						
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								callback.onError(result, null);								
							}							
						});
					}						
				} catch(final Exception e) {
					e.printStackTrace();
					
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							callback.onError(null, e);								
						}							
					});
				} finally {
					progressDialog.dismiss();
				}
			}				
		});
	}
	
	private void runOnUiThread(Runnable runnable) {
		((Activity)context).runOnUiThread(runnable);
	}
}