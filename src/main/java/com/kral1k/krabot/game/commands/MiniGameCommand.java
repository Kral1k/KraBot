package com.kral1k.krabot.game.commands;

import com.kral1k.krabot.buttons.DeleteButton;
import com.kral1k.krabot.command.CommandDispatcher;
import com.kral1k.krabot.command.CommandManager;
import com.kral1k.krabot.command.GuildCommandInteraction;
import com.kral1k.krabot.game.*;
import com.kral1k.krabot.game.buttons.GameLoadConfirmButton;
import com.kral1k.krabot.game.games.puzzle.Puzzle;
import com.kral1k.krabot.game.games.puzzle.editor.PuzzlePackBuilderManager;
import com.kral1k.krabot.modals.PuzzlePackModal;
import com.kral1k.krabot.modals.RiddleModal;
import com.kral1k.krabot.permission.PermissionRole;
import com.kral1k.krabot.permission.Perms;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

public class MiniGameCommand {
    public static void register(CommandDispatcher<GuildCommandInteraction> dispatcher) {

        OptionData optionData = new OptionData(OptionType.STRING, "name", "game name");
        for (MiniGames miniGame : MiniGames.values()) {
            optionData.addChoice(miniGame.name(), miniGame.name());
        }

        dispatcher.register("minigame", "minigame").addSubCommand(CommandManager.subCommand("load", "load").addOption(optionData).executor(interaction -> {
            OptionMapping optionMapping = interaction.getOption("name");
            GameManager gameManager = interaction.getGuild().getGameManager();
            String memberId = interaction.getMemberId();
            if (optionMapping == null) return;
            if (gameManager.isGaming(memberId)) {
                interaction.reply("Игра уже запущена! \nПредыдущая игра будет удалена. \nПродолжить?").addActionRow(
                        GameLoadConfirmButton.create("Да", ButtonStyle.SUCCESS, new Perms(memberId), 1, optionMapping.getAsString()),
                        DeleteButton.create("Нет", ButtonStyle.SECONDARY, new Perms(memberId))
                ).queue();
                return;
            }
            try {
                interaction.deferReply().queue();
                MiniGames miniGames = MiniGames.valueOf(optionMapping.getAsString());
                MiniGame miniGame = gameManager.loadMiniGame(memberId, miniGames);
                interaction.getHook().sendMessage("Загрузка...").queue(message -> {
                    miniGame.start(message, interaction.getMember());
                });
            } catch (IllegalArgumentException | GameNotFoundException e) {
                interaction.reply("Игра не найдена!").setEphemeral(true).queue();
            }
        })).addSubCommandGroup(CommandManager.subCommandGroup("puzzle", "puzzle").addSubcommand(CommandManager.subCommand("load", "load").addOption(new OptionData(OptionType.STRING, "name", "name", true)).executor(interaction -> {
            String name = interaction.getOption("name").getAsString();
            GameManager gameManager = interaction.getGuild().getGameManager();
            try {
                Puzzle puzzle = gameManager.getMiniGame(interaction.getMemberId(), Puzzle.class);
                puzzle.loadPack(name);
                interaction.reply("Запрос отправлен.").setEphemeral(true).queue();
            } catch (GameException e) {
                interaction.reply(e.getMessage()).setEphemeral(true).queue();
            }
        })).addSubcommand(CommandManager.subCommand("createpack", "Create new pack").permission(source -> source.hasPermission(PermissionRole.DJ)).executor(interaction -> {
            interaction.replyModal(PuzzlePackModal.create()).queue();
        })).addSubcommand(CommandManager.subCommand("createreiddle", "Create Riddle").permission(source -> source.hasPermission(PermissionRole.DEVELOPER)).executor(interaction -> {
            if (PuzzlePackBuilderManager.contains(interaction.getMemberId()))
                interaction.reply("Редактор уже открыт!");
            else
                interaction.replyModal(RiddleModal.create()).queue();
        })));
    }
}
