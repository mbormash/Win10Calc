package tests.model;

import com.implemica.bormashenko.calculator.model.Calculation;
import com.implemica.bormashenko.calculator.model.enums.BinaryOperation;
import com.implemica.bormashenko.calculator.model.enums.UnaryOperation;
import com.implemica.bormashenko.calculator.model.exceptions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.implemica.bormashenko.calculator.model.enums.BinaryOperation.*;
import static com.implemica.bormashenko.calculator.model.enums.UnaryOperation.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link Calculation}.
 *
 * @author Mykhailo Bormashenko
 */
class CalculationModelTest {

    /**
     * Regex for all integer numbers not in engineer representation.
     */
    private static final String INTEGER_NUMBER_REGEX = "-?\\d+";

    /**
     * Regex for all decimal numbers not in engineer representation.
     */
    private static final String DECIMAL_NUMBER_REGEX = "-?\\d+\\.\\d+";

    /**
     * Regex for all numbers not engineer representation.
     */
    private static final String ENGINEER_NUMBER_REGEX = "-?\\d\\.\\d*e[+-]\\d+";

    /**
     * Exception message for {@link OverflowException}.
     */
    private static final String OVERFLOW_MESSAGE = "Overflow";

    /**
     * Exception message for divide by zero {@link ArithmeticException}.
     */
    private static final String DIVIDE_BY_ZERO_MESSAGE = "Cannot divide by zero";

    /**
     * Exception message for divide zero by zero {@link ArithmeticException}.
     */
    private static final String DIVIDE_ZERO_BY_ZERO_MESSAGE = "Result is undefined";

    /**
     * Exception message for invalid input {@link ArithmeticException}.
     */
    private static final String INVALID_INPUT_MESSAGE = "Invalid input";

    /**
     * Object of {@link Calculation}.
     */
    private static Calculation calculation;

    /**
     * Setting up an object of {@link Calculation}.
     *
     * @see Calculation
     */
    @BeforeAll
    static void setupObject() {
        calculation = new Calculation();
    }

    /**
     * Test for reset all operation.
     */
    @Test
    void resetAllTests() {
        calculation.setFirst(BigDecimal.ONE);
        calculation.setSecond(BigDecimal.TEN);
        calculation.setBinaryOperation(ADD);

        try {
            calculation.calculateBinary();
        } catch (OverflowException | DivideByZeroException | DivideZeroByZeroException e) {
            fail();
        }

        calculation.resetAll();

        assertEquals(BigDecimal.ZERO, calculation.getFirst());
        assertEquals(BigDecimal.ZERO, calculation.getSecond());
        assertNull(calculation.getBinaryOperation());
    }

    /**
     * Tests for the only add operation.
     *
     * @throws DivideByZeroException     if this exception was thrown during calculation.
     * @throws OverflowException         if this exception was thrown during calculation.
     * @throws DivideZeroByZeroException if this exception was thrown during calculation.
     * @throws NegativeRootException     if this exception was thrown during calculation.
     */
    @Test
    void addOperationTests() throws DivideByZeroException, OverflowException, DivideZeroByZeroException,
            NegativeRootException {
        //easy cases
        checkEquation("2+2=", "4");
        checkEquation("-3+-3=", "-6");
        checkEquation("5.5+3.2=", "8.7");
        checkEquation("-10.2+-7=", "-17.2");
        checkEquation("7.4+-5.1=", "2.3");
        checkEquation("1.e+5+1.e+8=", "1.001e+8");
        checkEquation("1.e-20+5.e-20=", "6.e-20");

        //cases with zero
        checkEquation("0+0=", "0");
        checkEquation("2+0=", "2");
        checkEquation("-3+0=", "-3");
        checkEquation("5.5+0=", "5.5");
        checkEquation("0+-7=", "-7");
        checkEquation("0+-5.1=", "-5.1");
        checkEquation("0+1.e+8=", "1.e+8");

        //big numbers
        checkEquation("10000000000000000+10000000000000000=", "2.e+16");
        checkEquation("5000000000000000+9999999999999999=", "14999999999999999");
        checkEquation("1234567890987654321+1=", "1234567890987654322");
        checkEquation("100000000000000000000000000+10=", "1.0000000000000000000000001e+26");

        //with decimals
        checkEquation("10000000000000000+0.1=", "10000000000000000.1");
        checkEquation("5000000000000000+0.9999999999999999=",
                "5000000000000000.9999999999999999");
        checkEquation("1234567890987654321+123.123=", "1234567890987654444.123");
        checkEquation("100000000000000000000000000+0.00000000000000000000000000001=",
                "100000000000000000000000000.00000000000000000000000000001");

        //decimal and decimal
        checkEquation("0.0000000000001+0.1=", "0.1000000000001");
        checkEquation("0.0000000000000001+0.9999999999999999=", "1");
        checkEquation("1234567890.987654321+123.123=", "1234568014.110654321");
        checkEquation("0.01+0.00000000000000000000000000001=",
                "0.01000000000000000000000000001");

        //boundary
        checkEquation("8.e+9999+1.e+9999=", "9.e+9999");
        checkEquation("9.e+9999+9.e+9998=", "9.9e+9999");
        checkEquation("8.e-9999+1.e-9999=", "9.e-9999");
        checkEquation("9.e-9999+9.e-9998=", "9.9e-9998");

        checkEquation("-8.e+9999+-1.e+9999=", "-9.e+9999");
        checkEquation("-9.e+9999+-9.e+9998=", "-9.9e+9999");
        checkEquation("-8.e-9999+-1.e-9999=", "-9.e-9999");
        checkEquation("-9.e-9999+-9.e-9998=", "-9.9e-9998");

        //several random values
        checkEquation("197+8763=", "8.96e+3");
        checkEquation("36346+62=", "36408");

        checkEquation("62+-542=", "-4.8e+2");
        checkEquation("7654+-62=", "7592");

        checkEquation("-53252+-52=", "-53304");
        checkEquation("-1243+-65=", "-1308");

        checkEquation("623+124.123=", "747.123");
        checkEquation("324+653.523=", "977.523");

        checkEquation("7652+-23.598=", "7628.402");
        checkEquation("2431+-123.124=", "2307.876");

        checkEquation("-62+76.43=", "14.43");
        checkEquation("-87+876.1=", "789.1");

        checkEquation("-63+-0.234=", "-63.234");
        checkEquation("-1967+-22.76=", "-1989.76");

        checkEquation("53.14+51.65=", "104.79");
        checkEquation("75.234+75.234=", "150.468");

        checkEquation("64.26+-25.7=", "38.56");
        checkEquation("623.3+-75.2=", "548.1");

        checkEquation("-532.1+-2.2=", "-534.3");
        checkEquation("-622.2+-25.6=", "-647.8");
    }

    /**
     * Tests for the only subtract operation.
     *
     * @throws DivideByZeroException     if this exception was thrown during calculation.
     * @throws OverflowException         if this exception was thrown during calculation.
     * @throws DivideZeroByZeroException if this exception was thrown during calculation.
     * @throws NegativeRootException     if this exception was thrown during calculation.
     */
    @Test
    void subtractOperationTests() throws DivideByZeroException, OverflowException, DivideZeroByZeroException,
            NegativeRootException {
        //easy cases
        checkEquation("2-2=", "0");
        checkEquation("-3--3=", "0");
        checkEquation("5.5-3.2=", "2.3");
        checkEquation("-10.2--7=", "-3.2");
        checkEquation("-5.1-7.4=", "-12.5");
        checkEquation("1.e+8-1.e+5=", "9.99e+7");
        checkEquation("5.e-20-1.e-20=", "4.e-20");

        //cases with zero
        checkEquation("0-0=", "0");
        checkEquation("2-0=", "2");
        checkEquation("-3-0=", "-3");
        checkEquation("5.5-0=", "5.5");
        checkEquation("0--7=", "7");
        checkEquation("0--5.1=", "5.1");
        checkEquation("0-1.e+8=", "-1.e+8");

        //big numbers
        checkEquation("10000000000000000-10000000000000000=", "0");
        checkEquation("5000000000000000-9999999999999999=", "-4999999999999999");
        checkEquation("1234567890987654321-1=", "1.23456789098765432e+18");
        checkEquation("10-100000000000000000000000000=", "-9.999999999999999999999999e+25");

        //with decimals
        checkEquation("10000000000000000-0.1=", "9999999999999999.9");
        checkEquation("0.9999999999999999-5000000000000000=",
                "-4999999999999999.0000000000000001");
        checkEquation("1234567890987654321-123.123=", "1234567890987654197.877");
        checkEquation("100000000000000000000000000-0.00000000000000000000000000001=",
                "99999999999999999999999999.99999999999999999999999999999");

        //decimal and decimal
        checkEquation("0.0000000000001-0.1=", "-0.0999999999999");
        checkEquation("0.0000000000000001-0.9999999999999999=", "-0.9999999999999998");
        checkEquation("1234567890.987654321-123.123=", "1234567767.864654321");
        checkEquation("0.01-0.00000000000000000000000000001=", "0.00999999999999999999999999999");

        //boundary
        checkEquation("8.e+9999--1.e+9999=", "9.e+9999");
        checkEquation("-1.e+9999-8.e+9999=", "-9.e+9999");

        checkEquation("9.e+9999--9.e+9998=", "9.9e+9999");
        checkEquation("-9.e+9998-9.e+9999=", "-9.9e+9999");

        checkEquation("8.e-9999--1.e-9999=", "9.e-9999");
        checkEquation("-1.e-9999-8.e-9999=", "-9.e-9999");

        checkEquation("9.e-9999--9.e-9998=", "9.9e-9998");
        checkEquation("-9.e-9998-9.e-9999=", "-9.9e-9998");

        //several random values (and vice versa)
        checkEquation("523-876=", "-353");
        checkEquation("65-34=", "31");

        checkEquation("724--652=", "1376");
        checkEquation("-865-763=", "-1628");

        checkEquation("-6521--41=", "-6.48e+3");
        checkEquation("-7245--7624=", "379");

        checkEquation("763-245.876=", "517.124");
        checkEquation("123.87-9876=", "-9752.13");

        checkEquation("6425--123.65=", "6548.65");
        checkEquation("-12.65-987=", "-999.65");

        checkEquation("-6-76.123=", "-82.123");
        checkEquation("12.43--6543=", "6555.43");

        checkEquation("-876--21.41=", "-854.59");
        checkEquation("-12.21--987=", "974.79");

        checkEquation("12.11-87.0765=", "-74.9665");
        checkEquation("1111.09-123.66=", "987.43");

        checkEquation("123.65--1.2=", "124.85");
        checkEquation("-12.7-5.1=", "-17.8");

        checkEquation("-0.112--3.2=", "3.088");
        checkEquation("-1.224--9.1=", "7.876");
    }

