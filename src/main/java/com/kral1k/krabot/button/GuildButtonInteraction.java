package com.kral1k.krabot.button;

import com.kral1k.krabot.Bot;
import com.kral1k.krabot.guild.Guild;
import com.kral1k.krabot.guild.member.Member;
import com.kral1k.krabot.language.Text;
import com.kral1k.krabot.user.User;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.requests.restaction.interactions.MessageEditCallbackAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ModalCallbackAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

import javax.annotation.Nonnull;
import java.util.Collection;

public class GuildButtonInteraction {
    private final Bot bot;
    private final ButtonInteractionEvent event;
    private final Guild guild;
    private final Member member;
    private final User user;

    public GuildButtonInteraction(Bot bot, ButtonInteractionEvent event) {
        this.bot = bot;
        this.event = event;
        this.guild = bot.getGuild(event.getGuild());
        this.member = guild.getMember(event.getMember());
        this.user = member.getUser();
    }

    public Bot getBot() {
        return bot;
    }

    public User getUser() {
        return user;
    }

    public Guild getGuild() {
        return guild;
    }

    public Member getMember() {
        return member;
    }

    public String getMemberId() {
        return member.getId();
    }

    public net.dv8tion.jda.api.entities.User getJdaUser() {
        return event.getUser();
    }

    public net.dv8tion.jda.api.entities.Guild getJdaGuild() {
        return event.getGuild();
    }

    public net.dv8tion.jda.api.entities.Member getJdaMember() {
        return event.getMember();
    }

    public ButtonInteractionEvent getEvent() {
        return event;
    }

    public String getComponentId() {
        return event.getComponentId();
    }

    public MessageChannel getChannel() {
        return event.getChannel();
    }

    public long getMessageIdLong() {
        return event.getMessageIdLong();
    }

    public Message getMessage() {
        return event.getMessage();
    }

    public boolean isAcknowledged() {
        return event.isAcknowledged();
    }

    public ReplyCallbackAction reply(String message) {
        return event.reply(message);
    }

    public ReplyCallbackAction replayTranslatable(String key, Object... objects) {
        return event.reply(Text.translatable(key, objects));
    }

    public ModalCallbackAction replyModal(Modal modal) {
        return event.replyModal(modal);
    }

    public ReplyCallbackAction deferReply() {
        return event.deferReply();
    }

    public ReplyCallbackAction deferReply(boolean ephemeral) {
        return event.deferReply(ephemeral);
    }

    public MessageEditCallbackAction deferEdit() {
        return event.deferEdit();
    }

    public InteractionHook getHook() {
        return event.getHook();
    }


    public MessageEditCallbackAction editMessageEmbeds(MessageEmbed... embeds) {
        return event.editMessageEmbeds(embeds);
    }

    public MessageEditCallbackAction editMessageEmbeds(@Nonnull Collection<? extends MessageEmbed> embeds) {
        return event.editMessageEmbeds(embeds);
    }

}
