package com.webAppEmergency.Assignation;

import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import com.project.model.dto.Coord;
import com.project.model.dto.FireDto;

import java.util.List;

public class MoveRunnable implements Runnable{

	private RestTemplate restTemplate;
	
//////////////////////////////////////
//Rest template
//////////////////////////////////////

	public MoveRunnable(RestTemplateBuilder restTemplateBuilder) { // Gestion du rest template
	this.restTemplate = restTemplateBuilder.build();
	}

	@Override
	public void run() {
		try{
			Thread.sleep(1000);
			Vehicule[] tabVehicule = this.restTemplate.getForObject("http://127.0.0.1:8070/vehicule/getAll", Vehicule[].class);
			for (Vehicule v: tabVehicule) {
				switch (v.getEtat()) {
				case ALLER:
					this.restTemplate.put("http://127.0.0.1:8070/followPath/"+v.getRealid(), null);
					if (v.getPath()==null) {
						JSONObject body = new JSONObject();
						body.put("Etat", Etat.EXTINCTION);
						this.restTemplate.put("http://127.0.0.1/update/"+v.getRealid(), body);
					}
					break;
				case EXTINCTION:
					FireDto fire = this.restTemplate.getForObject("http://127.0.0.1:8090/get/"+v.getIdFire(), FireDto.class);
					if (fire.getIntensity()<0) {
						JSONObject body = new JSONObject();
						body.put("Etat", Etat.RETOUR);
						this.restTemplate.put("http://127.0.0.1/update/"+v.getRealid(), body);
					}
					break;
				case RETOUR:
					this.restTemplate.put("http://127.0.0.1:8070/followPath/"+v.getRealid(), null);
					if (v.getPath()==null) {
						JSONObject body = new JSONObject();
						body.put("Etat", Etat.DISPONIBLE);
						this.restTemplate.put("http://127.0.0.1/update/"+v.getRealid(), body);
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