package tests.view;

import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Window;
import org.junit.Test;
import util.RobotControl;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for testing {@link com.implemica.bormashenko.calculator.view.View}.
 *
 * @author Mykhailo Bormashenko
 */
public class ViewTest extends RobotControl {

    /**
     * Default application's width.
     */
    private static int DEFAULT_WIDTH = 322;

    /**
     * Default application's height.
     */
    private static int DEFAULT_HEIGHT = 501;

    /**
     * Default application's coordinate X.
     */
    private static int DEFAULT_X = 700;

    /**
     * Default application's coordinate Y.
     */
    private static int DEFAULT_Y = 150;

    /**
     * Constructor needed to extend {@link RobotControl}
     *
     * @throws AWTException signals that an Abstract Window Toolkit exception has occurred.
     */
    public ViewTest() throws AWTException {
        //nothing to construct
    }

    /**
     * Runs all tests.
     */
    @Test
    public void allTests() {
        colorAndFontTests();
        textTests();
        resizeTests();
        moveWindowTests();
        resizeScreenFontTests();
        maxScreenFontTest();
        resizeButtonsFontTests();
        visibleArrowsTests();
        expandTest();
        hideTest();
    }

    /**
     * Tests for background color, color of text and font for {@code Button} and {@code Label}.
     */
    @Test
    public void colorAndFontTests() {
        //colors
        String whiteColor = "0xffffffff";
        String mostlyWhileColor = "0xfafafaff";
        String veryLightGrayColor = "0xf0f0f0ff";
        String lightGrayColor = "0xe6e6e6ff";
        String grayColor = "0xd0d0d0ff";
        String veryDarkGrayColor = "0x333333ff";
        String vividRedColor = "0xe9091eff";
        String strongBlueColor = "0x0078d7ff";
        String equationLabelColor = "0x5b5b5bff";

        //fonts
        Font system_12 = new Font("System", 12);
        Font systemBold_14 = new Font("System Bold", 14);
        Font segoeUI_15 = new Font("Segoe UI", 15);
        Font segoeUI_20 = new Font("Segoe UI", 20);
        Font segoeBlack_16 = new Font("Segoe UI Black", 16);
        Font segoeSemilight_15 = new Font("Segoe UI Semilight", 15);
        Font segoeSemibold_12 = new Font("Segoe UI Semibold", 12);
        Font segoeSemibold_20 = new Font("Segoe UI Semibold", 20);
        Font segoeSemibold_23 = new Font("Segoe UI Semibold", 23);
        Font segoeSemibold_47 = new Font("Segoe UI Semibold", 47);
        Font segoeAssets_6 = new Font("Segoe MDL2 Assets", 6);
        Font segoeAssets_10 = new Font("Segoe MDL2 Assets", 10);
        Font segoeAssets_15 = new Font("Segoe MDL2 Assets", 15);
        Font segoeAssets_16 = new Font("Segoe MDL2 Assets", 16);
        Font calcAssets_15 = new Font("Calculator MDL2 Assets", 15);

        //buttons
        //digits
        checkColorAndFontForButtons(new String[]{ONE_ID, TWO_ID, THREE_ID, FOUR_ID, FIVE_ID, SIX_ID, SEVEN_ID,
                        EIGHT_ID, NINE_ID, ZERO_ID},
                mostlyWhileColor, veryDarkGrayColor, segoeSemibold_23,
                lightGrayColor, veryDarkGrayColor);

        //dot
        checkColorAndFontForButtons(new String[]{DOT_ID},
                veryLightGrayColor, veryDarkGrayColor, segoeBlack_16,
                lightGrayColor, veryDarkGrayColor);

        //math operations
        checkColorAndFontForButtons(new String[]{DIVIDE_ID, MULTIPLY_ID, SUBTRACT_ID, ADD_ID, EQUALS_ID},
                veryLightGrayColor, veryDarkGrayColor, segoeAssets_15,
                strongBlueColor, whiteColor);

        checkColorAndFontForButtons(new String[]{NEGATE_ID, PERCENT_ID, SQRT_ID},
                veryLightGrayColor, veryDarkGrayColor, segoeAssets_15,
                lightGrayColor, veryDarkGrayColor);

        checkColorAndFontForButtons(new String[]{SQR_ID, INVERSE_ID},
                veryLightGrayColor, veryDarkGrayColor, segoeUI_20,
                lightGrayColor, veryDarkGrayColor);

        //clear operations
        checkColorAndFontForButtons(new String[]{CLEAR_ALL_ID, CLEAR_TEXT_ID},
                veryLightGrayColor, veryDarkGrayColor, segoeUI_15,
                lightGrayColor, veryDarkGrayColor);

        checkColorAndFontForButtons(new String[]{BACKSPACE_ID},
                veryLightGrayColor, veryDarkGrayColor, segoeAssets_16,
                lightGrayColor, veryDarkGrayColor);

        //memory buttons
        checkColorAndFontForButtons(new String[]{MEMORY_CLEAR_ID, MEMORY_RECALL_ID, MEMORY_ADD_ID,
                        MEMORY_SUBTRACT_ID, MEMORY_STORE_ID, MEMORY_SHOW_ID},
                lightGrayColor, veryDarkGrayColor, segoeSemibold_12,
                lightGrayColor, veryDarkGrayColor);

        setNodeDisabled(MEMORY_CLEAR_ID, false);
        setNodeDisabled(MEMORY_RECALL_ID, false);

        checkColorAndFontForButtons(new String[]{MEMORY_CLEAR_ID, MEMORY_RECALL_ID},
                lightGrayColor, veryDarkGrayColor, segoeSemibold_12,
                lightGrayColor, veryDarkGrayColor);

        setNodeDisabled(MEMORY_CLEAR_ID, true);
        setNodeDisabled(MEMORY_RECALL_ID, true);

        setNodeDisabled(MEMORY_SHOW_ID, false);

        checkColorAndFontForButtons(new String[]{MEMORY_SHOW_ID},
                lightGrayColor, veryDarkGrayColor, segoeSemibold_12,
                grayColor, veryDarkGrayColor);

        setNodeDisabled(MEMORY_SHOW_ID, true);

        //navigation
        checkColorAndFontForButtons(new String[]{NAVIGATION_ID},
                lightGrayColor, veryDarkGrayColor, segoeAssets_16,
                grayColor, veryDarkGrayColor);

        //history
        checkColorAndFontForButtons(new String[]{HISTORY_ID},
                lightGrayColor, veryDarkGrayColor, segoeAssets_16,
                lightGrayColor, veryDarkGrayColor);

        //arrows
        setNodeVisible(RIGHT_ARROW_ID, true);
        setNodeVisible(LEFT_ARROW_ID, true);

        checkColorAndFontForButtons(new String[]{RIGHT_ARROW_ID, LEFT_ARROW_ID},
                lightGrayColor, strongBlueColor, system_12,
                grayColor, veryDarkGrayColor);

        setNodeVisible(RIGHT_ARROW_ID, false);
        setNodeVisible(LEFT_ARROW_ID, false);

        //minimize and expand
        checkColorAndFontForButtons(new String[]{HIDE_ID, EXPAND_ID},
                lightGrayColor, veryDarkGrayColor, segoeAssets_10,
                grayColor, veryDarkGrayColor);

        //close
        checkColorAndFontForButtons(new String[]{CLOSE_ID},
                lightGrayColor, veryDarkGrayColor, segoeAssets_10,
                vividRedColor, whiteColor);

        //navigation buttons in scroll pane
        setNodeVisible(NAVIGATION_BLOCK_ID, true);
        translateNodeByX(NAVIGATION_PANEL_ID, 256);
        translateNodeByX(ABOUT_PANEL_ID, 256);

        checkColorAndFontForButtons(new String[]{STANDARD_MODE_ID, SCIENTIFIC_MODE_ID, PROGRAMMER_MODE_ID,
                        DATE_CALCULATION_MODE_ID, CURRENCY_MODE_ID, VOLUME_MODE_ID, LENGTH_MODE_ID},
                lightGrayColor, veryDarkGrayColor, segoeUI_15,
                grayColor, veryDarkGrayColor);

        scrollNavigationBar();

        checkColorAndFontForButtons(new String[]{WEIGHT_AND_MASS_MODE_ID, TEMPERATURE_MODE_ID, ENERGY_MODE_ID,
                        AREA_MODE_ID},
                lightGrayColor, veryDarkGrayColor, segoeUI_15,
                grayColor, veryDarkGrayColor);

        scrollNavigationBar();

        checkColorAndFontForButtons(new String[]{SPEED_MODE_ID, TIME_MODE_ID, POWER_MODE_ID, DATA_MODE_ID,
                        PRESSURE_MODE_ID, ANGLE_MODE_ID, ABOUT_ID},
                lightGrayColor, veryDarkGrayColor, segoeUI_15,
                grayColor, veryDarkGrayColor);

        //check that standard operations buttons is under the navigation block,
        // while buttons in top panel are not under the block
        checkColorAndFontForButtons(new String[]{DIVIDE_ID, MULTIPLY_ID, SUBTRACT_ID, ADD_ID, EQUALS_ID},
                veryLightGrayColor, veryDarkGrayColor, segoeAssets_15,
                veryLightGrayColor, veryDarkGrayColor);

        //minimize and expand
        checkColorAndFontForButtons(new String[]{HIDE_ID, EXPAND_ID},
                lightGrayColor, veryDarkGrayColor, segoeAssets_10,
                grayColor, veryDarkGrayColor);

        //close
        checkColorAndFontForButtons(new String[]{CLOSE_ID},
                lightGrayColor, veryDarkGrayColor, segoeAssets_10,
                vividRedColor, whiteColor);

        setNodeVisible(NAVIGATION_BLOCK_ID, false);
        translateNodeByX(NAVIGATION_PANEL_ID, -256);
        translateNodeByX(ABOUT_PANEL_ID, -256);

        //check that navigation button is under the memory block,
        // while buttons in top panel are not under the block
        setNodeVisible(MEMORY_PANEL_ID, true);
        setNodeVisible(MEMORY_BLOCK_ID, true);

        checkColorAndFontForButtons(new String[]{NAVIGATION_ID},
                lightGrayColor, veryDarkGrayColor, segoeAssets_16,
                lightGrayColor, veryDarkGrayColor);

        //minimize and expand
        checkColorAndFontForButtons(new String[]{HIDE_ID, EXPAND_ID},
                lightGrayColor, veryDarkGrayColor, segoeAssets_10,
                grayColor, veryDarkGrayColor);

        //close
        checkColorAndFontForButtons(new String[]{CLOSE_ID},
                lightGrayColor, veryDarkGrayColor, segoeAssets_10,
                vividRedColor, whiteColor);

        setNodeVisible(MEMORY_PANEL_ID, false);
        setNodeVisible(MEMORY_BLOCK_ID, false);

        //labels
        checkColorAndFontForLabels(new String[]{TITLE_LABEL_ID}, veryDarkGrayColor, system_12);
        checkColorAndFontForLabels(new String[]{TYPE_LABEL_ID}, veryDarkGrayColor, segoeSemibold_20);
        checkColorAndFontForLabels(new String[]{EQUATION_LABEL_ID}, equationLabelColor, segoeSemilight_15);
        checkColorAndFontForLabels(new String[]{SCREEN_LABEL_ID}, veryDarkGrayColor, segoeSemibold_47);
        checkColorAndFontForLabels(new String[]{MODE_LABEL_ID, CONVERTER_LABEL_ID}, veryDarkGrayColor, systemBold_14);
        checkColorAndFontForLabels(new String[]{MEMORY_SHOW_SYMBOL}, veryDarkGrayColor, segoeAssets_6);
        checkColorAndFontForLabels(new String[]{STANDARD_MODE_SYMBOL, DATE_CALCULATION_MODE_SYMBOL,
                        LENGTH_MODE_SYMBOL, AREA_MODE_SYMBOL, POWER_MODE_SYMBOL, PRESSURE_MODE_SYMBOL,
                        ABOUT_SYMBOL},
                veryDarkGrayColor, segoeAssets_16);
        checkColorAndFontForLabels(new String[]{SCIENTIFIC_MODE_SYMBOL, PROGRAMMER_MODE_SYMBOL,
                        CURRENCY_MODE_SYMBOL, VOLUME_MODE_SYMBOL, WEIGHT_AND_MASS_MODE_SYMBOL,
                        TEMPERATURE_MODE_SYMBOL, ENERGY_MODE_SYMBOL, SPEED_MODE_SYMBOL, TIME_MODE_SYMBOL,
                        DATA_MODE_SYMBOL, ANGLE_MODE_SYMBOL},
                veryDarkGrayColor, calcAssets_15);
    }

