package me.retask.application;

import me.retask.dal.dto.TaskStatus;

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