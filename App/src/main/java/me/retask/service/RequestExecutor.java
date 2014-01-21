package me.retask.service;

import android.content.Context;
import android.content.Intent;

import com.google.inject.Singleton;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.retask.service.requests.RetaskServiceRequest;

@Singleton
public class RequestExecutor {
    private final Map<String, RequestInfo> requestsMap = new HashMap<String, RequestInfo>();

    public synchronized String run(Context context, RetaskServiceRequest retaskServiceRequest) {
        String requestToken = UUID.randomUUID().toString();

        RequestInfo requestInfo = new RequestInfo();
        requestInfo.requestToken = requestToken;
        requestInfo.requestListener = null;
        requestInfo.done = false;
        requestsMap.put(requestToken, requestInfo);

        Intent intent = retaskServiceRequest.getIntent(context);
        intent.putExtra("requestToken", requestToken);
        context.startService(intent);
        return requestToken;
    }

    public synchronized void setRequestListener(String requestToken, RequestListener requestListener) {
        RequestInfo requestInfo = requestsMap.get(requestToken);
        requestInfo.requestListener = requestListener;
        if(requestListener != null) {
            if(requestInfo.done) {
                boolean unsubscribe = requestListener.onRequestComplete(requestToken);
                if(unsubscribe) {
                    requestsMap.remove(requestToken);
                }
            }
        }
    }

    public synchronized void notifyRequestDone(String requestToken) {
        RequestInfo requestInfo = requestsMap.get(requestToken);
        requestInfo.done = true;
        if(requestInfo.requestListener != null) {
            boolean unsubscribe = requestInfo.requestListener.onRequestComplete(requestToken);
            if(unsubscribe) {
                requestsMap.remove(requestToken);
            }
        }
    }

    public static interface RequestListener {
        boolean onRequestComplete(String requestToken);
    }

    private static class RequestInfo {
        public String requestToken;
        public boolean done;
        public RequestListener requestListener;
    }
}
