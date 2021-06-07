package com.webAppEmergency.FireStation;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class FireStation {

	@Id
	@GeneratedValue
	private int id;
	private String libelle;
	private double lon;
	private double lat;
	private int maxCapacity;
	
	//@Transient
	//private Coord coord;
	
	@ElementCollection
	private List<Integer> ListVehicles = new ArrayList<Integer>(List.of(-1));
	
	@ElementCollection
	private List<Integer> Listfireman = new ArrayList<Integer>(List.of(-1));
	
	public FireStation() {}
	
	public FireStation(double lon, double lat, String libelle, List<Integer> ListVehicles, List<Integer> Listfireman, int maxCapacity) {
		
		super();
		//this.coord=new Coord(lon, lat);
		this.lon = lon;
		this.lat = lat;
		this.libelle = libelle;
		this.ListVehicles = ListVehicles;
		this.Listfireman = Listfireman;
		this.maxCapacity = maxCapacity;

	}
	
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

	public List<Integer> getListVehicles() {
		return ListVehicles;
	}

	public void setListVehicles(List<Integer> listVehicules) {
		ListVehicles = listVehicules;
	}

	public List<Integer> getListfireman() {
		return Listfireman;
	}

	public void setListfireman(List<Integer> listfireman) {
		Listfireman = listfireman;
	}

	public int getMaxCapacity() {
		return maxCapacity;
	}

	public void setMaxCapacity(int capaciteMax) {
		this.maxCapacity = capaciteMax;
	}
	
	@Override
	public String toString() {
		return "Caserne [id=" + id + ", libelle=" + libelle + ", lon=" + lon + ", lat=" + lat + ", capaciteMax="
				+ maxCapacity + ", ListVehicules=" + ListVehicles + ", Listfireman=" + Listfireman + "]";
	}
}