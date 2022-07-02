package com.kral1k.krabot.commands;

import com.kral1k.krabot.command.CommandDispatcher;
import com.kral1k.krabot.command.GuildCommandInteraction;
import com.kral1k.krabot.language.Text;
import com.kral1k.krabot.permission.PermissionRole;
import com.kral1k.krabot.player.music.MusicPlayer;

public class PauseCommand {
    public static void register(CommandDispatcher<GuildCommandInteraction> dispatcher) {
        dispatcher.register("pause", "pause").permission(source -> source.hasPermission(PermissionRole.DJ)).executor(interaction -> {
            MusicPlayer player = interaction.getGuild().getMusicPlayer();
            boolean pause = !player.audioPlayer.isPaused();
            player.audioPlayer.setPaused(pause);
            interaction.replayTranslatable("player.pause", Text.translatable(pause ? "player.pause.off" : "player.pause.on")).setEphemeral(true).queue();
        });
    }
}
