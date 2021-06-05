package com.webAppEmergency.Caserne;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CaserneService {

	@Autowired
	CaserneRepository cRepo;
	ObjectMapper mapper;

	
	private RestTemplate restTemplate;
	public String url;
	public String path;

		
	public CaserneService(RestTemplateBuilder restTemplateBuilder){ // Gestion du rest template
        this.restTemplate = restTemplateBuilder.build();
        this.url="https://download.data.grandlyon.com/wfs/grandlyon?SERVICE=WFS&VERSION=2.0.0&request=GetFeature&typename=adr_voie_lieu.adrsecourspct&outputFormat=application/json;subtype=geojson&SRSNAME=EPSG:4171&startIndex=0&count=100";
        //this.path ="/home/tp/git/PM/Java/Caserne/src/main/java/com/webAppEmergency/Caserne/grandlyon.json";
    	this.path="C:\\Users\\tperr\\git\\PM\\Java\\Caserne\\src\\main\\java\\com\\webAppEmergency\\Caserne\\grandlyon.json";
        mapper = new ObjectMapper();
	}

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
	
	public void initCaserneLyon() throws IOException, ParseException {
		List<Caserne> ListC = new ArrayList<>();
		for (int i = 0; i < 30; i++) {
		    JSONParser jsonP = new JSONParser();
	        JSONObject jsonO = (JSONObject)jsonP.parse(new FileReader(this.path));
	        JSONArray features = (JSONArray) jsonO.get("features");
	        JSONObject index = (JSONObject) features.get(i);
	        JSONObject properties = (JSONObject) index.get("properties");
	        String name = (String) properties.get("nom");
	        JSONObject geometry = (JSONObject) index.get("geometry");
	        JSONArray coordinates = (JSONArray) geometry.get("coordinates");
	        double lon = (double) coordinates.get(0);
	        double lat = (double) coordinates.get(1);
	        System.out.println(name);
	        System.out.println("Coordonnées : (" + lon + ","+ lat +")");
	        ListC.add(new Caserne(lon, lat, name, Arrays.asList(), Arrays.asList(), 15));
			for (Caserne c: ListC) {cRepo.save(c);}
		}
	}
}