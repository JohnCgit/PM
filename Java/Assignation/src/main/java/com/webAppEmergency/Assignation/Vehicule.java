package com.webAppEmergency.Assignation;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.project.model.dto.Coord;

@Entity
public class Vehicule {

	@Id
	@GeneratedValue
	private int realid;
	private String type;
	private float efficiency;
	private String liquidType;
	private float liquidQuantity;
	private float liquidConsumption;
	private float fuel;
	private float fuelConsumption;
	private int crewMember;
	private int crewMemberCapacity;
	private int facilityRefID;
	private String Etat;
	private Coord coord;
	
	public Vehicule() {}
	
	public Vehicule(int id, float lon, float lat, String type, float efficiency, String liquidType,
			float liquidQuantity, float liquidConsumption, float fuel, float fuelConsumption,
			int crewMember, int crewMemberCapacity, int facilityRefID) {
		
		super();
		this.type=type;
		this.efficiency=efficiency;
		this.liquidType=liquidType;
		this.liquidQuantity=liquidQuantity;
		this.liquidConsumption=liquidConsumption;
		this.fuel=fuel;
		this.fuelConsumption=fuelConsumption;
		this.crewMember=crewMember;
		this.crewMemberCapacity=crewMemberCapacity;
		this.facilityRefID=facilityRefID;
		this.Etat="chill";
		this.coord=new Coord(lon, lat);
		
	}
	
//////////////////////////////////////
//Getter & Setter
//////////////////////////////////////
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public float getEfficiency() {
		return efficiency;
	}

	public void setEfficiency(float efficiency) {
		this.efficiency = efficiency;
	}

	public String getLiquidType() {
		return liquidType;
	}

	public void setLiquidType(String liquidType) {
		this.liquidType = liquidType;
	}

	public float getLiquidQuantity() {
		return liquidQuantity;
	}

	public void setLiquidQuantity(float liquidQuantity) {
		this.liquidQuantity = liquidQuantity;
	}

	public float getLiquidConsumption() {
		return liquidConsumption;
	}

	public void setLiquidConsumption(float liquidConsumption) {
		this.liquidConsumption = liquidConsumption;
	}

	public float getFuel() {
		return fuel;
	}

	public void setFuel(float fuel) {
		this.fuel = fuel;
	}

	public float getFuelConsumption() {
		return fuelConsumption;
	}

	public void setFuelConsumption(float fuelConsumption) {
		this.fuelConsumption = fuelConsumption;
	}

	public int getCrewMember() {
		return crewMember;
	}

	public void setCrewMember(int crewMember) {
		this.crewMember = crewMember;
	}

	public int getCrewMemberCapacity() {
		return crewMemberCapacity;
	}

	public void setCrewMemberCapacity(int crewMemberCapacity) {
		this.crewMemberCapacity = crewMemberCapacity;
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

	public String getEtat() {
		return Etat;
	}

	public void setEtat(String etat) {
		Etat = etat;
	}

	public Coord getCoord() {
		return coord;
	}

	public void setCoord(Coord coord) {
		this.coord = coord;
	}	
	
}
