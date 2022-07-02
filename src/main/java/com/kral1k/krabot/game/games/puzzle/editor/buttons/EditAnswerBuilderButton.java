package com.kral1k.krabot.game.games.puzzle.editor.buttons;

import com.google.gson.Gson;
import com.kral1k.krabot.button.ButtonData;
import com.kral1k.krabot.button.ButtonDispatcher;
import com.kral1k.krabot.button.GuildButtonInteraction;
import com.kral1k.krabot.game.games.puzzle.editor.PuzzlePackBuilderManager;
import com.kral1k.krabot.modals.AnswerModal;
import com.kral1k.krabot.permission.Perms;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

public class EditAnswerBuilderButton {
    public static void register(ButtonDispatcher<GuildButtonInteraction> dispatcher) {
        dispatcher.register("ppea", interaction -> {
            Data data = Data.from(interaction.getComponentId());
            PuzzlePackBuilderManager manager = PuzzlePackBuilderManager.getManager(interaction.getMemberId());
            manager.setModifyId(data.aId);
            interaction.replyModal(AnswerModal.create()).queue();
        });
    }

    public static Button create(String label, ButtonStyle style, Perms permission, int aId) {
        return Button.of(style, new Gson().toJson(new Data("ppea", permission, aId)), label);
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
