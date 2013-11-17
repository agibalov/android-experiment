package me.loki2302.dal;

public interface ApiCallback<TResult> {
	void onSuccess(TResult result);
	void onError();
}