package me.loki2302.app.data;

public class TaskDisappearedEvent {
    public final int taskId;

    public TaskDisappearedEvent(int taskId) {
        this.taskId = taskId;
    }
}
