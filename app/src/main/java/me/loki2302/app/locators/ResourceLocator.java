package me.loki2302.app.locators;

import me.loki2302.app.App;

public interface ResourceLocator<TListener> {
    void initListener(App app, TListener listener);
    void handleEvent(Object event, TListener listener);
}
