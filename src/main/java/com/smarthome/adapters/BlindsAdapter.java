package com.smarthome.adapters;

import com.smarthome.core.SmartDevice;
import com.smarthome.thirdparty.MotorizedBlinds;

public class BlindsAdapter implements SmartDevice {
    
    private final MotorizedBlinds blinds; 
    
    public BlindsAdapter(MotorizedBlinds blinds){
        this.blinds = blinds;

    }


    @Override 
    public void turnOn(){
        blinds.raiseBlinds(100.0);
        //would be col if turnOn method would take a variable and percentage could be set there 
    } 


    @Override 
    public void turnOff(){
        blinds.lowerBlinds(0.0);
    } 

    @Override 
    public String getName(){
        return  blinds.getName();
    } 
    @Override 
    public String getStatus(){
        if (blinds.getPositionsStatus() < 100) {
            return "ON (" + blinds.getPositionsStatus()+"%)";
        }
        else 
            return "OFF";
    }

    @Override 
    public String getLocation(){
        return  blinds.getPosition();
    }


}