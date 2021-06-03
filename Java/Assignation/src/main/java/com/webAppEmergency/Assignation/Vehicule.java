package com.webAppEmergency.Assignation;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.project.model.dto.Coord;
import com.project.model.dto.LiquidType;
import com.project.model.dto.VehicleType;

@Entity
public class Vehicule {

	@Id
	@GeneratedValue
	private int realid;
//	private float lon;
//	private float lat;
	private Coord coord;
	private VehicleType  type;
	private float efficiency;
	private LiquidType liquidType;
	private float liquidQuantity;
	private float liquidConsumption;
	private float fuel;
	private float fuelConsumption;
	private int crewMember;
	private int crewMemberCapacity;
	private int facilityRefID;
	private String Etat;
	
	
	public Vehicule() {}
	
	public Vehicule(int id, float lon, float lat, VehicleType  type, float efficiency, LiquidType liquidType,
			float liquidQuantity, float liquidConsumption, float fuel, float fuelConsumption,
			int crewMember, int crewMemberCapacity, int facilityRefID) {
		
		super();
//		this.lon=lon;
//		this.lat=lat;
		this.coord=new Coord(lon, lat);
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
		
	}
	
//////////////////////////////////////
//Getter & Setter
//////////////////////////////////////
	
	public VehicleType getType() {
		return type;
	}

	public void setType(VehicleType type) {
		this.type = type;
	}

	public float getEfficiency() {
		return efficiency;
	}

	public void setEfficiency(float efficiency) {
		this.efficiency = efficiency;
	}

	public LiquidType getLiquidType() {
		return liquidType;
	}

	public void setLiquidType(LiquidType liquidType) {
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

	public Coord getCoord() {
		return coord;
	}

	public void setCoord(Coord coord) {
		this.coord = coord;
	}

	public String getEtat() {
		return Etat;
	}

	public void setEtat(String etat) {
		Etat = etat;
	}

	
}
