package controller;

import twitter.TweetReader;
import java.util.ArrayList;

public class GameConfigurer {
    private int BATTLEFIELD_W;
    private int BATTLEFIELD_X;

    private TweetReader tr = new TweetReader();
    private ArrayList<String> users = tr.getAllUsernames();

    public ArrayList<String> getAvailableUsers() {
        return users;
    }

    public boolean userAvailable(String user) {
        return users.contains(user);
    }
}