    /**
     * Tests for text that {@code Button} and {@code Label} contain.
     */
    @Test
    public void textTests() {
        //buttons
        checkTextForButton(ONE_ID, "1");
        checkTextForButton(TWO_ID, "2");
        checkTextForButton(THREE_ID, "3");
        checkTextForButton(FOUR_ID, "4");
        checkTextForButton(FIVE_ID, "5");
        checkTextForButton(SIX_ID, "6");
        checkTextForButton(SEVEN_ID, "7");
        checkTextForButton(EIGHT_ID, "8");
        checkTextForButton(NINE_ID, "9");
        checkTextForButton(ZERO_ID, "0");
        checkTextForButton(DOT_ID, ".");

        checkTextForButton(ADD_ID, "\uE948");
        checkTextForButton(SUBTRACT_ID, "\uE949");
        checkTextForButton(MULTIPLY_ID, "\uE947");
        checkTextForButton(DIVIDE_ID, "\uE94A");
        checkTextForButton(EQUALS_ID, "\uE94E");
        checkTextForButton(PERCENT_ID, "\uE94C");
        checkTextForButton(NEGATE_ID, "\uE94D");
        checkTextForButton(SQR_ID, "\uD835\uDC65²");
        checkTextForButton(SQRT_ID, "\uE94B");
        checkTextForButton(INVERSE_ID, "⅟\uD835\uDC65");
        checkTextForButton(CLEAR_ALL_ID, "C");
        checkTextForButton(CLEAR_TEXT_ID, "CE");
        checkTextForButton(BACKSPACE_ID, "\uE94F");

        checkTextForButton(MEMORY_CLEAR_ID, "MC");
        checkTextForButton(MEMORY_RECALL_ID, "MR");
        checkTextForButton(MEMORY_ADD_ID, "M+");
        checkTextForButton(MEMORY_SUBTRACT_ID, "M-");
        checkTextForButton(MEMORY_STORE_ID, "MS");
        checkTextForButton(MEMORY_SHOW_ID, "M");

        checkTextForButton(NAVIGATION_ID, "\uE700");
        checkTextForButton(HISTORY_ID, "\uE81C");
        checkTextForButton(HIDE_ID, "\uE921");
        checkTextForButton(EXPAND_ID, "\uE922");
        checkTextForButton(CLOSE_ID, "\uE8BB");

        checkTextForButton(STANDARD_MODE_ID, "Standard");
        checkTextForButton(SCIENTIFIC_MODE_ID, "Scientific");
        checkTextForButton(PROGRAMMER_MODE_ID, "Programmer");
        checkTextForButton(DATE_CALCULATION_MODE_ID, "Date Calculation");
        checkTextForButton(CURRENCY_MODE_ID, "Currency");
        checkTextForButton(VOLUME_MODE_ID, "Volume");
        checkTextForButton(LENGTH_MODE_ID, "Length");
        checkTextForButton(WEIGHT_AND_MASS_MODE_ID, "Weight and Mass");
        checkTextForButton(TEMPERATURE_MODE_ID, "Temperature");
        checkTextForButton(ENERGY_MODE_ID, "Energy");
        checkTextForButton(AREA_MODE_ID, "Area");
        checkTextForButton(SPEED_MODE_ID, "Speed");
        checkTextForButton(TIME_MODE_ID, "Time");
        checkTextForButton(POWER_MODE_ID, "Power");
        checkTextForButton(DATA_MODE_ID, "Data");
        checkTextForButton(PRESSURE_MODE_ID, "Pressure");
        checkTextForButton(ANGLE_MODE_ID, "Angle");
        checkTextForButton(ABOUT_ID, "About");

        //labels
        checkTextForLabel(TITLE_LABEL_ID, "Calculator");
        checkTextForLabel(TYPE_LABEL_ID, "Standard");
        checkTextForLabel(EQUATION_LABEL_ID, "");
        checkTextForLabel(SCREEN_LABEL_ID, "0");
        checkTextForLabel(MODE_LABEL_ID, "Calculator");
        checkTextForLabel(CONVERTER_LABEL_ID, "Converter");
        checkTextForLabel(RIGHT_ARROW_SYMBOL, "\uE970");
        checkTextForLabel(LEFT_ARROW_SYMBOL, "\uE96F");
    }

