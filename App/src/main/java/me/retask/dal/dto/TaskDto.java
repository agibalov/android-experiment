package me.retask.dal.dto;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class TaskDto {
	@JsonProperty("TaskId")
	public int taskId;
	
	@JsonProperty("TaskDescription")
	public String taskDescription;
	
	@JsonProperty("TaskStatus")
	public TaskStatus taskStatus;
	
	@JsonProperty("CreatedAt")
	public Date createdAt;
	
	@JsonProperty("ModifiedAt")
	public Date modifiedAt;
}