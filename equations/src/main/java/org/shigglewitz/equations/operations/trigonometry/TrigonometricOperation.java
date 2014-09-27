package org.shigglewitz.equations.operations.trigonometry;

import java.util.HashMap;
import java.util.Map;

import org.shigglewitz.equations.operations.Operation;

public abstract class TrigonometricOperation extends Operation {

    public static final String OPERATOR_REGEX = "(sin|tan|cos)";
    private static Map<String, Class<? extends Operation>> OPERATION_MAP;
    private static final Operation[] SUPPORTED_OPERATIONS = { new Sine(),
            new Tangent(), new Cosine() };

    public enum ANGLE_UNITS {
        RADIANS, DEGREES
    }

    private static final ANGLE_UNITS MODE = ANGLE_UNITS.DEGREES;

    private static final double DEG_TO_RAD = Math.PI / 180;

    public static double degreesToRadians(double degrees) {
        return degrees * DEG_TO_RAD;
    }

    protected double convertInput(double input) {
        switch (MODE) {
        case DEGREES:
            return degreesToRadians(input);
        case RADIANS:
        default:
            return input;

        }
    }

    @Override
    public Precedence getPrecedence() {
        return Precedence.PARENTHESES;
    }

    @Override
    public Associativity getAssociativity() {
        return null;
    }

    @Override
    public int getNumberOfInputs() {
        return 1;
    }

    public static Class<? extends Operation> determineOperation(String operator) {
        synchronized (TrigonometricOperation.class) {
            if (OPERATION_MAP == null) {
                OPERATION_MAP = initTrigonometricMap();
            }
        }

        return OPERATION_MAP.get(operator);
    }

    private static Map<String, Class<? extends Operation>> initTrigonometricMap() {

        Map<String, Class<? extends Operation>> ret = new HashMap<String, Class<? extends Operation>>();
        for (Operation o : SUPPORTED_OPERATIONS) {
            ret.put(o.getOperator(), o.getClass());
        }
        return ret;
    }
}