    /**
     * Tests for the only multiply operation.
     *
     * @throws DivideByZeroException     if this exception was thrown during calculation.
     * @throws OverflowException         if this exception was thrown during calculation.
     * @throws DivideZeroByZeroException if this exception was thrown during calculation.
     * @throws NegativeRootException     if this exception was thrown during calculation.
     */
    @Test
    void multiplyOperationTests() throws DivideByZeroException, OverflowException, DivideZeroByZeroException,
            NegativeRootException {
        //easy cases
        checkEquation("2*2=", "4");
        checkEquation("-3*-3=", "9");
        checkEquation("5.5*3.2=", "17.6");
        checkEquation("-10.2*-7=", "71.4");
        checkEquation("7.4*-5.1=", "-37.74");
        checkEquation("1.e+5*1.e+8=", "1.e+13");
        checkEquation("1.e-20*5.e-20=", "5.e-40");

        //cases with zero
        checkEquation("0*0=", "0");
        checkEquation("2*0=", "0");
        checkEquation("-3*0=", "0");
        checkEquation("5.5*0=", "0");
        checkEquation("0*-7=", "0");
        checkEquation("0*-5.1=", "0");
        checkEquation("0*1.e+8=", "0");

        //big numbers
        checkEquation("10000000000000000*10000000000000000=", "1.e+32");
        checkEquation("5000000000000000*9999999999999999=", "4.9999999999999995e+31");
        checkEquation("1234567890987654321*1=", "1234567890987654321");
        checkEquation("100000000000000000000000000*10=", "1.e+27");

        //with decimals
        checkEquation("10000000000000000*0.1=", "1.e+15");
        checkEquation("5000000000000000*0.9999999999999999=", "4999999999999999.5");
        checkEquation("1234567890987654321*123.123=", "152003702442072962964.483");
        checkEquation("100000000000000000000000000*0.00000000000000000000000000001=",
                "0.001");

        //decimal and decimal
        checkEquation("0.0000000000001*0.1=", "0.00000000000001");
        checkEquation("0.0000000000000001*0.9999999999999999=",
                "0.00000000000000009999999999999999");
        checkEquation("1234567890.987654321*123.123=", "152003702442.072962964483");
        checkEquation("0.01*0.00000000000000000000000000001=",
                "0.0000000000000000000000000000001");

        //boundary
        checkEquation("4.5e+9999*2=", "9.e+9999");
        checkEquation("9.e+9998*10=", "9.e+9999");
        checkEquation("9.e-9998*0.1=", "9.e-9999");
        checkEquation("4.5e-9999*2=", "9.e-9999");

        checkEquation("-4.5e+9999*2=", "-9.e+9999");
        checkEquation("9.e+9998*-10=", "-9.e+9999");
        checkEquation("9.e-9998*-0.1=", "-9.e-9999");
        checkEquation("-4.5e-9999*2=", "-9.e-9999");

        //several random values
        checkEquation("41*13=", "533");
        checkEquation("64*56=", "3584");

        checkEquation("123*-13=", "-1599");
        checkEquation("41*-65=", "-2665");

        checkEquation("-876*-13=", "11388");
        checkEquation("-54*-53=", "2862");

        checkEquation("12*541.652=", "6499.824");
        checkEquation("9*13.764=", "123.876");

        checkEquation("132*-23.13=", "-3053.16");
        checkEquation("12*-76.87=", "-922.44");

        checkEquation("-65*65.13=", "-4233.45");
        checkEquation("-76*75.123=", "-5709.348");

        checkEquation("-13*-6.12=", "79.56");
        checkEquation("-76*-13.5=", "1026");

        checkEquation("33.12*6.13=", "203.0256");
        checkEquation("86.7*5.132=", "444.9444");

        checkEquation("1.75*-0.1=", "-0.175");
        checkEquation("23.5*-6.87=", "-161.445");

        checkEquation("-765.1*-1.8=", "1377.18");
        checkEquation("-65.7*-7.8=", "512.46");
    }

    /**
     * Tests for the only divide operation.
     *
     * @throws DivideByZeroException     if this exception was thrown during calculation.
     * @throws OverflowException         if this exception was thrown during calculation.
     * @throws DivideZeroByZeroException if this exception was thrown during calculation.
     * @throws NegativeRootException     if this exception was thrown during calculation.
     */
    @Test
    void divideOperationTests() throws DivideByZeroException, OverflowException, DivideZeroByZeroException,
            NegativeRootException {
        //easy cases
        checkEquation("2/2=", "1");
        checkEquation("-3/-3=", "1");
        checkEquation("4/2=", "2");
        checkEquation("-10.2/-102=", "0.1");
        checkEquation("7.4/-0.5=", "-14.8");
        checkEquation("-5.55/1.11=", "-5");
        checkEquation("1.e+5/1.e+8=", "0.001");
        checkEquation("1.e-20/5.e-20=", "0.2");

        //cases with zero
        checkEquation("0/2=", "0");
        checkEquation("0/-3=", "0");
        checkEquation("0/5.5=", "0");
        checkEquation("0/-7=", "0");
        checkEquation("0/-5.1=", "0");
        checkEquation("0/1.e+8=", "0");

        //big numbers
        checkEquation("10000000000000000/-10000000000000000=", "-1");
        checkEquation("8888888888888888/-4444444444444444=", "-2");
        checkEquation("1234567890987654321/1=", "1234567890987654321");
        checkEquation("100000000000000000000000000/10=", "1.e+25");

        //with decimals
        checkEquation("10000000000000000/0.1=", "1.e+17");
        checkEquation("9999999999999999/0.9999999999999999=", "1.e+16");
        checkEquation("1234567890987654321/0.2=", "6172839454938271605");
        checkEquation("100000000000000000000000000/0.00000000000000000000000000001=", "1.e+55");

        //decimal and decimal
        checkEquation("0.0000000000001/0.1=", "0.000000000001");
        checkEquation("-0.00000000000000008/-0.0000000000000001=", "0.8");
        checkEquation("246.246/-123.123=", "-2");
        checkEquation("-0.01/0.00000000000000000000000000001=", "-1.e+27");

        //boundary
        checkEquation("1.e+9998/0.1=", "1.e+9999");
        checkEquation("-0.1/1.e+9998=", "-1.e-9999");

        checkEquation("9.e+9998/0.2=", "4.5e+9999");
        checkEquation("0.2/-1.e+9998=", "-2.e-9999");

        checkEquation("1.e-9998/10=", "1.e-9999");
        checkEquation("-10/1.e-9998=", "-1.e+9999");

        checkEquation("9.e-9998/20=", "4.5e-9999");
        checkEquation("20/-1.e-9998=", "-2.e+9999");

        //several random values
        checkEquation("24/12=", "2");
        checkEquation("415/5=", "83");

        checkEquation("123/-3=", "-41");
        checkEquation("140/-7=", "-2.e+1");

        checkEquation("-41/-41=", "1");
        checkEquation("-651/-6=", "108.5");

        checkEquation("504/2.52=", "2.e+2");
        checkEquation("1001/10.01=", "1.e+2");

        checkEquation("101/-1.01=", "-1.e+2");
        checkEquation("88/-2.2=", "-4.e+1");

        checkEquation("-5342/53.42=", "-1.e+2");
        checkEquation("-100/0.05=", "-2.e+3");

        checkEquation("-65/-6.5=", "1.e+1");
        checkEquation("-123/-1.23=", "1.e+2");

        checkEquation("555.555/555.555=", "1");
        checkEquation("132.5/66.25=", "2");

        checkEquation("65.65/-32.825=", "-2");
        checkEquation("15.12/-0.3=", "-50.4");

        checkEquation("-0.76/-0.001=", "7.6e+2");
        checkEquation("-1061.5/-1.1=", "965");
    }

