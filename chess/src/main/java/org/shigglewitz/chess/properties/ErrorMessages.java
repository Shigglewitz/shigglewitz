package org.shigglewitz.chess.properties;

public class ErrorMessages {
    public static final String WINNER_ALREADY_SET = "Winner has already been set!";
    public static final String NOT_PLAYERS_TURN = "It is not this player's turn";

    public static String formatErrorMessage(String errorMessage, Object... args) {
        return String.format(errorMessage, args);
    }
}
