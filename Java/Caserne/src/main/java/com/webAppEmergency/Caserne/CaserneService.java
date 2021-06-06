package com.webAppEmergency.Caserne;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

		
	public CaserneService(RestTemplateBuilder restTemplateBuilder){
        this.restTemplate = restTemplateBuilder.build();
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("[CASERNE-INIT] Current relative path is: " + s);
        this.path = "src/main/java/com/webAppEmergency/Caserne/grandlyon.json";
//    	this.path="src\\main\\java\\com\\webAppEmergency\\Caserne\\grandlyon.json";
        
        this.url="https://download.data.grandlyon.com/wfs/grandlyon?SERVICE=WFS&VERSION=2.0.0&request=GetFeature&typename=adr_voie_lieu.adrsecourspct&outputFormat=application/json;subtype=geojson&SRSNAME=EPSG:4171&startIndex=0&count=100";
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
	        System.out.println("[CASERNE-INITLYON] libelle"+name);
	        System.out.println("[CASERNE-INITLYON] Coordonnées : (" + lon + ","+ lat +")");
	        Caserne c = new Caserne(lon, lat, name, Arrays.asList(), Arrays.asList(), 15);
			cRepo.save(c);	        
	        ListC.add(c);
		}
		for (Caserne c: ListC) {initVehicule(c);cRepo.save(c);}
	}
	
	@SuppressWarnings("unchecked")
	public void initVehicule(Caserne c) {
		JSONObject body = new JSONObject();
		body.put("type", "TRUCK");
		body.put("efficiency", 2.0);
		body.put("liquideQuantity", 12.0);
		body.put("fuel", 42.0);
		body.put("crewMember", 1);
		body.put("facilityRefID", c.getId());
		
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = new HttpEntity<String>(body.toString(), headers);

		Vehicule v = this.restTemplate.postForObject("http://127.0.0.1:8070/fcreate", request, Vehicule.class);

		addVehiculeWCaserne(c, v.getRealid());
	}
		
	public void addVehiculeWCaserne(Caserne c, int VehiculeID) {
		System.out.println("[CASERNE-AddV2C] adding to"+c);
		List<Integer> ListVehicule = c.getListVehicules();
		if (ListVehicule.isEmpty()) {
			ListVehicule = new ArrayList<Integer>(List.of(VehiculeID));
			System.out.println(ListVehicule);
		}
		else {
			ListVehicule.add(VehiculeID);		
		}
		c.setListVehicules(ListVehicule);
		this.restTemplate.put("http://127.0.0.1:8070/move/"+VehiculeID+"?lon="+c.getLon()+"&lat="+c.getLat(), null);
		cRepo.save(c);
	}
	
	public void addVehiculeWCaserneID(int CaserneID, int VehiculeID) {
		Caserne c = getCaserne(CaserneID);
		List<Integer> ListVehicule = c.getListVehicules();
		if (ListVehicule.isEmpty()) {
			ListVehicule = new ArrayList<Integer>(List.of(VehiculeID));
			System.out.println(ListVehicule);
		}
		else {
			System.out.println(!ListVehicule.contains(VehiculeID));
			if (!ListVehicule.contains(VehiculeID)) {
				ListVehicule.add(VehiculeID);					
			}	
		}
		c.setListVehicules(ListVehicule);
		this.restTemplate.put("http://127.0.0.1:8070/move/"+VehiculeID+"?lon="+c.getLon()+"&lat="+c.getLat(), null);
		cRepo.save(c);
	}
}
