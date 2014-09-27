package org.shigglewitz.equations.terms;

import java.math.BigDecimal;
import java.util.Map;

import org.shigglewitz.equations.Token;
import org.shigglewitz.equations.exceptions.InsufficientVariableInformationException;

public class Variable implements Term {
    private final String variable;

    private Variable(String variable) {
        this.variable = variable;
    }

    @Override
    public double evaluate(Map<String, BigDecimal> varValues) {
        if (varValues.get(this.variable) == null) {
            throw new InsufficientVariableInformationException(
                    "Missing variable value for " + this.variable);
        } else {
            return varValues.get(this.variable).doubleValue();
        }
    }

    public static Term getTerm(String amount) {
        return new Variable(amount);
    }

    @Override
    public Token cloneToken() {
        return new Variable(this.variable);
    }

}
