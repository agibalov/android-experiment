package me.retask.webapi.dto;

import org.codehaus.jackson.annotate.JsonProperty;

public class TaskDescriptionDto {
	@JsonProperty("TaskDescription")
	public String taskDescription;
}