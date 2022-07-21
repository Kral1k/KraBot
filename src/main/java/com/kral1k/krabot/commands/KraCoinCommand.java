package com.kral1k.krabot.commands;

import com.kral1k.krabot.command.CommandDispatcher;
import com.kral1k.krabot.command.CommandException;
import com.kral1k.krabot.command.CommandManager;
import com.kral1k.krabot.command.GuildCommandInteraction;
import com.kral1k.krabot.guild.member.Member;
import com.kral1k.krabot.language.Text;
import com.kral1k.krabot.permission.PermissionRole;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class KraCoinCommand {
    public static void register(CommandDispatcher<GuildCommandInteraction> dispatcher) {
        dispatcher.register("kracoin", "kracoin").predicate(source -> {
            return source.hasPermission(PermissionRole.DEVELOPER);
        }).addSubCommand(CommandManager.subCommand("info", "info").addOption(new OptionData(OptionType.USER, "user", "user")).executor(interaction -> {
            OptionMapping optionMapping = interaction.getOption("user");
            Member member = optionMapping == null ? interaction.getMember() : interaction.getGuild().getMember(optionMapping.getAsUser());
            if (member == null) throw new CommandException(Text.translatable("member.notFound"));
            interaction.replayTranslatable("bcoin.balance", member.getAsMention(), member.getBalance()).setEphemeral(true).queue();
        })).addSubCommand(CommandManager.subCommand("give", "give").addOption(new OptionData(OptionType.USER, "user", "user", true)).addOption(new OptionData(OptionType.NUMBER, "amount", "amount", true)).executor(interaction -> {
            double amount = interaction.getOption("amount").getAsDouble();
            OptionMapping optionMapping = interaction.getOption("user");
            Member member = optionMapping == null ? interaction.getMember() : interaction.getGuild().getMember(optionMapping.getAsUser());
            if (member == null) throw new CommandException(Text.translatable("member.notFound"));
            member.setBalance(member.getBalance() + amount);
            interaction.replayTranslatable("bcoin.give", member.getAsMention(), member.getBalance()).setEphemeral(true).queue();
        })).addSubCommand(CommandManager.subCommand("set", "set").addOption(new OptionData(OptionType.USER, "user", "user", true)).addOption(new OptionData(OptionType.NUMBER, "amount", "amount", true)).executor(interaction -> {
            double amount = interaction.getOption("amount").getAsDouble();
            OptionMapping optionMapping = interaction.getOption("user");
            Member member = optionMapping == null ? interaction.getMember() : interaction.getGuild().getMember(optionMapping.getAsUser());
            if (member == null) throw new CommandException(Text.translatable("member.notFound"));
            member.setBalance(amount);
            interaction.replayTranslatable("bcoin.set", member.getAsMention(), member.getBalance()).setEphemeral(true).queue();
        }));
    }
}
