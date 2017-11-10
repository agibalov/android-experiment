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
import roboguice.util.Ln;

@Singleton
public class App implements TaskRepositoryListener {
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final TaskRepository taskRepository = new TaskRepository(this);
    private final List<ResourceSubscription<?>> resourceSubscriptions = new ArrayList<ResourceSubscription<?>>();
    private final List<ApplicationCommandResultInfo> applicationCommandResultInfos = new ArrayList<ApplicationCommandResultInfo>();
    private ApplicationCommandResultListener applicationCommandResultListener;
    private ProgressListener progressListener;
    private boolean isInProgress;

    public synchronized <TResult> String submit(final ApplicationCommand<TResult> applicationCommand) {
        final String requestToken = UUID.randomUUID().toString();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                notifyProgressStarted();
                final int n = 5;
                for(int i = 0; i < n; ++i) {
                    Ln.i("Working %d/%d...", i + 1, n);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Ln.i("DONE!");

                TResult result = applicationCommand.run(App.this);
                notifyApplicationCommandResultAvailable(requestToken, result);
                notifyProgressFinished();
            }
        });
        return requestToken;
    }

    public synchronized <TListener> String subscribe(ResourceLocator<TListener> resourceLocator, TListener listener) {
        String subscriptionToken = UUID.randomUUID().toString();
        ResourceSubscription<TListener> resourceSubscription = new ResourceSubscription<TListener>(subscriptionToken, resourceLocator, listener);
        resourceSubscription.initListener(this);
        resourceSubscriptions.add(resourceSubscription);
        return subscriptionToken;
    }

    public synchronized void unsubscribe(String subscriptionToken) {
        for(ResourceSubscription<?> resourceSubscription : resourceSubscriptions) {
            String token = resourceSubscription.getSubscriptionToken();
            if(!token.equals(subscriptionToken)) {
                continue;
            }

            resourceSubscriptions.remove(resourceSubscription);
            return;
        }

        throw new IllegalArgumentException();
    }

    public synchronized TaskRepository getTaskRepository() {
        return taskRepository;
    }

    @Override
    public synchronized void onRepositoryEvent(Object event) {
        for(ResourceSubscription<?> resourceSubscription : resourceSubscriptions) {
            resourceSubscription.handleEvent(event);
        }
    }

    public synchronized void setApplicationCommandResultListener(ApplicationCommandResultListener applicationCommandResultListener) {
        this.applicationCommandResultListener = applicationCommandResultListener;
        if(applicationCommandResultListener != null) {
            for(ApplicationCommandResultInfo applicationCommandResultInfo : applicationCommandResultInfos) {
                applicationCommandResultListener.onResultAvailable(
                        applicationCommandResultInfo.requestToken,
                        applicationCommandResultInfo.result);
            }

            applicationCommandResultInfos.clear();
        }
    }

    public synchronized void setProgressListener(ProgressListener progressListener) {
        if(isInProgress && this.progressListener != null) {
            this.progressListener.onProgressFinished();
        }

        this.progressListener = progressListener;

        if(progressListener != null && isInProgress) {
            progressListener.onProgressStarted();
        }
    }

    private synchronized void notifyApplicationCommandResultAvailable(String requestToken, Object result) {
        if(applicationCommandResultListener != null) {
            applicationCommandResultListener.onResultAvailable(requestToken, result);
        } else {
            ApplicationCommandResultInfo applicationCommandResultInfo = new ApplicationCommandResultInfo();
            applicationCommandResultInfo.requestToken = requestToken;
            applicationCommandResultInfo.result = result;
            applicationCommandResultInfos.add(applicationCommandResultInfo);
        }
    }

    private synchronized void notifyProgressStarted() {
        isInProgress = true;
        if(progressListener != null) {
            progressListener.onProgressStarted();
        }
    }

    private synchronized void notifyProgressFinished() {
        isInProgress = false;
        if(progressListener != null) {
            progressListener.onProgressFinished();
        }
    }

    private static class ApplicationCommandResultInfo {
        public String requestToken;
        public Object result;
    }

    public static interface ProgressListener {
        void onProgressStarted();
        void onProgressFinished();
    }
}
