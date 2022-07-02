package com.kral1k.krabot.button;

import com.kral1k.krabot.buttons.*;
import com.kral1k.krabot.game.buttons.GameLoadConfirmButton;
import com.kral1k.krabot.game.games.puzzle.buttons.*;
import com.kral1k.krabot.game.games.puzzle.editor.buttons.AddAnswerBuilderButton;
import com.kral1k.krabot.game.games.puzzle.editor.buttons.EditAnswerBuilderButton;
import com.kral1k.krabot.game.games.puzzle.editor.buttons.SaveAnswerBuilderButton;
import com.kral1k.krabot.language.Text;
import net.dv8tion.jda.api.entities.Message;

import java.util.concurrent.TimeUnit;

public class ButtonManager {

    private final ButtonDispatcher<GuildButtonInteraction> dispatcher = new ButtonDispatcher<>();

    public ButtonManager() {
        TestButton.register(dispatcher);
        ClearButton.register(dispatcher);
        DeleteButton.register(dispatcher);
        SplayButton.register(dispatcher);
        SkipButton.register(dispatcher);
        PauseButton.register(dispatcher);
        StopButton.register(dispatcher);
        InfoButton.register(dispatcher);
        ListButton.register(dispatcher);
        RepeatButton.register(dispatcher);
        ReplayButton.register(dispatcher);
        VolumeButton.register(dispatcher);

        GttsSettingsButton.register(dispatcher);
        GttsSampleButton.register(dispatcher);
        GttsNameButton.register(dispatcher);

        GameLoadConfirmButton.register(dispatcher);
        AcceptPackButton.register(dispatcher);
        StartPuzzleButton.register(dispatcher);
        AnswerButton.register(dispatcher);
        NextAnswerButton.register(dispatcher);
        CancelPackButton.register(dispatcher);

        AddAnswerBuilderButton.register(dispatcher);
        EditAnswerBuilderButton.register(dispatcher);
        SaveAnswerBuilderButton.register(dispatcher);
    }

    public void execute(String id, GuildButtonInteraction interaction) {
        try {
            dispatcher.execute(id, interaction);
        } catch (ButtonException exception) {
            this.sendThrowMessage(interaction, exception.getMessage());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            this.sendThrowMessage(interaction, Text.translatable("button.error"));
        }
    }

    private void sendThrowMessage(GuildButtonInteraction interaction, String message) {
        if (interaction.isAcknowledged()) interaction.getHook().sendMessage(message).delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue(null, error -> {});
        else interaction.reply(message).setEphemeral(true).queue();
    }
}
