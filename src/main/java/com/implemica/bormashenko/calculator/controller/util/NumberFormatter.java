package com.implemica.bormashenko.calculator.controller.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

/**
 * Class for editing numbers' representation.
 *
 * @author Mykhailo Bormashenko
 */
public class NumberFormatter {

    /**
     * Symbol for separating integer and decimal parts of number.
     */
    public static final char DECIMAL_SEPARATOR = '.';

    /**
     * Maximal amount of digit symbols that can be shown on screen {@code Label}.
     */
    final static int MAX_SYMBOLS = 16;

    /**
     * Minimal value that can be shown on screen {@code Label} without using engineer representation.
     */
    private static final BigDecimal MIN_PLAIN_VALUE = new BigDecimal("0.001");

    /**
     * Decimal formatter for formatting output.
     */
    private static final DecimalFormat formatter = new DecimalFormat();

    /**
     * Symbol for separating every three digits in integer number.
     */
    private static final char GROUPING_SEPARATOR = ',';

    /**
     * Symbol for separating exponent part of number if number is decimal.
     */
    private static final String DECIMAL_EXPONENT_SEPARATOR = "e";

    /**
     * Symbol for separating exponent part of number if number is integer.
     */
    private static final String INTEGER_EXPONENT_SEPARATOR = "e+";

    /**
     * Pattern string for 15 digits if they exist.
     */
    private static final String PATTERN_15_DIGITS = "###############";

    /**
     * Pattern string for splitting integer group.
     */
    private static final String PATTERN_SPLIT_GROUP = "###" + GROUPING_SEPARATOR + "###" + DECIMAL_SEPARATOR;

    /**
     * Pattern for exponent symbol.
     */
    private static final String PATTERN_EXPONENT = "E";

    /**
     * Pattern for digit if it exists.
     */
    private static final String PATTERN_DIGIT = "#";

    /**
     * Origin string in screen {@code Label}. Also used as symbol-or-zero part of pattern.
     */
    private static final String ZERO = "0";

    /**
     * Regex for checking that second char of unsigned number is {@code DECIMAL_EXPONENT_SEPARATOR}.
     */
    private static final String SECOND_CHAR_EXPONENT_REGEX = "-?\\d" + DECIMAL_EXPONENT_SEPARATOR + "\\+?-?\\d+";

    /**
     * Object for setting symbols for decimal formatter.
     */
    private static DecimalFormatSymbols symbols = new DecimalFormatSymbols();

    static {
        symbols.setGroupingSeparator(GROUPING_SEPARATOR);
        symbols.setDecimalSeparator(DECIMAL_SEPARATOR);
        formatter.setDecimalFormatSymbols(symbols);
        formatter.setParseBigDecimal(true);
    }

