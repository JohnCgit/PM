package com.webAppEmergency.Caserne;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;


//TODO Create method removeCard from collection 

@RestController
public class CaserneRestCrt {
	
	@Autowired
	CaserneService cService;
	
	@RequestMapping(method=RequestMethod.GET, value="/{id}")
	public Caserne getCaserne(@PathVariable int id){
		Caserne c=cService.getCaserne(Integer.valueOf(id));
	    return c;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/getAll")
	public List<Caserne> getAll(){
		return cService.getAll();
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/add")
	public void addCaserne(@RequestBody Caserne c){
		if(c.getLat() != 0 && c.getLon() !=0) {
			if(c.getLibelle() == null) {
				c.setLibelle("Nouvelle caserne");
			}
			cService.addCaserne(c);
		}
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/addVehicule/{facilityID}/{VehiculeID}")
	public void addVehicule(@PathVariable int facilityID, @PathVariable int VehiculeID) {
		cService.addVehicule(facilityID, VehiculeID);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/initLyon")
	public void initCaserneLyon() throws IOException, ParseException{
		cService.initCaserneLyon();
	}

}