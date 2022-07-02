package com.kral1k.krabot.modal;

import com.kral1k.krabot.modals.AnswerModal;
import com.kral1k.krabot.modals.PuzzlePackModal;
import com.kral1k.krabot.modals.RiddleModal;
import com.kral1k.krabot.modals.SaveRiddleModal;

public class ModalManager {
    private final ModalDispatcher<GuildModalInteraction> dispatcher = new ModalDispatcher<>();

    public ModalManager() {
        PuzzlePackModal.register(dispatcher);
        RiddleModal.register(dispatcher);
        AnswerModal.register(dispatcher);
        SaveRiddleModal.register(dispatcher);
    }

    public void execute(GuildModalInteraction interaction) {
        try {
            dispatcher.execute(interaction.getModalId(), interaction);
        } catch (Throwable throwable) {
            interaction.reply(throwable.getMessage()).setEphemeral(true).queue();
        }
    }
}
