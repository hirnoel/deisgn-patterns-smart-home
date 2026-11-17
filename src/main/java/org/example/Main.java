package org.example;

import com.smarthome.gui.SmartHomeDashboard;
import com.smarthome.hub.SmartHomeHub;
import com.smarthome.security.SecuritySystem;
import com.smarthome.simulation.EnvironmentSimulator;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SmartHomeHub hub = new SmartHomeHub();
        SecuritySystem security = new SecuritySystem();
        EnvironmentSimulator simulator = new EnvironmentSimulator();

        SwingUtilities.invokeLater(() -> {
            // Pass the simulator, hub, and security system to the GUI
            SmartHomeDashboard dashboard = new SmartHomeDashboard(simulator, hub, security);
            dashboard.setVisible(true);

            simulator.startSimulation(() -> dashboard.refreshUI());
        });
    }
}