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
	
	@Transient
	private Coord coord;
	
	@ElementCollection
	private List<Integer> ListVehicules;
	
	@ElementCollection
	private List<Integer> ListPompiers;
	
	public Caserne() {}
	
	public Caserne(int id, float lon, float lat, String libelle, List<Integer> ListVehicules, List<Integer> ListPompiers) {
		
		super();
		this.coord=new Coord(lon, lat);
		this.libelle = libelle;
		this.ListVehicules = ListVehicules;
		this.ListPompiers = ListPompiers;

	}

	public Coord getCoord() {
		return coord;
	}

	public void setCoord(Coord coord) {
		this.coord = coord;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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