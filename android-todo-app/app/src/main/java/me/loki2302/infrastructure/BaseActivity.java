package me.loki2302.infrastructure;

import android.app.ProgressDialog;

import com.google.inject.Inject;

import me.loki2302.app.App;
import me.loki2302.app.ApplicationCommandResultListener;
import me.loki2302.app.commands.ApplicationCommand;

public abstract class BaseActivity<TContext> extends RoboActionBarActivity implements App.ProgressListener {
    @Inject
    private App app;

    @Inject
    private RequestSubscriptionService requestSubscriptionService;

    private ProgressDialog progressDialog;

    @Override
    protected void onPause() {
        super.onPause();
        app.setApplicationCommandResultListener(null);
        app.setProgressListener(null);
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
        app.setProgressListener(this);
    }

    @Override
    public void onProgressStarted() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog = new ProgressDialog(BaseActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        });
    }

    @Override
    public void onProgressFinished() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                progressDialog = null;
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
