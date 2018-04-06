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
            output = new FileOutputStream("TwitterRobot.properties");

            //Set Properties
            prop.setProperty("robot.description", "This robot's actions are entirely dependent on the character values from Twitter Tweets stored in a MongoDB Atlas Cluster. It is controller via the RobotController that parses Tweets from the TweetParser. A Twitter user is chosen at random to represent it and therefore choose its actions.");
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
