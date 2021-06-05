package com.webAppEmergency.Vehicule;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.webAppEmergency.Vehicule.Vehicule;

//TODO Create method removeCard from collection 

@RestController
public class VehiculeRestCrt {
	
	@Autowired
	VehiculeService vService;

//////////////////////////////////////
// Get Vehicule
//////////////////////////////////////
	
	@RequestMapping(method=RequestMethod.GET, value="/get/{id}")
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
		System.out.println(v);
		return vService.createVehicule(v);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/delete/{id}")
	public void delete(@PathVariable int id) {
		vService.deleteVehicule(id);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/move/{id}")
	public void move(@PathVariable int id, @RequestParam double lon, @RequestParam double lat) {
		vService.moveVehicule(id, lon, lat);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/followPath/{id}")
	public boolean followPath(@PathVariable int id) {
		return vService.followPath(id);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/setPath/{id}")
	public void setPath(@PathVariable int id, @RequestBody ArrayList<ArrayList<Double>> newPath) {
		vService.setPath(id, newPath);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/state/{id}")
	public void etatVehicule(@PathVariable int id, @RequestParam Etat state) {
		vService.etatVehicule(id, state);
	}
}