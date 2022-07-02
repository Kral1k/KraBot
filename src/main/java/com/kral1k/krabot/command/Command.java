package com.kral1k.krabot.command;

import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Command<T extends CommandInteraction> {
    private final String name;
    private final String description;
    private final List<OptionData> optionDataList = new ArrayList<>();
    private final Map<String, SubCommand<T>> subCommandMap = new HashMap<>();
    private final Map<String, SubCommandGroup<T>> subCommandGroupMap = new HashMap<>();
    private CommandPermission<CommandSource> permission = source -> true;
    private CommandExecutor<T> executor = interaction -> { throw new CommandNotFoundException(); };

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Command<T> permission(CommandPermission<CommandSource> permission) {
        this.permission = permission;
        return this;
    }

    public Command<T> executor(CommandExecutor<T> executor) {
        this.executor = executor;
        return this;
    }

    public Command<T> addSubCommand(SubCommand<T> subCommand) {
        subCommandMap.put(subCommand.getName(), subCommand);
        return this;
    }

    public Command<T> addSubCommandGroup(SubCommandGroup<T> subCommandGroup) {
        subCommandGroupMap.put(subCommandGroup.getName(), subCommandGroup);
        return this;
    }

    public Command<T> addOption(OptionData optionData) {
        optionDataList.add(optionData);
        return this;
    }

    protected void execute(T interaction) throws CommandException {
        if (!permission.accept(interaction.getSource())) throw new CommandPermissionException();
        if (interaction.getSubcommandGroup() != null) subCommandGroupMap.get(interaction.getSubcommandGroup()).execute(interaction);
        else if (interaction.getSubcommand() != null) subCommandMap.get(interaction.getSubcommand()).execute(interaction);
        else executor.run(interaction);
    }

    protected String getName() {
        return name;
    }

    protected SlashCommandData build() {
        SlashCommandData slashCommandData = Commands.slash(name, description);
        slashCommandData.addOptions(optionDataList);
        for (SubCommand<T> subCommand : subCommandMap.values())
            slashCommandData.addSubcommands(subCommand.build());
        for (SubCommandGroup<T> subCommandGroup : subCommandGroupMap.values())
            slashCommandData.addSubcommandGroups(subCommandGroup.build());
        return slashCommandData;
    }
}
