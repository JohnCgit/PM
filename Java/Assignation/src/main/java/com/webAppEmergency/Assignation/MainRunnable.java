package com.webAppEmergency.Assignation;

import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import com.project.model.dto.FireDto;
import com.webAppEmergency.Assignation.Assignation;
import com.project.tools.GisTools;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.model.dto.Coord;
import com.webAppEmergency.Assignation.MoveRunnable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainRunnable implements Runnable {

	boolean isEnd = false;
	RestTemplate restTemplate;
	public List<FireDto> List_Feu;
	RestTemplateBuilder restTemplateBuilder;
	ObjectMapper mapper;
	JsonNode jNode;
//////////////////////////////////////
//Rest template
//////////////////////////////////////

	public MainRunnable() { // Gestion du rest template
		this.restTemplate = restTemplateBuilder.build();
		this.mapper = new ObjectMapper();
	}

//////////////////////////////////////
//Threading
//////////////////////////////////////
	
	@Override
	public void run() {
		while(!this.isEnd) {
			try {
				Thread.sleep(10000); //wait 10sec
				FireDto Tab_Fire[]=this.restTemplate.getForObject("http://127.0.0.1:8081/fire/", null);
//				List<FireDto> fList=new ArrayList<FireDto>();
				for (FireDto feu:Tab_Fire) {
					if (!List_Feu.contains(feu)) {
						Vehicule v=new Vehicule();
						try {
							v = PickVehicule1(feu);
							List_Feu.add(feu);
							createPath(v, feu);
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						JSONObject body = new JSONObject();
						body.put("Etat", Etat.ALLER);
						this.restTemplate.put("http://127.0.0.1/update/"+v.getRealid(), body);
				
					}
				}

			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		
	}
	
	public void stop() {
		this.isEnd=true;
	}
	
	public Vehicule PickVehicule1(FireDto feu) {
		Coord CoordFire= new Coord(feu.getLon(), feu.getLat());
		return ClosestVehicule(CoordFire);
	}
	
	public Vehicule ClosestVehicule(Coord CoordFire) {
		Vehicule Tab_Vehicule[]=this.restTemplate.getForObject("http://127.0.0.1:8090/getAll", null);
		Vehicule res = new Vehicule();
		Integer minDistance = -1;
		//TODO hashmap vehicule, distance, regarder le type
		for (Vehicule v:Tab_Vehicule) {
			Integer Distance=GisTools.computeDistance2(new Coord(v.getLon(), v.getLat()), CoordFire);
			if (minDistance<=0 || minDistance>=Distance) {
				res=v;
				minDistance=Distance;
			}
		}
		return res;
	}

	public Vehicule PickVehicule2(FireDto feu) {
		Coord CoordFire= new Coord(feu.getLon(), feu.getLat());
		Caserne c = ClosestCaserne(CoordFire);
		Vehicule v = SelectVehiculeInCaserne(c, feu.getType());
		//TODO GetPompiers
		return v;
	}
	
	public Caserne ClosestCaserne(Coord CoordFire) {
		List<Caserne> ListCaserne=this.restTemplate.getForObject("http://127.0.0.1:8010/caserne/getAll", null);
		Caserne res = new Caserne();
		Integer minDistance = -1;
		//TODO hashmap vehicule, distance, regarder le type
		for (Caserne c:ListCaserne) {
			Integer Distance=GisTools.computeDistance2(c.getCoord(), CoordFire);
			if (minDistance<=0 || minDistance>=Distance) {
				res=c;
				minDistance=Distance;
			}
		}
		return res;
	}
	
	
	// Parcours les vehicules d'une caserne
	// S'ils sont disponibles alors
	// S'ils sont plus efficace alors
	// Ils sont choisis, le dernier restant est renvoye
	public Vehicule SelectVehiculeInCaserne(Caserne c, String fireType)
	{
		Vehicule res = null;
		float maxefficacite=-1;
		for (Integer idVehicule:c.getListVehicules()) {
			Vehicule v=this.restTemplate.getForObject("http://127.0.0.1:8010/vehicule/id/"+idVehicule, Vehicule.class);
			float efficacite = v.getType().getLiquidType().getEfficiency(fireType);
			if (v.getEtat()==Etat.DISPONIBLE) {
				if (efficacite>maxefficacite) {
					res=v;
					maxefficacite=efficacite;
				}
			}
			
		}
		return null;
	}
	
	public void createPath(Vehicule v, FireDto feu) throws IOException {
		String Path = v.getLon()+","+v.getLat()+";"+feu.getLon()+","+feu.getLat();
		String url="https://api.mapbox.com/directions/v5/mapbox/driving/"+Path+"?alternatives=false&geometries=geojson&steps=false&access_token=pk.eyJ1IjoiZXJtaXphaGQiLCJhIjoiY2twaTJxdGRjMGY3MjJ1cGM1NDNqc3NsNyJ9.xxjbVbTAlxUklvOFvXG9Bw";
		String res = this.restTemplate.getForObject(url, String.class);
		this.jNode = mapper.readTree(res);
		JsonNode jPath = jNode.get("routes").get(0).get("geometry").get("coordinates");
		List<Coord> path = new ArrayList<>();
		for (JsonNode coord: jPath) {
			double lon = coord.get(0).asDouble();
			double lat = coord.get(1).asDouble();
			path.add(new Coord(lon, lat));
		}
	}
}
