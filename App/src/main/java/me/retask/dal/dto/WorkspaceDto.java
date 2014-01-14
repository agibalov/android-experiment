package me.retask.dal.dto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class WorkspaceDto {
	@JsonProperty("Tasks")
	public List<TaskDto> tasks;
}