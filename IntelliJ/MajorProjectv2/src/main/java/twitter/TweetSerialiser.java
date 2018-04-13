package twitter;

import org.bson.Document;
import view.RobotObserver;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class TweetSerialiser implements Serializable {
    private TweetReader tweetReader = new TweetReader();
    private ArrayList<String> takenUsers = new ArrayList<>();
    private String USER;


    public void serialiseTweets(int robot) throws IOException {
        setRandomUser();
        System.out.println("Downloading Tweets From " + USER + "...");
        ArrayList<Document> tweets = tweetReader.getTweetsByUser(USER);

        try {
            //C:/Users/thoma/Dropbox%20(University)/Year%203/CPU6001%20-%20Major%20Project%20(Amanda%20&%20Louise)/IntelliJ/MajorProjectv2/robocode_master/robots/
            //String path = TweetSerialiser.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            String path = "./robots/controller/";
            FileOutputStream fileOut = new FileOutputStream(path + "TwitterRobotData" + robot + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(tweets);
            out.close();
            fileOut.close();
            System.out.println("Written Tweets To " + path + "TwitterRobotData.ser");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Document> readTweets(int robot) {
        ArrayList<Document> tweets = new ArrayList<>();
        try {
            FileInputStream fileIn = new FileInputStream("./robots/controller/TwitterRobotData" + robot + ".ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            tweets = (ArrayList<Document>) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("Successfully Read " + tweets.size() + " Tweets From " + USER);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Tweets Not Found");
            c.printStackTrace();
        }
        return tweets;
    }

    private void setRandomUser() {
        //Get all usernames from cluster
        ArrayList<String> users = tweetReader.getAllScreenames();

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
                System.out.println(user + " already taken by the other robot. Picking another...");

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

    private void setUSER(String USER) {
        this.USER = USER;
    }

    public TweetReader getTweetReader() {
        return tweetReader;
    }
}
