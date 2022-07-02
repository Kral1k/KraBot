package com.kral1k.krabot.modals;

import com.kral1k.krabot.game.games.puzzle.PuzzleContent;
import com.kral1k.krabot.game.games.puzzle.editor.PuzzlePackBuilderManager;
import com.kral1k.krabot.game.games.puzzle.editor.PuzzlePackRiddleBuilder;
import com.kral1k.krabot.modal.GuildModalInteraction;
import com.kral1k.krabot.modal.ModalDispatcher;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

public class AnswerModal {
    public static void register(ModalDispatcher<GuildModalInteraction> dispatcher) {
        dispatcher.register("answer", interaction -> {
            String text = interaction.getValue("text").getAsString();
            String correctlyString = interaction.getValue("correctly").getAsString();
            boolean correctly;
            try {
                correctly = Integer.parseInt(correctlyString) == 1;
            } catch (NumberFormatException e) {
                correctly = Boolean.parseBoolean(correctlyString);
            }

            PuzzlePackBuilderManager manager = PuzzlePackBuilderManager.getManager(interaction.getMemberId());
            PuzzlePackRiddleBuilder builder = manager.getBuilder();

            PuzzleContent.Answer answer = new PuzzleContent.Answer(text, correctly);
            if (manager.getModifyId() < 0) {
                builder.addAnswer(answer);
            } else {
                builder.setAnswer(manager.getModifyId(), answer);
                manager.setModifyId(-1);
            }
            interaction.deferEdit().setActionRows(manager.buildAction()).queue();
        });
    }

    public static Modal create() {
        TextInput text = TextInput.create("text", "Answer Text", TextInputStyle.SHORT)
                .setPlaceholder("answer text")
                .setMinLength(1)
                .setMaxLength(50)
                .build();
        TextInput correctly = TextInput.create("correctly", "Answer Correctly", TextInputStyle.SHORT)
                .setPlaceholder("(boolean: (true or false) | integer: (0 = false or 1 = true))")
                .setMinLength(0)
                .setMaxLength(5)
                .build();

        return Modal.create("answer", "Answer Builder")
                .addActionRows(ActionRow.of(text), ActionRow.of(correctly))
                .build();
    }
}
