package test;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;

public class RobotJDK6 extends AdvancedRobot {
    public void run() {
        ahead(100);
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        fire(1);
    }
}
