package com.webAppEmergency.Fire;

public class FireConfBehavior {
	
	private double propagationThreshold = 5.0;
	private double attenuationFactor = 0.8;
	private double intensityReplicationThreshold = 10.0;
    private double replicationProbability = 0.0;
    private double maxFireRange = 50.0;
    private double maxFireIntensity = 50.0;
    private double intensity_inc = 0.1;
    private int sleepTime = 5000;

    public FireConfBehavior() {}

	public double getPropagationThreshold() {
		return propagationThreshold;
	}

	public void setPropagationThreshold(double propagationThreshold) {
		this.propagationThreshold = propagationThreshold;
	}

	public double getAttenuationFactor() {
		return attenuationFactor;
	}

	public void setAttenuationFactor(double attenuationFactor) {
		this.attenuationFactor = attenuationFactor;
	}

	public double getIntensityReplicationThreshold() {
		return intensityReplicationThreshold;
	}

	public void setIntensityReplicationThreshold(double intensityReplicationThreshold) {
		this.intensityReplicationThreshold = intensityReplicationThreshold;
	}

	public double getReplicationProbability() {
		return replicationProbability;
	}

	public void setReplicationProbability(double replicationProbability) {
		this.replicationProbability = replicationProbability;
	}

	public double getMaxFireRange() {
		return maxFireRange;
	}

	public void setMaxFireRange(double maxFireRange) {
		this.maxFireRange = maxFireRange;
	}

	public double getMaxFireIntensity() {
		return maxFireIntensity;
	}

	public void setMaxFireIntensity(double maxFireIntensity) {
		this.maxFireIntensity = maxFireIntensity;
	}

	public double getIntensity_inc() {
		return intensity_inc;
	}

	public void setIntensity_inc(double intensity_inc) {
		this.intensity_inc = intensity_inc;
	}

	public int getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}

	@Override
	public String toString() {
		return "FireConfBehavior [propagationThreshold=" + propagationThreshold + ", attenuationFactor="
				+ attenuationFactor + ", intensityReplicationThreshold=" + intensityReplicationThreshold
				+ ", replicationProbability=" + replicationProbability + ", maxFireRange=" + maxFireRange
				+ ", maxFireIntensity=" + maxFireIntensity + ", intensity_inc=" + intensity_inc + ", sleepTime="
				+ sleepTime + "]";
	}
}
