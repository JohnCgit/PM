package com.webAppEmergency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.webAppEmergency.Assignation.MainRunnable;

@SpringBootApplication
public class AppAssignation {
	public static void main(String [] args) {
		SpringApplication.run(AppAssignation.class, args);
		MainRunnable mRunnable;
		Thread displayThread;
		
		//Create a Runnable is charge of executing cyclic actions 
		mRunnable=new MainRunnable();
		
		// A Runnable is held by a Thread which manage lifecycle of the Runnable
		displayThread=new Thread(mRunnable);
		
		// The Thread is started and the method run() of the associated DisplayRunnable is launch
		displayThread.start();

	}
}