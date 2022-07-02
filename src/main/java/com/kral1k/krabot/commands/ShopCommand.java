package com.kral1k.krabot.commands;

import com.kral1k.krabot.command.CommandDispatcher;
import com.kral1k.krabot.command.GuildCommandInteraction;

public class ShopCommand {
    public static void register(CommandDispatcher<GuildCommandInteraction> dispatcher) {
        dispatcher.register("shop", "shop").executor(interaction -> {
           interaction.reply("soon...").setEphemeral(true).queue();
        });
    }
}
