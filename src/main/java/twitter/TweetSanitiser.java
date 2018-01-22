package twitter;

import com.vdurmont.emoji.*;

class TweetSanitiser {
    private String tweet;

    TweetSanitiser(String tweet) {
        setTweet(tweet);
    }

    TweetSanitiser() {

    }

    void removeWhiteSpaces() {
        if (tweet != null) {
            // \s is space characters (space or tab). Need to escape with \\s for regex engine
            tweet = tweet.replaceAll("\\s","");
        }
    }

    void removeEmoji() {
        EmojiParser.removeAllEmojis(tweet);
    }

    void removeLineBreaks() {
        tweet = tweet.replaceAll("\\r|\\n", "");
    }

    public String extractLetters() {
        String cleanTweet = "";
        removeLineBreaks();
        removeWhiteSpaces();
        removeEmoji();
        for (int i = 0; i < tweet.length(); i++) {
            if (Character.isLetter(tweet.charAt(i))) {
                cleanTweet += tweet.charAt(i);
            }
        }
        return cleanTweet;
    }

    String getTweet() {
        return tweet;
    }

    private void setTweet(String tweet) {
        this.tweet = tweet;
    }
}
