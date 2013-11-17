package me.loki2302.dal;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import me.loki2302.dal.dto.ServiceResultDto;

import roboguice.inject.ContextSingleton;

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
					if(result.ok) {
						((Activity)context).runOnUiThread(new Runnable() {
							@Override
							public void run() {
								callback.onSuccess(result.payload);								
							}							
						});						
					} else {
						((Activity)context).runOnUiThread(new Runnable() {
							@Override
							public void run() {
								callback.onError();								
							}							
						});
					}						
				} catch(Exception e) {
					e.printStackTrace();
					
					((Activity)context).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							callback.onError();								
						}							
					});
				} finally {
					progressDialog.dismiss();
				}
			}				
		});
	}
}