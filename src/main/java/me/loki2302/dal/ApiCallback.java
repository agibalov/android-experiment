package me.loki2302.dal;

import me.loki2302.dal.dto.ServiceResultDto;

public interface ApiCallback<TResult> {
	void onSuccess(TResult result);
	void onError(ServiceResultDto<TResult> response, Exception e);
}