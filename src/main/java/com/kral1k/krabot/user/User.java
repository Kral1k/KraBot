package com.kral1k.krabot.user;

import com.kral1k.krabot.Bot;
import com.kral1k.krabot.command.CommandSource;

public class User extends CommandSource {
    private final Bot bot;
    private net.dv8tion.jda.api.entities.User jdaUser;
    private final UserData data;

    public User(Bot bot, net.dv8tion.jda.api.entities.User jdaUser, UserData data) {
        this.bot = bot;
        this.jdaUser = jdaUser;
        this.data = data;
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

    public UserData getData() {
        return data;
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
}
