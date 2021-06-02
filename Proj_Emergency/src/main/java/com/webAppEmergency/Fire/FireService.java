package com.webAppEmergency.Fire;

import java.util.Optional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

//Gestion des feux
@Service
public class FireService{

	private final RestTemplate restTemplate;
	
	
	public FireService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }
	
	//Récupère tous les feux
	public List<Fire> getAllFire() {
		List<Fire> L_fire = this.restTemplate.getForObject("http://127.0.0.1:8081/fire", List.class);
		return L_fire;
	}
	
	public Fire getFire(int id) {
		Fire res=null;
		List<Fire> L_fire = this.restTemplate.getForObject("http://127.0.0.1:8081/fire", List.class);
		System.out.println(L_fire);
		for (Fire feu:L_fire) {
			System.out.println(feu);
			if (feu.getId()==id) {
				res=feu;
			}
		}
		return res;
	}

//	//Permet de récupérer l'id d'un feu
//	public int getId(Fire fire) {
//		return fire.getId();
//	}



}