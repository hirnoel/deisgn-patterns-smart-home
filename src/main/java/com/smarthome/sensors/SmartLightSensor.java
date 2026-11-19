package com.smarthome.sensors;

import com.smarthome.rules.AutomationRule;
import java.util.ArrayList;
import java.util.List;

public class SmartLightSensor {
    private String location;
    private double currentLightLevel = 100.0; // 100% means fully bright
    private List<AutomationRule> rules = new ArrayList<>();

    public SmartLightSensor(String location) {
        this.location = location;
    }

    // Allows users to add custom light-based automations
    public void addRule(AutomationRule rule) {
        rules.add(rule);
    }

    public List<AutomationRule> getRules() { return rules; }
    
    public void removeRule(AutomationRule rule) { rules.remove(rule); }

    // Called by your Simulation Engine to change the ambient light
    public void updateLightLevel(double newLightLevel) {
        System.out.println("\n[" + location + " Light Sensor] detects: " + newLightLevel + "% ambient light");
        this.currentLightLevel = newLightLevel;

        // Check all user-defined rules
        for (AutomationRule rule : rules) {
            rule.evaluate(currentLightLevel);
        }
    }
}