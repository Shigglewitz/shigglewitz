package org.shigglewitz.equations;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.shigglewitz.utils.Utils;
import org.shigglewitz.equations.exceptions.EvaluationException;
import org.shigglewitz.equations.exceptions.InvalidEquationException;
import org.shigglewitz.equations.exceptions.InvalidParenthesisException;
import org.shigglewitz.equations.operations.Multiplication;
import org.shigglewitz.equations.operations.Negate;
import org.shigglewitz.equations.operations.Operation;
import org.shigglewitz.equations.operations.Subtraction;
import org.shigglewitz.equations.operations.trigonometry.TrigonometricOperation;
import org.shigglewitz.equations.terms.ConstantAmount;
import org.shigglewitz.equations.terms.Term;
import org.shigglewitz.equations.terms.Variable;

public class Equation {
    public static final String VARIABLE_REGEX = "[A-Z]";
    private static final String NUMBER_REGEX = "-?[0-9]+(\\.[0-9]+)?";
    private static final String IMPLIED_BEFORE_PAREN_REGEX = "([A-Z0-9)]\\()";
    private static final String IMPLIED_AFTER_PAREN_REGEX = "(\\)[(0-9A-Z])";
    private static final String IMPLIED_WITH_VAR_REGEX = "((" + NUMBER_REGEX
            + ")|" + VARIABLE_REGEX + ")(?=" + VARIABLE_REGEX + ")";
    private static final Pattern IMPLIED_BEFORE_PAREN = Pattern
            .compile(IMPLIED_BEFORE_PAREN_REGEX);
    private static final Pattern IMPLIED_AFTER_PAREN = Pattern
            .compile(IMPLIED_AFTER_PAREN_REGEX);
    private static final Pattern IMPLIED_WITH_VAR = Pattern
            .compile(IMPLIED_WITH_VAR_REGEX);

    private static final Map<String, BigDecimal> STANDARD_VARS = new HashMap<>();

    static {
        char var;
        for (int i = 0; i < 26; i++) {
            var = (char) ('A' + i);
            STANDARD_VARS.put(Character.toString(var), new BigDecimal(1));
        }
    }

    private final String originalEquation;
    private final List<Token> tokens;

    public Equation(String input) throws InvalidEquationException {
        if (input == null) {
            throw new InvalidEquationException("Equation cannot be null");
        }
        this.originalEquation = input;
        input = Utils.removeAllWhiteSpace(input);
        if ("".equals(input)) {
            throw new InvalidEquationException("Equation cannot be empty");
        }
        input = addImpliedMultiplication(input);
        this.tokens = djikstraShunt(input);
        this.evaluate(STANDARD_VARS);
    }