    /**
     * Tests for resizing application.
     */
    @Test
    public void resizeTests() {
        int zero = 0;
        int maxOffsetUp = 150;
        int maxOffsetDown = 248;
        int fiveHundred = 500;

        //left up corner
        //left
        checkResize(DEFAULT_X, DEFAULT_Y, -fiveHundred, zero,
                DEFAULT_X - fiveHundred, DEFAULT_Y,
                DEFAULT_WIDTH + fiveHundred, DEFAULT_HEIGHT,
                Cursor.NW_RESIZE);

        //right
        checkResize(DEFAULT_X, DEFAULT_Y, fiveHundred, zero,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT,
                Cursor.NW_RESIZE);

        //up
        checkResize(DEFAULT_X, DEFAULT_Y, zero, -fiveHundred,
                DEFAULT_X, zero,
                DEFAULT_WIDTH, DEFAULT_HEIGHT + maxOffsetUp,
                Cursor.NW_RESIZE);

        //down
        checkResize(DEFAULT_X, DEFAULT_Y, zero, fiveHundred,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT,
                Cursor.NW_RESIZE);

        //left up
        checkResize(DEFAULT_X, DEFAULT_Y, -fiveHundred, -fiveHundred,
                DEFAULT_X - fiveHundred, zero,
                DEFAULT_WIDTH + fiveHundred, DEFAULT_HEIGHT + maxOffsetUp,
                Cursor.NW_RESIZE);

        //left down
        checkResize(DEFAULT_X, DEFAULT_Y, -fiveHundred, fiveHundred,
                DEFAULT_X - fiveHundred, DEFAULT_Y,
                DEFAULT_WIDTH + fiveHundred, DEFAULT_HEIGHT,
                Cursor.NW_RESIZE);

        //right up
        checkResize(DEFAULT_X, DEFAULT_Y, fiveHundred, -fiveHundred,
                DEFAULT_X, zero,
                DEFAULT_WIDTH, DEFAULT_HEIGHT + maxOffsetUp,
                Cursor.NW_RESIZE);

        //right down
        checkResize(DEFAULT_X, DEFAULT_Y, fiveHundred, fiveHundred,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT,
                Cursor.NW_RESIZE);

        //right up corner
        //left
        checkResize(DEFAULT_X + DEFAULT_WIDTH - 1, DEFAULT_Y, -fiveHundred, zero,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT,
                Cursor.NE_RESIZE);

        //right
        checkResize(DEFAULT_X + DEFAULT_WIDTH - 1, DEFAULT_Y, fiveHundred, zero,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH + fiveHundred - 1, DEFAULT_HEIGHT,
                Cursor.NE_RESIZE);

        //up
        checkResize(DEFAULT_X + DEFAULT_WIDTH - 1, DEFAULT_Y, zero, -fiveHundred,
                DEFAULT_X, zero,
                DEFAULT_WIDTH, DEFAULT_HEIGHT + maxOffsetUp,
                Cursor.NE_RESIZE);

        //down
        checkResize(DEFAULT_X + DEFAULT_WIDTH - 1, DEFAULT_Y, zero, fiveHundred,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT,
                Cursor.NE_RESIZE);

        //left up
        checkResize(DEFAULT_X + DEFAULT_WIDTH - 1, DEFAULT_Y, -fiveHundred, -fiveHundred,
                DEFAULT_X, zero,
                DEFAULT_WIDTH, DEFAULT_HEIGHT + maxOffsetUp,
                Cursor.NE_RESIZE);

        //left down
        checkResize(DEFAULT_X + DEFAULT_WIDTH - 1, DEFAULT_Y, -fiveHundred, fiveHundred,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT,
                Cursor.NE_RESIZE);

        //right up
        checkResize(DEFAULT_X + DEFAULT_WIDTH - 1, DEFAULT_Y, fiveHundred, -fiveHundred,
                DEFAULT_X, zero,
                DEFAULT_WIDTH + fiveHundred - 1, DEFAULT_HEIGHT + maxOffsetUp,
                Cursor.NE_RESIZE);

        //right down
        checkResize(DEFAULT_X + DEFAULT_WIDTH - 1, DEFAULT_Y, fiveHundred, fiveHundred,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH + fiveHundred - 1, DEFAULT_HEIGHT,
                Cursor.NE_RESIZE);

        //left down corner
        //left
        checkResize(DEFAULT_X, DEFAULT_Y + DEFAULT_HEIGHT - 1, -fiveHundred, zero,
                DEFAULT_X - fiveHundred, DEFAULT_Y,
                DEFAULT_WIDTH + fiveHundred, DEFAULT_HEIGHT,
                Cursor.SW_RESIZE);

        //right
        checkResize(DEFAULT_X, DEFAULT_Y + DEFAULT_HEIGHT - 1, fiveHundred, zero,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT,
                Cursor.SW_RESIZE);

        //up
        checkResize(DEFAULT_X, DEFAULT_Y + DEFAULT_HEIGHT - 1, zero, -fiveHundred,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT,
                Cursor.SW_RESIZE);

        //down
        checkResize(DEFAULT_X, DEFAULT_Y + DEFAULT_HEIGHT - 1, zero, fiveHundred,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT + maxOffsetDown,
                Cursor.SW_RESIZE);

        //left up
        checkResize(DEFAULT_X, DEFAULT_Y + DEFAULT_HEIGHT - 1, -fiveHundred, -fiveHundred,
                DEFAULT_X - fiveHundred, DEFAULT_Y,
                DEFAULT_WIDTH + fiveHundred, DEFAULT_HEIGHT,
                Cursor.SW_RESIZE);

        //left down
        checkResize(DEFAULT_X, DEFAULT_Y + DEFAULT_HEIGHT - 1, -fiveHundred, fiveHundred,
                DEFAULT_X - fiveHundred, DEFAULT_Y,
                DEFAULT_WIDTH + fiveHundred, DEFAULT_HEIGHT + maxOffsetDown,
                Cursor.SW_RESIZE);

        //right up
        checkResize(DEFAULT_X, DEFAULT_Y + DEFAULT_HEIGHT - 1, fiveHundred, -fiveHundred,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT,
                Cursor.SW_RESIZE);

        //right down
        checkResize(DEFAULT_X, DEFAULT_Y + DEFAULT_HEIGHT - 1, fiveHundred, fiveHundred,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT + maxOffsetDown,
                Cursor.SW_RESIZE);

        //right down corner
        //left
        checkResize(DEFAULT_X + DEFAULT_WIDTH - 1, DEFAULT_Y + DEFAULT_HEIGHT - 1, -fiveHundred, zero,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT,
                Cursor.SE_RESIZE);

        //right
        checkResize(DEFAULT_X + DEFAULT_WIDTH - 1, DEFAULT_Y + DEFAULT_HEIGHT - 1, fiveHundred, zero,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH + fiveHundred - 1, DEFAULT_HEIGHT,
                Cursor.SE_RESIZE);

        //up
        checkResize(DEFAULT_X + DEFAULT_WIDTH - 1, DEFAULT_Y + DEFAULT_HEIGHT - 1, zero, -fiveHundred,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT,
                Cursor.SE_RESIZE);

        //down
        checkResize(DEFAULT_X + DEFAULT_WIDTH - 1, DEFAULT_Y + DEFAULT_HEIGHT - 1, zero, fiveHundred,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT + maxOffsetDown,
                Cursor.SE_RESIZE);

        //left up
        checkResize(DEFAULT_X + DEFAULT_WIDTH - 1, DEFAULT_Y + DEFAULT_HEIGHT - 1,
                -fiveHundred, -fiveHundred,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT,
                Cursor.SE_RESIZE);

        //left down
        checkResize(DEFAULT_X + DEFAULT_WIDTH - 1, DEFAULT_Y + DEFAULT_HEIGHT - 1,
                -fiveHundred, fiveHundred,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT + maxOffsetDown,
                Cursor.SE_RESIZE);

        //right up
        checkResize(DEFAULT_X + DEFAULT_WIDTH - 1, DEFAULT_Y + DEFAULT_HEIGHT - 1,
                fiveHundred, -fiveHundred,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH + fiveHundred - 1, DEFAULT_HEIGHT,
                Cursor.SE_RESIZE);

        //right down
        checkResize(DEFAULT_X + DEFAULT_WIDTH - 1, DEFAULT_Y + DEFAULT_HEIGHT - 1,
                fiveHundred, fiveHundred,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH + fiveHundred - 1, DEFAULT_HEIGHT + maxOffsetDown,
                Cursor.SE_RESIZE);

        //left side
        //left
        checkResize(DEFAULT_X + 1, DEFAULT_Y + (DEFAULT_WIDTH / 2), -fiveHundred, zero,
                DEFAULT_X - fiveHundred + 1, DEFAULT_Y,
                DEFAULT_WIDTH + fiveHundred - 1, DEFAULT_HEIGHT,
                Cursor.H_RESIZE);

        //right
        checkResize(DEFAULT_X + 1, DEFAULT_Y + (DEFAULT_WIDTH / 2), fiveHundred, zero,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT,
                Cursor.H_RESIZE);

        //up
        checkResize(DEFAULT_X + 1, DEFAULT_Y + (DEFAULT_WIDTH / 2), zero, -fiveHundred,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT,
                Cursor.H_RESIZE);

        //down
        checkResize(DEFAULT_X + 1, DEFAULT_Y + (DEFAULT_WIDTH / 2), zero, fiveHundred,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT,
                Cursor.H_RESIZE);


        //left up
        checkResize(DEFAULT_X + 1, DEFAULT_Y + (DEFAULT_WIDTH / 2), -fiveHundred, -fiveHundred,
                DEFAULT_X - fiveHundred + 1, DEFAULT_Y,
                DEFAULT_WIDTH + fiveHundred - 1, DEFAULT_HEIGHT,
                Cursor.H_RESIZE);

        //left down
        checkResize(DEFAULT_X + 1, DEFAULT_Y + (DEFAULT_WIDTH / 2), -fiveHundred, fiveHundred,
                DEFAULT_X - fiveHundred + 1, DEFAULT_Y,
                DEFAULT_WIDTH + fiveHundred - 1, DEFAULT_HEIGHT,
                Cursor.H_RESIZE);

        //right up
        checkResize(DEFAULT_X + 1, DEFAULT_Y + (DEFAULT_WIDTH / 2), fiveHundred, -fiveHundred,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT,
                Cursor.H_RESIZE);

        //right down
        checkResize(DEFAULT_X + 1, DEFAULT_Y + (DEFAULT_WIDTH / 2), fiveHundred, fiveHundred,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT,
                Cursor.H_RESIZE);

        //right side
        //left
        checkResize(DEFAULT_X + DEFAULT_WIDTH - 1, DEFAULT_Y + (DEFAULT_HEIGHT / 2),
                -fiveHundred, zero,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT,
                Cursor.H_RESIZE);

        //right
        checkResize(DEFAULT_X + DEFAULT_WIDTH - 1, DEFAULT_Y + (DEFAULT_HEIGHT / 2),
                fiveHundred, zero,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH + fiveHundred - 1, DEFAULT_HEIGHT,
                Cursor.H_RESIZE);

        //up
        checkResize(DEFAULT_X + DEFAULT_WIDTH - 1, DEFAULT_Y + (DEFAULT_HEIGHT / 2),
                zero, -fiveHundred,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT,
                Cursor.H_RESIZE);

        //down
        checkResize(DEFAULT_X + DEFAULT_WIDTH - 1, DEFAULT_Y + (DEFAULT_HEIGHT / 2),
                zero, fiveHundred,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT,
                Cursor.H_RESIZE);


        //left up
        checkResize(DEFAULT_X + DEFAULT_WIDTH - 1, DEFAULT_Y + (DEFAULT_HEIGHT / 2),
                -fiveHundred, -fiveHundred,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT,
                Cursor.H_RESIZE);

        //left down
        checkResize(DEFAULT_X + DEFAULT_WIDTH - 1, DEFAULT_Y + (DEFAULT_HEIGHT / 2),
                -fiveHundred, fiveHundred,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT,
                Cursor.H_RESIZE);

        //right up
        checkResize(DEFAULT_X + DEFAULT_WIDTH - 1, DEFAULT_Y + (DEFAULT_HEIGHT / 2),
                fiveHundred, -fiveHundred,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH + fiveHundred - 1, DEFAULT_HEIGHT,
                Cursor.H_RESIZE);

        //right down
        checkResize(DEFAULT_X + DEFAULT_WIDTH - 1, DEFAULT_Y + (DEFAULT_HEIGHT / 2),
                fiveHundred, fiveHundred,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH + fiveHundred - 1, DEFAULT_HEIGHT,
                Cursor.H_RESIZE);

        //top side
        //left
        checkResize(DEFAULT_X + (DEFAULT_WIDTH / 2), DEFAULT_Y, -fiveHundred, zero,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT,
                Cursor.V_RESIZE);

        //right
        checkResize(DEFAULT_X + (DEFAULT_WIDTH / 2), DEFAULT_Y, fiveHundred, zero,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT,
                Cursor.V_RESIZE);

        //up
        checkResize(DEFAULT_X + (DEFAULT_WIDTH / 2), DEFAULT_Y, zero, -fiveHundred,
                DEFAULT_X, zero,
                DEFAULT_WIDTH, DEFAULT_HEIGHT + maxOffsetUp,
                Cursor.V_RESIZE);

        //down
        checkResize(DEFAULT_X + (DEFAULT_WIDTH / 2), DEFAULT_Y, zero, fiveHundred,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT,
                Cursor.V_RESIZE);


        //left up
        checkResize(DEFAULT_X + (DEFAULT_WIDTH / 2), DEFAULT_Y, -fiveHundred, -fiveHundred,
                DEFAULT_X, zero,
                DEFAULT_WIDTH, DEFAULT_HEIGHT + maxOffsetUp,
                Cursor.V_RESIZE);

        //left down
        checkResize(DEFAULT_X + (DEFAULT_WIDTH / 2), DEFAULT_Y, -fiveHundred, fiveHundred,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT,
                Cursor.V_RESIZE);

        //right up
        checkResize(DEFAULT_X + (DEFAULT_WIDTH / 2), DEFAULT_Y, fiveHundred, -fiveHundred,
                DEFAULT_X, zero,
                DEFAULT_WIDTH, DEFAULT_HEIGHT + maxOffsetUp,
                Cursor.V_RESIZE);

        //right down
        checkResize(DEFAULT_X + (DEFAULT_WIDTH / 2), DEFAULT_Y, fiveHundred, fiveHundred,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT,
                Cursor.V_RESIZE);

        //bottom side
        //left
        checkResize(DEFAULT_X + (DEFAULT_WIDTH / 2), DEFAULT_Y + DEFAULT_HEIGHT - 1,
                -fiveHundred, zero,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT,
                Cursor.V_RESIZE);

        //right
        checkResize(DEFAULT_X + (DEFAULT_WIDTH / 2), DEFAULT_Y + DEFAULT_HEIGHT - 1,
                fiveHundred, zero,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT,
                Cursor.V_RESIZE);

        //up
        checkResize(DEFAULT_X + (DEFAULT_WIDTH / 2), DEFAULT_Y + DEFAULT_HEIGHT - 1,
                zero, -fiveHundred,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT,
                Cursor.V_RESIZE);

        //down
        checkResize(DEFAULT_X + (DEFAULT_WIDTH / 2), DEFAULT_Y + DEFAULT_HEIGHT - 1,
                zero, fiveHundred,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT + maxOffsetDown,
                Cursor.V_RESIZE);


        //left up
        checkResize(DEFAULT_X + (DEFAULT_WIDTH / 2), DEFAULT_Y + DEFAULT_HEIGHT - 1,
                -fiveHundred, -fiveHundred,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT,
                Cursor.V_RESIZE);

        //left down
        checkResize(DEFAULT_X + (DEFAULT_WIDTH / 2), DEFAULT_Y + DEFAULT_HEIGHT - 1,
                -fiveHundred, fiveHundred,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT + maxOffsetDown,
                Cursor.V_RESIZE);

        //right up
        checkResize(DEFAULT_X + (DEFAULT_WIDTH / 2), DEFAULT_Y + DEFAULT_HEIGHT - 1,
                fiveHundred, -fiveHundred,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT,
                Cursor.V_RESIZE);

        //right down
        checkResize(DEFAULT_X + (DEFAULT_WIDTH / 2), DEFAULT_Y + DEFAULT_HEIGHT - 1,
                fiveHundred, fiveHundred,
                DEFAULT_X, DEFAULT_Y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT + maxOffsetDown,
                Cursor.V_RESIZE);
    }

