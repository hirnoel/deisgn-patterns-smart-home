package com.smarthome.rules;

import com.smarthome.commands.Command;
import com.smarthome.hub.SmartHomeHub;
import java.util.function.Predicate;

public class AutomationRule {
    private String ruleName;
    private Predicate<Double> condition; // Evaluates a math condition (e.g., temp >= 25)
    private Command commandToExecute;
    private SmartHomeHub hub;

    public AutomationRule(String ruleName, Predicate<Double> condition, Command commandToExecute, SmartHomeHub hub) {
        this.ruleName = ruleName;
        this.condition = condition;
        this.commandToExecute = commandToExecute;
        this.hub = hub;
    }

    // Evaluates the sensor's current value against the user's condition
    public void evaluate(double sensorValue) {
        if (condition.test(sensorValue)) {
            System.out.println("--> Automation Triggered: " + ruleName);
            hub.executeCommand(commandToExecute);
        }
    }

    public String getRuleName() { return ruleName; }

}