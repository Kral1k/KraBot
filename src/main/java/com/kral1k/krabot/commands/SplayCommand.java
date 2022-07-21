package com.kral1k.krabot.commands;

import com.kral1k.krabot.api.YouTubeSearch;
import com.kral1k.krabot.api.YouTubeSearchResponse;
import com.kral1k.krabot.buttons.*;
import com.kral1k.krabot.command.CommandDispatcher;
import com.kral1k.krabot.command.GuildCommandInteraction;
import com.kral1k.krabot.language.Text;
import com.kral1k.krabot.permission.PermissionRole;
import com.kral1k.krabot.permission.Perms;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class SplayCommand {
    public static void register(CommandDispatcher<GuildCommandInteraction> dispatcher) {
        dispatcher.register("splay", "splay").addOption(new OptionData(STRING, "name", "Название трека", true)).predicate(source -> {
            return source.hasPermission(PermissionRole.DJ);
        }).executor(interaction -> {
            String name = interaction.getOption("name").getAsString();
            String key = interaction.getGuild().getProperties().youTubeApiToken;

            interaction.deferReply().queue();
            InteractionHook hook = interaction.getHook();

            YouTubeSearch.newRequest(key, name).setType(YouTubeSearch.Type.VIDEO).setRegionCode("UA").sendAsync((statusCode, body) -> {
                if (statusCode != 200) hook.sendMessage(Text.translatable("api.error")).queue();
                else if (body.getItems().isEmpty()) hook.sendMessage(Text.translatable("notFound")).queue();
                else {
                    Collection<Button> videoIdButtons = new ArrayList<>();
                    StringJoiner stringJoiner = new StringJoiner("\n");
                    for (int i = 0; i < Math.min(5, body.getItems().size()); i++) {
                        YouTubeSearchResponse.Body.Item item = body.getItem(i);
                        String videoId = item.getId().getVideoId();
                        String title =  item.getSnippet().getTitle();
                        videoIdButtons.add(SplayButton.create((i+1) + " ▶", ButtonStyle.PRIMARY, new Perms(PermissionRole.DJ), videoId));
                        stringJoiner.add("**" + (i+1) + ")** " + title);
                    }
                    ActionRow videoIdAction = ActionRow.of(videoIdButtons);
                    ActionRow controlAction = ActionRow.of(
                            PauseButton.create("▶||", ButtonStyle.SUCCESS, new Perms(PermissionRole.DJ)),
                            StopButton.create("▉", ButtonStyle.DANGER, new Perms(PermissionRole.DJ)),
                            SkipButton.create("▶▶|", ButtonStyle.PRIMARY, new Perms(PermissionRole.DJ)),
                            DeleteButton.create(Text.translatable("remove"), ButtonStyle.SECONDARY, new Perms(interaction.getUser().getId()))
                    );
                    hook.sendMessage(stringJoiner.toString()).addActionRows(videoIdAction, controlAction).delay(30, TimeUnit.MINUTES).flatMap(Message::delete).queue(null, error -> {});
                }
            });
        });
    }
}
