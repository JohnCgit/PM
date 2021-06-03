package com.webAppEmergency.Assignation;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import com.project.model.dto.FireDto;
import com.webAppEmergency.Assignation.Assignation;
import com.project.tools.GisTools;
import com.project.model.dto.Coord;



import java.util.List;

public class MainRunnable implements Runnable {

	boolean isEnd = false;
	RestTemplate restTemplate;
	public List<FireDto> List_Feu;
	RestTemplateBuilder restTemplateBuilder;
//////////////////////////////////////
//Rest template
//////////////////////////////////////

	public MainRunnable() { // Gestion du rest template
		this.restTemplate = restTemplateBuilder.build();
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
						Coord CoordFire=new Coord(feu.getLon(), feu.getLat());
						Vehicule v = ClosestVehicule(CoordFire);
						List_Feu.add(feu);
						//TODO send deplacement
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
	
	public Vehicule ClosestVehicule(Coord CoordFire) {
		Vehicule Tab_Vehicule[]=this.restTemplate.getForObject("http://127.0.0.1:8090/getAll", null);
		Vehicule res = new Vehicule();
		Integer minDistance = -1;
		//TODO hashmap vehicule, distance, regarder le type
		for (Vehicule v:Tab_Vehicule) {
			Coord CoordVehicule=new Coord(v.getLon(), v.getLat());
			Integer Distance=GisTools.computeDistance2(CoordVehicule, CoordFire);
			if (minDistance<=0 || minDistance>=Distance) {
				res=v;
				minDistance=Distance;
			}
		}
		return res;
	}

}
