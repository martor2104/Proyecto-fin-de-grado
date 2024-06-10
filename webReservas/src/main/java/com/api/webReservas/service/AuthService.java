package com.api.webReservas.service;

import org.springframework.http.ResponseEntity;

import com.api.webReservas.auth.LoginRequest;
import com.api.webReservas.auth.RegisterRequest;
import com.api.webReservas.entity.User;

public interface AuthService {

		ResponseEntity<?> login(LoginRequest request);
	    ResponseEntity<?> register(User userDetails, RegisterRequest user);	

}
