package me.loki2302.infrastructure;

public interface ContextAwareApplicationCommandResultListener<TContext, TResult> {
    void onResult(TContext context, TResult result);
}
