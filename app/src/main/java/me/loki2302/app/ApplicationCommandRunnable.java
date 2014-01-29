package me.loki2302.app;

import me.loki2302.app.commands.ApplicationCommand;

public class ApplicationCommandRunnable implements Runnable {
    private final App app;
    private final ApplicationCommand applicationCommand;

    public ApplicationCommandRunnable(App app, ApplicationCommand applicationCommand) {
        this.app = app;
        this.applicationCommand = applicationCommand;
    }

    @Override
    public void run() {
        applicationCommand.run(app);
    }
}
