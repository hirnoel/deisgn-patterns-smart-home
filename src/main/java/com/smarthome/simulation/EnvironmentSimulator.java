package com.smarthome.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class EnvironmentSimulator {
    private int timeInMinutes = 720;
    private List<RoomEnvironment> rooms = new ArrayList<>();
    private Timer timer;

    // Checks if room exists; if not, creates it
    public RoomEnvironment getOrCreateRoom(String roomName) {
        for (RoomEnvironment room : rooms) {
            if (room.getRoomName().equalsIgnoreCase(roomName)) {
                return room;
            }
        }
        RoomEnvironment newRoom = new RoomEnvironment(roomName, 22.0); // Default 22°C
        rooms.add(newRoom);
        return newRoom;
    }

    public List<RoomEnvironment> getRooms() { return rooms; }

    // Formats time for the GUI clock
    public String getFormattedTime() {
        return String.format("%02d:%02d", timeInMinutes / 60, timeInMinutes % 60);
    }

    public void startSimulation(Runnable onTickUpdate) {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                tick();
                // Notifies the GUI to refresh its visuals
                if (onTickUpdate != null) onTickUpdate.run();
            }
        }, 0, 1000);
    }

    private void tick() {
        timeInMinutes = (timeInMinutes + 10) % 1440;
        double outsideTemp = 20.0 - (5.0 * Math.cos(((timeInMinutes - 180) / 1440.0) * 2 * Math.PI));
        double outsideLight = 50.0 - (50.0 * Math.cos((timeInMinutes / 1440.0) * 2 * Math.PI));

        for (RoomEnvironment room : rooms) {
            room.updatePhysics(outsideTemp, outsideLight);
            room.simulateRandomMotion(0.05);
        }
    }
}