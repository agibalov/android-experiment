package me.retask.webapi.dto;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;

public class ServiceResultDto<TPayload> {
    public final static int RETASK_RESULT_NO_ERROR = 0;
    public final static int RETASK_RESULT_INTERNAL_ERROR = 1;
    public final static int RETASK_RESULT_NO_SUCH_USER = 2;
    public final static int RETASK_RESULT_INVALID_PASSWORD = 3;
    public final static int RETASK_RESULT_USER_ALREADY_REGISTERED = 4;
    public final static int RETASK_RESULT_SESSION_EXPIRED = 5;
    public final static int RETASK_RESULT_NO_SUCH_TASK = 6;
    public final static int RETASK_RESULT_NO_PERMISSIONS = 7;
    public final static int RETASK_RESULT_INVALID_TASK_STATUS = 8;
    public final static int RETASK_RESULT_VALIDATION_ERROR = 9;
    public final static int RETASK_RESULT_NO_SUCH_PENDING_USER = 10;
    public final static int RETASK_RESULT_NO_SUCH_PENDING_PASSWORD_RESET_REQUEST = 11;

	@JsonProperty("Ok")
	public boolean ok;
	
	@JsonProperty("Payload")
	public TPayload payload;
	
	@JsonProperty("Error")
	public int error;
	
	@JsonProperty("FieldsInError")
	public Map<String, List<String>> fieldsInError;
}