    /**
     * Tests for equations that contains only binary operations.
     *
     * @throws DivideByZeroException     if this exception was thrown during calculation.
     * @throws OverflowException         if this exception was thrown during calculation.
     * @throws DivideZeroByZeroException if this exception was thrown during calculation.
     * @throws NegativeRootException     if this exception was thrown during calculation.
     */
    @Test
    void severalBinaryOperationsTests() throws DivideByZeroException, OverflowException, DivideZeroByZeroException,
            NegativeRootException {
        //integer numbers
        checkEquation("1+1+1=", "3");
        checkEquation("10-2-2=", "6");
        checkEquation("2*2*2=", "8");
        checkEquation("125/5/5=", "5");

        checkEquation("1000+1245-1455+123=", "913");
        checkEquation("1014+14-28/100=", "1.e+1");
        checkEquation("2*90/45+25-5=", "24");
        checkEquation("125/5*55+12-9=", "1378");

        //integer and decimal
        checkEquation("1.6+1+1=", "3.6");
        checkEquation("10-2.7-2=", "5.3");
        checkEquation("2*2*2.102=", "8.408");
        checkEquation("125.5/5/0.251=", "1.e+2");

        checkEquation("124.126+0.143-0.043*2+5=", "253.452");
        checkEquation("132/2*0.4+1.2=", "27.6");
        checkEquation("564.01-0.1+2.1*500-100=", "282905");
        checkEquation("111*222/2.22*11.5=", "1.2765e+5");

        //decimal numbers
        checkEquation("1.6+6.8+0.2=", "8.6");
        checkEquation("50.5-0.1-0.4=", "5.e+1");
        checkEquation("0.1*4.1*0.2=", "0.082");
        checkEquation("0.1/0.01/0.001=", "1.e+4");

        checkEquation("19481.312+312.124-123.14=", "19670.296");
        checkEquation("777.777/7.77*1.111-5.4=", "105.8111");
        checkEquation("123.123-1.1+1.2*5.2=", "640.7596");
        checkEquation("100.001/0.0001*5.5-1231313.131313=", "4268741.868687");

        //engineers
        checkEquation("1.e+120+5.e+120+7.e+120=", "1.3e+121");
        checkEquation("5.e-6-1.e-6-1.e-2=", "-0.009996");
        checkEquation("7.e+12*2.e+5*2.e-5=", "2.8e+13");
        checkEquation("1.e-500/1.e+1000/1.e+500=", "1.e-2000");

        checkEquation("1.6532e+13*1.41e+134-1.e+147=", "1.331012e+147");
        checkEquation("1.11e-67/2.22e+67*1.e+131=", "0.0005");
        checkEquation("1.67e+120-1.57e+120+1.e+120/5.e+100*2.e-5=", "4.4e+14");
        checkEquation("1.e-50+1.e-100*1.e+50=",
                "1.00000000000000000000000000000000000000000000000001");
    }

    /**
     * Tests for the only negate operation.
     *
     * @throws DivideByZeroException     if this exception was thrown during calculation.
     * @throws OverflowException         if this exception was thrown during calculation.
     * @throws DivideZeroByZeroException if this exception was thrown during calculation.
     * @throws NegativeRootException     if this exception was thrown during calculation.
     */
    @Test
    void negateOperationTests() throws DivideByZeroException, OverflowException, DivideZeroByZeroException,
            NegativeRootException {
        //easy cases
        checkEquation("2 NEG", "-2");
        checkEquation("-3 NEG", "3");
        checkEquation("5.5 NEG", "-5.5");
        checkEquation("-10.2 NEG", "10.2");
        checkEquation("7.4 NEG", "-7.4");
        checkEquation("-1.e+5 NEG", "1.e+5");
        checkEquation("1.e-20 NEG", "-1.e-20");

        //cases with zero
        checkEquation("0 NEG", "0");

        //big numbers
        checkEquation("10000000000000000 NEG", "-1.e+16");
        checkEquation("-5000000000000000 NEG", "5.e+15");
        checkEquation("1234567890987654321 NEG", "-1234567890987654321");
        checkEquation("-100000000000000000000000000 NEG", "1.e+26");

        //decimals
        checkEquation("0.0000000000001 NEG", "-0.0000000000001");
        checkEquation("-0.0000000000000001 NEG", "0.0000000000000001");
        checkEquation("1234567890.987654321 NEG", "-1234567890.987654321");
        checkEquation("-0.00000000000000000000000000009 NEG", "0.00000000000000000000000000009");

        //several random values
        checkEquation("6324 NEG", "-6324");
        checkEquation("987 NEG", "-987");

        checkEquation("-213 NEG", "213");
        checkEquation("-6512 NEG", "6512");

        checkEquation("84.13 NEG", "-84.13");
        checkEquation("98735.8457 NEG", "-98735.8457");

        checkEquation("-234.123 NEG", "234.123");
        checkEquation("-6434.213 NEG", "6434.213");
    }

    /**
     * Tests for the only square operation.
     *
     * @throws DivideByZeroException     if this exception was thrown during calculation.
     * @throws OverflowException         if this exception was thrown during calculation.
     * @throws DivideZeroByZeroException if this exception was thrown during calculation.
     * @throws NegativeRootException     if this exception was thrown during calculation.
     */
    @Test
    void sqrOperationTests() throws DivideByZeroException, OverflowException, DivideZeroByZeroException,
            NegativeRootException {
        //easy cases
        checkEquation("2 SQR", "4");
        checkEquation("-3 SQR", "9");
        checkEquation("5.5 SQR", "30.25");
        checkEquation("-10.2 SQR", "104.04");
        checkEquation("7.4 SQR", "54.76");
        checkEquation("-1.e+5 SQR", "1.e+10");
        checkEquation("1.e-20 SQR", "1.e-40");

        //cases with zero
        checkEquation("0 SQR", "0");

        //big numbers
        checkEquation("10000000000000000 SQR", "1.e+32");
        checkEquation("-5000000000000000 SQR", "2.5e+31");
        checkEquation("1234567890987654321 SQR", "1524157877457704723228166437789971041");
        checkEquation("-100000000000000000000000000 SQR", "1.e+52");

        //decimals
        checkEquation("0.0000000000001 SQR", "1.e-26");
        checkEquation("-0.0000000000000001 SQR", "1.e-32");
        checkEquation("1234567890.987654321 SQR", "1524157877457704723.228166437789971041");
        checkEquation("-0.00000000000000000000000000009 SQR", "8.1e-57");

        //boundary
        checkEquation("1.e+4999 SQR", "1.e+9998");
        checkEquation("-9.e+4999 SQR", "8.1e+9999");
        checkEquation("1.e-4999 SQR", "1.e-9998");
        checkEquation("-9.e-4999 SQR", "8.1e-9997");

        //several random values
        checkEquation("743 SQR", "552049");
        checkEquation("324 SQR", "104976");

        checkEquation("-87634 SQR", "7679717956");
        checkEquation("-234 SQR", "54756");

        checkEquation("987.12 SQR", "974405.8944");
        checkEquation("1.2 SQR", "1.44");

        checkEquation("-31.62 SQR", "999.8244");
        checkEquation("-65.123 SQR", "4241.005129");
    }

    /**
     * Tests for the only square root operation.
     *
     * @throws DivideByZeroException     if this exception was thrown during calculation.
     * @throws OverflowException         if this exception was thrown during calculation.
     * @throws DivideZeroByZeroException if this exception was thrown during calculation.
     * @throws NegativeRootException     if this exception was thrown during calculation.
     */
    @Test
    void sqrtOperationTests() throws DivideByZeroException, OverflowException, DivideZeroByZeroException,
            NegativeRootException {
        //easy cases
        checkEquation("4 ROOT", "2");
        checkEquation("9 ROOT", "3");
        checkEquation("100 ROOT", "1.e+1");
        checkEquation("1.21 ROOT", "1.1");
        checkEquation("0.81 ROOT", "0.9");
        checkEquation("1.e+5 ROOT", "316.2277660168379");
        checkEquation("1.e-20 ROOT", "1.e-10");

        //cases with zero
        checkEquation("0 ROOT", "0");

        //big numbers
        checkEquation("10000000000000000 ROOT", "1.e+8");
        checkEquation("5000000000000000 ROOT", "70710678.11865475");
        checkEquation("1234567890987654 ROOT", "35136418.30049918");
        checkEquation("100000000000000000000000000 ROOT", "1.e+13");

        //decimals
        checkEquation("0.0000000000001 ROOT", "3.162277660168379e-7");
        checkEquation("0.0000000000000001 ROOT", "0.00000001");
        checkEquation("1234567890.987654 ROOT", "35136.41830049918");
        checkEquation("0.0000000000000000000000000009 ROOT", "3.e-14");

        //several random values
        checkEquation("9132131 ROOT", "3021.941594405822");
        checkEquation("1235123 ROOT", "1111.360877483097");

        checkEquation("123.5523 ROOT", "11.11540822462225");
        checkEquation("123.1243 ROOT", "11.09613896812761");

        checkEquation("7373 ROOT", "85.86617494683224");
        checkEquation("123198743 ROOT", "11099.49291634532");

        checkEquation("312.5632 ROOT", "17.67945700523633");
        checkEquation("123.87624525 ROOT", "11.1299705862145");
    }

