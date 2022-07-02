package com.kral1k.krabot.guild.member;

import com.kral1k.krabot.Bot;
import com.kral1k.krabot.command.CommandSource;
import com.kral1k.krabot.guild.Guild;
import com.kral1k.krabot.permission.PermissionRole;
import com.kral1k.krabot.user.User;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Role;

import java.util.List;

public class Member extends CommandSource {
    private final Bot bot;
    private net.dv8tion.jda.api.entities.Member jdaMember;

    private final MemberData data;
    private final User user;

    public Member(Guild guild, net.dv8tion.jda.api.entities.Member jdaMember, MemberData data) {
        this.bot = guild.getBot();
        this.jdaMember = jdaMember;
        this.data = data;
        this.user = bot.getUser(jdaMember.getUser());
    }

    public Bot getBot() {
        return bot;
    }

    public void update(net.dv8tion.jda.api.entities.Member jdaMember) {
        this.jdaMember = jdaMember;
        user.update(jdaMember.getUser());
    }

    public net.dv8tion.jda.api.entities.Member getJdaMember() {
        return jdaMember;
    }

    public MemberData getData() {
        return data;
    }

    public double getBalance() {
        return data.kraCoin;
    }

    public void addBalance(double balance) {
        data.kraCoin += balance;
        data.serialize();
    }

    public void setBalance(double newBalance) {
        data.kraCoin = newBalance;
        data.serialize();
    }

    public User getUser() {
        return user;
    }

    public net.dv8tion.jda.api.entities.User getJdaUser() {
        return jdaMember.getUser();
    }

    public String getId() {
        return jdaMember.getId();
    }

    public String getUserAsTag() {
        return user.getAsTag();
    }

    public List<Role> getRoles() {
        return jdaMember.getRoles();
    }

    public GuildVoiceState getVoiceState() {
        return jdaMember.getVoiceState();
    }

    public AudioChannel getVoiceChannel() {
        return this.getVoiceState().getChannel();
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return jdaMember.hasPermission(permission);
    }

    @Override
    public boolean hasPermission(PermissionRole permissionRole) {
        return this.getRoles().stream().anyMatch(role -> role.getId().equals(permissionRole.getId()));
    }
}
