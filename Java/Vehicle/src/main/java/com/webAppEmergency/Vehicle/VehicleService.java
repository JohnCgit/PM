package com.webAppEmergency.Vehicle;

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
import com.project.model.dto.LiquidType;
@Service
public class VehicleService {

	@Autowired
	VehicleRepository vRepo;
	private RestTemplate restTemplate;
	JsonNode jNode;
	ObjectMapper mapper;
		
//////////////////////////////////////
// Rest template
//////////////////////////////////////
		
	public VehicleService(RestTemplateBuilder restTemplateBuilder) { // Gestion du rest template
        this.restTemplate = restTemplateBuilder.build();
        this.mapper=new ObjectMapper();
    }
		
//////////////////////////////////////
// get Vehicule
//////////////////////////////////////

	public List<Vehicle> getAll() {
		return vRepo.findAll();
	}
	
	public Vehicle getVehicule(int id) {
		Vehicle res=null;
		Optional<Vehicle> oVehicule = vRepo.findById(id);
		if (oVehicule.isPresent()){res=oVehicule.get();}
		return res;
	}
	
//////////////////////////////////////
//update/create Vehicule
//////////////////////////////////////
	
	public Vehicle createVehicule(Vehicle v) {
		Vehicle res = new Vehicle();
		if (v.getfireStationID()!=0) {
			createVehiculeRepo(v);
			createVehiculeFireSim(v);
			linkVehiculeFacility(v);
		}
		return res;
	}
	
	public Vehicle createVehiculeViaFacility(Vehicle v) {
		Vehicle res = createVehiculeRepo(v);
		createVehiculeFireSim(v);
		return res;
	}
	
	public void linkVehiculeFacility(Vehicle v) {
		this.restTemplate.put("http://127.0.0.1:8050/addVehicule/"+v.getfireStationID()+"/"+v.getId(), null);
	}
	
	public void createVehiculeFireSim(Vehicle v) {
		String content = vehiculeToFireSim(v);
		try {
			this.jNode = this.mapper.readTree(content);
			JsonNode jId = this.jNode.get("id");
			System.out.println("[VEHICULE-CREATE] Vehicule "+v.getId()+" id in fire sim is : "+jId);
			v.setIdFs(jId.asInt());
			vRepo.save(v);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Vehicle createVehiculeRepo(Vehicle v) {
		Optional<Vehicle> oVehicule = vRepo.findById(v.getId());
		if (oVehicule.isEmpty()) {
			v.setCrewMember(v.getType().getVehicleCrewCapacity());
			vRepo.save(v);
			}
		return v;
	}
	
	public void moveVehicule(int id, double lon, double lat) {
		Vehicle v = getVehicule(id);
		System.out.println("[VEHICULE-MOVE] vehicule"+v.getId()+" is moving to : "+ lon+", "+lat);
		v.setLon(lon);
		v.setLat(lat);
		vRepo.save(v);
		vehiculeToFireSim(v);
	}
	
	public void deleteVehicule(int id) {
		Vehicle v = getVehicule(id);
		vRepo.delete(v);
		this.restTemplate.delete("http://127.0.0.1:8081/vehicle/"+v.getIdFs());
	}
	
	public boolean followPath(int id) {
		Vehicle v = getVehicule(id);
		boolean res=false;
		if (v.getPath().size()>0) {			
			ArrayList<Double> Coord = v.getPath().remove(0);
			moveVehicule(id, Coord.get(0), Coord.get(1));
			res=true;
			}
		return res;
	}
	
//////////////////////////////////////
//gestion fire simulator
//////////////////////////////////////
	
	public String vehiculeToFireSim(Vehicle v) {
		JSONObject body = new JSONObject();
		if (v.getId()!=0) {body.put("id", v.getId());}
		body.put("lon", v.getLon());
		body.put("lat", v.getLat());
		body.put("type", v.getType());
		body.put("efficiency", v.getEfficiency());
		body.put("liquideType", v.getType().getLiquidType());
		body.put("liquidQuantity", v.getLiquidQuantity());
		body.put("liquidConsumption", v.getType().getLiquidConsumption());
		body.put("fuel", v.getFuel());
		body.put("fuelConsumption", v.getType().getFuelConsumption());
		body.put("crewMember", v.getCrewMember());
		body.put("crewMemberCapacity", v.getType().getVehicleCrewCapacity());
		body.put("facilityRefID", v.getfireStationID());
		System.out.println("[VEHICULE-TOFIRESIM] Body is "+body);
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = 
			      new HttpEntity<String>(body.toString(), headers);

		String res = this.restTemplate.postForObject("http://127.0.0.1:8081/vehicle", request, String.class);
		System.out.println("[VEHICULE-TOFIRESIM] FireSim response : "+res);
		return res;
	} 
	
//////////////////////////////////////
//setters Vehicule
//////////////////////////////////////
	
	public void setPath(int id, ArrayList<ArrayList<Double>> newPath) { // change path
		Vehicle v = getVehicule(id);
		v.setPath(newPath);
		vRepo.save(v);		
	}
	
	public void etatVehicule(int id, State state) { //change etat
		Vehicle v = getVehicule(id);
		v.setEtat(state);;
		vRepo.save(v);
	}
	
	public void facilityVehicule(int vid, int cid) { //change facility
		Vehicle v = getVehicule(vid);
		v.getfireStationID();
		vRepo.save(v);
	}

	public void giveFire(int vehiculeID, int idFire) { //change feu associe
		Vehicle v = getVehicule(vehiculeID);
		v.setIdFire(idFire);
		vRepo.save(v);
	}


	
	public void update(int vehicleId, String content) throws IOException {
		Vehicle v = getVehicule(vehicleId);
		JsonNode jNode = this.mapper.readTree(content);

		if(jNode.get("fuel")!=null) {
			v.setFuel(jNode.get("fuel").asDouble());
		}

		if(jNode.get("lon")!=null) {
			v.setLon(jNode.get("lon").asDouble());
		}

		if(jNode.get("lat")!=null) {
			v.setLat(jNode.get("lat").asDouble());
		}

		if(jNode.get("crewMember")!=null) {
			v.setCrewMember(jNode.get("crewMember").asInt());
		}

		if(jNode.get("liquidQuantity")!=null) {
			v.setLiquidQuantity(jNode.get("liquidQuantity").asDouble());
		}

		if(jNode.get("facilityRefID")!=null) {
			changeFacility(v.getId(),jNode.get("facilityRefID").asInt());
		}

		if(jNode.get("type").asText()!=null) {
			v.setType(EnumVehicle.valueOf((jNode.get("type").asText())));
		}

		if(jNode.get("liquidType").asText()!=null) {
			v.setLiquidType(LiquidType.valueOf((jNode.get("liquidType").asText())));
		}

		vRepo.save(v);
		vehiculeToFireSim(v);
}

	private void changeFacility(int id, int asInt) {
		// TODO check doability test
		
		
	}
}
