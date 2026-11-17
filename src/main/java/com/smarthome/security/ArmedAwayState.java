package com.smarthome.security;

public class ArmedAwayState implements SecurityState {
    @Override
    public void armHome(SecuritySystem system) {
        System.out.println("Switching from AWAY to HOME mode.");
        system.setState(new ArmedHomeState()); // (Assuming you created this class)
    }

    @Override
    public void armAway(SecuritySystem system) {
        System.out.println("Security System is already armed for AWAY.");
    }

    @Override
    public void disarm(SecuritySystem system) {
        System.out.println("Security System: DISARMED.");
        system.setState(new DisarmedState());
    }

    @Override
    public void motionDetected(SecuritySystem system) { // <-- Update signature
        System.out.println("ALARM TRIGGERED! Motion detected while AWAY. Calling authorities!");

        // Now you can successfully set the alert on the GUI!
        system.setAlertMessage("ALARM: Motion Detected While AWAY!");
    }
}