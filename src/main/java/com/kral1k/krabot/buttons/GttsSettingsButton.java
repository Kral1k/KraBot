package com.kral1k.krabot.buttons;

import com.google.gson.Gson;
import com.kral1k.krabot.button.ButtonData;
import com.kral1k.krabot.button.ButtonDispatcher;
import com.kral1k.krabot.button.GuildButtonInteraction;
import com.kral1k.krabot.permission.PermissionRole;
import com.kral1k.krabot.permission.Perms;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

public class GttsSettingsButton {
    public static void register(ButtonDispatcher<GuildButtonInteraction> dispatcher) {
        dispatcher.register("sgtts", interaction -> {
            ActionRow gttsSample = ActionRow.of(
                    GttsSampleButton.create("Прослушать A", ButtonStyle.PRIMARY, new Perms(PermissionRole.DJ), "ru-RU-Wavenet-A"),
                    GttsSampleButton.create("Прослушать B", ButtonStyle.PRIMARY, new Perms(PermissionRole.DJ), "ru-RU-Wavenet-B"),
                    GttsSampleButton.create("Прослушать C", ButtonStyle.PRIMARY, new Perms(PermissionRole.DJ), "ru-RU-Wavenet-C"),
                    GttsSampleButton.create("Прослушать D", ButtonStyle.PRIMARY, new Perms(PermissionRole.DJ), "ru-RU-Wavenet-D"),
                    GttsSampleButton.create("Прослушать E", ButtonStyle.PRIMARY, new Perms(PermissionRole.DJ), "ru-RU-Wavenet-E")
            );

            ActionRow gttsName = ActionRow.of(
                    GttsNameButton.create("Применить A", ButtonStyle.SUCCESS, new Perms(PermissionRole.DJ), "ru-RU-Wavenet-A"),
                    GttsNameButton.create("Применить B", ButtonStyle.SUCCESS, new Perms(PermissionRole.DJ), "ru-RU-Wavenet-B"),
                    GttsNameButton.create("Применить C", ButtonStyle.SUCCESS, new Perms(PermissionRole.DJ), "ru-RU-Wavenet-C"),
                    GttsNameButton.create("Применить D", ButtonStyle.SUCCESS, new Perms(PermissionRole.DJ), "ru-RU-Wavenet-D"),
                    GttsNameButton.create("Применить E", ButtonStyle.SUCCESS, new Perms(PermissionRole.DJ), "ru-RU-Wavenet-E")
            );
            interaction.deferEdit().setContent("**Synthesizer Settings**").setActionRows(gttsSample, gttsName).queue();
        });
    }

    public static Button create(String label, ButtonStyle style, Perms permission) {
        return Button.of(style, new Gson().toJson(new ButtonData("sgtts", permission)), label);
    }
}
