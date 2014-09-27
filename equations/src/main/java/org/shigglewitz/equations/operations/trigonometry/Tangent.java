package org.shigglewitz.equations.operations.trigonometry;

import java.math.BigDecimal;
import java.util.Map;

import org.shigglewitz.equations.Token;
import org.shigglewitz.equations.terms.ConstantAmount;
import org.shigglewitz.equations.terms.Term;

public class Tangent extends TrigonometricOperation {
    @Override
    public Token cloneToken() {
        return new Tangent();
    }

    @Override
    public ConstantAmount operate(Term[] inputs,
            Map<String, BigDecimal> variableValues) {
        return new ConstantAmount(Math.tan(this.convertInput(inputs[0]
                .evaluate(variableValues))));
    }

    @Override
    public String getOperator() {
        return "tan";
    }
}
