package com.kral1k.krabot.command;

import com.kral1k.krabot.button.Source;
import com.kral1k.krabot.utils.ExecutionException;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class SubCommandGroup<T extends CommandInteraction> {
    private final String name;
    private final String description;
    private final Map<String, SubCommand<T>> subCommandMap = new HashMap<>();
    private Predicate<Source> predicate = source -> true;

    public SubCommandGroup(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public SubCommandGroup<T> predicate(Predicate<Source> predicate) {
        this.predicate = predicate;
        return this;
    }

    public SubCommandGroup<T> addSubcommand(SubCommand<T> subCommand) {
        subCommandMap.put(subCommand.getName(), subCommand);
        return this;
    }

    protected void execute(T interaction) throws ExecutionException {
        if (!predicate.test(interaction.getSource())) throw new CommandPermissionException();
        subCommandMap.get(interaction.getSubcommand()).execute(interaction);
    }

    protected String getName() {
        return name;
    }

    protected SubcommandGroupData build() {
        SubcommandGroupData subcommandGroupData = new SubcommandGroupData(this.name, this.description);
        for (SubCommand<T> subCommand : subCommandMap.values())
            subcommandGroupData.addSubcommands(subCommand.build());
        return subcommandGroupData;
    }
}
