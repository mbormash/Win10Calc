package com.implemica.bormashenko.calculator.model.util;

import java.math.BigDecimal;

/**
 * Class that contains method for deleting and appending last digit of number.
 *
 * @author Mykhailo Bormashenko
 */
public class AppendAndDeleteDigit {

    /**
     * {@code BigDecimal} value for appending new digit to decimal number.
     */
    private static final BigDecimal ONE_TENTH = new BigDecimal("0.1");

    /**
     * Maximal amount of digit symbols that can be shown on screen {@code Label}.
     */
    private final static int MAX_SYMBOLS = 16;

    /**
     * Appends digit to number if it's precision less than {@code MAX_SYMBOLS}. If number is 0 with scale == 0 and dot
     * should not be prepended before digit, returns digit. If number is negative, digit should be negated. Note that
     * digit should be a non-negative number in range [0-9] and {@code prependDotBeforeDigit} can be true only if
     * number's scale == 0.
     * <p>
     * Examples:
     * <ul>
     *   <li>
     *       If number is 0 with scale == 0 and dot should not be prepended before digit: <br>
     *       <ul>
     *           <li>
     *              {@code appendDigitToNumber(BigDecimal.ZERO, BigDecimal.ZERO, false) == BigDecimal.ZERO}; and <br>
     *           </li>
     *           <li>
     *              {@code appendDigitToNumber(BigDecimal.ZERO, new BigDecimal("9"), false) == new BigDecimal("9")}
     *           </li>
     *       </ul>
     *    </li>
     *   <li>
     *       If number's precision is less than {@code MAX_SYMBOLS} and dot should be prepended before digit and number's
     *       scale == 0: <br>
     *       <ul>
     *           <li>
     *              {@code appendDigitToNumber(new BigDecimal("123"), BigDecimal.ZERO, true) == new BigDecimal("123.0")}; and <br>
     *           </li>
     *           <li>
     *              {@code appendDigitToNumber(new BigDecimal("123456"), new BigDecimal("9"), true) == new BigDecimal("123456.9")}; and <br>
     *           </li>
     *           <li>
     *              {@code appendDigitToNumber(new BigDecimal("-123"), BigDecimal.ZERO, true) == new BigDecimal("-123.0")}; and <br>
     *           </li>
     *           <li>
     *              {@code appendDigitToNumber(new BigDecimal("-123456"), new BigDecimal("9"), true) == new BigDecimal("-123456.9")}
     *           </li>
     *       </ul>
     *    </li>
     *   <li>
     *       If number's precision is less than {@code MAX_SYMBOLS} and it's scale == 0: <br>
     *       <ul>
     *           <li>
     *              {@code appendDigitToNumber(new BigDecimal("123"), BigDecimal.ZERO, false) == new BigDecimal("1230")}; and <br>
     *           </li>
     *           <li>
     *              {@code appendDigitToNumber(new BigDecimal("123456"), new BigDecimal("9"), false) == new BigDecimal("1234569")}; and <br>
     *           </li>
     *           <li>
     *              {@code appendDigitToNumber(new BigDecimal("-123"), BigDecimal.ZERO, false) == new BigDecimal("-1230")}; and <br>
     *           </li>
     *           <li>
     *              {@code appendDigitToNumber(new BigDecimal("-123456"), new BigDecimal("9"), false) == new BigDecimal("-1234569")}
     *           </li>
     *       </ul>
     *   <li>
     *       If number's precision is less than {@code MAX_SYMBOLS} and it's scale != 0: <br>
     *       <ul>
     *           <li>
     *              {@code appendDigitToNumber(new BigDecimal("123.1"), BigDecimal.ZERO, false) == new BigDecimal("123.10")}; and <br>
     *           </li>
     *           <li>
     *              {@code appendDigitToNumber(new BigDecimal("123456.5001"), new BigDecimal("9"), false) == new BigDecimal("123456.50019")}; and <br>
     *            </li>
     *            <li>
     *              {@code appendDigitToNumber(new BigDecimal("-123.1"), BigDecimal.ZERO, false) == new BigDecimal("-123.10")}; and <br>
     *            </li>
     *            <li>
     *              {@code appendDigitToNumber(new BigDecimal("-123456.5001"), new BigDecimal("9"), false) == new BigDecimal("-123456.50019")}
     *           </li>
     *       </ul>
     *    </li>
     *    <li>
     *       If number's precision is not less than {@code MAX_SYMBOLS}: <br>
     *       <ul>
     *           <li>
     *              {@code appendDigitToNumber(new BigDecimal("1234567812345678"), BigDecimal.ZERO, false) == new BigDecimal("1234567812345678")}; and <br>
     *           </li>
     *           <li>
     *              {@code appendDigitToNumber(new BigDecimal("123456789.123456789"), new BigDecimal("9"), false) == new BigDecimal("123456789.123456789")}; and <br>
     *           </li>
     *           <li>
     *              {@code appendDigitToNumber(new BigDecimal("-1234567812345678"), BigDecimal.ZERO, false) == new BigDecimal("-1234567812345678")}; and <br>
     *           </li>
     *           <li>
     *              {@code appendDigitToNumber(new BigDecimal("-123456789.123456789"), new BigDecimal("9"), false) == new BigDecimal("-123456789.123456789")}
     *           </li>
     *       </ul>
     *   </li>
     *   <li>
     *       If digit is not in range [0-9] or it's scale is not 0, exception should be thrown: <br>
     *       <ul>
     *           <li>
     *              {@code appendDigitToNumber(new BigDecimal("123"), new BigDecimal("10"), false)
     *              throws new IllegalArgumentException("Expected: integer value of digit in range [0-9]. Got: " + 10)}; and <br>
     *           </li>
     *           <li>
     *              {@code appendDigitToNumber(new BigDecimal("123"), new BigDecimal("-1"), true)
     *              throws new IllegalArgumentException("Expected: integer value of digit in range [0-9]. Got: " + -1)}; and <br>
     *            </li>
     *            <li>
     *              {@code appendDigitToNumber(new BigDecimal("123"), new BigDecimal("1.1"), false)
     *              throws new IllegalArgumentException("Expected: integer value of digit in range [0-9]. Got: " + 1.1)}; and <br>
     *            </li>
     *            <li>
     *              {@code appendDigitToNumber(new BigDecimal("123"), new BigDecimal("1.1"), true)
     *              throws new IllegalArgumentException("Expected: integer value of digit in range [0-9]. Got: " + 1.1)}
     *           </li>
     *       </ul>
     *   </li>
     *   <li>
     *       If {@code prependDotBeforeDigit} == true and number's scale != 0: <br>
     *       <ul>
     *           <li>
     *              {@code appendDigitToNumber(new BigDecimal("123.123"), BigDecimal.ZERO, true)
     *              throws new IllegalArgumentException("Expected: value of prependDotBeforeDigit is true only if number's
     *              scale is 0. Got: prependDotBeforeDigit == true and number is 123.123)}
     *           </li>
     *       </ul>
     *    </li>
     * </ul>
     *
     * @param number                number to edit.
     * @param digit                 digit to append to number.
     * @param prependDotBeforeDigit true if {@code DECIMAL_SEPARATOR} should be prepended before digit or false
     *                              otherwise. It helps not to loose trailing {@code DECIMAL_SEPARATOR} in string
     *                              representation of number while parsing it to {@code BigDecimal}.
     * @return edited number if it was possible to edit.
     */
    public static BigDecimal appendDigitToNumber(BigDecimal number, BigDecimal digit, boolean prependDotBeforeDigit) {
        if (digitValidationFailed(digit)) {
            throw new IllegalArgumentException("Expected: integer value of digit in range [0-9]. Got: " + digit);
        }

        if (number.scale() != 0 && prependDotBeforeDigit) {
            throw new IllegalArgumentException("Expected: value of prependDotBeforeDigit is true only if number's " +
                    "scale is 0. Got: prependDotBeforeDigit == " + true + " and number is " + number);
        }

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
     * Deletes last digit in number. If number's precision is 1, returns 0. Otherwise returns number without last digit.
     * <p>
     * Examples:
     * <ul>
     *   <li>
     *       If number's precision is 0: <br>
     *       <ul>
     *           <li>
     *               {@code deleteLastDigit(BigDecimal.ZERO) == BigDecimal.ZERO}; and <br>
     *           </li>
     *           <li>
     *               {@code deleteLastDigit(new BigDecimal("1")) == BigDecimal.ZERO}; and <br>
     *           </li>
     *           <li>
     *               {@code deleteLastDigit(new BigDecimal("9")) == BigDecimal.ZERO}; and <br>
     *           </li>
     *           <li>
     *               {@code deleteLastDigit(new BigDecimal("-1")) == BigDecimal.ZERO}; and <br>
     *           </li>
     *           <li>
     *               {@code deleteLastDigit(new BigDecimal("-9")) == BigDecimal.ZERO}
     *           </li>
     *       </ul>
     *   </li>
     *   <li>
     *       If number's precision is not 0: <br>
     *       <ul>
     *           <li>
     *               {@code deleteLastDigit(new BigDecimal("10")) == BigDecimal.ONE}; and <br>
     *           </li>
     *           <li>
     *               {@code deleteLastDigit(new BigDecimal("-10")) == new BigDecimal("-1")}; and <br>
     *           </li>
     *           <li>
     *               {@code deleteLastDigit(new BigDecimal("9.9")) == new BigDecimal("9")}; and <br>
     *           </li>
     *           <li>
     *               {@code deleteLastDigit(new BigDecimal("-9.9")) == new BigDecimal("-9")}; and <br>
     *           </li>
     *           <li>
     *               {@code deleteLastDigit(new BigDecimal("123.123")) == new BigDecimal("123.12")}; and <br>
     *           </li>
     *           <li>
     *               {@code deleteLastDigit(new BigDecimal("-123.123")) == new BigDecimal("-123.12")}
     *           </li>
     *       </ul>
     *   </li>
     * </ul>
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

    /**
     * Validation that number is integer non-negative and in range [0-9].
     *
     * @param number number to check.
     * @return true if number is negative or decimal or not in range [0-9] or false otherwise.
     */
    private static boolean digitValidationFailed(BigDecimal number) {
        boolean severalDigitsInNumber = number.compareTo(BigDecimal.TEN) >= 0;

        if (severalDigitsInNumber) {
            return true;
        }

        boolean negativeNumber = number.compareTo(BigDecimal.ZERO) < 0;

        if (negativeNumber) {
            return true;
        }

        return number.scale() != 0;
    }
}
