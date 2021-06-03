package com.webAppEmergency.Vehicule;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//TODO Create method removeCard from collection 

@RestController
public class VehiculeRestCrt {
	
	@Autowired
	VehiculeService vService;

//////////////////////////////////////
// Get Vehicule
//////////////////////////////////////
	
	@RequestMapping(method=RequestMethod.GET, value="/id/{id}")
	public Vehicule getUserById(@PathVariable int id){
		Vehicule v=vService.getVehicule(Integer.valueOf(id));
	    return v;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/getAll")
	public List<Vehicule> getAll(){
		return vService.getAll();
	}
	
//////////////////////////////////////
// Create/Update Vehicule
//////////////////////////////////////
	
	@RequestMapping(method=RequestMethod.POST, value="/create")
	public boolean createVehicule(@RequestBody Vehicule v) {
		return vService.createVehicule(v);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/update/{id}")
	public void update(@PathVariable int id, @RequestBody JSONObject json) {
		vService.updateVehicule(id, json);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/delete/{id}")
	public void delete(@PathVariable int id) {
		vService.deleteVehicule(id);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/move/{id}")
	public void move(@PathVariable int id, @RequestParam float lon, @RequestParam float lat) {
		vService.moveVehicule(id, lon, lat);
	}

}