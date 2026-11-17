package com.smarthome.security;

public class ArmedHomeState implements SecurityState {
    @Override
    public void armHome(SecuritySystem system) {
        System.out.println("Security System is already armed for Home.");
    }

    @Override
    public void armAway(SecuritySystem system) {
        System.out.println("Switching from HOME to AWAY mode.");
        system.setState(new ArmedAwayState());
    }

    @Override
    public void disarm(SecuritySystem system) {
        System.out.println("Security System: DISARMED.");
        system.setState(new DisarmedState());
    }

    @Override
    public void motionDetected(SecuritySystem system) { // <-- Update signature
        System.out.println("Motion detected, but system is DISARMED. No action taken.");
        system.setAlertMessage("Motion Detected.");
    }
}