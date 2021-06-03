package com.webAppEmergency.Assignation;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;

@Entity
public class Assignation {
	@Id
	@GeneratedValue
	private int id;
	private int idVehicule;
	private int idFeu;
	
	public Assignation() {}
	
	public Assignation(int idFeu) {
		this.idFeu=idFeu;
		this.idVehicule=-1;
	}
	
	public Assignation(int idFeu, int idVehicule) {
		this.idFeu=idFeu;
		this.idVehicule=idVehicule;
	}
	
//////////////////////////////////////
// Getter & Setter
//////////////////////////////////////
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdVehicule() {
		return idVehicule;
	}

	public void setIdVehicule(int idVehicule) {
		this.idVehicule = idVehicule;
	}

	public int getIdFeu() {
		return idFeu;
	}

	public void setIdFeu(int idFeu) {
		this.idFeu = idFeu;
	}
	
	
}
