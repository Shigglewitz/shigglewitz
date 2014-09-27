package org.shigglewitz.equations.exceptions;

public class InsufficientVariableInformationException extends RuntimeException {

    private static final long serialVersionUID = 2624232956195618460L;

    public InsufficientVariableInformationException() {
        super();
    }

    public InsufficientVariableInformationException(String message) {
        super(message);
    }
}
