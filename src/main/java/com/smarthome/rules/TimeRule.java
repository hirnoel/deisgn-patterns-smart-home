package com.smarthome.rules;

import com.smarthome.commands.Command;
import com.smarthome.hub.SmartHomeHub;

public class TimeRule {
    private String ruleName;
    private String targetTime;
    private Command commandToExecute;
    private SmartHomeHub hub;

    public String getRuleName(){return ruleName;}
    
    public TimeRule(String ruleName, String targetTime, Command command, SmartHomeHub hub){
        this.ruleName = ruleName;
        this.targetTime = targetTime;
        this.commandToExecute = command;
        this.hub  = hub;
    }    

    public void evaluate(String currentTime){
        
        if (currentTime.equals(targetTime)) {
            System.out.println("--> Automation Triggered: " + ruleName);
            hub.executeCommand(commandToExecute);
        }
    }
    
}