    /**
     * Tests for moving window.
     */
    @Test
    public void moveWindowTests() {
        int zero = 0;
        int fifty = 50;
        int five_hundred = 500;
        int locationEdge = -20;

        //move horizontal
        //right
        checkMovingWindow(fifty, zero, DEFAULT_X + fifty, DEFAULT_Y);
        checkMovingWindow(five_hundred, zero, DEFAULT_X + five_hundred, DEFAULT_Y);

        //left
        checkMovingWindow(-fifty, zero, DEFAULT_X - fifty, DEFAULT_Y);
        checkMovingWindow(-five_hundred, zero, DEFAULT_X - five_hundred, DEFAULT_Y);

        //move vertical
        //up
        checkMovingWindow(zero, -fifty, DEFAULT_X, DEFAULT_Y - fifty);
        checkMovingWindow(zero, -five_hundred, DEFAULT_X, locationEdge);

        //down
        checkMovingWindow(zero, fifty, DEFAULT_X, DEFAULT_Y + fifty);
        checkMovingWindow(zero, five_hundred, DEFAULT_X, DEFAULT_Y + five_hundred);

        //horizontal and vertical
        //right up
        checkMovingWindow(fifty, -fifty, DEFAULT_X + fifty, DEFAULT_Y - fifty);
        checkMovingWindow(five_hundred, -fifty, DEFAULT_X + five_hundred, DEFAULT_Y - fifty);

        checkMovingWindow(fifty, -five_hundred, DEFAULT_X + fifty, locationEdge);
        checkMovingWindow(five_hundred, -five_hundred, DEFAULT_X + five_hundred, locationEdge);

        //right down
        checkMovingWindow(fifty, fifty, DEFAULT_X + fifty, DEFAULT_Y + fifty);
        checkMovingWindow(five_hundred, fifty, DEFAULT_X + five_hundred, DEFAULT_Y + fifty);

        checkMovingWindow(fifty, five_hundred, DEFAULT_X + fifty, DEFAULT_Y + five_hundred);
        checkMovingWindow(five_hundred, five_hundred, DEFAULT_X + five_hundred,
                DEFAULT_Y + five_hundred);

        //left up
        checkMovingWindow(-fifty, -fifty, DEFAULT_X - fifty, DEFAULT_Y - fifty);
        checkMovingWindow(-five_hundred, -fifty, DEFAULT_X - five_hundred, DEFAULT_Y - fifty);

        checkMovingWindow(-fifty, -five_hundred, DEFAULT_X - fifty, locationEdge);
        checkMovingWindow(-five_hundred, -five_hundred, DEFAULT_X - five_hundred, locationEdge);

        //left down
        checkMovingWindow(-fifty, fifty, DEFAULT_X - fifty, DEFAULT_Y + fifty);
        checkMovingWindow(-five_hundred, fifty, DEFAULT_X - five_hundred, DEFAULT_Y + fifty);

        checkMovingWindow(-fifty, five_hundred, DEFAULT_X - fifty, DEFAULT_Y + five_hundred);
        checkMovingWindow(-five_hundred, five_hundred, DEFAULT_X - five_hundred,
                DEFAULT_Y + five_hundred);
    }

