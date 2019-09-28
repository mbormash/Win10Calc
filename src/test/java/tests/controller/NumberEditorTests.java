package tests.controller;

import com.implemica.bormashenko.calculator.controller.util.NumberEditor;
import com.implemica.bormashenko.calculator.controller.util.NumberFormatter;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for testing {@link NumberEditor}.
 *
 * @author Mykhailo Bormashenko
 */
class NumberEditorTests {

    /**
     * Tests for append digit operation.
     */
    @Test
    void appendDigitTests() {
        //can append
        {
            //append to zero
            checkAppendDigit("0", "0", "0");
            checkAppendDigit("0", "1", "1");
            checkAppendDigit("0", "5", "5");
            checkAppendDigit("0", "8", "8");
            checkAppendDigit("0", "9", "9");

            //append to one-digit positive integer number
            checkAppendDigit("1", "0", "10");
            checkAppendDigit("1", "1", "11");
            checkAppendDigit("1", "5", "15");
            checkAppendDigit("1", "8", "18");
            checkAppendDigit("1", "9", "19");


            checkAppendDigit("5", "0", "50");
            checkAppendDigit("5", "1", "51");
            checkAppendDigit("5", "5", "55");
            checkAppendDigit("5", "8", "58");
            checkAppendDigit("5", "9", "59");

            checkAppendDigit("9", "0", "90");
            checkAppendDigit("9", "1", "91");
            checkAppendDigit("9", "5", "95");
            checkAppendDigit("9", "8", "98");
            checkAppendDigit("9", "9", "99");

            //append to one-digit negative integer number
            checkAppendDigit("-1", "0", "-10");
            checkAppendDigit("-1", "1", "-11");
            checkAppendDigit("-1", "5", "-15");
            checkAppendDigit("-1", "8", "-18");
            checkAppendDigit("-1", "9", "-19");

            checkAppendDigit("-5", "0", "-50");
            checkAppendDigit("-5", "1", "-51");
            checkAppendDigit("-5", "5", "-55");
            checkAppendDigit("-5", "8", "-58");
            checkAppendDigit("-5", "9", "-59");

            checkAppendDigit("-9", "0", "-90");
            checkAppendDigit("-9", "1", "-91");
            checkAppendDigit("-9", "5", "-95");
            checkAppendDigit("-9", "8", "-98");
            checkAppendDigit("-9", "9", "-99");

            //append to one-digit positive decimal number
            checkAppendDigit("1.", "0", "1.0");
            checkAppendDigit("1.", "1", "1.1");
            checkAppendDigit("1.", "5", "1.5");
            checkAppendDigit("1.", "8", "1.8");
            checkAppendDigit("1.", "9", "1.9");

            checkAppendDigit("5.", "0", "5.0");
            checkAppendDigit("5.", "1", "5.1");
            checkAppendDigit("5.", "5", "5.5");
            checkAppendDigit("5.", "8", "5.8");
            checkAppendDigit("5.", "9", "5.9");

            checkAppendDigit("9.", "0", "9.0");
            checkAppendDigit("9.", "1", "9.1");
            checkAppendDigit("9.", "5", "9.5");
            checkAppendDigit("9.", "8", "9.8");
            checkAppendDigit("9.", "9", "9.9");

            //append to one-digit negative decimal number
            checkAppendDigit("-1", "0", "-10");
            checkAppendDigit("-1", "1", "-11");
            checkAppendDigit("-1", "5", "-15");
            checkAppendDigit("-1", "8", "-18");
            checkAppendDigit("-1", "9", "-19");

            checkAppendDigit("-5", "0", "-50");
            checkAppendDigit("-5", "1", "-51");
            checkAppendDigit("-5", "5", "-55");
            checkAppendDigit("-5", "8", "-58");
            checkAppendDigit("-5", "9", "-59");

            checkAppendDigit("-9", "0", "-90");
            checkAppendDigit("-9", "1", "-91");
            checkAppendDigit("-9", "5", "-95");
            checkAppendDigit("-9", "8", "-98");
            checkAppendDigit("-9", "9", "-99");

            //append to three-digits positive integer number
            checkAppendDigit("934", "0", "9340");
            checkAppendDigit("825", "1", "8251");
            checkAppendDigit("230", "2", "2302");
            checkAppendDigit("827", "3", "8273");
            checkAppendDigit("828", "4", "8284");
            checkAppendDigit("821", "5", "8215");
            checkAppendDigit("997", "6", "9976");
            checkAppendDigit("282", "7", "2827");
            checkAppendDigit("962", "8", "9628");
            checkAppendDigit("973", "9", "9739");

            //append to three-digits negative integer number
            checkAppendDigit("-641", "0", "-6410");
            checkAppendDigit("-345", "1", "-3451");
            checkAppendDigit("-970", "2", "-9702");
            checkAppendDigit("-175", "3", "-1753");
            checkAppendDigit("-962", "4", "-9624");
            checkAppendDigit("-537", "5", "-5375");
            checkAppendDigit("-970", "6", "-9706");
            checkAppendDigit("-234", "7", "-2347");
            checkAppendDigit("-468", "8", "-4688");
            checkAppendDigit("-738", "9", "-7389");

            //append to three-digits (before dot) positive decimal number
            checkAppendDigit("623.", "0", "623.0");
            checkAppendDigit("838.", "1", "838.1");
            checkAppendDigit("234.", "5", "234.5");
            checkAppendDigit("126.", "8", "126.8");
            checkAppendDigit("967.", "9", "967.9");

            //checkAppendDigit("821.25", "0", "821.250");
            checkAppendDigit("997.813489", "1", "997.8134891");
            checkAppendDigit("282.342342", "5", "282.3423425");
            checkAppendDigit("962.9253", "8", "962.92538");
            checkAppendDigit("973.8234", "9", "973.82349");

            //append to three-digits (before dot) negative decimal number
            checkAppendDigit("-836.", "0", "-836.0");
            checkAppendDigit("-822.", "1", "-822.1");
            checkAppendDigit("-147.", "5", "-147.5");
            checkAppendDigit("-890.", "8", "-890.8");
            checkAppendDigit("-100.", "9", "-100.9");

            checkAppendDigit("-450.74790", "0", "-450.747900");
            checkAppendDigit("-670.9259304", "1", "-670.92593041");
            checkAppendDigit("-532.83", "5", "-532.835");
            checkAppendDigit("-842.82452", "8", "-842.824528");
            checkAppendDigit("-341.72368", "9", "-341.723689");

            //append to integer number with commas
            checkAppendDigit("723567", "0", "7235670");
            checkAppendDigit("82458", "1", "824581");
            checkAppendDigit("23634", "5", "236345");
            checkAppendDigit("2578", "8", "25788");
            checkAppendDigit("2155478547", "9", "21554785479");

            checkAppendDigit("-457345", "0", "-4573450");
            checkAppendDigit("-73456", "1", "-734561");
            checkAppendDigit("-3457", "5", "-34575");
            checkAppendDigit("-25679", "8", "-256798");
            checkAppendDigit("-63467435", "9", "-634674359");

            //append to decimal number with commas
            checkAppendDigit("21344.", "0", "21344.0");
            checkAppendDigit("7346346.8", "1", "7346346.81");
            checkAppendDigit("83463.375", "5", "83463.3755");
            checkAppendDigit("32467778.34678", "8", "32467778.346788");
            checkAppendDigit("23447.84365346", "9", "23447.843653469");

            checkAppendDigit("-32567.", "0", "-32567.0");
            checkAppendDigit("-23654.7", "1", "-23654.71");
            checkAppendDigit("-8762435.425", "5", "-8762435.4255");
            checkAppendDigit("-34637457.7346", "8", "-34637457.73468");
            checkAppendDigit("-3453635.8346346", "9", "-3453635.83463469");

            //append to 15-digits integer number
            checkAppendDigit("678096234678975", "0", "6780962346789750");
            checkAppendDigit("780875456897543", "1", "7808754568975431");
            checkAppendDigit("987456875345789", "5", "9874568753457895");
            checkAppendDigit("987564567986246", "8", "9875645679862468");
            checkAppendDigit("189090098567245", "9", "1890900985672459");

            checkAppendDigit("-987456752346783", "0", "-9874567523467830");
            checkAppendDigit("-987567356782345", "1", "-9875673567823451");
            checkAppendDigit("-987457621103536", "5", "-9874576211035365");
            checkAppendDigit("-123567256721457", "8", "-1235672567214578");
            checkAppendDigit("-423767464768144", "9", "-4237674647681449");

            //append to 15-digits (summary) decimal number
            checkAppendDigit("678987175238.357", "0", "678987175238.3570");
            checkAppendDigit("89798.6297342734", "1", "89798.62973427341");
            checkAppendDigit("98273492302.5727", "5", "98273492302.57275");
            checkAppendDigit("863.240000000023", "8", "863.2400000000238");
            checkAppendDigit("624592234242.500", "9", "624592234242.5009");

            checkAppendDigit("-7862359237.50226", "0", "-7862359237.502260");
            checkAppendDigit("-842.789273942046", "1", "-842.7892739420461");
            checkAppendDigit("-898402348028.492", "5", "-898402348028.4925");
            checkAppendDigit("-725836.826346363", "8", "-725836.8263463638");
            checkAppendDigit("-90000000.0000000", "9", "-90000000.00000009");

            //append to decimal number with 16 digits summary (while integer part is 0)
            checkAppendDigit("0.678986456781345", "0", "0.6789864567813450");
            checkAppendDigit("0.825252834573467", "1", "0.8252528345734671");
            checkAppendDigit("0.000000000000000", "5", "0.0000000000000005");
            checkAppendDigit("0.845634528235992", "8", "0.8456345282359928");
            checkAppendDigit("0.029840234005268", "9", "0.0298402340052689");

            checkAppendDigit("-0.09234927358283", "0", "-0.092349273582830");
            checkAppendDigit("-0.23492759238567", "1", "-0.234927592385671");
            checkAppendDigit("-0.03295027359285", "5", "-0.032950273592855");
            checkAppendDigit("-0.02934982635782", "8", "-0.029349826357828");
            checkAppendDigit("-0.76235826754836", "9", "-0.762358267548369");
        }

        //can not append
        {
            //append to 16-digits integer number
            checkAppendDigit("9847862357839869", "0", "9847862357839869");
            checkAppendDigit("7374149817289492", "1", "7374149817289492");
            checkAppendDigit("7349564303904532", "5", "7349564303904532");
            checkAppendDigit("9872358289579258", "8", "9872358289579258");
            checkAppendDigit("6729423840284622", "9", "6729423840284622");

            checkAppendDigit("-9239582957295726", "0", "-9239582957295726");
            checkAppendDigit("-7290000008234928", "1", "-7290000008234928");
            checkAppendDigit("-6666666666666666", "5", "-6666666666666666");
            checkAppendDigit("-2592838592590224", "8", "-2592838592590224");
            checkAppendDigit("-7535253536367367", "9", "-7535253536367367");

            //append to 16-digits (summary) decimal number
            checkAppendDigit("6269657463635353.", "0", "6269657463635353.");
            checkAppendDigit("7.334536437634563", "1", "7.334536437634563");
            checkAppendDigit("74564.64654646464", "5", "74564.64654646464");
            checkAppendDigit("966434529745647.4", "8", "966434529745647.4");
            checkAppendDigit("84.74745745745749", "9", "84.74745745745749");

            checkAppendDigit("-6346346394365337.", "0", "-6346346394365337.");
            checkAppendDigit("-845787846363733.7", "1", "-845787846363733.7");
            checkAppendDigit("-734.6367934653793", "5", "-734.6367934653793");
            checkAppendDigit("-95675.67568758474", "8", "-95675.67568758474");
            checkAppendDigit("-734639356475.4563", "9", "-734639356475.4563");

            //append to decimal number with 17 digits summary (while integer part is 0)
            checkAppendDigit("0.7564734634634638", "0", "0.7564734634634638");
            checkAppendDigit("0.2359346457735875", "1", "0.2359346457735875");
            checkAppendDigit("0.8847352526843832", "5", "0.8847352526843832");
            checkAppendDigit("0.8457934693468346", "8", "0.8457934693468346");
            checkAppendDigit("0.8734636834583467", "9", "0.8734636834583467");

            checkAppendDigit("-0.8346362645875478", "0", "-0.8346362645875478");
            checkAppendDigit("-0.8734634634683468", "1", "-0.8734634634683468");
            checkAppendDigit("-0.2353645754673458", "5", "-0.2353645754673458");
            checkAppendDigit("-0.3468435473637297", "8", "-0.3468435473637297");
            checkAppendDigit("-0.4578657686567588", "9", "-0.4578657686567588");
        }
    }

