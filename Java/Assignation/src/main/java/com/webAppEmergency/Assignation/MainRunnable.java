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
	List<Integer> LFireStation;
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
				System.out.println("[MAIN-RUN] current list of has been assigned fire is : "+this.List_Fire);
				Thread.sleep(2500); //wait 10sec
				FireDto Tab_Fire[]=this.restTemplate.getForObject("http://127.0.0.1:8081/fire", FireDto[].class);
				
				for (FireDto fire:Tab_Fire) {
					Vehicle[] Tab_Vehicle = this.restTemplate.getForObject("http://127.0.0.1:8070/getAll", Vehicle[].class);
					boolean isOneFree = false;
					for (Vehicle v:Tab_Vehicle) {if (v.getState()==State.DISPONIBLE) {isOneFree=true;}}
					if ((!this.List_Fire.contains(fire.getId())) && isOneFree) {
						System.out.println("[MAIN-RUN] New fire : "+fire.getId());
						Vehicle v=new Vehicle();
						try {
							v = PickVehicle2(fire);
							this.List_Fire.add(fire.getId());
							createPath(v, fire);
							System.out.println("[MAIN-RUN] Choosed vehicle : "+v);

							this.restTemplate.put("http://127.0.0.1:8070/state/"+v.getId()+"?state=ALLER", null);
							this.restTemplate.put("http://127.0.0.1:8070/giveFire/"+v.getId()+"/"+fire.getId(), null);
						} catch (IOException e) {
							e.printStackTrace();
						}
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
//		System.out.println("[MAIN-RUN-PickV2] PickVehicule2");
		Coord CoordFire= new Coord(fire.getLon(), fire.getLat());
		Vehicle v = null;
		this.LFireStation = new ArrayList<Integer>();
		while (v==null) {
//			System.out.println("[MAIN-RUN-PickV2] fire coordinates : "+fire.getLon()+", "+fire.getLat());
			FireStation f = ClosestCaserne(CoordFire);
			this.LFireStation.add(f.getId());
//			System.out.println("[MAIN-RUN-PickV2] closest firestation : "+f);
			v = SelectVehicleInFireStation(f, fire.getType());
		}
		return v;
	}
	
	public FireStation ClosestCaserne(Coord CoordFire) {
		FireStation[] ListCaserne=this.restTemplate.getForObject("http://127.0.0.1:8050/getAll", FireStation[].class);
		FireStation res = new FireStation();
		Integer minDistance = -1;
		for (FireStation f:ListCaserne) { 
			if (!this.LFireStation.contains(f.getId())) {
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
		if (!f.getListVehicles().isEmpty()) {		
			float maxefficiency=-1;
//			System.out.println("[MAIN-RUN-SELECTV] vehicles available : "+f.getListVehicles());
			for (Integer idVehicle:f.getListVehicles()) { 
//				System.out.println("[MAIN-RUN-SELECTV] vehicles treated : "+idVehicle);
				Vehicle v=this.restTemplate.getForObject("http://127.0.0.1:8070/get/"+idVehicle, Vehicle.class);
//				System.out.println("[MAIN-RUN-SELECTV] "+v);
				float efficiency = v.getLiquidType().getEfficiency(fireType);// *v.getEfficiency
				//TODO si le vehicule a assez de fuel / AE
				if (v.getEtat()==State.DISPONIBLE) { 
					if (efficiency>maxefficiency) { 
						res=v; 						
						maxefficiency=efficiency;
					}
				}
			}
		}
		return res;
	}
	
	public void createPath(Vehicle v, FireDto feu) throws IOException {
		ArrayList<ArrayList<Double>> path = wouldBePath(v, feu);
//		System.out.println("[MAIN-RUN-P] new path is" + path);
		//transmets ce path a vehicule
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = 
			      new HttpEntity<String>(path.toString(), headers);
		this.restTemplate.put("http://127.0.0.1:8070/setPath/"+v.getId(), request);
	}
	
	public boolean hasEnough(Vehicle v, FireDto feu) {
		boolean res1 = canGoThereAndBack(v, feu);
		boolean res2 = canStopFire(v, feu);
		return (res1 && res2);
	}
	
	public boolean canGoThereAndBack(Vehicle v, FireDto feu) {
		boolean res1 = false;
		ArrayList<ArrayList<Double>> path = wouldBePath(v, feu);
		List<Integer> LDist = new ArrayList<Integer>();
		path = wouldBePath(v, feu);
		Coord c1 = new Coord(v.getLon(), v.getLat());
		Coord c2 = new Coord();
		//total distance /2
		for (List<Double> nextStep:path) {
			c2 = new Coord(nextStep.get(0), nextStep.get(1));
			int distance = GisTools.computeDistance2(c1, c2);
			LDist.add(distance);
			c1 = new Coord(nextStep.get(0), nextStep.get(1));
			}
		int sum = 0;
		for (int dist:LDist) {sum +=dist;}
		sum *= 2;
		int tic = sum/200;
		tic++;
		double ticLeft = v.getFuel()/v.getFuelConsumption();
		if (ticLeft>tic) {res1 = true;}
		return (res1);
	}
	
	public boolean canStopFire(Vehicle v, FireDto feu) { //assume fires will be at 50 by time someones over there
		boolean res2 = false;
		int progress = (int) (0.8*v.getEfficiency()*v.getLiquidType().getEfficiency(feu.getType()) - 0.1);
		int tic = 50 / progress;
		int ticLeft = (int) (v.getLiquidQuantity()/v.getLiquidConsumption());
		if (ticLeft>tic) {res2 = true;}
		return res2;
	}
	
	public ArrayList<ArrayList<Double>> wouldBePath(Vehicle v, FireDto feu) {
		String Path = v.getLon()+","+v.getLat()+";"+feu.getLon()+","+feu.getLat();
		String url="https://api.mapbox.com/directions/v5/mapbox/driving/"+Path+"?alternatives=false&geometries=geojson&steps=false&access_token=pk.eyJ1IjoiZXJtaXphaGQiLCJhIjoiY2twaTJxdGRjMGY3MjJ1cGM1NDNqc3NsNyJ9.xxjbVbTAlxUklvOFvXG9Bw";
		String res = this.restTemplate.getForObject(url, String.class);
		// Traduit la requete pour creer un path
		try {
			this.jNode = mapper.readTree(res);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JsonNode jPath = jNode.get("routes").get(0).get("geometry").get("coordinates");
		ArrayList<ArrayList<Double>> path = new ArrayList<ArrayList<Double>>();
		for (JsonNode coord: jPath) {
			double lon = coord.get(0).asDouble();
			double lat = coord.get(1).asDouble();
			path.add(new ArrayList<>(List.of(lon, lat)));
		}
		
		return path;
	}
}
