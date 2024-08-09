package com.binary.spring.security.dto;

import jakarta.validation.constraints.NotEmpty;

public class SignInRequest {
	
	@NotEmpty
	private String userName;
	@NotEmpty
	private String password;
	
	
	
	public SignInRequest(@NotEmpty String userName, @NotEmpty String password) {
		super();
		this.userName = userName;
		this.password = password;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	
	
	
}
