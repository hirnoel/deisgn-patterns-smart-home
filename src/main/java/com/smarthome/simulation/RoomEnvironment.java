package com.smarthome.simulation;

import com.smarthome.core.SmartDevice;
import com.smarthome.sensors.SmartHeatSensor;
import com.smarthome.sensors.SmartLightSensor;
import com.smarthome.sensors.SmartMotionSensor;
import java.util.ArrayList;
import java.util.List;

public class RoomEnvironment {
    private String roomName;
    private double currentTemp;
    private SmartHeatSensor heatSensor;
    private SmartLightSensor lightSensor;
    private SmartMotionSensor motionSensor;
    private List<SmartDevice> devices;

    // Simplified constructor for dynamic creation
    public RoomEnvironment(String roomName, double startingTemp) {
        this.roomName = roomName;
        this.currentTemp = startingTemp;
        this.devices = new ArrayList<>();
    }

    public String getRoomName() { return roomName; }
    public List<SmartDevice> getDevices() { return devices; }

    // Dynamic Adders
    public void addDevice(SmartDevice device) { this.devices.add(device); }
    public void setHeatSensor(SmartHeatSensor heatSensor) { this.heatSensor = heatSensor; }
    public void setLightSensor(SmartLightSensor lightSensor) { this.lightSensor = lightSensor; }
    public void setMotionSensor(SmartMotionSensor motionSensor) { this.motionSensor = motionSensor; }

    public SmartHeatSensor getHeatSensor() { return heatSensor; }
    public SmartLightSensor getLightSensor() { return lightSensor; }
    public SmartMotionSensor getMotionSensor() { return motionSensor; }
    public boolean hasLightSensor() { return this.lightSensor != null; }
    // Add to RoomEnvironment class
    public boolean hasMotionSensor() { return this.motionSensor != null; }

    // GUI Checks
    public boolean hasHeatSensor() { return this.heatSensor != null; }
    public double getCurrentTemp() { return currentTemp; }

    public void updatePhysics(double outsideTemp, double outsideLight) {
        currentTemp += (outsideTemp - currentTemp) * 0.1;
        for (SmartDevice device : devices) {
            currentTemp += device.getTemperatureModifier();
        }

        if (lightSensor != null) lightSensor.updateLightLevel(Math.round(outsideLight * 10.0) / 10.0);
        if (heatSensor != null) heatSensor.updateTemperature(Math.round(currentTemp * 10.0) / 10.0);
    }

    public void simulateRandomMotion(double probabilityThreshold) {
        if (motionSensor != null && Math.random() < probabilityThreshold) {
            motionSensor.detectMotion();
        }
    }
}