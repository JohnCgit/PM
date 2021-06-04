package com.webAppEmergency;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.webAppEmergency.Caserne.Caserne;
import com.webAppEmergency.Caserne.CaserneService;

@SpringBootApplication
public class AppCaserne {
	
	@Autowired
	static CaserneService cService;
	
	public static void main(String [] args) {
		SpringApplication.run(AppCaserne.class, args);
		List<Caserne> ListC = new ArrayList<>();
		ListC.add(new Caserne(1, 50, 50, "oiui", Arrays.asList(), Arrays.asList(1, 2, 3)));
		cService.initCaserne(ListC);
	}
}


