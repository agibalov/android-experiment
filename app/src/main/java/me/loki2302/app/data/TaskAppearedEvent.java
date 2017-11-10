package me.loki2302.app.data;

public class TaskAppearedEvent {
    public final int taskId;

    public TaskAppearedEvent(int taskId) {
        this.taskId = taskId;
    }
}
