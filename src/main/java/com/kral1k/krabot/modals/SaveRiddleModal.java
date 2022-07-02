package com.kral1k.krabot.modals;

import com.kral1k.krabot.game.games.puzzle.PuzzlePack;
import com.kral1k.krabot.game.games.puzzle.editor.PuzzlePackBuilderManager;
import com.kral1k.krabot.game.games.puzzle.editor.PuzzlePackRiddleBuilder;
import com.kral1k.krabot.modal.GuildModalInteraction;
import com.kral1k.krabot.modal.ModalDispatcher;
import com.kral1k.krabot.utils.GuildDirectory;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class SaveRiddleModal {
    public static void register(ModalDispatcher<GuildModalInteraction> dispatcher) {
        dispatcher.register("saveriddle", interaction -> {
            String puzzleId = interaction.getValue("id").getAsString();
            Path directory = interaction.getGuild().getDirectory(GuildDirectory.GAME_PUZZLE).resolve(puzzleId);
            if (Files.notExists(directory)) {
                interaction.reply("Pack not found").setEphemeral(true).queue();
                return;
            }

            try {
                PuzzlePackBuilderManager manager = PuzzlePackBuilderManager.unHashedManager(interaction.getMemberId());
                PuzzlePackRiddleBuilder builder = manager.getBuilder();
                PuzzlePack.newBuilder(interaction.getGuild(), puzzleId).addRiddle(builder.getId(), builder.build()).build();
                interaction.deferEdit().setContent(manager.getBuilder().getId() + " saved").setActionRows().delay(5, TimeUnit.SECONDS).flatMap(InteractionHook::deleteOriginal).queue(null, throwable -> {});
            } catch (IOException e) {
                e.printStackTrace();
                interaction.reply("Saved error").setEphemeral(true).queue();
            }
        });
    }

    public static Modal create() {
        TextInput id = TextInput.create("id", "PackId", TextInputStyle.SHORT)
                .setPlaceholder("packId")
                .setMinLength(3)
                .setMaxLength(16)
                .build();

        return Modal.create("saveriddle", "Riddle Saver")
                .addActionRows(ActionRow.of(id))
                .build();
    }
}
