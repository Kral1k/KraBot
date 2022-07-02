package com.kral1k.krabot.command;

import com.kral1k.krabot.Bot;
import com.kral1k.krabot.guild.Guild;
import com.kral1k.krabot.guild.member.Member;
import com.kral1k.krabot.language.Text;
import com.kral1k.krabot.user.User;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.requests.restaction.interactions.ModalCallbackAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Collection;

public class GuildCommandInteraction extends CommandInteraction {
    private final Guild guild;
    private final Member member;
    private final User user;

    public GuildCommandInteraction(Bot bot, SlashCommandInteractionEvent event) {
        super(bot, event);
        this.guild = bot.getGuild(event.getGuild());
        this.member = guild.getMember(event.getMember());
        this.user = member.getUser();
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

    public String getUserAsTag() {
        return user.getAsTag();
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

    public SlashCommandInteractionEvent getEvent() {
        return event;
    }

    public OptionMapping getOption(@Nonnull String name) {
        return event.getOption(name);
    }

    public ReplyCallbackAction reply(@NotNull String content) {
        return event.reply(content);
    }

    public ReplyCallbackAction replayTranslatable(String key, Object... objects) {
        return event.reply(Text.translatable(key, objects));
    }

    public ReplyCallbackAction replyEmbeds(@Nonnull MessageEmbed embed, @Nonnull MessageEmbed... embeds) {
        return event.replyEmbeds(embed, embeds);
    }

    public ReplyCallbackAction replyEmbeds(@Nonnull Collection<? extends MessageEmbed> embeds) {
        return event.replyEmbeds(embeds);
    }

    public ReplyCallbackAction deferReply() {
        return event.deferReply();
    }

    public ReplyCallbackAction deferReply(boolean ephemeral) {
        return event.deferReply(ephemeral);
    }

    public InteractionHook getHook() {
        return event.getHook();
    }

    public ModalCallbackAction replyModal(@Nonnull Modal modal) {
        return event.replyModal(modal);
    }

    public boolean isAcknowledged() {
        return event.isAcknowledged();
    }

    @Override
    public CommandSource getSource() {
        return member;
    }
}
