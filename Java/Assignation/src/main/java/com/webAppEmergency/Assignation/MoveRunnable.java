package com.webAppEmergency.Assignation;

import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import com.project.model.dto.Coord;
import com.project.model.dto.FireDto;

import java.util.List;

public class MoveRunnable implements Runnable{

	private RestTemplate restTemplate;
	boolean isEnd = false;
	
//////////////////////////////////////
//Rest template
//////////////////////////////////////

	public MoveRunnable() { // Gestion du rest template
	this.restTemplate = new RestTemplate();
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
						this.restTemplate.put("http://127.0.0.1:8070/followPath/"+v.getRealid(), null);
						if (v.getPath()==null) {
							this.restTemplate.put("http://127.0.0.1:8070/state/"+v.getRealid()+",state=EXTINCTION", null);
						}
						break;
					case EXTINCTION:
						FireDto fire = this.restTemplate.getForObject("http://127.0.0.1:8090/get/"+v.getIdFire(), FireDto.class);
						if (fire.getIntensity()<0) {
							this.restTemplate.put("http://127.0.0.1:8070/state/"+v.getRealid()+",state=RETOUR", null);

						}
						break;
					case RETOUR:
						this.restTemplate.put("http://127.0.0.1:8070/followPath/"+v.getRealid(), null);
						if (v.getPath()==null) {
							this.restTemplate.put("http://127.0.0.1:8070/state/"+v.getRealid()+",state=DISPONIBLE", null);

						}
						break;
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
	
}