package me.retask.application;

import me.retask.dal.dto.TaskDto;
import me.retask.dal.dto.TaskStatus;

public class Task extends Entity {
	public TaskStatus status;
	public String description;

    public static Task fromTaskDto(TaskDto taskDto) {
        Task task = new Task();
        task.id = taskDto.taskId;
        task.description = taskDto.taskDescription;
        task.status = taskDto.taskStatus;
        return task;
    }
}