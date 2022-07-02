package com.kral1k.krabot.commands;

import com.kral1k.krabot.api.Wiki;
import com.kral1k.krabot.command.CommandDispatcher;
import com.kral1k.krabot.command.GuildCommandInteraction;
import com.kral1k.krabot.language.Text;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.concurrent.TimeUnit;

public class WikiCommand {

    public static void register(CommandDispatcher<GuildCommandInteraction> dispatcher) {
        dispatcher.register("wiki", "wikipedia").addOption(new OptionData(OptionType.STRING, "title", "title", true)).executor(interaction -> {
            interaction.deferReply().queue();
            InteractionHook hook = interaction.getHook();
            Wiki.request(interaction.getOption("title").getAsString(), (statusCode, data) -> {
                if (statusCode != 200) {
                    String message = Text.translatable(statusCode == 404 ? "notFound" : "api.error");
                    hook.sendMessage(message).delay(6, TimeUnit.SECONDS).flatMap(Message::delete).queue(null, throwable -> {});
                } else hook.sendMessage(data.extract).queue();
            });
        });
    }
}
