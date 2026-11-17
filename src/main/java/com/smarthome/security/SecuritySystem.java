package com.smarthome.security;

public class SecuritySystem {
    private SecurityState currentState;
    private String alertMessage = "No Alerts";

    public String getAlertMessage() { return alertMessage; }
    public void setAlertMessage(String msg) { this.alertMessage = msg; }
    public void clearAlert() { this.alertMessage = "No Alerts"; }

    public SecuritySystem() {
        // Default state is disarmed
        this.currentState = new DisarmedState();
    }

    public void setState(SecurityState state) {
        this.currentState = state;
    }

    // Delegate actions to the current state
    public void armHome() { currentState.armHome(this); }
    public void armAway() { currentState.armAway(this); }
    public void disarm() { currentState.disarm(this); }

    // Called by a MotionDetector sensor
    public void triggerMotion() { currentState.motionDetected(this); }
}