package controller;

import robocode.*;

public class TwitterRobot extends AdvancedRobot {
    private RobotController rc = new RobotController();

    public void onStatus(StatusEvent e) {
        //Update Robot's Current Coordinates
        RobotStatus robotStatus = e.getStatus();
        rc.setRobotX(robotStatus.getX());
        rc.setRobotY(robotStatus.getY());
    }

    public void run() {
        while (true) {
            rc.randomiseValues();

            ahead(rc.getMOVE_UP());
            if (rc.getROTATE_DIRECTION() == 1) {
                turnGunRight(rc.getROTATE_GUN());
            } else {
                turnGunLeft(rc.getROTATE_GUN() * -1);
            }

            rc.invalidateAllValues();
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        fire(rc.getFIRE_POWER());
    }

    public void onHitWall(HitWallEvent e) {
        back(rc.getMOVE_DOWN());
    }
}
