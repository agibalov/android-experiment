package me.loki2302.app.locators;

import me.loki2302.app.data.Task;

public interface SingleTaskListener {
    void onSetTask(Task task);
    void onTaskChanged();
}
