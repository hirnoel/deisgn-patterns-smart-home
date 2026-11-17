package com.smarthome.core;

public interface SmartDevice {
    void turnOn();
    void turnOff();
    String getStatus();
    String getName();
    String getLocation();

    default double getTemperatureModifier() {
        return 0.0;
    }
}