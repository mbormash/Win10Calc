package com.implemica.bormashenko.calculator.model;

import com.implemica.bormashenko.calculator.model.enums.Operation;
import com.implemica.bormashenko.calculator.model.enums.OperationType;
import com.implemica.bormashenko.calculator.model.exceptions.DivideByZeroException;
import com.implemica.bormashenko.calculator.model.exceptions.DivideZeroByZeroException;
import com.implemica.bormashenko.calculator.model.exceptions.NegativeRootException;
import com.implemica.bormashenko.calculator.model.exceptions.OverflowException;
import com.implemica.bormashenko.calculator.model.util.OverflowValidation;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * This class contains model of how the calculator works.
 *
 * @author Mykhailo Bormashenko
 */
public class Calculation {

    /**
     * Scale for {@code Operation.DIVIDE}.
     *
     * @see Operation
     */
    private static final int DIVIDE_SCALE = 10000;

    /**
     * MathContext for {@code Operation.SQRT}.
     *
     * @see Operation
     */
    private static final MathContext SQRT_CONTEXT = MathContext.DECIMAL64;

    /**
     * Max possible scale for number.
     * <p>
     * If scale is bigger, {@link OverflowException} should be thrown.
     */
    private static final int MAX_SCALE = 9999;

    /**
     * The number and any number less that that should cause {@link OverflowException} if trying to calculate percent of
     * 100 for the number.
     */
    private static final BigDecimal PERCENT_OF_100_EDGE = new BigDecimal("1.e-9998");

    /**
     * {@code BigDecimal} value of 0.5.
     */
    private static final BigDecimal ONE_HALF = new BigDecimal("0.5");

    /**
     * {@code BigDecimal} value of 100.
     */
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    /**
     * First number of equation.
     */
    private BigDecimal first = BigDecimal.ZERO;

    /**
     * First number of equation.
     */
    private BigDecimal second = BigDecimal.ZERO;

    /**
     * True if second number is set or false otherwise.
     */
    private boolean isSecondSet = false;

    /**
     * True if next unary operations should be made with first number or false otherwise.
     */
    private boolean unaryOnFirst = false;

    /**
     * True if previous operation was equals or false otherwise.
     */
    private boolean previousEquals = false;

    /**
     * {@link Operation} of equation.
     */
    private Operation binaryOperation = null;

    public Operation getBinaryOperation() {
        return binaryOperation;
    }

    public void setFirst(BigDecimal first) {
        this.first = first;
    }

    public BigDecimal getFirst() {
        return first;
    }

    public void setSecond(BigDecimal second) {
        this.second = second;
        isSecondSet = true;
    }

    /**
     * Resets all fields to theirs primary state.
     */
    public void resetAll() {
        first = BigDecimal.ZERO;
        second = BigDecimal.ZERO;
        binaryOperation = null;
        isSecondSet = false;
        unaryOnFirst = false;
        previousEquals = false;
    }

    /**
     * Performs operation. If numbers were passed, sets them as first or second. Operations can be made with first
     * number, second or between both of them. Does not support priority.
     *
     * @param operation operation to use.
     * @param numbers   numbers to set as first and/or second.
     * @return result of operation.
     * @throws OverflowException         if overflow validation failed.
     * @throws DivideZeroByZeroException if trying to divide zero by zero.
     * @throws DivideByZeroException     if trying to divide by zero.
     * @throws NegativeRootException     if trying to calculate negative root.
     */
    public BigDecimal doOperation(Operation operation, BigDecimal... numbers) throws OverflowException,
            DivideZeroByZeroException, DivideByZeroException, NegativeRootException {
        if (operation.type == OperationType.UNARY && numbers.length > 1) {
            throw new IllegalArgumentException("Excepted: 0 or 1 number for setting as first and performing unary " +
                    "operation. Got: " + numbers.length + " numbers.");
        } else if (numbers.length > 2) {
            throw new IllegalArgumentException("Excepted: 2 or less numbers for setting as first and second. Got: " +
                    numbers.length + " numbers.");
        }

        if (numbers.length == 1) {

            if (operation.type == OperationType.BINARY) {
                second = numbers[0];
                isSecondSet = true;
                binaryOperation = null;
            } else {
                first = numbers[0];
            }
        }

        if (numbers.length == 2) {
            first = numbers[0];
            second = numbers[1];
            isSecondSet = true;
        }

        BigDecimal result;

        if (!isSecondSet) {
            result = operationWithoutSecond(operation);
        } else {
            result = operationWithSecond(operation);
        }

        if (numbers.length == 2) {
            unaryOnFirst = true;
        }

        return result;
    }