    /**
     * Tests for the only inverse operation.
     *
     * @throws DivideByZeroException     if this exception was thrown during calculation.
     * @throws OverflowException         if this exception was thrown during calculation.
     * @throws DivideZeroByZeroException if this exception was thrown during calculation.
     * @throws NegativeRootException     if this exception was thrown during calculation.
     */
    @Test
    void inverseOperationTests() throws DivideByZeroException, OverflowException, DivideZeroByZeroException,
            NegativeRootException {
        //easy cases
        checkEquation("2 INVERSE", "0.5");
        checkEquation("0.5 INVERSE", "2");

        checkEquation("-100 INVERSE", "-0.01");
        checkEquation("-0.01 INVERSE", "-1.e+2");

        checkEquation("1.e+5 INVERSE", "1.e-5");
        checkEquation("1.e+5 INVERSE", "0.00001");

        //big numbers
        checkEquation("10000000000000000 INVERSE", "1.e-16");
        checkEquation("0.0000000000000001 INVERSE", "1.e+16");

        checkEquation("-5000000000000000 INVERSE", "-2.e-16");
        checkEquation("-0.00000000000000002 INVERSE", "-5.e+16");

        checkEquation("-100000000000000000000000000 INVERSE", "-1.e-26");
        checkEquation("-0.00000000000000000000000001 INVERSE", "-1.e+26");

        checkEquation("0.00000000000000000000000000008 INVERSE", "1.25E+28");
        checkEquation("12500000000000000000000000000 INVERSE", "0.00000000000000000000000000008");

        //boundary
        checkEquation("1.e+9999 INVERSE", "1.e-9999");
        checkEquation("-2.e+9998 INVERSE", "-5.e-9999");

        checkEquation("1.e-9999 INVERSE", "1.e+9999");
        checkEquation("-2.e-9999 INVERSE", "-5.e+9998");

        //several random values
        checkEquation("50 INVERSE", "0.02");
        checkEquation("2 INVERSE", "0.5");

        checkEquation("-800 INVERSE", "-0.00125");
        checkEquation("-1000 INVERSE", "-0.001");

        checkEquation("0.5 INVERSE", "2");
        checkEquation("0.8 INVERSE", "1.25");

        checkEquation("-0.025 INVERSE", "-4.e+1");
        checkEquation("-6.25 INVERSE", "-0.16");
    }

    /**
     * Tests for equations that contains only unary operations.
     *
     * @throws DivideByZeroException     if this exception was thrown during calculation.
     * @throws OverflowException         if this exception was thrown during calculation.
     * @throws DivideZeroByZeroException if this exception was thrown during calculation.
     * @throws NegativeRootException     if this exception was thrown during calculation.
     */
    @Test
    void severalUnaryOperationTests() throws DivideByZeroException, OverflowException, DivideZeroByZeroException,
            NegativeRootException {
        //integer numbers
        checkEquation("1 NEG NEG", "1");
        checkEquation("10 SQR SQR SQR", "1.e+8");
        checkEquation("256 ROOT ROOT ROOT", "2");
        checkEquation("125 INVERSE INVERSE", "125");

        checkEquation("1000 NEG SQR ROOT", "1.e+3");
        checkEquation("1014 SQR SQR NEG", "-1057187014416");
        checkEquation("2 NEG NEG NEG SQR SQR INVERSE", "0.0625");
        checkEquation("125 ROOT SQR", "125.0000000000000339424862511025");

        //decimal numbers
        checkEquation("1.6 NEG NEG NEG NEG NEG", "-1.6");
        checkEquation("50.5 SQR SQR", "6503775.0625");
        checkEquation("0.1 ROOT ROOT ROOT", "0.7498942093324558");
        checkEquation("0.1 INVERSE INVERSE INVERSE", "1.e+1");

        checkEquation("19481.312 SQR ROOT SQR ROOT", "19481.312");
        checkEquation("777.777 INVERSE INVERSE SQR ROOT NEG", "-777.777");
        checkEquation("123.123 NEG NEG NEG SQR", "15159.273129");
        checkEquation("200.002 INVERSE INVERSE NEG", "-200.00200000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000002");

        //engineers
        checkEquation("1.e+120 NEG NEG", "1.e+120");
        checkEquation("5.e-6 SQR SQR SQR SQR", "1.52587890625E-85");
        checkEquation("1.6e+13 ROOT ROOT", "2.e+3");
        checkEquation("1.e-500 INVERSE INVERSE INVERSE INVERSE", "1.e-500");

        checkEquation("1.6532e+13 NEG NEG ROOT SQR", "16531999999999.997751328808181025");
        checkEquation("1.11e-67 INVERSE INVERSE SQR", "1.2321e-134");
        checkEquation("1.67e+120 ROOT SQR NEG", "-1.670000000000001171108593976081e+120");
        checkEquation("1.e-50 NEG NEG SQR", "1.e-100");
    }

    /**
     * Tests for the only percentage of first operation.
     *
     * @throws DivideByZeroException     if this exception was thrown during calculation.
     * @throws OverflowException         if this exception was thrown during calculation.
     * @throws DivideZeroByZeroException if this exception was thrown during calculation.
     * @throws NegativeRootException     if this exception was thrown during calculation.
     */
    @Test
    void percentageOfFirstTests() throws DivideByZeroException, OverflowException, DivideZeroByZeroException,
            NegativeRootException {
        //easy cases
        checkEquation("100+100%", "1.e+2");
        checkEquation("50+-100%", "-5.e+1");
        checkEquation("100+15%", "15");
        checkEquation("10.1--5%", "-0.505");
        checkEquation("75.276-67.2%", "50.585472");
        checkEquation("1.e+5-1.e+8%", "1.e+11");
        checkEquation("1.e-20-5.e-20%", "5.e-42");

        //cases with zero
        checkEquation("0+0%", "0");
        checkEquation("2+0%", "0");
        checkEquation("-3+0%", "0");
        checkEquation("5.5+0%", "0");
        checkEquation("0--7%", "0");
        checkEquation("0--5.1%", "0");
        checkEquation("0-1.e+8%", "0");

        //big numbers
        checkEquation("10000000000000000+10000000000000000%", "1.e+30");
        checkEquation("5000000000000000-9999999999999999%", "4.9999999999999995e+29");
        checkEquation("1234567890987654321+1%", "12345678909876543.21");
        checkEquation("100000000000000000000000000-10%", "1.e+25");

        //with decimals
        checkEquation("10000000000000000-0.1%", "1.e+13");
        checkEquation("5000000000000000+0.9999999999999999%", "49999999999999.995");
        checkEquation("1234567890987654321-123.123%", "1520037024420729629.64483");
        checkEquation("100000000000000000000000000+0.00000000000000000000000000001%", "0.00001");

        //decimal and decimal
        checkEquation("0.0000000000001-0.1%", "1.e-16");
        checkEquation("0.0000000000000001-0.9999999999999999%", "9.999999999999999e-19");
        checkEquation("1234567890.987654321+123.123%", "1520037024.42072962964483");
        checkEquation("0.01+0.00000000000000000000000000001%", "1.e-33");

        //boundary
        checkEquation("9.e+9997+10000%", "9.e+9999");
        checkEquation("-9.e+9999+-100%", "9.e+9999");
        checkEquation("9.e-9997-1%", "9.e-9999");
        checkEquation("-9.e-9999--100%", "9.e-9999");

        checkEquation("-9.e+9997-10000%", "-9.e+9999");
        checkEquation("9.e+9999--100%", "-9.e+9999");
        checkEquation("-9.e-9997+1%", "-9.e-9999");
        checkEquation("9.e-9999+-100%", "-9.e-9999");

        //several random values
        checkEquation("14-51%", "7.14");
        checkEquation("6523+123%", "8023.29");

        checkEquation("123+-31%", "-38.13");
        checkEquation("874--41%", "-358.34");

        checkEquation("-31--1321%", "409.51");
        checkEquation("-132+-52%", "68.64");

        checkEquation("51354-132.12%", "67848.9048");
        checkEquation("54+21.4%", "11.556");

        checkEquation("221--123.512%", "-272.96152");
        checkEquation("54+-2135.13%", "-1152.9702");

        checkEquation("-12+1.2%", "-0.144");
        checkEquation("-87-23.61%", "-20.5407");

        checkEquation("-65+-10.11%", "6.5715");
        checkEquation("-324+-12.31%", "39.8844");

        checkEquation("21.12-7.6%", "1.60512");
        checkEquation("5132.123-24.24%", "1244.0266152");

        checkEquation("42.64--67.8%", "-28.90992");
        checkEquation("31.31--34.5%", "-10.80195");

        checkEquation("-74.1+-2.23%", "1.65243");
        checkEquation("-7.2+-23.53%", "1.69416");
    }

