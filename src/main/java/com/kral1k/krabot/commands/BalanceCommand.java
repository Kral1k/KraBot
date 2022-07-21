package com.kral1k.krabot.commands;

import com.kral1k.krabot.command.CommandDispatcher;
import com.kral1k.krabot.command.GuildCommandInteraction;

public class BalanceCommand {
    public static void register(CommandDispatcher<GuildCommandInteraction> dispatcher) {
        dispatcher.register("balance", "баланс").executor(interaction -> {
            double balance = interaction.getMember().getBalance();
            interaction.replayTranslatable("balance", balance).setEphemeral(true).queue();
        });
    }
}
