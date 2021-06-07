package com.webAppEmergency.Vehicle;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.project.model.dto.LiquidType;

@Entity
public class Vehicle {

	@Id
	@GeneratedValue
	private int id;
	private int idFs=-1;
	private EnumVehicle type;
	
	private double efficiency = 5.0;
	private double liquidQuantity=250.0;
	private double fuel=100.0;
	private int crewMember;
	private int fireStationID;
	private State state=State.DISPONIBLE;
	private double lon;
	private double lat;
	private Integer idFire; 
	private LiquidType liquidType;
	private int deplacement;
	
	@ElementCollection
	private List<ArrayList<Double>> Path = new ArrayList<ArrayList<Double>>();

	public Vehicle() {
	}

	public Vehicle(EnumVehicle type, int fireStationID, LiquidType liquidType) {

		super();
		this.type = type;
		this.fireStationID = fireStationID;
		this.liquidType = liquidType;
	}

//////////////////////////////////////
//Getter & Setter
//////////////////////////////////////
	
	@Override
	public String toString() {
		return "Vehicle [id=" + id + ", idFs=" + idFs + ", type=" + type + ", efficiency=" + efficiency + ", liquidQuantity="
				+ liquidQuantity + ", fuel=" + fuel + ", crewMember=" + crewMember + ", facilityRefID=" + fireStationID
				+ ", Etat=" + state + ", lon=" + lon + ", lat=" + lat + ", idFire=" + idFire + ", Path=" + Path + 
				", LiquidType= "+ getLiquidType()+"]";
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getLat() {
		return lat;
	}
	
	public EnumVehicle getType() {
		return type;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public void setType(EnumVehicle type) {
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

	public void setLiquidQuantity(double d) {
		this.liquidQuantity = d;
	}

	public double getFuel() {
		return fuel;
	}

	public void setFuel(double d) {
		this.fuel = d;
	}

	public int getCrewMember() {
		return crewMember;
	}

	public void setCrewMember(int crewMember) {
		this.crewMember = crewMember;
	}

	public int getIdFs() {
		return idFs;
	}

	public void setIdFs(int idFs) {
		this.idFs = idFs;
	}

	public State getEtat() {
		return state;
	}

	public void setEtat(State state) {
		this.state = state;
	}

	public List<ArrayList<Double>> getPath() {
		return Path;
	}

	public void setPath(List<ArrayList<Double>> path) {
		Path = path;
	}

	public Integer getIdFire() {
		return idFire;
	}

	public void setIdFire(Integer idFire) {
		this.idFire = idFire;
	}
	

	public double getLiquidConsumption() {
		return type.getLiquidConsumption();
	}
	
	public int getLiquidQuantityMax() {
		return type.getLiquidQuantity();
	}
	
	public double getFuelConsumption() {
		return type.getFuelConsumption();
	}
	
	public int getFuelQuantityMax() {
		return type.getFuelQuantity();
	}
	
	public int getVehicleCrewCapacity() {
		return type.getVehicleCrewCapacity();
	}

	public void setLiquidType(LiquidType liquidType) {
		this.liquidType = liquidType;
	}
	
	public LiquidType getLiquidType() {
		return liquidType;
	}

	public int getFireStationID() {
		return fireStationID;
	}	
	
	public void setFireStationID(int fireStationID) {
		this.fireStationID= fireStationID;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public int getDeplacementType() {
		return type.getDeplacement();
	}

	public void setDeplacementType(int deplacement) {
		this.type.setDeplacement(deplacement);
	}

	public int getDeplacement() {
		return deplacement;
	}

	public void setDeplacement(int deplacement) {
		this.deplacement = deplacement;
	}
	
	
	
}