    /**
     * Performs operation if second number is not set.
     *
     * @param operation operation to use.
     * @return result of operation.
     * @throws OverflowException         if overflow validation failed.
     * @throws DivideZeroByZeroException if trying to divide zero by zero.
     * @throws DivideByZeroException     if trying to divide by zero.
     * @throws NegativeRootException     if trying to calculate negative root.
     */
    private BigDecimal operationWithoutSecond(Operation operation) throws OverflowException, DivideZeroByZeroException,
            DivideByZeroException, NegativeRootException {
        BigDecimal result = BigDecimal.ZERO;

        if (operation.type == OperationType.UNARY) {
            result = calculateUnary(first, operation);

            if (binaryOperation != null) {
                second = result;
                isSecondSet = true;
            } else {
                first = result;
            }

            previousEquals = false;
        } else if (operation.type == OperationType.BINARY) {
            result = first;
            binaryOperation = operation;
            previousEquals = false;
        } else if (operation.type == OperationType.PERCENT) {
            result = calculatePercentage(first);

            if (binaryOperation != null) {
                second = result;
                isSecondSet = true;
            }

            previousEquals = false;
        } else if (operation.type == OperationType.EQUALS) {

            if (binaryOperation != null) {
                second = first;
                isSecondSet = true;
                result = calculateBinary();
                first = result;
            } else {
                result = first;
            }

            previousEquals = true;
        }

        return result;
    }

    /**
     * Performs operation if second number is set.
     *
     * @param operation operation to use.
     * @return result of operation.
     * @throws OverflowException         if overflow validation failed.
     * @throws DivideZeroByZeroException if trying to divide zero by zero.
     * @throws DivideByZeroException     if trying to divide by zero.
     * @throws NegativeRootException     if trying to calculate negative root.
     */
    private BigDecimal operationWithSecond(Operation operation) throws OverflowException, DivideByZeroException,
            NegativeRootException, DivideZeroByZeroException {
        BigDecimal result = BigDecimal.ZERO;

        if (operation.type == OperationType.UNARY) {

            if (unaryOnFirst) {
                result = calculateUnary(first, operation);
                first = result;
            } else {
                result = calculateUnary(second, operation);
                second = result;
            }

            previousEquals = false;
        } else if (operation.type == OperationType.BINARY) {

            if (binaryOperation == null) {
                binaryOperation = operation;
            }

            if (previousEquals) {
                result = first;
                second = first;
            } else {
                result = calculateBinary();
                first = result;
            }

            binaryOperation = operation;
            previousEquals = false;
            unaryOnFirst = false;
        } else if (operation.type == OperationType.PERCENT) {

            if (unaryOnFirst) {
                result = calculatePercentage(first);
            } else {
                result = calculatePercentage(second);
            }

            second = result;
            previousEquals = false;
        } else if (operation.type == OperationType.EQUALS) {
            result = calculateBinary();
            first = result;
            unaryOnFirst = true;
            previousEquals = true;
        }

        return result;
    }

    /**
     * Calculates result using first value, {@link Operation} and second value.
     *
     * @return result of operation.
     * @throws OverflowException         while validation for result is failed.
     * @throws DivideByZeroException     if trying to divide by zero.
     * @throws DivideZeroByZeroException if trying to divide zero by zero.
     */
    private BigDecimal calculateBinary() throws OverflowException, DivideByZeroException, DivideZeroByZeroException {
        BigDecimal result = BigDecimal.ZERO;

        if (binaryOperation == Operation.ADD) {
            result = add();
        } else if (binaryOperation == Operation.SUBTRACT) {
            result = subtract();
        } else if (binaryOperation == Operation.MULTIPLY) {
            result = multiply();
        } else if (binaryOperation == Operation.DIVIDE) {
            result = divide();
        }

        result = result.stripTrailingZeros();

        if (OverflowValidation.overflowValidationFailed(result, binaryOperation == Operation.DIVIDE,
                first)) {
            throw new OverflowException();
        }

        return result;
    }

