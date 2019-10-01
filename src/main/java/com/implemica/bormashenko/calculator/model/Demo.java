package com.implemica.bormashenko.calculator.model;

import com.implemica.bormashenko.calculator.model.enums.Operation;
import com.implemica.bormashenko.calculator.model.exceptions.DivideByZeroException;
import com.implemica.bormashenko.calculator.model.exceptions.DivideZeroByZeroException;
import com.implemica.bormashenko.calculator.model.exceptions.NegativeRootException;
import com.implemica.bormashenko.calculator.model.exceptions.OverflowException;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import static com.implemica.bormashenko.calculator.model.enums.Operation.*;

/**
 * Demonstration of how to work with model.
 *
 * @author Mykhailo Bormashenko
 */
public class Demo {

    /**
     * Shows how model works.
     *
     * @param args command line args.
     */
    public static void main(String... args) {
//        dynamicDemo();
        staticDemo();
    }

    /**
     * Dynamically changeable demonstration.
     */
    private static void dynamicDemo() {
        Calculation calculation = new Calculation();

        List<Object> equation = new LinkedList<>();

        equation.add(new BigDecimal("5"));
        equation.add(SQR);
        equation.add(ADD);
        equation.add(new BigDecimal("3"));
        equation.add(NEGATE);
        equation.add(DIVIDE);
        equation.add(new BigDecimal("100"));
        equation.add(SQRT);
        equation.add(EQUALS);

        calculation.setFirst((BigDecimal) equation.get(0));

        BigDecimal result = BigDecimal.ZERO;

        try {
            for (int i = 1; i < equation.size(); i++) {
                if (equation.get(i) instanceof BigDecimal) {
                    calculation.setSecond((BigDecimal) equation.get(i));
                } else {
                    result = calculation.calculate((Operation) equation.get(i));
                }
            }

            System.out.println("Result of operations: " + result);
        } catch (OverflowException | DivideByZeroException | DivideZeroByZeroException | NegativeRootException e) {
            System.out.println("Result of operations: " + e.getMessage());
        }
    }

    /**
     * Static demonstration.
     */
    private static void staticDemo() {
        Calculation calculation = new Calculation();

        try{
            calculation.setFirst(new BigDecimal("5"));
            calculation.setBinaryOperation(ADD);
            calculation.setSecond(new BigDecimal("7"));
            calculation.calculate(EQUALS);
            calculation.calculate(SQRT);
            calculation.setSecond(new BigDecimal("2"));
            calculation.setBinaryOperation(DIVIDE);
            calculation.calculate(EQUALS);
            calculation.setSecond(new BigDecimal("100"));
            calculation.setBinaryOperation(MULTIPLY);
            BigDecimal result = calculation.calculate(EQUALS);

            System.out.println("Result of operations: " + result);
        } catch (OverflowException | DivideByZeroException | DivideZeroByZeroException | NegativeRootException e) {
            System.out.println("Result of operations: " + e.getMessage());
        }
    }
}

