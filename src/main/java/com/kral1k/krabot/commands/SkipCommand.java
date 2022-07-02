package com.kral1k.krabot.commands;

import com.kral1k.krabot.command.CommandDispatcher;
import com.kral1k.krabot.command.GuildCommandInteraction;
import com.kral1k.krabot.permission.PermissionRole;

public class SkipCommand {
    public static void register(CommandDispatcher<GuildCommandInteraction> dispatcher) {
        dispatcher.register("skip", "skip").permission(source -> source.hasPermission(PermissionRole.DJ)).executor(interaction -> {
            interaction.getGuild().getMusicPlayer().skip();
            interaction.replayTranslatable("player.skip").setEphemeral(true).queue();
        });
    }
}
