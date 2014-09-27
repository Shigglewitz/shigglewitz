package org.shigglewitz.equations.exceptions;

public class EvaluationException extends RuntimeException {

    private static final long serialVersionUID = 1325734181470986033L;

    public EvaluationException(Exception e) {
        super(e);
    }

    public EvaluationException(String message) {
        super(message);
    }

    public EvaluationException(String message, Exception e) {
        super(message, e);
    }

}
