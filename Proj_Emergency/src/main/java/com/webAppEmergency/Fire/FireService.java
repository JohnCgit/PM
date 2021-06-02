package com.webAppEmergency.Fire;


import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.project.model.dto.FireDto;

//Gestion des feux
@Service
public class FireService{

	private final RestTemplate restTemplate;
	
	
	public FireService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }
	
	//Récupère tous les feux
	public List<FireDto> getAllFire() {
		FireDto[] L_fire = this.restTemplate.getForObject("http://127.0.0.1:8081/fire", FireDto[].class);
		List<FireDto> fList=new ArrayList<FireDto>();
		for(FireDto f: L_fire) {
			fList.add(f);
		}
		return fList;
	}
	
	public FireDto getFire(int id) {
		FireDto res=null;
		FireDto[] L_fire = this.restTemplate.getForObject("http://127.0.0.1:8081/fire", FireDto[].class);
		for(FireDto f: L_fire) {
			if(f.getId()==id) {
				res = f;
			}
		}
		return res;
	}

}