package com.implemica.bormashenko.calculator.model.enums;

/**
 * Enum for possible types of operation.
 *
 * @author Mykhailo Bormahenko.
 */
public enum OperationType {

    /**
     * Binary operation requires two numbers.
     */
    BINARY,

    /**
     * Unary operation require one number.
     */
    UNARY,

    /**
     * Percent operation depends on previously used binary operation and can require one or two numbers.
     */
    PERCENT,

    /**
     * Special type for calculating result.
     */
    EQUALS
}
