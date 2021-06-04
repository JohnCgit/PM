package com.webAppEmergency.Caserne;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.project.model.dto.Coord;

@Entity
public class Caserne {

	@Id
	@GeneratedValue
	private int id;
	private String libelle;
	private double lon;
	private double lat;
	
	//@Transient
	//private Coord coord;
	
	@ElementCollection
	private List<Integer> ListVehicules;
	
	@ElementCollection
	private List<Integer> ListPompiers;
	
	public Caserne() {}
	
	public Caserne(double lon, double lat, String libelle, List<Integer> ListVehicules, List<Integer> ListPompiers) {
		
		super();
		//this.coord=new Coord(lon, lat);
		this.lon = lon;
		this.lat = lat;
		this.libelle = libelle;
		this.ListVehicules = ListVehicules;
		this.ListPompiers = ListPompiers;

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