package me.retask.webapi.dto;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class TaskDto {
    public final static int TASK_STATUS_TODO = 0;
    public final static int TASK_STATUS_IN_PROGRESS = 1;
    public final static int TASK_STATUS_DONE = 2;
    public final static int TASK_STATUS_COMPLETE = 3;

	@JsonProperty("TaskId")
	public int taskId;
	
	@JsonProperty("TaskDescription")
	public String taskDescription;
	
	@JsonProperty("TaskStatus")
	public int taskStatus;
	
	@JsonProperty("CreatedAt")
	public Date createdAt;
	
	@JsonProperty("ModifiedAt")
	public Date modifiedAt;
}