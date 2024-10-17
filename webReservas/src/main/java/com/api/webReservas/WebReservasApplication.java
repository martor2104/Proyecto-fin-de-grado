package com.api.webReservas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.api.webReservas")
@EnableJpaRepositories(basePackages = "com.api.webReservas.repository")
public class WebReservasApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebReservasApplication.class, args);
	}

}
