package com.webAppEmergency.Assignation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssignationRestCrt {
	
	@Autowired
	AssignationService aServ;
	
	@RequestMapping(method=RequestMethod.POST, value="/Start")
	public void Start() {
		aServ.Start();
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/Stop")
	public void Stop() {
		aServ.Stop();
	}
}
