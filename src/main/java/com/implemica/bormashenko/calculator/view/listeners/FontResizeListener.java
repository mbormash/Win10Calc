package com.implemica.bormashenko.calculator.view.listeners;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Listener for resizing font in screen {@code Label}.
 *
 * @author Mykhailo Bormashenko
 */
public class FontResizeListener implements InvalidationListener {

    /**
     * Max standard size of font for screen {@code Label}.
     */
    private static final int MAX_STANDARD_FONT_SIZE = 47;

    /**
     * Increased size of font for screen {@code Label}.
     */
    private static final int INCREASED_FONT_SIZE = 72;

    /**
     * If width of text in {@code Label} more than application's width minus this value, font size for screen
     * {@code Label} should be reduced.
     */
    private static final int WIDTH_DIFF_TO_REDUCE = 35;

    /**
     * If width of text in {@code Label} less than applications's width minus this value, font size for screen
     * {@code Label} should be increased.
     */
    private static final int WIDTH_DIFF_TO_INCREASE = 50;

    /**
     * Application's height from which fonts' sizes should be increased.
     */
    private static final int HEIGHT_FOR_INCREASING = 800;

    /**
     * Application's width from which fonts' sizes should be increased.
     */
    private static final int WIDTH_FOR_INCREASING = 400;

    /**
     * Standard font size for operation {@code Button}.
     */
    private static final int STANDARD_OPERATIONS_FONT = 15;

    /**
     * Standard font size for backspace and dot {@code Button}.
     */
    private static final int STANDARD_BACKSPACE_AND_DOT_FONT = 16;

    /**
     * Standard font size for degree {@code Button}.
     */
    private static final int STANDARD_DEGREE_FONT = 20;

    /**
     * Standard font size for digits {@code Button}.
     */
    private static final int STANDARD_DIGITS_FONT = 23;

    /**
     * Increased font size for operation {@code Button}.
     */
    private static final int INCREASED_OPERATIONS_FONT = 20;

    /**
     * Increased font size for backspace and dot {@code Button}.
     */
    private static final int INCREASED_BACKSPACE_AND_DOT_FONT = 21;

    /**
     * Increased font size for degree {@code Button}.
     */
    private static final int INCREASED_DEGREE_FONT = 24;

    /**
     * Increased font size for digits {@code Button}.
     */
    private static final int INCREASED_DIGITS_FONT = 29;

    /**
     * ID of screen {@code Label}.
     */
    private static final String SCREEN_LABEL_ID = "#screen";

    /**
     * ID of percent {@code Button}.
     */
    private static final String PERCENT_BUTTON_ID = "#percent";

    /**
     * ID of sqrt {@code Button}.
     */
    private static final String SQRT_BUTTON_ID = "#sqrt";

    /**
     * ID of divide {@code Button}.
     */
    private static final String DIVIDE_BUTTON_ID = "#divide";

    /**
     * ID of multiply {@code Button}.
     */
    private static final String MULTIPLY_BUTTON_ID = "#multiply";

    /**
     * ID of subtract {@code Button}.
     */
    private static final String SUBTRACT_BUTTON_ID = "#subtract";

    /**
     * ID of add {@code Button}.
     */
    private static final String ADD_BUTTON_ID = "#add";

    /**
     * ID of equals {@code Button}.
     */
    private static final String EQUALS_BUTTON_ID = "#equals";

    /**
     * ID of negate {@code Button}.
     */
    private static final String NEGATE_BUTTON_ID = "#negate";

    /**
     * ID of clear text {@code Button}.
     */
    private static final String CLEAR_TEXT_BUTTON_ID = "#clearText";

    /**
     * ID of clear all {@code Button}.
     */
    private static final String CLEAR_ALL_BUTTON_ID = "#clearAll";

    /**
     * ID of backspace {@code Button}.
     */
    private static final String BACKSPACE_BUTTON_ID = "#backspace";

