package com.kral1k.krabot.buttons;

import com.google.gson.Gson;
import com.kral1k.krabot.button.ButtonData;
import com.kral1k.krabot.button.ButtonDispatcher;
import com.kral1k.krabot.button.GuildButtonInteraction;
import com.kral1k.krabot.permission.Perms;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

public class TestButton {
    public static void register(ButtonDispatcher<GuildButtonInteraction> dispatcher) {
        dispatcher.register("test", interaction -> {
            TestButtonData data = TestButtonData.from(interaction.getComponentId());
            interaction.getEvent().editMessage(data.amount + "").queue();
        });
    }

    public static Button create(String label, ButtonStyle style, Perms permission, int amount) {
        return Button.of(style, new Gson().toJson(new TestButtonData("test", permission, amount)), label);
    }

    private static class TestButtonData extends ButtonData {
        public final int amount;

        public TestButtonData(String id, Perms permission, int amount) {
            super(id, permission);
            this.amount = amount;
        }

        public static TestButtonData from(String s) {
            return GSON.fromJson(s, TestButtonData.class);
        }
    }
}
