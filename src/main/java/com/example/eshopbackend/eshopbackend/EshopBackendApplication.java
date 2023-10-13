package com.example.eshopbackend.eshopbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity(prePostEnabled = true)
public class EshopBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EshopBackendApplication.class, args);
	}

}