    /**
     * Tests for resizing font in screen {@code Label}.
     */
    @Test
    public void resizeScreenFontTests() {
        checkResizeFontScreen("1", 0, 47);
        checkResizeFontScreen("1 2", 0, 47);
        checkResizeFontScreen("1 2 3 4 5 6 7 8 9 0", 0, 46);
        checkResizeFontScreen("0 . 1 F9 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6", 0, 28);

        checkResizeFontScreen("1", 50, 47);
        checkResizeFontScreen("1 2", 50, 47);
        checkResizeFontScreen("1 2 3 4 5 6 7 8 9 0 1", 50, 47);
        checkResizeFontScreen("0 . 1 F9 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6", 50, 34);
    }

    /**
     * Test for max size of font for screen {@code Label}.
     */
    @Test
    public void maxScreenFontTest() {
        setWindowsSizeAndLayout(DEFAULT_WIDTH, DEFAULT_HEIGHT, 0, 0);
        dragFromTo(DEFAULT_WIDTH / 2, DEFAULT_HEIGHT - 1, DEFAULT_WIDTH / 2, 800);

        assertEquals(72, getLabeledBySelector(SCREEN_LABEL_ID).getFont().getSize());
    }

    /**
     * Tests for resizing font in application's {@code Button}.
     */
    @Test
    public void resizeButtonsFontTests() {
        Button[] operationButtons = new Button[]{
                getButtonBySelector(PERCENT_ID),
                getButtonBySelector(SQRT_ID),
                getButtonBySelector(DIVIDE_ID),
                getButtonBySelector(MULTIPLY_ID),
                getButtonBySelector(SUBTRACT_ID),
                getButtonBySelector(ADD_ID),
                getButtonBySelector(EQUALS_ID),
                getButtonBySelector(NEGATE_ID),
                getButtonBySelector(CLEAR_TEXT_ID),
                getButtonBySelector(CLEAR_ALL_ID)
        };

        Button[] backspaceAndDot = new Button[]{
                getButtonBySelector(BACKSPACE_ID),
                getButtonBySelector(DOT_ID)
        };

        Button[] degreeButtons = new Button[]{
                getButtonBySelector(SQR_ID),
                getButtonBySelector(INVERSE_ID)
        };

        Button[] digitsButtons = new Button[]{
                getButtonBySelector(ZERO_ID),
                getButtonBySelector(ONE_ID),
                getButtonBySelector(TWO_ID),
                getButtonBySelector(THREE_ID),
                getButtonBySelector(FOUR_ID),
                getButtonBySelector(FIVE_ID),
                getButtonBySelector(SIX_ID),
                getButtonBySelector(SEVEN_ID),
                getButtonBySelector(EIGHT_ID),
                getButtonBySelector(NINE_ID),
        };

        checkResizeFontButtons(operationButtons, 100, 300, 20);
        checkResizeFontButtons(backspaceAndDot, 200, 400, 21);
        checkResizeFontButtons(degreeButtons, 100, 300, 24);
        checkResizeFontButtons(digitsButtons, 200, 400, 29);
    }

