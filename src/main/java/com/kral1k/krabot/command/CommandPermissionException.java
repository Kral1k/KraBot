package com.kral1k.krabot.command;

import com.kral1k.krabot.language.Text;

public class CommandPermissionException extends CommandException{
    public CommandPermissionException() {
        super(Text.translatable("command.permission"));
    }
}
