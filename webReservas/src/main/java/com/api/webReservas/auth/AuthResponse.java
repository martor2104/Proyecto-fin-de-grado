package com.api.webReservas.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    String token;

	public AuthResponse(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
    
    
}
