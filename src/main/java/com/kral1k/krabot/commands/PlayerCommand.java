package com.kral1k.krabot.commands;

import com.kral1k.krabot.buttons.*;
import com.kral1k.krabot.command.CommandDispatcher;
import com.kral1k.krabot.command.CommandManager;
import com.kral1k.krabot.command.GuildCommandInteraction;
import com.kral1k.krabot.language.Text;
import com.kral1k.krabot.permission.PermissionRole;
import com.kral1k.krabot.permission.Perms;
import com.kral1k.krabot.player.music.MusicPlayer;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.util.concurrent.TimeUnit;

public class PlayerCommand {
    public static void register(CommandDispatcher<GuildCommandInteraction> dispatcher) {
        dispatcher.register("player", "Проигрыватель").predicate(source -> {
            return source.hasPermission(PermissionRole.DJ);
        }).addSubCommand(CommandManager.subCommand("volume", "Громкость воспроизедения.").addOption(new OptionData(OptionType.INTEGER, "amount", "Громкость", true)).executor(interaction -> {
            executeVolume(interaction);
        })).addSubCommand(CommandManager.subCommand("control", "Панель управления плеером.").executor(interaction -> {
            executeControl(interaction);
        })).addSubCommand(CommandManager.subCommand("list", "Плейлист").executor(interaction -> {
            MusicPlayer musicPlayer = interaction.getGuild().getMusicPlayer();
            interaction.replyEmbeds(musicPlayer.listEmbed()).setEphemeral(true).queue();
        }));
    }

    private static void executeVolume(GuildCommandInteraction interaction) {
        OptionMapping amountOption = interaction.getOption("amount");
        MusicPlayer player = interaction.getGuild().getMusicPlayer();

        if (amountOption == null) {
            int volume = player.audioPlayer.getVolume();
            interaction.reply(Text.translatable("player.volume", volume)).setEphemeral(true).queue();
        }else {
            boolean isDev = interaction.getMember().hasPermission(PermissionRole.DEVELOPER);
            int newVolume = Math.max(1, Math.min(isDev ? 1000 : 200, amountOption.getAsInt()));
            int oldVolume = player.audioPlayer.getVolume();
            if (newVolume == oldVolume) {
                interaction.reply(Text.translatable("player.volume.warn", newVolume)).setEphemeral(true).queue();
            }else {
                player.audioPlayer.setVolume(newVolume);
                interaction.reply(Text.translatable("player.volume.replace", oldVolume, newVolume)).setEphemeral(true).queue();
            }
        }
    }

    private static void executeControl(GuildCommandInteraction interaction) {
        ActionRow actionRowVolume = ActionRow.of(
                VolumeButton.create("🔊 8%", ButtonStyle.SECONDARY, new Perms(PermissionRole.DJ), 8),
                VolumeButton.create("🔊 35%", ButtonStyle.SUCCESS, new Perms(PermissionRole.DJ), 35),
                VolumeButton.create("🔊 60%", ButtonStyle.SUCCESS, new Perms(PermissionRole.DJ), 60),
                VolumeButton.create("🔊 100%", ButtonStyle.SUCCESS, new Perms(PermissionRole.DJ), 100),
                VolumeButton.create("🔊 200%", ButtonStyle.DANGER, new Perms(PermissionRole.DJ), 200)
        );
        ActionRow actionRowControl1 = ActionRow.of(
                PauseButton.create("▶||",ButtonStyle.SECONDARY, new Perms(PermissionRole.DJ)),
                StopButton.create("▉", ButtonStyle.DANGER, new Perms(PermissionRole.DJ)),
                SkipButton.create("▶▶|", ButtonStyle.PRIMARY, new Perms(PermissionRole.DJ)),
                ListButton.create("☰", ButtonStyle.PRIMARY, new Perms(PermissionRole.DJ)),
                InfoButton.create("?", ButtonStyle.PRIMARY, new Perms(PermissionRole.DJ))
        );
        ActionRow actionRowControl2 = ActionRow.of(
                ReplayButton.create("⤾", ButtonStyle.PRIMARY, new Perms(PermissionRole.DJ)),
                RepeatButton.create("⥄", ButtonStyle.SUCCESS, new Perms(PermissionRole.DJ)),
                DeleteButton.create(Text.translatable("remove"), ButtonStyle.SECONDARY, new Perms(interaction.getUser().getId()))
        );

        interaction.reply(Text.translatable("player.controlPanel")).addActionRows(
                actionRowVolume,
                actionRowControl1,
                actionRowControl2
        ).delay(1, TimeUnit.HOURS).flatMap(InteractionHook::deleteOriginal).queue(null, throwable -> {});
    }
}
