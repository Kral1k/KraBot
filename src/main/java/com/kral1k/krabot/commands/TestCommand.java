package com.kral1k.krabot.commands;

import com.kral1k.krabot.button.test.ButtonManager;
import com.kral1k.krabot.button.test.ButtonBuilder;
import com.kral1k.krabot.command.CommandDispatcher;
import com.kral1k.krabot.command.GuildCommandInteraction;
import com.kral1k.krabot.permission.PermissionRole;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

public class TestCommand {


    public static void register(CommandDispatcher<GuildCommandInteraction> dispatcher) {
        dispatcher.register("test", "test").addOption(new OptionData(OptionType.STRING, "string", "s", false)).executor(interaction -> {
            Button button = ButtonManager.newBuilder().predicate(source -> {
                return source.hasPermission(PermissionRole.DJ);
            }).setStyle(ButtonStyle.DANGER).setDisabled(true).build();
            Button button1 = new ButtonBuilder().predicate(interaction.getMemberId()).execute(executor -> {
                executor.reply("dawdawd").queue();
            }).build();

            interaction.reply("TEST BUTTON").addActionRow(button, button1).queue();

//            MessageChannel channel = interaction.getChannel();
//            EmbedBuilder embedBuilder = new EmbedBuilder();
//            embedBuilder.setColor(new Color(186, 22, 4));
//            embedBuilder.setTitle("Правила.");
//            embedBuilder.addField("1. Следуйте TOS Discord.", "Обязательно следуйте условиям использования Discord, которые можно найти на https://discordapp.com/tos", false);
//            embedBuilder.addField("2. Будьте уважительны.", "Расистские, сексистские, гомофобные, ксенофобные, трансфобные, эйллистские, разжигающие ненависть высказывания, оскорбления или любое другое унизительное, токсичное или дискриминационное поведение недопустимы.", false);
//            embedBuilder.addField("3. Спам.", "Включая, но не ограничиваясь: любые сообщения, которые не участвуют в разговоре, повторяющиеся сообщения, случайные теги пользователей.", false);
//            embedBuilder.addField("4. Реклама.", "Не продвигайте что-либо без предварительного разрешения администратора.", false);
//            embedBuilder.setFooter("Последнее обновление");
//            embedBuilder.setTimestamp(OffsetDateTime.now());
//            interaction.reply("ok").setEphemeral(true).queue();
//            channel.sendMessageEmbeds(embedBuilder.build()).queue();
        });

//        dispatcher.register("test", "test").addSubCommandGroup(CommandManager.subCommandGroup("puzzle", "puzzle").addSubcommand(CommandManager.subCommand("load", "load pack").addOption(new OptionData(OptionType.STRING, "name", "Название пака", true)).executor(interaction -> {
//
//
//
//        }))).addSubCommand(CommandManager.subCommand("load", "load game").addOption(new OptionData(OptionType.STRING, "game", "Выбор игры", true).addChoice("puzzle", "PUZZLE")).executor(interaction -> {
////            executorLoad(interaction);
//        })).addSubCommand(CommandManager.subCommand("unload", "unload game").executor(interaction -> {
////            executorUnload(interaction);
//        }));
    }
}
