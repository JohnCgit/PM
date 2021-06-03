package com.webAppEmergency.Caserne;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Caserne {

	@Id
	@GeneratedValue
	private int id;
	private String libelle;
	private float lon;
	private float lat;
	
	@ElementCollection
	private List<Integer> ListVehicules;
	
	@ElementCollection
	private List<Integer> ListPompiers;
	
	public Caserne() {}
	
	public Caserne(int id, float lon, float lat, String libelle, List<Integer> ListVehicules, List<Integer> ListPompiers) {
		
		super();
		this.lon = lon;
		this.lat = lat;
		this.libelle = libelle;
		this.ListVehicules = ListVehicules;
		this.ListPompiers = ListPompiers;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getLon() {
		return lon;
	}

	public void setLon(float lon) {
		this.lon = lon;
	}

	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public List<Integer> getListVehicules() {
		return ListVehicules;
	}

	public void setListVehicules(List<Integer> listVehicules) {
		ListVehicules = listVehicules;
	}

	public List<Integer> getListPompiers() {
		return ListPompiers;
	}

	public void setListPompiers(List<Integer> listPompiers) {
		ListPompiers = listPompiers;
	}
	
	
}