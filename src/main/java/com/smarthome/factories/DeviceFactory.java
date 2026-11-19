package com.smarthome.factories;
import com.smarthome.adapters.BlindsAdapter;
import com.smarthome.core.SmartDevice;
import com.smarthome.devices.SmartAC;
import com.smarthome.devices.SmartLight;
import com.smarthome.thirdparty.MotorizedBlinds;

public class DeviceFactory {

    // Factory Method to create devices based on type[cite: 23]
    public static SmartDevice createDevice(String type, String name, String location) {
        switch (type.toLowerCase()) {
            case "light":
                return new SmartLight(name, location);
            case "ac":
                return new SmartAC(name, location);
            case "blinds":
                MotorizedBlinds blinds = new MotorizedBlinds(name, location);
                return new BlindsAdapter(blinds);
            default:
                throw new IllegalArgumentException("Unknown device type: " + type);
        }
    }
}