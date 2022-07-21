package com.kral1k.krabot.commands;

import com.kral1k.krabot.buttons.GttsSettingsButton;
import com.kral1k.krabot.command.CommandDispatcher;
import com.kral1k.krabot.command.GuildCommandInteraction;
import com.kral1k.krabot.permission.PermissionRole;
import com.kral1k.krabot.permission.Perms;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

public class SettingsCommand {
    public static void register(CommandDispatcher<GuildCommandInteraction> dispatcher) {
        dispatcher.register("settings", "bot settings").predicate(source -> source.hasPermission(PermissionRole.DEVELOPER)).executor(interaction -> {
            ActionRow sGtts = ActionRow.of(
                    GttsSettingsButton.create("Gtts setting", ButtonStyle.SUCCESS, new Perms(PermissionRole.DEVELOPER))
            );
            interaction.reply("BOT SETTINGS").addActionRows(sGtts).setEphemeral(true).queue();
        });
    }
}
