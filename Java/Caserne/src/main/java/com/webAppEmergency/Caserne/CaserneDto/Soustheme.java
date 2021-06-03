package com.webAppEmergency.Caserne.CaserneDto;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Soustheme {
    CASERNE_DE_POMPIERS;

    @JsonValue
    public String toValue() {
        switch (this) {
        case CASERNE_DE_POMPIERS: return "Caserne de pompiers";
        }
        return null;
    }

    @JsonCreator
    public static Soustheme forValue(String value) throws IOException {
        if (value.equals("Caserne de pompiers")) return CASERNE_DE_POMPIERS;
        throw new IOException("Cannot deserialize Soustheme");
    }
}