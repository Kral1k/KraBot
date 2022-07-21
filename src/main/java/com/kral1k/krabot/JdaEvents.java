package com.kral1k.krabot;

import com.kral1k.krabot.button.ButtonData;
import com.kral1k.krabot.button.GuildButtonInteraction;
import com.kral1k.krabot.button.test.ButtonInteraction;
import com.kral1k.krabot.button.test.ButtonManager;
import com.kral1k.krabot.command.CommandManager;
import com.kral1k.krabot.command.GuildCommandInteraction;
import com.kral1k.krabot.guild.Guild;
import com.kral1k.krabot.guild.member.Member;
import com.kral1k.krabot.language.Text;
import com.kral1k.krabot.modal.GuildModalInteraction;
import com.kral1k.krabot.modal.ModalManager;
import com.kral1k.krabot.player.tts.GoogleTextToSpeech;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;

public class JdaEvents extends ListenerAdapter {
    private static final Logger LOGGER = JDALogger.getLog(Bot.class);
    private final Bot bot;
    private final CommandManager commandManager;
    private final com.kral1k.krabot.button.ButtonManager buttonManager;
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
        if (event.getComponentId().startsWith("{")) {
            if (event.isFromGuild()) {
                ButtonData data = ButtonData.from(event.getComponentId());
                GuildButtonInteraction interaction = new GuildButtonInteraction(bot, event);
                if (!data.perms.has(interaction.getMember())) event.reply(Text.translatable("button.permission")).setEphemeral(true).queue();
                else buttonManager.execute(data.id, interaction);
            }
        } else ButtonManager.onPress(new ButtonInteraction(bot, event));
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if (event.isFromGuild()) {
            modalManager.execute(new GuildModalInteraction(bot, event));
        }
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        bot.getGuildCache().load(bot, event.getGuild());
    }

    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {
        bot.getGuildCache().remove(event.getGuild().getId());
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        Guild guild = bot.getGuild(event.getGuild());
        guild.getMemberCache().remove(event.getMember().getId());
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

        if (event.getChannel().getId().equals(guild.getData().gTtsChannelId)) {

            AudioChannel audioChannel = member.getVoiceChannel();
            if(audioChannel == null) {
                event.getMessage().addReaction(Emoji.fromUnicode("❓")).queue();
                return;
            }
            guild.getAudioManager().openAudioConnection(audioChannel);

            event.getGuild().getAudioManager().openAudioConnection(audioChannel);
            String messageRaw = event.getMessage().getContentRaw();
            GoogleTextToSpeech gTts = guild.getGTts();
            if (gTts == null) {

                event.getMessage().addReaction(Emoji.fromUnicode("❌")).queue();
                event.getMessage().addReaction(Emoji.fromUnicode("\uD83D\uDD11")).queue();
                return;
            }

            gTts.synthesize(messageRaw);
            event.getMessage().addReaction(Emoji.fromUnicode("\uD83C\uDFA4")).queue();
        }

    }
}
