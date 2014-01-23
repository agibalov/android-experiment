package me.retask.service;

import me.retask.service.requests.ServiceRequest;

public interface ResponseListener {
    void onSuccess(String requestToken, ServiceRequest<?> request, Object result);
    void onError(String requestToken, ServiceRequest<?> request, RuntimeException exception);
}
