package com.implemica.bormashenko.calculator.model;

import com.implemica.bormashenko.calculator.model.enums.BinaryOperation;
import com.implemica.bormashenko.calculator.model.enums.UnaryOperation;
import com.implemica.bormashenko.calculator.model.exceptions.DivideByZeroException;
import com.implemica.bormashenko.calculator.model.exceptions.DivideZeroByZeroException;
import com.implemica.bormashenko.calculator.model.exceptions.NegativeRootException;
import com.implemica.bormashenko.calculator.model.exceptions.OverflowException;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import static com.implemica.bormashenko.calculator.model.enums.BinaryOperation.*;
import static com.implemica.bormashenko.calculator.model.enums.UnaryOperation.*;

/**
 * Demonstration of how to work with model.
 *
 * @author Mykhailo Bormashenko
 */
public class Demo {

    private static Calculation calculation = new Calculation();

    /**
     * Shows how model works.
     *
     * @param args command line args.
     */
    public static void main(String... args) {
        List<Object> equation = new LinkedList<>();

        equation.add(new BigDecimal("5"));
        equation.add(SQR);
        equation.add(ADD);
        equation.add(new BigDecimal("3"));
        equation.add(NEGATE);
        equation.add(DIVIDE);
        equation.add(new BigDecimal("0"));
        equation.add(SQRT);
        equation.add("=");

        calculation.setFirst((BigDecimal) equation.get(0));

        boolean secondSet = false;
        BigDecimal result = BigDecimal.ZERO;

        try {
            for (int i = 1; i < equation.size(); i++) {

                if (equation.get(i) instanceof BigDecimal) {
                    calculation.setSecond((BigDecimal) equation.get(i));
                    secondSet = true;
                } else if (equation.get(i) instanceof BinaryOperation) {
                    result = doBinary(secondSet, (BinaryOperation) equation.get(i));
                    secondSet = false;
                } else if (equation.get(i) instanceof UnaryOperation) {
                    result = doUnary(secondSet, (UnaryOperation) equation.get(i));
                } else if (equation.get(i) == "=") {

                    if (!secondSet) {
                        calculation.setSecond(calculation.getFirst());
                    }

                    result = calculation.calculateBinary();
                } else {
                    throw new IllegalArgumentException("Expected: binary operation or number. Got: " + equation.get(i));
                }
            }

            System.out.println("Result of operations: " + result);
        } catch (OverflowException | DivideByZeroException | DivideZeroByZeroException | NegativeRootException e) {
            System.out.println("Result of operations: " + e.getMessage());
        }
    }

    /**
     * Performs {@code BinaryOperation} from model.
     *
     * @param secondSet true if second number is already set or false otherwise.
     * @param operation operation to perform.
     * @return result of operation.
     * @throws OverflowException         if the exception was thrown by model.
     * @throws DivideZeroByZeroException if the exception was thrown by model.
     * @throws DivideByZeroException     if the exception was thrown by model.
     */
    private static BigDecimal doBinary(boolean secondSet, BinaryOperation operation) throws OverflowException,
            DivideZeroByZeroException, DivideByZeroException {
        BigDecimal result = BigDecimal.ZERO;

        if (secondSet) {
            result = calculation.calculateBinary();
            calculation.setFirst(result);
        }

        calculation.setBinaryOperation(operation);

        return result;
    }

    /**
     * Performs {@code UnaryOperation} from model.
     *
     * @param secondSet true if second number is already set or false otherwise.
     * @param operation operation to perform.
     * @return result of operation.
     * @throws OverflowException     if the exception was thrown by model.
     * @throws NegativeRootException if the exception was thrown by model.
     * @throws DivideByZeroException if the exception was thrown by model.
     */
    private static BigDecimal doUnary(boolean secondSet, UnaryOperation operation) throws OverflowException,
            NegativeRootException, DivideByZeroException {

        BigDecimal result;

        if (secondSet) {
            result = calculation.calculateUnary(calculation.getSecond(), operation);
            calculation.setSecond(result);
        } else {
            result = calculation.calculateUnary(calculation.getFirst(), operation);

            if (calculation.getBinaryOperation() == null) {
                calculation.setFirst(result);
            } else {
                calculation.setSecond(result);
            }
        }

        return result;
    }
}

