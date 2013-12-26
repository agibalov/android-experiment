package me.loki2302.dal;

import me.loki2302.dal.dto.ServiceResultDto;

public class RetaskException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public final ServiceResultDto<?> serviceResult;
	
	public RetaskException(ServiceResultDto<?> serviceResult) {
		this.serviceResult = serviceResult;
	}
}