package com.webAppEmergency.Assignation;


import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.model.dto.Coord;
import com.project.model.dto.FireDto;
import com.project.tools.GisTools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MoveRunnable implements Runnable{

	private RestTemplate restTemplate;
	boolean isEnd = false;
	JsonNode jNode;
	ObjectMapper mapper;
//////////////////////////////////////
//Rest template
//////////////////////////////////////

	public MoveRunnable() { // Gestion du rest template
	this.restTemplate = new RestTemplate();
	this.mapper=new ObjectMapper();
	}
	
//////////////////////////////////////
//Threading
//////////////////////////////////////
	
	@Override
	public void run() {
		while (!this.isEnd) {
			try{
				Thread.sleep(5000);
				Vehicle[] tabVehicle = this.restTemplate.getForObject("http://127.0.0.1:8070/getAll", Vehicle[].class);
				for (Vehicle v: tabVehicle) {
					int vehicleID = v.getId();
					switch (v.getEtat()) {
					case ALLER:
						FireDto fire=this.restTemplate.getForObject("http://127.0.0.1:8090/get/"+v.getIdFire(), FireDto.class);

						Coord c1 = new Coord(v.getLon(), v.getLat());
						Coord c2 = new Coord(fire.getLon(), fire.getLat());
						if (v.getPath().size()==0) {
							this.restTemplate.put("http://127.0.0.1:8070/move/"+vehicleID+"?lon="+fire.getLon()+"&lat="+fire.getLat(), null);
							System.out.println("[MOVE-RUN-A] "+vehicleID+" est a "+GisTools.computeDistance2(c1, c2)+"m du feu "+fire.getId());
							System.out.println("[MOVE-RUN-A] Le vehicule "+vehicleID+" vas en extinction");
							FireStation c = this.restTemplate.getForObject("http://127.0.0.1:8050/"+v.getFireStationID(), FireStation.class);
							this.restTemplate.put("http://127.0.0.1:8070/state/"+vehicleID+"?state=EXTINCTION", null);
							createPath(v, c); 
						}
						else {
							System.out.println("[MOVE-RUN-A] Le vehicule "+vehicleID+" est a l aller");
							System.out.println("[MOVE-RUN-A] "+vehicleID+" est a "+GisTools.computeDistance2(c1, c2)+"m du feu "+fire.getId());
							move(v);
						}
						break;
					case EXTINCTION:
						System.out.println("[MOVE-RUN-E] "+vehicleID+ " est a l extinction");
						FireDto fire1 = this.restTemplate.getForObject("http://127.0.0.1:8090/get/"+v.getIdFire(), FireDto.class);
						if (fire1==null) {
//							useAE(v);
							this.restTemplate.put("http://127.0.0.1:8070/state/"+vehicleID+"?state=RETOUR", null);
						}
						else {
							System.out.println("[MOVE-RUN-E] le feu "+fire1.getId()+" a une intensite de "+fire1.getIntensity());
						}
						break;
					case RETOUR:
						System.out.println("[MOVE-RUN-R] Le vehicule "+vehicleID+" est au retour");
						if (v.getPath().size()==0) {
							FireStation c = this.restTemplate.getForObject("http://127.0.0.1:8050/"+v.getFireStationID(), FireStation.class);
							this.restTemplate.put("http://127.0.0.1:8070/move/"+vehicleID+"?lon="+c.getLon()+"&lat="+c.getLat(), null);
							System.out.println("[MOVE-RUN-R] Le vehicule "+vehicleID+" est rentre");
							this.restTemplate.put("http://127.0.0.1:8070/state/"+vehicleID+"?state=DISPONIBLE", null);
						}
						else {
							move(v);
						}
						break;
					case DISPONIBLE:
					case NON_OPERATIONNAL:
						if (v.getFuel()<v.getFuelQuantityMax()) {	
							refuel(v);
						}
						if (v.getLiquidQuantity()<v.getLiquidQuantityMax()) {	
							refill(v);
						}											
						break;
					}
				}
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	public void stop() {
		this.isEnd=true;
	}
	
	public void createPath(Vehicle v, FireStation f) throws IOException {
		// Recupere le trajet sur mapbox api
		String Path = v.getLon()+","+v.getLat()+";"+f.getLon()+","+f.getLat();
		String url="https://api.mapbox.com/directions/v5/mapbox/driving/"+Path+"?alternatives=false&geometries=geojson&steps=false&access_token=pk.eyJ1IjoiZXJtaXphaGQiLCJhIjoiY2twaTJxdGRjMGY3MjJ1cGM1NDNqc3NsNyJ9.xxjbVbTAlxUklvOFvXG9Bw";
		String res = this.restTemplate.getForObject(url, String.class);
		// Traduit la requete pour creer un path
		this.jNode = mapper.readTree(res);
		JsonNode jPath = jNode.get("routes").get(0).get("geometry").get("coordinates");
		ArrayList<ArrayList<Double>> path = new ArrayList<ArrayList<Double>>();
		for (JsonNode coord: jPath) {
			double lon = coord.get(0).asDouble();
			double lat = coord.get(1).asDouble();
			path.add(new ArrayList<>(List.of(lon, lat)));
		}
		//System.out.println("[MOVE-RUN-P] new path is" + path);
		//transmets ce path a vehicule
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = 
			      new HttpEntity<String>(path.toString(), headers);
		this.restTemplate.put("http://127.0.0.1:8070/setPath/"+v.getId(), request);
	}
	
	public void move(Vehicle v) {
		Coord c1 = new Coord(v.getLon(), v.getLat());
		List<Double> nextStep = v.getPath().remove(0);
		Coord c2 = new Coord(nextStep.get(0), nextStep.get(1));
		double distance = GisTools.computeDistance2(c1, c2);
		
		int deplacement = v.getDeplacement();
		
		while (distance < deplacement && v.getPath().size()>0) { //au moins 2 deplacement
			deplacement -= (int)distance;
			//System.out.println("[MOVE-MOVE-IT] le deplacement de "+v.getId()+" est de "+deplacement);
			this.restTemplate.put("http://127.0.0.1:8070/followPath/"+v.getId(), null);
			
			nextStep = v.getPath().remove(0);
			c2 = new Coord(nextStep.get(0), nextStep.get(1));
			distance = GisTools.computeDistance2(c1, c2);
		}
		if (distance < deplacement) {//plot armor
			deplacement -= (int)distance;
			//System.out.println("[MOVE-MOVE-IT] le deplacement de "+v.getId()+" est de "+deplacement);
			this.restTemplate.put("http://127.0.0.1:8070/followPath/"+v.getId(), null);
		}
		deplacement += v.getDeplacementType();
		this.restTemplate.put("http://127.0.0.1:8070/setDeplacement/"+v.getId()+"/"+deplacement, null);
		
//		useFuel(v);
		
	}
	
	public void useFuel(Vehicle v) {
		double fuel = v.getFuel();
		double consumption = v.getFuelConsumption();
		JSONObject body = new JSONObject();
		body.put("fuel", fuel-consumption);
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = 
			      new HttpEntity<String>(body.toString(), headers);
		this.restTemplate.put("http://127.0.0.1:8070/update/1", request);
	}
	
	public void useAE(Vehicle v) {
		double liquid = v.getLiquidQuantity();
		double consumption = v.getLiquidConsumption();
		JSONObject body = new JSONObject();
		body.put("fuel", liquid-consumption);
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = 
			      new HttpEntity<String>(body.toString(), headers);
		this.restTemplate.put("http://127.0.0.1:8070/update/1", request);
	}
	
	public void refuel(Vehicle v) {
		JSONObject body = new JSONObject();
		int refuel = (int) Math.min(v.getFuel()+40, v.getLiquidQuantityMax());
		body.put("fuel", refuel);
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = 
			      new HttpEntity<String>(body.toString(), headers);
			this.restTemplate.put("http://127.0.0.1:8070/update/"+v.getId(), request);
	}
	
	public void refill (Vehicle v) {
		JSONObject body = new JSONObject();
		int refill = (int) Math.min(v.getLiquidQuantity()+40, v.getLiquidQuantityMax());
		body.put("fuel", refill);
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = 
			      new HttpEntity<String>(body.toString(), headers);
			this.restTemplate.put("http://127.0.0.1:8070/update/"+v.getId(), request);
	}
}