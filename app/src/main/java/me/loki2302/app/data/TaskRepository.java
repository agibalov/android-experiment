package me.loki2302.app.data;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository {
    private final List<Task> tasks = new ArrayList<Task>();
    private int lastTaskId;
    private TaskRepositoryListener taskRepositoryListener;

    public TaskRepository(TaskRepositoryListener taskRepositoryListener) {
        this.taskRepositoryListener = taskRepositoryListener;
    }

    public void insert(Task task) {
        task.id = ++lastTaskId;
        tasks.add(task);
        taskRepositoryListener.onEvent(new TaskAppearedEvent(task.id));
    }

    public void update(Task task) {
        taskRepositoryListener.onEvent(new TaskChangedEvent(task.id));
    }

    public void delete(int taskId) {
        Task task = findOne(taskId);
        tasks.remove(task);
        taskRepositoryListener.onEvent(new TaskDisappearedEvent(task.id));
    }

    public List<Task> findAll() {
        return tasks;
    }

    public Task findOne(int taskId) {
        for(Task task : tasks) {
            if(task.id != taskId) {
                continue;
            }

            return task;
        }

        return null;
    }
}
