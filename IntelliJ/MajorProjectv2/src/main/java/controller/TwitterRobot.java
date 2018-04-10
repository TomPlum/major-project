package controller;

import robocode.*;
import view.RobotObserver;

import java.awt.*;

/**
 * ---------------------------------------------------------------------------------------------------
 * This robot's actions are entirely dependent on the character values from Twitter Tweets stored in a
 * MongoDB Atlas Cluster. It is controller via the RobotController that parses Tweets from the
 * TweetParser. A Twitter user is chosen at random to represent it and therefore choose its actions.
 * ---------------------------------------------------------------------------------------------------
 * @author Thomas Plumpton
 * @version 1.0.0
 */
@SuppressWarnings("unused")
public class TwitterRobot extends AdvancedRobot {
    /**
     * Called whenever the Robot's status changes.
     * @param e Robocode StatusEvent
     */
    public void onStatus(StatusEvent e) {
        //Update Robot's Current Coordinates
        RobotStatus robotStatus = e.getStatus();
        RobotController.setRobotX(robotStatus.getX());
        RobotController.setRobotY(robotStatus.getY());

        //Update RobotObserver GUI
        RobotObserver.updateRobot1x(robotStatus.getX());
    }

    /**
     * The robots main event loop. This method is called every turn and contains
     * the robots main functionality for moving, turning, scanning and firing.
     */
    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        //Set Twitter Palette Colours
        setBodyColor(new Color(29, 202, 255));
        setGunColor(new Color(0, 172, 237));
        setRadarColor(new Color(0, 132, 180));
        setBulletColor(new Color(29, 202, 255));
        setScanColor(new Color(192, 222, 237));

        //Loop Indefinitely
        while (true) {
            //Randomise Robot Values
            RobotController.randomiseValues();

            //Move Ahead
            ahead(RobotController.getMOVE_UP());

            //Rotate Robot Body
            if (RobotController.getROTATE_DIRECTION() == 1) {
                turnGunRight(RobotController.getROTATE_GUN());
            } else {
                turnGunLeft(RobotController.getROTATE_GUN() * -1);
            }

            //Invalidate All RobotController Values
            RobotController.invalidateAllValues();

            //Increment Number of Turns
            BattleObserver.numberOfTurns++;
        }
    }

    /**
     * Called whenever the robots scanner finds another robot.
     * @param e Robocode ScannedRobotEvent
     */
    public void onScannedRobot(ScannedRobotEvent e) {
        fire(RobotController.getFIRE_POWER());
    }

    /**
     * Called whenever the robot collides with one of the arena walls.
     * @param e Robocode HitWallEvent
     */
    public void onHitWall(HitWallEvent e) {
        back(RobotController.getMOVE_DOWN());
    }

    /**
     * Called whenever the robot dies.
     * @param e Robocode DeathEvent
     */
    public void onDeath(DeathEvent e) {
        //Count time up until, then record time taken to die. Save in DB.
    }
}
