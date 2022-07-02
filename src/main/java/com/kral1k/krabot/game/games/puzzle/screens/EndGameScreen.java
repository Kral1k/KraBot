package com.kral1k.krabot.game.games.puzzle.screens;

import com.kral1k.krabot.game.screen.Screen;
import net.dv8tion.jda.api.interactions.components.ActionRow;

import java.util.StringJoiner;

public class EndGameScreen implements Screen {

    private final String ownerId;
    private final int packSize;
    private final int correctlySize;

    public EndGameScreen(String ownerId, int packSize, int correctlySize) {
        this.ownerId = ownerId;
        this.packSize = packSize;
        this.correctlySize = correctlySize;
    }

    @Override
    public String getContent() {
        StringJoiner stringJoiner = new StringJoiner("\n");
        stringJoiner.add("Игра завершена. Верных ответов - " + correctlySize + "/" + packSize);
        if (correctlySize > 0) stringJoiner.add(correctlySize + " KraCoin зачислено на баланс");
        stringJoiner.add("-----------------------");
        stringJoiner.add("Для начала игры загрузите пакет, используйте /game puzzle load: name");
        return stringJoiner.toString();
    }

    @Override
    public ActionRow[] getActionRows() {
        return new ActionRow[0];
    }
}
