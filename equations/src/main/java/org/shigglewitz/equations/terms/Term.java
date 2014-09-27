package org.shigglewitz.equations.terms;

import java.math.BigDecimal;
import java.util.Map;

import org.shigglewitz.equations.Token;

public interface Term extends Token {
    public double evaluate(Map<String, BigDecimal> varValues);
}
