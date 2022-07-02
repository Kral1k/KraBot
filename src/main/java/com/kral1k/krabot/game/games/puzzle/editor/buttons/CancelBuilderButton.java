package com.kral1k.krabot.game.games.puzzle.editor.buttons;

import com.google.gson.Gson;
import com.kral1k.krabot.button.ButtonData;
import com.kral1k.krabot.button.ButtonDispatcher;
import com.kral1k.krabot.button.GuildButtonInteraction;
import com.kral1k.krabot.game.games.puzzle.editor.PuzzlePackBuilderManager;
import com.kral1k.krabot.permission.Perms;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

public class CancelBuilderButton {
    public static void register(ButtonDispatcher<GuildButtonInteraction> dispatcher) {
        dispatcher.register("ppcb", interaction -> {
            PuzzlePackBuilderManager.unHashedManager(interaction.getMemberId());
            interaction.getMessage().delete().queue();
        });
    }

    public static Button create(String label, ButtonStyle style, Perms permission) {
        return Button.of(style, new Gson().toJson(new ButtonData("ppcb", permission)), label);
    }
}
