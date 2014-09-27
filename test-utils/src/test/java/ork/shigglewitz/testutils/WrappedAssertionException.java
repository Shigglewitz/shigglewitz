package ork.shigglewitz.testutils;

public class WrappedAssertionException extends Exception {
    private static final long serialVersionUID = -5002027771508906755L;

    public WrappedAssertionException(Error e) {
        super(e);
    }
}