    /**
     * ID of dot {@code Button}.
     */
    private static final String DOT_BUTTON_ID = "#dot";

    /**
     * ID of sqr {@code Button}.
     */
    private static final String SQR_BUTTON_ID = "#sqr";

    /**
     * ID of inverse {@code Button}.
     */
    private static final String INVERSE_BUTTON_ID = "#inverse";

    /**
     * ID of zero {@code Button}.
     */
    private static final String ZERO_BUTTON_ID = "#zero";

    /**
     * ID of one {@code Button}.
     */
    private static final String ONE_BUTTON_ID = "#one";

    /**
     * ID of two {@code Button}.
     */
    private static final String TWO_BUTTON_ID = "#two";

    /**
     * ID of three {@code Button}.
     */
    private static final String THREE_BUTTON_ID = "#three";

    /**
     * ID of four {@code Button}.
     */
    private static final String FOUR_BUTTON_ID = "#four";

    /**
     * ID of five {@code Button}.
     */
    private static final String FIVE_BUTTON_ID = "#five";

    /**
     * ID of six {@code Button}.
     */
    private static final String SIX_BUTTON_ID = "#six";

    /**
     * ID of seven {@code Button}.
     */
    private static final String SEVEN_BUTTON_ID = "#seven";

    /**
     * ID of eight {@code Button}.
     */
    private static final String EIGHT_BUTTON_ID = "#eight";

    /**
     * ID of nine {@code Button}.
     */
    private static final String NINE_BUTTON_ID = "#nine";

    /**
     * Font family of {@code Segoe MDL2 Assets} font.
     */
    private static final String FONT_ASSETS = "Segoe MDL2 Assets";

    /**
     * Font family of {@code Segoe UI} font.
     */
    private static final String FONT_SEGOE_UI = "Segoe UI";

    /**
     * Font family of {@code Segoe UI Semibold} font.
     */
    private static final String FONT_SEGOE_UI_SEMIBOLD = "Segoe UI Semibold";

    /**
     * Font family of {@code Segoe UI Black} font.
     */
    private static final String FONT_SEGOE_UI_BLACK = "Segoe UI Black";

    /**
     * JavaFX {@code Scene}.
     */
    private Scene scene;

