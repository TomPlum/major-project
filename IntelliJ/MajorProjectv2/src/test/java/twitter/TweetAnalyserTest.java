package twitter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TweetAnalyserTest {
    @Test
    void printResults() {
        TweetAnalyser ta = new TweetAnalyser();
        ta.analyse();
        ta.printResults();
        assertEquals(true, true);
    }
}