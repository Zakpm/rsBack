package com.rootsshivasou.contact;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EntityScan("com.rootsshivasou.moduleCommun.model")
@ComponentScan(basePackages = {"com.rootsshivasou.moduleCommun", "com.rootsshivasou.contact"})
@EnableTransactionManagement
@EnableWebSocket
public class ContactApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContactApplication.class, args);
	}
}
