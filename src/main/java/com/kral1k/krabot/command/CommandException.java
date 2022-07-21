package com.kral1k.krabot.command;

import com.kral1k.krabot.utils.ExecutionException;

public class CommandException extends ExecutionException {

    public CommandException(String message) {
        super(message);
    }

    public CommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandException(Throwable cause) {
        super(cause);
    }
}
