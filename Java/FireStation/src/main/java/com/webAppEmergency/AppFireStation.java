package com.webAppEmergency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.webAppEmergency.FireStation.FireStationService;

@SpringBootApplication
public class AppFireStation {
	
	@Autowired
	static FireStationService cService;
	
	public static void main(String [] args) {
		SpringApplication.run(AppFireStation.class, args);
	}
}


