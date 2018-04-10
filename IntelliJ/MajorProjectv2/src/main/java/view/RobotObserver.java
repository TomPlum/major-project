package view;

import javax.swing.*;
import java.awt.*;

public class RobotObserver {
    private static JPanel GUI, header, robot1, robot2, footer;
    private static JLabel robot1x, robot1y;
    private static JLabel robot2x, robot2y;
    private static JTextField robot1xField, robot1yField;
    public static void startObserving() {
        javax.swing.SwingUtilities.invokeLater(RobotObserver::createAndShowGUI);
    }

    private static JPanel createContentPane()  {
        GUI = new JPanel();
        header = new JPanel();
        robot1 = new JPanel();
        robot2 = new JPanel();
        footer = new JPanel();

        //Setting LayoutManager Types
        GUI.setLayout(new BorderLayout());
        header.setLayout(new FlowLayout());
        robot1.setLayout(new FlowLayout());
        robot2.setLayout(new FlowLayout());
        footer.setLayout(new FlowLayout());

        //Set JLabels
        robot1x = new JLabel("X-Ordinate");
        robot2x = new JLabel("X-Ordinate");
        robot2y = new JLabel("Y-Ordinate");
        robot1y = new JLabel("Y-Ordinate");

        //Set Value Fields
        robot1xField = new JTextField();
        robot1yField = new JTextField();

        //Set JPanel Borders
        header.setBorder(BorderFactory.createTitledBorder("Header"));
        robot1.setBorder(BorderFactory.createTitledBorder("TwitterRobot 1"));
        robot2.setBorder(BorderFactory.createTitledBorder("TwitterRobot 2"));
        footer.setBorder(BorderFactory.createTitledBorder("Footer"));

        //Add Components To JPanels
        robot1.add(robot1x);
        robot1.add(robot1xField);
        robot1.add(robot1y);
        robot1.add(robot1yField);

        //Adding JPanel Containers To Overall GUI Container
        GUI.add(header, BorderLayout.PAGE_START);
        GUI.add(robot1, BorderLayout.LINE_START);
        GUI.add(robot2, BorderLayout.LINE_END);
        GUI.add(footer, BorderLayout.PAGE_END);

        //Make It Visible & Return
        GUI.setOpaque(true);
        return GUI;
    }

    private static void createAndShowGUI() {
        //Set Overall GUI JFrame Settings
        JFrame frame = new JFrame("TwitterRobot Observer");
        frame.setContentPane(RobotObserver.createContentPane());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void updateRobot1x(double x) {
        robot1xField.setText(String.valueOf(x));
    }

    public static void stopObserving() {

    }
}
