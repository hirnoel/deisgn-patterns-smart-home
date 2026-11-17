package com.smarthome.security;

public class DisarmedState implements SecurityState {
    @Override
    public void armHome(SecuritySystem system) {
        System.out.println("Security System: Armed for HOME.");
        system.setState(new ArmedHomeState());
    }

    @Override
    public void armAway(SecuritySystem system) {
        System.out.println("Security System: Armed for AWAY.");
        system.setState(new ArmedAwayState());
    }

    @Override
    public void disarm(SecuritySystem system) {
        System.out.println("Security System is already disarmed.");
    }

    @Override
    public void motionDetected(SecuritySystem system) { // <-- Update signature
        System.out.println("Motion detected, but system is DISARMED. No action taken.");
        // Optional: system.setAlertMessage("Motion Detected (Ignored)");
    }
}