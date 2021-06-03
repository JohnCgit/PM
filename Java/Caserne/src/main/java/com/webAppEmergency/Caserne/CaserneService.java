package com.webAppEmergency.Caserne;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.project.model.dto.VehicleType;

@Service
public class CaserneService {

	@Autowired
	CaserneRepository cRepo;
	
	private RestTemplate restTemplate;
	public String url;
//////////////////////////////////////
// Rest template
//////////////////////////////////////
		
	public CaserneService(RestTemplateBuilder restTemplateBuilder) { // Gestion du rest template
        this.restTemplate = restTemplateBuilder.build();
        this.url="https://download.data.grandlyon.com/wfs/grandlyon?SERVICE=WFS&VERSION=2.0.0&request=GetFeature&typename=adr_voie_lieu.adrsecourspct&outputFormat=application/json;%20subtype=geojson&SRSNAME=EPSG:4171&startIndex=0&count=100";
    }
		
//////////////////////////////////////
// get Vehicule
//////////////////////////////////////

	public List<Caserne> getAll() {
		return cRepo.findAll();
	}
	
	public Caserne getCaserne(int realid) {
		Caserne res=null;
		Optional<Caserne> oCaserne = cRepo.findById(realid);
		if (oCaserne.isPresent()){res=oCaserne.get();}
		return res;
	}
	
	
	//Récupérer informations Casernes de Lyon
	public void getCaserneLyon() {
		List<String> json = this.restTemplate.getForObject(this.url,List.class);
		System.out.println(json);
	}

}