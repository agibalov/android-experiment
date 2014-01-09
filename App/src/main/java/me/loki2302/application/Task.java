package me.loki2302.application;

import me.loki2302.dal.dto.TaskDto;
import me.loki2302.dal.dto.TaskStatus;

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