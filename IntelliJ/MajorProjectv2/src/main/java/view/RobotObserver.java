package view;

import javax.swing.*;
import java.awt.*;

/**
 * ----------------------------------------------------------------------------------------------------------
 * This class create a Swing/AWT GUI Window that displays all the current values of the robots in the battle.
 * It is updated by the CompetitorOne.class and CompetitorTwo.class classes.
 * ----------------------------------------------------------------------------------------------------------
 * @author Thomas Plumpton
 * @version 2.1.4
 */
public class RobotObserver {
    private static boolean isOpen = false;

    private static JFrame frame = new JFrame("TwitterRobot Observer");

    //JPanel's for the overall GUI Layout
    private static JPanel GUI, headerOne, headerTwo, robotOne, robotTwo, footer, bodyOneLeft, bodyOneRight, bodyOneContainer;
    private static JPanel bodyTwoLeft, bodyTwoRight, bodyTwoContainer;

    //Footer: JLabels for the Robocode Battle Information
    private static JLabel roundNumber, turnNumber;

    //Body: JLabels for Robot One's Information
    private static JLabel xOrdinateOne, yOrdinateOne, firePowerOne, moveUpOne, moveRightOne, moveDownOne, moveLeftOne, rotateOne, rotateDirectionOne;
    private static JLabel rotateGunOne, rotateGunDirectionOne, rotateScannerOne, rotateScannerDirectionOne;

    //Body: JLabels for Robot Two's Information
    private static JLabel xOrdinateTwo, yOrdinateTwo;

    //Header: static JLabels for Robot One's Tweet Information
    private static JLabel numberOfTweetsOne, userOne, currentTweetOne;

    //Header: static JLabels for Robot Two's Tweet Information
    private static JLabel numberOfTweetsTwo, userTwo;

    //Header: JTextFields for Robot One's Tweet Information
    private static JTextField numberOfTweetsOneField, userOneField, currentTweetOneField;

    //Header: JTextFields for Robot Two's Twitter Information
    private static JTextField numberOfTweetsTwoField, userTwoField;

    //Footer: JTextFields for the Robocode Battle Information
    private static JTextField roundNumberField, turnNumberField;

    //Body: JTextFields for Robot One's Information
    private static JTextField xOrdinateOneField, yOrdinateOneField, firePowerOneField, moveUpOneField, moveRightOneField, moveDownOneField, moveLeftOneField;
    private static JTextField rotateOneField, rotateDirectionOneField, rotateGunOneField, rotateGunDirectionOneField, rotateScannerOneField, rotateScannerDirectionOneField;

    //Body: JTextFields for Robot Two's Information
    private static JTextField xOrdinateTwoField, yOrdinateTwoField;

    public static void startObserving() {
        createAndShowGUI();
    }

    private static void configureMainPanels() {
        GUI = new JPanel();
        footer = new JPanel();
        GUI.setLayout(new BorderLayout());
        footer.setLayout(new FlowLayout());
    }

    private static void configureRobotOnePanels() {
        //Instantiate Panels
        robotOne = new JPanel();
        headerOne = new JPanel();
        bodyOneLeft = new JPanel();
        bodyOneRight = new JPanel();
        headerOne = new JPanel();
        bodyOneContainer = new JPanel();

        //Set Layout Manager Types
        robotOne.setLayout(new BorderLayout());
        headerOne.setLayout(new FlowLayout());
        bodyOneContainer.setLayout(new BorderLayout());
        bodyOneLeft.setLayout(new BoxLayout(bodyOneLeft, BoxLayout.PAGE_AXIS));
        bodyOneRight.setLayout(new BoxLayout(bodyOneRight, BoxLayout.PAGE_AXIS));
        headerOne.setLayout(new FlowLayout());
    }

    private static void configureRobotTwoPanels() {
        //Instantiate Panels
        robotTwo = new JPanel();
        headerTwo = new JPanel();
        bodyTwoLeft = new JPanel();
        bodyTwoRight = new JPanel();
        bodyTwoContainer = new JPanel();
        headerTwo = new JPanel();

        //Set Layout Manager Types
        robotTwo.setLayout(new BorderLayout());

        bodyTwoContainer.setLayout(new BorderLayout());
        bodyTwoLeft.setLayout(new BoxLayout(bodyTwoLeft, BoxLayout.PAGE_AXIS));
        bodyTwoRight.setLayout(new BoxLayout(bodyTwoRight, BoxLayout.PAGE_AXIS));
        headerTwo.setLayout(new FlowLayout());
    }

