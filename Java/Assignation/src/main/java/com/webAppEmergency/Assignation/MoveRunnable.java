package com.webAppEmergency.Assignation;

import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
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
				Thread.sleep(10000);
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
							System.out.println("[MOVE-RUN-A] il est a "+GisTools.computeDistance2(c1, c2)+"m du feu "+fire.getId());
							System.out.println("[MOVE-RUN-A] Le vehicule "+vehicleID+" vas en extinction");
							FireStation c = this.restTemplate.getForObject("http://127.0.0.1:8050/"+v.getfireStationID(), FireStation.class);
							this.restTemplate.put("http://127.0.0.1:8070/state/"+vehicleID+"?state=EXTINCTION", null);
							createPath(v, c); 
						}
						else {
							System.out.println("[MOVE-RUN-A] Le vehicule "+vehicleID+" est a l aller");
							System.out.println("[MOVE-RUN-A] il est a "+GisTools.computeDistance2(c1, c2)+"m du feu "+fire.getId());
							this.restTemplate.put("http://127.0.0.1:8070/followPath/"+vehicleID, null);
						}
						break;
					case EXTINCTION:
						System.out.println("[MOVE-RUN-E] "+vehicleID+ " est a l extinction");
						FireDto fire1 = this.restTemplate.getForObject("http://127.0.0.1:8090/get/"+v.getIdFire(), FireDto.class);
						if (fire1==null) {
							this.restTemplate.put("http://127.0.0.1:8070/state/"+vehicleID+"?state=RETOUR", null);
						}
						else {
							System.out.println("[MOVE-RUN-E] le feu "+" a une intensite de "+fire1.getIntensity());
						}
						break;
					case RETOUR:
						System.out.println("[MOVE-RUN-R] Le vehicule "+vehicleID+" est au retour");
						if (v.getPath().size()==0) {
							System.out.println("[MOVE-RUN-R] Le vehicule "+vehicleID+" est rentre");
							this.restTemplate.put("http://127.0.0.1:8070/state/"+vehicleID+"?state=DISPONIBLE", null);
						}
						else {
							this.restTemplate.put("http://127.0.0.1:8070/followPath/"+vehicleID, null);
						}
						break;
					default:
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
		String Path = f.getLon()+","+f.getLat()+";"+v.getLon()+","+v.getLat();
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
		System.out.println("[MOVE-RUN-P] the path is" + path);
		//transmets ce path a vehicule
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = 
			      new HttpEntity<String>(path.toString(), headers);
		this.restTemplate.put("http://127.0.0.1:8070/setPath/"+v.getId(), request);
	}
	
}