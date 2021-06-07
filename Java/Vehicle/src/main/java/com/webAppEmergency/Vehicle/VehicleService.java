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
// get Vehicle
//////////////////////////////////////

	public List<Vehicle> getAll() {
		return vRepo.findAll();
	}
	
	public Vehicle getVehicle(int id) {
		Vehicle res=null;
		Optional<Vehicle> oVehicle = vRepo.findById(id);
		if (oVehicle.isPresent()){res=oVehicle.get();}
		return res;
	}
	
//////////////////////////////////////
//update/create Vehicle
//////////////////////////////////////
	
	public Vehicle createVehicle(Vehicle v) {
		Vehicle res = new Vehicle();
		System.out.println("[VEHICLE-CREATE1] vehicle to be created : " +v);
		if (v.getFireStationID()!=0) {
			System.out.println("[VEHICLE-CREATE2] vehicle to be created : " +v);
			Vehicle res = createVehicleRepo(v);
			createVehicleFireSim(v);
			linkVehicleFireStation(v);
		}
		return res;
	}
	
	public Vehicle createVehicleViaFireStation(Vehicle v) {
		Vehicle res = createVehicleRepo(v);
		createVehicleFireSim(v);
		return res;
	}
	
	public void linkVehicleFireStation(Vehicle v) {
		System.out.print("http://127.0.0.1:8050/addVehicle/"+v.getFireStationID()+"/"+v.getId());
		this.restTemplate.put("http://127.0.0.1:8050/addVehicle/"+v.getFireStationID()+"/"+v.getId(), null);
	}

	public void removeVehicleFireStation(Vehicle v) {
		this.restTemplate.put("http://127.0.0.1:8050/removeVehicle/"+v.getFireStationID()+"/"+v.getId(), null);
	}
	
	public void createVehicleFireSim(Vehicle v) {
		String content = vehicleToFireSim(v);
		try {
			this.jNode = this.mapper.readTree(content);
			JsonNode jId = this.jNode.get("id");
			System.out.println("[VEHICLE-CREATE] Vehicle "+v.getId()+" id in fire sim is : "+jId);
			v.setIdFs(jId.asInt());
			vRepo.save(v);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Vehicle createVehicleRepo(Vehicle v) {
		Optional<Vehicle> oVehicle = vRepo.findById(v.getId());
		if (oVehicle.isEmpty()) {
			v.setCrewMember(v.getType().getVehicleCrewCapacity());
			vRepo.save(v);
			}
		return v;
	}
	
	public void moveVehicle(int id, double lon, double lat) {
		Vehicle v = getVehicle(id);
		System.out.println("[VEHICLE-MOVE] vehicle"+v.getId()+" is moving to : "+ lon+", "+lat);
		v.setLon(lon);
		v.setLat(lat);
		vRepo.save(v);
		vehicleToFireSim(v);
	}
	
	public void deleteVehicle(int id) {
		Vehicle v = getVehicle(id);
		vRepo.delete(v);
		this.restTemplate.delete("http://127.0.0.1:8081/vehicle/"+v.getIdFs());
	}
	
	public boolean followPath(int id) {
		Vehicle v = getVehicle(id);
		boolean res=false;
		if (v.getPath().size()>0) {			
			ArrayList<Double> Coord = v.getPath().remove(0);
			moveVehicle(id, Coord.get(0), Coord.get(1));
			res=true;
			}
		return res;
	}
	
//////////////////////////////////////
//gestion fire simulator
//////////////////////////////////////
	
	public String vehicleToFireSim(Vehicle v) {
		JSONObject body = new JSONObject();
		if (v.getId()!=0) {body.put("id", v.getIdFs());}
		body.put("lon", v.getLon());
		body.put("lat", v.getLat());
		body.put("type", v.getType());
		body.put("efficiency", v.getEfficiency());
		body.put("liquideType", v.getLiquidType());
		body.put("liquidQuantity", v.getLiquidQuantity());
		body.put("liquidConsumption", v.getType().getLiquidConsumption());
		body.put("fuel", v.getFuel());
		body.put("fuelConsumption", v.getType().getFuelConsumption());
		body.put("crewMember", v.getCrewMember());
		body.put("crewMemberCapacity", v.getType().getVehicleCrewCapacity());
		body.put("facilityRefID", v.getFireStationID());
		System.out.println("[VEHICLE-TOFIRESIM] Body is "+body);
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = 
			      new HttpEntity<String>(body.toString(), headers);

		String res = this.restTemplate.postForObject("http://127.0.0.1:8081/vehicle", request, String.class);
		System.out.println("[VEHICLE-TOFIRESIM] FireSim response : "+res);
		return res;
	} 
	
//////////////////////////////////////
//setters Vehicle
//////////////////////////////////////
	
	public void setPath(int id, ArrayList<ArrayList<Double>> newPath) { // change path
		Vehicle v = getVehicle(id);
		v.setPath(newPath);
		vRepo.save(v);		
	}
	
	public void etatVehicle(int id, State state) { //change etat
		Vehicle v = getVehicle(id);
		v.setEtat(state);;
		vRepo.save(v);
	}
	
	public void facilityVehicle(int vid, int cid) { //change facility
		Vehicle v = getVehicle(vid);
		v.getFireStationID();
		vRepo.save(v);
	}

	public void giveFire(int vehicleID, int idFire) { //change feu associe
		Vehicle v = getVehicle(vehicleID);
		v.setIdFire(idFire);
		vRepo.save(v);
	}


	
	public void update(int vehicleId, String content) throws IOException {
		Vehicle v = getVehicle(vehicleId);
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
		vehicleToFireSim(v);
}

	private void changeFacility(int id, int asInt) {
		//TODO test place
		//if()
		Vehicle v = getVehicle(id);
		
		
		
	}
}
