package view;

import javax.swing.*;
import java.awt.*;

public class RobotObserver {
    //JPanel's for the overall GUI Layout
    private static JPanel GUI, header, robotOne, robotTwo, footer, tweetInfoOne, tweetInfoTwo;

    //Footer: JLabels for the Robocode Battle Information
    private static JLabel roundNumber, turnNumber;

    //Body: JLabels for Robot One's Information
    private static JLabel xOrdinateOne, yOrdinateOne;

    //Body: JLabels for Robot Two's Information
    private static JLabel xOrdinateTwo, yOrdinateTwo;

    //Header: JLabels for Robot One's Tweet Information
    private static JLabel numberOfTweetsOne, userOne;

    //Header: JLabels for Robot Two's Tweet Information
    private static JLabel numberOfTweetsTwo, userTwo;

    //Header: JTextFields for Robot One's Tweet Information
    private volatile static JTextField numberOfTweetsOneField, userOneField;

    //Header: JTextFields for Robot Two's Twitter Information
    private static JTextField numberOfTweetsTwoField, userTwoField;

    //Footer: JTextFields for the Robocode Battle Information
    private static JTextField roundNumberField, turnNumberField;

    //Body: JTextFields for Robot One's Information
    private static JTextField xOrdinateOneField, yOrdinateOneField;

    //Body: JTextFields for Robot Two's Information
    private static JTextField xOrdinateTwoField, yOrdinateTwoField;

    public synchronized static void startObserving() {
        javax.swing.SwingUtilities.invokeLater(RobotObserver::createAndShowGUI);
    }

    private synchronized static JPanel createContentPane()  {
        GUI = new JPanel();
        header = new JPanel();
        robotOne = new JPanel();
        robotTwo = new JPanel();
        footer = new JPanel();
        tweetInfoOne = new JPanel();
        tweetInfoTwo = new JPanel();

        //Setting LayoutManager Types
        GUI.setLayout(new BorderLayout());
        header.setLayout(new BorderLayout());
        robotOne.setLayout(new FlowLayout());
        robotTwo.setLayout(new FlowLayout());
        footer.setLayout(new FlowLayout());
        tweetInfoOne.setLayout(new FlowLayout());
        tweetInfoTwo.setLayout(new FlowLayout());

        //Set JLabels
        xOrdinateOne = new JLabel("X-Ordinate");
        xOrdinateTwo = new JLabel("X-Ordinate");
        yOrdinateTwo = new JLabel("Y-Ordinate");
        yOrdinateOne = new JLabel("Y-Ordinate");

        //Robocode Battle Information
        roundNumber = new JLabel("Round Number");
        turnNumber = new JLabel("Turn Number");

        //Twitter Information
        numberOfTweetsOne = new JLabel("Number of Tweets");
        numberOfTweetsTwo = new JLabel("Number of Tweets");
        userOne = new JLabel("Twitter ScreenName");
        userTwo = new JLabel("Twitter ScreenName");

        //Set Value Fields
        xOrdinateOneField = new JTextField(10);
        yOrdinateOneField = new JTextField(10);
        xOrdinateTwoField = new JTextField(10);
        yOrdinateTwoField = new JTextField(10);

        numberOfTweetsOneField = new JTextField("0", 10);
        numberOfTweetsTwoField = new JTextField("0", 10);
        userOneField = new JTextField("N/A", 10);
        userTwoField = new JTextField("N/A", 10);

        roundNumberField = new JTextField("0", 10);
        turnNumberField = new JTextField("0", 10);

        //Set JPanel Borders
        header.setBorder(BorderFactory.createTitledBorder("Tweet Information"));
        robotOne.setBorder(BorderFactory.createTitledBorder("TwitterRobot 1"));
        robotTwo.setBorder(BorderFactory.createTitledBorder("TwitterRobot 2"));
        footer.setBorder(BorderFactory.createTitledBorder("Robocode Battle Information"));

        //Add Components To JPanels
        robotOne.add(xOrdinateOne);
        robotOne.add(xOrdinateOneField);
        robotOne.add(yOrdinateOne);
        robotOne.add(yOrdinateOneField);

        robotTwo.add(xOrdinateTwo);
        robotTwo.add(xOrdinateTwoField);
        robotTwo.add(yOrdinateTwo);
        robotTwo.add(yOrdinateTwoField);

        tweetInfoOne.add(numberOfTweetsOne);
        tweetInfoOne.add(numberOfTweetsOneField);
        tweetInfoTwo.add(numberOfTweetsTwo);
        tweetInfoTwo.add(numberOfTweetsTwoField);
        tweetInfoOne.add(userOne);
        tweetInfoOne.add(userOneField);
        tweetInfoTwo.add(userTwo);
        tweetInfoTwo.add(userTwoField);

        footer.add(roundNumber);
        footer.add(roundNumberField);
        footer.add(turnNumber);
        footer.add(turnNumberField);

        //Adding JPanel Containers To Overall GUI Container
        GUI.add(header, BorderLayout.PAGE_START);
        GUI.add(robotOne, BorderLayout.LINE_START);
        GUI.add(robotTwo, BorderLayout.LINE_END);
        GUI.add(footer, BorderLayout.PAGE_END);
        header.add(tweetInfoOne, BorderLayout.LINE_START);
        header.add(tweetInfoTwo, BorderLayout.LINE_END);

        //Make It Visible & Return
        GUI.setOpaque(true);
        return GUI;
    }

    private synchronized static void createAndShowGUI() {
        //Set Overall GUI JFrame Settings
        JFrame frame = new JFrame("TwitterRobot Observer");
        frame.setContentPane(RobotObserver.createContentPane());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public synchronized static void setXOrdinateOne(double x) {
        System.out.println("Setting RobotOne X-Ordinate: " + x);
        xOrdinateOneField.setText(String.valueOf(x));
    }

    public synchronized static void setyOrdinateOne(double y) {
        System.out.println("Setting RobotOne Y-Ordinate: " + y);
        yOrdinateOneField.setText(String.valueOf(y));
    }

    public synchronized static void setNumberOfTweetsOne(int number) {
        System.out.println("Setting Number of Tweets One: " + number);
        numberOfTweetsOneField.setText(String.valueOf(number));
        //refresh(numberOfTweetsOneField);
    }

    public synchronized static void setUserOne(String name) {
        userOneField.setText(name);
        refresh(userOneField);
    }

    public synchronized static void setRoundNumber(int round) {
        roundNumberField.setText(String.valueOf(round));
        refresh(roundNumberField);
    }

    public synchronized static void setTurnNumber(int turn) {
        turnNumberField.setText(String.valueOf(turn));
        refresh(turnNumberField);
    }

    public synchronized static void setSkippedTurnsOne(int skipped) {

    }

    private synchronized static void refresh(JTextField field) {
        field.setVisible(true);
        field.repaint();
        field.validate();
    }

    public synchronized static void stopObserving() {

    }

    public synchronized static void main(String[] args) {
        createContentPane();
        createAndShowGUI();
        setNumberOfTweetsOne(3200);
    }
}
