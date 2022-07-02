package com.kral1k.krabot.modals;

import com.kral1k.krabot.game.games.puzzle.editor.PuzzlePackBuilderManager;
import com.kral1k.krabot.game.games.puzzle.editor.PuzzlePackRiddleBuilder;
import com.kral1k.krabot.modal.GuildModalInteraction;
import com.kral1k.krabot.modal.ModalDispatcher;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

public class RiddleModal {
    public static void register(ModalDispatcher<GuildModalInteraction> dispatcher) {
        dispatcher.register("test2", interaction -> {
            String id = interaction.getValue("id").getAsString();
            String text = interaction.getValue("text").getAsString();

            PuzzlePackRiddleBuilder builder = new PuzzlePackRiddleBuilder(id, text);
            PuzzlePackBuilderManager manager = new PuzzlePackBuilderManager(builder, interaction.getMember());
            PuzzlePackBuilderManager.hasManager(interaction.getMemberId(), manager);
            interaction.reply("**Riddle Builder**" + "\n" + text).addActionRows(manager.buildAction()).queue();
        });
    }

    public static Modal create() {
        TextInput id = TextInput.create("id", "Riddle Id", TextInputStyle.SHORT)
                .setPlaceholder("riddle id")
                .setMinLength(1)
                .setMaxLength(16)
                .build();
        TextInput text = TextInput.create("text", "Riddle Text", TextInputStyle.PARAGRAPH)
                .setPlaceholder("riddle text")
                .setMinLength(1)
                .setMaxLength(500) // or setRequiredRange(10, 100)
                .build();

        return Modal.create("test2", "Riddle Builder")
                .addActionRows(ActionRow.of(id), ActionRow.of(text))
                .build();
    }
}
