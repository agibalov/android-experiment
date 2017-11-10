package me.loki2302.app;

public interface ApplicationCommandResultListener {
    void onResultAvailable(String requestToken, Object result);
}
