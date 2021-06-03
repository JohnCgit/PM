package com.webAppEmergency.Assignation;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import com.project.model.dto.Coord;
import com.project.model.dto.FireDto;

public class MoveRunnable implements Runnable{

	private String Step;
	private Vehicule Vehicule;
	private FireDto Fire;
	RestTemplate restTemplate;
	RestTemplateBuilder restTemplateBuilder;
	Coord c1;
	Coord c2;

	public MoveRunnable(Vehicule v, FireDto f) {
		this.Step="Debut";
		this.Vehicule=v;
		this.Fire=f;
		this.restTemplate = restTemplateBuilder.build();

		this.c1 = v.getCoord();
		this.c2 = new Coord(f.getLon(), f.getLat());
	}

	@Override
	public void run() {
		while (!this.Step.equals("End")) {
			try {
				Thread.sleep(10000); // wait 10sec
				v1();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
	
	private void v1() {
		switch(this.Step) {
		case "Debut":
			SimpleMove(this.c2);
			this.Step="Extinction";
			break;
		case "Extinction":
			updateFire();
			if (this.Fire.getIntensity()<=0) {
				this.Step="Retour";
			}
			break;
//		case "Retour":
//			SimpleMove(this.Vehicule.getFacilityRefID())
		}

	}
	
	private void SimpleMove(Coord coord) {
		String url="http://127.0.0.1:8070/move/"+this.Vehicule.getRealid() + "?lon=" + coord.getLon() + "&lat=" + coord.getLat();
		this.restTemplate.put(url, null);
	}
	
	private void updateFire() {
		this.Fire=this.restTemplate.getForObject("http://127.0.0.1:8090/get/"+this.Fire.getId(), FireDto.class);
	}

}
