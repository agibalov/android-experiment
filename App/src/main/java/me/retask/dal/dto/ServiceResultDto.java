package me.retask.dal.dto;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;

public class ServiceResultDto<TPayload> {
	@JsonProperty("Ok")
	public boolean ok;
	
	@JsonProperty("Payload")
	public TPayload payload;
	
	@JsonProperty("Error")
	public ServiceError error;
	
	@JsonProperty("FieldsInError")
	public Map<String, List<String>> fieldsInError;
}