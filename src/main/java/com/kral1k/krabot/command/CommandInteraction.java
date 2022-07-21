package com.kral1k.krabot.command;

import com.kral1k.krabot.Bot;
import com.kral1k.krabot.button.Source;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public abstract class CommandInteraction {
    protected final Bot bot;
    protected final SlashCommandInteractionEvent event;

    public CommandInteraction(Bot bot, SlashCommandInteractionEvent event) {
        this.bot = bot;
        this.event = event;
    }

    public Bot getBot() {
        return bot;
    }

    public abstract Source getSource();

    public String getSubcommand() {
        return event.getSubcommandName();
    }

    public String getSubcommandGroup() {
        return event.getSubcommandGroup();
    }
}
