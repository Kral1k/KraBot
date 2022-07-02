package com.kral1k.krabot.button;

public class ButtonException extends Throwable {

    public ButtonException() {
    }

    public ButtonException(String message) {
        super(message);
    }

    public ButtonException(String message, Throwable cause) {
        super(message, cause);
    }

    public ButtonException(Throwable cause) {
        super(cause);
    }
}
