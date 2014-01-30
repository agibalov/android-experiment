package me.loki2302.infrastructure;

public class RequestSubscription<TContext, TResult> {
    public String activityId;
    public String requestToken;
    public ContextAwareApplicationCommandResultListener<TContext, TResult> contextAwareResultListener;
}
