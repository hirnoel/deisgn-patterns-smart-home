package com.smarthome.security;

public interface SecurityState {
    void armHome(SecuritySystem system);
    void armAway(SecuritySystem system);
    void disarm(SecuritySystem system);
    void motionDetected(SecuritySystem system);
}