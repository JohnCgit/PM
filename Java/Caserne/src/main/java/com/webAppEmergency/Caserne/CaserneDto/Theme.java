package com.webAppEmergency.Caserne.CaserneDto;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Theme {
    URGENCE;

    @JsonValue
    public String toValue() {
        switch (this) {
        case URGENCE: return "URGENCE";
        }
        return null;
    }

    @JsonCreator
    public static Theme forValue(String value) throws IOException {
        if (value.equals("URGENCE")) return URGENCE;
        throw new IOException("Cannot deserialize Theme");
    }
}