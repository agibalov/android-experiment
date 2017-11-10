package me.loki2302.app;

import me.loki2302.app.locators.ResourceLocator;

public class ResourceSubscription<TListener> {
    private final String subscriptionToken;
    private final ResourceLocator<TListener> resourceLocator;
    private final TListener listener;

    public ResourceSubscription(String subscriptionToken, ResourceLocator<TListener> resourceLocator, TListener listener) {
        this.subscriptionToken = subscriptionToken;
        this.resourceLocator = resourceLocator;
        this.listener = listener;
    }

    public String getSubscriptionToken() {
        return subscriptionToken;
    }

    public void initListener(App app) {
        resourceLocator.initListener(app, listener);
    }

    public void handleEvent(Object event) {
        resourceLocator.handleEvent(event, listener);
    }
}
