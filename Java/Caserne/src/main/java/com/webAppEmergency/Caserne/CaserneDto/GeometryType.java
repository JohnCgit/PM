package com.webAppEmergency.Caserne.CaserneDto;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum GeometryType {
    POINT;

    @JsonValue
    public String toValue() {
        switch (this) {
        case POINT: return "Point";
        }
        return null;
    }

    @JsonCreator
    public static GeometryType forValue(String value) throws IOException {
        if (value.equals("Point")) return POINT;
        throw new IOException("Cannot deserialize GeometryType");
    }
}