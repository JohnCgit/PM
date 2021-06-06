package com.webAppEmergency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.webAppEmergency.Assignation.MainRunnable;
import com.webAppEmergency.Assignation.MoveRunnable;

@SpringBootApplication
public class AppAssignation {
	public static void main(String [] args) {
		SpringApplication.run(AppAssignation.class, args);
	}
}