package org.shigglewitz.equations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.shigglewitz.utils.Utils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.shigglewitz.equations.Equation;
import org.shigglewitz.equations.exceptions.InsufficientVariableInformationException;
import org.shigglewitz.equations.exceptions.InvalidEquationException;
import org.shigglewitz.equations.exceptions.InvalidParenthesisException;
import org.shigglewitz.equations.operations.trigonometry.TrigonometricOperation;

public class EquationTest {
    private static final double DELTA = 0.0000001;
    private static final Map<String, BigDecimal> STANDARD_VARS = new HashMap<>();

    @BeforeClass
    public static void before() {
        char var;
        for (int i = 0; i < 26; i++) {
            var = (char) ('A' + i);
            STANDARD_VARS.put(Character.toString(var), new BigDecimal(i + 1));
        }
    }

    @Test
    public void testInvalidEquations() {
        String[] input = { "", " ", null, "+", "+1", "1*2*8*" };

        for (String s : input) {
            this.testEquationValidity(s, false);
        }
    }

    @Test
    public void testValidEquations() {
        String[] input = { "1+2", "3 * 4 / 5" };

        for (String s : input) {
            this.testEquationValidity(s, true);
        }
    }

    @Test
    public void testInvalidEquationsWithParens() {
        String[] input = { "1+(2", "(1*3)) * (4 / 5)" };

        for (String s : input) {
            this.testEquationValidity(s, false);
        }
    }

    @Test
    public void testValidEquationsWithParens() {
        String[] input = { "(1+2)", "(1+(3*4)) " };

        for (String s : input) {
            this.testEquationValidity(s, true);
        }
    }

    @Test
    public void testValidDecimals() {
        String[] input = { "1.0004 * 0.01", "0.1(5.6)/4.3" };

        for (String s : input) {
            this.testEquationValidity(s, true);
        }
    }

    @Test
    public void testInvalidDecimals() {
        String[] input = { ".3*4.5", "0.567 + 9.", "0.45 * 78..12", "1+2+3.4.5" };

        for (String s : input) {
            this.testEquationValidity(s, false);
        }
    }

    @Test
    public void testValidVariables() {
        String[] input = { "1+2+X", "((4*5)^X-1)", "X-Y-Z+T^F", "1(2.05X)(Y)",
                "XYZ", "20XY", "2(X)+1", "X(2*Y)Z" };

        for (String s : input) {
            this.testEquationValidity(s, true);
        }
    }

    @Test
    public void testInvalidVariables() {
        String[] input = { "2xY" };

        for (String s : input) {
            this.testEquationValidity(s, false);
        }
    }

    @Test
    public void testImplicitMultiplication() {
        String[] tests = { "1+2(3*4)7+8", "(1)30+45+67(89)(56)(12)" };
        String[] expected = { "1+2*(3*4)*7+8", "(1)*30+45+67*(89)*(56)*(12)" };

        for (int i = 0; i < tests.length; i++) {
            assertEquals(expected[i],
                    Equation.addImpliedMultiplication(tests[i]));
        }
    }

    @Test
    public void testOriginalEquation() throws InvalidEquationException {
        String[] equation = { "1(2) B  (C)", "4^5(67   )" };
        for (String e : equation) {
            assertEquals(e, new Equation(e).getOriginalEquation());
        }
    }

    @Test
    public void testInsufficientVariables() throws InvalidEquationException {
        String[] tests = { "X+4+Y+Z*45", "(A)*B^((C)+D)" };
        Map<String, BigDecimal> vars = new HashMap<>();
        String baseMessage = "Missing variable value for ";
        String[] expectedMessages = { baseMessage + "Z", baseMessage + "D" };

        for (int i = 0; i < tests.length; i++) {
            try {
                new Equation(tests[i]).evaluate(vars);
                assertTrue("Exception not thrown for " + tests[i], false);
            } catch (InsufficientVariableInformationException e) {
                assertEquals(expectedMessages[i], e.getMessage());
            }
        }
    }

