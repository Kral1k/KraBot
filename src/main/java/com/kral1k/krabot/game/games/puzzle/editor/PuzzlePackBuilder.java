package com.kral1k.krabot.game.games.puzzle.editor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kral1k.krabot.game.games.puzzle.PuzzleContent;
import com.kral1k.krabot.game.games.puzzle.PuzzlePack;
import com.kral1k.krabot.guild.Guild;
import com.kral1k.krabot.utils.GuildDirectory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class PuzzlePackBuilder {

    private final Path directory;
    private boolean isEdit;
    private String name = "unknown";
    private String description;
    private final Map<String, PuzzleContent.Riddle> riddleMap = new HashMap<>();

    public PuzzlePackBuilder(Guild guild, String puzzleId) {
        directory = guild.getDirectory(GuildDirectory.GAME_PUZZLE).resolve(puzzleId);
        if (Files.exists(directory)) this.openEditor();
        else this.openBuilder();
    }

    public void openEditor() {
        try {
            PuzzlePack pack = PuzzlePack.load(directory.resolve("pack.json"));
            this.name = pack.getName();
            this.description = pack.getDescription();
            for (String riddlePath : pack.getRiddles()) {
                riddleMap.put(riddlePath, null);
            }
        } catch (Throwable ignore) {
            this.isEdit = false;
        } finally {
            this.isEdit = true;
        }
    }

    public void openBuilder() {
        this.isEdit = false;
    }


    public PuzzlePackBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public PuzzlePackBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public PuzzlePackBuilder addRiddle(String riddleName, PuzzleContent.Riddle riddle) {
        riddleMap.put(riddleName, riddle);
        return this;
    }

    public PuzzlePack build() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String[] riddlePaths = new String[0];
        if (!riddleMap.keySet().isEmpty()) riddlePaths = riddleMap.keySet().toArray(String[]::new);

        PuzzlePack puzzlePack = new PuzzlePack(name, description, riddlePaths);
        if (!isEdit) Files.createDirectories(directory);
        Path path = directory.resolve("pack.json");
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            gson.toJson(puzzlePack, writer);
        }

        Path riddlesDirectory = directory.resolve("riddles");
        if (!isEdit) Files.createDirectories(riddlesDirectory);
        for (Map.Entry<String, PuzzleContent.Riddle> entry : riddleMap.entrySet()) {
            if (entry.getValue() == null) continue;
            Path riddlePath = riddlesDirectory.resolve(entry.getKey() + ".json");
            try (BufferedWriter writer = Files.newBufferedWriter(riddlePath)) {
                gson.toJson(entry.getValue(), writer);
            }
        }
        return puzzlePack;
    }
}
