package com.kral1k.krabot.game.games.puzzle.editor;

import com.kral1k.krabot.game.games.puzzle.editor.buttons.AddAnswerBuilderButton;
import com.kral1k.krabot.game.games.puzzle.editor.buttons.CancelBuilderButton;
import com.kral1k.krabot.game.games.puzzle.editor.buttons.EditAnswerBuilderButton;
import com.kral1k.krabot.game.games.puzzle.editor.buttons.SaveAnswerBuilderButton;
import com.kral1k.krabot.guild.member.Member;
import com.kral1k.krabot.permission.Perms;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PuzzlePackBuilderManager {

    private static final Map<String, PuzzlePackBuilderManager> managerHashMap = new HashMap<>();

    public static PuzzlePackBuilderManager getManager(String memberId) {
        return managerHashMap.get(memberId);
    }

    public static void hasManager(String memberId, PuzzlePackBuilderManager builder) {
        managerHashMap.put(memberId, builder);
    }
    public static boolean contains(String memberId) {
        return managerHashMap.containsKey(memberId);
    }

    public static PuzzlePackBuilderManager unHashedManager(String memberId) {
        return managerHashMap.remove(memberId);
    }

    public final Member owner;
    private final PuzzlePackRiddleBuilder builder;
    private int modifyId = -1;

    public PuzzlePackBuilderManager(PuzzlePackRiddleBuilder builder, Member owner) {
        this.builder = builder;
        this.owner = owner;
    }

    public PuzzlePackRiddleBuilder getBuilder() {
        return builder;
    }

    public void setModifyId(int modifyId) {
        this.modifyId = modifyId;
    }

    public int getModifyId() {
        return modifyId;
    }

    public List<ActionRow> buildAction() {
        List<ActionRow> actionRows = new ArrayList<>();
        Perms perms = new Perms(owner.getId());
        List<Button> buttons = new ArrayList<>();
        for (int i = 0; i < builder.answerSize(); i++) {
            buttons.add(EditAnswerBuilderButton.create(builder.getAnswer(i).text, ButtonStyle.PRIMARY, perms, i));
        }
        if (builder.answerSize() < 5) buttons.add(AddAnswerBuilderButton.create("add", ButtonStyle.SECONDARY, perms));
        actionRows.add(ActionRow.of(buttons));
        List<Button> buttons2 = new ArrayList<>();
        buttons2.add(CancelBuilderButton.create("cancel", ButtonStyle.DANGER, perms));
        if (builder.answerSize() >= 2) buttons2.add(SaveAnswerBuilderButton.create("save", ButtonStyle.SUCCESS, perms));
        actionRows.add(ActionRow.of(buttons2));
        return actionRows;
    }
}
