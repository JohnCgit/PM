package com.webAppEmergency.Assignation;

import org.json.JSONObject;
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
import com.webAppEmergency.Assignation.MoveRunnable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainRunnable implements Runnable {

	boolean isEnd = false;
	RestTemplate restTemplate;
	public List<Integer> List_Feu = new ArrayList<Integer>();
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
				System.out.println("[MAIN-RUN] current list is : "+this.List_Feu);
				Thread.sleep(10000); //wait 10sec
				FireDto Tab_Fire[]=this.restTemplate.getForObject("http://127.0.0.1:8081/fire", FireDto[].class);
//				List<FireDto> fList=new ArrayList<FireDto>();
				System.out.println("[MAIN-RUN] There are these fires : ");
				for (FireDto feu:Tab_Fire) {
					System.out.println(feu.getId());
					if (!this.List_Feu.contains(feu.getId())) {
						System.out.println("[MAIN-RUN-55] qui est nouveau et associe avec : ");
						Vehicule v=new Vehicule();
						try {
							v = PickVehicule2(feu);
							this.List_Feu.add(feu.getId());
							createPath(v, feu);
						} catch (IOException e) {
							e.printStackTrace();
						}
						System.out.println("[MAIN-RUN-64] "+v);

						this.restTemplate.put("http://127.0.0.1:8070/state/"+v.getId()+"?state=ALLER", null);
						this.restTemplate.put("http://127.0.0.1:8070/giveFire/"+v.getId()+"/"+feu.getId(), null);
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
	public Vehicule PickVehicule2(FireDto feu) { // version actuelle
		System.out.println("[MAIN-RUN-PickV2] PickVehicule2");
		Coord CoordFire= new Coord(feu.getLon(), feu.getLat());
		Vehicule v = null;
		List<Integer> LCaserne = new ArrayList<Integer>();
		while (v==null) {
			System.out.println("[MAIN-RUN-PickV2] Coord du feu : "+feu.getLon()+", "+feu.getLat());
			Caserne c = ClosestCaserne(CoordFire, LCaserne);
			System.out.println("[MAIN-RUN-PickV2] caserne la plus proche : "+c);
			v = SelectVehiculeInCaserne(c, feu.getType());
			System.out.println("[MAIN-RUN-PickV2] vehicule choisi : "+v);
		}
		//TODO GetPompiers
		return v;
	}
	
	//regarde la plus proche des casernes
	
	public Caserne ClosestCaserne(Coord CoordFire, List<Integer> lCaserne) {
		Caserne[] ListCaserne=this.restTemplate.getForObject("http://127.0.0.1:8050/getAll", Caserne[].class);
		Caserne res = new Caserne();
		Integer minDistance = -1;
		for (Caserne c:ListCaserne) { 
			if (!lCaserne.contains(c.getId())) {
				Integer Distance=GisTools.computeDistance2(new Coord(c.getLon(), c.getLat()), CoordFire);
				if (minDistance<=0 || minDistance>=Distance) {
					System.out.println("[MAIN-RUN-Caserne] current casern : "+c);
					res=c;
					minDistance=Distance;
				}
			}
		}
		System.out.println("[MAIN-RUN-Caserne] select one : "+res);
		return res;
	}
	
	
	// Parcours les vehicules d'une caserne
	// S'ils sont disponibles alors
	// S'ils sont plus efficace alors
	// Ils sont choisis, le dernier restant est renvoye
	public Vehicule SelectVehiculeInCaserne(Caserne c, String fireType)
	{
		Vehicule res = null;
		if (!c.getListVehicules().isEmpty()) {		
			float maxefficacite=-1;
			for (Integer idVehicule:c.getListVehicules()) { 
				Vehicule v=this.restTemplate.getForObject("http://127.0.0.1:8070/get/"+idVehicule, Vehicule.class);
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
	
	public void createPath(Vehicule v, FireDto feu) throws IOException {
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