    /**
     * Test for visibility of arrow {@code Button}.
     */
    @Test
    public void visibleArrowsTests() {
        int numberOfSqrClicks = 10;

        setWindowsSizeAndLayout(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_X, DEFAULT_Y);

        Button leftArrow = getButtonBySelector(LEFT_ARROW_ID);
        Button rightArrow = getButtonBySelector(RIGHT_ARROW_ID);
        assertFalse(leftArrow.isVisible());
        assertFalse(rightArrow.isVisible());

        for (int i = 0; i < numberOfSqrClicks; i++) {
            clickOn(getButtonBySelector(SQR_ID));
        }

        assertTrue(leftArrow.isVisible());
        assertFalse(rightArrow.isVisible());

        dragFromTo(DEFAULT_X, DEFAULT_Y, 0, 0);

        assertFalse(leftArrow.isVisible());
        assertFalse(rightArrow.isVisible());

        dragFromTo(0, 0, DEFAULT_X, DEFAULT_Y);

        assertTrue(leftArrow.isVisible());
        assertFalse(rightArrow.isVisible());
    }

    /**
     * Test for expanding application.
     */
    @Test
    public void expandTest() {
        String minimizedSymbol = "\uE922";
        String maximizedSymbol = "\uE923";

        Button expand = getButtonBySelector(EXPAND_ID);
        clickOn(expand);
        assertTrue(stage.isMaximized());
        assertEquals(expand.getText(), maximizedSymbol);

        clickOn(expand);
        assertFalse(stage.isMaximized());
        assertEquals(expand.getText(), minimizedSymbol);
    }

