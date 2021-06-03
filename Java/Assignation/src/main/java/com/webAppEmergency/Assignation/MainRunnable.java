package com.webAppEmergency.Assignation;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import com.project.model.dto.FireDto;
import com.webAppEmergency.Assignation.Assignation;
import com.project.tools.GisTools;
import com.project.model.dto.Coord;
import com.webAppEmergency.Assignation.MoveRunnable;



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
						Vehicule v = PickVehicule1(feu);
						List_Feu.add(feu);
						deplacement(v, feu);
					}
				}

			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		
	}
	
	public void deplacement(Vehicule v, FireDto f) {
		MoveRunnable moveRunnable;
		Thread displayThread;
		
		//Create a Runnable is charge of executing cyclic actions 
		moveRunnable=new MoveRunnable(v, f);
		
		// A Runnable is held by a Thread which manage lifecycle of the Runnable
		displayThread=new Thread(moveRunnable);
		
		// The Thread is started and the method run() of the associated DisplayRunnable is launch
		displayThread.start();
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
			Integer Distance=GisTools.computeDistance2(v.getCoord(), CoordFire);
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
		Vehicule v = SelectVehiculeInCaserne(c, CoordFire, feu.getType());
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
	
	
	
//	public Vehicule SelectVehiculeInCaserne(Caserne c, Coord CoordFire, String type)
//	{
//		float efficacite=-1;
//		for (Integer idVehicule:c.getListVehicules()) {
//			Vehicule v=this.restTemplate.getForObject("http://127.0.0.1:8010/vehicule/id/"+idVehicule, Vehicule.class);
//			
//		}
//	}
}
