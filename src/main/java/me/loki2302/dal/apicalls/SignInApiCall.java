package me.loki2302.dal.apicalls;

import me.loki2302.dal.ApiCall;
import me.loki2302.dal.RetaskApi;
import me.loki2302.dal.dto.ServiceResultDto;
import me.loki2302.dal.dto.SessionDto;

public class SignInApiCall implements ApiCall<SessionDto> {
	private final String email;
	private final String password;
	
	public SignInApiCall(String email, String password) {
		this.email = email;
		this.password = password;
	}

	@Override
	public ServiceResultDto<SessionDto> performApiCall(RetaskApi retaskApi) {
		return retaskApi.signIn(email, password);
	}

	@Override
	public String describe() {
		return "Signing in...";
	}
}