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

public class AnswerButton {

    public static void register(ButtonDispatcher<GuildButtonInteraction> dispatcher) {
        dispatcher.register("pa", interaction -> {
            GameManager gameManager = interaction.getGuild().getGameManager();
            try {
                Puzzle puzzle = gameManager.getMiniGame(interaction.getMemberId(), Puzzle.class);
                Data data = Data.from(interaction.getComponentId());
                puzzle.correctly(interaction.deferEdit(), data.aId);
            } catch (GameException e) {
                interaction.reply(e.getMessage()).setEphemeral(true).queue();
            }
        });
    }

    public static Button create(String label, ButtonStyle style, Perms permission, int aId) {
        return Button.of(style, new Gson().toJson(new Data("pa", permission, aId)), label);
    }

    private static class Data extends ButtonData {
        private final int aId;

        public Data(String id, Perms permission, int aId) {
            super(id, permission);
            this.aId = aId;
        }

        public static Data from(String componentId) {
            return GSON.fromJson(componentId, Data.class);
        }
    }
}
