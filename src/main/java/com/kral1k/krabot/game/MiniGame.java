package com.kral1k.krabot.game;

import com.kral1k.krabot.guild.member.Member;
import net.dv8tion.jda.api.entities.Message;

public interface MiniGame {
    void start(Message message, Member owner);

    void stop();
}
