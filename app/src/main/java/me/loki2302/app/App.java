package me.loki2302.app;

import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import me.loki2302.app.commands.ApplicationCommand;
import me.loki2302.app.data.TaskRepository;
import me.loki2302.app.data.TaskRepositoryListener;
import me.loki2302.app.locators.ResourceLocator;

@Singleton
public class App implements TaskRepositoryListener {
    private final Executor executor = Executors.newSingleThreadExecutor();
    private TaskRepository taskRepository = new TaskRepository(this);
    private final List<Subscription<?>> subscriptions = new ArrayList<Subscription<?>>();

    public void submit(ApplicationCommand applicationCommand) {
        executor.execute(new ApplicationCommandRunnable(this, applicationCommand));
    }

    public <TListener> String subscribe(ResourceLocator<TListener> resourceLocator, TListener listener) {
        String subscriptionToken = UUID.randomUUID().toString();
        Subscription<TListener> subscription = new Subscription<TListener>(subscriptionToken, resourceLocator, listener);
        subscription.initListener(this);
        subscriptions.add(subscription);
        return subscriptionToken;
    }

    public void unsubscribe(String subscriptionToken) {
        for(Subscription<?> subscription : subscriptions) {
            String token = subscription.getSubscriptionToken();
            if(!token.equals(subscriptionToken)) {
                continue;
            }

            subscriptions.remove(subscription);
            return;
        }

        throw new IllegalArgumentException();
    }

    public TaskRepository getTaskRepository() {
        return taskRepository;
    }

    @Override
    public void onEvent(Object event) {
        for(Subscription<?> subscription : subscriptions) {
            subscription.handleEvent(event);
        }
    }
}
