package com.smarthome.gui;

import com.smarthome.core.SmartDevice;
import com.smarthome.commands.Command;
import com.smarthome.commands.TurnOnCommand;
import com.smarthome.commands.TurnOffCommand;
import com.smarthome.factories.DeviceFactory;
import com.smarthome.hub.SmartHomeHub;
import com.smarthome.rules.AutomationRule;
import com.smarthome.security.SecuritySystem;
import com.smarthome.sensors.SmartHeatSensor;
import com.smarthome.sensors.SmartLightSensor;
import com.smarthome.sensors.SmartMotionSensor;
import com.smarthome.simulation.EnvironmentSimulator;
import com.smarthome.simulation.RoomEnvironment;
import com.smarthome.rules.TimeRule;

import javax.swing.*;
import java.awt.*;
import java.util.function.Predicate;

public class SmartHomeDashboard extends JFrame {
    private EnvironmentSimulator simulator;
    private SmartHomeHub hub;
    private SecuritySystem security;
    private JLabel timeLabel;
    private JLabel alertLabel;
    private JPanel roomsPanel;
    private JComboBox<String> securityModeBox;

    public SmartHomeDashboard(EnvironmentSimulator simulator, SmartHomeHub hub, SecuritySystem security) {
        this.simulator = simulator;
        this.hub = hub;
        this.security = security;

        setTitle("Smart Home & IoT Hub");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();

        timeLabel = new JLabel("Time: --:-- | ");
        timeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(timeLabel);

        topPanel.add(new JLabel("Security: "));
        String[] securityModes = {"Disarmed", "Armed Home", "Armed Away"};
        securityModeBox = new JComboBox<>(securityModes);
        securityModeBox.addActionListener(e -> changeSecurityState());
        topPanel.add(securityModeBox);

        JButton addDeviceBtn = new JButton("Add Equipment");
        addDeviceBtn.addActionListener(e -> openAddDeviceDialog());
        topPanel.add(addDeviceBtn);

        JButton addRuleBtn = new JButton("Add Rule");
        addRuleBtn.addActionListener(e -> openAddRuleDialog());
        topPanel.add(addRuleBtn);

        JButton addScheduleBtn = new JButton("Add Schedule");
        addScheduleBtn.addActionListener(e -> openAddScheduleDialog());
        topPanel.add(addScheduleBtn);

        JButton manageRulesBtn = new JButton("Manage Rules");
        manageRulesBtn.addActionListener(e -> openManageRulesDialog());
        topPanel.add(manageRulesBtn);

        add(topPanel, BorderLayout.NORTH);

        // Alert Banner
        JPanel alertPanel = new JPanel();
        alertLabel = new JLabel(security.getAlertMessage());
        alertLabel.setForeground(Color.RED);
        alertLabel.setFont(new Font("Arial", Font.BOLD, 16));
        alertPanel.add(alertLabel);

        JButton dismissBtn = new JButton("Dismiss Alert");
        dismissBtn.addActionListener(e -> {
            security.clearAlert();
            refreshUI();
        });
        alertPanel.add(dismissBtn);
        add(alertPanel, BorderLayout.SOUTH);

        roomsPanel = new JPanel();
        roomsPanel.setLayout(new GridLayout(0, 2, 10, 10));
        add(new JScrollPane(roomsPanel), BorderLayout.CENTER);
    }

    private void changeSecurityState() {
        String mode = (String) securityModeBox.getSelectedItem();
        if (mode.equals("Disarmed")) security.disarm();
        else if (mode.equals("Armed Home")) security.armHome();
        else if (mode.equals("Armed Away")) security.armAway();
        security.clearAlert();
    }

