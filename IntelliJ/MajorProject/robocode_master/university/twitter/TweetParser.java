package university.twitter;

import org.bson.Document;

import java.text.DecimalFormat;
import java.util.Objects;

public class TweetParser {
    private String twitterUser;
    private TweetReader tr;
    private TweetSanitiser ts;
    private String[] alphabet = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    private DecimalFormat dp1 = new DecimalFormat(".#");
    private Document tweet;
    private String tweetString;


    public TweetParser(Document tweet) {
        setTweet(tweet);
        ts = new TweetSanitiser(getTweet().get("text").toString());
        setTweetString(ts.extractLetters());
        setTwitterUser(tweet.get("user").toString());
    }

    public TweetParser() {

    }

    public int getValue(String letter) {
        for (int i = 0; i < alphabet.length; i++) {
            if (Objects.equals(letter.toLowerCase(), alphabet[i])) {
                return i;
            }
        }
        return -1;
    }

    public void setTweet(Document tweet) {
        this.tweet = tweet;
    }

    public void setTweetString(String tweetString) {
        this.tweetString = tweetString;
    }

    public Document getTweet() {
        return tweet;
    }

    public String getTwitterUser() {
        return twitterUser;
    }

    public void setTwitterUser(String twitter_user) {
        this.twitterUser = twitter_user;
    }

    public TweetReader getTr() {
        return tr;
    }

    public void setTr(TweetReader tr) {
        this.tr = tr;
    }

    public TweetSanitiser getTs() {
        return ts;
    }

    public void setTs(TweetSanitiser ts) {
        this.ts = ts;
    }

    //Add Getters & Setters for each value, above method randomly switches them all from Tweet
}
