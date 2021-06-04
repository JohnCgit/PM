package com.webAppEmergency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.webAppEmergency.Caserne.CaserneService;

@SpringBootApplication
public class AppCaserne {
	
	@Autowired
	static CaserneService cService;
	
	public static void main(String [] args) {
		SpringApplication.run(AppCaserne.class, args);
		cService.getCaserneLyon();
	}
}