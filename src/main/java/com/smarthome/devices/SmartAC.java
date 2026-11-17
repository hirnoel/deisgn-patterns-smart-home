package com.smarthome.devices;
import com.smarthome.core.SmartDevice;

public class SmartAC implements SmartDevice {
    private String name;
    private String location;
    private boolean isOn = false;
    private int activeTicks = 0; // Tracks how long it has been running

    public SmartAC(String name, String location) {
        this.name = name;
        this.location = location;
    }

    @Override
    public String getName() { return name; }

    @Override
    public String getLocation() { return location; }

    @Override
    public void turnOn() {
        this.isOn = true;
        System.out.println(location + " " + name + " is now ON.");
    }

    @Override
    public void turnOff() {
        this.isOn = false;
        this.activeTicks = 0; // Reset the startup counter
        System.out.println(location + " " + name + " is now OFF.");
    }

    @Override
    public String getStatus() { return isOn ? "ON" : "OFF"; }

    @Override
    public double getTemperatureModifier() {
        if (!isOn) return 0.0; // No effect if off

        activeTicks++;
        if (activeTicks <= 3) {
            System.out.println("   [" + name + " is starting up... weak cooling]");
            return -0.1; // Weak cooling for the first 3 ticks
        } else {
            return -0.4; // Full power cooling tick-by-tick
        }
    }
}