package com.implemica.bormashenko.calculator.controller.util;

import java.math.BigDecimal;

import static com.implemica.bormashenko.calculator.controller.util.NumberFormatter.MAX_SYMBOLS;

/**
 * Class for editing number.
 *
 * @author Mykhailo Bormashenko
 */
public class NumberEditor {

    /**
     * {@code BigDecimal} value for appending new digit to decimal number.
     */
    private static final BigDecimal ONE_TENTH = new BigDecimal("0.1");

    /**
     * Appends digit to number if it's precision less than {@code MAX_SYMBOLS}.
     * <p>
     * For appending digit, math operations are used.
     * <p>
     * If number < 0, digit should be negated.
     * <p>
     * If dot should be prepended before digit, multiplies digit on 0.1 and adds it to number.
     * <p>
     * Otherwise, if number is 0 (with scale = 0), returns digit only.
     * <p>
     * Otherwise, if number does not have decimal part, multiplies number on 10 and adds digit to number.
     * <p>
     * Otherwise, multiplies digit on 0.1^(number's scale + 1) and adds it to number.
     *
     * @param number                number to edit.
     * @param digit                 digit to append to number.
     * @param prependDotBeforeDigit true if {@code DECIMAL_SEPARATOR} should be prepended before digit or false
     *                              otherwise. It helps not to loose trailing {@code DECIMAL_SEPARATOR} in string
     *                              representation of number while parsing it to {@code BigDecimal}.
     * @return edited number if it was possible to edit.
     */
    public static BigDecimal appendDigitToNumber(BigDecimal number, BigDecimal digit, boolean prependDotBeforeDigit) {
        //note that exactly equals method should be used there cause does matter scale of number
        if (number.equals(BigDecimal.ZERO) && !prependDotBeforeDigit) {
            return digit;
        }

        BigDecimal result = number;

        if (number.precision() < MAX_SYMBOLS) {

            if (number.signum() < 0) {
                digit = digit.negate();
            }

            BigDecimal multiplyDigitBy = BigDecimal.ONE;
            BigDecimal multiplyNumberBy = BigDecimal.ONE;

            if (prependDotBeforeDigit) {
                multiplyDigitBy = ONE_TENTH;
            } else if (number.scale() == 0) {
                multiplyNumberBy = BigDecimal.TEN;
            } else {
                multiplyDigitBy = ONE_TENTH.pow(number.scale() + 1);
            }

            number = number.multiply(multiplyNumberBy);
            digit = digit.multiply(multiplyDigitBy);
            result = number.add(digit);
        }

        return result;
    }

    /**
     * Deletes last digit in number.
     * <p>
     * Otherwise, if number is one-digit number, returns 0.
     * <p>
     * Otherwise, if number does not have decimal part, divides in by 10 and sets scale of result to 0.
     * <p>
     * Otherwise, reduces number's scale by 1.
     *
     * @param number number to edit.
     * @return edited number.
     */
    public static BigDecimal deleteLastDigit(BigDecimal number) {
        if (number.precision() == 1) {
            return BigDecimal.ZERO;
        }

        BigDecimal result;
        int newScale;

        if (number.scale() == 0) {
            result = number.divide(BigDecimal.TEN, BigDecimal.ROUND_DOWN);
            newScale = 0;
        } else {
            result = number;
            newScale = result.scale() - 1;
        }

        return result.setScale(newScale, BigDecimal.ROUND_DOWN);
    }
}
