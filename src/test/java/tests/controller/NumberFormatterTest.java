package tests.controller;

import com.implemica.bormashenko.calculator.controller.util.NumberFormatter;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for testing {@link NumberFormatter}.
 *
 * @author Mykhailo Bormashenko
 */
class NumberFormatterTest {

    /**
     * Tests for screen to big decimal operation.
     */
    @Test
    void parseToBigDecimalTests() throws ParseException {
        //integers
        //without commas
        checkParseToBigDecimal("0", new BigDecimal("0"));

        checkParseToBigDecimal("1", new BigDecimal("1"));
        checkParseToBigDecimal("8", new BigDecimal("8"));
        checkParseToBigDecimal("9", new BigDecimal("9"));
        checkParseToBigDecimal("10", new BigDecimal("10"));
        checkParseToBigDecimal("100", new BigDecimal("100"));
        checkParseToBigDecimal("500", new BigDecimal("500"));

        checkParseToBigDecimal("-1", new BigDecimal("-1"));
        checkParseToBigDecimal("-8", new BigDecimal("-8"));
        checkParseToBigDecimal("-9", new BigDecimal("-9"));
        checkParseToBigDecimal("-10", new BigDecimal("-10"));
        checkParseToBigDecimal("-100", new BigDecimal("-100"));
        checkParseToBigDecimal("-500", new BigDecimal("-500"));

        //with commas
        checkParseToBigDecimal("84,357", new BigDecimal("84357"));
        checkParseToBigDecimal("8,762,423,634", new BigDecimal("8762423634"));
        checkParseToBigDecimal("873,283,568", new BigDecimal("873283568"));
        checkParseToBigDecimal("8,235,854,645", new BigDecimal("8235854645"));
        checkParseToBigDecimal("23,482,314", new BigDecimal("23482314"));
        checkParseToBigDecimal("234,643,737", new BigDecimal("234643737"));

        checkParseToBigDecimal("-72,341,234", new BigDecimal("-72341234"));
        checkParseToBigDecimal("-8,023,042,394", new BigDecimal("-8023042394"));
        checkParseToBigDecimal("-626,356,345", new BigDecimal("-626356345"));
        checkParseToBigDecimal("-67,235,923,052", new BigDecimal("-67235923052"));
        checkParseToBigDecimal("-7,625,252,352,352", new BigDecimal("-7625252352352"));
        checkParseToBigDecimal("-62,523,523,523,525", new BigDecimal("-62523523523525"));

        //decimals
        //without commas
        checkParseToBigDecimal("0.6", new BigDecimal("0.6"));
        checkParseToBigDecimal("1.235", new BigDecimal("1.235"));
        checkParseToBigDecimal("8.8236", new BigDecimal("8.8236"));
        checkParseToBigDecimal("9.8245", new BigDecimal("9.8245"));
        checkParseToBigDecimal("10.7", new BigDecimal("10.7"));
        checkParseToBigDecimal("100.3247", new BigDecimal("100.3247"));
        checkParseToBigDecimal("500.7235", new BigDecimal("500.7235"));

        checkParseToBigDecimal("-0.736", new BigDecimal("-0.736"));
        checkParseToBigDecimal("-1.8356", new BigDecimal("-1.8356"));
        checkParseToBigDecimal("-8.5437", new BigDecimal("-8.5437"));
        checkParseToBigDecimal("-9.7235", new BigDecimal("-9.7235"));
        checkParseToBigDecimal("-10.834", new BigDecimal("-10.834"));
        checkParseToBigDecimal("-100.01", new BigDecimal("-100.01"));
        checkParseToBigDecimal("-500.5", new BigDecimal("-500.5"));

        //with commas
        checkParseToBigDecimal("124,513.25835", new BigDecimal("124513.25835"));
        checkParseToBigDecimal("62,352.7235", new BigDecimal("62352.7235"));
        checkParseToBigDecimal("6,626.8256", new BigDecimal("6626.8256"));
        checkParseToBigDecimal("2,346.7925", new BigDecimal("2346.7925"));
        checkParseToBigDecimal("762,462.6782", new BigDecimal("762462.6782"));
        checkParseToBigDecimal("7,624,623.2", new BigDecimal("7624623.2"));
        checkParseToBigDecimal("76,236,262.72", new BigDecimal("76236262.72"));

        checkParseToBigDecimal("-77,322,225.7", new BigDecimal("-77322225.7"));
        checkParseToBigDecimal("-823,452,168,746.734535", new BigDecimal("-823452168746.734535"));
        checkParseToBigDecimal("-2,523,578.725", new BigDecimal("-2523578.725"));
        checkParseToBigDecimal("-8,643.825", new BigDecimal("-8643.825"));
        checkParseToBigDecimal("-6,235.725", new BigDecimal("-6235.725"));
        checkParseToBigDecimal("-734,535.73", new BigDecimal("-734535.73"));
        checkParseToBigDecimal("-84,564.622", new BigDecimal("-84564.622"));

        //engineer numbers
        checkParseToBigDecimal("7.e+7234", new BigDecimal("7.e+7234"));
        checkParseToBigDecimal("1.e+72", new BigDecimal("1.e+72"));
        checkParseToBigDecimal("5.e+92", new BigDecimal("5.e+92"));
        checkParseToBigDecimal("4.e-234", new BigDecimal("4.e-234"));
        checkParseToBigDecimal("8.e-19", new BigDecimal("8.e-19"));
        checkParseToBigDecimal("2.e-84", new BigDecimal("2.e-84"));

        checkParseToBigDecimal("-4.e+13", new BigDecimal("-4.e+13"));
        checkParseToBigDecimal("-2.e+126", new BigDecimal("-2.e+126"));
        checkParseToBigDecimal("-7.e+1482", new BigDecimal("-7.e+1482"));
        checkParseToBigDecimal("-6.e-723", new BigDecimal("-6.e-723"));
        checkParseToBigDecimal("-5.e-17", new BigDecimal("-5.e-17"));
        checkParseToBigDecimal("-2.e-79", new BigDecimal("-2.e-79"));

        checkParseToBigDecimal("6.21e+24", new BigDecimal("6.21e+24"));
        checkParseToBigDecimal("3.762e+789", new BigDecimal("3.762e+789"));
        checkParseToBigDecimal("1.627e+25", new BigDecimal("1.627e+25"));
        checkParseToBigDecimal("7.831e-19", new BigDecimal("7.831e-19"));
        checkParseToBigDecimal("9.1346e-3216", new BigDecimal("9.1346e-3216"));
        checkParseToBigDecimal("1.143e-1446", new BigDecimal("1.143e-1446"));

        checkParseToBigDecimal("-8.09e+74", new BigDecimal("-8.09e+74"));
        checkParseToBigDecimal("-6.863e+835", new BigDecimal("-6.863e+835"));
        checkParseToBigDecimal("-4.325e+7267", new BigDecimal("-4.325e+7267"));
        checkParseToBigDecimal("-2.13e-874", new BigDecimal("-2.13e-874"));
        checkParseToBigDecimal("-2.126e-1353", new BigDecimal("-2.126e-1353"));
        checkParseToBigDecimal("-1.73e-735", new BigDecimal("-1.73e-735"));
    }

