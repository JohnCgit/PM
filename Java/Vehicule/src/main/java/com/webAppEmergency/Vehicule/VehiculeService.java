package com.webAppEmergency.Vehicule;

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
public class VehiculeService {

		
	@Autowired
	VehiculeRepository vRepo;
	private RestTemplate restTemplate;
		
//////////////////////////////////////
// Rest template
//////////////////////////////////////
		
	public VehiculeService(RestTemplateBuilder restTemplateBuilder) { // Gestion du rest template
        this.restTemplate = restTemplateBuilder.build();
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
		JSONObject obj = new JSONObject();
		obj.put("id", v.getRealid());
		obj.put("lon", v.getLon());
		obj.put("lat", v.getLat());
		obj.put("type", v.getType());
		obj.put("efficiency", v.getEfficiency());
		obj.put("liquideType", v.getLiquidType());
		obj.put("liquideQuantity", v.getLiquidQuantity());
		obj.put("liquidConsumption", v.getLiquidConsumption());
		obj.put("fuel", v.getFuel());
		obj.put("fuelConsumption", v.getFuelConsumption());
		obj.put("crewMember", v.getCrewMember());
		obj.put("crewMemberCapacity", v.getCrewMemberCapacity());
		obj.put("facilityRefID", v.getFacilityRefID());
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
	
	public void moveVehicule(int id, float lon, float lat) {
		Vehicule v = getVehicule(id);
		v.setLat(lat);
		v.setLon(lon);
		JSONObject body=new JSONObject();
		body.put("lon",lon);
		body.put("lat",lat);
		this.restTemplate.put("http://127.0.0.1:8081/vehicule/"+id, body);
	}
	
	public void deleteVehicule(int id) {
		Vehicule v = getVehicule(id);
		vRepo.delete(v);
		this.restTemplate.delete("http://127.0.0.1:8081/vehicle/"+id);
	}
}