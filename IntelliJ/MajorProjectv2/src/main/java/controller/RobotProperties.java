package controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class RobotProperties {
    public static void main(String[] args) {
        Properties prop = new Properties();
        OutputStream output = null;

        try {
            output = new FileOutputStream("C:/Users/thoma/Dropbox (University)/Year 3/CPU6001 - Major Project (Amanda & Louise)/IntelliJ/MajorProjectv2/robocode_master/robots/controller/TwitterRobot.properties");

            //Set Properties
            prop.setProperty("robot.description", "Controlled by Twitter");
            prop.setProperty("robot.webpage", "https://major-project.tomplumpton.me");
            prop.setProperty("robocode.version", "1.9.3.2");
            prop.setProperty("robot.author.name", "Thomas Plumpton");
            prop.setProperty("robot.classname", "TwitterRobot");
            prop.setProperty("robot.name", "TwitterRobot");

            //Save Properties
            prop.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                    System.out.println("Successfully written TwitterRobot.properties");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
