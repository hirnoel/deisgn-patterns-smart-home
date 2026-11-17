package com.smarthome.devices;
import com.smarthome.core.SmartDevice;

public class SmartLight implements SmartDevice {
    private String name;
    private String location;
    private boolean isOn = false;
    private int brightness = 100;

    public SmartLight(String name, String location) {
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
        System.out.println(location + " " + name + " is now OFF.");
    }

    @Override
    public String getStatus() {
        return isOn ? "ON" : "OFF";
    }
}