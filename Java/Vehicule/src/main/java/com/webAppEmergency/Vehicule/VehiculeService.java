package com.webAppEmergency.Vehicule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
	
	public Vehicule createVehicule(Vehicule v) {
		Vehicule res = createVehiculeRepo(v);
		createVehiculeFireSim(v);
		linkVehiculeFacility(v);
		return res;
	}
	
	public Vehicule createVehiculeViaFacility(Vehicule v) {
		Vehicule res = createVehiculeRepo(v);
		createVehiculeFireSim(v);
		return res;
	}
	
	public void linkVehiculeFacility(Vehicule v) {
		this.restTemplate.put("http://127.0.0.1:8050/addVehicule/"+v.getFacilityRefID()+"/"+v.getRealid(), null);
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
			System.out.println(jId);
			v.setIdVehicle(jId.asInt());
			vRepo.save(v);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Vehicule createVehiculeRepo(Vehicule v) {
		Optional<Vehicule> oVehicule = vRepo.findById(v.getRealid());
		if (oVehicule.isEmpty()) {
			vRepo.save(v);
			}
		return v;
	}
	
	public void moveVehicule(int id, double lon, double lat) {
		Vehicule v = getVehicule(id);
		System.out.println("vehicule"+v.getRealid()+" is moving to : "+ lon+", "+lat);
		v.setLon(lon);
		v.setLat(lat);
		vRepo.save(v);
		JSONObject body=new JSONObject();
		body.put("id",v.getIdVehicle());
		body.put("lon",lon);
		body.put("lat",lat);
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = 
			      new HttpEntity<String>(body.toString(), headers);
		this.restTemplate.put("http://127.0.0.1:8081/vehicle/"+id, request);
	}
	
	public void deleteVehicule(int id) {
		Vehicule v = getVehicule(id);
		vRepo.delete(v);
		this.restTemplate.delete("http://127.0.0.1:8081/vehicle/"+v.getIdVehicle());
	}
	
	public boolean followPath(int id) {
		Vehicule v = getVehicule(id);
		boolean res=false;
		if (v.getPath().size()>0) {			
			ArrayList<Double> Coord = v.getPath().remove(0);
			moveVehicule(id, Coord.get(0), Coord.get(1));
			res=true;
			}
		return res;
	}
	
	public void setPath(int id, ArrayList<ArrayList<Double>> newPath) {
		Vehicule v = getVehicule(id);
		v.setPath(newPath);
		vRepo.save(v);		
	}
	
	public void etatVehicule(int id, Etat etat) {
		Vehicule v = getVehicule(id);
		v.setEtat(etat);;
		vRepo.save(v);
	}
	
	public void facilityVehicule(int vid, int cid) {
		Vehicule v = getVehicule(vid);
		v.setFacilityRefID(cid);
		vRepo.save(v);
	}

	
//	public void update(int vehicleId, String content) throws IOException {
//		JsonNode jNode = this.mapper.readTree(content);
//	}
	// TODO Update, utiliser mapper.findTree pour trouver attributs a update  
	// puis parcourir ces noms dans le jNode en .get(String)

}
