package me.loki2302.app.locators;

import me.loki2302.app.App;
import me.loki2302.app.data.Task;
import me.loki2302.app.data.TaskChangedEvent;
import me.loki2302.app.data.TaskRepository;

public class SingleTaskResourceLocator implements ResourceLocator<SingleTaskListener> {
    private final int taskId;

    public SingleTaskResourceLocator(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public void initListener(App app, SingleTaskListener singleTaskListener) {
        TaskRepository taskRepository = app.getTaskRepository();
        Task task = taskRepository.findOne(taskId);
        /*if(task == null) {
             String message = String.format("There is no task with id %d", taskId);
            throw new IllegalStateException(message);
        }*/

        singleTaskListener.onSetTask(task);
    }

    @Override
    public void handleEvent(Object event, SingleTaskListener singleTaskListener) {
        if(event instanceof TaskChangedEvent) {
            TaskChangedEvent taskChangedEvent = (TaskChangedEvent)event;
            if(taskChangedEvent.taskId != taskId) {
                return;
            }

            singleTaskListener.onTaskChanged();
        }
    }
}
