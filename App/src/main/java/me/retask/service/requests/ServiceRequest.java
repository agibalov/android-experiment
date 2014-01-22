package me.retask.service.requests;

import android.content.ContentResolver;

import me.retask.dal.ApplicationState;
import me.retask.webapi.ApiCallProcessor;

public interface ServiceRequest<TResult> {
    TResult run(
            ApiCallProcessor apiCallProcessor,
            ApplicationState applicationState,
            ContentResolver contentResolver);
}
