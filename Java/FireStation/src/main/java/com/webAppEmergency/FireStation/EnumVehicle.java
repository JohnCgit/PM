package com.webAppEmergency.FireStation;


public enum EnumVehicle {
	/*
	 * NOM (
	 * place de parking?
	 * Capacite equipage
	 * LiquidType (All)
	 * LiquidQuantity
	 * LiquidConsumption
	 * FuelQuantity
	 * FuelConsumption
	 * )
	 * */
	CAR(2,2, 5, 0.5, 50,5),
	FIRE_ENGINE(6,6, 10, 1, 100, 10),
	PUMPER_TRUCK(6,4, 100, 10, 1000, 100),
	WATER_TENDERS(4,2, 50, 5, 500, 50), 
	TURNTABLE_LADDER_TRUCK(10,5, 20, 2, 200, 20),
	TRUCK(10,8, 40, 4, 400, 40);
	
	private int spaceUsedInFacility;
	private int vehicleCrewCapacity;
	private int liquidQuantityMax;
	private double liquidConsumption;
	private int fuelQuantityMax;
	private double fuelConsumption;
	
	private EnumVehicle(int spaceUsedInFacility,int vehicleCrewCapacity,
			int liquidQuantity, double liquidConsumption, int fuelQuantity, double fuelConsumption) {
		this.spaceUsedInFacility=spaceUsedInFacility;
		this.vehicleCrewCapacity=vehicleCrewCapacity;
		this.liquidQuantityMax=liquidQuantity;
		this.liquidConsumption=liquidConsumption;
		this.fuelQuantityMax=fuelQuantity;
		this.fuelConsumption=fuelConsumption;
		
	}
	
	public int getSpaceUsedInFacility() {
		return this.spaceUsedInFacility;
	}
	
	public int getVehicleCrewCapacity() {
		return this.vehicleCrewCapacity;
	}

	public int getLiquidQuantity() {
		return liquidQuantityMax;
	}

	public void setLiquidQuantity(int liquidQuantity) {
		this.liquidQuantityMax = liquidQuantity;
	}

	public double getLiquidConsumption() {
		return liquidConsumption;
	}

	public void setLiquidConsumption(double liquidConsumption) {
		this.liquidConsumption = liquidConsumption;
	}

	public int getFuelQuantity() {
		return fuelQuantityMax;
	}

	public void setFuelQuantity(int fuelQuantity) {
		this.fuelQuantityMax = fuelQuantity;
	}

	public double getFuelConsumption() {
		return fuelConsumption;
	}

	public void setFuelConsumption(double fuelConsumption) {
		this.fuelConsumption = fuelConsumption;
	}

	public void setSpaceUsedInFacility(int spaceUsedInFacility) {
		this.spaceUsedInFacility = spaceUsedInFacility;
	}

	public void setVehicleCrewCapacity(int vehicleCrewCapacity) {
		this.vehicleCrewCapacity = vehicleCrewCapacity;
	}
}
