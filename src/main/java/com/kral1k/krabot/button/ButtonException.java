package com.kral1k.krabot.button;

import com.kral1k.krabot.utils.ExecutionException;

public class ButtonException extends ExecutionException {

    public ButtonException() {}
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
