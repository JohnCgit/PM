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
	
	public FireService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
        this.mapper = new ObjectMapper();
    }
	
	//Récupère tous les feux
	public List<FireDto> getAllFire() {
		FireDto[] Tab_fire = this.restTemplate.getForObject("http://127.0.0.1:8081/fire", FireDto[].class);
		List<FireDto> fList=new ArrayList<FireDto>();
		for(FireDto f: Tab_fire) {
			fList.add(f);
		}
		return fList;
	}
	
	public void startOneFire() throws InterruptedException, IOException {
		int currentFreq=getFreqFire();
		freqFire(100);
		startFire();
		Thread.sleep(105);
		stopFire();
		freqFire(currentFreq);
	}
	
	public void startFire() {
		JSONObject body = new JSONObject();
	    body.put("fireCreationProbability", 1.0);
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = 
			      new HttpEntity<String>(body.toString(), headers);
		this.restTemplate.put("http://127.0.0.1:8081/config/creation", request);
	}
	
	public void stopFire() {
		JSONObject body = new JSONObject();
	    body.put("fireCreationProbability", 0.0);
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = 
			      new HttpEntity<String>(body.toString(), headers);
		this.restTemplate.put("http://127.0.0.1:8081/config/creation", request);
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
		
		public void freqFire(int freq) {
			JSONObject body = new JSONObject();
		    body.put("fireCreationSleep", freq);
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<String> request = 
				      new HttpEntity<String>(body.toString(), headers);
			this.restTemplate.put("http://127.0.0.1:8081/config/creation", request);
		}
		
		public int getFreqFire() throws IOException {
			String response = this.restTemplate.getForObject("http://127.0.0.1:8081/config/creation", String.class)	;				
			this.jNode=mapper.readTree(response);
			int res = jNode.get("fireCreationSleep").asInt();
			return res;
		}
		
		public void probFire(double prob) {
			JSONObject body = new JSONObject();
		    body.put("fireCreationProbability", prob);
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<String> request = 
				      new HttpEntity<String>(body.toString(), headers);
			this.restTemplate.put("http://127.0.0.1:8081/config/creation", request);
		}
}