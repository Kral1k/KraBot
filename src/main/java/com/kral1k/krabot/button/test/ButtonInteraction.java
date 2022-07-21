package com.kral1k.krabot.button.test;

import com.kral1k.krabot.Bot;
import com.kral1k.krabot.guild.Guild;
import com.kral1k.krabot.guild.member.Member;
import com.kral1k.krabot.language.Text;
import com.kral1k.krabot.user.User;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.requests.restaction.interactions.MessageEditCallbackAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public class ButtonInteraction {
    private final Bot bot;
    private final ButtonInteractionEvent event;
    private final Guild guild;
    private final Member member;
    private final User user;

    public ButtonInteraction(Bot bot, ButtonInteractionEvent event) {
        this.bot = bot;
        this.event = event;
        if (event.isFromGuild()) {
            this.guild = bot.getGuild(event.getGuild());
            this.member = guild.getMember(event.getMember());
            this.user = member.getUser();
        } else {
            this.guild = null;
            this.member = null;
            this.user = bot.getUser(event.getUser());
        }
    }

    public Bot getBot() {
        return bot;
    }

    public ButtonInteractionEvent getEvent() {
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

    public String getButtonId() {
        return event.getComponentId();
    }

    @Nonnull
    public Message getMessage() {
        return event.getMessage();
    }

    @Nonnull
    @CheckReturnValue
    public ReplyCallbackAction reply(@Nonnull String content) {
        return event.reply(content);
    }

    @Nonnull
    public MessageEditCallbackAction deferEdit() {
        return event.deferEdit();
    }

    @Nonnull
    @CheckReturnValue
    public ReplyCallbackAction replyTranslatable(@Nonnull String key) {
        return event.reply(Text.translatable(key));
    }

    public boolean isAcknowledged() {
        return event.isAcknowledged();
    }

    @Nonnull
    public InteractionHook getHook() {
        return event.getHook();
    }
}
