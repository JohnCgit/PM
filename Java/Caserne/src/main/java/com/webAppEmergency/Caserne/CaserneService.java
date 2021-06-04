package com.webAppEmergency.Caserne;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.project.model.dto.Coord;

import com.project.model.dto.VehicleType;
import com.webAppEmergency.Caserne.CaserneDto.CaserneDto;

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
	
	public void addCaserne(Caserne c) {
	  	cRepo.save(c);
	}
	
	public void initCaserne() {
		List<Caserne> ListC = new ArrayList<>();
		ListC.add(new Caserne(4.855997011435769, 45.8295890450427, "Centre d'Intervention des 2 Fontaines", Arrays.asList(), Arrays.asList(1, 2, 3), 15));
		ListC.add(new Caserne(4.819356307585479, 45.89601102593775, "Centre d'Intervention de Genay-Neuville", Arrays.asList(), Arrays.asList(4, 5), 10));
		ListC.add(new Caserne(4.715254999410806, 45.824827365743985, "Centre d'Intervention de La Tour-de-Salvagny", Arrays.asList(), Arrays.asList(6, 7, 8, 9), 12));
		for (Caserne c: ListC) {cRepo.save(c);}

	}
	
	//Récupérer informations Casernes de Lyon
	public void getCaserneLyon() {
		CaserneDto json = this.restTemplate.getForObject(this.url,CaserneDto.class);
		System.out.println(json);
	}



}