    /**
     * Test for hiding application.
     */
    @Test
    public void hideTest() {
        clickOn(getButtonBySelector(HIDE_ID));
        assertTrue(stage.isIconified());
    }

    /**
     * Checks that {@code Button} has required color, required text color, required font, and the same information
     * while the {@code Button} is hovered.
     *
     * @param selectors                array with {@code Button}'s selectors.
     * @param expectedColor            background color that should be set for every {@code Button} in array.
     * @param expectedTextColor        text color that should be set for every {@code Button} in array.
     * @param expectedFont             font that should be set for every {@code Button} in array.
     * @param expectedColorOnHover     background color that should be set for every {@code Button} in array while
     *                                 the {@code Button} is hovered.
     * @param expectedTextColorOnHover text color that should be set for every {@code Button} in array while the
     *                                 {@code Button} is hovered.
     */
    private void checkColorAndFontForButtons(String[] selectors,
                                             String expectedColor, String expectedTextColor,
                                             Font expectedFont,
                                             String expectedColorOnHover, String expectedTextColorOnHover) {
        for (String selector : selectors) {
            Button button = getButtonBySelector(selector);
            assertEquals(expectedColor, button.getBackground().getFills().get(0).getFill().toString());
            assertEquals(expectedTextColor, button.getTextFill().toString());
            assertEquals(expectedFont, button.getFont());

            hoverOn(button);

            assertEquals(expectedColorOnHover, button.getBackground().getFills().get(0).getFill().toString());
            assertEquals(expectedTextColorOnHover, button.getTextFill().toString());
        }
    }