    private static void configureRobotOneLabels() {
        //Twitter Information
        numberOfTweetsOne = new JLabel("Number of Tweets");
        userOne = new JLabel("Twitter ScreenName");
        currentTweetOne = new JLabel("Current Tweet");

        //Body
        xOrdinateOne = new JLabel("X-Ordinate");
        yOrdinateOne = new JLabel("Y-Ordinate");
        firePowerOne = new JLabel("Fire Power");
        moveUpOne = new JLabel("Move Up");
        moveRightOne = new JLabel("Move Right");
        moveDownOne = new JLabel("Move Down");
        moveLeftOne = new JLabel("Move Left");
        rotateOne = new JLabel("Rotate");
        rotateDirectionOne = new JLabel("Rotate Direction");
        rotateGunOne = new JLabel("Rotate Gun");
        rotateGunDirectionOne = new JLabel("Rotate Gun Direction");
        rotateScannerOne = new JLabel("Rotate Scanner");
        rotateScannerDirectionOne = new JLabel("Rotate Scanner Direction");
    }

    private static void configureFooterLabels() {
        roundNumber = new JLabel("Round Number");
        turnNumber = new JLabel("Turn Number");
    }

    private static void configureFooterFields() {
        roundNumberField = new JTextField("0", 10);
        turnNumberField = new JTextField("0", 10);
    }

    private static void addFooterLabelsAndFields() {
        footer.add(roundNumber);
        footer.add(roundNumberField);
        footer.add(turnNumber);
        footer.add(turnNumberField);
    }

    private static void addRobotOneLabelsAndFields() {
        //Header
        headerOne.add(numberOfTweetsOne);
        headerOne.add(numberOfTweetsOneField);
        headerOne.add(userOne);
        headerOne.add(userOneField);
        headerOne.add(currentTweetOne);
        headerOne.add(currentTweetOneField);

        //Left
        bodyOneLeft.add(xOrdinateOne);
        bodyOneLeft.add(xOrdinateOneField);
        bodyOneLeft.add(yOrdinateOne);
        bodyOneLeft.add(yOrdinateOneField);
        bodyOneLeft.add(moveUpOne);
        bodyOneLeft.add(moveUpOneField);
        bodyOneLeft.add(moveRightOne);
        bodyOneLeft.add(moveRightOneField);
        bodyOneLeft.add(moveDownOne);
        bodyOneLeft.add(moveDownOneField);
        bodyOneLeft.add(moveLeftOne);
        bodyOneLeft.add(moveLeftOneField);

        //Right
        bodyOneRight.add(rotateOne);
        bodyOneRight.add(rotateOneField);
        bodyOneRight.add(rotateDirectionOne);
        bodyOneRight.add(rotateDirectionOneField);
        bodyOneRight.add(rotateGunOne);
        bodyOneRight.add(rotateGunOneField);
        bodyOneRight.add(rotateGunDirectionOne);
        bodyOneRight.add(rotateGunDirectionOneField);
        bodyOneRight.add(firePowerOne);
        bodyOneRight.add(firePowerOneField);
        bodyOneRight.add(rotateScannerOne);
        bodyOneRight.add(rotateScannerOneField);
        bodyOneRight.add(rotateScannerDirectionOne);
        bodyOneRight.add(rotateScannerDirectionOneField);
    }
    
    private static void addRobotTwoLabelsAndFields() {
        //Header
        headerTwo.add(numberOfTweetsTwo);
        headerTwo.add(numberOfTweetsTwoField);
        headerTwo.add(userTwo);
        headerTwo.add(userTwoField);

        //Left
        bodyTwoLeft.add(xOrdinateTwo);
        bodyTwoLeft.add(xOrdinateTwoField);
        bodyTwoLeft.add(yOrdinateTwo);
        bodyTwoLeft.add(yOrdinateTwoField);

        //Right
    }

