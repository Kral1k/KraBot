package com.kral1k.krabot.command;

import java.util.HashMap;
import java.util.Map;

public class CommandDispatcher<T extends CommandInteraction> {
    private final Map<String, Command<T>> commandMap = new HashMap<>();

    public Command<T> register(String name, String description) {
        Command<T> command = new Command<>(name, description);
        this.commandMap.put(command.getName(), command);
        return command;
    }

    protected void execute(String name, T interaction) throws CommandException {
        Command<T> command = commandMap.get(name);
        if (command == null) throw new CommandNotFoundException();
        command.execute(interaction);
    }

    protected Map<String, Command<T>> getMap() {
        return commandMap;
    }

    protected Command<T> getCommand(String name) {
        return commandMap.get(name);
    }
}
