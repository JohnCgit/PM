package com.webAppEmergency.FireStation;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.project.model.dto.Coord;

@Entity
public class FireStation {

	@Id
	@GeneratedValue
	private int id;
	private String libelle;
	private double lon;
	private double lat;
	private int capaciteMax;
	
	//@Transient
	//private Coord coord;
	
	@ElementCollection
	private List<Integer> ListVehicles = new ArrayList<Integer>(List.of(-1));
	
	@ElementCollection
	private List<Integer> ListPompiers = new ArrayList<Integer>(List.of(-1));
	
	public FireStation() {}
	
	public FireStation(double lon, double lat, String libelle, List<Integer> ListVehicules, List<Integer> ListPompiers, int capaciteMax) {
		
		super();
		//this.coord=new Coord(lon, lat);
		this.lon = lon;
		this.lat = lat;
		this.libelle = libelle;
		this.ListVehicles = ListVehicules;
		this.ListPompiers = ListPompiers;
		this.capaciteMax = capaciteMax;

	}
/*
	public Coord getCoord() {
		return this.coord;
	}

	public void setCoord(Coord coord) {
		this.coord = coord;
	}
*/
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}
	
	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public List<Integer> getListVehicules() {
		return ListVehicles;
	}

	public void setListVehicules(List<Integer> listVehicules) {
		ListVehicles = listVehicules;
	}

	public List<Integer> getListPompiers() {
		return ListPompiers;
	}

	public void setListPompiers(List<Integer> listPompiers) {
		ListPompiers = listPompiers;
	}

	public int getCapaciteMax() {
		return capaciteMax;
	}

	public void setCapaciteMax(int capaciteMax) {
		this.capaciteMax = capaciteMax;
	}
	
	@Override
	public String toString() {
		return "Caserne [id=" + id + ", libelle=" + libelle + ", lon=" + lon + ", lat=" + lat + ", capaciteMax="
				+ capaciteMax + ", ListVehicules=" + ListVehicles + ", ListPompiers=" + ListPompiers + "]";
	}
}