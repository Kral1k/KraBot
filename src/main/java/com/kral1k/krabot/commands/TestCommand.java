package com.kral1k.krabot.commands;

import com.kral1k.krabot.command.CommandDispatcher;
import com.kral1k.krabot.command.GuildCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class TestCommand {


    public static void register(CommandDispatcher<GuildCommandInteraction> dispatcher) {
        dispatcher.register("test", "test").addOption(new OptionData(OptionType.STRING, "string", "s", false)).executor(interaction -> {
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
