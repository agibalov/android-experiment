package me.loki2302.dal;

import me.loki2302.dal.dto.ServiceResultDto;

public interface ApiCall<TResult> {
	String describe();
	ServiceResultDto<TResult> performApiCall(RetaskApi retaskApi);
}