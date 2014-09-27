package org.shigglewitz.equations.operations;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.shigglewitz.equations.Token;
import org.shigglewitz.equations.terms.ConstantAmount;
import org.shigglewitz.equations.terms.Term;

public abstract class Operation implements Token {
    private static final Operation[] SUPPORTED_OPERATIONS = { new Addition(),
            new Subtraction(), new Multiplication(), new Division(),
            new Exponent() };
    private static Map<String, Class<? extends Operation>> OPERATION_MAP;
    public static final String OPERATOR_REGEX = "[\\^*/+-]";

    public Operation() {
    };

    public enum Associativity {
        LEFT, RIGHT
    }

    public enum Precedence implements Comparable<Precedence> {
        ADDITION_SUBTRACTION(2), MULTIPLY_DIVIDE(3), EXPONENT(4), PARENTHESES(5), NEGATE(
                6);
        private int precedence;

        private Precedence(int precedence) {
            this.precedence = precedence;
        }

        public int getPrecedence() {
            return this.precedence;
        }
    }

    public abstract ConstantAmount operate(Term[] inputs,
            Map<String, BigDecimal> variableValues);

    public abstract String getOperator();

    public abstract Precedence getPrecedence();

    public abstract Associativity getAssociativity();

    public abstract int getNumberOfInputs();

    public static boolean isOperator(String input) {
        return input.matches(OPERATOR_REGEX);
    }

    public static Class<? extends Operation> determineOperation(String operator) {
        synchronized (Operation.class) {
            if (OPERATION_MAP == null) {
                OPERATION_MAP = initMap();
            }
        }

        return OPERATION_MAP.get(operator);
    }

    private static Map<String, Class<? extends Operation>> initMap() {

        Map<String, Class<? extends Operation>> ret = new HashMap<String, Class<? extends Operation>>();
        for (Operation o : SUPPORTED_OPERATIONS) {
            ret.put(o.getOperator(), o.getClass());
        }
        return ret;
    }
}
