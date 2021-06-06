package com.webAppEmergency.Fire;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.model.dto.FireDto;

//Gestion des feux
@Service
public class FireService{

	private final RestTemplate restTemplate;
	ObjectMapper mapper ;
	JsonNode jNode;
	FireConfBehavior behavior;
	FireConfCreation creation;
	
//////////////////////////////////////
//Init
//////////////////////////////////////
	
	public FireService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
        this.mapper = new ObjectMapper();
        this.behavior = new FireConfBehavior();
        this.creation = new FireConfCreation();
	}
	
//////////////////////////////////////
//Get fire
//////////////////////////////////////
	
	//Recupere tous les feux
	public List<FireDto> getAllFire() {
		FireDto[] Tab_fire = this.restTemplate.getForObject("http://127.0.0.1:8081/fire", FireDto[].class);
		List<FireDto> fList=new ArrayList<FireDto>();
		for(FireDto f: Tab_fire) {
			fList.add(f);
		}
		return fList;
	}

		public FireDto getFire(int id) {
		FireDto res=null;
		FireDto[] Tab_fire = this.restTemplate.getForObject("http://127.0.0.1:8081/fire", FireDto[].class);
		for(FireDto f: Tab_fire) {
			if(f.getId()==id) {
				res = f;
			}
		}
		return res;
	}
		
//////////////////////////////////////
//Creation fire
//////////////////////////////////////
		
		// Lance un seul feu
		public void startOneFire() throws InterruptedException, IOException {
			int freq = this.creation.getFireCreationSleep();
			double prob = this.creation.getFireCreationProbability();
			probFire(1.0);
			freqFire(100);
			Thread.sleep(105);
			freqFire(freq);
			probFire(prob);
		}
		
		public void freqFire(int freq) {
			this.creation.setFireCreationSleep(freq);
			updateCreaSim();
		}
		
		public void probFire(double prob) {
			this.creation.setFireCreationProbability(prob);;
			updateCreaSim();
		}
		
		public void updateCreaSim() {
			JSONObject body = new JSONObject();
		    body.put("fireCreationProbability", this.creation.getFireCreationProbability());
		    body.put("fireCreationSleep", this.creation.getFireCreationSleep());
		    body.put("max_INTENSITY", this.creation.getMax_INTENSITY());
		    body.put("max_RANGE", this.creation.getMax_RANGE());
		    
		    List<Integer> c1 = this.creation.getTopLeft();
		    List<Integer> c2 = this.creation.getBottomRight();
		    
		    JSONObject jObj1 = new JSONObject();
		    jObj1.put("type", "Point");
		    jObj1.put("coordinates",c1);
		    JSONObject jObj2 = new JSONObject();
		    jObj2.put("type", "Point");
		    jObj2.put("coordinates",c2);
		    List<JSONObject> ListJObj=new ArrayList<JSONObject>(List.of(jObj1, jObj2));
		    
		    body.put("fireCreationZone", ListJObj);
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<String> request = 
				      new HttpEntity<String>(body.toString(), headers);
			this.restTemplate.put("http://127.0.0.1:8081/config/creation", request);
		}
		
//////////////////////////////////////
//Behavior fire?
//////////////////////////////////////
}