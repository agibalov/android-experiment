package me.retask.dal.dto;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

public enum ServiceError {
	NoError(0),
    InternalError(1),
    NoSuchUser(2),
    InvalidPassword(3),
    UserAlreadyRegistered(4),
    SessionExpired(5),
    NoSuchTask(6),
    NoPermissions(7),
    InvalidTaskStatus(8),
    ValidationError(9),
    NoSuchPendingUser(10),
    NoSuchPendingPasswordResetRequest(11);
    
    private final int errorCode;
    
    ServiceError(int errorCode) {
    	this.errorCode = errorCode;
    }
    
    @JsonValue
    public int getErrorCode() {
    	return errorCode;
    }
    
    @JsonCreator
    public static ServiceError makeServiceError(int errorCode) {
    	for(ServiceError serviceError : ServiceError.values()) {
    		if(serviceError.errorCode != errorCode) {
    			continue;
    		}
    		
    		return serviceError;
    	}
    	
    	throw new IllegalArgumentException(String.format("Don't know how to map error code %d", errorCode));
    }
}
