package com.tests.DatabaseTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DatabaseTestApplication {

	@Autowired
	OutputService outputService;

	public static void main(String[] args) {
		SpringApplication.run(DatabaseTestApplication.class, args);
	}

	@Bean
	public CommandLineRunner run() {
		return this.outputService::run;
	}

}

