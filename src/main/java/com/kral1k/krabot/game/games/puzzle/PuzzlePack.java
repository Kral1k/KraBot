package com.kral1k.krabot.game.games.puzzle;

import com.kral1k.krabot.game.games.puzzle.editor.PuzzlePackBuilder;
import com.kral1k.krabot.guild.Guild;
import com.kral1k.krabot.utils.GsonHelper;
import com.kral1k.krabot.utils.GuildDirectory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PuzzlePack {
    private transient Path riddlesDirectory;
    private final String name;
    private final String description;
    private final String[] riddles;

    public static boolean exists(Guild guild, String puzzleId) {
        return Files.exists(guild.getDirectory(GuildDirectory.GAME_PUZZLE).resolve(puzzleId));
    }

    public static PuzzlePackBuilder newBuilder(Guild guild, String puzzleId) {
        return new PuzzlePackBuilder(guild, puzzleId);
    }

    public PuzzlePack(String name, String description, String... riddles) {
        this.name = name;
        this.description = description;
        this.riddles = riddles;
    }

    private PuzzlePack setDirectory(Path directory) {
        this.riddlesDirectory = directory;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getRiddleLength() {
        return riddles.length;
    }

    public String[] getRiddles() {
        return riddles;
    }

    public static PuzzlePack load(Path path) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            return GsonHelper.deserialize(reader, PuzzlePack.class).setDirectory(path.getParent().resolve("riddles"));
        }
    }

    public PuzzleContent loadContent() throws IOException {

        List<PuzzleContent.Riddle> riddleList = new ArrayList<>();

        Consumer<BufferedReader> consumer = reader -> {
            PuzzleContent.Riddle riddle = GsonHelper.deserialize(reader, PuzzleContent.Riddle.class);
            if (riddle.answers.length < 2) return;
            riddleList.add(riddle);
        };

        if (riddles == null || riddles.length < 1) {
            File[] files = riddlesDirectory.toFile().listFiles();
            if (files == null) throw new FileNotFoundException();
            for (File file : files) {
                if (file.isDirectory()) continue;
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    consumer.accept(reader);
                }
            }
        } else {
            for (String pathString : riddles) {
                Path path = riddlesDirectory.resolve(pathString.replace(":", "/") + ".json");
                if (Files.notExists(path)) continue;
                try (BufferedReader reader = Files.newBufferedReader(path)) {
                    consumer.accept(reader);
                }
            }
        }
        if (riddleList.isEmpty()) throw new RuntimeException();

        return new PuzzleContent(riddleList);
    }
}
