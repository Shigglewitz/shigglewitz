package org.shigglewitz.equations.operations;

import java.math.BigDecimal;
import java.util.Map;

import org.shigglewitz.equations.Token;
import org.shigglewitz.equations.terms.ConstantAmount;
import org.shigglewitz.equations.terms.Term;

public class Multiplication extends Operation {
    public static final String OPERATOR = "*";

    public Multiplication() {
    }

    @Override
    public String getOperator() {
        return OPERATOR;
    }

    @Override
    public ConstantAmount operate(Term[] inputs,
            Map<String, BigDecimal> variableValues) {
        return new ConstantAmount(inputs[0].evaluate(variableValues)
                * inputs[1].evaluate(variableValues));
    }

    @Override
    public Precedence getPrecedence() {
        return Precedence.MULTIPLY_DIVIDE;
    }

    @Override
    public Associativity getAssociativity() {
        return Associativity.LEFT;
    }

    @Override
    public int getNumberOfInputs() {
        return 2;
    }

    @Override
    public Token cloneToken() {
        return new Multiplication();
    }

}