    /**
     * Calculates result using first value and {@link Operation}.
     *
     * @param number         number to work with.
     * @param unaryOperation operation to perform.
     * @return result of operation.
     * @throws OverflowException     while validation for result is failed.
     * @throws NegativeRootException if trying to divide inverse zero.
     * @throws DivideByZeroException if trying to divide inverse zero.
     */
    private BigDecimal calculateUnary(BigDecimal number, Operation unaryOperation) throws OverflowException,
            NegativeRootException, DivideByZeroException {
        BigDecimal result = BigDecimal.ZERO;

        if (unaryOperation == Operation.NEGATE) {
            result = negate(number);
        } else if (unaryOperation == Operation.SQR) {
            result = sqr(number);
        } else if (unaryOperation == Operation.SQRT) {
            result = sqrt(number);
        } else if (unaryOperation == Operation.INVERSE) {
            result = inverse(number);
        }

        result = result.stripTrailingZeros();

        if (OverflowValidation.overflowValidationFailed(result, false, first)) {
            throw new OverflowException();
        }

        return result;
    }

    /**
     * Calculates second number as a percentage of first number if current {@code BinaryOperation} is
     * {@code BinaryOperation.ADD} or {@code BinaryOperation.SUBTRACT}, or as a percentage of 100 if current
     * {@code BinaryOperation} is {@code BinaryOperation.MULTIPLY} or {@code BinaryOperation.DIVIDE}
     *
     * @param number number to perform operation.
     * @return result of operation.
     * @throws OverflowException while validation for result is failed.
     */
    private BigDecimal calculatePercentage(BigDecimal number) throws OverflowException {
        BigDecimal result = BigDecimal.ZERO;

        if (binaryOperation == null) {
            resetAll();
        } else if (binaryOperation == Operation.ADD || binaryOperation == Operation.SUBTRACT) {
            result = percentageOfFirst(number);
        } else if (binaryOperation == Operation.MULTIPLY || binaryOperation == Operation.DIVIDE) {
            result = percentageOf100(number);
        }

        result = result.stripTrailingZeros();

        if (OverflowValidation.overflowValidationFailed(result, false, first)) {
            throw new OverflowException();
        }

        return result;
    }

    /**
     * Adds first number to second.
     *
     * @return result of adding two numbers.
     */
    private BigDecimal add() {
        return first.add(second);
    }

    /**
     * Subtracts second value from first.
     *
     * @return result of subtracting one number from another.
     */
    private BigDecimal subtract() {
        return first.subtract(second);
    }

    /**
     * Multiplies first number and second.
     *
     * @return result of multiplying two numbers.
     */
    private BigDecimal multiply() {
        return first.multiply(second);
    }

