package me.loki2302.dal;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import me.loki2302.dal.dto.ServiceResultDto;

import roboguice.util.Ln;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ApiCallProcessor {	
	private final Executor executor = Executors.newSingleThreadExecutor();
	
	@Inject
	private RetaskApi retaskApi;
	
	public <TResult> void process(final LongOperationListener longOperationListener, final ApiCall<TResult> apiCall, final ApiCallback<TResult> callback) {
		longOperationListener.onLongOperationStarted(apiCall.describe());
		
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					final ServiceResultDto<TResult> result = apiCall.performApiCall(retaskApi);
					
					Ln.i("api=%s, response ok=%b, error=%s", apiCall.describe(), result.ok, result.error);
					
					if(result.ok) {
						callback.onSuccess(result.payload);
					} else {						
						callback.onError(result, null);								
					}						
				} catch(final Exception e) {
					e.printStackTrace();
					callback.onError(null, e);								
				} finally {
					longOperationListener.onLongOperationFinished();
				}
			}				
		});
	}
}