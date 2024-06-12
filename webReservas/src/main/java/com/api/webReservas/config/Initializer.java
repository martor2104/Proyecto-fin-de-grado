package com.api.webReservas.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.api.webReservas.entity.Role;
import com.api.webReservas.entity.User;
import com.api.webReservas.repository.UserRepository;

import java.util.Optional;

@Component
public class Initializer implements CommandLineRunner {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {

		/*
		User user1 = new User();

		user1.setEmail("roberto.marqueztorres@gmail.com");
		user1.setName("Roberto");
		user1.setPassword(passwordEncoder.encode("1234"));
		user1.setRole(Role.ADMIN);
*/


		//System.out.println("Encoded password: " + user1.getPassword());


	}

}
