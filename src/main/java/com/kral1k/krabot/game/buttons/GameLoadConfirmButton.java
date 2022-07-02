package com.kral1k.krabot.game.buttons;

import com.google.gson.Gson;
import com.kral1k.krabot.button.ButtonData;
import com.kral1k.krabot.button.ButtonDispatcher;
import com.kral1k.krabot.button.GuildButtonInteraction;
import com.kral1k.krabot.game.*;
import com.kral1k.krabot.permission.Perms;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

public class GameLoadConfirmButton {
    public static void register(ButtonDispatcher<GuildButtonInteraction> dispatcher) {
        dispatcher.register("glc", interaction -> {
            GameManager gameManager = interaction.getGuild().getGameManager();
            Data data = Data.from(interaction.getComponentId());
            interaction.deferEdit().queue();
            if (data.type == 0) {
                gameManager.stop();
                Game loader = gameManager.loadGame(Games.valueOf(data.game));
                loader.start(interaction.getMessage());
            } else if (data.type == 1) {
                String memberId = interaction.getMember().getId();
                gameManager.stop(memberId);
                MiniGame loader = gameManager.loadMiniGame(memberId, MiniGames.valueOf(data.game));
                loader.start(interaction.getMessage(), interaction.getMember());
            }
        });
    }

    public static Button create(String label, ButtonStyle style, Perms permission, int type, String game) {
        return Button.of(style, new Gson().toJson(new Data("glc", permission, type, game)), label);
    }

    private static class Data extends ButtonData {
        public final int type;
        public final String game;

        public Data(String id, Perms permission, int type, String game) {
            super(id, permission);
            this.type = type;
            this.game = game;
        }

        public static Data from(String componentId) {
            return GSON.fromJson(componentId, Data.class);
        }
    }
}
