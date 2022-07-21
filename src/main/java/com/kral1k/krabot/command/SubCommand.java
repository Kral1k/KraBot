package com.kral1k.krabot.command;

import com.kral1k.krabot.button.Source;
import com.kral1k.krabot.utils.ExecutionException;
import com.kral1k.krabot.utils.Executor;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class SubCommand<T extends CommandInteraction> {
    private final String name;
    private final String description;
    private final List<OptionData> optionDataList = new ArrayList<>();
    private Predicate<Source> predicate = source -> true;
    private Executor<T> executor = interaction -> { throw new CommandNotFoundException(); };

    public SubCommand(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public SubCommand<T> predicate(Predicate<Source> predicate) {
        this.predicate = predicate;
        return this;
    }

    public SubCommand<T> executor(Executor<T> executor) {
        this.executor = executor;
        return this;
    }

    public SubCommand<T> addOption(OptionData optionData) {
        optionDataList.add(optionData);
        return this;
    }

    protected void execute(T interaction) throws ExecutionException {
        if (!predicate.test(interaction.getSource())) throw new CommandPermissionException();
        executor.execute(interaction);
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