    /**
     * Tests for the only percentage of 100 operation.
     *
     * @throws DivideByZeroException     if this exception was thrown during calculation.
     * @throws OverflowException         if this exception was thrown during calculation.
     * @throws DivideZeroByZeroException if this exception was thrown during calculation.
     * @throws NegativeRootException     if this exception was thrown during calculation.
     */
    @Test
    void percentageOf100Tests() throws DivideByZeroException, OverflowException, DivideZeroByZeroException,
            NegativeRootException {
        //easy cases
        checkEquation("100*%", "1");
        checkEquation("-10*%", "-0.1");
        checkEquation("50*%", "0.5");
        checkEquation("-789*%", "-7.89");
        checkEquation("5.5/%", "0.055");
        checkEquation("-10.2/%", "-0.102");
        checkEquation("7.4/%", "0.074");
        checkEquation("-1.e+5/%", "-1.e+3");
        checkEquation("1.e-20/%", "1.e-22");

        //cases with zero
        checkEquation("0*%", "0");

        //big numbers
        checkEquation("10000000000000000*%", "1.e+14");
        checkEquation("-5000000000000000*%", "-5.e+13");
        checkEquation("1234567890987654321/%", "12345678909876543.21");
        checkEquation("-100000000000000000000000000/%", "-1.e+24");

        //decimals
        checkEquation("0.0000000000001*%", "0.000000000000001");
        checkEquation("-0.0000000000000001*%", "-0.000000000000000001");
        checkEquation("1234567890.987654321/%", "12345678.90987654321");
        checkEquation("-0.00000000000000000000000000009/%", "-0.0000000000000000000000000000009");

        //boundary
        checkEquation("9.e+9999*%", "9.e+9997");
        checkEquation("1.e-9997*%", "1.e-9999");
        checkEquation("-9.e-9997/%", "-9.e-9999");

        //several random values
        checkEquation("73*%", "0.73");
        checkEquation("8734/%", "87.34");

        checkEquation("-42*%", "-0.42");
        checkEquation("-876/%", "-8.76");

        checkEquation("2423.73/%", "24.2373");
        checkEquation("87.234*%", "0.87234");

        checkEquation("-123.623/%", "-1.23623");
        checkEquation("-432.62*%", "-4.3262");
    }

    /**
     * Test for percentage operation while {@code BinaryOperation} in {@code Calculation} is set to null.
     */
    @Test
    void percentageForBinaryNull() {
        calculation.setBinaryOperation(null);
        calculation.setFirst(BigDecimal.ONE);

        try {
            calculation.calculatePercentage(BigDecimal.TEN);
        } catch (OverflowException e) {
            fail();
        }

        assertEquals(BigDecimal.ZERO, calculation.getFirst());
        assertEquals(BigDecimal.ZERO, calculation.getSecond());
    }

    /**
     * Tests for equations that contain binary, unary and percent operations.
     *
     * @throws DivideByZeroException     if this exception was thrown during calculation.
     * @throws OverflowException         if this exception was thrown during calculation.
     * @throws DivideZeroByZeroException if this exception was thrown during calculation.
     * @throws NegativeRootException     if this exception was thrown during calculation.
     */
    @Test
    void allOperationsTests() throws DivideByZeroException, OverflowException, DivideZeroByZeroException,
            NegativeRootException {
        checkEquation("2 + 2 SQR =", "6");
        checkEquation("5 SQR + 9 ROOT =", "28");
        checkEquation("10 SQR ROOT + 15 =", "25");
        checkEquation("5 INVERSE + 4 NEG = SQR", "14.44");
        checkEquation("9 - 9 ROOT =", "6");

        checkEquation("0.1 * 25 INVERSE SQR =", "0.00016");
        checkEquation("0.5 NEG NEG NEG * 45.5 =", "-22.75");
        checkEquation("0.0001 SQR INVERSE ROOT + 0.0001 SQR INVERSE NEG =", "-9.999e+7");
        checkEquation("5 / 0.04 ROOT = INVERSE", "0.04");
        checkEquation("0.1 INVERSE / 2 SQR =", "2.5");

        checkEquation("1.e+100 * 1.e+500 SQR =", "1.e+1100");
        checkEquation("1.e+100 ROOT + =", "2.e+50");
        checkEquation("1.e+100 - 1.e+100 NEG =", "2.e+100");
        checkEquation("1.e-100 INVERSE INVERSE * 4.e+400 ROOT =", "2.e+100");
        checkEquation("1.e-1000 ROOT / 1.e-500 INVERSE =", "1.e-1000");

        checkEquation("5 + 5% =", "5.25");
        checkEquation("5 - 10 SQR SQR % =", "-495");
        checkEquation("25 INVERSE * 100 % =", "0.04");
        checkEquation("100 ROOT / 10 % + 5 =", "105");

        checkEquation("5 ROOT SQR %", "0");
    }

    /**
     * Tests for exceptions for equations that contain binary, unary and percent operations.
     */
    @Test
    void allOperationsExceptionTests() {
        checkEquationException("2 + 2 NEG ROOT =", INVALID_INPUT_MESSAGE);
        checkEquationException("0 INVERSE SQR + 9 ROOT =", DIVIDE_BY_ZERO_MESSAGE);

        checkEquationException("0.1 * 25 INVERSE SQR NEG = ROOT", INVALID_INPUT_MESSAGE);
        checkEquationException("0.5 NEG NEG NEG + 0.5 / 0 ROOT =", DIVIDE_ZERO_BY_ZERO_MESSAGE);

        checkEquationException("1.e+100 * 1.e+100 SQR SQR SQR SQR SQR SQR SQR =", OVERFLOW_MESSAGE);
        checkEquationException("1.e-100 INVERSE INVERSE SQR SQR SQR SQR SQR SQR SQR SQR SQR SQR - 100 =", OVERFLOW_MESSAGE);
    }

