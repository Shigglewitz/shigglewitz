package org.shigglewitz.equations.exceptions;

public class InvalidEquationException extends Exception {

    private static final long serialVersionUID = -3649371200438490763L;

    public InvalidEquationException() {
        super();
    }

    public InvalidEquationException(String message) {
        super(message);
    }

    public InvalidEquationException(String message, Exception cause) {
        super(message, cause);
    }

}
