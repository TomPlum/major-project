package twitter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class TweetSerialiser implements Serializable {
    private TweetReader tweetReader = new TweetReader();
    private ArrayList<String> takenUsers = new ArrayList<>();
    private String USER;


    public void serialiseTweets() {
        setRandomUser();
        System.out.println("Downloading Tweets From " + USER + "...");
        ArrayList<String> tweets = tweetReader.getAllTweetTextByUser(USER);

        try {
            //C:/Users/thoma/Dropbox%20(University)/Year%203/CPU6001%20-%20Major%20Project%20(Amanda%20&%20Louise)/IntelliJ/MajorProjectv2/robocode_master/robots/
            String path = TweetSerialiser.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            FileOutputStream fileOut = new FileOutputStream(path + "TwitterRobotData.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(tweets);
            out.close();
            fileOut.close();
            System.out.println("Written TwitterRobotData.ser");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setRandomUser() {
        //Get all usernames from cluster
        ArrayList<String> users = tweetReader.getAllUsernames();

        //Generate Random Number (From 0 - users.size())
        Random rand = new Random();
        Integer n = rand.nextInt(users.size());

        //Get random user string and set RobotController2 USER
        String user = users.get(n);

        //Check If User Already Taken
        boolean foundAvailableUser = false;
        while(!foundAvailableUser) {
            if (!takenUsers.contains(user)) {
                foundAvailableUser = true;
            } else {
                //Regenerate Random Number
                n = rand.nextInt(users.size());

                //Try Again
                user = users.get(n);
            }
        }

        setUSER(user);
        takenUsers.add(user);
        System.out.println("User set to " + user);
    }

    public void setUSER(String USER) {
        this.USER = USER;
    }
}