    /**
     * Tests for format operation.
     */
    @Test
    void formatTests() {
        //integers
        {
            //less that 16 digits
            checkFormat(new BigDecimal("0"), "0");

            checkFormat(new BigDecimal("1"), "1");
            checkFormat(new BigDecimal("2"), "2");
            checkFormat(new BigDecimal("100"), "100");

            checkFormat(new BigDecimal("5000"), "5,000");
            checkFormat(new BigDecimal("62368"), "62,368");
            checkFormat(new BigDecimal("73737375"), "73,737,375");

            checkFormat(new BigDecimal("-1"), "-1");
            checkFormat(new BigDecimal("-2"), "-2");
            checkFormat(new BigDecimal("-100"), "-100");

            checkFormat(new BigDecimal("-5000"), "-5,000");
            checkFormat(new BigDecimal("-137687"), "-137,687");
            checkFormat(new BigDecimal("-1236879000"), "-1,236,879,000");

            //16 digits
            checkFormat(new BigDecimal("7891236742367892"), "7,891,236,742,367,892");
            checkFormat(new BigDecimal("8914815712245616"), "8,914,815,712,245,616");
            checkFormat(new BigDecimal("1472184129453219"), "1,472,184,129,453,219");

            checkFormat(new BigDecimal("-1237894562345678"), "-1,237,894,562,345,678");
            checkFormat(new BigDecimal("-7123912369284216"), "-7,123,912,369,284,216");
            checkFormat(new BigDecimal("-1237891401000000"), "-1,237,891,401,000,000");

            //more than 16 digits
            checkFormat(new BigDecimal("354678126471241518756213"), "3.546781264712415e+23");
            checkFormat(new BigDecimal("86583242839429024222221664265286"), "8.658324283942902e+31");
            checkFormat(new BigDecimal("982147981274912749812748912749"), "9.821479812749127e+29");

            checkFormat(new BigDecimal("-6315214812841571983251953218198156"), "-6.315214812841572e+33");
            checkFormat(new BigDecimal("-1231238456344543890000000000"), "-1.231238456344544e+27");
            checkFormat(new BigDecimal("-3123137862452352347899"), "-3.123137862452352e+21");
        }

        //decimals
        {
            //less that 16 digits (summary)
            checkFormat(new BigDecimal("13.16325"), "13.16325");
            checkFormat(new BigDecimal("0.724658"), "0.724658");
            checkFormat(new BigDecimal("625.72"), "625.72");

            checkFormat(new BigDecimal("12383254.8234324"), "12,383,254.8234324");
            checkFormat(new BigDecimal("12312476.2434"), "12,312,476.2434");
            checkFormat(new BigDecimal("1316.123"), "1,316.123");

            checkFormat(new BigDecimal("-0.721111"), "-0.721111");
            checkFormat(new BigDecimal("-12.1"), "-12.1");
            checkFormat(new BigDecimal("-0.721111"), "-0.721111");

            checkFormat(new BigDecimal("-14312.73"), "-14,312.73");
            checkFormat(new BigDecimal("-5952.43521"), "-5,952.43521");
            checkFormat(new BigDecimal("-23425252.25"), "-23,425,252.25");

            //16 digits (summary) and starts with 0.
            checkFormat(new BigDecimal("0.765986234567097"), "0.765986234567097");
            checkFormat(new BigDecimal("0.987187591481567"), "0.987187591481567");
            checkFormat(new BigDecimal("0.123689238599145"), "0.123689238599145");

            checkFormat(new BigDecimal("-0.126789043256784"), "-0.126789043256784");
            checkFormat(new BigDecimal("-0.213123099852516"), "-0.213123099852516");
            checkFormat(new BigDecimal("-0.918237192749815"), "-0.918237192749815");

            //16 digits (summary) and does not start with 0.
            checkFormat(new BigDecimal("765678.6786785423"), "765,678.6786785423");
            checkFormat(new BigDecimal("988624352525.7111"), "988,624,352,525.7111");
            checkFormat(new BigDecimal("3192831937198.355"), "3,192,831,937,198.355");

            checkFormat(new BigDecimal("-98.12748917915915"), "-98.12748917915915");
            checkFormat(new BigDecimal("-1235823.592917895"), "-1,235,823.592917895");
            checkFormat(new BigDecimal("-12936661991.79419"), "-12,936,661,991.79419");

            //17 digits (summary) and starts with 0.
            checkFormat(new BigDecimal("0.7634568901234567"), "0.7634568901234567");
            checkFormat(new BigDecimal("0.8269023457876125"), "0.8269023457876125");
            checkFormat(new BigDecimal("0.1465698791749184"), "0.1465698791749184");

            checkFormat(new BigDecimal("-0.123123213698235"), "-0.123123213698235");
            checkFormat(new BigDecimal("-0.102398019815789"), "-0.102398019815789");
            checkFormat(new BigDecimal("-0.876523452349824"), "-0.876523452349824");

            //17 digits (summary) and does not start with 0.
            checkFormat(new BigDecimal("1237894445.6723489"), "1,237,894,445.672349");
            checkFormat(new BigDecimal("73463.632980090322"), "73,463.63298009032");
            checkFormat(new BigDecimal("926598259202.22578"), "926,598,259,202.2258");

            checkFormat(new BigDecimal("-1238091250715979.8"), "-1,238,091,250,715,980");
            checkFormat(new BigDecimal("-723.52358725265226"), "-723.5235872526523");
            checkFormat(new BigDecimal("-3447.7789023345812"), "-3,447.778902334581");

            //more than 17 digits (summary) and starts with 0.
            checkFormat(new BigDecimal("0.9418724917491750150179815191"), "0.941872491749175");
            checkFormat(new BigDecimal("0.5418715871759237626982786206123"), "0.5418715871759238");
            checkFormat(new BigDecimal("0.982137987150875380173258619"), "0.9821379871508754");

            checkFormat(new BigDecimal("-0.198471895719857193751084791"), "-0.1984718957198572");
            checkFormat(new BigDecimal("-0.12310958190357878062046206"), "-0.1231095819035788");
            checkFormat(new BigDecimal("-0.12465982153209520592805"), "-0.1246598215320952");

            //more than 17 digits (summary) and does not start with 0.
            checkFormat(new BigDecimal("128419581095019580128.75019875"), "1.284195810950196e+20");
            checkFormat(new BigDecimal("128758.917509715091750128750175"), "128,758.9175097151");
            checkFormat(new BigDecimal("1235992.1347819991923816"), "1,235,992.134781999");

            checkFormat(new BigDecimal("-1312981740181908.0985023"), "-1,312,981,740,181,908");
            checkFormat(new BigDecimal("-51351309848719847109847109.7431098471984"), "-5.135130984871985e+25");
            checkFormat(new BigDecimal("-124189579327598.4325798327592769"), "-124,189,579,327,598.4");
        }

        //engineer
        {
            //e+
            checkFormat(new BigDecimal("1.e+1"), "10");
            checkFormat(new BigDecimal("1.e+2"), "100");
            checkFormat(new BigDecimal("5.e+5"), "500,000");
            checkFormat(new BigDecimal("6.e+15"), "6,000,000,000,000,000");
            checkFormat(new BigDecimal("9.e+16"), "9.e+16");

            checkFormat(new BigDecimal("1.5e+1"), "15");
            checkFormat(new BigDecimal("1.68e+2"), "168");
            checkFormat(new BigDecimal("5.25e+5"), "525,000");
            checkFormat(new BigDecimal("6.73e+15"), "6,730,000,000,000,000");
            checkFormat(new BigDecimal("9.74e+16"), "9.74e+16");

            checkFormat(new BigDecimal("1.3e+67"), "1.3e+67");
            checkFormat(new BigDecimal("1.6128e+22"), "1.6128e+22");
            checkFormat(new BigDecimal("5.21235e+1235"), "5.21235e+1235");
            checkFormat(new BigDecimal("6.71563e+1213"), "6.71563e+1213");
            checkFormat(new BigDecimal("9.744e+156"), "9.744e+156");

            //-e+
            checkFormat(new BigDecimal("-1.e+1"), "-10");
            checkFormat(new BigDecimal("-1.e+2"), "-100");
            checkFormat(new BigDecimal("-5.e+5"), "-500,000");
            checkFormat(new BigDecimal("-6.e+15"), "-6,000,000,000,000,000");
            checkFormat(new BigDecimal("-9.e+16"), "-9.e+16");

            checkFormat(new BigDecimal("-1.5e+1"), "-15");
            checkFormat(new BigDecimal("-1.68e+2"), "-168");
            checkFormat(new BigDecimal("-5.25e+5"), "-525,000");
            checkFormat(new BigDecimal("-6.73e+15"), "-6,730,000,000,000,000");
            checkFormat(new BigDecimal("-9.74e+16"), "-9.74e+16");

            checkFormat(new BigDecimal("-1.3e+67"), "-1.3e+67");
            checkFormat(new BigDecimal("-1.6128e+22"), "-1.6128e+22");
            checkFormat(new BigDecimal("-5.21235e+1235"), "-5.21235e+1235");
            checkFormat(new BigDecimal("-6.71563e+1213"), "-6.71563e+1213");
            checkFormat(new BigDecimal("-9.744e+156"), "-9.744e+156");

            //e-
            checkFormat(new BigDecimal("1.e-1"), "0.1");
            checkFormat(new BigDecimal("1.e-2"), "0.01");
            checkFormat(new BigDecimal("5.e-5"), "0.00005");
            checkFormat(new BigDecimal("9.e-16"), "0.0000000000000009");
            checkFormat(new BigDecimal("9.e-17"), "9.e-17");

            checkFormat(new BigDecimal("1.5e-1"), "0.15");
            checkFormat(new BigDecimal("1.68e-2"), "0.0168");
            checkFormat(new BigDecimal("5.25e-5"), "0.0000525");
            checkFormat(new BigDecimal("6.73e-16"), "6.73e-16");
            checkFormat(new BigDecimal("9.74e-17"), "9.74e-17");

            checkFormat(new BigDecimal("1.3e-67"), "1.3e-67");
            checkFormat(new BigDecimal("1.6128e-22"), "1.6128e-22");
            checkFormat(new BigDecimal("5.21235e-1235"), "5.21235e-1235");
            checkFormat(new BigDecimal("6.71563e-1213"), "6.71563e-1213");
            checkFormat(new BigDecimal("9.744e-156"), "9.744e-156");

            //-e-
            checkFormat(new BigDecimal("-1.e-1"), "-0.1");
            checkFormat(new BigDecimal("-1.e-2"), "-0.01");
            checkFormat(new BigDecimal("-5.e-5"), "-0.00005");
            checkFormat(new BigDecimal("-6.e-15"), "-0.000000000000006");
            checkFormat(new BigDecimal("-9.e-16"), "-0.0000000000000009");

            checkFormat(new BigDecimal("-1.5e-1"), "-0.15");
            checkFormat(new BigDecimal("-1.68e-2"), "-0.0168");
            checkFormat(new BigDecimal("-5.25e-5"), "-0.0000525");
            checkFormat(new BigDecimal("-6.73e-16"), "-6.73e-16");
            checkFormat(new BigDecimal("-9.74e-17"), "-9.74e-17");

            checkFormat(new BigDecimal("-1.3e-67"), "-1.3e-67");
            checkFormat(new BigDecimal("-1.6128e-22"), "-1.6128e-22");
            checkFormat(new BigDecimal("-5.21235e-1235"), "-5.21235e-1235");
            checkFormat(new BigDecimal("-6.71563e-1213"), "-6.71563e-1213");
            checkFormat(new BigDecimal("-9.744e-156"), "-9.744e-156");
        }
    }

