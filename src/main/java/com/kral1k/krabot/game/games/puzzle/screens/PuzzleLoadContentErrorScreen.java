package com.kral1k.krabot.game.games.puzzle.screens;

import com.kral1k.krabot.game.screen.Screen;
import net.dv8tion.jda.api.interactions.components.ActionRow;

public class PuzzleLoadContentErrorScreen implements Screen {
    @Override
    public String getContent() {
        return "Упс. Ошибка инициализации!";
    }

    @Override
    public ActionRow[] getActionRows() {
        return new ActionRow[0];
    }
}
