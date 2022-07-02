package com.kral1k.krabot.game.games.puzzle.screens;

import com.kral1k.krabot.game.games.puzzle.PuzzleContent;
import com.kral1k.krabot.game.games.puzzle.buttons.AnswerButton;
import com.kral1k.krabot.game.screen.Screen;
import com.kral1k.krabot.permission.Perms;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.util.ArrayList;
import java.util.List;

public class RiddleScreen implements Screen {
    private final PuzzleContent.Riddle riddle;
    private final String ownerId;

    public RiddleScreen(PuzzleContent.Riddle riddle, String ownerId) {
        this.riddle = riddle;
        this.ownerId = ownerId;
    }

    @Override
    public String getContent() {
        return riddle.text;
    }

    @Override
    public ActionRow[] getActionRows() {
        List<Button> buttons = new ArrayList<>();

        for (int i = 0; i < Math.min(riddle.answers.length, 5); i++) {
            PuzzleContent.Answer answer = riddle.answers[i];
            buttons.add(AnswerButton.create(answer.text, ButtonStyle.PRIMARY, new Perms(ownerId), i));
        }

        return new ActionRow[] { ActionRow.of(buttons) };
    }
}
