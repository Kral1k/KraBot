package com.kral1k.krabot.commands;

import com.kral1k.krabot.command.CommandDispatcher;
import com.kral1k.krabot.command.GuildCommandInteraction;
import com.kral1k.krabot.permission.PermissionRole;

public class StopCommand {
    public static void register(CommandDispatcher<GuildCommandInteraction> dispatcher) {
        dispatcher.register("stop", "stop").permission(source -> source.hasPermission(PermissionRole.DJ)).executor(interaction -> {
            interaction.getGuild().getMusicPlayer().stop();
            interaction.replayTranslatable("player.stop").setEphemeral(true).queue();
        });
    }
}
