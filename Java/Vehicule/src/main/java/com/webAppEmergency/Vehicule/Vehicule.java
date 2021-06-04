package com.webAppEmergency.Vehicule;

import java.util.List;

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
	private VehiculeType type;
	private float efficiency;
	private float liquidQuantity;
	private float fuel;
	private int crewMember;
	private int facilityRefID;
	private Etat Etat;
	private Coord coord;
	private List<Coord> Path;
	private Integer idFire;
	public Vehicule() {
	}

	public Vehicule(int id, float lon, float lat, VehiculeType type, float efficiency, LiquidType liquidType,
			float liquidQuantity, float liquidConsumption, float fuel, float fuelConsumption, int crewMember,
			int crewMemberCapacity, int facilityRefID) {

		super();
		this.coord = new Coord(lon, lat);
		this.type = type;
		this.efficiency = efficiency;
		this.fuel = fuel;
		this.crewMember = crewMember;
		this.facilityRefID = facilityRefID;
		this.Etat = Etat.DISPONIBLE;
		this.Path=null;
		this.idFire=-1;
	}

//////////////////////////////////////
//Getter & Setter
//////////////////////////////////////

	public VehiculeType getType() {
		return type;
	}

	public void setType(VehiculeType type) {
		this.type = type;
	}

	public float getEfficiency() {
		return efficiency;
	}

	public void setEfficiency(float efficiency) {
		this.efficiency = efficiency;
	}

	public float getLiquidQuantity() {
		return liquidQuantity;
	}

	public void setLiquidQuantity(float liquidQuantity) {
		this.liquidQuantity = liquidQuantity;
	}

	public float getFuel() {
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
		return Etat;
	}

	public void setEtat(Etat etat) {
		Etat = etat;
	}

	public Coord getCoord() {
		return coord;
	}

	public void setCoord(Coord coord) {
		this.coord = coord;
	}

	public List<Coord> getPath() {
		return Path;
	}

	public void setPath(List<Coord> path) {
		Path = path;
	}

	public Integer getIdFire() {
		return idFire;
	}

	public void setIdFire(Integer idFire) {
		this.idFire = idFire;
	}

}
