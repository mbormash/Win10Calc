package com.implemica.bormashenko.calculator.model.enums;

/**
 * Enum with operations that can be used in model.
 *
 * @author Mykhailo Bormashenko
 */
public enum Operation {

    /**
     * Sums two numbers.
     */
    ADD(OperationType.BINARY),

    /**
     * Subtracts one number from another.
     */
    SUBTRACT(OperationType.BINARY),

    /**
     * Multiplies two numbers.
     */
    MULTIPLY(OperationType.BINARY),

    /**
     * Divides one number to another.
     */
    DIVIDE(OperationType.BINARY),

    /**
     * Negates number.
     */
    NEGATE(OperationType.UNARY),

    /**
     * Calculates square of number.
     */
    SQR(OperationType.UNARY),

    /**
     * Calculates square root of number.
     */
    SQRT(OperationType.UNARY),

    /**
     * Inverses number.
     */
    INVERSE(OperationType.UNARY),

    /**
     * Calculates percentage.
     */
    PERCENT(OperationType.PERCENT),

    /**
     * Calculates result of previously set binary operation.
     */
    EQUALS(OperationType.EQUALS);

    /**
     * Shows type of operation.
     */
    public OperationType type;

    Operation(OperationType type) {
        this.type = type;
    }
}
