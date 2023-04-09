package com.smart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SMartApplication {

	public static void main(String[] args) {
		SpringApplication.run(SMartApplication.class, args);
	}

}
