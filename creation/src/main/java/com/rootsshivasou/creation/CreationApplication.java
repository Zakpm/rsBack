package com.rootsshivasou.creation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
@EntityScan("com.rootsshivasou.moduleCommun.model")
@ComponentScan(basePackages = {"com.rootsshivasou.moduleCommun", "com.rootsshivasou.creation"})
public class CreationApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreationApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
	}
}