    /**
     * Tests for {@code OverflowException} while using {@code BinaryOperation}.
     */
    @Test
    void binaryOverflowExceptionTests() {
        //add operation
        checkEquationException("9.e+9999+9.e+9999=", OVERFLOW_MESSAGE);
        checkEquationException("9.e+9999+1.e+9999=", OVERFLOW_MESSAGE);
        checkEquationException("8.e+9999+2.e+9999=", OVERFLOW_MESSAGE);

        checkEquationException("-9.e+9999+-9.e+9999=", OVERFLOW_MESSAGE);
        checkEquationException("-9.e+9999+-1.e+9999=", OVERFLOW_MESSAGE);
        checkEquationException("-8.e+9999+-2.e+9999=", OVERFLOW_MESSAGE);

        //subtract operation
        checkEquationException("9.e+9999--9.e+9999=", OVERFLOW_MESSAGE);
        checkEquationException("-9.e+9999-9.e+9999=", OVERFLOW_MESSAGE);

        checkEquationException("9.e+9999--1.e+9999=", OVERFLOW_MESSAGE);
        checkEquationException("-1.e+9999-9.e+9999=", OVERFLOW_MESSAGE);

        checkEquationException("8.e+9999--2.e+9999=", OVERFLOW_MESSAGE);
        checkEquationException("-2.e+9999-8.e+9999=", OVERFLOW_MESSAGE);

        checkEquationException("-9.e+9999-9.e+9999=", OVERFLOW_MESSAGE);
        checkEquationException("9.e+9999--9.e+9999=", OVERFLOW_MESSAGE);

        checkEquationException("-9.e+9999-1.e+9999=", OVERFLOW_MESSAGE);
        checkEquationException("1.e+9999--9.e+9999=", OVERFLOW_MESSAGE);

        checkEquationException("-8.e+9999-2.e+9999=", OVERFLOW_MESSAGE);
        checkEquationException("2.e+9999--8.e+9999=", OVERFLOW_MESSAGE);

        //multiply operation
        checkEquationException("1.e+9999*-1.e+9999=", OVERFLOW_MESSAGE);
        checkEquationException("1.e+9999*1.e+9999=", OVERFLOW_MESSAGE);

        checkEquationException("1.e+9999*-10=", OVERFLOW_MESSAGE);
        checkEquationException("1.e+9999*10=", OVERFLOW_MESSAGE);

        checkEquationException("1.e+5000*-1.e+5000=", OVERFLOW_MESSAGE);
        checkEquationException("1.e+5000*1.e+5000=", OVERFLOW_MESSAGE);

        checkEquationException("-1.e+9999*-10=", OVERFLOW_MESSAGE);
        checkEquationException("-1.e+9999*10=", OVERFLOW_MESSAGE);

        checkEquationException("-1.e+9999*-1.e+9999=", OVERFLOW_MESSAGE);
        checkEquationException("-1.e+9999*1.e+9999=", OVERFLOW_MESSAGE);

        checkEquationException("-1.e+5000*-1.e+5000=", OVERFLOW_MESSAGE);
        checkEquationException("-1.e+5000*1.e+5000=", OVERFLOW_MESSAGE);


        checkEquationException("1.e-9999*-1.e-9999=", OVERFLOW_MESSAGE);
        checkEquationException("1.e-9999*1.e-9999=", OVERFLOW_MESSAGE);

        checkEquationException("1.e-9999*-0.1=", OVERFLOW_MESSAGE);
        checkEquationException("1.e-9999*0.1=", OVERFLOW_MESSAGE);

        checkEquationException("1.e-5000*-1.e-5000=", OVERFLOW_MESSAGE);
        checkEquationException("1.e-5000*1.e-5000=", OVERFLOW_MESSAGE);

        checkEquationException("-1.e-9999*-0.1=", OVERFLOW_MESSAGE);
        checkEquationException("-1.e-9999*0.1=", OVERFLOW_MESSAGE);

        checkEquationException("-1.e-9999*-1.e-9999=", OVERFLOW_MESSAGE);
        checkEquationException("-1.e-9999*1.e-9999=", OVERFLOW_MESSAGE);

        checkEquationException("-1.e-5000*-1.e-5000=", OVERFLOW_MESSAGE);
        checkEquationException("-1.e-5000*1.e-5000=", OVERFLOW_MESSAGE);

        //divide operation
        checkEquationException("1.e+9999/-1.e-9999=", OVERFLOW_MESSAGE);
        checkEquationException("1.e+9999/1.e-9999=", OVERFLOW_MESSAGE);

        checkEquationException("1.e+9999/-0.1=", OVERFLOW_MESSAGE);
        checkEquationException("1.e+9999/0.1=", OVERFLOW_MESSAGE);

        checkEquationException("1.e+5000/-1.e-5000=", OVERFLOW_MESSAGE);
        checkEquationException("1.e+5000/1.e-5000=", OVERFLOW_MESSAGE);

        checkEquationException("-1.e+9999/-1.e-9999=", OVERFLOW_MESSAGE);
        checkEquationException("-1.e+9999/1.e-9999=", OVERFLOW_MESSAGE);

        checkEquationException("-1.e+9999/-0.1=", OVERFLOW_MESSAGE);
        checkEquationException("-1.e+9999/0.1=", OVERFLOW_MESSAGE);

        checkEquationException("-1.e+5000/-1.e-5000=", OVERFLOW_MESSAGE);
        checkEquationException("-1.e+5000/1.e-5000=", OVERFLOW_MESSAGE);


        checkEquationException("1.e-9999/-1.e+9999=", OVERFLOW_MESSAGE);
        checkEquationException("1.e-9999/1.e+9999=", OVERFLOW_MESSAGE);

        checkEquationException("1.e-9999/-10=", OVERFLOW_MESSAGE);
        checkEquationException("1.e-9999/10=", OVERFLOW_MESSAGE);

        checkEquationException("1.e-5000/-1.e+5000=", OVERFLOW_MESSAGE);
        checkEquationException("1.e-5000/1.e+5000=", OVERFLOW_MESSAGE);

        checkEquationException("-1.e-9999/-1.e+9999=", OVERFLOW_MESSAGE);
        checkEquationException("-1.e-9999/1.e+9999=", OVERFLOW_MESSAGE);

        checkEquationException("-1.e-9999/-10=", OVERFLOW_MESSAGE);
        checkEquationException("-1.e-9999/10=", OVERFLOW_MESSAGE);

        checkEquationException("-1.e-5000/-1.e+5000=", OVERFLOW_MESSAGE);
        checkEquationException("-1.e-5000/1.e+5000=", OVERFLOW_MESSAGE);
    }

    /**
     * Tests for {@code OverflowException} while using the only {@code UnaryOperation.SQR} operation.
     */
    @Test
    void sqrOverflowExceptionTests() {
        checkEquationException("1.e+9999 SQR", OVERFLOW_MESSAGE);
        checkEquationException("1.e+5000 SQR", OVERFLOW_MESSAGE);

        checkEquationException("-1.e+9999 SQR", OVERFLOW_MESSAGE);
        checkEquationException("-1.e+5000 SQR", OVERFLOW_MESSAGE);

        checkEquationException("1.e-9999 SQR", OVERFLOW_MESSAGE);
        checkEquationException("1.e-5000 SQR", OVERFLOW_MESSAGE);

        checkEquationException("-1.e-9999 SQR", OVERFLOW_MESSAGE);
        checkEquationException("-1.e-5000 SQR", OVERFLOW_MESSAGE);

        checkEquationException("10 SQR SQR SQR SQR SQR SQR SQR SQR SQR SQR SQR SQR SQR SQR", OVERFLOW_MESSAGE);
        checkEquationException("-10 SQR SQR SQR SQR SQR SQR SQR SQR SQR SQR SQR SQR SQR SQR", OVERFLOW_MESSAGE);
    }

    /**
     * Tests for {@code OverflowException} while using the only percentage of first operation.
     */
    @Test
    void percentageOfFirstOverflowExceptionTests() {
        //e+
        checkEquationException("1.e+9999+-1.e+9999%", OVERFLOW_MESSAGE);
        checkEquationException("1.e+9999-1.e+9999%", OVERFLOW_MESSAGE);

        checkEquationException("1.e+9999+-1000%", OVERFLOW_MESSAGE);
        checkEquationException("1.e+9999-1000%", OVERFLOW_MESSAGE);

        checkEquationException("1.e+5001--1.e+5001%", OVERFLOW_MESSAGE);
        checkEquationException("1.e+5001+1.e+5001%", OVERFLOW_MESSAGE);

        //-e+
        checkEquationException("-1.e+9999--1.e+9999%", OVERFLOW_MESSAGE);
        checkEquationException("-1.e+9999+1.e+9999%", OVERFLOW_MESSAGE);

        checkEquationException("-1.e+9999+-1000%", OVERFLOW_MESSAGE);
        checkEquationException("-1.e+9999+1000%", OVERFLOW_MESSAGE);

        checkEquationException("-1.e+5001--1.e+5001%", OVERFLOW_MESSAGE);
        checkEquationException("-1.e+5001-1.e+5001%", OVERFLOW_MESSAGE);

        //e-
        checkEquationException("1.e-9999+-1.e-9999%", OVERFLOW_MESSAGE);
        checkEquationException("1.e-9999-1.e-9999%", OVERFLOW_MESSAGE);

        checkEquationException("1.e-9999+-0.0001%", OVERFLOW_MESSAGE);
        checkEquationException("1.e-9999-0.0001%", OVERFLOW_MESSAGE);

        checkEquationException("1.e-5001+-1.e-5001%", OVERFLOW_MESSAGE);
        checkEquationException("1.e-5001-1.e-5001%", OVERFLOW_MESSAGE);

        //-e-
        checkEquationException("-1.e-9999--1.e-9999%", OVERFLOW_MESSAGE);
        checkEquationException("-1.e-9999+1.e-9999%", OVERFLOW_MESSAGE);

        checkEquationException("-1.e-9999+-0.0001%", OVERFLOW_MESSAGE);
        checkEquationException("-1.e-9999+0.0001%", OVERFLOW_MESSAGE);

        checkEquationException("-1.e-5001--1.e-5001%", OVERFLOW_MESSAGE);
        checkEquationException("-1.e-5001-1.e-5001%", OVERFLOW_MESSAGE);
    }

    /**
     * Tests for {@code OverflowException} while using the only percentage of 100 operation.
     */
    @Test
    void percentageOf100OverFlowExceptionTests() {
        checkEquationException("-1.e-9999*%", OVERFLOW_MESSAGE);
        checkEquationException("-1.e-9998/%", OVERFLOW_MESSAGE);

        checkEquationException("1.e-9998/%", OVERFLOW_MESSAGE);
        checkEquationException("1.e-9999*%", OVERFLOW_MESSAGE);
    }

    /**
     * Tests for divide by zero exception.
     */
    @Test
    void divideByZeroExceptionTests() {
        checkEquationException("1/0=", DIVIDE_BY_ZERO_MESSAGE);
        checkEquationException("2/0=", DIVIDE_BY_ZERO_MESSAGE);
        checkEquationException("10000000000000000/0=", DIVIDE_BY_ZERO_MESSAGE);
        checkEquationException("-1/0=", DIVIDE_BY_ZERO_MESSAGE);
        checkEquationException("-2/0=", DIVIDE_BY_ZERO_MESSAGE);
        checkEquationException("-10000000000000000/0=", DIVIDE_BY_ZERO_MESSAGE);

        checkEquationException("0.1/0=", DIVIDE_BY_ZERO_MESSAGE);
        checkEquationException("0.9/0=", DIVIDE_BY_ZERO_MESSAGE);
        checkEquationException("0.9999999999999999/0=", DIVIDE_BY_ZERO_MESSAGE);
        checkEquationException("-0.1/0=", DIVIDE_BY_ZERO_MESSAGE);
        checkEquationException("-0.9/0=", DIVIDE_BY_ZERO_MESSAGE);
        checkEquationException("-0.9999999999999999/0=", DIVIDE_BY_ZERO_MESSAGE);

        checkEquationException("1.e+9999/0=", DIVIDE_BY_ZERO_MESSAGE);
        checkEquationException("1.e+9998/0=", DIVIDE_BY_ZERO_MESSAGE);
        checkEquationException("-1.e+9999/0=", DIVIDE_BY_ZERO_MESSAGE);
        checkEquationException("-1.e+9998/0=", DIVIDE_BY_ZERO_MESSAGE);

        checkEquationException("1.e-9999/0=", DIVIDE_BY_ZERO_MESSAGE);
        checkEquationException("1.e-9998/0=", DIVIDE_BY_ZERO_MESSAGE);
        checkEquationException("-1.e-9999/0=", DIVIDE_BY_ZERO_MESSAGE);
        checkEquationException("-1.e-9998/0=", DIVIDE_BY_ZERO_MESSAGE);
    }

