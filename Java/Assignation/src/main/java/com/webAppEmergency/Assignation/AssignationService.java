package com.webAppEmergency.Assignation;

import org.springframework.stereotype.Service;

@Service
public class AssignationService {

	private MainRunnable mainRunnable;
	private Thread mainThread;
	
	private MoveRunnable moveRunnable;
	private Thread moveThread;
	
	public void Start() {
		StartMain();
		StartMove();
	}
	
	public void StartMain() {
		//Create a Runnable is charge of executing cyclic actions 
		this.mainRunnable=new MainRunnable();
		
		// A Runnable is held by a Thread which manage lifecycle of the Runnable
		this.mainThread=new Thread(this.mainRunnable);
		
		// The Thread is started and the method run() of the associated DisplayRunnable is launch
		this.mainThread.start();
	}

	public void StartMove() {	
		//Create a Runnable is charge of executing cyclic actions 
		this.moveRunnable=new MoveRunnable();
		
		// A Runnable is held by a Thread which manage lifecycle of the Runnable
		this.moveThread=new Thread(this.moveRunnable);
		
		// The Thread is started and the method run() of the associated DisplayRunnable is launch
		this.moveThread.start();
	}
	
	public void Stop() {
		this.mainRunnable.stop();
		this.moveRunnable.stop();
	}
}
