package com.kral1k.krabot.modal;

import com.kral1k.krabot.Bot;
import com.kral1k.krabot.guild.Guild;
import com.kral1k.krabot.guild.member.Member;
import com.kral1k.krabot.user.User;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import net.dv8tion.jda.api.requests.restaction.interactions.MessageEditCallbackAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

public class GuildModalInteraction {
    private final Bot bot;
    private final ModalInteractionEvent event;
    private final Guild guild;
    private final Member member;
    private final User user;

    public GuildModalInteraction(Bot bot, ModalInteractionEvent event) {
        this.bot = bot;
        this.event = event;
        this.guild = bot.getGuild(event.getGuild());
        this.member = guild.getMember(event.getMember());
        this.user = member.getUser();
    }

    public Bot getBot() {
        return bot;
    }

    public ModalInteractionEvent getEvent() {
        return event;
    }

    public Guild getGuild() {
        return guild;
    }

    public Member getMember() {
        return member;
    }

    public User getUser() {
        return user;
    }

    public String getMemberId() {
        return member.getId();
    }

    public String getModalId() {
        return event.getModalId();
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

    public ReplyCallbackAction reply(String message) {
        return event.reply(message);
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

    public ModalMapping getValue(String id) {
        return event.getValue(id);
    }

    public List<ModalMapping> getValues() {
        return event.getValues();
    }

    public MessageEditCallbackAction editMessageEmbeds(MessageEmbed... embeds) {
        return event.editMessageEmbeds(embeds);
    }

    public MessageEditCallbackAction editMessageEmbeds(@Nonnull Collection<? extends MessageEmbed> embeds) {
        return event.editMessageEmbeds(embeds);
    }
}
