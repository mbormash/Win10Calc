package com.implemica.bormashenko.calculator.model;

import com.implemica.bormashenko.calculator.model.enums.BinaryOperation;
import com.implemica.bormashenko.calculator.model.enums.UnaryOperation;
import com.implemica.bormashenko.calculator.model.exceptions.DivideByZeroException;
import com.implemica.bormashenko.calculator.model.exceptions.DivideZeroByZeroException;
import com.implemica.bormashenko.calculator.model.exceptions.NegativeRootException;
import com.implemica.bormashenko.calculator.model.exceptions.OverflowException;

import java.math.BigDecimal;

import static com.implemica.bormashenko.calculator.model.enums.BinaryOperation.*;
import static com.implemica.bormashenko.calculator.model.enums.UnaryOperation.*;

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

        Calculation calculation = new Calculation();
        BigDecimal a = new BigDecimal("5");
        BigDecimal b = new BigDecimal("3");
        BigDecimal c = new BigDecimal("0");

        UnaryOperation unary1 = SQR;
        UnaryOperation unary2 = NEGATE;
        UnaryOperation unary3 = SQRT;

        BinaryOperation binary1 = ADD;
        BinaryOperation binary2 = DIVIDE;

        BigDecimal result;

        try {
            //calculate (unary1(a)) and set it as first.
            result = calculation.calculateUnary(a, unary1);
            calculation.setFirst(result);

            System.out.println("Result of previous operation: " + result);

            //calculate (unary2(b)) and set it as second.
            result = calculation.calculateUnary(b, unary2);
            calculation.setSecond(result);

            System.out.println("Result of previous operation: " + result);

            //calculate (unary1(a) binary1 unary2(b))
            calculation.setBinaryOperation(binary1);
            result = calculation.calculateBinary();

            System.out.println("Result of previous operation: " + result);

            //set previous result as first, calculate (unary3(c)) and set it as second.
            calculation.setFirst(result);
            result = calculation.calculateUnary(c, unary3);
            calculation.setSecond(result);

            System.out.println("Result of previous operation: " + result);

            //calculate ((unary1(a) binary1 unary2(b)) binary2 unary3(c))
            calculation.setBinaryOperation(binary2);
            result = calculation.calculateBinary();

            System.out.println("Result of previous operation: " + result);
        } catch (OverflowException | DivideByZeroException | DivideZeroByZeroException | NegativeRootException e) {
            System.out.println("Result of previous operation: " + e.getMessage());
        }
    }
}