    private static void configureRobotTwoLabels() {
        //Twitter Information
        numberOfTweetsTwo = new JLabel("Number of Tweets");
        userTwo = new JLabel("Twitter ScreenName");

        //Body
        xOrdinateTwo = new JLabel("X-Ordinate");
        yOrdinateTwo = new JLabel("Y-Ordinate");
    }

    private static void configureBorders() {
        headerOne.setBorder(BorderFactory.createTitledBorder("Tweet Information"));
        headerTwo.setBorder(BorderFactory.createTitledBorder("Tweet Information"));
        robotOne.setBorder(BorderFactory.createTitledBorder("TwitterRobot 1"));
        robotTwo.setBorder(BorderFactory.createTitledBorder("TwitterRobot 2"));
        bodyOneContainer.setBorder(BorderFactory.createTitledBorder("Values"));
        bodyTwoContainer.setBorder(BorderFactory.createTitledBorder("Values"));
        footer.setBorder(BorderFactory.createTitledBorder("Robocode Battle Information"));
    }

    private static void configureRobotOneFields() {
        numberOfTweetsOneField = new JTextField("0", 10);
        userOneField = new JTextField("N/A", 10);
        xOrdinateOneField = new JTextField(10);
        yOrdinateOneField = new JTextField(10);
        firePowerOneField = new JTextField(10);
        moveUpOneField = new JTextField(10);
        moveRightOneField = new JTextField(10);
        moveDownOneField = new JTextField(10);
        moveLeftOneField = new JTextField(10);
        rotateOneField = new JTextField(10);
        rotateDirectionOneField = new JTextField(10);
        rotateGunOneField = new JTextField(10);
        rotateGunDirectionOneField = new JTextField(10);
        currentTweetOneField = new JTextField(50);
        rotateScannerOneField = new JTextField(10);
        rotateScannerDirectionOneField = new JTextField(10);
    }
    
    private static void configureRobotTwoFields() {
        numberOfTweetsTwoField = new JTextField("0", 10);
        userTwoField = new JTextField("N/A", 10);
        xOrdinateTwoField = new JTextField(10);
        yOrdinateTwoField = new JTextField(10);
    }

    private static JPanel createContentPane()  {
        //Panels
        configureMainPanels();
        configureRobotTwoPanels();
        configureRobotOnePanels();

        //Labels
        configureRobotOneLabels();
        configureRobotTwoLabels();
        configureFooterLabels();

        //Fields
        configureRobotOneFields();
        configureRobotTwoFields();
        configureFooterFields();

        //Borders
        configureBorders();

        //Add Content
        addRobotOneLabelsAndFields();
        addRobotTwoLabelsAndFields();
        addFooterLabelsAndFields();

        //Build GUI Structure
        GUI.add(robotOne, BorderLayout.LINE_START);
        GUI.add(robotTwo, BorderLayout.LINE_END);
        GUI.add(footer, BorderLayout.PAGE_END);

        robotOne.add(headerOne, BorderLayout.PAGE_START);
        robotOne.add(bodyOneContainer, BorderLayout.CENTER);
        robotTwo.add(headerTwo, BorderLayout.PAGE_START);
        robotTwo.add(bodyTwoContainer, BorderLayout.CENTER);
        bodyOneContainer.add(bodyOneLeft, BorderLayout.LINE_START);
        bodyOneContainer.add(bodyOneRight, BorderLayout.LINE_END);
        bodyTwoContainer.add(bodyTwoLeft, BorderLayout.LINE_START);
        bodyTwoContainer.add(bodyTwoRight, BorderLayout.LINE_END);

        //Make It Visible & Return
        GUI.setOpaque(true);
        return GUI;
    }

