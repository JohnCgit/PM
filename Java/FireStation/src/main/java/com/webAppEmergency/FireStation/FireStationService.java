package com.webAppEmergency.FireStation;

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
public class FireStationService {

	@Autowired
	FireStationRepository cRepo;
	ObjectMapper mapper;

	
	private RestTemplate restTemplate;
	public String url;
	public String path;

		
	public FireStationService(RestTemplateBuilder restTemplateBuilder){
        this.restTemplate = restTemplateBuilder.build();
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("[FIRESTATION-INIT] Current relative path is: " + s);
        this.path = "src/main/java/com/webAppEmergency/FireStation/grandlyon.json";
//    	this.path="src\\main\\java\\com\\webAppEmergency\\Caserne\\grandlyon.json";
        
        this.url="https://download.data.grandlyon.com/wfs/grandlyon?SERVICE=WFS&VERSION=2.0.0&request=GetFeature&typename=adr_voie_lieu.adrsecourspct&outputFormat=application/json;subtype=geojson&SRSNAME=EPSG:4171&startIndex=0&count=100";
        mapper = new ObjectMapper();
	}

	public List<FireStation> getAll() {
		return cRepo.findAll();
	}
	
	public FireStation getFireStation(int realid) {
		FireStation res=null;
		Optional<FireStation> oFS = cRepo.findById(realid);
		if (oFS.isPresent()){res=oFS.get();}
		return res;
	}
	
	public void addFireStation(FireStation c) {
	  	cRepo.save(c);
	}
	
	public void initFireStationLyon() throws IOException, ParseException {
		List<FireStation> ListC = new ArrayList<>();
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
	        System.out.println("[FIRESTATION-INITLYON] libelle"+name);
	        System.out.println("[FIRESTATION-INITLYON] Coordonnées : (" + lon + ","+ lat +")");
	        FireStation c = new FireStation(lon, lat, name, Arrays.asList(), Arrays.asList(), 15);
			cRepo.save(c);	        
	        ListC.add(c);
		}
		for (FireStation c: ListC) {initVehicle(c);cRepo.save(c);}
	}
	
	@SuppressWarnings("unchecked")
	public void initVehicle(FireStation f) {
		JSONObject body = new JSONObject();
		body.put("type", "CAR");
		body.put("fireStationID", f.getId());
		
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = new HttpEntity<String>(body.toString(), headers);
		System.out.println("[CASERNE-INITVEHICULE] vehicule created with : "+body);
		Vehicle v = this.restTemplate.postForObject("http://127.0.0.1:8070/fcreate", request, Vehicle.class);

		addVehicleWFireStation(f, v.getId());
	}
		
	public void addVehicleWFireStation(FireStation c, int VehicleID) {
		System.out.println("[FIRESTATION-AddV2C] adding to"+c);
		List<Integer> ListVehicle = c.getListVehicles();
		if (ListVehicle.isEmpty()) {
			ListVehicle = new ArrayList<Integer>(List.of(VehicleID));
			System.out.println(ListVehicle);
		}
		else {
			ListVehicle.add(VehicleID);		
		}
		c.setListVehicles(ListVehicle);
		this.restTemplate.put("http://127.0.0.1:8070/move/"+VehicleID+"?lon="+c.getLon()+"&lat="+c.getLat(), null);
		cRepo.save(c);
	}
	
	public void addVehicleWFireStationID(int FireStationID, int VehicleID) {
		FireStation c = getFireStation(FireStationID);
		List<Integer> ListVehicle = c.getListVehicles();
		if (ListVehicle.isEmpty()) {
			ListVehicle = new ArrayList<Integer>(List.of(VehicleID));
			System.out.println(ListVehicle);
		}
		else {
			System.out.println(!ListVehicle.contains(VehicleID));
			if (!ListVehicle.contains(VehicleID)) {
				ListVehicle.add(VehicleID);					
			}	
		}
		c.setListVehicles(ListVehicle);
		this.restTemplate.put("http://127.0.0.1:8070/move/"+VehicleID+"?lon="+c.getLon()+"&lat="+c.getLat(), null);
		cRepo.save(c);
	}
}
