package me.loki2302.app.commands;

import me.loki2302.app.App;
import me.loki2302.app.data.Task;
import me.loki2302.app.data.TaskRepository;

public class UpdateTaskApplicationCommand implements ApplicationCommand {
    private final int taskId;
    private final String taskDescription;

    public UpdateTaskApplicationCommand(int taskId, String taskDescription) {
        this.taskId = taskId;
        this.taskDescription = taskDescription;
    }

    @Override
    public void run(App app) {
        TaskRepository taskRepository = app.getTaskRepository();
        Task task = taskRepository.findOne(taskId);
        if(task == null) {
            throw new IllegalStateException();
        }

        task.description = taskDescription;
        taskRepository.update(task);
    }
}
