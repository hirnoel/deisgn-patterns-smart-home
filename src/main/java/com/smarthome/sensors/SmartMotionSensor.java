package com.smarthome.sensors;

import com.smarthome.security.SecuritySystem;

public class SmartMotionSensor {
    private String location;
    private SecuritySystem securitySystem;

    public SmartMotionSensor(String location, SecuritySystem securitySystem) {
        this.location = location;
        this.securitySystem = securitySystem;
    }

    // Called by your Simulation Engine to simulate random events (like someone walking by)
    public void detectMotion() {
        System.out.println("\n[" + location + " Motion Sensor] detected movement!");
        // Delegates the event to the State Pattern context
        securitySystem.triggerMotion();
    }
}