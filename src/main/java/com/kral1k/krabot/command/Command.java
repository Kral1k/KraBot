package com.kral1k.krabot.command;

import com.kral1k.krabot.button.Source;
import com.kral1k.krabot.utils.ExecutionException;
import com.kral1k.krabot.utils.Executor;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class Command<T extends CommandInteraction> {
    private final String name;
    private final String description;
    private final List<OptionData> optionDataList = new ArrayList<>();
    private final Map<String, SubCommand<T>> subCommandMap = new HashMap<>();
    private final Map<String, SubCommandGroup<T>> subCommandGroupMap = new HashMap<>();
    private Predicate<Source> predicate = source -> true;
    private Executor<T> executor = interaction -> { throw new CommandNotFoundException(); };

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Command<T> predicate(Predicate<Source> predicate) {
        this.predicate = predicate;
        return this;
    }

    public Command<T> executor(Executor<T> executor) {
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

    protected void execute(T interaction) throws ExecutionException {
        if (!predicate.test(interaction.getSource())) throw new CommandPermissionException();
        if (interaction.getSubcommandGroup() != null) subCommandGroupMap.get(interaction.getSubcommandGroup()).execute(interaction);
        else if (interaction.getSubcommand() != null) subCommandMap.get(interaction.getSubcommand()).execute(interaction);
        else executor.execute(interaction);
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