    /**
     * Tests for {@code UnaryOperation.SQRT} of negative number exception.
     */
    @Test
    void negativeSqrtExceptionTests() {
        checkEquationException("-1 ROOT", INVALID_INPUT_MESSAGE);
        checkEquationException("-9 ROOT", INVALID_INPUT_MESSAGE);
        checkEquationException("-10000000000000000 ROOT", INVALID_INPUT_MESSAGE);

        checkEquationException("-0.1 ROOT", INVALID_INPUT_MESSAGE);
        checkEquationException("-0.9 ROOT", INVALID_INPUT_MESSAGE);
        checkEquationException("-0.9999999999999999 ROOT", INVALID_INPUT_MESSAGE);

        checkEquationException("-1.e+9998 ROOT", INVALID_INPUT_MESSAGE);
        checkEquationException("-1.e+9999 ROOT", INVALID_INPUT_MESSAGE);

        checkEquationException("-1.e-9999 ROOT", INVALID_INPUT_MESSAGE);
        checkEquationException("-1.e-9998 ROOT", INVALID_INPUT_MESSAGE);
    }

    /**
     * Test for divide zero by zero exception.
     */
    @Test
    void divideZeroByZeroExceptionTest() {
        calculation.setFirst(BigDecimal.ZERO);
        calculation.setSecond(BigDecimal.ZERO);
        calculation.setBinaryOperation(DIVIDE);

        try {
            calculation.calculateBinary();
            fail();
        } catch (OverflowException | DivideByZeroException e) {
            fail();
        } catch (DivideZeroByZeroException e) {
            assertEquals(DIVIDE_ZERO_BY_ZERO_MESSAGE, e.getMessage());
        }
    }

    /**
     * Tests for inverse by zero exception.
     */
    @Test
    void inverseZeroExceptionTest() {
        try {
            calculation.calculateUnary(BigDecimal.ZERO, INVERSE);
            fail();
        } catch (OverflowException | NegativeRootException e) {
            fail();
        } catch (DivideByZeroException e) {
            assertEquals(DIVIDE_BY_ZERO_MESSAGE, e.getMessage());
        }
    }

    /**
     * Tests for bounds for {@link OverflowException}.
     *
     * @throws DivideByZeroException     if this exception was thrown during calculation.
     * @throws OverflowException         if this exception was thrown during calculation.
     * @throws DivideZeroByZeroException if this exception was thrown during calculation.
     * @throws NegativeRootException     if this exception was thrown during calculation.
     */
    @Test
    void boundaryTests() throws DivideByZeroException, OverflowException, DivideZeroByZeroException,
            NegativeRootException {
        StringBuilder veryBigNumbers = new StringBuilder();

        //integer part with 10000 nines
        for (int i = 0; i < 10000; i++) {
            veryBigNumbers.append("9");
        }

        veryBigNumbers.append(".");

        //decimal part with 9998 nines
        for (int i = 0; i < 9998; i++) {
            veryBigNumbers.append("9");
        }

        String theVeryRightNumberBeforeTheVeryRightNumber = veryBigNumbers + "8";
        String theVeryRightNumber = veryBigNumbers + "9";
        String theVeryLeftNumberBeforeTheVeryLeftNumber = "2.e-9999";
        String theVeryLeftNumber = "1.e-9999";

        //max numbers
        //right bound
        checkEquation(theVeryRightNumberBeforeTheVeryRightNumber + "+" + theVeryLeftNumber + "=",
                theVeryRightNumber);

        checkEquationException(theVeryRightNumberBeforeTheVeryRightNumber + "+" +
                theVeryLeftNumberBeforeTheVeryLeftNumber + "=", OVERFLOW_MESSAGE);
        checkEquationException(theVeryRightNumber + "+" + theVeryLeftNumber + "=", OVERFLOW_MESSAGE);
        checkEquationException(theVeryRightNumber + "+" + theVeryLeftNumberBeforeTheVeryLeftNumber + "=",
                OVERFLOW_MESSAGE);

        //left bound
        checkEquation("-" + theVeryRightNumberBeforeTheVeryRightNumber + "-" + theVeryLeftNumber + "=",
                "-" + theVeryRightNumber);

        checkEquationException("-" + theVeryRightNumberBeforeTheVeryRightNumber + "-" +
                theVeryLeftNumberBeforeTheVeryLeftNumber + "=", OVERFLOW_MESSAGE);
        checkEquationException("-" + theVeryRightNumber + "-" + theVeryLeftNumber + "=", OVERFLOW_MESSAGE);
        checkEquationException("-" + theVeryRightNumber + "-" + theVeryLeftNumberBeforeTheVeryLeftNumber + "=",
                OVERFLOW_MESSAGE);

        //min numbers
        //left bound
        checkEquation(theVeryLeftNumberBeforeTheVeryLeftNumber + "-" + theVeryLeftNumber + "=",
                theVeryLeftNumber);

        //positive and positive
        checkEquation("1.e-9998*0.1 =", "1.e-9999");
        checkEquation("1.e-9998/10 =", "1.e-9999");

        checkEquationException("1.e-9999*0.1 =", OVERFLOW_MESSAGE);
        checkEquationException("1.e-9999/10 =", OVERFLOW_MESSAGE);
        checkEquationException("1.e-9999*0.01 =", OVERFLOW_MESSAGE);
        checkEquationException("1.e-9999/100 =", OVERFLOW_MESSAGE);

        //negative and negative
        checkEquation("-1.e-9998*-0.1 =", "1.e-9999");
        checkEquation("-1.e-9998/-10 =", "1.e-9999");

        checkEquationException("-1.e-9999*-0.1 =", OVERFLOW_MESSAGE);
        checkEquationException("-1.e-9999/-10 =", OVERFLOW_MESSAGE);
        checkEquationException("-1.e-9999*-0.01 =", OVERFLOW_MESSAGE);
        checkEquationException("-1.e-9999/-100 =", OVERFLOW_MESSAGE);

        //right bound
        checkEquation("-" + theVeryLeftNumberBeforeTheVeryLeftNumber + "--" + theVeryLeftNumber + "=",
                "-" + theVeryLeftNumber);

        //positive and negative
        checkEquation("1.e-9998*-0.1 =", "-1.e-9999");
        checkEquation("1.e-9998/-10 =", "-1.e-9999");

        checkEquationException("1.e-9999*-0.1 =", OVERFLOW_MESSAGE);
        checkEquationException("1.e-9999/-10 =", OVERFLOW_MESSAGE);
        checkEquationException("1.e-9999*-0.01 =", OVERFLOW_MESSAGE);
        checkEquationException("1.e-9999/-100 =", OVERFLOW_MESSAGE);

        //negative and positive
        checkEquation("-1.e-9998*0.1 =", "-1.e-9999");
        checkEquation("-1.e-9998/10 =", "-1.e-9999");

        checkEquationException("-1.e-9999*0.1 =", OVERFLOW_MESSAGE);
        checkEquationException("-1.e-9999/10 =", OVERFLOW_MESSAGE);
        checkEquationException("-1.e-9999*0.01 =", OVERFLOW_MESSAGE);
        checkEquationException("-1.e-9999/100 =", OVERFLOW_MESSAGE);
    }

    /**
     * Method for testing {@link BinaryOperation} in {@link Calculation}.
     *
     * @param equation       equation that should be calculated. Should contain only numbers or operation symbols and
     *                       starts with number. Splitting numbers and operations is not necessary, but possible with
     *                       any number of spacings.
     *                       <p>
     *                       F.e., 2+2*2 will firstly calculate 2 + 2, and then the result will be multiplied on 2.
     *                       <p>
     *                       - 5 + 10 will cause an exception, but -5 + 10 will correctly calculate result (5).
     *                       <p>
     *                       4 * 2 4 will calculate 4 * 2 and then will multiply the result on 4 (final result is 32)
     *                       <p>
     *                       Passing several operations in a row will set the last passed operation (only if it is not -,
     *                       otherwise will be performed previous operation with negated next number).
     *                       <p>
     *                       3 +* 6 will calculate 3 * 6 (but 3 +-6 will calculate 3 + -6, as well as 3 + -6).
     *                       <p>
     *                       Any unary operation will be performed on previously inputted number.
     * @param expectedResult result that should be obtained.
     * @throws DivideByZeroException     if this exception was thrown during calculation.
     * @throws OverflowException         if this exception was thrown during calculation.
     * @throws DivideZeroByZeroException if this exception was thrown during calculation.
     * @throws NegativeRootException     if this exception was thrown during calculation.
     */
    private void checkEquation(String equation, String expectedResult) throws DivideByZeroException, OverflowException,
            NegativeRootException, DivideZeroByZeroException {
        String[] args = splitEquation(equation);
        assertEquals(new BigDecimal(expectedResult), performTestCalculation(args));
    }

