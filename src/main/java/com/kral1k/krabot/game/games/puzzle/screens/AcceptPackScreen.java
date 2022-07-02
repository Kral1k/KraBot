package com.kral1k.krabot.game.games.puzzle.screens;

import com.kral1k.krabot.game.games.puzzle.PuzzlePack;
import com.kral1k.krabot.game.games.puzzle.buttons.AcceptPackButton;
import com.kral1k.krabot.game.games.puzzle.buttons.CancelPackButton;
import com.kral1k.krabot.game.screen.Screen;
import com.kral1k.krabot.permission.Perms;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.util.StringJoiner;

public class AcceptPackScreen implements Screen {
    private final PuzzlePack pack;
    private final String ownerId;

    public AcceptPackScreen(PuzzlePack pack, String ownerId) {
        this.pack = pack;
        this.ownerId = ownerId;
    }

    @Override
    public String getContent() {
        StringJoiner stringJoiner = new StringJoiner("\n");
        stringJoiner.add("Запрос на загрузку пака \"" + pack.getName() + "\"");
        if (pack.getDescription().isEmpty()) stringJoiner.add(pack.getDescription());
        stringJoiner.add("Загрузить?");
        return stringJoiner.toString();
    }

    @Override
    public ActionRow[] getActionRows() {
        return new ActionRow[] { ActionRow.of(
                AcceptPackButton.create("Да", ButtonStyle.SUCCESS, new Perms(ownerId)),
                CancelPackButton.create("Нет", ButtonStyle.DANGER, new Perms(ownerId))
        )};
    }
}
