package university.twitter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TweetAnalyserTest {
    TweetAnalyser ta = new TweetAnalyser();

    @Test
    private void testAnalyse() {
        ta.analyse();
        ta.printResults();
    }
}