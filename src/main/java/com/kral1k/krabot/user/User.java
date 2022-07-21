package com.kral1k.krabot.user;

import com.kral1k.krabot.Bot;
import com.kral1k.krabot.button.Source;
import com.kral1k.krabot.guild.member.MemberData;
import com.kral1k.krabot.utils.jsonconfig.JsonConfigSaver;

public class User extends Source {
    private final Bot bot;
    private net.dv8tion.jda.api.entities.User jdaUser;
    private final JsonConfigSaver<UserData> dataSaver;

    public User(Bot bot, net.dv8tion.jda.api.entities.User jdaUser, JsonConfigSaver<UserData> dataSaver) {
        this.bot = bot;
        this.jdaUser = jdaUser;
        this.dataSaver = dataSaver;
    }

    public Bot getBot() {
        return bot;
    }

    public void update(net.dv8tion.jda.api.entities.User jdaUser) {
        this.jdaUser = jdaUser;
    }

    public net.dv8tion.jda.api.entities.User getJdaUser() {
        return jdaUser;
    }

    public JsonConfigSaver<UserData> getDataSaver() {
        return dataSaver;
    }

    public void saveData() {
        dataSaver.save();
    }

    public UserData getData() {
        return dataSaver.get();
    }

    public String getId() {
        return jdaUser.getId();
    }

    public String getName() {
        return jdaUser.getName();
    }

    public String getAsTag() {
        return jdaUser.getAsTag();
    }

    @Override
    public boolean has(String userId) {
        return jdaUser.getId().equals(userId);
    }
}
