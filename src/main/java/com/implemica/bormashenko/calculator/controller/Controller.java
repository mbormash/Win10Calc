package com.implemica.bormashenko.calculator.controller;

import com.implemica.bormashenko.calculator.model.*;
import com.implemica.bormashenko.calculator.model.enums.*;
import com.implemica.bormashenko.calculator.model.exceptions.*;
import com.implemica.bormashenko.calculator.model.util.OverflowValidation;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.stream.Stream;

import static com.implemica.bormashenko.calculator.controller.util.NumberEditor.*;
import static com.implemica.bormashenko.calculator.controller.util.NumberFormatter.*;
import static com.implemica.bormashenko.calculator.model.enums.Operation.*;

/**
 * Controller for application.
 *
 * @author Mykhailo Bormashenko
 */
public class Controller implements Initializable {

    /**
     * Application's {@code Button} used in controller.
     */
    @FXML
    private Button one, two, three, four, five, six, seven, eight, nine, zero, dot,
            add, subtract, multiply, divide, equals, percent, negate, sqr, sqrt, inverse,
            memoryClear, memoryRecall, memoryAdd, memorySubtract, memoryStore, memoryShow,
            clearAll, clearText, backspace, leftArrow, rightArrow;

    /**
     * Application's {@code Label} used in controller.
     */
    @FXML
    private Label screen, equation;

    /**
     * Application's {@code AnchorPane} used in controller.
     */
    @FXML
    private AnchorPane memoryAnchorPane, memoryPanel, memoryBlock, navigationBlock, aboutPanel;

    /**
     * Application's {@code ScrollPane} used in controller.
     */
    @FXML
    private ScrollPane navigationPanel, equationScroll;

    /**
     * Multiplicand for {@code Text} width used in moving the {@code Text} in equation {@code Label}.
     */
    private static final double MULTIPLICAND_FOR_TEXT_WIDTH = 1.5;

    /**
     * Scale for offset that should be applied to H value of equation scroll.
     */
    private static final int MOVE_LABEL_SCALE = 2;

    /**
     * Layout for memory {@code Label}.
     */
    private static final int MEMORY_LABELS_LAYOUT = 16;

    /**
     * Pref height for memory {@code Label}.
     */
    private static final int MEMORY_LABELS_HEIGHT = 63;

    /**
     * Font size for memory {@code Label}.
     */
    private static final int MEMORY_LABELS_FONT_SIZE = 24;

    /**
     * Insets for memory {@code Label}.
     */
    private static final Insets MEMORY_LABELS_INSETS = new Insets(0, 15, 0, 15);

    /**
     * Zero symbol is primary number in screen {@code Label}.
     */
    private static final String ZERO = "0";

    /**
     * Symbol for separation numbers and operations in equation {@code Label}.
     */
    private static final String NARROW_SPACE = "\u2009";

    /**
     * Empty string is primary string in equation {@code Label}.
     */
    private static final String EMPTY_STRING = "";

    /**
     * Symbol for separating number and operation's symbol while using {@code UnaryOperation} (before number).
     */
    private static final String OPENING_BRACKET = "(";

    /**
     * Symbol for separating number and operation's symbol while using {@code UnaryOperation} (after number).
     */
    private static final String CLOSING_BRACKET = ")";

    /**
     * Symbol of {@code BinaryOperation.ADD} operation used in equation {@code Label}.
     */
    private static final String ADD_SYMBOL = "+";

    /**
     * Symbol of {@code BinaryOperation.SUBTRACT} operation used in equation {@code Label}.
     */
    private static final String SUBTRACT_SYMBOL = "-";

    /**
     * Symbol of {@code BinaryOperation.MULTIPLY} operation used in equation {@code Label}.
     */
    private static final String MULTIPLY_SYMBOL = "×";

    /**
     * Symbol of {@code BinaryOperation.DIVIDE} operation used in equation {@code Label}.
     */
    private static final String DIVIDE_SYMBOL = "÷";

    /**
     * Symbol of {@code UnaryOperation.NEGATE} operation used in equation {@code Label}.
     */
    private static final String NEGATE_SYMBOL = "negate";

    /**
     * Symbol of {@code UnaryOperation.SQR} operation used in equation {@code Label}.
     */
    private static final String SQR_SYMBOL = "sqr";

    /**
     * Symbol of {@code UnaryOperation.SQRT} operation used in equation {@code Label}.
     */
    private static final String SQRT_SYMBOL = "√";

    /**
     * Symbol of {@code UnaryOperation.INVERSE} operation used in equation {@code Label}.
     */
    private static final String INVERSE_SYMBOL = "1/";

    /**
     * Negative number symbol.
     */
    private static final String MINUS = "-";

    /**
     * {@link Calculation} model of application.
     */
    private Calculation calculation = new Calculation();

    /**
     * {@link Memory} model of application.
     */
    private Memory memory = new Memory();