    /**
     * Splits equation to numbers and operations.
     *
     * @param equation equation to split.
     * @return array obtained after split operation.
     */
    private String[] splitEquation(String equation) {
        equation = equation.toLowerCase();

        //saving characters
        equation = equation.replaceAll("e\\+", "ePlus");
        equation = equation.replaceAll("e-", "eMinus");
        equation = equation.replaceAll("^\\s*-", "minus");
        equation = equation.replaceAll("\\+\\s*-", "+minus");
        equation = equation.replaceAll("-\\s*-", "-minus");
        equation = equation.replaceAll("\\*\\s*-", "*minus");
        equation = equation.replaceAll("/\\s*-", "/minus");

        //replacing operations
        equation = equation.replaceAll("\\+", " + ");
        equation = equation.replaceAll("-", " - ");
        equation = equation.replaceAll("\\*", " * ");
        equation = equation.replaceAll("/", " / ");
        equation = equation.replaceAll("neg", " neg ");
        equation = equation.replaceAll("sqr", " sqr ");
        equation = equation.replaceAll("root", " root ");
        equation = equation.replaceAll("inverse", " inverse ");
        equation = equation.replaceAll("%", " % ");
        equation = equation.replaceAll("=", " = ");

        //returning saved characters
        equation = equation.replaceAll("ePlus", "e+");
        equation = equation.replaceAll("eMinus", "e-");
        equation = equation.replaceAll("minus", "-");

        return equation.split("\\s+");
    }

    /**
     * Performs calculation for tests.
     *
     * @param args numbers that should be set as second in {@link Calculation} (except the first number, that should be
     *             set as first) and {@link BinaryOperation}s that should be set as operation.
     *             <p>
     *             F.e., args = {"2", "+", "2", "="}. Method will calculate 2 + 2.
     *             <p>
     *             args = {"-", "5", "+", "10", "="} will cause an exception, but args = {"-5", "+", "10", "="} will
     *             correctly calculate result (5).
     *             <p>
     *             args = {"3", "+*", "6", "="} will cause an exception, but args = {"3", "+", "*", "6", "="} will
     *             calculate 3 * 6.
     *             <p>
     *             args = {"4", "*", "2", "4", "="} will calculate 4 * 4.
     *             <p>
     *             args = {"5", "sqr"} will calculate 5^2, as well as args = {"5", "sqr", "="}.
     *             <p>
     *             args = {"5", "+", "9", "sqr", "="} will calculate 5 + 9^2 (that is 81) and the final result is 86.
     * @throws OverflowException         if this exception was thrown during calculation.
     * @throws DivideByZeroException     if this exception was thrown during calculation.
     * @throws DivideZeroByZeroException if this exception was thrown during calculation.
     * @throws NegativeRootException     if this exception was thrown during calculation.
     */
    private BigDecimal performTestCalculation(String[] args) throws OverflowException, DivideByZeroException,
            DivideZeroByZeroException, NegativeRootException {
        calculation.resetAll();
        calculation.setFirst(new BigDecimal(args[0]));
        boolean secondSet = false;
        BigDecimal result = BigDecimal.ZERO;

        for (int i = 1; i < args.length; i++) {

            if (args[i].matches(INTEGER_NUMBER_REGEX) || args[i].matches(DECIMAL_NUMBER_REGEX) ||
                    args[i].matches(ENGINEER_NUMBER_REGEX)) {
                calculation.setSecond(new BigDecimal(args[i]));
                secondSet = true;
            } else if (args[i].equals("+")) {
                result = doBinary(secondSet);
                calculation.setBinaryOperation(ADD);
                secondSet = false;
            } else if (args[i].equals("-")) {
                result = doBinary(secondSet);
                calculation.setBinaryOperation(SUBTRACT);
                secondSet = false;
            } else if (args[i].equals("*")) {
                result = doBinary(secondSet);
                calculation.setBinaryOperation(MULTIPLY);
                secondSet = false;
            } else if (args[i].equals("/")) {
                result = doBinary(secondSet);
                calculation.setBinaryOperation(DIVIDE);
                secondSet = false;
            } else if (args[i].equals("neg")) {
                result = doUnary(secondSet, NEGATE);
            } else if (args[i].equals("sqr")) {
                result = doUnary(secondSet, SQR);
            } else if (args[i].equals("root")) {
                result = doUnary(secondSet, SQRT);
            } else if (args[i].equals("inverse")) {
                result = doUnary(secondSet, INVERSE);
            } else if (args[i].equals("%")) {
                result = doPercent(secondSet);
            } else if (args[i].equals("=")) {
                if (!secondSet) {
                    calculation.setSecond(calculation.getFirst());
                }

                result = doBinary(true);
                secondSet = false;
            } else {
                throw new IllegalArgumentException("Expected: operation or number. Got: " + args[i]);
            }
        }

        return result;
    }

    /**
     * Performs binary calculation from {@link Calculation} for tests. The same method should be used in controller.
     *
     * @param secondSet only if true calculation should be made.
     * @throws OverflowException         if this exception was thrown during calculation.
     * @throws DivideByZeroException     if this exception was thrown during calculation.
     * @throws DivideZeroByZeroException if this exception was thrown during calculation.
     */
    private BigDecimal doBinary(boolean secondSet) throws OverflowException, DivideByZeroException,
            DivideZeroByZeroException {
        BigDecimal result = BigDecimal.ZERO;

        if (secondSet) {
            result = calculation.calculateBinary();
            calculation.setFirst(result);
        }

        return result;
    }

    /**
     * Performs unary calculation from {@link Calculation} for tests. The same method should be used in controller.
     * If second number is set, first should be saved before calculation for not to loose it.
     *
     * @param secondSet if true calculation should be made on second number, otherwise on first.
     * @throws OverflowException     if this exception was thrown during calculation.
     * @throws NegativeRootException if this exception was thrown during calculation.
     * @throws DivideByZeroException if this exception was thrown during calculation.
     */
    private BigDecimal doUnary(boolean secondSet, UnaryOperation operation) throws OverflowException, NegativeRootException,
            DivideByZeroException {
        BigDecimal result;
        if (secondSet) {
            result = calculation.calculateUnary(calculation.getSecond(), operation);
            calculation.setSecond(result);
        } else {
            result = calculation.calculateUnary(calculation.getFirst(), operation);

            if (calculation.getBinaryOperation() == null) {
                calculation.setFirst(result);
            } else {
                calculation.setSecond(result);
            }
        }

        return result;
    }

    /**
     * Performs percent operation from {@link Calculation} for tests. The same method should be used in controller.
     * If second number is not set, first should be set as second.
     *
     * @param secondSet if false first number should be set as second.
     * @throws OverflowException if this exception was thrown during calculation.
     */
    private BigDecimal doPercent(boolean secondSet) throws OverflowException {
        if (!secondSet) {
            calculation.setSecond(calculation.getFirst());
        }

        BigDecimal result = calculation.calculatePercentage(calculation.getSecond());
        calculation.setSecond(result);

        return result;
    }

    /**
     * Method for testing throwing {@link OverflowException} while using binary operations.
     *
     * @param equation        equation that should be calculated. Should contain only numbers or operation symbols and
     *                        starts with number. Splitting numbers and operations is not necessary, but possible with
     *                        any number of spacings. For the equation, an {@link OverflowException} or
     *                        {@link ArithmeticException}should be thrown.
     *                        <p>
     *                        F.e., 2+2*2 will firstly calculate 2 + 2, and then the result will be multiplied on 2.
     *                        <p>
     *                        - 5 + 10 will cause an exception, but -5 + 10 will correctly calculate result (5).
     *                        <p>
     *                        4 * 2 4 will calculate 4 * 2 and then will multiply the result on 4 (final result is 32)
     *                        <p>
     *                        Passing several operations in a row will set the last passed operation (only if it is not
     *                        -, otherwise will be performed previous operation with negated next number).
     *                        <p>
     *                        3 +* 6 will calculate 3 * 6 (but 3 +-6 will calculate 3 + -6, as well as 3 + -6).
     *                        <p>
     *                        Any unary operation will be performed on previously inputted number.
     * @param expectedMessage message that thrown exception should contain.
     */
    private void checkEquationException(String equation, String expectedMessage) {
        String[] args = splitEquation(equation);

        try {
            performTestCalculation(args);
            fail();
        } catch (OverflowException | DivideByZeroException | DivideZeroByZeroException | NegativeRootException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}