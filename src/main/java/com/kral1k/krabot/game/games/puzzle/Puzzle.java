package com.kral1k.krabot.game.games.puzzle;

import com.kral1k.krabot.game.GameException;
import com.kral1k.krabot.game.MiniGame;
import com.kral1k.krabot.game.games.puzzle.buttons.NextAnswerButton;
import com.kral1k.krabot.game.games.puzzle.screens.*;
import com.kral1k.krabot.game.screen.ScreenHandler;
import com.kral1k.krabot.guild.Guild;
import com.kral1k.krabot.guild.member.Member;
import com.kral1k.krabot.permission.Perms;
import com.kral1k.krabot.utils.GuildDirectory;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.requests.restaction.interactions.MessageEditCallbackAction;
import net.dv8tion.jda.internal.interactions.component.ButtonImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Puzzle implements MiniGame {
    private final Path directory;
    private String ownerId;
    private Member owner;
    private PuzzleStatus status = PuzzleStatus.INACTIVE;
    private ScreenHandler screenHandler;
    private PuzzlePack puzzlePack;
    private final Queue<PuzzleContent.Riddle> queue = new LinkedList<>();
    private PuzzleContent.Riddle riddle;
    private int packSize;
    private int point;

    public Puzzle(Guild guild) {
        this.directory = guild.getDirectory(GuildDirectory.GAME_PUZZLE);
    }

    public void setStatus(PuzzleStatus status) {
        this.status = status;
    }

    @Override
    public void start(Message message, Member owner) {
        this.screenHandler = new ScreenHandler(message);
        this.setStatus(PuzzleStatus.WAITING_PACK_LOAD);
        screenHandler.setScreen(new FirsScreen());
        this.ownerId = owner.getId();
        this.owner = owner;
    }

    public PuzzleStatus getStatus() {
        return status;
    }

    public void loadPack(String name) throws GameException {
        if (status != PuzzleStatus.WAITING_PACK_LOAD) throw new GameException("Команда недоступна!");
        Path path = directory.resolve(name + "/pack.json");
        if (Files.notExists(path)) throw new GameException("Pack not found");
        try {
            this.puzzlePack = PuzzlePack.load(path);
            screenHandler.setScreen(new AcceptPackScreen(puzzlePack, ownerId));
            this.setStatus(PuzzleStatus.WAITING_PACK_ACCEPT);
        } catch (IOException e) {
            throw new GameException("ERROR");
        }
    }

    public void acceptPack(MessageEditCallbackAction action) throws GameException {
        try {
            PuzzleContent content = puzzlePack.loadContent();
            queue.addAll(content.getRiddleList());
            screenHandler.setScreen(action, new StartPuzzleScreen(ownerId));
            this.setStatus(PuzzleStatus.WAITING_START_GAME);
        } catch (Throwable e) {
            screenHandler.setScreen(action, new PuzzleLoadContentErrorScreen());
            this.setStatus(PuzzleStatus.WAITING_PACK_LOAD);
        }
    }

    public void cancelPack(MessageEditCallbackAction action) {
        screenHandler.setOldScreen(action);
        this.setStatus(PuzzleStatus.WAITING_PACK_LOAD);
    }

    public void startPuzzle(MessageEditCallbackAction action) {
        this.packSize = queue.size();
        this.riddle = queue.poll();
        screenHandler.setScreen(action, new RiddleScreen(riddle, ownerId));
        this.setStatus(PuzzleStatus.ACTIVE);
    }

    public void correctly(MessageEditCallbackAction action, int puzzleId) {
        if (riddle.answers[puzzleId].correctly) this.point += 1;

        List<Button> buttons = new ArrayList<>();
        for (int i=0; i<Math.min(5, riddle.answers.length); i++) {

            PuzzleContent.Answer answer = riddle.answers[i];
            buttons.add(new ButtonImpl("null" + i, answer.text,
                    answer.correctly ? ButtonStyle.SUCCESS : ButtonStyle.DANGER,
                    (i != puzzleId), null));
        }
        ActionRow abstractAnswerRows = ActionRow.of(buttons);
        ActionRow nextAnswerRow = ActionRow.of(NextAnswerButton.create("Продолжить.", ButtonStyle.PRIMARY, new Perms(ownerId)));
        action.setActionRows(abstractAnswerRows, nextAnswerRow).queue();
    }

    public void next(MessageEditCallbackAction action) {
        if (queue.isEmpty()) {
            screenHandler.setScreen(action, new EndGameScreen(ownerId, packSize, point));
            owner.addBalance(point);
            this.setStatus(PuzzleStatus.WAITING_PACK_LOAD);
        } else {
            this.riddle = queue.poll();
            screenHandler.setScreen(action, new RiddleScreen(riddle, ownerId));
        }
    }

    public void restart() {
        try {
            queue.clear();
            PuzzleContent content = puzzlePack.loadContent();
            queue.addAll(content.getRiddleList());
            screenHandler.setScreen(new StartPuzzleScreen(ownerId));
            this.setStatus(PuzzleStatus.WAITING_START_GAME);
        } catch (Throwable e) {
            screenHandler.setScreen(new PuzzleLoadContentErrorScreen());
            this.setStatus(PuzzleStatus.WAITING_PACK_LOAD);
        }
    }

    @Override
    public void stop() {
        screenHandler.getMessage().delete().queue();
    }
}
