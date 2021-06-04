package com.webAppEmergency.Vehicule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.model.dto.Coord;
import com.project.model.dto.VehicleType;

@Service
public class VehiculeService {

		
	@Autowired
	VehiculeRepository vRepo;
	private RestTemplate restTemplate;
	JsonNode jNode;
	ObjectMapper mapper;
		
//////////////////////////////////////
// Rest template
//////////////////////////////////////
		
	public VehiculeService(RestTemplateBuilder restTemplateBuilder) { // Gestion du rest template
        this.restTemplate = restTemplateBuilder.build();
        this.mapper=new ObjectMapper();
    }
		
//////////////////////////////////////
// get Vehicule
//////////////////////////////////////

	public List<Vehicule> getAll() {
		return vRepo.findAll();
	}
	
	public Vehicule getVehicule(int realid) {
		Vehicule res=null;
		Optional<Vehicule> oVehicule = vRepo.findById(realid);
		if (oVehicule.isPresent()){res=oVehicule.get();}
		return res;
	}
	
//////////////////////////////////////
//update/create Vehicule
//////////////////////////////////////
	
	public boolean createVehicule(Vehicule v) {
		boolean res=false;
		res=createVehiculeRepo(v);
		if (res) {createVehiculeFireSim(v);}
		return res;
	}
	
	
	public void createVehiculeFireSim(Vehicule v) {
		JSONObject body = new JSONObject();
		body.put("id", v.getRealid());
		body.put("lon", v.getLon());
		body.put("lat", v.getLat());
		body.put("type", v.getType());
		body.put("efficiency", v.getEfficiency());
		body.put("liquideType", v.getType().getLiquidType());
		body.put("liquideQuantity", v.getLiquidQuantity());
		body.put("liquidConsumption", v.getType().getLiquidConsumption());
		body.put("fuel", v.getFuel());
		body.put("fuelConsumption", v.getType().getFuelConsumption());
		body.put("crewMember", v.getCrewMember());
		body.put("crewMemberCapacity", v.getType().getVehicleCrewCapacity());
		System.out.println("Body is "+body);
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = 
			      new HttpEntity<String>(body.toString(), headers);

		String res = this.restTemplate.postForObject("http://127.0.0.1:8081/vehicle", request, String.class);
		
		try {
			this.jNode = this.mapper.readTree(res);
			JsonNode jId = this.jNode.get("id");
			v.setRealid(jId.asInt());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean createVehiculeRepo(Vehicule v) {
		boolean res=false;
		Optional<Vehicule> oVehicule = vRepo.findById(v.getRealid());
		if (oVehicule.isEmpty()) {vRepo.save(v); res=true;}
		return res;
	}
	
	public void updateVehicule(int id, JSONObject json) {
		Vehicule Vehi = getVehicule(id);
		System.out.println(json);
	}
	
	public void moveVehicule(int id, double lon, double lat) {
		Vehicule v = getVehicule(id);
		v.setLon(lon);
		v.setLat(lat);
		JSONObject body=new JSONObject();
		body.put("lon",lon);
		body.put("lat",lat);
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = 
			      new HttpEntity<String>(body.toString(), headers);
		this.restTemplate.put("http://127.0.0.1:8081/vehicule/"+id, request);
	}
	
	public void deleteVehicule(int id) {
		Vehicule v = getVehicule(id);
		vRepo.delete(v);
		this.restTemplate.delete("http://127.0.0.1:8081/vehicle/"+id);
	}
	
//	public void followPath(int id) {
//		Vehicule v = getVehicule(id);
//		Coord c1 = v.getPath().remove(0);
//		v.setLon(c1.getLon());
//		v.setLat(c1.getLat());
//		JSONObject body=new JSONObject();
//		body.put("lon",c1.getLon());
//		body.put("lat",c1.getLat());
//		this.restTemplate.put("http://127.0.0.1:8081/vehicule/"+id, body);
//	}
}