    /**
     * Tests for bounds for exponential form of number.
     */
    @Test
    void boundaryFormatting() {
        checkFormat(new BigDecimal("9999999999999999.4"), "9,999,999,999,999,999");
        checkFormat(new BigDecimal("9999999999999999.5"), "1.e+16");
        checkFormat(new BigDecimal("9999999999999999.6"), "1.e+16");

        checkFormat(new BigDecimal("-9999999999999999.4"), "-9,999,999,999,999,999");
        checkFormat(new BigDecimal("-9999999999999999.5"), "-1.e+16");
        checkFormat(new BigDecimal("-9999999999999999.6"), "-1.e+16");

        checkFormat(new BigDecimal("0.0000000000000001"), "0.0000000000000001");
        checkFormat(new BigDecimal("0.00000000000000011"), "1.1e-16");
        checkFormat(new BigDecimal("0.00000000000000012"), "1.2e-16");
        checkFormat(new BigDecimal("0.00000000000000001"), "1.e-17");

        checkFormat(new BigDecimal("-0.0000000000000001"), "-0.0000000000000001");
        checkFormat(new BigDecimal("-0.00000000000000011"), "-1.1e-16");
        checkFormat(new BigDecimal("-0.00000000000000012"), "-1.2e-16");
        checkFormat(new BigDecimal("-0.00000000000000001"), "-1.e-17");
    }

    /**
     * Check result of screen to big decimal operation.
     *
     * @param string     number to convert.
     * @param bigDecimal expected result after performing operation.
     */
    private void checkParseToBigDecimal(String string, BigDecimal bigDecimal) throws ParseException {
        BigDecimal bigDecimalResult = NumberFormatter.parseToBigDecimal(string);
        assertEquals(bigDecimal, bigDecimalResult);
    }

    /**
     * Check result of format number operation (with and without group separator).
     *
     * @param bigDecimal     number to format.
     * @param expectedResult expected result after performing operation.
     */
    private void checkFormat(BigDecimal bigDecimal, String expectedResult) {
        String resultWithGroupSeparator = NumberFormatter.formatNumber(bigDecimal, true);
        assertEquals(expectedResult, resultWithGroupSeparator);

        String resultWithoutGroupSeparator = NumberFormatter.formatNumber(bigDecimal, false);

        assertEquals(expectedResult.replaceAll(",", ""), resultWithoutGroupSeparator);
    }
}
