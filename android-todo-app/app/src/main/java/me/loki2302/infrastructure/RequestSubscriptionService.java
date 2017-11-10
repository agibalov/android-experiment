package me.loki2302.infrastructure;

import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

import roboguice.util.Ln;

@Singleton
public class RequestSubscriptionService {
    private final List<RequestSubscription> requestSubscriptions = new ArrayList<RequestSubscription>();

    public void addSubscription(String activityId, String requestToken, ContextAwareApplicationCommandResultListener<?, ?> contextAwareResultListener) {
        RequestSubscription requestSubscription = new RequestSubscription();
        requestSubscription.activityId = activityId;
        requestSubscription.requestToken = requestToken;
        requestSubscription.contextAwareResultListener = contextAwareResultListener;
        requestSubscriptions.add(requestSubscription);
    }

    public void handleResult(String activityId, String requestToken, Object context, Object result) {
        for(RequestSubscription requestSubscription : requestSubscriptions) {
            if(!requestSubscription.activityId.equals(activityId)) {
                continue;
            }

            if(!requestSubscription.requestToken.equals(requestToken)) {
                continue;
            }

            requestSubscription.contextAwareResultListener.onResult(context, result);
            requestSubscriptions.remove(requestSubscription);
            return;
        }

        //
        for(RequestSubscription requestSubscription : requestSubscriptions) {
            Ln.i("Request subscription: %s %s", requestSubscription.activityId, requestSubscription.requestToken);
        }
        //

        String message = String.format("There was no subscription for request %s [%s]", requestToken, activityId);
        throw new IllegalStateException(message);
    }
}
