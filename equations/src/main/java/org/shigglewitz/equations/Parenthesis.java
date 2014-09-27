package org.shigglewitz.equations;

import org.shigglewitz.equations.exceptions.InvalidParenthesisException;

public class Parenthesis implements Token {
    private final boolean isLeft;
    private static final String LEFT_PAREN_REGEX = "[(]";
    private static final String RIGHT_PAREN_REGEX = "[)]";
    public static final String ALL_PARENS_REGEX = "[()]";

    public Parenthesis(String input) {
        if (input.matches(LEFT_PAREN_REGEX)) {
            this.isLeft = true;
        } else if (input.matches(RIGHT_PAREN_REGEX)) {
            this.isLeft = false;
        } else {
            throw new InvalidParenthesisException(input
                    + " is not a valid parenthesis.");
        }
    }

    public boolean isLeft() {
        return this.isLeft;
    }

    @Override
    public Token cloneToken() {
        return null;
    }

}