    /**
     * {@code BigDecimal} value of result of operation just performed.
     */
    private BigDecimal result = BigDecimal.ZERO;

    /**
     * True if number on screen can be edited.
     */
    private boolean isEditableScreen = true;

    /**
     * True if any binary operation was just pressed.
     */
    private boolean isBinaryOperationPressed = false;

    /**
     * True if any unary operation was just pressed.
     */
    private boolean isUnaryOrPercentPressed = false;

    /**
     * True if equals was just pressed.
     */
    private boolean isEqualsPressed = false;

    /**
     * True if first number was calculated.
     */
    private boolean isFirstSet = false;

    /**
     * True if error was just happened.
     */
    private boolean isError = false;

    /**
     * True if recall from memory button was just pressed.
     */
    private boolean isRecalledFromMemory = false;

    /**
     * True if memory is shown.
     */
    private boolean isMemoryShown = false;

    /**
     * True if navigation is shown.
     */
    private boolean isNavigationShown = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //no initializing is needed
    }

    /**
     * Handles keyboard input and fires required {@code Button}.
     *
     * @param event keyboard code or combination that was/were pressed.
     */
    @FXML
    public void keyboardHandling(KeyEvent event) {
        try {
            KeyCode keyCode = event.getCode();
            Button buttonToFire = null;

            //combinations with ctrl
            if (event.isControlDown()) {

                if (keyCode == KeyCode.M) {
                    buttonToFire = memoryStore;
                } else if (keyCode == KeyCode.P) {
                    buttonToFire = memoryAdd;
                } else if (keyCode == KeyCode.Q) {
                    buttonToFire = memorySubtract;
                } else if (keyCode == KeyCode.R) {
                    buttonToFire = memoryRecall;
                } else if (keyCode == KeyCode.L) {
                    buttonToFire = memoryClear;
                }

                //combinations with shift
            } else if (event.isShiftDown()) {

                if (keyCode == KeyCode.DIGIT2) {
                    buttonToFire = sqrt;
                } else if (keyCode == KeyCode.DIGIT5) {
                    buttonToFire = percent;
                } else if (keyCode == KeyCode.DIGIT8) {
                    buttonToFire = multiply;
                } else if (keyCode == KeyCode.EQUALS) {
                    buttonToFire = add;
                }

                //not a combination
            } else {

                if (keyCode == KeyCode.DIGIT0 || keyCode == KeyCode.NUMPAD0) {
                    buttonToFire = zero;
                } else if (keyCode == KeyCode.DIGIT1 || keyCode == KeyCode.NUMPAD1) {
                    buttonToFire = one;
                } else if (keyCode == KeyCode.DIGIT2 || keyCode == KeyCode.NUMPAD2) {
                    buttonToFire = two;
                } else if (keyCode == KeyCode.DIGIT3 || keyCode == KeyCode.NUMPAD3) {
                    buttonToFire = three;
                } else if (keyCode == KeyCode.DIGIT4 || keyCode == KeyCode.NUMPAD4) {
                    buttonToFire = four;
                } else if (keyCode == KeyCode.DIGIT5 || keyCode == KeyCode.NUMPAD5) {
                    buttonToFire = five;
                } else if (keyCode == KeyCode.DIGIT6 || keyCode == KeyCode.NUMPAD6) {
                    buttonToFire = six;
                } else if (keyCode == KeyCode.DIGIT7 || keyCode == KeyCode.NUMPAD7) {
                    buttonToFire = seven;
                } else if (keyCode == KeyCode.DIGIT8 || keyCode == KeyCode.NUMPAD8) {
                    buttonToFire = eight;
                } else if (keyCode == KeyCode.DIGIT9 || keyCode == KeyCode.NUMPAD9) {
                    buttonToFire = nine;
                } else if (keyCode == KeyCode.R) {
                    buttonToFire = inverse;
                } else if (keyCode == KeyCode.F9) {
                    buttonToFire = negate;
                } else if (keyCode == KeyCode.PERIOD) {
                    buttonToFire = dot;
                } else if (keyCode == KeyCode.BACK_SPACE) {
                    buttonToFire = backspace;
                } else if (keyCode == KeyCode.ADD) {
                    buttonToFire = add;
                } else if (keyCode == KeyCode.MINUS || keyCode == KeyCode.SUBTRACT) {
                    buttonToFire = subtract;
                } else if (keyCode == KeyCode.MULTIPLY) {
                    buttonToFire = multiply;
                } else if (keyCode == KeyCode.DIVIDE || keyCode == KeyCode.SLASH) {
                    buttonToFire = divide;
                } else if (keyCode == KeyCode.EQUALS || keyCode == KeyCode.ENTER) {
                    buttonToFire = equals;
                } else if (keyCode == KeyCode.DELETE) {
                    buttonToFire = clearText;
                } else if (keyCode == KeyCode.ESCAPE) {
                    buttonToFire = clearAll;
                }

            }

            if (buttonToFire != null && !isMemoryShown && !isNavigationShown) {
                buttonToFire.fire();
            }
        } catch (Throwable e) {
            tellUserAboutError(e);
        }
    }

    /**
     * Opens or closes navigation bar.
     */
    @FXML
    public void showOrHideNavigationPanel() {
        try {
            translateNavigation(!isNavigationShown);
            navigationBlock.setVisible(!isNavigationShown);
            isNavigationShown = !isNavigationShown;
        } catch (Throwable e) {
            tellUserAboutError(e);
        }
    }

    /**
     * Moves text in equation {@code Label} to the left.
     */
    @FXML
    public void moveEquationLeft() {
        try {
            moveTextInEquationLabel(leftArrow, rightArrow, true);
        } catch (Throwable e) {
            tellUserAboutError(e);
        }
    }

    /**
     * Moves text in equation {@code Label} to the right.
     */
    @FXML
    public void moveEquationRight() {
        try {
            moveTextInEquationLabel(rightArrow, leftArrow, false);
        } catch (Throwable e) {
            tellUserAboutError(e);
        }
    }

    /**
     * Saves number in memory.
     */
    @FXML
    public void memoryStoreOperation() {
        try {
            try {
                BigDecimal number = getCorrectNumber(true);

                memory.storeToMemory(number);
                setButtonsDisability(false, memoryClear, memoryRecall, memoryShow);

                isEditableScreen = false;
            } catch (OverflowException e) {
                exceptionThrown(e.getMessage());
            }
        } catch (Throwable e) {
            tellUserAboutError(e);
        }
    }

    /**
     * Shows memory.
     */
    @FXML
    public void memoryShowOperation() {
        try {
            memoryAnchorPane.setVisible(!isMemoryShown);
            memoryBlock.setVisible(!isMemoryShown);

            isMemoryShown = !isMemoryShown;

            if (isMemoryShown) {
                updateMemoryLabels();
            }
        } catch (Throwable e) {
            tellUserAboutError(e);
        }
    }

    /**
     * Clears all memory.
     */
    @FXML
    public void memoryClearOperation() {
        try {
            memory.clearMemory();
            setButtonsDisability(true, memoryClear, memoryRecall, memoryShow);

            isEditableScreen = false;
        } catch (Throwable e) {
            tellUserAboutError(e);
        }
    }

    /**
     * Recalls number from memory.
     */
    @FXML
    public void memoryRecallOperation() {
        try {
            try {
                BigDecimal number = memory.recall();
                showNumberOnScreen(formatNumber(number, true), false);

                if (!isFirstSet) {
                    calculation.setFirst(number);
                } else {
                    calculation.setSecond(number);
                }

                isEditableScreen = false;
            } catch (OverflowException e) {
                exceptionThrown(e.getMessage());
            }

            if (isUnaryOrPercentPressed) {
                equation.setText(EMPTY_STRING);
            }

            if (isEqualsPressed || isUnaryOrPercentPressed) {
                isFirstSet = false;
            }

            setFlags(false, false, false,
                    false, isFirstSet, false, true);
        } catch (Throwable e) {
            tellUserAboutError(e);
        }
    }

    /**
     * Adds number to memory.
     */
    @FXML
    public void memoryAddOperation() {
        try {
            try {
                BigDecimal number = getCorrectNumber(false);

                memory.addToMemory(number);
                setButtonsDisability(false, memoryClear, memoryRecall, memoryShow);

                isEditableScreen = false;
            } catch (OverflowException e) {
                exceptionThrown(e.getMessage());
            }
        } catch (Throwable e) {
            tellUserAboutError(e);
        }
    }

    /**
     * Subtracts number from memory.
     */
    @FXML
    public void memorySubtractOperation() {
        try {
            try {
                BigDecimal number = getCorrectNumber(false);

                memory.subtractFromMemory(number);
                setButtonsDisability(false, memoryClear, memoryRecall, memoryShow);

                isEditableScreen = false;
            } catch (OverflowException e) {
                exceptionThrown(e.getMessage());
            }
        } catch (Throwable e) {
            tellUserAboutError(e);
        }
    }

    /**
     * Appends digit from {@code Button} to screen {@code Label} if it is allowed.
     * Otherwise, sets the digit to screen {@code Label}.
     *
     * @param event {@code ActionEvent} that called the method.
     */
    @FXML
    public void appendDigit(ActionEvent event) {
        try {
            if (isError) {
                returnAfterError();
            }

            String digit = ((Button) event.getSource()).getText();
            String number;

            if (isEditableScreen) {
                number = screen.getText();
            } else {
                number = ZERO;
            }

            result = appendDigitToNumber(parseToBigDecimal(number), new BigDecimal(digit), isLastDot(number));

            if (!isFirstSet) {
                calculation.setFirst(result);
            } else {
                calculation.setSecond(result);
            }

            screen.setText(formatNumber(result, true));

            if (isUnaryOrPercentPressed) {
                equation.setText(EMPTY_STRING);
            }

            if (isEqualsPressed || isUnaryOrPercentPressed) {
                isFirstSet = false;
            }

            setFlags(true, false, false,
                    false, isFirstSet, false, false);
        } catch (Throwable e) {
            tellUserAboutError(e);
        }
    }

    /**
     * Makes number in screen {@code Label} decimal (if not decimal yet) if it is allowed.
     * Otherwise, sets "0." to screen {@code Label}.
     */
    @FXML
    public void appendDecimalSeparator() {
        try {
            String number;

            if (isEditableScreen) {
                number = screen.getText();
            } else {
                number = ZERO;
            }

            if (!number.contains(String.valueOf(DECIMAL_SEPARATOR))) {
                number += DECIMAL_SEPARATOR;
            }

            screen.setText(number);

            if (isUnaryOrPercentPressed) {
                equation.setText(EMPTY_STRING);
            }

            if (isEqualsPressed || isUnaryOrPercentPressed) {
                isFirstSet = false;
            }

            setFlags(true, false, false,
                    false, isFirstSet, false, false);
        } catch (Throwable e) {
            tellUserAboutError(e);
        }
    }

    /**
     * Deletes last symbol in screen {@code Label} if it is allowed.
     */
    @FXML
    public void backspace() {
        try {
            if (isError) {
                returnAfterError();
            }

            if (isEditableScreen) {
                String number = screen.getText();
                result = parseToBigDecimal(number);

                boolean saveDecimalSeparator = false;

                if (result.scale() == 1) {
                    saveDecimalSeparator = true;
                }

                if (!number.endsWith(String.valueOf(DECIMAL_SEPARATOR))) {
                    result = deleteLastDigit(result);
                }

                String screenText = formatNumber(result, true);

                if (saveDecimalSeparator) {
                    screenText += DECIMAL_SEPARATOR;
                }

                screen.setText(screenText);

                if (!isFirstSet) {
                    calculation.setFirst(result);
                } else {
                    calculation.setSecond(result);
                }
            }
        } catch (Throwable e) {
            tellUserAboutError(e);
        }
    }

    /**
     * Sets text in screen {@code Label} to 0.
     */
    @FXML
    public void clearText() {
        try {
            if (isError) {
                returnAfterError();
            }

            screen.setText(ZERO);
            result = BigDecimal.ZERO;

            if (!isFirstSet) {
                calculation.setFirst(result);
            } else {
                calculation.setSecond(result);
            }
            setFlags(true, false, false,
                    false, isFirstSet, false, false);
        } catch (Throwable e) {
            tellUserAboutError(e);
        }
    }

    /**
     * Resets application to it's primary.
     */
    @FXML
    public void clearAll() {
        try {

            if (isError) {
                returnAfterError();
            }

            clearText();
            calculation.resetAll();
            result = BigDecimal.ZERO;
            equation.setText(EMPTY_STRING);

            setFlags(true, false, false,
                    false, false, false, false);
        } catch (Throwable e) {
            tellUserAboutError(e);
        }
    }

    //todo
    @FXML
    public void operationPressed(Event event) {
        try {
            returnAfterError();

            Button button = (Button) event.getSource();
            Operation operation;

            if (button == add) {
                operation = ADD;
            } else if (button == subtract) {
                operation = SUBTRACT;
            } else if (button == multiply) {
                operation = MULTIPLY;
            } else if (button == divide) {
                operation = DIVIDE;
            } else if (button == negate) {
                operation = NEGATE;
            } else if (button == sqr) {
                operation = SQR;
            } else if (button == sqrt) {
                operation = SQRT;
            } else if (button == inverse) {
                operation = INVERSE;
            } else if (button == percent) {
                operation = PERCENT;
            } else {
                operation = EQUALS;
            }

            if (operation == NEGATE && isEditableScreen) {
                prependMinusIfMissed();
            } else {

                try {
                    result = calculation.doOperation(operation);

                    setEquationText(operation);
                    showNumberOnScreen(formatNumber(result, true), operation == DIVIDE);
                } catch (OverflowException | DivideZeroByZeroException | DivideByZeroException | NegativeRootException e) {
                    setEquationText(operation);
                    exceptionThrown(e.getMessage());
                }
            }
        } catch (Throwable e) {
            tellUserAboutError(e);
        }
    }

    /**
     * Shows to user that something goes wrong.
     * Also resets application to its primary.
     *
     * @param e Throwable object that was thrown.
     */
    private void tellUserAboutError(Throwable e) {
        e.printStackTrace();

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Unsupported error was thrown with message:\n" + e.getMessage() +
                "\nApplication will be reset to its primary.");

        alert.showAndWait();

        clearAll();
        memoryClearOperation();
    }

    /**
     * Slowly moves navigation bar to the right or left.
     *
     * @param visible if true, moves navigation bar right, otherwise moves right.
     */
    private void translateNavigation(boolean visible) {
        int durationLength = 250;
        Duration duration = Duration.millis(durationLength);
        TranslateTransition transitionNavigation = new TranslateTransition(duration, navigationPanel);
        TranslateTransition transitionAbout = new TranslateTransition(duration, aboutPanel);

        int signum = convertBooleanToSignum(visible);

        transitionNavigation.setByX(navigationPanel.getWidth() * signum);
        transitionAbout.setByX((navigationPanel.getWidth() + 1) * signum);

        transitionNavigation.play();
        transitionAbout.play();
    }

    /**
     * Converts boolean to int with sign.
     *
     * @param flag boolean value to convert.
     * @return 1 if flag is true or -1 otherwise.
     */
    private int convertBooleanToSignum(boolean flag) {
        return flag ? 1 : -1;
    }

    /**
     * Moves {@code Text} in equation {@code Label} to the right or left.
     *
     * @param clickedButton  right or left arrow {@code Button} that should move {@code Text} to the specific side.
     * @param oppositeButton left or right{@code Button} that should move {@code Text} to the opposite of clicked
     *                       {@code Button} side.
     * @param moveLeft       true if moving left or false if moving right.
     */
    private void moveTextInEquationLabel(Button clickedButton, Button oppositeButton, boolean moveLeft) {
        oppositeButton.setVisible(true);

        Text text = new Text(equation.getText());
        text.setFont(equation.getFont());

        double newHValue = equationScroll.getHvalue();

        if (text.getBoundsInLocal().getWidth() > clickedButton.getScene().getWidth() * MULTIPLICAND_FOR_TEXT_WIDTH) {
            double offset = NumberUtils.toScaledBigDecimal(clickedButton.getScene().getWidth() /
                    text.getBoundsInLocal().getWidth(), MOVE_LABEL_SCALE, RoundingMode.HALF_UP).doubleValue();

            if (moveLeft) {
                newHValue += offset;
            } else {
                newHValue -= offset;
            }

        } else {

            if (moveLeft) {
                newHValue = equationScroll.getHmax();
            } else {
                newHValue = equationScroll.getHmin();
            }

        }

        equationScroll.setHvalue(newHValue);

        if ((moveLeft && equationScroll.getHvalue() == equationScroll.getHmax()) ||
                (!moveLeft && equationScroll.getHvalue() == equationScroll.getHmin())) {
            clickedButton.setVisible(false);
        }
    }

    /**
     * Creates memory {@code Label} for each {@link Memory} cell.
     */
    private void updateMemoryLabels() {
        Stack<BigDecimal> store = memory.getStore();

        if (!store.isEmpty()) {
            memoryPanel.getChildren().removeAll(memoryPanel.getChildren());

            double layoutY = MEMORY_LABELS_LAYOUT;

            for (int i = 0; i < store.size(); i++) {
                Label label = new Label();
                label.setText(formatNumber(store.elementAt(store.size() - i - 1), true));
                configureMemoryLabel(label, layoutY);

                memoryPanel.getChildren().add(label);
                layoutY += MEMORY_LABELS_HEIGHT + MEMORY_LABELS_LAYOUT;
            }
        }
    }

    /**
     * Sets configuration such as size, layout and style for memory {@code Label}.
     *
     * @param label   {@code Label} to edit.
     * @param layoutY coordinate Y for the {@code Label}.
     */
    private void configureMemoryLabel(Label label, double layoutY) {
        label.setPrefWidth(memoryPanel.getWidth());
        label.setPrefHeight(MEMORY_LABELS_HEIGHT);
        label.setMinHeight(label.getPrefHeight());
        label.setMaxHeight(label.getPrefHeight());
        label.setPadding(MEMORY_LABELS_INSETS);
        label.setLayoutY(layoutY);
        label.setStyle(setStyleForLabels());
        label.setWrapText(true);
        label.setOnMouseMoved(event -> label.setStyle(setStyleForLabelsOnHover()));
        label.setOnMouseExited(event -> label.setStyle(setStyleForLabels()));
        label.setAlignment(Pos.TOP_LEFT);
    }

    /**
     * Sets style for memory {@code Label}.
     *
     * @return style (as css representation).
     */
    private static String setStyleForLabels() {
        return "-fx-background-color: transparent;" +
                "-fx-font-size: " + MEMORY_LABELS_FONT_SIZE + "px;" +
                "-fx-font-family: \"Segoe UI Semibold\"";
    }

    /**
     * Sets style for memory {@code Label} on hover.
     *
     * @return style (as css representation).
     */
    private static String setStyleForLabelsOnHover() {
        return "-fx-background-color: #e7e7e7;" +
                "-fx-font-size: " + MEMORY_LABELS_FONT_SIZE + "px;" +
                "-fx-font-family: \"Segoe UI Semibold\"";
    }

    /**
     * Returns correct number for next operations.
     * <p>
     * If calculation was just made, returns result.
     * <p>
     * If number from memory was just recalled, returns recalled value.
     * <p>
     * Otherwise, returns number from screen {@code Label}.
     *
     * @param checkResult true if result can be returned or false otherwise.
     * @return correct number for next calculations.
     * @throws OverflowException if recalled from memory value failed validation.
     * @throws ParseException    if can not parse number.
     */
    private BigDecimal getCorrectNumber(boolean checkResult) throws OverflowException, ParseException {
        BigDecimal number;

        if (checkResult && (isEqualsPressed || isBinaryOperationPressed || isUnaryOrPercentPressed)) {
            number = result;
        } else if (isRecalledFromMemory) {
            number = memory.recall();
        } else {
            formatNumber(BigDecimal.ZERO, true);
            number = parseToBigDecimal(screen.getText());
        }

        return number;
    }

    //todo
    private void prependMinusIfMissed() throws ParseException {
        String screenText = screen.getText();

        if (!screenText.equals(ZERO)) {

            if (screenText.startsWith(MINUS)) {
                screenText = screenText.replace(MINUS, EMPTY_STRING);
            } else {
                screenText = MINUS + screenText;
            }

        }

        if (!isFirstSet) {
            calculation.setFirst(parseToBigDecimal(screenText));
        } else {
            calculation.setSecond(parseToBigDecimal(screenText));
        }

        screen.setText(screenText);
    }

    //todo
    private void setEquationText(Operation operation) throws OverflowException, ParseException {
        String equationTextToSet;

        if (operation.type == OperationType.BINARY) {
            equationTextToSet = equationForBinary(operation);
        } else if (operation.type == OperationType.UNARY) {
            equationTextToSet = equationForUnary(operation);
        } else if (operation.type == OperationType.PERCENT) {
            equationTextToSet = equationForPercentage();
        } else {
            equationTextToSet = EMPTY_STRING;
            setFlags(false, false, false,
                    true, isFirstSet, false, false);
        }

        equation.setText(equationTextToSet);
    }

    //todo
    private String equationForBinary(Operation operation) throws OverflowException, ParseException {
        String equationTextToSet;

        if (!isBinaryOperationPressed) {
            equationTextToSet = equationForBinaryNotAfterBinary(operation);
        } else {
            equationTextToSet = equationForBinaryAfterBinary(operation);
        }

        return equationTextToSet;
    }

    //todo
    private String equationForBinaryNotAfterBinary(Operation operation) throws ParseException, OverflowException {
        String equationTextToSet;

        BigDecimal number = getCorrectNumber(false);

        if (!isFirstSet) {
            equationTextToSet = formatNumber(number, false) + NARROW_SPACE +
                    operationSymbol(operation);
        } else if (!isEqualsPressed && !isUnaryOrPercentPressed) {
            equationTextToSet = equation.getText() + NARROW_SPACE + formatNumber(number, false) +
                    NARROW_SPACE + operationSymbol(operation);
        } else {

            if (isEqualsPressed) {
                equationTextToSet = equationForBinaryAfterEquals(operation, result);
            } else {
                equationTextToSet = equation.getText() + NARROW_SPACE + operationSymbol(operation);
            }

        }

        setFlags(false, true, false,
                false, true, false, false);

        return equationTextToSet;
    }

    //todo
    private String equationForBinaryAfterEquals(Operation operation, BigDecimal number) {
        String equationTextToSet;

        if (calculation.getBinaryOperation() == null) {
            equationTextToSet = formatNumber(number, false) +
                    NARROW_SPACE + operationSymbol(operation);
        } else {
            equationTextToSet = formatNumber(result, false) +
                    NARROW_SPACE + operationSymbol(operation);
        }

        return equationTextToSet;
    }

    //todo
    private String equationForBinaryAfterBinary(Operation operation) {
        setFlags(false, true, false,
                false, true, false, false);

        return StringUtils.chop(equation.getText()) + operationSymbol(operation);
    }

    //todo
    private String equationForUnary(Operation operation) throws OverflowException, ParseException {
        String equationTextToSet;

        BigDecimal number = getCorrectNumber(false);

        if (!isFirstSet) {
            equationTextToSet = operationSymbol(operation) + OPENING_BRACKET + NARROW_SPACE +
                    formatNumber(number, false) + NARROW_SPACE + CLOSING_BRACKET;
        } else if (isUnaryOrPercentPressed) {
            equationTextToSet = equationAfterSeveralUnaryOrPercentage(operation);
        } else {
            equationTextToSet = equationAfterUnary(operation, number);
        }

        setFlags(false, false, true,
                false, true, false, false);

        return equationTextToSet;
    }

    /**
     * Updates text set in equation {@code Label} after several unary or percentage operations in a row (but the last
     * one is percentage) and returns it.
     * <p>
     * First of all, looks for the last {@code BinaryOperation} symbol in equation {@code Label}. If it was found,
     * separates text in equation {@code Label} into two parts: text before the last {@code BinaryOperation} symbol
     * (including it) and text after. Then wraps "text after" into brackets and operation's symbol, and appends received
     * string to "text after".
     * <p>
     * If equation {@code Label} does not contain any {@code BinaryOperation} symbol, wraps text in equation
     * {@code Label} into brackets and operation's symbol.
     *
     * @param operation {@code UnaryOperation} that was performed.
     * @return string that was received after operations described below.
     */
    private String equationAfterSeveralUnaryOrPercentage(Operation operation) {
        String equationTextToSet = equation.getText();

        String textBefore = EMPTY_STRING;
        String textAfter = equationTextToSet;

        if (equationTextToSet.contains(ADD_SYMBOL) ||
                equationTextToSet.contains(SUBTRACT_SYMBOL) ||
                equationTextToSet.contains(MULTIPLY_SYMBOL) ||
                equationTextToSet.contains(DIVIDE_SYMBOL)) {

            int lastIndexOfOperation = findLastIndexOfOperation(equationTextToSet);

            textBefore = equationTextToSet.substring(0, lastIndexOfOperation);
            textAfter = equationTextToSet.substring(lastIndexOfOperation + 1);
        }

        if (textBefore.equals(EMPTY_STRING)) {
            equationTextToSet = operationSymbol(operation) + OPENING_BRACKET + NARROW_SPACE + textAfter +
                    NARROW_SPACE + CLOSING_BRACKET;
        } else {
            equationTextToSet = textBefore + NARROW_SPACE + operationSymbol(operation) + OPENING_BRACKET +
                    NARROW_SPACE + textAfter + NARROW_SPACE + CLOSING_BRACKET;
        }

        return equationTextToSet;
    }

    /**
     * Looks for the last {@code BinaryOperation} symbol in required text (which is not belongs to
     * {@code EXPONENT_SEPARATOR}).
     *
     * @param text string where to look for.
     * @return last index of {@code BinaryOperation}.
     */
    private int findLastIndexOfOperation(String text) {
        int lastIndexOfAdd = text.lastIndexOf(ADD_SYMBOL);

        if (lastIndexOfAdd != -1 && text.charAt(lastIndexOfAdd - 1) == 'e') {
            lastIndexOfAdd = text.substring(0, lastIndexOfAdd - 1).lastIndexOf(ADD_SYMBOL);
        }

        int lastIndexOfSubtract = text.lastIndexOf(SUBTRACT_SYMBOL);

        if (lastIndexOfSubtract != -1 && text.charAt(lastIndexOfSubtract - 1) == 'e') {
            lastIndexOfSubtract = text.substring(0, lastIndexOfSubtract - 1).lastIndexOf(SUBTRACT_SYMBOL);
        }

        int lastIndexOfMultiply = text.lastIndexOf(MULTIPLY_SYMBOL);
        int lastIndexOfDivide = text.lastIndexOf(DIVIDE_SYMBOL);

        return Math.max(Math.max(lastIndexOfAdd, lastIndexOfSubtract),
                Math.max(lastIndexOfMultiply, lastIndexOfDivide)) + 1;
    }

    //todo
    private String equationAfterUnary(Operation operation, BigDecimal number) {
        String equationTextToSet;

        if (equation.getText().equals(EMPTY_STRING)) {
            equationTextToSet = operationSymbol(operation) + OPENING_BRACKET + NARROW_SPACE +
                    formatNumber(number, false) + NARROW_SPACE + CLOSING_BRACKET;
        } else {
            equationTextToSet = equation.getText() + NARROW_SPACE + operationSymbol(operation) + OPENING_BRACKET +
                    NARROW_SPACE + formatNumber(number, false) + NARROW_SPACE + CLOSING_BRACKET;
        }

        return equationTextToSet;
    }

    //todo
    private String equationForPercentage() {
        String equationTextToSet = equation.getText();

        if (isUnaryOrPercentPressed) {
            equationTextToSet = equationTextAfterSeveralPercentage(equationTextToSet);
        } else {

            if (!equationTextToSet.equals(EMPTY_STRING)) {
                equationTextToSet += NARROW_SPACE;
            }

            equationTextToSet += formatNumber(result, false);
        }

        setFlags(false, false, true,
                false, true, false, false);

        return equationTextToSet;
    }

    //todo
    private String equationTextAfterSeveralPercentage(String equationTextToSet) {
        int lastIndexOfOperation = findLastIndexOfOperation(equationTextToSet);
        String textBefore = equationTextToSet.substring(0, lastIndexOfOperation);

        return textBefore + NARROW_SPACE + formatNumber(result, false);
    }

    /**
     * Performs reset all operation in {@link Calculation} and sets message to screen {@code Label}.
     * Also disables mostly all {@code Button}.
     *
     * @param message message of exception to set as screen {@code Label} text.
     */
    private void exceptionThrown(String message) {
        calculation.resetAll();
        screen.setText(message);

        Button[] buttonsToDisable = {
                memoryClear, memoryRecall, memoryAdd, memorySubtract, memoryStore,
                percent, sqrt, sqr, inverse, divide, multiply, subtract, add, negate, dot
        };

        setButtonsDisability(true, buttonsToDisable);

        setFlags(false, false, false,
                false, false, true, false);
    }

    /**
     * Returns to normal state after any exception was thrown.
     * <p>
     * Sets text in screen and equation {@code Label} to default. Enables all disabled {@code Button} (but memory
     * {@code Button} such as {@code memoryClear}, {@code memoryRecall}, {@code memoryShow} enabled only if there is
     * anything stored in memory).
     */
    private void returnAfterError() {
        if (isError) {
            screen.setText(ZERO);
            equation.setText(EMPTY_STRING);

            Button[] buttonsToEnable = {
                    memoryAdd, memorySubtract, memoryStore,
                    percent, sqrt, sqr, inverse, divide, multiply, subtract, add, negate, dot
            };

            Button[] memoryStandardDisabledButtons = {
                    memoryClear, memoryRecall, memoryShow
            };

            setButtonsDisability(false, buttonsToEnable);
            setButtonsDisability(memory.getStore().isEmpty(), memoryStandardDisabledButtons);
        }
    }

    /**
     * Shows formatted number in screen {@code Label} if it should not cause {@link OverflowException}.
     *
     * @param number            number to show.
     * @param isDividePerformed true if divide operation was just performed or false otherwise.
     * @throws OverflowException while validation for result is failed.
     * @throws ParseException    if can not parse number.
     */
    private void showNumberOnScreen(String number, boolean isDividePerformed) throws OverflowException, ParseException {
        if (OverflowValidation.overflowValidationFailed(parseToBigDecimal(number), isDividePerformed,
                calculation.getFirst())) {
            throw new OverflowException();
        } else {
            screen.setText(number);
        }
    }

    /**
     * Sets flags for boolean fields of controller.
     *
     * @param isEditableScreen                 true if digit should be appended to screen number.
     * @param isBinaryOperationPressed         true if binary operation was just pressed.
     * @param isUnaryOrPercentOperationPressed true if unary operation was just pressed.
     * @param isEqualsPressed                  true if equals was just pressed.
     * @param isFirstSet                       true if first operand for model is set.
     * @param isError                          true if is error was just happened.
     * @param isRecalledFromMemory             true if recall from memory button was just pressed.
     */
    private void setFlags(boolean isEditableScreen, boolean isBinaryOperationPressed,
                          boolean isUnaryOrPercentOperationPressed, boolean isEqualsPressed, boolean isFirstSet,
                          boolean isError, boolean isRecalledFromMemory) {
        this.isEditableScreen = isEditableScreen;
        this.isBinaryOperationPressed = isBinaryOperationPressed;
        this.isUnaryOrPercentPressed = isUnaryOrPercentOperationPressed;
        this.isEqualsPressed = isEqualsPressed;
        this.isFirstSet = isFirstSet;
        this.isError = isError;
        this.isRecalledFromMemory = isRecalledFromMemory;
    }

    /**
     * Creates symbol to show in equation {@code Label} for {@link Operation}.
     *
     * @param operation {@link Operation} to use.
     * @return symbol of the operation.
     */
    private String operationSymbol(Operation operation) {
        String symbol = "";

        if (operation == ADD) {
            symbol = ADD_SYMBOL;
        } else if (operation == SUBTRACT) {
            symbol = SUBTRACT_SYMBOL;
        } else if (operation == MULTIPLY) {
            symbol = MULTIPLY_SYMBOL;
        } else if (operation == DIVIDE) {
            symbol = DIVIDE_SYMBOL;
        } else if (operation == NEGATE) {
            symbol = NEGATE_SYMBOL;
        } else if (operation == SQR) {
            symbol = SQR_SYMBOL;
        } else if (operation == SQRT) {
            symbol = SQRT_SYMBOL;
        } else if (operation == INVERSE) {
            symbol = INVERSE_SYMBOL;
        }

        return symbol;
    }

    /**
     * Checks if string ends with dot.
     *
     * @param string string to check.
     * @return true if string ends with dot or false otherwise.
     */
    private boolean isLastDot(String string) {
        return string.endsWith(String.valueOf(DECIMAL_SEPARATOR));
    }

    /**
     * Disables or enables several {@code Button}, passed as args.
     *
     * @param flag    true for disabling and false for enabling.
     * @param buttons several {@code Button} that should change their disability.
     */
    private void setButtonsDisability(boolean flag, Button... buttons) {
        Stream.of(buttons).forEach(button -> button.setDisable(flag));
    }
}