    /**
     * Formats a number using {@link DecimalFormat}.
     * <p>
     * Result of formatting depends on a number's scale and precision. If number is less than {@code MIN_PLAIN_VALUE}
     * and it's scale more than {@code MAX_SYMBOLS}, number should be shown with exponent. The similar formatting is
     * used if length of integer part is more than {@code MAX_SYMBOLS}. Otherwise, whole integer part should be shown
     * and decimal part should be shown unless the whole number length is less than {@code MAX_SYMBOLS}. Note that
     * number's scale should be set to the {@code MAX_SYMBOLS} value.
     * <p>
     * Examples:
     * <ul>
     *   <li>
     *       If number is less than {@code MIN_PLAIN_VALUE} and it's scale more than {@code MAX_SYMBOLS}: <br>
     *       <ul>
     *           <li>
     *              {@code formatNumber(0.00000000000000001, true) == 1.e-17}; and
     *              {@code formatNumber(0.00000000000000001, false) == 1.e-17}
     *           </li>
     *           <li>
     *              {@code formatNumber(0.00000000123456789, true) == 1.23456789.e-9}; and
     *              {@code formatNumber(0.00000000123456789, false) == 1.23456789.e-9}
     *           </li>
     *           <li>
     *              {@code formatNumber(0.00000000123456789123456789, true) == 1.234567891234568.e-9}; and
     *              {@code formatNumber(0.00000000123456789123456789, false) == 1.234567891234568.e-9}
     *           </li>
     *       </ul>
     *    </li>
     *   <li>
     *       If length of integer part is more than {@code MAX_SYMBOLS}: <br>
     *       <ul>
     *           <li>
     *              {@code formatNumber(10000000000000000, true) == 1.e+16}; and
     *              {@code formatNumber(10000000000000000, false) == 1.e+16}
     *           </li>
     *           <li>
     *              {@code formatNumber(111222333444555666777888999, true) == 1.112223334445557.e+26}; and
     *              {@code formatNumber(111222333444555666777888999, false) == 1.112223334445557.e+26}
     *           </li>
     *       </ul>
     *    </li>
     * <li>
     *       Otherwise: <br>
     *       <ul>
     *           <li>
     *              {@code formatNumber(123, true) == 123}; and
     *              {@code formatNumber(123, false) == 123}
     *           </li>
     *           <li>
     *              {@code formatNumber(1234, true) == 1,234}; and
     *              {@code formatNumber(1234, false) == 1234}
     *           </li>
     *           <li>
     *              {@code formatNumber(123456, true) == 123,456}; and
     *              {@code formatNumber(123456, false) == 123456}
     *           </li>
     *           <li>
     *              {@code formatNumber(1234567890, true) == 1,234,567,890}; and
     *              {@code formatNumber(1234567890, false) == 1234567890}
     *           </li>
     *           <li>
     *              {@code formatNumber(123.123456, true) == 123.123456}; and
     *              {@code formatNumber(123.123456, false) == 123.123456}
     *           </li>
     *           <li>
     *              {@code formatNumber(1234567.123456, true) == 1,234,567.123456}; and
     *              {@code formatNumber(1234567.123456, false) == 1234567.123456}
     *           </li>
     *           <li>
     *              {@code formatNumber(1.2345678901234567, true) == 1.234567890123457}; and
     *              {@code formatNumber(1.2345678901234567, false) == 1.2345678901234567}
     *           </li>
     *           <li>
     *              {@code formatNumber(12345678.12345678222222222, true) == 12,345,678.12345678}; and
     *              {@code formatNumber(12345678.12345678222222222, false) == 12345678.12345678}
     *           </li>
     *        </ul>
     *    </li>
     * </ul>
     *
     * @param number      number to format.
     * @param useGrouping true if {@code GROUPING_SEPARATOR} should be used or false otherwise.
     * @return formatted number as string.
     */
    public static String formatNumber(BigDecimal number, boolean useGrouping) {
        BigDecimal numberToWorkWith;

        if (number.scale() == 0) {
            numberToWorkWith = number;
        } else {
            numberToWorkWith = number.stripTrailingZeros();
        }

        int trailingZeros = number.scale() - numberToWorkWith.scale();

        setExponentSeparatorSymbol(numberToWorkWith.abs().compareTo(BigDecimal.ONE) >= 0);

        int scale = numberToWorkWith.scale();
        int precision = numberToWorkWith.precision();

        String pattern;

        if (numberToWorkWith.abs().compareTo(MIN_PLAIN_VALUE) < 0 && scale > MAX_SYMBOLS) {
            pattern = ZERO + DECIMAL_SEPARATOR + PATTERN_15_DIGITS + PATTERN_EXPONENT + ZERO;
        } else {
            int integerPartLength = precision - scale;

            if (integerPartLength > MAX_SYMBOLS) {
                pattern = ZERO + DECIMAL_SEPARATOR;

                if (scale > 0 && scale < MAX_SYMBOLS) {
                    StringBuilder patternBuilder = new StringBuilder(scale);

                    for (int i = 0; i < scale; i++) {
                        patternBuilder.append(ZERO);
                    }

                    pattern += patternBuilder;
                } else {
                    pattern += PATTERN_15_DIGITS;
                }

                pattern += PATTERN_EXPONENT + ZERO;
            } else {
                pattern = PATTERN_SPLIT_GROUP;

                StringBuilder patternBuilder = new StringBuilder(MAX_SYMBOLS - integerPartLength);

                for (int i = 0; i < MAX_SYMBOLS - integerPartLength; i++) {
                    patternBuilder.append(PATTERN_DIGIT);
                }

                pattern += patternBuilder;

                numberToWorkWith = numberToWorkWith.setScale(MAX_SYMBOLS, BigDecimal.ROUND_HALF_UP);
            }
        }

        formatter.applyPattern(pattern);
        formatter.setGroupingUsed(useGrouping);
        return finalFormat(formatter.format(numberToWorkWith), trailingZeros);
    }

