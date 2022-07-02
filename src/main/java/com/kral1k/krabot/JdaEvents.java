package com.kral1k.krabot;

import com.kral1k.krabot.button.ButtonData;
import com.kral1k.krabot.button.ButtonManager;
import com.kral1k.krabot.button.GuildButtonInteraction;
import com.kral1k.krabot.command.CommandManager;
import com.kral1k.krabot.command.GuildCommandInteraction;
import com.kral1k.krabot.guild.Guild;
import com.kral1k.krabot.guild.member.Member;
import com.kral1k.krabot.language.Text;
import com.kral1k.krabot.modal.GuildModalInteraction;
import com.kral1k.krabot.modal.ModalManager;
import com.kral1k.krabot.player.tts.GoogleTextToSpeech;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.emoji.CustomEmoji;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.entities.emoji.UnicodeEmojiImpl;
import net.dv8tion.jda.internal.requests.Route;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;

public class JdaEvents extends ListenerAdapter {
    private static final Logger LOGGER = JDALogger.getLog(Bot.class);
    private final Bot bot;
    private final CommandManager commandManager;
    private final ButtonManager buttonManager;
    private final ModalManager modalManager;

    public JdaEvents(Bot bot) {
        this.bot = bot;
        this.commandManager = bot.getCommandManager();
        this.buttonManager = bot.getButtonManager();
        this.modalManager = bot.getModalManager();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        LOGGER.info(event.getUser().getAsTag() + " used command: " + event.getCommandString());
        if (event.isFromGuild()) {
            commandManager.execute(event.getName(), new GuildCommandInteraction(bot, event));
        } else {
            event.reply("This command is only available in the guild").setEphemeral(true).queue();
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        LOGGER.info(event.getUser().getAsTag() + " used button: " + event.getComponentId());
        if (event.isFromGuild()) {
            ButtonData data = ButtonData.from(event.getComponentId());
            if (!data.perms.has(event.getMember())) event.reply(Text.translatable("button.permission")).setEphemeral(true).queue();
            else buttonManager.execute(data.id, new GuildButtonInteraction(bot, event));
        }
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if (event.isFromGuild()) {
            modalManager.execute(new GuildModalInteraction(bot, event));
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (!event.isFromGuild()) {
            LOGGER.info(event.getAuthor().getId() + "|" + event.getAuthor().getAsTag() + " whispers to you: " + event.getMessage().getContentRaw());
            return;
        }

        Guild guild = this.bot.getGuild(event.getGuild());
        Member member = guild.getMember(event.getMember());

        if (event.getChannel().getId().equals("930075228959277076")) {

            AudioChannel audioChannel = member.getVoiceChannel();
            if(audioChannel == null) {
                event.getMessage().addReaction(Emoji.fromUnicode("❓")).queue();
                event.getMessage().delete().queueAfter(10, TimeUnit.SECONDS, null, t->{});
                return;
            }
            guild.getAudioManager().openAudioConnection(audioChannel);

            event.getGuild().getAudioManager().openAudioConnection(audioChannel);
            String messageRaw = event.getMessage().getContentRaw();
            GoogleTextToSpeech gTts = guild.getGTts();
            if (gTts == null) {

                event.getMessage().addReaction(Emoji.fromUnicode("❌")).queue();
                event.getMessage().addReaction(Emoji.fromUnicode("\uD83D\uDD11")).queue();
                event.getMessage().delete().queueAfter(5, TimeUnit.SECONDS, null, error -> {});
                return;
            }

            gTts.synthesize(messageRaw);
            event.getMessage().addReaction(Emoji.fromUnicode("\uD83C\uDFA4")).queue();
            event.getMessage().delete().queueAfter(5, TimeUnit.SECONDS, null, error -> {});
        }

    }
}
