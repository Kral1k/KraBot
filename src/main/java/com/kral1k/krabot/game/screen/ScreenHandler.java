package com.kral1k.krabot.game.screen;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.requests.restaction.interactions.MessageEditCallbackAction;

public class ScreenHandler {
    private final Message message;
    private Screen oldScreen;
    private Screen screen;

    public ScreenHandler(Message message) {
        this.message = message;
    }

    public void setScreen(Screen screen) {
        this.oldScreen = this.screen;
        this.screen = screen;
        message.editMessage(screen.getContent()).setActionRows(screen.getActionRows()).queue();
    }

    public void setOldScreen() {
        this.setScreen(oldScreen);
    }

    public void setScreen(MessageEditCallbackAction action, Screen screen) {
        this.oldScreen = this.screen;
        this.screen = screen;
        action.setContent(screen.getContent()).setActionRows(screen.getActionRows()).queue();
    }

    public void setOldScreen(MessageEditCallbackAction action) {
        this.setScreen(action, oldScreen);
    }

    public void setErrorScreen() {
        this.oldScreen = this.screen;
        message.editMessage("ERROR").setActionRows().queue();
    }


    public Screen getOldScreen() {
        return oldScreen;
    }

    public Screen getScreen() {
        return screen;
    }

    public Message getMessage() {
        return message;
    }
}
