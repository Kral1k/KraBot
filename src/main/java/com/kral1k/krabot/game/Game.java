package com.kral1k.krabot.game;

import net.dv8tion.jda.api.entities.Message;

public interface Game {
    void start(Message message);
    void stop();
}
