package me.loki2302.app.locators;

import java.util.List;

import me.loki2302.app.data.Task;

public interface AllTasksListener {
    void onSetTaskList(List<Task> tasks);
    void onTaskListChanged();
}
