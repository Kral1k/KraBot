package com.kral1k.krabot.guild.member;

import com.kral1k.krabot.Bot;
import com.kral1k.krabot.button.Source;
import com.kral1k.krabot.guild.Guild;
import com.kral1k.krabot.permission.PermissionRole;
import com.kral1k.krabot.user.User;
import com.kral1k.krabot.utils.jsonconfig.JsonConfigSaver;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Role;

import java.util.List;

public class Member extends Source {
    private final Bot bot;
    private final Guild guild;
    private net.dv8tion.jda.api.entities.Member jdaMember;
    private final JsonConfigSaver<MemberData> dataSaver;
    private final User user;

    public Member(Guild guild, net.dv8tion.jda.api.entities.Member jdaMember, JsonConfigSaver<MemberData> dataSaver) {
        this.bot = guild.getBot();
        this.guild = guild;
        this.jdaMember = jdaMember;
        this.dataSaver = dataSaver;
        this.user = bot.getUser(jdaMember.getUser());
    }

    public Bot getBot() {
        return bot;
    }

    public void update(net.dv8tion.jda.api.entities.Member jdaMember) {
        this.jdaMember = jdaMember;
        user.update(jdaMember.getUser());
    }

    public Guild getGuild() {
        return guild;
    }

    public net.dv8tion.jda.api.entities.Member getJdaMember() {
        return jdaMember;
    }

    public JsonConfigSaver<MemberData> getDataSaver() {
        return dataSaver;
    }

    public void saveData() {
        dataSaver.save();
    }

    public MemberData getData() {
        return dataSaver.get();
    }

    public double getBalance() {
        return this.getData().kraCoin;
    }

    public void addBalance(double balance) {
        this.getData().kraCoin += balance;
        dataSaver.save();
    }

    public void setBalance(double balance) {
        this.getData().kraCoin = balance;
        dataSaver.save();
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

    public String getAsMention() {
        return jdaMember.getAsMention();
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
        String roleId = guild.getPermissionRoleId(permissionRole);
        return this.getRoles().stream().anyMatch(role -> role.getId().equals(roleId));
    }

    @Override
    public boolean has(String userId) {
        return this.getId().equals(userId);
    }
}
