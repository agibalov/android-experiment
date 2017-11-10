package me.loki2302.app.locators;

import java.util.List;

import me.loki2302.app.App;
import me.loki2302.app.data.Task;
import me.loki2302.app.data.TaskAppearedEvent;
import me.loki2302.app.data.TaskChangedEvent;
import me.loki2302.app.data.TaskDisappearedEvent;
import me.loki2302.app.data.TaskRepository;

public class AllTasksResourceLocator implements ResourceLocator<AllTasksListener> {
    @Override
    public void initListener(App app, AllTasksListener allTasksListener) {
        TaskRepository taskRepository = app.getTaskRepository();
        List<Task> allTasks = taskRepository.findAll();
        allTasksListener.onSetTaskList(allTasks);
    }

    @Override
    public void handleEvent(Object event, AllTasksListener allTasksListener) {
        if(event instanceof TaskAppearedEvent) {
            allTasksListener.onTaskListChanged();
            return;
        }

        if(event instanceof TaskDisappearedEvent) {
            allTasksListener.onTaskListChanged();
            return;
        }

        if(event instanceof TaskChangedEvent) {
            allTasksListener.onTaskListChanged();
            return;
        }
    }
}
