package me.loki2302.app.commands;

import me.loki2302.app.App;
import me.loki2302.app.data.Task;
import me.loki2302.app.data.TaskRepository;

public class CreateTaskApplicationCommand implements ApplicationCommand {
    private final String taskDescription;

    public CreateTaskApplicationCommand(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    @Override
    public void run(App app) {
        Task task = new Task(-1, taskDescription);
        TaskRepository taskRepository = app.getTaskRepository();
        taskRepository.insert(task);
    }
}
