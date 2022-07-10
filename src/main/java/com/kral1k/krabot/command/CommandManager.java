package com.kral1k.krabot.command;

import com.kral1k.krabot.commands.*;
import com.kral1k.krabot.game.commands.GameCommand;
import com.kral1k.krabot.game.commands.MiniGameCommand;
import com.kral1k.krabot.language.Text;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class CommandManager  {
    private final CommandDispatcher<GuildCommandInteraction> dispatcher = new CommandDispatcher<>();

    public CommandManager() {
        BalanceCommand.register(dispatcher);
        KraCoinCommand.register(dispatcher);
        ClearCommand.register(dispatcher);
        GTtsCommand.register(dispatcher);
        PlayCommand.register(dispatcher);
        PlayerCommand.register(dispatcher);
        SettingsCommand.register(dispatcher);
        SplayCommand.register(dispatcher);
        TestCommand.register(dispatcher);
        WikiCommand.register(dispatcher);

        SkipCommand.register(dispatcher);
        StopCommand.register(dispatcher);
        PauseCommand.register(dispatcher);
        PlayPlayList.register(dispatcher);
        SplayPlayList.register(dispatcher);

        GameCommand.register(dispatcher);
        MiniGameCommand.register(dispatcher);
    }

    public static SubCommand<GuildCommandInteraction> subCommand(String name, String description) {
        return new SubCommand<>(name, description);
    }

    public static SubCommandGroup<GuildCommandInteraction> subCommandGroup(String name, String description) {
        return new SubCommandGroup<>(name, description);
    }

    public CommandDispatcher<GuildCommandInteraction> getDispatcher() {
        return dispatcher;
    }

    public void execute(String name, GuildCommandInteraction interaction) {
        try {
            dispatcher.execute(name, interaction);
        } catch (CommandException exception) {
            this.sendThrowMessage(interaction, exception.getMessage());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            this.sendThrowMessage(interaction, Text.translatable("command.error"));
        }
    }

    private void sendThrowMessage(GuildCommandInteraction interaction, String message) {
        if (interaction.isAcknowledged()) interaction.getHook().sendMessage(message).delay(5,TimeUnit.SECONDS).flatMap(Message::delete).queue(null, error -> {});
        else interaction.reply(message).setEphemeral(true).queue();
    }

    public List<SlashCommandData> getJdaCommands() {
        return dispatcher.getMap().values().stream().map(Command::build).toList();
    }
}