    /**
     * Checks that {@code Label} have required text color and font.
     *
     * @param selectors         array with {@code Label}'s selectors.
     * @param expectedTextColor text color that should be set for every {@code Label} in array.
     * @param expectedFont      font that should be set for every {@code Label} in array.
     */
    private void checkColorAndFontForLabels(String[] selectors, String expectedTextColor, Font expectedFont) {
        for (String selector : selectors) {
            Labeled labeled = getLabeledBySelector(selector);
            assertEquals(expectedTextColor, labeled.getTextFill().toString());
            assertEquals(expectedFont, labeled.getFont());
        }
    }

    /**
     * Checks that {@code Button} has required text.
     *
     * @param selector     {@code Button}'s selector.
     * @param expectedText text that {@code Button} should contain.
     */
    private void checkTextForButton(String selector, String expectedText) {
        Button button = getButtonBySelector(selector);
        assertEquals(expectedText, button.getText());
    }

    /**
     * Checks that {@code Label} has required text.
     *
     * @param selector     {@code Label}'s selector.
     * @param expectedText text that {@code Label} should contain.
     */
    private void checkTextForLabel(String selector, String expectedText) {
        Labeled labeled = getLabeledBySelector(selector);
        assertEquals(expectedText, labeled.getText());
    }

    /**
     * Checks that window has required location and size after being resized.
     *
     * @param fromX          coordinate X from where cursor should start resizing.
     * @param fromY          coordinate Y from where cursor should start resizing.
     * @param offsetX        offset for moving cursor by X coordinate.
     * @param offsetY        offset for moving cursor by Y coordinate.
     * @param expectedX      coordinate X that window should has after resizing.
     * @param expectedY      coordinate Y that window should has after resizing.
     * @param expectedHeight height that window should has after resizing.
     * @param expectedWidth  window that window should has after resizing.
     */
    private void checkResize(int fromX, int fromY, int offsetX, int offsetY,
                             double expectedX, double expectedY, double expectedWidth, double expectedHeight,
                             Cursor expectedCursor) {
        hoverOn((int) (stage.getX() + stage.getWidth() / 2), (int) (stage.getY() + stage.getHeight() / 2));
        setWindowsSizeAndLayout(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_X, DEFAULT_Y);

        hoverOn(fromX, fromY);

        Window window = getWindowByIndex(0);
        assertEquals(expectedCursor, window.getScene().getCursor());

        dragFromTo(fromX, fromY, fromX + offsetX, fromY + offsetY);

        assertEquals(expectedX, window.getX());
        assertEquals(expectedY, window.getY());
        assertEquals(expectedWidth, window.getWidth());
        assertEquals(expectedHeight, window.getHeight());
    }

    /**
     * Checks that window has required location after moving it by dragging top panel.
     *
     * @param offsetX   offset for moving cursor by X coordinate.
     * @param offsetY   offset for moving cursor by Y coordinate.
     * @param expectedX coordinate X that window should has after moving.
     * @param expectedY coordinate Y that window should has after moving.
     */
    private void checkMovingWindow(int offsetX, int offsetY, double expectedX, double expectedY) {
        setWindowsSizeAndLayout(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_X, DEFAULT_Y);

        AnchorPane topPanel = (AnchorPane) getNodeBySelector(TOP_PANEL_ID);
        Bounds bounds = topPanel.localToScreen(topPanel.getBoundsInLocal());

        int centerX = (int) (bounds.getMinX() + bounds.getMaxX()) / 2;
        int centerY = (int) (bounds.getMinY() + bounds.getMaxY()) / 2;

        dragFromTo(centerX, centerY, centerX + offsetX, centerY + offsetY);

        Window window = getWindowByIndex(0);
        assertEquals(expectedX, window.getX());
        assertEquals(expectedY, window.getY());
    }

    /**
     * Checks that font for screen {@code Label} changes while text in the {@code Label} or window's width changes.
     *
     * @param text             buttons to press on keyboard.
     * @param offsetX          offset X to drag window.
     * @param expectedFontSize size of font that screen {@code Label} should has.
     */
    private void checkResizeFontScreen(String text, int offsetX, double expectedFontSize) {
        setWindowsSizeAndLayout(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_X, DEFAULT_Y);

        resetAll();

        pressKeyboard(text);
        dragFromTo(DEFAULT_X, DEFAULT_Y, DEFAULT_X - offsetX, DEFAULT_Y);

        Labeled labeled = getLabeledBySelector(SCREEN_LABEL_ID);
        assertEquals(expectedFontSize, labeled.getFont().getSize());
    }

    /**
     * Checks that {@code Button}s have required font size after resizing application.
     *
     * @param buttons          {@code Button}s that should have required font size.
     * @param offsetX          offset X to drag window.
     * @param offsetY          offset Y to drag window.
     * @param expectedFontSize size of font that each required {@code Button} should have.
     */
    private void checkResizeFontButtons(Button[] buttons, int offsetX, int offsetY,
                                        double expectedFontSize) {
        setWindowsSizeAndLayout(DEFAULT_WIDTH, DEFAULT_HEIGHT, 0, 0);

        dragFromTo(DEFAULT_WIDTH - 1, DEFAULT_HEIGHT - 1,
                DEFAULT_WIDTH + offsetX - 1, DEFAULT_HEIGHT + offsetY - 1);

        for (Button button : buttons) {
            assertEquals(expectedFontSize, button.getFont().getSize());
        }
    }


}
