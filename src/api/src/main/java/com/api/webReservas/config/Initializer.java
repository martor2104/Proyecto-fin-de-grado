package com.api.webReservas.config;

import com.api.webReservas.entity.Table;
import com.api.webReservas.entity.TableStatus;
import com.api.webReservas.repository.TableRepository;
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

	@Autowired
	private TableRepository tableRepository;

	@Override
	public void run(String... args) throws Exception {
/*
		System.out.println("Initializing the user...");
		User user = new User();

		user.setName("roberto");
		user.setRole(Role.ADMIN);
		user.setPassword(passwordEncoder.encode("1234"));
		user.setEmail("roberto.marqueztorres@gmail.com");

		userRepository.save(user);
*/
	}

}
