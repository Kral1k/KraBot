package com.kral1k.krabot.button;

import com.kral1k.krabot.language.Text;

public class ButtonNotFoundException extends ButtonException {
    public ButtonNotFoundException() {
        super(Text.translatable("button.notFound"));
    }
}
