package com.smarthome.commands;
import com.smarthome.core.SmartDevice;

public class TurnOnCommand implements Command {
    private SmartDevice device;

    public TurnOnCommand(SmartDevice device) {
        this.device = device;
    }

    @Override
    public void execute() {
        device.turnOn();
    }

    @Override
    public void undo() {
        device.turnOff();
    }
}