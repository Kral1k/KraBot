package com.kral1k.krabot.command;

import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.ArrayList;
import java.util.List;

public class SubCommand<T extends CommandInteraction> {
    private final String name;
    private final String description;
    private final List<OptionData> optionDataList = new ArrayList<>();
    private CommandPermission<CommandSource> permission = source -> true;
    private CommandExecutor<T> executor = interaction -> { throw new CommandNotFoundException(); };

    public SubCommand(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public SubCommand<T> permission(CommandPermission<CommandSource> permission) {
        this.permission = permission;
        return this;
    }

    public SubCommand<T> executor(CommandExecutor<T> executor) {
        this.executor = executor;
        return this;
    }

    public SubCommand<T> addOption(OptionData optionData) {
        optionDataList.add(optionData);
        return this;
    }

    protected void execute(T interaction) throws CommandException {
        if (!permission.accept(interaction.getSource())) throw new CommandPermissionException();
        executor.run(interaction);
    }

    protected String getName() {
        return name;
    }

    protected SubcommandData build() {
        SubcommandData subcommandData = new SubcommandData(this.name, this.description);
        subcommandData.addOptions(this.optionDataList);
        return subcommandData;
    }
}
