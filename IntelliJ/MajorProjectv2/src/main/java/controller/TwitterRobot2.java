package controller;

import robocode.*;

public class TwitterRobot2 extends AdvancedRobot {
    /**
     * Called whenever the Robot's status changes.
     * @param e Robocode StatusEvent
     */
    public void onStatus(StatusEvent e) {
        //Update Robot's Current Coordinates
        RobotStatus robotStatus = e.getStatus();
        RobotController2.setRobotX(robotStatus.getX());
        RobotController2.setRobotY(robotStatus.getY());
    }

    /**
     * The robots main event loop. This method is called every turn and contains
     * the robots main functionality for moving, turning, scanning and firing.
     */
    public void run() {
        while (true) {
            RobotController2.randomiseValues();

            ahead(RobotController2.getMOVE_UP());
            if (RobotController2.getROTATE_DIRECTION() == 1) {
                turnGunRight(RobotController2.getROTATE_GUN());
            } else {
                turnGunLeft(RobotController2.getROTATE_GUN() * -1);
            }

            RobotController2.invalidateAllValues();
            //BattleObserver.numberOfTurns++;
        }
    }

    /**
     * Called whenever the robots scanner finds another robot.
     * @param e Robocode ScannedRobotEvent
     */
    public void onScannedRobot(ScannedRobotEvent e) {
        fire(RobotController2.getFIRE_POWER());
    }

    /**
     * Called whenever the robot collides with one of the arena walls.
     * @param e Robocode HitWallEvent
     */
    public void onHitWall(HitWallEvent e) {
        back(RobotController2.getMOVE_DOWN());
    }

    /**
     * Called whenever the robot dies.
     * @param e Robocode DeathEvent
     */
    public void onDeath(DeathEvent e) {
        //Count time up until, then record time taken to die. Save in DB.
    }
}
