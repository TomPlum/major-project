package controller;

import robocode.AdvancedRobot;

import robocode.*;

import java.awt.*;
import java.util.ArrayList;

public class TestRobot extends AdvancedRobot {
    boolean movingForward;

    /**
     * run: Crazy's main run function
     */
    public void run() {
        // Set colors
        setBodyColor(new Color(0, 200, 0));
        setGunColor(new Color(0, 150, 50));
        setRadarColor(new Color(0, 100, 100));
        setBulletColor(new Color(255, 255, 100));
        setScanColor(new Color(255, 200, 200));

        // Loop forever
        while (true) {
            setAhead(40000);
            movingForward = true;
            setTurnRight(90);
            waitFor(new TurnCompleteCondition(this));
            setTurnLeft(180);
            waitFor(new TurnCompleteCondition(this));
            setTurnRight(180);
            waitFor(new TurnCompleteCondition(this));
        }
    }

    /**
     * onHitWall:  Handle collision with wall.
     */
    public void onHitWall(HitWallEvent e) {
        reverseDirection();
    }

    /**
     * reverseDirection:  Switch from ahead to back & vice versa
     */
    public void reverseDirection() {
        if (movingForward) {
            setBack(40000);
            movingForward = false;
        } else {
            setAhead(40000);
            movingForward = true;
        }
    }

    /**
     * onScannedRobot:  Fire!
     */
    public void onScannedRobot(ScannedRobotEvent e) {
        fire(1);
    }

    /**
     * onHitRobot:  Back up!
     */
    public void onHitRobot(HitRobotEvent e) {
        if (e.isMyFault()) {
            reverseDirection();
        }
    }
}