    @Test
    public void testConstants() throws InvalidEquationException {
        String[] input = { "1", "20", "5", "4", "(40)", "-5" };
        double[] expected = { 1, 20, 5, 4, 40, -5 };
        for (int i = 0; i < input.length; i++) {
            this.testEquation(input[i], expected[i]);
        }
    }

    @Test
    public void testSimpleEquations() throws InvalidEquationException {
        String[] input = { "1+1", "1*3", "3/5", "0-4", "5^0", "5^1", "3^3",
                "-1-2" };
        double[] output = { 2, 3, 0.6, -4, 1, 5, 27, -3 };

        for (int i = 0; i < input.length; i++) {
            this.testEquation(input[i], output[i]);
        }
    }

    @Test
    public void testLongEquations() throws InvalidEquationException {
        // none of these tests should test the order of operations
        String[] input = { "1 + 1  + 1 +  1", "1 * 3 * 2 * 4 ", "3 * 2 + 1",
                "2^3*2+1" };
        double[] output = { 4, 24, 7, 17 };

        for (int i = 0; i < input.length; i++) {
            this.testEquation(input[i], output[i]);
        }
    }

    @Test
    public void testOrderOfOperations() throws InvalidEquationException {
        String[] input = { "1+2*3", "1+3^3+1*4", "0/4+1*3",
                "1*30+45+67*89*56*12", "1*3+-4" };
        double[] output = { 7, 32, 3, 4007211, -1 };

        this.testEquation(input[4], output[4]);

        for (int i = 0; i < input.length; i++) {
            this.testEquation(input[i], output[i]);
        }
    }

    @Test
    public void testEquationsWithParens() throws InvalidEquationException {
        String[] input = { "(20)*(19)", "(1+2)*3", "1+3^(3+1)*4", "0/(4+1)3",
                "1*30+(45+67)89(56)*12", "1+((2*(5+2)))" };
        double[] output = { 380, 9, 325, 0, 6698526, 15, };

        for (int i = 0; i < input.length; i++) {
            this.testEquation(input[i], output[i]);
        }
    }

    @Test
    public void testEquationsWithVariables() throws InvalidEquationException {
        String[] tests = { "X+Y", "B^C", "B*B", "B(C)/(D+E)", "X^2" };
        double[] expected = { 49, 8, 4, 2.0 / 3.0, 576 };

        for (int i = 0; i < tests.length; i++) {
            this.testEquation(tests[i], expected[i], STANDARD_VARS);
        }
    }

    @Test
    public void testValidationForGeometricEquations() {
        String[] tests = { "sin", "cos", "tan", "sicotan" };
        boolean[] expected = { true, true, true, false };

        for (int i = 0; i < tests.length; i++) {
            assertEquals("Unexpected (in)validity for " + tests[i],
                    tests[i].matches(TrigonometricOperation.OPERATOR_REGEX),
                    expected[i]);
        }
    }

    @Test
    public void testGeometricEquations() throws InvalidEquationException {
        String[] tests = { "sin(0)", "cos(0)", "tan(0)", "sin(X)", "cos(X)",
                "tan(X)" };
        double[] expected = { 0, 1, 0, 0.4067366, 0.9135454, 0.4452286 };

        for (int i = 0; i < tests.length; i++) {
            this.testEquation(tests[i], expected[i], STANDARD_VARS);
        }
    }

    private void testEquationValidity(String equation, boolean valid) {
        equation = Utils.removeAllWhiteSpace(equation);
        equation = Equation.addImpliedMultiplication(equation);
        try {
            new Equation(equation);
            assertTrue(equation + " should have been invalid.", valid);
        } catch (InvalidEquationException | InvalidParenthesisException e) {
            assertFalse(equation + " should have been valid.", valid);
        }
    }

    private void testEquation(String equation, double expected)
            throws InvalidEquationException {
        this.testEquation(equation, expected, null);
    }

    private void testEquation(String equation, double expected,
            Map<String, BigDecimal> vars) throws InvalidEquationException {
        assertEquals("Equation " + equation + " did not evaluate to "
                + expected, expected, new Equation(equation).evaluate(vars),
                DELTA);
    }
}
