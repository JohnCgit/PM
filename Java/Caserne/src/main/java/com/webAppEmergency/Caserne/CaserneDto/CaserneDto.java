package com.webAppEmergency.Caserne.CaserneDto;

import java.util.Map;

public class CaserneDto {
    private String type;
    private String name;
    private Feature[] features;

    public String getType() { return type; }
    public void setType(String value) { this.type = value; }

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    public Feature[] getFeatures() { return features; }
    public void setFeatures(Feature[] value) { this.features = value; }
}