    /**
     * Constructor for listener.
     *
     * @param scene JavaFX {@code Scene}.
     */
    public FontResizeListener(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void invalidated(Observable observable) {
        setFontSizeForButtons();

        Label label = (Label) scene.lookup(SCREEN_LABEL_ID);

        Text text = new Text(label.getText());
        text.setFont(label.getFont());
        int width = (int) text.getBoundsInLocal().getWidth();
        int fontSize = (int) label.getFont().getSize();
        int widthToReduce = (int) (scene.getWidth() - WIDTH_DIFF_TO_REDUCE);

        while (width > widthToReduce) {
            text.setFont(new Font(fontSize--));
            width = (int) text.getBoundsInLocal().getWidth();
        }

        int widthToIncrease = (int) (scene.getWidth() - WIDTH_DIFF_TO_INCREASE);

        while (width < widthToIncrease) {
            text.setFont(new Font(fontSize++));
            width = (int) text.getBoundsInLocal().getWidth();
        }

        if (fontSize > MAX_STANDARD_FONT_SIZE) {
            fontSize = MAX_STANDARD_FONT_SIZE;
        }

        if (scene.getHeight() >= HEIGHT_FOR_INCREASING) {
            fontSize = INCREASED_FONT_SIZE;
        }

        label.setStyle(getFontString(fontSize, FONT_SEGOE_UI_SEMIBOLD));
    }

    /**
     * Sets font size for several {@code Button} in application.
     * <p>
     * If application's size more than required size, font size should be increased, otherwise in should be set to
     * standard.
     */
    private void setFontSizeForButtons() {
        int operationFont = STANDARD_OPERATIONS_FONT;
        int backspaceAndDotFont = STANDARD_BACKSPACE_AND_DOT_FONT;
        int degreeFont = STANDARD_DEGREE_FONT;
        int digitsFont = STANDARD_DIGITS_FONT;

        if (scene.getHeight() >= HEIGHT_FOR_INCREASING && scene.getWidth() >= WIDTH_FOR_INCREASING) {
            operationFont = INCREASED_OPERATIONS_FONT;
            backspaceAndDotFont = INCREASED_BACKSPACE_AND_DOT_FONT;
            degreeFont = INCREASED_DEGREE_FONT;
            digitsFont = INCREASED_DIGITS_FONT;
        }

        Button[] operationButtonsAssets = new Button[]{
                (Button) scene.lookup(PERCENT_BUTTON_ID),
                (Button) scene.lookup(SQRT_BUTTON_ID),
                (Button) scene.lookup(DIVIDE_BUTTON_ID),
                (Button) scene.lookup(MULTIPLY_BUTTON_ID),
                (Button) scene.lookup(SUBTRACT_BUTTON_ID),
                (Button) scene.lookup(ADD_BUTTON_ID),
                (Button) scene.lookup(EQUALS_BUTTON_ID),
                (Button) scene.lookup(NEGATE_BUTTON_ID)
        };

        Button[] operationButtonsSegoeUI = new Button[]{
                (Button) scene.lookup(CLEAR_TEXT_BUTTON_ID),
                (Button) scene.lookup(CLEAR_ALL_BUTTON_ID)
        };

        Button[] degreeButtons = new Button[]{
                (Button) scene.lookup(SQR_BUTTON_ID),
                (Button) scene.lookup(INVERSE_BUTTON_ID)
        };

        Button[] digitsButtons = new Button[]{
                (Button) scene.lookup(ZERO_BUTTON_ID),
                (Button) scene.lookup(ONE_BUTTON_ID),
                (Button) scene.lookup(TWO_BUTTON_ID),
                (Button) scene.lookup(THREE_BUTTON_ID),
                (Button) scene.lookup(FOUR_BUTTON_ID),
                (Button) scene.lookup(FIVE_BUTTON_ID),
                (Button) scene.lookup(SIX_BUTTON_ID),
                (Button) scene.lookup(SEVEN_BUTTON_ID),
                (Button) scene.lookup(EIGHT_BUTTON_ID),
                (Button) scene.lookup(NINE_BUTTON_ID),
        };

        setFontSizeForButtons(operationFont, FONT_ASSETS, operationButtonsAssets);
        setFontSizeForButtons(operationFont, FONT_SEGOE_UI, operationButtonsSegoeUI);
        setFontSizeForButtons(backspaceAndDotFont, FONT_ASSETS, (Button) scene.lookup(BACKSPACE_BUTTON_ID));
        setFontSizeForButtons(backspaceAndDotFont, FONT_SEGOE_UI_BLACK, (Button) scene.lookup(DOT_BUTTON_ID));
        setFontSizeForButtons(degreeFont, FONT_SEGOE_UI, degreeButtons);
        setFontSizeForButtons(digitsFont, FONT_SEGOE_UI_SEMIBOLD, digitsButtons);
    }

    /**
     * Stylesheet representation of font size.
     *
     * @param size size of font to set in px.
     * @return string for setting size of font in stylesheets.
     */
    private String getFontString(double size, String fontFamily) {
        return "-fx-font-size: " + size + "px;" +
                "-fx-font-family: \"" + fontFamily + "\"";
    }

    /**
     * Sets style that includes font size and font family for every required button.
     *
     * @param size       size of font that every button should has.
     * @param fontFamily name of font that every button should has.
     * @param buttons    buttons that style should be changed.
     */
    private void setFontSizeForButtons(int size, String fontFamily, Button... buttons) {
        for (Button button : buttons) {
            button.setStyle(getFontString(size, fontFamily));
        }
    }
}