    public static List<Token> djikstraShunt(String equation)
            throws InvalidEquationException {
        Pattern p = Pattern.compile(Operation.OPERATOR_REGEX + "|"
                + TrigonometricOperation.OPERATOR_REGEX + "|"
                + Parenthesis.ALL_PARENS_REGEX + "|" + VARIABLE_REGEX + "|("
                + NUMBER_REGEX + ")");
        Matcher m = p.matcher(equation);
        List<Token> inFix = new LinkedList<>();
        String group;
        String previous = null;
        int lastEnd = 0;
        while (m.find()) {
            if (m.start() != lastEnd) {
                throw new InvalidEquationException("Could not parse part of "
                        + equation);
            }
            group = m.group(0);
            if (group.matches(NUMBER_REGEX)) {
                inFix.add(ConstantAmount.getTerm(group));
            } else if (group.matches(VARIABLE_REGEX)) {
                inFix.add(Variable.getTerm(group));
            } else if (group.matches(Parenthesis.ALL_PARENS_REGEX)) {
                inFix.add(new Parenthesis(group));
            } else if (group.matches(TrigonometricOperation.OPERATOR_REGEX)) {
                Constructor<? extends Operation> constructor;
                try {
                    constructor = TrigonometricOperation.determineOperation(
                            group).getConstructor();
                    inFix.add(constructor.newInstance());
                } catch (NoSuchMethodException | SecurityException
                        | InstantiationException | IllegalAccessException
                        | IllegalArgumentException | InvocationTargetException e) {
                    throw new EvaluationException(e);
                }
            } else if (group.equals(Subtraction.OPERATOR)) {
                if (previous == null || previous.matches("[(]")
                        || Operation.isOperator(previous)) {
                    inFix.add(new Negate());
                } else {
                    inFix.add(new Subtraction());
                }
            } else {
                Constructor<? extends Operation> constructor;
                try {
                    constructor = Operation.determineOperation(group)
                            .getConstructor();
                    inFix.add(constructor.newInstance());
                } catch (NoSuchMethodException | SecurityException
                        | InstantiationException | IllegalAccessException
                        | IllegalArgumentException | InvocationTargetException e) {
                    throw new EvaluationException(e);
                }
            }

            previous = group;
            lastEnd = m.end();
        }
        if (lastEnd != equation.length()) {
            throw new InvalidEquationException("Unable to parse all of "
                    + equation);
        }

        Stack<Token> operatorStack = new Stack<>();
        List<Token> ret = new ArrayList<>();

        Operation o;
        for (Token t : inFix) {
            if (t instanceof Term) {
                ret.add(t);
            } else if (t instanceof Operation) {
                o = (Operation) t;
                while (operatorStack.size() > 0
                        && operatorStack.peek() instanceof Operation
                        && ((Operation) operatorStack.peek()).getPrecedence()
                                .compareTo(o.getPrecedence()) > 0) {
                    ret.add(operatorStack.pop());
                }
                operatorStack.push(o);
            } else if (t instanceof Parenthesis) {
                if (((Parenthesis) t).isLeft()) {
                    operatorStack.push(t);
                } else {
                    try {
                        while (!(operatorStack.peek() instanceof Parenthesis)) {
                            ret.add(operatorStack.pop());
                        }
                    } catch (EmptyStackException e) {
                        throw new InvalidParenthesisException(
                                "Unmatched parenthesis in " + equation);
                    }
                    operatorStack.pop();
                }
            }
        }

        while (operatorStack.size() > 0) {
            if (operatorStack.peek() instanceof Parenthesis) {
                throw new InvalidParenthesisException(
                        "Unmatched parenthesis found for " + equation);
            } else {
                ret.add(operatorStack.pop());
            }
        }

        return ret;
    }

    public static String addImpliedMultiplication(String equation) {
        if (equation == null) {
            return null;
        }

        Matcher before = IMPLIED_BEFORE_PAREN.matcher(equation);
        String ret = "";
        int position = 0;
        while (before.find()) {
            ret += equation.substring(position, before.start())
                    + before.group(1).replace("(",
                            Multiplication.OPERATOR + "(");
            position = before.end();
        }

        if (position != 0) {
            ret += equation.substring(position, equation.length());
            position = 0;
            equation = ret;
            ret = "";
        }

        Matcher after = IMPLIED_AFTER_PAREN.matcher(equation);
        while (after.find()) {
            ret += equation.substring(position, after.start())
                    + after.group(1)
                            .replace(")", ")" + Multiplication.OPERATOR);
            position = after.end();
        }

        if (position != 0) {
            ret += equation.substring(position, equation.length());
            position = 0;
            equation = ret;
            ret = "";
        }

        Matcher variables = IMPLIED_WITH_VAR.matcher(equation);
        while (variables.find()) {
            ret += equation.substring(position, variables.start())
                    + variables.group(0) + Multiplication.OPERATOR;
            position = variables.end();
        }

        if (position != 0) {
            ret += equation.substring(position, equation.length());
            position = 0;
            equation = ret;
            ret = "";
        }

        return equation;
    }

    public double evaluate(Map<String, BigDecimal> varValues)
            throws InvalidEquationException {
        Stack<Term> termStack = new Stack<>();
        Operation o;
        // currently the highest number of inputs for an equation is 2
        // adjust the size of this array if this changes
        Term[] evaluateMe = new Term[2];
        int i;

        for (Token t : this.tokens) {
            if (t instanceof Term) {
                termStack.push((Term) t);
            } else if (t instanceof Operation) {
                o = (Operation) t;
                if (o.getNumberOfInputs() > termStack.size()) {
                    throw new InvalidEquationException(
                            "Too few values in equation "
                                    + this.originalEquation + " for "
                                    + o.getClass().getSimpleName());
                } else {
                    for (i = 0; i < o.getNumberOfInputs(); i++) {
                        evaluateMe[i] = termStack.pop();
                    }
                    termStack.push(o.operate(evaluateMe, varValues));
                }
            } else {
                throw new EvaluationException(
                        "Invalid token found in equation "
                                + this.originalEquation);
            }
        }

        if (termStack.size() == 1) {
            return termStack.pop().evaluate(varValues);
        } else {
            throw new InvalidEquationException("Too many values in equation "
                    + this.originalEquation);
        }
    }

    public String getOriginalEquation() {
        return this.originalEquation;
    }
}
