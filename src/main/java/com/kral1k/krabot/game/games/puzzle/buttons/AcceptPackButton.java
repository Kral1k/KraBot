package com.kral1k.krabot.game.games.puzzle.buttons;

import com.google.gson.Gson;
import com.kral1k.krabot.button.ButtonData;
import com.kral1k.krabot.button.ButtonDispatcher;
import com.kral1k.krabot.button.GuildButtonInteraction;
import com.kral1k.krabot.game.GameException;
import com.kral1k.krabot.game.GameManager;
import com.kral1k.krabot.game.games.puzzle.Puzzle;
import com.kral1k.krabot.permission.Perms;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

public class AcceptPackButton {
    public static void register(ButtonDispatcher<GuildButtonInteraction> dispatcher) {
        dispatcher.register("pap", interaction -> {
            GameManager gameManager = interaction.getGuild().getGameManager();
            try {
                Puzzle puzzle = gameManager.getMiniGame(interaction.getMemberId(), Puzzle.class);
                puzzle.acceptPack(interaction.deferEdit());
            } catch (GameException e) {
                interaction.reply(e.getMessage()).setEphemeral(true).queue();
            }
        });
    }

    public static Button create(String label, ButtonStyle style, Perms permission) {
        return Button.of(style, new Gson().toJson(new ButtonData("pap", permission)), label);
    }
}
