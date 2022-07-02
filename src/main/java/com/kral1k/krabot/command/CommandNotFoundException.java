package com.kral1k.krabot.command;

import com.kral1k.krabot.language.Text;

public class CommandNotFoundException extends CommandException {

    public CommandNotFoundException() {
        super(Text.translatable("command.notFound"));
    }
}
