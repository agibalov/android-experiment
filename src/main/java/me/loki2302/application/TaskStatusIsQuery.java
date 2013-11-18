package me.loki2302.application;

import me.loki2302.dal.dto.TaskStatus;

public class TaskStatusIsQuery implements Query<Task> {
	private final TaskStatus taskStatus;
	
	public TaskStatusIsQuery(TaskStatus taskStatus) {
		this.taskStatus = taskStatus;
	}
	
	@Override
	public boolean match(Task entity) {
		return entity.status == taskStatus;
	}		
}