    private static void createAndShowGUI() {
        //Set Overall GUI JFrame Settings
        frame.setContentPane(createContentPane());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void setXOrdinateOne(double x) {
        SwingUtilities.invokeLater(() -> xOrdinateOneField.setText(String.valueOf(Math.round(x * 100.0) / 100.0)));
    }

    public static void setXOrdinateTwo(double x) {
        SwingUtilities.invokeLater(() -> xOrdinateTwoField.setText(String.valueOf(Math.round(x * 100.0) / 100.0)));
    }

    public static void setYOrdinateOne(double y) {
        SwingUtilities.invokeLater(() -> yOrdinateOneField.setText(String.valueOf(Math.round(y * 100.0) / 100.0)));
    }

    public static void setYOrdinateTwo(double y) {
        SwingUtilities.invokeLater(() -> yOrdinateTwoField.setText(String.valueOf(Math.round(y * 100.0) / 100.0)));
    }

    public static void setNumberOfTweetsOne(int number) {
        SwingUtilities.invokeLater(() -> numberOfTweetsOneField.setText(String.valueOf(number)));
    }

    public static void setNumberOfTweetsTwo(int number) {
        SwingUtilities.invokeLater(() -> numberOfTweetsTwoField.setText(String.valueOf(number)));
    }

    public static void setUserOne(String name) {
        SwingUtilities.invokeLater(() -> userOneField.setText(name));
    }

    public static void setRoundNumber(int round) {
        SwingUtilities.invokeLater(() -> roundNumberField.setText(String.valueOf(round)));
    }

    public static void setFirePowerOne(double power) {
        SwingUtilities.invokeLater(() -> firePowerOneField.setText(String.valueOf(power)));
    }

    public static void setMoveUpOne(int up) {
        SwingUtilities.invokeLater(() -> moveUpOneField.setText(String.valueOf(up)));
    }

    public static void setMoveRightOne(int right) {
        SwingUtilities.invokeLater(() -> moveRightOneField.setText(String.valueOf(right)));
    }

    public static void setMoveDownOne(int down) {
        SwingUtilities.invokeLater(() -> moveDownOneField.setText(String.valueOf(down)));
    }

    public static void setMoveLeftOne(int left) {
        SwingUtilities.invokeLater(() -> moveLeftOneField.setText(String.valueOf(left)));
    }

    public static void setRotateOne(int rotate) {
        SwingUtilities.invokeLater(() -> rotateOneField.setText(String.valueOf(rotate)));
    }

    public static void setRotateDirectionOne(int direction) {
        SwingUtilities.invokeLater(() -> {
            if (direction == 1) {
                rotateDirectionOneField.setText("Clockwise");
            } else if (direction == -1) {
                rotateDirectionOneField.setText("Anti-Clockwise");
            } else {
                rotateDirectionOneField.setText("Error: " + direction);
            }
        });
    }

    public static void setRotateGunOne(int rotate) {
        SwingUtilities.invokeLater(() -> rotateGunOneField.setText(String.valueOf(rotate)));
    }

    public static void setRotateGunDirectionOne(int direction) {
        SwingUtilities.invokeLater(() -> {
            if (direction == 1) {
                rotateGunDirectionOneField.setText("Clockwise");
            } else if (direction == -1) {
                rotateGunDirectionOneField.setText("Anti-Clockwise");
            } else {
                rotateGunDirectionOneField.setText("Error: " + direction);
            }
        });
    }

    public static void setCurrentTweetOne(String tweet) {
        SwingUtilities.invokeLater(() -> currentTweetOneField.setText(tweet));
    }

    public static void setRotateScannerOne(int rotate) {
        SwingUtilities.invokeLater(() -> rotateScannerOneField.setText(String.valueOf(rotate)));
    }

    public static void setRotateScannerDirectionOne(int direction) {
        SwingUtilities.invokeLater(() -> {
            if (direction == 1) {
                rotateScannerDirectionOneField.setText("Clockwise");
            } else if (direction == -1) {
                rotateScannerDirectionOneField.setText("Anti-Clockwise");
            } else {
                rotateScannerDirectionOneField.setText("Error: " + direction);
            }
        });
    }

    public static void setTurnNumber(int turn) {
        SwingUtilities.invokeLater(() -> turnNumberField.setText(String.valueOf(turn)));
    }

    public static void setSkippedTurnsOne(int skipped) {

    }

    public void stopObserving() {
        GUI.setVisible(false);
        try {
            frame.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        RobotObserver ro = new RobotObserver();
        ro.createContentPane();
        ro.createAndShowGUI();
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
