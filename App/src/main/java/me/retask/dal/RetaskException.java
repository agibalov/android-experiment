package me.retask.dal;

import me.retask.dal.dto.ServiceResultDto;

public class RetaskException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public final ServiceResultDto<?> serviceResult;
	
	public RetaskException(ServiceResultDto<?> serviceResult) {
		this.serviceResult = serviceResult;
	}
}