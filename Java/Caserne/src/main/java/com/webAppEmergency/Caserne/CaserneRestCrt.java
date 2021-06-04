package com.webAppEmergency.Caserne;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.project.model.dto.Coord;

import com.webAppEmergency.Caserne.CaserneService;

//TODO Create method removeCard from collection 

@RestController
public class CaserneRestCrt {
	
	@Autowired
	CaserneService cService;

//////////////////////////////////////
// Get Caserne
//////////////////////////////////////
	
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
	
	@RequestMapping(method=RequestMethod.GET, value="/getLyon", produces="application/json")
	public void getCaserneLyon() throws JsonMappingException, JsonProcessingException{
		cService.getCaserneLyon();
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/init")
	public void initCaserne(){
		cService.initCaserne();
	}
	
}