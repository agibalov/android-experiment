package me.loki2302.activities;

public class Credentials {
	final String email;
	final String password;
	
	public Credentials(String email, String password) {
		this.email = email;
		this.password = password;					
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}
}