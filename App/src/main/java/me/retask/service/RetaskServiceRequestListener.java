package me.retask.service;

public interface RetaskServiceRequestListener<TResult> {
    void onSuccess(String requestToken, TResult result);
    void onError(String requestToken, RuntimeException exception);
}
