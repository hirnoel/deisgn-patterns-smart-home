package com.smarthome.factories;
import com.smarthome.core.SmartDevice;
import com.smarthome.devices.SmartAC;
import com.smarthome.devices.SmartLight;

public class DeviceFactory {

    // Factory Method to create devices based on type[cite: 23]
    public static SmartDevice createDevice(String type, String name, String location) {
        switch (type.toLowerCase()) {
            case "light":
                return new SmartLight(name, location);
            case "ac":
                return new SmartAC(name, location);
            default:
                throw new IllegalArgumentException("Unknown device type: " + type);
        }
    }
}