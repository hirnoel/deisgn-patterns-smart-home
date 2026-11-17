package com.smarthome.core;

public interface SensorSubject {
    void subscribe(SmartDevice device);
    void unsubscribe(SmartDevice device);
    void notifyDevices();
}