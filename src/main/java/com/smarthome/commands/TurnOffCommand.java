package com.smarthome.commands;
import com.smarthome.core.SmartDevice;

public class TurnOffCommand implements Command {
    private SmartDevice device;

    public TurnOffCommand(SmartDevice device) {
        this.device = device;
    }

    @Override
    public void execute() {
        device.turnOff();
    }

    @Override
    public void undo() {
        device.turnOn();
    }
}