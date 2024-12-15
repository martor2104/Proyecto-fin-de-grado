package com.api.webReservas.service;

import org.springframework.http.ResponseEntity;

import com.api.webReservas.auth.LoginRequest;
import com.api.webReservas.auth.RegisterRequest;
import com.api.webReservas.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

		ResponseEntity<?> login(LoginRequest request);
	    ResponseEntity<?> register(User userDetails, RegisterRequest user);	

}
