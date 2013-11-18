package me.loki2302.dal;

public interface ApplicationServiceCallback<TResult> {
	void onSuccess(TResult result);
	void onError();
}