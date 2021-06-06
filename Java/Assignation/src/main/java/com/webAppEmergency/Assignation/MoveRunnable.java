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
				Vehicule[] tabVehicule = this.restTemplate.getForObject("http://127.0.0.1:8070/getAll", Vehicule[].class);
				for (Vehicule v: tabVehicule) {
					switch (v.getEtat()) {
					case ALLER:
						FireDto fire=this.restTemplate.getForObject("http://127.0.0.1:8090/get/"+v.getIdFire(), FireDto.class);
						if (v.getPath().size()==0) {
							System.out.println("[MOVE-RUN-A] Le vehicule "+v.getRealid()+" vas en extinction");
							this.restTemplate.put("http://127.0.0.1:8070/state/"+v.getRealid()+"?state=EXTINCTION", null);
							createPath(v, fire);
						}
						else {
							System.out.println("[MOVE-RUN-A] Le vehicule "+v.getRealid()+" est a l aller");
							Coord c1 = new Coord(v.getLon(), v.getLat());
							Coord c2 = new Coord(fire.getLon(), fire.getLat());
							System.out.println("[MOVE-RUN-A] il est a "+GisTools.computeDistance2(c1, c2)+" du feu"+fire.getId());
							this.restTemplate.put("http://127.0.0.1:8070/followPath/"+v.getRealid(), null);
						}
						break;
					case EXTINCTION:
						System.out.println("[MOVE-RUN-E] "+v.getRealid()+ " est a l extinction");
						FireDto fire1 = this.restTemplate.getForObject("http://127.0.0.1:8090/get/"+v.getIdFire(), FireDto.class);
						System.out.println("[MOVE-RUN-E] le feu "+" a une intensite de "+fire1.getIntensity());
						if (fire1.getIntensity()<0) {
							this.restTemplate.put("http://127.0.0.1:8070/state/"+v.getRealid()+",state=RETOUR", null);
						}
						break;
					case RETOUR:
						System.out.println("[MOVE-RUN-R] Le vehicule "+v.getRealid()+" est au retour");
						if (v.getPath().size()==0) {
							System.out.println("[MOVE-RUN-R] Le vehicule "+v.getRealid()+" est rentre");
							this.restTemplate.put("http://127.0.0.1:8070/state/"+v.getRealid()+",state=DISPONIBLE", null);
						}
						else {
							this.restTemplate.put("http://127.0.0.1:8070/followPath/"+v.getRealid(), null);
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
	
	public void createPath(Vehicule v, FireDto feu) throws IOException {
		// Recupere le trajet sur mapbox api
		String Path = feu.getLon()+","+feu.getLat()+";"+v.getLon()+","+v.getLat();
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
		this.restTemplate.put("http://127.0.0.1:8070/setPath/"+v.getRealid(), request);
	}
	
}