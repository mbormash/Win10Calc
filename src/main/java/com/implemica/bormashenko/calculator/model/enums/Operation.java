package com.implemica.bormashenko.calculator.model.enums;

/**
 * This enum contains binary operations used in {@link com.implemica.bormashenko.calculator.model.Calculation}.
 *
 * @author Mykhailo Bormashenko
 */
public enum Operation {

    /**
     * Sums two numbers.
     */
    ADD("binary"),

    /**
     * Subtracts one number from another.
     */
    SUBTRACT("binary"),

    /**
     * Multiplies two numbers.
     */
    MULTIPLY("binary"),

    /**
     * Divides one number to another.
     */
    DIVIDE("binary"),

    /**
     * Negates number.
     */
    NEGATE("unary"),

    /**
     * Calculates square of number.
     */
    SQR("unary"),

    /**
     * Calculates square root of number.
     */
    SQRT("unary"),

    /**
     * Inverses number.
     */
    INVERSE("unary"),

    /**
     * Calculates percentage.
     */
    PERCENT("percent"),

    /**
     * Calculates result of previously set binary operation.
     */
    EQUALS("equals");

    public String type;

    Operation(String type) {
        this.type = type;
    }


}
