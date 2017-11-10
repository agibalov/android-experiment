package me.retask.service;

import android.app.Application;
import android.content.ContentResolver;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.retask.service.requests.ServiceRequest;
import me.retask.webapi.ApiCallProcessor;

@Singleton
public class RetaskService {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final List<RequestInfo> requestInfos = new ArrayList<RequestInfo>();
    private ProgressListener progressListener;
    private ResponseListener responseListener;

    @Inject
    private ApiCallProcessor apiCallProcessor;

    @Inject
    private ApplicationState applicationState;

    @Inject
    private Application application;

    public synchronized <TResult> void submit(ServiceRequest<TResult> request) {
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.serviceRequest = request;
        requestInfo.pending = true;
        requestInfo.ok = false;
        requestInfo.result = null;
        requestInfo.exception = null;
        requestInfos.add(requestInfo);

        ServiceRunnable serviceRunnable = new ServiceRunnable(
                apiCallProcessor,
                application.getContentResolver(),
                request);

        executorService.submit(serviceRunnable);
    }

    public synchronized void setRequestListener(ResponseListener listener) {
        responseListener = listener;

        if(responseListener == null) {
            return;
        }

        List<RequestInfo> requestInfosToRemove = new ArrayList<RequestInfo>();
        for(RequestInfo requestInfo : requestInfos) {
            if(requestInfo.pending) {
                continue;
            }

            if(requestInfo.ok) {
                responseListener.onSuccess(requestInfo.serviceRequest, requestInfo.result);
            } else {
                responseListener.onError(requestInfo.serviceRequest, requestInfo.exception);
            }

            requestInfosToRemove.add(requestInfo);
        }

        requestInfos.removeAll(requestInfosToRemove);
    }

    public synchronized void setProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
        notifyProgressListener();
    }

    private synchronized void notifyProgressListener() {
        if(progressListener == null) {
            return;
        }

        for(RequestInfo requestInfo : requestInfos) {
            if(requestInfo.pending) {
                progressListener.onShouldDisplayProgress();
                return;
            }
        }

        progressListener.onShouldNotDisplayProgress();
    }

    private synchronized void notifyRequestSuccess(ServiceRequest<?> serviceRequest, Object result) {
        for(RequestInfo requestInfo : requestInfos) {
            if(requestInfo.serviceRequest != serviceRequest) {
                continue;
            }

            requestInfo.pending = false;
            requestInfo.ok = true;
            requestInfo.result = result;

            if(responseListener != null) {
                responseListener.onSuccess(requestInfo.serviceRequest, result);
                requestInfos.remove(requestInfo);
                return;
            }
        }
    }

    private synchronized void notifyRequestFailure(ServiceRequest<?> serviceRequest, RuntimeException exception) {
        for(RequestInfo requestInfo : requestInfos) {
            if(requestInfo.serviceRequest != serviceRequest) {
                continue;
            }

            requestInfo.pending = false;
            requestInfo.ok = true;
            requestInfo.exception = exception;

            if(responseListener != null) {
                responseListener.onError(requestInfo.serviceRequest, exception);
                requestInfos.remove(requestInfo);
                return;
            }
        }
    }

    private class ServiceRunnable implements Runnable {
        private final ApiCallProcessor apiCallProcessor;
        private final ContentResolver contentResolver;
        private final ServiceRequest serviceRequest;

        public ServiceRunnable(
                ApiCallProcessor apiCallProcessor,
                ContentResolver contentResolver,
                ServiceRequest serviceRequest) {

            this.apiCallProcessor = apiCallProcessor;
            this.contentResolver = contentResolver;
            this.serviceRequest = serviceRequest;
        }

        @Override
        public void run() {
            notifyProgressListener();

            try {
                Object result = serviceRequest.run(apiCallProcessor, applicationState, contentResolver);
                notifyRequestSuccess(serviceRequest, result);
            } catch(RuntimeException e) {
                notifyRequestFailure(serviceRequest, e);
            } finally {
                notifyProgressListener();
            }
        }
    }

    private static class RequestInfo {
        public ServiceRequest<?> serviceRequest;
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
