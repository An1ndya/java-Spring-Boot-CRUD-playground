package com.example.cruddemo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.cruddemo.example.LoggingDemo;
import com.example.cruddemo.util.AppLogger;

@SpringBootApplication
public class CrudDemoApplication {

	@Autowired
	private Environment environment;

	public static void main(String[] args) {
		SpringApplication.run(CrudDemoApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner demoEnvironment() {
		return args -> {
			// Log the active profiles
			String[] activeProfiles = environment.getActiveProfiles();
			if (activeProfiles.length == 0) {
				AppLogger.log1Info("Running with default profile (no active profile set)");
			} else {
				AppLogger.log1Info("Active Profiles: " + String.join(", ", activeProfiles));
			}
			
			// Optional: Add a log to indicate the application is ready
			AppLogger.log1Info("CrudDemoApplication is fully initialized and ready.");
		};
	}
}
