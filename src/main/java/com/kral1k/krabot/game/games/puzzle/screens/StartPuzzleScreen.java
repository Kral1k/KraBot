package com.kral1k.krabot.game.games.puzzle.screens;

import com.kral1k.krabot.game.games.puzzle.buttons.StartPuzzleButton;
import com.kral1k.krabot.game.screen.Screen;
import com.kral1k.krabot.permission.Perms;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

public class StartPuzzleScreen implements Screen {

    private final String ownerId;

    public StartPuzzleScreen(String ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String getContent() {
        return "Пак успено загружен.";
    }

    @Override
    public ActionRow[] getActionRows() {
        return new ActionRow[] { ActionRow.of(
                StartPuzzleButton.create("Поехали.", ButtonStyle.SUCCESS, new Perms(ownerId))
        )};
    }
}
