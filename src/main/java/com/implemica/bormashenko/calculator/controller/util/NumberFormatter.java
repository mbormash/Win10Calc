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
     * Formats number using {@link DecimalFormat}.
     * <p>
     * If number is less than {@code MIN_PLAIN_VALUE} and it's scale is more than {@code MAX_SYMBOLS}, shows number with
     * one-digit integer part and {@code MAX_SYMBOLS}-digits decimal part in engineer representation.
     * <p>
     * The same pattern applied if integer part of number contains more than {@code MAX_SYMBOLS} digits and scale is non
     * positive or more than {@code MAX_SYMBOLS}.
     * <p>
     * If integer part of number contains more than {@code MAX_SYMBOLS} digits and its scale is between 0 and
     * {@code MAX_SYMBOLS}, shows number with one-digit integer part and scale-digits decimal part in engineer
     * representation.
     * <p>
     * If number contains less than {@code MAX_SYMBOLS} or the same number of digits, shows number in usual way with
     * {@code GROUPING_SEPARATOR}.
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
     * If number is engineer, creates new {@code BigDecimal} object using this number.
     * <p>
     * Otherwise, parses it using {@code DecimalFormatter}.
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
