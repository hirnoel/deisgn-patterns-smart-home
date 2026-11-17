package com.smarthome.sensors;
import com.smarthome.rules.AutomationRule;
import java.util.ArrayList;
import java.util.List;

public class SmartHeatSensor {
    private String location;
    private double currentTemperature = 20.0;
    private List<AutomationRule> rules = new ArrayList<>();

    public SmartHeatSensor(String location) {
        this.location = location;
    }

    // Users can now add custom automations
    public void addRule(AutomationRule rule) {
        rules.add(rule);
    }

    public List<AutomationRule> getRules() { return rules; }
    public void removeRule(AutomationRule rule) { rules.remove(rule); }

    public void updateTemperature(double newTemp) {
        System.out.println("\n[" + location + " Heat Sensor] detects: " + newTemp + "°C");
        this.currentTemperature = newTemp;

        // Check all user-defined rules
        for (AutomationRule rule : rules) {
            rule.evaluate(currentTemperature);
        }
    }
}