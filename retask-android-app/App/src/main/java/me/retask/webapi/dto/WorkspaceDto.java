package me.retask.webapi.dto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class WorkspaceDto {
	@JsonProperty("Tasks")
	public List<TaskDto> tasks;
}