package com.webAppEmergency.Caserne.CaserneDto;

import java.util.Map;

public class Feature {
    private FeatureType type;
    private Properties properties;
    private Geometry geometry;

    public FeatureType getType() { return type; }
    public void setType(FeatureType value) { this.type = value; }

    public Properties getProperties() { return properties; }
    public void setProperties(Properties value) { this.properties = value; }

    public Geometry getGeometry() { return geometry; }
    public void setGeometry(Geometry value) { this.geometry = value; }
}