package com.webAppEmergency.FireStation;

import java.io.IOException;
import java.util.List;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


//TODO Create method removeCard from collection 

@RestController
public class FireStationRestCrt {
	
	@Autowired
	FireStationService cService;
	
	@RequestMapping(method=RequestMethod.GET, value="/{id}")
	public FireStation getCaserne(@PathVariable int id){
		FireStation c=cService.getFireStation(Integer.valueOf(id));
	    return c;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/getAll")
	public List<FireStation> getAll(){
		return cService.getAll();
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/add")
	public void addCaserne(@RequestBody FireStation f){
		if(f.getLat() != 0 && f.getLon() !=0) {
			if(f.getLibelle() == null) {
				f.setLibelle("Nouvelle caserne");
			}
			cService.addFireStation(f);
		}
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/addVehicle/{facilityID}/{VehiculeID}")
	public void addVehicule(@PathVariable int facilityID, @PathVariable int VehicleID) {
		cService.addVehicleWFireStationID(facilityID, VehicleID);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/initLyon")
	public void initFireStationLyon() throws IOException, ParseException{
		cService.initFireStationLyon();
	}

}