package com.webAppEmergency.Assignation;


import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.project.model.dto.FireDto;
import com.project.tools.GisTools;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.model.dto.Coord;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainRunnable implements Runnable {

	boolean isEnd = false;
	RestTemplate restTemplate;
	public List<Integer> List_Fire = new ArrayList<Integer>();
	RestTemplateBuilder restTemplateBuilder;
	ObjectMapper mapper;
	JsonNode jNode;
//////////////////////////////////////
//Rest template
//////////////////////////////////////

	public MainRunnable() { // Gestion du rest template
		this.restTemplate = new RestTemplate();
		this.mapper = new ObjectMapper();
	}

//////////////////////////////////////
//Threading
//////////////////////////////////////
	
	@Override
	public void run() {
		while(!this.isEnd) {
			try {
				System.out.println("[MAIN-RUN] Begin loop");
				System.out.println("[MAIN-RUN] current list of fire is : "+this.List_Fire);
				Thread.sleep(10000); //wait 10sec
				FireDto Tab_Fire[]=this.restTemplate.getForObject("http://127.0.0.1:8081/fire", FireDto[].class);
				for (FireDto fire:Tab_Fire) {
					System.out.println(fire.getId());
					if (!this.List_Fire.contains(fire.getId())) {
						System.out.println("[MAIN-RUN] New fire : "+fire.getId());
						Vehicle v=new Vehicle();
						try {
							v = PickVehicle2(fire);
							this.List_Fire.add(fire.getId());
							createPath(v, fire);
						} catch (IOException e) {
							e.printStackTrace();
						}
						System.out.println("[MAIN-RUN] Choosed vehicle : "+v);

						this.restTemplate.put("http://127.0.0.1:8070/state/"+v.getId()+"?state=ALLER", null);
						this.restTemplate.put("http://127.0.0.1:8070/giveFire/"+v.getId()+"/"+fire.getId(), null);
					}
				}

			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		
	}
	
//////////////////////////////////////
//Functions
//////////////////////////////////////
	
	public void stop() { 
		this.isEnd=true;
	}

	// recupere la caserne la plus proche
	//choisis le vehicule le lus adapte de cette caserne
	public Vehicle PickVehicle2(FireDto fire) { // version actuelle
		System.out.println("[MAIN-RUN-PickV2] PickVehicule2");
		Coord CoordFire= new Coord(fire.getLon(), fire.getLat());
		Vehicle v = null;
		List<Integer> LFireStation = new ArrayList<Integer>();
		while (v==null) {
			System.out.println("[MAIN-RUN-PickV2] fire coordinates : "+fire.getLon()+", "+fire.getLat());
			FireStation f = ClosestCaserne(CoordFire, LFireStation);
			System.out.println("[MAIN-RUN-PickV2] closest firestation : "+f);
			v = SelectVehicleInFireStation(f, fire.getType());
		}
		//TODO GetPompiers
		return v;
	}
	
	public FireStation ClosestCaserne(Coord CoordFire, List<Integer> LFireStation) {
		FireStation[] ListCaserne=this.restTemplate.getForObject("http://127.0.0.1:8050/getAll", FireStation[].class);
		FireStation res = new FireStation();
		Integer minDistance = -1;
		for (FireStation f:ListCaserne) { 
			if (!LFireStation.contains(f.getId())) {
				Integer Distance=GisTools.computeDistance2(new Coord(f.getLon(), f.getLat()), CoordFire);
				if (minDistance<=0 || minDistance>=Distance) {
					res=f;
					minDistance=Distance;
				}
			}
		}
		return res;
	}
	
	
	// Parcours les vehicules d'une caserne
	// S'ils sont disponibles alors
	// S'ils sont plus efficace alors
	// Ils sont choisis, le dernier restant est renvoye
	public Vehicle SelectVehicleInFireStation(FireStation f, String fireType)
	{
		Vehicle res = null;
		if (!f.getListVehicules().isEmpty()) {		
			float maxefficacite=-1;
			for (Integer idVehicle:f.getListVehicules()) { 
				Vehicle v=this.restTemplate.getForObject("http://127.0.0.1:8070/get/"+idVehicle, Vehicle.class);
				float efficacite = v.getType().getLiquidType().getEfficiency(fireType);
				if (v.getEtat()==State.DISPONIBLE) { 
					if (efficacite>maxefficacite) { 
						res=v; 						
						maxefficacite=efficacite;
					}
				}
			}
		}
		return res;
	}
	
	public void createPath(Vehicle v, FireDto feu) throws IOException {
		// Recupere le trajet sur mapbox api
		String Path = v.getLon()+","+v.getLat()+";"+feu.getLon()+","+feu.getLat();
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
		System.out.println("[MAIN-RUN-P] the path is" + path);
		//transmets ce path a vehicule
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = 
			      new HttpEntity<String>(path.toString(), headers);
		this.restTemplate.put("http://127.0.0.1:8070/setPath/"+v.getId(), request);
	}
}
