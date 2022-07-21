package com.kral1k.krabot.commands;

import com.kral1k.krabot.buttons.ClearButton;
import com.kral1k.krabot.buttons.DeleteButton;
import com.kral1k.krabot.command.CommandDispatcher;
import com.kral1k.krabot.command.GuildCommandInteraction;
import com.kral1k.krabot.language.Text;
import com.kral1k.krabot.permission.Perms;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.util.concurrent.TimeUnit;

public class ClearCommand {
    public static void register(CommandDispatcher<GuildCommandInteraction> dispatcher) {
        dispatcher.register("clear", "Очистка чата.").predicate(source -> {
            return source.hasPermission(Permission.MESSAGE_MANAGE);
        }).addOption(new OptionData(OptionType.INTEGER, "amount", "Сколько сообщений необходимо удалить? (по умолчанию 10)")).executor(interaction -> {
            OptionMapping amountOption = interaction.getOption("amount");
            int amount = amountOption == null ? 10 : (int) Math.min(200, Math.max(2, amountOption.getAsLong()));
            String userId = interaction.getUser().getId();
            interaction.reply(Text.translatable("clear.command", amount)).addActionRow(
                    DeleteButton.create(Text.translatable("cancel"), ButtonStyle.SECONDARY, new Perms(userId)),
                    ClearButton.create(Text.translatable("yes"), ButtonStyle.DANGER , new Perms(userId), amount)
            ).delay(5, TimeUnit.SECONDS).flatMap(InteractionHook::deleteOriginal).queue(null, throwable -> {});;
        });
    }
}
