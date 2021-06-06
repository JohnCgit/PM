package com.webAppEmergency.Fire;

@Entity
public class FireConf {
	
	public FireConfBehavior behavior;
	public FireConfCreation creation;
	
	public FireConf() {}

	public FireConfBehavior getBehavior() {
		return behavior;
	}

	public void setBehavior(FireConfBehavior behavior) {
		this.behavior = behavior;
	}

	public FireConfCreation getCreation() {
		return creation;
	}

	public void setCreation(FireConfCreation creation) {
		this.creation = creation;
	}

	@Override
	public String toString() {
		return "FireConf [behavior=" + behavior + ", creation=" + creation + "]";
	}
	
	

}
