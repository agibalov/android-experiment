package me.loki2302.app.data;

public class TaskChangedEvent {
    public final int taskId;

    public TaskChangedEvent(int taskId) {
        this.taskId = taskId;
    }
}
