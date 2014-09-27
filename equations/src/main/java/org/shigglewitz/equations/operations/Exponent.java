package org.shigglewitz.equations.operations;

import java.math.BigDecimal;
import java.util.Map;

import org.shigglewitz.equations.Token;
import org.shigglewitz.equations.terms.ConstantAmount;
import org.shigglewitz.equations.terms.Term;

public class Exponent extends Operation {
    public Exponent() {
    }

    @Override
    public String getOperator() {
        return "^";
    }

    @Override
    public ConstantAmount operate(Term[] inputs,
            Map<String, BigDecimal> variableValues) {
        return new ConstantAmount(Math.pow(inputs[1].evaluate(variableValues),
                inputs[0].evaluate(variableValues)));
    }

    @Override
    public Precedence getPrecedence() {
        return Precedence.EXPONENT;
    }

    @Override
    public Associativity getAssociativity() {
        return Associativity.RIGHT;
    }

    @Override
    public int getNumberOfInputs() {
        return 2;
    }

    @Override
    public Token cloneToken() {
        return new Exponent();
    }
}
