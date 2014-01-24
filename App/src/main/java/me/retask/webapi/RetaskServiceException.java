package me.retask.webapi;

import java.util.List;
import java.util.Map;

public class RetaskServiceException extends ApiCallException {
    public int errorCode;
    public Map<String, List<String>> fieldsInError;

    public RetaskServiceException(int errorCode, Map<String, List<String>> fieldsInError) {
        this.errorCode = errorCode;
        this.fieldsInError = fieldsInError;
    }
}
