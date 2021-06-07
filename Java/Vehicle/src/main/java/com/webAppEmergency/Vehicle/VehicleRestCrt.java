package com.webAppEmergency.Vehicle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class VehicleRestCrt {
	
	@Autowired
	VehicleService vService;

//////////////////////////////////////
// Get Vehicule
//////////////////////////////////////
	
	@RequestMapping(method=RequestMethod.GET, value="/get/{id}")
	public Vehicle getUserById(@PathVariable int id){
		Vehicle v=vService.getVehicle(Integer.valueOf(id));
	    return v;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/getAll")
	public List<Vehicle> getAll(){
		return vService.getAll();
	}
	
//////////////////////////////////////
// Create/Update Vehicule
//////////////////////////////////////
	
	@RequestMapping(method=RequestMethod.POST, value="/fcreate")
	public Vehicle createVehicleViaFireStation(@RequestBody Vehicle v) {
		System.out.println(v);
		return vService.createVehicleViaFireStation(v);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/create")
	public Vehicle createVehicle(@RequestBody Vehicle v) {
		System.out.println(v);
		return vService.createVehicle(v);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/delete/{id}")
	public void delete(@PathVariable int id) {
		vService.deleteVehicle(id);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/move/{id}")
	public void move(@PathVariable int id, @RequestParam double lon, @RequestParam double lat) {
		vService.moveVehicle(id, lon, lat);
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
	public void etatVehicle(@PathVariable int id, @RequestParam State state) {
		vService.etatVehicle(id, state);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/setfireStationID/{vid}/{cid}")
	public void etatVehicle(@PathVariable int vehicleID, @PathVariable int fireStationID) {
		vService.facilityVehicle(vehicleID, fireStationID);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/giveFire/{VehiculeID}/{idFire}")
	public void giveFire(@PathVariable int VehiculeID, @PathVariable int idFire) {
		vService.giveFire(VehiculeID,idFire);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/update/{id}")
	public void update(@PathVariable int id, @RequestBody String content) throws IOException {
		vService.update(id, content);
	}
}