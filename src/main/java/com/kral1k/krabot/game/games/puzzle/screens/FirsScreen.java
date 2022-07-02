package com.kral1k.krabot.game.games.puzzle.screens;

import com.kral1k.krabot.game.screen.Screen;
import net.dv8tion.jda.api.interactions.components.ActionRow;

public class FirsScreen implements Screen {
    @Override
    public String getContent() {
        return "**Добро паожаловать в игру \"Puzzle\"**" +
                "\nДля начала игры необходимо загрузить пакет, используйте /game puzzle load: name";
    }

    @Override
    public ActionRow[] getActionRows() {
        return new ActionRow[0];
    }
}
