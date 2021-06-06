package com.webAppEmergency.Fire;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.model.dto.FireDto;

@RestController
public class FireRestCrt {

	@Autowired
	FireService fServ;
	
	@RequestMapping(method=RequestMethod.GET, value="/getAll")
	public List<FireDto> getAllFire() {
		return fServ.getAllFire();
	} 
	
	@RequestMapping(method=RequestMethod.GET, value="/get/{id}")
	public FireDto getFire(@PathVariable int id) {
		return fServ.getFire(id);
	} 
	
	@RequestMapping(method=RequestMethod.POST, value="/startOneFire")
	public void startOneFire() throws InterruptedException, IOException {
		fServ.startOneFire();
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/startFire")
	public void startFire() {
		fServ.startFire();
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/freqFire/{freq}")
	public void freqFire(@PathVariable int freq) {
		fServ.freqFire(freq);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/probFire/{prob}")
	public void probFire(@PathVariable double prob) {
		fServ.probFire(prob);
	}
}
