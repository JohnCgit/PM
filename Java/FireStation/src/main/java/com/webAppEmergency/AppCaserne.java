package com.webAppEmergency;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.webAppEmergency.FireStation.FireStation;
import com.webAppEmergency.FireStation.FireStationService;

@SpringBootApplication
public class AppCaserne {
	
	@Autowired
	static FireStationService cService;
	
	public static void main(String [] args) {
		SpringApplication.run(AppCaserne.class, args);
	}
}