    /**
     * Divides first number on second.
     *
     * @return result of dividing one number on another.
     * @throws DivideByZeroException     if second number is 0.
     * @throws DivideZeroByZeroException if first and second numbers are 0.
     */
    private BigDecimal divide() throws DivideByZeroException, DivideZeroByZeroException {
        if (first.compareTo(BigDecimal.ZERO) == 0 && second.compareTo(BigDecimal.ZERO) != 0) {
            return BigDecimal.ZERO;
        }

        if (second.compareTo(BigDecimal.ZERO) == 0) {

            if (first.compareTo(BigDecimal.ZERO) == 0) {
                throw new DivideZeroByZeroException();
            }

            throw new DivideByZeroException();
        }

        return first.divide(second, DIVIDE_SCALE, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Calculates negated first number.
     *
     * @param number number to perform operation.
     * @return negated first number.
     */
    private BigDecimal negate(BigDecimal number) {
        return number.negate();
    }

    /**
     * Calculates square of first number.
     *
     * @param number number to perform operation.
     * @return square of first number.
     */
    private BigDecimal sqr(BigDecimal number) {
        return number.multiply(number);
    }

    /**
     * Calculates square root of first number.
     * <p>
     * This method is almost copied from JDK.9 src.
     * All additional information You can find at
     * {@link = "https://docs.oracle.com/javase/9/docs/api/java/math/BigDecimal.html#sqrt-java.math.MathContext-"}
     *
     * @param number number to perform operation.
     * @return square root of first number.
     * @throws NegativeRootException while first value is negative.
     */
    private BigDecimal sqrt(BigDecimal number) throws NegativeRootException {
        int signum = number.signum();

        if (signum < 0) {
            throw new NegativeRootException();
        }

        if (signum == 0) {
            return BigDecimal.ZERO;
        }

        int preferredScale = number.scale() / 2;
        BigDecimal zeroWithFinalPreferredScale = BigDecimal.valueOf(0L, preferredScale);
        BigDecimal stripped = number.stripTrailingZeros();
        int strippedScale = stripped.scale();

        if (stripped.unscaledValue().compareTo(BigInteger.ONE) == 0 &&
                strippedScale % 2 == 0) {
            BigDecimal result = BigDecimal.valueOf(1L, strippedScale / 2);

            if (result.scale() != preferredScale) {
                result = result.add(zeroWithFinalPreferredScale, SQRT_CONTEXT);
            }

            return result;
        }

        int scaleAdjust;
        int scale = stripped.scale() - stripped.precision() + 1;

        if (scale % 2 == 0) {
            scaleAdjust = scale;
        } else {
            scaleAdjust = scale - 1;
        }

        BigDecimal working = stripped.scaleByPowerOfTen(scaleAdjust);

        BigDecimal guess = new BigDecimal(Math.sqrt(working.doubleValue()));
        int guessPrecision = 15;
        int originalPrecision = SQRT_CONTEXT.getPrecision();

        BigDecimal approx = guess;
        int workingPrecision = working.precision();

        do {
            int tmpPrecision = Math.max(Math.max(guessPrecision, originalPrecision + 2), workingPrecision);
            MathContext mcTmp = new MathContext(tmpPrecision, RoundingMode.HALF_EVEN);
            approx = ONE_HALF.multiply(approx.add(working.divide(approx, mcTmp), mcTmp));
            guessPrecision *= 2;
        } while (guessPrecision < originalPrecision + 2);

        BigDecimal result;
        result = approx.scaleByPowerOfTen(-scaleAdjust / 2).round(SQRT_CONTEXT);

        if (result.scale() != preferredScale) {
            result = result.stripTrailingZeros().
                    add(zeroWithFinalPreferredScale,
                            new MathContext(originalPrecision, RoundingMode.UNNECESSARY));
        }

        return result;
    }

    /**
     * Calculates inverted first number.
     *
     * @param number number to perform operation.
     * @return inverted first number.
     * @throws DivideByZeroException while first number is 0.
     */
    private BigDecimal inverse(BigDecimal number) throws DivideByZeroException {
        if (number.compareTo(BigDecimal.ZERO) == 0) {
            throw new DivideByZeroException();
        }

        return BigDecimal.ONE.divide(number, DIVIDE_SCALE, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Calculates second number as a percentage of the first.
     *
     * @param number number to perform operation.
     * @throws OverflowException while validation for second value is failed.
     */
    private BigDecimal percentageOfFirst(BigDecimal number) throws OverflowException {
        if (number.scale() + first.scale() > MAX_SCALE) {
            throw new OverflowException();
        }

        return first.multiply(number).divide(ONE_HUNDRED, DIVIDE_SCALE, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();
    }

    /**
     * Calculates second number as a percentage of 100.
     *
     * @param number number to perform operation.
     * @throws OverflowException while validation for second number is failed.
     */
    private BigDecimal percentageOf100(BigDecimal number) throws OverflowException {
        if (number.abs().compareTo(PERCENT_OF_100_EDGE) <= 0 && number.compareTo(BigDecimal.ZERO) != 0) {
            throw new OverflowException();
        }

        return number.divide(ONE_HUNDRED, DIVIDE_SCALE, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();
    }
}
