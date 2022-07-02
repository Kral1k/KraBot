package com.kral1k.krabot.modals;

import com.kral1k.krabot.game.games.puzzle.PuzzlePack;
import com.kral1k.krabot.modal.GuildModalInteraction;
import com.kral1k.krabot.modal.ModalDispatcher;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

import java.io.IOException;

public class PuzzlePackModal {
    public static void register(ModalDispatcher<GuildModalInteraction> dispatcher) {
        dispatcher.register("test", interaction -> {
            String puzzleId = interaction.getValue("id").getAsString();
            String name = interaction.getValue("name").getAsString();
            String description = interaction.getValue("description").getAsString();


            if (PuzzlePack.exists(interaction.getGuild(), puzzleId)) {
                interaction.reply("Пак уже существукт!").setEphemeral(true).queue();
                return;
            }

            try {
                PuzzlePack.newBuilder(interaction.getGuild(), puzzleId)
                        .setName(name).setDescription(description)
                        .build();
            } catch (IOException e) {
                interaction.reply("Ошибка!").setEphemeral(true).queue();
            } finally {
                interaction.reply("Пак успешно создан").setEphemeral(true).queue();
            }

        });
    }

    public static Modal create() {
        TextInput id = TextInput.create("id", "Id", TextInputStyle.SHORT)
                .setPlaceholder("puzzle id")
                .setMinLength(3)
                .setMaxLength(16) // or setRequiredRange(10, 100)
                .build();
        TextInput name = TextInput.create("name", "Name", TextInputStyle.SHORT)
                .setPlaceholder("puzzle name")
                .setMinLength(3)
                .setMaxLength(20) // or setRequiredRange(10, 100)
                .build();
        TextInput description = TextInput.create("description", "Description", TextInputStyle.PARAGRAPH)
                .setPlaceholder("puzzle description")
                .setMaxLength(1000)
                .build();

        return Modal.create("test", "Puzzle Pack Builder")
                .addActionRows(ActionRow.of(id), ActionRow.of(name), ActionRow.of(description))
                .build();
    }
}
