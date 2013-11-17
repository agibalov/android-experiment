package me.loki2302.dal.apicalls;

import me.loki2302.dal.ApiCall;
import me.loki2302.dal.RetaskApi;
import me.loki2302.dal.dto.ServiceResultDto;

public class SignUpApiCall implements ApiCall<Object> {
	private final String email;
	private final String password;
	
	public SignUpApiCall(String email, String password) {
		this.email = email;
		this.password = password;
	}

	@Override
	public ServiceResultDto<Object> performApiCall(RetaskApi retaskApi) {
		return retaskApi.signUp(email, password);
	}
	
	@Override
	public String describe() {
		return "Signing up...";
	}
}