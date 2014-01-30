package me.loki2302.infrastructure;

import com.google.inject.Inject;

import me.loki2302.app.App;
import me.loki2302.app.ApplicationCommandResultListener;
import me.loki2302.app.commands.ApplicationCommand;

public abstract class BaseActivity<TContext> extends RoboActionBarActivity {
    @Inject
    private App app;

    @Inject
    private RequestSubscriptionService requestSubscriptionService;

    @Override
    protected void onPause() {
        super.onPause();
        app.setApplicationCommandResultListener(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        app.setApplicationCommandResultListener(new ApplicationCommandResultListener() {
            @Override
            public void onResultAvailable(final String requestToken, final Object result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        requestSubscriptionService.handleResult(getActivityId(), requestToken, BaseActivity.this, result);
                    }
                });
            }
        });
    }

    protected <TResult> void submit(
            ApplicationCommand<TResult> applicationCommand,
            ContextAwareApplicationCommandResultListener<TContext, TResult> contextAwareApplicationCommandResultListener) {
        String requestToken = app.submit(applicationCommand);
        requestSubscriptionService.addSubscription(getActivityId(), requestToken, contextAwareApplicationCommandResultListener);
    }

    protected abstract String getActivityId();
}
