package com.webAppEmergency.Fire;

import java.util.List;
import java.util.ArrayList;

public class FireConfCreation {
	
	private double fireCreationProbability = 0.0;
	private int fireCreationSleep = 20000;
	private double max_INTENSITY = 50.0;
	private double max_RANGE = 50.0;
	private List<Integer> TopLeft = new ArrayList<Integer>(List.of(520820, 5719535));
	private List<Integer> BottomRight = new ArrayList<Integer>(List.of(566984, 5754240));
	
	public FireConfCreation() {}

	public double getFireCreationProbability() {
		return fireCreationProbability;
	}

	public void setFireCreationProbability(double fireCreationProbability) {
		this.fireCreationProbability = fireCreationProbability;
	}

	public int getFireCreationSleep() {
		return fireCreationSleep;
	}

	public void setFireCreationSleep(int fireCreationSleep) {
		this.fireCreationSleep = fireCreationSleep;
	}

	public double getMax_INTENSITY() {
		return max_INTENSITY;
	}

	public void setMax_INTENSITY(double max_INTENSITY) {
		this.max_INTENSITY = max_INTENSITY;
	}

	public double getMax_RANGE() {
		return max_RANGE;
	}

	public void setMax_RANGE(double max_RANGE) {
		this.max_RANGE = max_RANGE;
	}

	public List<Integer> getTopLeft() {
		return TopLeft;
	}

	public void setTopLeft(List<Integer> topLeft) {
		TopLeft = topLeft;
	}

	public List<Integer> getBottomRight() {
		return BottomRight;
	}

	public void setBottomRight(List<Integer> bottomRight) {
		BottomRight = bottomRight;
	}

	@Override
	public String toString() {
		return "FireConfCrea [fireCreationProbability=" + fireCreationProbability + ", fireCreationSleep="
				+ fireCreationSleep + ", max_INTENSITY=" + max_INTENSITY + ", max_RANGE=" + max_RANGE + ", TopLeft="
				+ TopLeft + ", BottomRight=" + BottomRight + "]";
	}
	
	
}
