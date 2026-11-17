package com.smarthome.hub;
import com.smarthome.commands.Command;
import java.util.Stack;

public class SmartHomeHub {
    private Stack<Command> commandHistory = new Stack<>();

    // Executes a command and logs it in the history stack
    public void executeCommand(Command command) {
        command.execute();
        commandHistory.push(command);
    }

    // Reversible operation (undo)
    public void undoLastCommand() {
        if (!commandHistory.isEmpty()) {
            Command lastCommand = commandHistory.pop();
            lastCommand.undo();
            System.out.println("Undid last command.");
        } else {
            System.out.println("No commands to undo.");
        }
    }
}