    /**
     * Parses string number obtained by formatter to {@code BigDecimal}.
     * <p>
     * Before parsing {@code INTEGER_EXPONENT_SEPARATOR} should be set as exponent separator for {@code DecimalFormatter}
     * if number contains it, otherwise should be {@code DECIMAL_EXPONENT_SEPARATOR}.
     * <p>
     * Examples:
     * <ul>
     *     <li>
     *       {@code parseToBigDecimal("123") == new BigDecimal("123456")}
     *      </li>
     *      <li>
     *       {@code parseToBigDecimal("123456") == new BigDecimal("123456")}; and
     *       {@code parseToBigDecimal("123,456") == new BigDecimal("123456")}
     *      </li>
     *      <li>
     *       {@code parseToBigDecimal("123.123456") == new BigDecimal("123.123456")}
     *      </li>
     *      <li>
     *       {@code parseToBigDecimal("123456.123456") == new BigDecimal("123456.123456")}; and
     *       {@code parseToBigDecimal("123,456.123456") == new BigDecimal("123456.123456")}
     *      </li>
     *      <li>
     *       {@code parseToBigDecimal("1.e+16") == new BigDecimal("1.e+16")}
     *      </li>
     *      <li>
     *       {@code parseToBigDecimal("1.e-16") == new BigDecimal("1.e-16")}
     *      </li>
     *      <li>
     *       {@code parseToBigDecimal("1.23456e+20") == new BigDecimal("1.23456e+20")}
     *      </li>
     *      <li>
     *       {@code parseToBigDecimal("1.23456e-20") == new BigDecimal("1.23456e-20")}
     *      </li>
     * </ul>
     *
     * @param number number to edit.
     * @return edited number if it was necessary to edit.
     * @throws ParseException if impossible to parse number.
     */
    public static BigDecimal parseToBigDecimal(String number) throws ParseException {
        setExponentSeparatorSymbol(number.contains(INTEGER_EXPONENT_SEPARATOR));

        return (BigDecimal) formatter.parse(number);
    }

    /**
     * Sets symbol for exponent separator in decimal formatter.
     *
     * @param isIntegerSeparator true if integer exponent separator should be used or false if decimal.
     */
    private static void setExponentSeparatorSymbol(boolean isIntegerSeparator) {
        symbols.setExponentSeparator(isIntegerSeparator ? INTEGER_EXPONENT_SEPARATOR : DECIMAL_EXPONENT_SEPARATOR);
        formatter.setDecimalFormatSymbols(symbols);
    }

    /**
     * Corrects formatted number.
     * <p>
     * If engineer number is not formatted correctly (no {@code DECIMAL_SEPARATOR} in engineer number), prepends
     * {@code DECIMAL_SEPARATOR} before {@code EXPONENT_SEPARATOR}.
     * <p>
     * If the last symbol of number is {@code DECIMAL_SEPARATOR}, removes it.
     * <p>
     * If {@code GROUPING_SEPARATOR} should not be used, replaces them with empty string.
     * <p>
     * Appends trailing zeros if necessary.
     *
     * @param number        number that was formatted.
     * @param trailingZeros number of trailing zeros that should be appended.
     * @return corrected number if it was necessary to correct.
     */
    private static String finalFormat(String number, int trailingZeros) {
        if (isSecondCharExponent(number)) {
            number = number.replace(DECIMAL_EXPONENT_SEPARATOR,
                    DECIMAL_SEPARATOR + DECIMAL_EXPONENT_SEPARATOR);
        }

        if (isLastDecimalSeparator(number)) {
            number = StringUtils.chop(number);
        }

        if (trailingZeros != 0) {
            StringBuilder zeros = new StringBuilder(trailingZeros);

            for (int i = 0; i < trailingZeros; i++) {
                zeros.append(ZERO);
            }

            if (!number.contains(String.valueOf(DECIMAL_SEPARATOR))) {
                number += DECIMAL_SEPARATOR;
            }

            number += zeros;
        }

        return number;
    }

    /**
     * Checks if second char of unsigned number is {@code EXPONENT_SEPARATOR}.
     *
     * @param formattedNumber formatted number to check.
     * @return true if second char of unsigned number is {@code EXPONENT_SEPARATOR} or false otherwise.
     */
    private static boolean isSecondCharExponent(String formattedNumber) {
        return formattedNumber.matches(SECOND_CHAR_EXPONENT_REGEX);
    }

    /**
     * Checks if last symbol of string is {@code DECIMAL_SEPARATOR}.
     *
     * @param formattedNumber formatted number to check.
     * @return true if last symbol of string is {@code DECIMAL_SEPARATOR} or false otherwise.
     */
    private static boolean isLastDecimalSeparator(String formattedNumber) {
        return formattedNumber.endsWith(String.valueOf(DECIMAL_SEPARATOR));
    }
}