    /**
     * Tests for delete last char operation.
     */
    @Test
    void deleteLastCharTests() {
        //one-digit numbers
        checkDeleteLastChar("0", "0");
        checkDeleteLastChar("1", "0");
        checkDeleteLastChar("2", "0");
        checkDeleteLastChar("7", "0");
        checkDeleteLastChar("8", "0");
        checkDeleteLastChar("9", "0");

        checkDeleteLastChar("-0", "0");
        checkDeleteLastChar("-1", "0");
        checkDeleteLastChar("-2", "0");
        checkDeleteLastChar("-7", "0");
        checkDeleteLastChar("-8", "0");
        checkDeleteLastChar("-9", "0");

        //several digits without commas
        checkDeleteLastChar("10", "1");
        checkDeleteLastChar("21", "2");
        checkDeleteLastChar("35", "3");
        checkDeleteLastChar("488", "48");
        checkDeleteLastChar("516", "51");
        checkDeleteLastChar("741", "74");

        checkDeleteLastChar("-15", "-1");
        checkDeleteLastChar("-75", "-7");
        checkDeleteLastChar("-84", "-8");
        checkDeleteLastChar("-763", "-76");
        checkDeleteLastChar("-862", "-86");
        checkDeleteLastChar("-123", "-12");

        //with commas
        checkDeleteLastChar("1234", "123");
        checkDeleteLastChar("6225", "622");
        checkDeleteLastChar("84623", "8462");
        checkDeleteLastChar("64362234", "6436223");
        checkDeleteLastChar("752384257", "75238425");
        checkDeleteLastChar("32572357832", "3257235783");

        checkDeleteLastChar("-235623", "-23562");
        checkDeleteLastChar("-762723", "-76272");
        checkDeleteLastChar("-142790", "-14279");
        checkDeleteLastChar("-35246980", "-3524698");
        checkDeleteLastChar("-234268436", "-23426843");
        checkDeleteLastChar("-2435724525", "-243572452");

        //with dot in the middle and no commas
        checkDeleteLastChar("1.76", "1.7");
        checkDeleteLastChar("7.8", "7.");
        checkDeleteLastChar("85.0", "85.");
        checkDeleteLastChar("123.82", "123.8");
        checkDeleteLastChar("752.26782", "752.2678");
        checkDeleteLastChar("126.1", "126.");

        checkDeleteLastChar("-12.1765", "-12.176");
        checkDeleteLastChar("-2.23765", "-2.2376");
        checkDeleteLastChar("-236.76532", "-236.7653");
        checkDeleteLastChar("-822.12", "-822.1");
        checkDeleteLastChar("-752.752", "-752.75");
        checkDeleteLastChar("-80.213", "-80.21");

        //with dot in the middle and commas
        checkDeleteLastChar("1477.234", "1477.23");
        checkDeleteLastChar("23572.23462", "23572.2346");
        checkDeleteLastChar("762852.0", "762852.");
        checkDeleteLastChar("2575723572.9", "2575723572.");
        checkDeleteLastChar("876532.2", "876532.");
        checkDeleteLastChar("9014.23", "9014.2");

        checkDeleteLastChar("-234823547.234", "-234823547.23");
        checkDeleteLastChar("-23472.23468", "-23472.2346");
        checkDeleteLastChar("-87358245.24", "-87358245.2");
        checkDeleteLastChar("-2472357.23478", "-2472357.2347");
        checkDeleteLastChar("-32723467.23482", "-32723467.2348");
        checkDeleteLastChar("-23475345.7625", "-23475345.762");
    }

    /**
     * Checks that digit appended to number if it should be appended.
     *
     * @param number         number to edit.
     * @param digit          digit to append.
     * @param expectedResult expected result after performing operation.
     */
    private void checkAppendDigit(String number, String digit, String expectedResult) {
        BigDecimal bigDecimalNumber = new BigDecimal(number);
        BigDecimal bigDecimalDigit = new BigDecimal(digit);
        BigDecimal bigDecimalExpectedResult = new BigDecimal(expectedResult);

        assertEquals(bigDecimalExpectedResult, NumberEditor.appendDigitToNumber(bigDecimalNumber, bigDecimalDigit,
                number.endsWith(String.valueOf(NumberFormatter.DECIMAL_SEPARATOR))));
    }

    /**
     * Checks result of delete last char operation.
     *
     * @param number         number to edit.
     * @param expectedResult expected result after performing operation.
     */
    private void checkDeleteLastChar(String number, String expectedResult) {
        BigDecimal bigDecimalNumber = new BigDecimal(number);
        BigDecimal bigDecimalExpectedResult = new BigDecimal(expectedResult);

        assertEquals(bigDecimalExpectedResult, NumberEditor.deleteLastDigit(bigDecimalNumber));
    }
}
