package me.loki2302.dal.dto;

import org.codehaus.jackson.annotate.JsonProperty;

public class SessionDto {
	@JsonProperty("SessionToken")
	public String sessionToken;
}