package me.retask.service;

import android.app.Application;
import android.content.ContentResolver;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.retask.dal.ApplicationState;
import me.retask.service.requests.ServiceRequest;
import me.retask.webapi.ApiCallProcessor;

@Singleton
public class RetaskService {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Map<String, RequestInfo> requestInfoMap = new HashMap<String, RequestInfo>();
    private ProgressListener progressListener;

    @Inject
    private ApiCallProcessor apiCallProcessor;

    @Inject
    private ApplicationState applicationState;

    @Inject
    private Application application;

    public synchronized <TResult> String submit(ServiceRequest<TResult> request) {
        String requestToken = UUID.randomUUID().toString();

        RequestInfo requestInfo = new RequestInfo();
        requestInfo.requestToken = requestToken;
        requestInfo.listener = null;
        requestInfo.pending = true;
        requestInfo.ok = false;
        requestInfo.result = null;
        requestInfo.exception = null;
        requestInfoMap.put(requestToken, requestInfo);

        ServiceRunnable serviceRunnable = new ServiceRunnable(
                requestToken,
                apiCallProcessor,
                application.getContentResolver(),
                request);

        executorService.submit(serviceRunnable);

        return requestToken;
    }

    public synchronized void setRequestListener(String requestToken, RetaskServiceRequestListener listener) {
        RequestInfo requestInfo = requestInfoMap.get(requestToken);
        requestInfo.listener = listener;

        if(listener != null && !requestInfo.pending) {
            if(requestInfo.ok) {
                requestInfo.listener.onSuccess(requestToken, requestInfo.result);
            } else {
                requestInfo.listener.onError(requestToken, requestInfo.exception);
            }

            requestInfoMap.remove(requestToken);
        }
    }

    public synchronized void setProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
        notifyProgressListener();
    }

    private synchronized void notifyProgressListener() {
        if(progressListener == null) {
            return;
        }

        for(RequestInfo requestInfo : requestInfoMap.values()) {
            if(requestInfo.pending) {
                progressListener.onShouldDisplayProgress();
                return;
            }
        }

        progressListener.onShouldNotDisplayProgress();
    }

    private synchronized void notifyRequestStarted(String requestToken) {
        RequestInfo requestInfo = requestInfoMap.get(requestToken);
        requestInfo.pending = true;
    }

    private synchronized void notifyRequestSuccess(String requestToken, Object result) {
        RequestInfo requestInfo = requestInfoMap.get(requestToken);
        requestInfo.pending = false;
        requestInfo.ok = true;
        requestInfo.result = result;

        if(requestInfo.listener != null) {
            requestInfo.listener.onSuccess(requestToken, result);
            requestInfoMap.remove(requestToken);
        }
    }

    private synchronized void notifyRequestFailure(String requestToken, RuntimeException exception) {
        RequestInfo requestInfo = requestInfoMap.get(requestToken);
        requestInfo.pending = false;
        requestInfo.ok = false;
        requestInfo.exception = exception;

        if(requestInfo.listener != null) {
            requestInfo.listener.onError(requestToken, exception);
            requestInfoMap.remove(requestToken);
        }
    }

    private class ServiceRunnable implements Runnable {
        private final String requestToken;
        private final ApiCallProcessor apiCallProcessor;
        private final ContentResolver contentResolver;
        private final ServiceRequest retaskServiceRequest;

        public ServiceRunnable(
                String requestToken,
                ApiCallProcessor apiCallProcessor,
                ContentResolver contentResolver,
                ServiceRequest retaskServiceRequest) {

            this.requestToken = requestToken;
            this.apiCallProcessor = apiCallProcessor;
            this.contentResolver = contentResolver;
            this.retaskServiceRequest = retaskServiceRequest;
        }

        @Override
        public void run() {
            notifyRequestStarted(requestToken);
            notifyProgressListener();

            //
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //

            try {
                Object result = retaskServiceRequest.run(apiCallProcessor, applicationState, contentResolver);
                notifyRequestSuccess(requestToken, result);
            } catch(RuntimeException e) {
                notifyRequestFailure(requestToken, e);
            } finally {
                notifyProgressListener();
            }
        }
    }

    private static class RequestInfo {
        public String requestToken;
        public RetaskServiceRequestListener listener;
        public boolean pending;
        public boolean ok;
        public Object result;
        public RuntimeException exception;
    }

    public static interface ProgressListener {
        void onShouldDisplayProgress();
        void onShouldNotDisplayProgress();
    }
}
