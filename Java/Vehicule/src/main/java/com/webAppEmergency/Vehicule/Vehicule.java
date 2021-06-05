package com.webAppEmergency.Vehicule;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.project.model.dto.Coord;
import com.project.model.dto.LiquidType;
import com.project.model.dto.VehicleType;

@Entity
public class Vehicule {

	@Id
	@GeneratedValue
	private int realid;
	private int idVehicle;
	private VehiculeType type;
	private double efficiency;
	private double liquidQuantity;
	private double fuel;
	private int crewMember;
	private int facilityRefID;
	private Etat etat=Etat.DISPONIBLE;
	private double lon;
	private double lat;
	private Integer idFire; 
	
	@ElementCollection
	private List<ArrayList<Float>> Path;

	
	public Vehicule() {
	}

	public Vehicule(float lon, float lat, VehiculeType type, double efficiency,
			double liquidQuantity, double fuel, int crewMember, int facilityRefID) {

		super();
		this.lon=lon;
		this.lat=lat;
		this.type = type;
		this.efficiency = efficiency;
		this.fuel = fuel;
		this.crewMember = crewMember;
		this.facilityRefID = facilityRefID;
		this.Path=new ArrayList<ArrayList<Float>>();
		this.idFire=-1;
		this.idVehicle=-1;
	}

//////////////////////////////////////
//Getter & Setter
//////////////////////////////////////
	
	@Override
	public String toString() {
		return "Vehicule [realid=" + realid + ", idVehicle=" + idVehicle + ", type=" + type + ", efficiency=" + efficiency + ", liquidQuantity="
				+ liquidQuantity + ", fuel=" + fuel + ", crewMember=" + crewMember + ", facilityRefID=" + facilityRefID
				+ ", Etat=" + etat + ", lon=" + lon + ", lat=" + lat + ", idFire=" + idFire + ", Path=" + Path + 
				", LiquidType= "+ type.getLiquidType()+"]";
	}
	
	
//	public Coord getCoord() {
//		return coord;
//	}
//
//	public void setCoord(Coord coord) {
//		this.coord = coord;
//	}
	

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public int getIdVehicle() {
		return idVehicle;
	}

	public void setIdVehicle(int idVehicle) {
		this.idVehicle = idVehicle;
	}

	public double getLat() {
		return lat;
	}
	
	public VehiculeType getType() {
		return type;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public void setType(VehiculeType type) {
		this.type = type;
	}

	public double getEfficiency() {
		return efficiency;
	}

	public void setEfficiency(double efficiency) {
		this.efficiency = efficiency;
	}

	public double getLiquidQuantity() {
		return liquidQuantity;
	}

	public void setLiquidQuantity(float liquidQuantity) {
		this.liquidQuantity = liquidQuantity;
	}

	public double getFuel() {
		return fuel;
	}

	public void setFuel(float fuel) {
		this.fuel = fuel;
	}

	public int getCrewMember() {
		return crewMember;
	}

	public void setCrewMember(int crewMember) {
		this.crewMember = crewMember;
	}

	public int getFacilityRefID() {
		return facilityRefID;
	}

	public void setFacilityRefID(int facilityRefID) {
		this.facilityRefID = facilityRefID;
	}

	public int getRealid() {
		return realid;
	}

	public void setRealid(int realid) {
		this.realid = realid;
	}

	public Etat getEtat() {
		return etat;
	}

	public void setEtat(Etat etat) {
		this.etat = etat;
	}

	public List<ArrayList<Float>> getPath() {
		return Path;
	}

	public void setPath(List<ArrayList<Float>> path) {
		Path = path;
	}

	public Integer getIdFire() {
		return idFire;
	}

	public void setIdFire(Integer idFire) {
		this.idFire = idFire;
	}

}
