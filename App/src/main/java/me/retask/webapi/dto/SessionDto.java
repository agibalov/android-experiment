package me.retask.webapi.dto;

import org.codehaus.jackson.annotate.JsonProperty;

public class SessionDto {
	@JsonProperty("SessionToken")
	public String sessionToken;
}