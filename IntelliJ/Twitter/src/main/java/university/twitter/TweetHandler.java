package university.twitter;

import connection.MongoConnection;
import com.mongodb.DuplicateKeyException;
import org.bson.Document;
import org.bson.types.ObjectId;
import twitter4j.*;
import twitter4j.api.UsersResources;
import twitter4j.conf.*;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class TweetHandler {
    private String username;
    private ArrayList<Status> statuses;
    private MongoConnection conn = new MongoConnection("university/twitter", "tweets");
    private int numberOfTweets;

    /**
     * Connects to the Twitter API, Downloads Status Objects, Creates Document JSON, Commits to MongoDB Cluster.
     * @param screenName Twitter Screen Name
     * @param num Quantity to Download (Max 3200)
     */
    public TweetHandler(String screenName, int num) {
        setUsername(screenName);
        setNumberOfTweets(num);
    }

    /**
     * Formats time into minutes and seconds from a given millisecond duration.
     * @param elapsed Elapsed Time in Milliseconds
     * @return Formatted String in Xm Ys
     */
    private String calculateTime(long elapsed) {
        int minutes = Math.round(elapsed / (60 * 1000F));
        int seconds = Math.round((elapsed % 60000) / 1000);
        return "Operation Completed In " + minutes + "m " + seconds + "s.";
    }

    /**
     *
     * @param screenName Twitter ScreenName to search for
     * @return true if exists, false if does not.
     * @throws TwitterException When the Twitter Service or Network is unavailable.
     */
    private boolean userExists(String screenName) throws TwitterException {
        String username = "";
        try {
            ConfigurationBuilder cb = getConfigurationBuilder();
            Twitter twitter = new TwitterFactory(cb.build()).getInstance();
            UsersResources ur = twitter.users();
            User u = ur.showUser(screenName);
            username = u.getName();
            System.out.println("User '" + u.getName() + "' found.");
            System.out.println("They have " + u.getFollowersCount() + " followers.");
        } catch (TwitterException e) {
            e.printStackTrace();
        }

        return username.length() > 0;
    }

    /**
     * Downloads Twitter User Timeline by ScreenName. Created Document Objects, Committs them to MongoDB Cluster,
     * Closes the Connection. Operations happen asynchronously.
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void handleTweets() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            try {
                userExists(username);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            System.out.println("Downloading Twitter Status Objects From " + username + "...");

            int pageno = 1; //Must be positive integer
            ConfigurationBuilder cb = getConfigurationBuilder();
            Twitter twitter = new TwitterFactory(cb.build()).getInstance();
            boolean notExceededRateLimit = true;

            while (notExceededRateLimit) {
                try {
                    Paging page = new Paging(pageno++, numberOfTweets);
                    //getUserTimeline() gets 20 most recent
                    statuses.addAll(twitter.getUserTimeline(username, page));
                    System.out.println("Collecting Statuses: Page " + pageno + " - " + statuses.size() + "/" + numberOfTweets);
                    if (statuses.size() == numberOfTweets) {
                        break;
                    }
                } catch (TwitterException e) {
                    RateLimitStatus rls = e.getRateLimitStatus();
                    System.out.println("Twitter Rate Limit Exceeded!");
                    int s = rls.getSecondsUntilReset();
                    System.out.println("Rate Limit Time Until Reset: " + s / 60 + " m " + s % 60 + "s.");
                    System.out.println("Rate Limit: " + rls.getLimit());
                    System.out.println("Remaining: " + rls.getRemaining());
                    notExceededRateLimit = false;
                }
            }
            System.out.println("Successfully Downloaded " + statuses.size() + " Statuses.");
            return statuses;
        }).thenApply((ArrayList<Status> statuses) -> {
            ArrayList<Document> tweetJson = new ArrayList<>(3200);
            statuses.forEach((Status status) -> {
                Document json = new Document();
                try {
                    json.append("_id", new ObjectId());
                    json.append("tweet_id", status.getId());
                    json.append("created_at", status.getCreatedAt());
                    json.append("text", status.getText());
                    json.append("user", status.getUser().getName());
                    //json.put("country", status.getPlace());
                    json.append("language", status.getLang());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (DuplicateKeyException e) {
                    System.out.println("Duplicate Twitter Key!");
                }
                tweetJson.add(json);
            });
            return tweetJson;
        }).thenApply((ArrayList<Document> tweets) -> {
            //System.out.println(tweets);
            int count = 0;
            int size = tweets.size();
            for (Document obj : tweets) {
                conn.insertDocument(obj);
                count++;
                System.out.println("Committed Tweet " + count + " of " + size + " @ " + obj.get("_id"));
            }
            return tweets;
        }).thenRun(new Thread(() -> {
            //conn.printCollectionStats();
            conn.disconnect();
            long elapsed = System.currentTimeMillis() - start;
            System.out.println(calculateTime(elapsed));
        }));

        future.get();
    }

    /**
     * OAuth Keys - Twitter Application
     */
    private ConfigurationBuilder getConfigurationBuilder() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey("dMy160Q3loDNlgtcm5ug4akDW");
        cb.setOAuthConsumerSecret("QLMn6ZwxygCzkuAQ2Zpxbm9d4NsQkYubscgntYBgOpLBx5h68f");
        cb.setOAuthAccessToken("569379136-Bt5ZJGog8xjxnhL9m24JacPqvg8IaYZHHGZ4ftRL");
        cb.setOAuthAccessTokenSecret("1gETn9RdTo30uWlfyPhANyve1wfUeST9zZ5UwIkXwafPP");
        System.out.println("OAuth Token Authenticated - Connected To Twitter");
        return cb;
    }

    /**
     * Sets the username of the Twitter user in which to download Tweets from.
     * @param username Twitter ScreenName
     */
    private void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the number of Tweets to download from the provided Twitter ScreenName.
     * @param numberOfTweets Quantity of Tweets (Max 3200)
     */
    private void setNumberOfTweets(int numberOfTweets) {
        if (numberOfTweets <= 3200) {
            this.numberOfTweets = numberOfTweets;
            statuses = new ArrayList<>(numberOfTweets);
        } else {
            System.out.println("You cannot download more than 3200 Tweets per API call.");
        }
    }
}