package com.kral1k.krabot.game.screen;

import net.dv8tion.jda.api.interactions.components.ActionRow;

public interface Screen {
    String getContent();
    ActionRow[] getActionRows();

}
