package twitter;

import com.vdurmont.emoji.*;

public class TweetSanitiser {
    private String tweet;

    public TweetSanitiser(String tweet) {
        setTweet(tweet);
    }

    /**
     * Strips the Tweet of all white-spaces
     */
    public void removeWhiteSpaces() {
        if (tweet != null) {
            // \s is space characters (space or tab). Need to escape with \\s for regex engine
            tweet = tweet.replaceAll("\\s","");
        }
    }

    /**
     * Strips the Tweet of all emoji's
     */
    public void removeEmoji() {
        EmojiParser.removeAllEmojis(tweet);
    }

    /**
     * Strips the Tweet of all line breaks.
     */
    public void removeLineBreaks() {
        tweet = tweet.replaceAll("\\r|\\n", "");
    }

    public String extractLetters() {
        StringBuilder cleanTweet = new StringBuilder();
        removeLineBreaks();
        removeWhiteSpaces();
        removeEmoji();
        for (int i = 0; i < tweet.length(); i++) {
            if (Character.isLetter(tweet.charAt(i))) {
                cleanTweet.append(tweet.charAt(i));
            }
        }
        return cleanTweet.toString();
    }

    String getTweet() {
        return tweet;
    }

    private void setTweet(String tweet) {
        this.tweet = tweet;
    }
}
