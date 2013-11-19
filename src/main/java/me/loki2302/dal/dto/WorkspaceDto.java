package me.loki2302.dal.dto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class WorkspaceDto {
	@JsonProperty("Tasks")
	public List<TaskDto> tasks;
}