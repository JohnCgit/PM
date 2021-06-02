package com.webAppEmergency.Fire;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Fire {
	@Id
	@GeneratedValue
	private int id;
	private FireType type;
	private float intensity;
	private float range;
	private float lon;
	private float lat;
	
	public Fire() {
	}
	
	public Fire(int id,FireType type, float intensity, float range, float lon, float lat){
		super();
		this.type = type;
		this.setIntensity(intensity);
		this.setRange(range);
		this.lon=lon;
		this.lat=lat;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public FireType getType() {
		return type;
	}

	public void setType(FireType type) {
		this.type = type;
	}

	public float getIntensity() {
		return intensity;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}

	public float getRange() {
		return range;
	}

	public void setRange(float range) {
		this.range = range;
	}


	public float getLon() {
		return this.lon;
	}
	
	public float getLat() {
		return this.lat;
	}
	
	public void setLon(float lon) {
		this.lon=lon;
	}
	
	public void setLat(float lat) {
		this.lat=lat;
	}

	
	@Override
	public String toString() {
		return "FEU ["+this.id+"]: type "+this.type + " d'étendue " + this.range + " et d'intensité " + this.range + " aux coordonnées (" + this.lon + "," + this.lat + ")";
	}
	
}

