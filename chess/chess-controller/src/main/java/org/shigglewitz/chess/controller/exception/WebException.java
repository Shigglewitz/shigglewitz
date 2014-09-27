package org.shigglewitz.chess.controller.exception;

public class WebException extends RuntimeException {
    private static final long serialVersionUID = 6744917558917414976L;

    public WebException(String string) {
        super(string);
    }
}
