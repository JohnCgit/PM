package com.webAppEmergency.Caserne;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CaserneService {

	@Autowired
	CaserneRepository cRepo;
	ObjectMapper mapper;

	
	private RestTemplate restTemplate;
	public String url;
	public String path;

		
	public CaserneService(RestTemplateBuilder restTemplateBuilder) throws UnsupportedEncodingException { // Gestion du rest template
        this.restTemplate = restTemplateBuilder.build();
        this.url="https://download.data.grandlyon.com/wfs/grandlyon?SERVICE=WFS&VERSION=2.0.0&request=GetFeature&typename=adr_voie_lieu.adrsecourspct&outputFormat=application/json;subtype=geojson&SRSNAME=EPSG:4171&startIndex=0&count=100";
        this.path ="/home/tp/git/PM/Java/Caserne/src/main/java/com/webAppEmergency/Caserne/grandlyon.json";
    	mapper = new ObjectMapper();
	}

	public List<Caserne> getAll() {
		return cRepo.findAll();
	}
	
	public Caserne getCaserne(int realid) {
		Caserne res=null;
		Optional<Caserne> oCaserne = cRepo.findById(realid);
		if (oCaserne.isPresent()){res=oCaserne.get();}
		return res;
	}
	
	public void addCaserne(Caserne c) {
	  	cRepo.save(c);
	}
	
	public void initCaserne() {
		if(cRepo.findAll().isEmpty()) {
			List<Caserne> ListC = new ArrayList<>();
			ListC.add(new Caserne(4.855997011435769, 45.8295890450427, "Centre d'Intervention des 2 Fontaines", Arrays.asList(), Arrays.asList(1, 2, 3), 15));
			ListC.add(new Caserne(4.819356307585479, 45.89601102593775, "Centre d'Intervention de Genay-Neuville", Arrays.asList(), Arrays.asList(4, 5), 10));
			ListC.add(new Caserne(4.715254999410806, 45.824827365743985, "Centre d'Intervention de La Tour-de-Salvagny", Arrays.asList(), Arrays.asList(6, 7, 8, 9), 12));
			for (Caserne c: ListC) {cRepo.save(c);}
		}
	}

	public void getCaserneLyon() throws FileNotFoundException, IOException, ParseException {
	     JSONParser jsonP = new JSONParser();
	     
         JSONObject jsonO = (JSONObject)jsonP.parse(new FileReader(this.path));
         System.out.println(jsonO);
         String name = (String) jsonO.get("name");
         System.out.println("Name :"+ name);
         JSONArray features = (JSONArray) jsonO.get("features");
         System.out.println("Features :"+ features);
//         ObjectReader jNode = this.mapper.reader();
//         System.out.println(jNode);
	     
//         String jsonO = (String)jsonP.parse(new FileReader(this.path));
//         System.out.println(jsonO);
//         JsonNode jNode = this.mapper.readTree(jsonO);
//         System.out.println(jNode);
//         JsonNode name = jNode.get("features").get(0).get("properties").get("nom");
//         System.out.println(name);
//         JsonNode lon = jNode.get("features").get(0).get("geometry").get("coordinates").get(0);
//         System.out.println(lon);
//         JsonNode lat = jNode.get("features").get(0).get("geometry").get("coordinates").get(1);
//         System.out.println(lat);


	}
	
//	//Récupérer informations Casernes de Lyon
//	public void getCaserneLyonv1() {
//		CaserneDto json = this.restTemplate.getForObject(this.url,CaserneDto.class);
//		System.out.println(json);
//	}
	
	public void getCaserneLyonv2() throws JsonMappingException, JsonProcessingException {
		restTemplate = new RestTemplate();
		String json = restTemplate.getForObject(this.path, String.class);
		System.out.println(json);
//		JsonNode jNode = this.mapper.readTree(json);
//		System.out.println(jNode);
//		JsonNode truc = jNode.get("routes").get(0).get("geometry");
//		System.out.println(truc);
		
		JsonNode jNode;
		try {
			jNode = this.mapper.readTree(json);
			System.out.println(jNode);
			JsonNode nom = jNode.get("features").get(0).get("properties").get("nom");
			System.out.println(nom);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}