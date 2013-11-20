package me.loki2302.dal;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.jdeferred.Deferred;
import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;
import org.springframework.web.client.RestTemplate;

import me.loki2302.dal.dto.ServiceResultDto;

import roboguice.util.Ln;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@Singleton
public class ApiCallProcessor {	
	private final Executor executor = Executors.newSingleThreadExecutor();
	
	@Inject
	@Named("apiRootUrl")
	private String apiRootUrl;
	
	@Inject	
	private RestTemplate restTemplate;
	
	public <TResult> Promise<TResult, Exception, Void> process(
			final LongOperationListener longOperationListener, 
			final ApiCall<TResult> apiCall) {
		
		longOperationListener.onLongOperationStarted(apiCall.describe());
		
		final Deferred<TResult, Exception, Void> deferred = new DeferredObject<TResult, Exception, Void>(); 
		
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					final ServiceResultDto<TResult> result = apiCall.performApiCall(apiRootUrl, restTemplate);
					
					Ln.i("api=%s, response ok=%b, error=%s", apiCall, result.ok, result.error);
					
					if(result.ok) {
						deferred.resolve(result.payload);
					} else {
						deferred.reject(null);								
					}						
				} catch(final Exception e) {
					e.printStackTrace();
					deferred.reject(e);								
				} finally {
					longOperationListener.onLongOperationFinished();
				}
			}				
		});
		
		return deferred.promise();
	}
}