package com.tests.DatabaseInitializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DatabaseInitializerApplication {

	@Autowired
	Outputservice outputservice;

	public static void main(String[] args) {
		SpringApplication.run(DatabaseInitializerApplication.class, args);
	}

	@Bean
	public CommandLineRunner run() {
		return this.outputservice::run;
	}

}