    private void openAddDeviceDialog() {
        JTextField roomNameField = new JTextField();
        JTextField deviceNameField = new JTextField();
        String[] types = {"Light", "AC", "Blinds", "Heat Sensor", "Light Sensor", "Motion Sensor"};
        JComboBox<String> typeBox = new JComboBox<>(types);

        Object[] message = { "Room Name:", roomNameField, "Custom Name (Devices):", deviceNameField, "Type:", typeBox };

        if (JOptionPane.showConfirmDialog(this, message, "Add Equipment", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            String roomName = roomNameField.getText().trim();
            String deviceName = deviceNameField.getText().trim();
            String type = typeBox.getSelectedItem().toString();

            if (!roomName.isEmpty()) {
                RoomEnvironment room = simulator.getOrCreateRoom(roomName);

                if (type.equals("Heat Sensor") && !room.hasHeatSensor()) {
                    room.setHeatSensor(new SmartHeatSensor(roomName));
                } else if (type.equals("Light Sensor") && !room.hasLightSensor()) {
                    room.setLightSensor(new SmartLightSensor(roomName));
                } else if (type.equals("Motion Sensor") && !room.hasMotionSensor()) {
                    room.setMotionSensor(new SmartMotionSensor(roomName, security));
                } else if (!type.contains("Sensor")) {
                    if (deviceName.isEmpty()) deviceName = type;
                    room.addDevice(DeviceFactory.createDevice(type.toLowerCase(), deviceName, roomName));
                } else {
                    JOptionPane.showMessageDialog(this, "Room already has this sensor type.");
                }
                refreshUI();
            }
        }
    }

    private void openAddRuleDialog() {
        if (simulator.getRooms().isEmpty()) return;

        JComboBox<String> roomBox = new JComboBox<>();
        for (RoomEnvironment room : simulator.getRooms()) roomBox.addItem(room.getRoomName());

        JComboBox<String> sensorBox = new JComboBox<>(new String[]{"Temperature", "Light Level"});
        JComboBox<String> operatorBox = new JComboBox<>(new String[]{">=", "<"});
        JTextField thresholdField = new JTextField("25.0");
        JTextField targetDeviceNameField = new JTextField();
        JComboBox<String> actionBox = new JComboBox<>(new String[]{"Turn ON", "Turn OFF"});

        Object[] message = { "Room:", roomBox, "Sensor:", sensorBox, "Condition:", operatorBox,
                "Value:", thresholdField, "Target Device Name:", targetDeviceNameField, "Action:", actionBox };

        if (JOptionPane.showConfirmDialog(this, message, "Add Rule", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            RoomEnvironment room = simulator.getOrCreateRoom((String) roomBox.getSelectedItem());
            String targetName = targetDeviceNameField.getText().trim();

            SmartDevice targetDevice = room.getDevices().stream()
                    .filter(d -> d.getName().equalsIgnoreCase(targetName)).findFirst().orElse(null);

            if (targetDevice == null) {
                JOptionPane.showMessageDialog(this, "Device '" + targetName + "' not found.");
                return;
            }

            try {
                double threshold = Double.parseDouble(thresholdField.getText());
                boolean isGreater = operatorBox.getSelectedItem().equals(">=");
                Predicate<Double> condition = val -> isGreater ? val >= threshold : val < threshold;

                Command cmd = actionBox.getSelectedItem().equals("Turn ON") ?
                        new TurnOnCommand(targetDevice) : new TurnOffCommand(targetDevice);

                AutomationRule rule = new AutomationRule("GUI Rule: " + targetName, condition, cmd, hub);

                if (sensorBox.getSelectedItem().equals("Temperature") && room.hasHeatSensor()) {
                    room.getHeatSensor().addRule(rule);
                } else if (sensorBox.getSelectedItem().equals("Light Level") && room.hasLightSensor()) {
                    room.getLightSensor().addRule(rule);
                } else {
                    JOptionPane.showMessageDialog(this, "Room missing required sensor.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid threshold.");
            }
        }
    }

    private JPanel createTimeRuleItem(TimeRule rule, JDialog dialog) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.add(new JLabel(rule.getRuleName()));
        JButton delBtn = new JButton("Delete");
        delBtn.addActionListener(e -> {
            simulator.getScheduler().removeRule(rule);
            dialog.dispose();
            openManageRulesDialog();
        });
        p.add(delBtn);
        return p;
    }

    private void openManageRulesDialog() {
        JDialog dialog = new JDialog(this, "Manage Rules", true);
        dialog.setSize(400, 400);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        for (RoomEnvironment room : simulator.getRooms()) {
            if (room.hasHeatSensor()) {
                for (AutomationRule rule : room.getHeatSensor().getRules()) {
                    panel.add(createRuleItem(rule, room.getHeatSensor(), dialog));
                }
            }
            if (room.hasLightSensor()) {
                for (AutomationRule rule : room.getLightSensor().getRules()) {
                    panel.add(createRuleItem(rule, room.getLightSensor(), dialog));
                }
            }
        }

        for (TimeRule rule : simulator.getScheduler().getRules()) {
            panel.add(createTimeRuleItem(rule, dialog));
        }

        dialog.add(new JScrollPane(panel));
        dialog.setVisible(true);
    }

    private void openAddScheduleDialog() {
    if (simulator.getRooms().isEmpty()) return;

    JComboBox<String> roomBox = new JComboBox<>();
    for (RoomEnvironment room : simulator.getRooms()) roomBox.addItem(room.getRoomName());

    JTextField timeField = new JTextField("08:00");
    JTextField targetDeviceNameField = new JTextField();
    JComboBox<String> actionBox = new JComboBox<>(new String[]{"Turn ON", "Turn OFF"});

    Object[] message = {
        "Room:", roomBox,
        "Time (HH:MM, 10-min steps):", timeField,
        "Target Device Name:", targetDeviceNameField,
        "Action:", actionBox
    };

    if (JOptionPane.showConfirmDialog(this, message, "Add Schedule", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
        RoomEnvironment room = simulator.getOrCreateRoom((String) roomBox.getSelectedItem());
        String targetName = targetDeviceNameField.getText().trim();
        String targetTime = timeField.getText().trim();

        SmartDevice targetDevice = room.getDevices().stream()
                .filter(d -> d.getName().equalsIgnoreCase(targetName)).findFirst().orElse(null);

        if (targetDevice == null) {
            JOptionPane.showMessageDialog(this, "Device '" + targetName + "' not found.");
            return;
        }

        Command cmd = actionBox.getSelectedItem().equals("Turn ON") ?
                new TurnOnCommand(targetDevice) : new TurnOffCommand(targetDevice);

        String ruleName = "Schedule [" + targetTime + "]: " + room.getRoomName() + " " + targetName;
        TimeRule rule = new TimeRule(ruleName, targetTime, cmd, hub);
        simulator.getScheduler().addRule(rule);
    }
}

    private JPanel createRuleItem(AutomationRule rule, Object sensor, JDialog dialog) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.add(new JLabel(rule.getRuleName()));
        JButton delBtn = new JButton("Delete");
        delBtn.addActionListener(e -> {
            if (sensor instanceof SmartHeatSensor) ((SmartHeatSensor)sensor).removeRule(rule);
            if (sensor instanceof SmartLightSensor) ((SmartLightSensor)sensor).removeRule(rule);
            dialog.dispose();
            openManageRulesDialog();
        });
        p.add(delBtn);
        return p;
    }

    public void refreshUI() {
        SwingUtilities.invokeLater(() -> {
            timeLabel.setText("Time: " + simulator.getFormattedTime() + " | ");
            alertLabel.setText(security.getAlertMessage());
            roomsPanel.removeAll();

            for (RoomEnvironment room : simulator.getRooms()) {
                JPanel roomBox = new JPanel();
                roomBox.setBorder(BorderFactory.createTitledBorder(room.getRoomName()));
                roomBox.setLayout(new BoxLayout(roomBox, BoxLayout.Y_AXIS));

                if (room.hasHeatSensor()) {
                    roomBox.add(new JLabel(String.format("Temperature: %.1f°C", room.getCurrentTemp())));
                }

                String sensorsInstalled = "Sensors: ";
                if (room.hasHeatSensor()) sensorsInstalled += "[Heat] ";
                if (room.hasLightSensor()) sensorsInstalled += "[Light] ";
                if (room.hasMotionSensor()) sensorsInstalled += "[Motion]";
                if (!sensorsInstalled.equals("Sensors: ")) {
                    JLabel sensorLabel = new JLabel(sensorsInstalled);
                    sensorLabel.setForeground(Color.GRAY);
                    roomBox.add(sensorLabel);
                }

                for (SmartDevice device : room.getDevices()) {
                    JPanel devicePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    devicePanel.add(new JLabel("- " + device.getName() + ": " + device.getStatus()));

                    JButton toggleBtn = new JButton("Toggle");
                    toggleBtn.addActionListener(e -> {
                        if (device.getStatus().equals("ON") || device.getStatus().contains("ON")) {
                            hub.executeCommand(new TurnOffCommand(device));
                        } else {
                            hub.executeCommand(new TurnOnCommand(device));
                        }
                        refreshUI();
                    });
                    devicePanel.add(toggleBtn);
                    roomBox.add(devicePanel);
                }
                roomsPanel.add(roomBox);
            }
            roomsPanel.revalidate();
            roomsPanel.repaint();
        });
    }
}