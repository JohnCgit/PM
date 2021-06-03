package com.webAppEmergency.Caserne.CaserneDto;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FeatureType {
    FEATURE;

    @JsonValue
    public String toValue() {
        switch (this) {
        case FEATURE: return "Feature";
        }
        return null;
    }

    @JsonCreator
    public static FeatureType forValue(String value) throws IOException {
        if (value.equals("Feature")) return FEATURE;
        throw new IOException("Cannot deserialize FeatureType");
    }
}