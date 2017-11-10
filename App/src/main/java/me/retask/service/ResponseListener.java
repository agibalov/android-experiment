package me.retask.service;

import me.retask.service.requests.ServiceRequest;

public interface ResponseListener {
    void onSuccess(ServiceRequest<?> request, Object result);
    void onError(ServiceRequest<?> request, RuntimeException exception);
}
