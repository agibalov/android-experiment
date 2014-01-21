package me.retask.service;

import android.content.ContentResolver;
import android.content.Intent;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import me.retask.service.handlers.CreateTaskRetaskServiceRequestHandler;
import me.retask.service.handlers.DeleteTaskRetaskServiceRequestHandler;
import me.retask.service.handlers.ProgressTaskRetaskServiceRequestHandler;
import me.retask.service.handlers.RetaskServiceRequestHandler;
import me.retask.service.handlers.SignInRetaskServiceRequestHandler;
import me.retask.service.handlers.SignUpRetaskServiceRequestHandler;
import me.retask.service.handlers.UnprogressTaskRetaskServiceRequestHandler;
import me.retask.service.handlers.UpdateTaskRetaskServiceRequestHandler;
import me.retask.service.requests.CreateTaskRetaskServiceRequest;
import me.retask.service.requests.DeleteTaskRetaskServiceRequest;
import me.retask.service.requests.ProgressTaskRetaskServiceRequest;
import me.retask.service.requests.RetaskServiceRequest;
import me.retask.service.requests.SignInRetaskServiceRequest;
import me.retask.service.requests.SignUpRetaskServiceRequest;
import me.retask.service.requests.UnprogressTaskRetaskServiceRequest;
import me.retask.service.requests.UpdateTaskRetaskServiceRequest;
import roboguice.service.RoboIntentService;

public class RetaskService extends RoboIntentService {
    public final Map<String, RetaskServiceRequestHandler> handlersMap = new HashMap<String, RetaskServiceRequestHandler>();

    @Inject
    @Named("apiRootUrl")
    private String apiRootUrl;

    @Inject
    private RestTemplate restTemplate;

    @Inject
    private RequestExecutor requestExecutor;

    public RetaskService() {
        super("RetaskService");

        handlersMap.put(SignInRetaskServiceRequest.COMMAND, new SignInRetaskServiceRequestHandler());
        handlersMap.put(SignUpRetaskServiceRequest.COMMAND, new SignUpRetaskServiceRequestHandler());
        handlersMap.put(CreateTaskRetaskServiceRequest.COMMAND, new CreateTaskRetaskServiceRequestHandler());
        handlersMap.put(UpdateTaskRetaskServiceRequest.COMMAND, new UpdateTaskRetaskServiceRequestHandler());
        handlersMap.put(ProgressTaskRetaskServiceRequest.COMMAND, new ProgressTaskRetaskServiceRequestHandler());
        handlersMap.put(UnprogressTaskRetaskServiceRequest.COMMAND, new UnprogressTaskRetaskServiceRequestHandler());
        handlersMap.put(DeleteTaskRetaskServiceRequest.COMMAND, new DeleteTaskRetaskServiceRequestHandler());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String command = intent.getStringExtra(RetaskServiceRequest.EXTRA_COMMAND);
        if(command == null || command.equals("")) {
            throw new IllegalArgumentException("Command has not been provided");
        }

        if(!handlersMap.containsKey(command)) {
            throw new IllegalStateException("Unknown command " + command);
        }

        RetaskServiceRequestHandler handler = handlersMap.get(command);
        ContentResolver contentResolver = getContentResolver();
        handler.process(intent, contentResolver, apiRootUrl, restTemplate);

        String requestToken = intent.getStringExtra("requestToken");
        requestExecutor.notifyRequestDone(requestToken);
    }
}
