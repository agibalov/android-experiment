package me.loki2302.dal;

public interface LongOperationListener {
	void onLongOperationStarted(String message);
	void onLongOperationFinished();
}