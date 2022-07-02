package com.kral1k.krabot.game;

import com.kral1k.krabot.game.games.puzzle.Puzzle;
import com.kral1k.krabot.guild.Guild;

import java.util.HashMap;
import java.util.Map;

public class GameManager {

    private final Guild guild;
    private final Map<String, MiniGame> miniGameMap = new HashMap<>();
    private Game game;

    public GameManager(Guild guild) {
        this.guild = guild;
    }

    public MiniGame loadMiniGame(String memberId, MiniGames miniGames) throws GameNotFoundException {
        MiniGame miniGame;
        switch (miniGames) {
            case Puzzle -> miniGame = new Puzzle(guild);
            default -> throw new GameNotFoundException();
        }
        miniGameMap.put(memberId, miniGame);
        return miniGame;
    }

    public <T extends MiniGame> T getMiniGame(String memberId, Class<T> tClass) throws GameException {
        if (!miniGameMap.containsKey(memberId)) throw new GameException("Game not loaded");
        MiniGame miniGame = miniGameMap.get(memberId);
        return tClass.cast(miniGame);
    }

    public Game loadGame(Games games) throws GameNotFoundException {
        switch (games) {
            case Puzzle -> game = null;
            default -> throw new GameNotFoundException();
        }
        return game;
    }

    public <T extends Game> T getGame(Class<T> tClass) throws GameException {
        if (game == null) throw new GameException("Game not loaded");
        return tClass.cast(game);
    }

    public boolean isGaming(String memberId) {
        return miniGameMap.containsKey(memberId);
    }

    public boolean isGaming() {
        return game != null;
    }

    public boolean stop(String memberId) {
        if (!this.isGaming(memberId)) return false;
        miniGameMap.get(memberId).stop();
        miniGameMap.remove(memberId);
        return true;
    }

    public boolean stop() {
        if (!this.isGaming()) return false;
        game.stop();
        this.game = null;
        return true;
    }
}
