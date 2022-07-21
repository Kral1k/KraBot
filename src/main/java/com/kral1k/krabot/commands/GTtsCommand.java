package com.kral1k.krabot.commands;

import com.kral1k.krabot.command.CommandDispatcher;
import com.kral1k.krabot.command.CommandException;
import com.kral1k.krabot.command.GuildCommandInteraction;
import com.kral1k.krabot.language.Text;
import com.kral1k.krabot.permission.PermissionRole;
import com.kral1k.krabot.player.tts.GoogleTextToSpeech;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class GTtsCommand {
    public static void register(CommandDispatcher<GuildCommandInteraction> dispatcher) {
        dispatcher.register("gtts", "google-text-to-speech").addOption(new OptionData(OptionType.STRING, "message", "Ваше сообщение", true)).predicate(source -> {
            return source.hasPermission(PermissionRole.DJ);
        }).executor(interaction -> {
            String message = interaction.getOption("message").getAsString();

            AudioChannel audioChannel = interaction.getMember().getVoiceChannel();
            if(audioChannel == null) throw new CommandException(Text.translatable("player.channelNotFound"));
            interaction.getGuild().getAudioManager().openAudioConnection(audioChannel);

            interaction.deferReply(true).queue();

            GoogleTextToSpeech gTts = interaction.getGuild().getGTts();
            if (gTts == null) {
                interaction.getHook().sendMessage(Text.translatable("api.error")).queue();
                return;
            }
            gTts.synthesize(message);
            interaction.getHook().sendMessage(Text.translatable("gtts.play")).queue();
        });
    }

    private static void executeSettings(GuildCommandInteraction interaction) {
//        ActionRow actionRowVolume = ActionRow.of(
//                VolumeButton.create("🔊 8%", ButtonStyle.PRIMARY, new Permissions(PermissionRole.DJ), 8, true),
//                VolumeButton.create("🔊 35%", ButtonStyle.SUCCESS, new Permissions(PermissionRole.DJ), 35, true),
//                VolumeButton.create("🔊 60%", ButtonStyle.SUCCESS, new Permissions(PermissionRole.DJ), 60, true),
//                VolumeButton.create("🔊 100%", ButtonStyle.SUCCESS, new Permissions(PermissionRole.DJ), 100, true),
//                VolumeButton.create("🔊 200%", ButtonStyle.DANGER, new Permissions(PermissionRole.DJ), 200, true)
//        );
//
//        ActionRow actionRowSampleName = ActionRow.of(
//                SampleSynthesizeNameButton.create("Прослушать A", ButtonStyle.PRIMARY, new Permissions(PermissionRole.DJ), "ru-RU-Wavenet-A"),
//                SampleSynthesizeNameButton.create("Прослушать B", ButtonStyle.PRIMARY, new Permissions(PermissionRole.DJ), "ru-RU-Wavenet-B"),
//                SampleSynthesizeNameButton.create("Прослушать C", ButtonStyle.PRIMARY, new Permissions(PermissionRole.DJ), "ru-RU-Wavenet-C"),
//                SampleSynthesizeNameButton.create("Прослушать D", ButtonStyle.PRIMARY, new Permissions(PermissionRole.DJ), "ru-RU-Wavenet-D"),
//                SampleSynthesizeNameButton.create("Прослушать E", ButtonStyle.PRIMARY, new Permissions(PermissionRole.DJ), "ru-RU-Wavenet-E")
//        );
//
//        ActionRow actionRowName = ActionRow.of(
//                SynthesizeNameButton.create("Применить A", ButtonStyle.SUCCESS, new Permissions(PermissionRole.DJ), "ru-RU-Wavenet-A"),
//                SynthesizeNameButton.create("Применить B", ButtonStyle.SUCCESS, new Permissions(PermissionRole.DJ), "ru-RU-Wavenet-B"),
//                SynthesizeNameButton.create("Применить C", ButtonStyle.SUCCESS, new Permissions(PermissionRole.DJ), "ru-RU-Wavenet-C"),
//                SynthesizeNameButton.create("Применить D", ButtonStyle.SUCCESS, new Permissions(PermissionRole.DJ), "ru-RU-Wavenet-D"),
//                SynthesizeNameButton.create("Применить E", ButtonStyle.SUCCESS, new Permissions(PermissionRole.DJ), "ru-RU-Wavenet-E")
//        );
//        callback.reply("**Synthesizer Settings**").addActionRows(
//                actionRowSampleName,
//                actionRowName,
//                actionRowVolume
//        ).setEphemeral(true).queue();
    }

}
